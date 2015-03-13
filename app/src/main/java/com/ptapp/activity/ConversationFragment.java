package com.ptapp.activity;


import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ptapp.adapter.MessageAdapter;
import com.ptapp.crypto.PgpEngine;
import com.ptapp.entities.Account;
import com.ptapp.entities.Contact;
import com.ptapp.entities.Conversation;
import com.ptapp.entities.Downloadable;
import com.ptapp.entities.DownloadableFile;
import com.ptapp.entities.DownloadablePlaceholder;
import com.ptapp.entities.Message;
import com.ptapp.entities.MucOptions;
import com.ptapp.entities.Presences;
import com.ptapp.provider.PTAppContract;
import com.ptapp.provider.PTAppDatabase;
import com.ptapp.app.R;
import com.ptapp.service.XmppConnectionService;
import com.ptapp.utils.CommonConstants;
import com.ptapp.utils.SharedPrefUtil;
import com.ptapp.xmpp.jid.InvalidJidException;
import com.ptapp.xmpp.jid.Jid;

import net.java.otr4j.session.SessionStatus;

import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.ptapp.utils.LogUtils.LOGW;
import static com.ptapp.utils.LogUtils.makeLogTag;


public class ConversationFragment extends Fragment {

    private static final String TAG = makeLogTag(ConversationFragment.class);
    protected Conversation conversation;
    private View.OnClickListener leaveMuc = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            activity.endConversation(conversation);
        }
    };
    private View.OnClickListener joinMuc = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            activity.xmppConnectionService.joinMuc(conversation);
        }
    };
    private View.OnClickListener enterPassword = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            MucOptions muc = conversation.getMucOptions();
            String password = muc.getPassword();
            if (password == null) {
                password = "";
            }
            activity.quickPasswordEdit(password, new XmppActivity.OnValueEdited() {

                @Override
                public void onValueEdited(String value) {
                    activity.xmppConnectionService.providePasswordForMuc(
                            conversation, value);
                }
            });
        }
    };
    protected ListView messagesView;
    final protected List<Message> messageList = new ArrayList<>();
    protected MessageAdapter messageListAdapter;
    protected Contact contact;
    private EditMessage mEditMessage;
    private ImageButton mSendButton;
    private RelativeLayout snackbar;
    private TextView snackbarMessage;
    private TextView snackbarAction;
    private boolean messagesLoaded = true;
    private Toast messageLoaderToast;
    /*Button flag_f, flag_m;*/
    private HashMap<String, Jid> flagsWithJids = new HashMap<>();
    private ArrayList<Jid> jIds = new ArrayList<>();
    ArrayList<String> selectedFlags = new ArrayList<>();
    /*RelativeLayout sendToBar;*/
    LinearLayout nameBar;

    private AbsListView.OnScrollListener mOnScrollListener = new AbsListView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            synchronized (ConversationFragment.this.messageList) {
                if (firstVisibleItem < 5 && messagesLoaded && messageList.size() > 0) {
                    long timestamp = ConversationFragment.this.messageList.get(0).getTimeSent();
                    messagesLoaded = false;
                    activity.xmppConnectionService.loadMoreMessages(conversation, timestamp, new XmppConnectionService.OnMoreMessagesLoaded() {
                        @Override
                        public void onMoreMessagesLoaded(final int count, Conversation conversation) {
                            if (ConversationFragment.this.conversation != conversation) {
                                return;
                            }
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    final int oldPosition = messagesView.getFirstVisiblePosition();
                                    View v = messagesView.getChildAt(0);
                                    final int pxOffset = (v == null) ? 0 : v.getTop();
                                    ConversationFragment.this.conversation.populateWithMessages(ConversationFragment.this.messageList);
                                    updateStatusMessages();
                                    messageListAdapter.notifyDataSetChanged();
                                    if (count != 0) {
                                        final int newPosition = oldPosition + count;
                                        int offset = 0;
                                        try {
                                            Message tmpMessage = messageList.get(newPosition);

                                            while (tmpMessage.wasMergedIntoPrevious()) {
                                                offset++;
                                                tmpMessage = tmpMessage.prev();
                                            }
                                        } catch (final IndexOutOfBoundsException ignored) {

                                        }
                                        messagesView.setSelectionFromTop(newPosition - offset, pxOffset);
                                        messagesLoaded = true;
                                        if (messageLoaderToast != null) {
                                            messageLoaderToast.cancel();
                                        }
                                    }
                                }
                            });
                        }

                        @Override
                        public void informUser(final int resId) {

                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (messageLoaderToast != null) {
                                        messageLoaderToast.cancel();
                                    }
                                    if (ConversationFragment.this.conversation != conversation) {
                                        return;
                                    }
                                    messageLoaderToast = Toast.makeText(activity, resId, Toast.LENGTH_LONG);
                                    messageLoaderToast.show();
                                }
                            });

                        }
                    });

                }
            }
        }
    };
    private IntentSender askForPassphraseIntent = null;
    protected View.OnClickListener clickToDecryptListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (activity.hasPgp() && askForPassphraseIntent != null) {
                try {
                    getActivity().startIntentSenderForResult(
                            askForPassphraseIntent,
                            ConversationActivity.REQUEST_DECRYPT_PGP, null, 0,
                            0, 0);
                } catch (IntentSender.SendIntentException e) {
                    //
                }
            }
        }
    };
    protected View.OnClickListener clickToVerify = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            //SCH: No need
//            activity.verifyOtrSessionDialog(conversation, v);
        }
    };
    private ConcurrentLinkedQueue<Message> mEncryptedMessages = new ConcurrentLinkedQueue<>();
    private boolean mDecryptJobRunning = false;
    private TextView.OnEditorActionListener mEditorActionListener = new TextView.OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                InputMethodManager imm = (InputMethodManager) v.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                sendMessage();
                return true;
            } else {
                return false;
            }
        }
    };
    private View.OnClickListener mSendButtonListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            sendMessage();
            XmppConnectionService.takeDbBackup(getActivity());
        }
    };

    //SCH: No need
//    private View.OnClickListener clickToMuc = new View.OnClickListener() {
//
//        @Override
//        public void onClick(View v) {
//            Intent intent = new Intent(getActivity(),
//                    ConferenceDetailsActivity.class);
//            intent.setAction(ConferenceDetailsActivity.ACTION_VIEW_MUC);
//            intent.putExtra("uuid", conversation.getUuid());
//            startActivity(intent);
//        }
//    };
    private View.OnClickListener fFlagButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            /*Jid jid = flagsWithJids.get(Message.PARENT_TYPE_F);
            if (!jIds.contains(jid)) {
                jIds.add(jid);
                selectedFlags.add(Message.PARENT_TYPE_F);
                ((LinearLayout) view.getParent()).setBackgroundColor(
                        getResources().getColor(R.color.refresh_progress_1));
            } else {
                jIds.remove(jid);
                selectedFlags.remove(Message.PARENT_TYPE_F);
                ((LinearLayout) view.getParent()).setBackgroundColor(
                        getResources().getColor(R.color.theme_primary));
            }*/
        }
    };
    private View.OnClickListener mFlagButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /*Jid jid = flagsWithJids.get(Message.PARENT_TYPE_M);
            if (!jIds.contains(jid)) {
                jIds.add(jid);
                selectedFlags.add(Message.PARENT_TYPE_M);
            } else {
                jIds.remove(jid);
                selectedFlags.remove(Message.PARENT_TYPE_M);
            }*/
        }
    };
    private ConversationActivity activity;
    private Message selectedMessage;

    private void sendMessage() {
        if (this.conversation == null) {
            return;
        }
        if (mEditMessage.getText().length() < 1) {
            if (this.conversation.getMode() == Conversation.MODE_MULTI) {
                conversation.setNextCounterpart(null);
                updateChatMsgHint();
            }
            return;
        }
        Message message = new Message(conversation, mEditMessage.getText()
                .toString(), conversation.getNextEncryption(activity
                .forceEncryption()), jIds, conversation.getStudentId());
        if (SharedPrefUtil.getPrefUserInRole(getActivity()).equals(CommonConstants.ROLE_STAFF)) {
            message.setFlags(selectedFlags);
        } else if (SharedPrefUtil.getPrefUserInRole(getActivity()).equals(CommonConstants.ROLE_PARENT)) {
            message.setFlags(null);
        }
        if (conversation.getMode() == Conversation.MODE_MULTI) {
            /*if (conversation.getNextCounterpart() != null) {
                message.setCounterpart(conversation.getNextCounterpart());
                message.setType(Message.TYPE_PRIVATE);
                conversation.setNextCounterpart(null);
            }*/
        }
        if (conversation.getNextEncryption(activity.forceEncryption()) == Message.ENCRYPTION_OTR) {
            /*sendOtrMessage(message);*/
        } else if (conversation.getNextEncryption(activity.forceEncryption()) == Message.ENCRYPTION_PGP) {
            /*sendPgpMessage(message);*/
        } else {
            sendPlainTextMessage(message);
        }
    }

    public void updateChatMsgHint() {
        if (conversation.getMode() == Conversation.MODE_MULTI && conversation.getNextCounterpart() != null) {
            this.mEditMessage.setHint(getString(
                    R.string.send_private_message_to,
                    conversation.getNextCounterpart().getResourcepart()));
        } else {
            switch (conversation.getNextEncryption(activity.forceEncryption())) {
                case Message.ENCRYPTION_NONE:
                    mEditMessage
                            .setHint(getString(R.string.send_plain_text_message));
                    break;
                case Message.ENCRYPTION_OTR:
                    mEditMessage.setHint(getString(R.string.send_otr_message));
                    break;
                case Message.ENCRYPTION_PGP:
                    mEditMessage.setHint(getString(R.string.send_pgp_message));
                    break;
                default:
                    break;
            }
        }
    }

    private void setupIme() {
        if (((ConversationActivity) getActivity()).usingEnterKey()) {
            mEditMessage.setInputType(mEditMessage.getInputType() & (~InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE));
        } else {
            mEditMessage.setInputType(mEditMessage.getInputType() | InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        mEditMessage = (EditMessage) view.findViewById(R.id.textinput);
        nameBar = (LinearLayout) view.findViewById(R.id.name_bar);
        /*sendToBar = (RelativeLayout) view.findViewById(R.id.send_to_bar);*/
        setupIme();

        if (SharedPrefUtil.getPrefUserInRole(getActivity()).equals(CommonConstants.ROLE_STAFF)) {
            if (nameBar != null) {
                nameBar.setVisibility(View.VISIBLE);
            }
            /*sendToBar.setVisibility(View.VISIBLE);
            flag_f = (Button) view.findViewById(R.id.flag_f);
            flag_m = (Button) view.findViewById(R.id.flag_m);*/

            /*flag_f.setOnClickListener(fFlagButtonListener);
            flag_m.setOnClickListener(mFlagButtonListener);*/
        } else if (SharedPrefUtil.getPrefUserInRole(getActivity()).equals(CommonConstants.ROLE_PARENT)) {
            if (nameBar != null) {
                nameBar.setVisibility(View.GONE);
            }
            /*sendToBar.setVisibility(View.GONE);*/
        }
        mEditMessage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                activity.hideConversationsOverview();
            }
        });
        mEditMessage.setOnEditorActionListener(mEditorActionListener);
        mEditMessage.setOnEnterPressedListener(new EditMessage.OnEnterPressed() {

            @Override
            public boolean onEnterPressed() {
                if (activity.enterIsSend()) {
                    sendMessage();
                    return true;
                } else {
                    return false;
                }
            }
        });

        mSendButton = (ImageButton) view.findViewById(R.id.textSendButton);
        mSendButton.setOnClickListener(this.mSendButtonListener);

        snackbar = (RelativeLayout) view.findViewById(R.id.snackbar);
        snackbarMessage = (TextView) view.findViewById(R.id.snackbar_message);
        snackbarAction = (TextView) view.findViewById(R.id.snackbar_action);

        messagesView = (ListView) view.findViewById(R.id.messages_view);
        messagesView.setOnScrollListener(mOnScrollListener);
        messagesView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        messageListAdapter = new MessageAdapter((ConversationActivity) getActivity(), this.messageList);
        messageListAdapter.setOnContactPictureClicked(new MessageAdapter.OnContactPictureClicked() {

            @Override
            public void onContactPictureClicked(Message message) {
                if (message.getStatus() <= Message.STATUS_RECEIVED) {
                    if (message.getConversation().getMode() == Conversation.MODE_MULTI) {
                        /*if (message.getCounterpart() != null) {
                            if (!message.getCounterpart().isBareJid()) {
                                highlightInConference(message.getCounterpart().getResourcepart());
                            } else {
                                highlightInConference(message.getCounterpart().toString());
                            }
                        }*/
                    } else {
                        //SCH: No need
//                        activity.switchToContactDetails(message.getContact());
                    }
                } else {
                    //SCH: No need
//                    Account account = message.getConversation().getAccount();
//                    Intent intent = new Intent(activity, EditAccountActivity.class);
//                    intent.putExtra("jid", account.getJid().toBareJid().toString());
//                    startActivity(intent);
                }
            }
        });
        messageListAdapter
                .setOnContactPictureLongClicked(new MessageAdapter.OnContactPictureLongClicked() {

                    @Override
                    public void onContactPictureLongClicked(Message message) {
                        if (message.getStatus() <= Message.STATUS_RECEIVED) {
                            if (message.getConversation().getMode() == Conversation.MODE_MULTI) {
                                /*if (message.getCounterpart() != null) {
                                    privateMessageWith(message.getCounterpart());
                                }*/
                            }
                        } else {
                            activity.showQrCode();
                        }
                    }
                });
        messagesView.setAdapter(messageListAdapter);
        registerForContextMenu(messagesView);
        return view;
    }

    //get JIDs of this studentId(6)'s parents
    private void setFlagsWithJids() {
        Uri parentJidUri = PTAppContract.ParentChildRelation.CONTENT_URI;
        String selection = PTAppContract.ParentChildRelation.STUDENT_ID + "=?";
        String[] args = new String[]{String.valueOf(conversation.getStudentId())};
        String[] projection = {PTAppContract.ParentChildRelation.PARENT_TYPE, PTAppContract.Parent.JID};
        Cursor c = getActivity().getContentResolver().query(parentJidUri, projection, selection, args, null);
        while (c.moveToNext()) {
            try {
                flagsWithJids.put(c.getString(0), Jid.fromString(c.getString(1))); //ParentType will be as F,M
            } catch (InvalidJidException e) {
                if (e == null) {
                    LOGW(TAG, "InvalidJidException");
                } else {
                    LOGW(TAG, e.getMessage());
                }
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        synchronized (this.messageList) {
            super.onCreateContextMenu(menu, v, menuInfo);
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            this.selectedMessage = this.messageList.get(acmi.position);
            populateContextMenu(menu);
        }
    }

    private void populateContextMenu(ContextMenu menu) {
        final Message m = this.selectedMessage;
        if (m.getType() != Message.TYPE_STATUS) {
            activity.getMenuInflater().inflate(R.menu.message_context, menu);
            menu.setHeaderTitle(R.string.message_options);
            MenuItem copyText = menu.findItem(R.id.copy_text);
            MenuItem shareWith = menu.findItem(R.id.share_with);
            MenuItem sendAgain = menu.findItem(R.id.send_again);
            MenuItem copyUrl = menu.findItem(R.id.copy_url);
            MenuItem downloadImage = menu.findItem(R.id.download_image);
            MenuItem cancelTransmission = menu.findItem(R.id.cancel_transmission);
            if (m.getType() != Message.TYPE_TEXT || m.getDownloadable() != null) {
                copyText.setVisible(false);
            }
            if (m.getType() == Message.TYPE_TEXT
                    || m.getType() == Message.TYPE_PRIVATE
                    || m.getDownloadable() != null) {
                shareWith.setVisible(false);
            }
            if (m.getStatus() != Message.STATUS_SEND_FAILED) {
                sendAgain.setVisible(false);
            }
            if ((m.getType() != Message.TYPE_IMAGE && m.getDownloadable() == null)
                    || m.getImageParams().url == null) {
                copyUrl.setVisible(false);
            }
            if (m.getType() != Message.TYPE_TEXT
                    || m.getDownloadable() != null
                    || !m.bodyContainsDownloadable()) {
                downloadImage.setVisible(false);
            }
            if (!((m.getDownloadable() != null && !(m.getDownloadable() instanceof DownloadablePlaceholder))
                    || (m.isFileOrImage() && (m.getStatus() == Message.STATUS_WAITING
                    || m.getStatus() == Message.STATUS_OFFERED)))) {
                cancelTransmission.setVisible(false);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share_with:
                shareWith(selectedMessage);
                return true;
            case R.id.copy_text:
                copyText(selectedMessage);
                return true;
            case R.id.send_again:
                resendMessage(selectedMessage);
                return true;
            case R.id.copy_url:
                copyUrl(selectedMessage);
                return true;
            case R.id.download_image:
                downloadImage(selectedMessage);
                return true;
            case R.id.cancel_transmission:
                cancelTransmission(selectedMessage);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void shareWith(Message message) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM,
                activity.xmppConnectionService.getFileBackend()
                        .getJingleFileUri(message));
        shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        String path = message.getRelativeFilePath();
        String mime = path == null ? null : URLConnection.guessContentTypeFromName(path);
        if (mime == null) {
            mime = "image/webp";
        }
        shareIntent.setType(mime);
        activity.startActivity(Intent.createChooser(shareIntent, getText(R.string.share_with)));
    }

    private void copyText(Message message) {
        if (activity.copyTextToClipboard(message.getMergedBody(),
                R.string.message_text)) {
            Toast.makeText(activity, R.string.message_copied_to_clipboard,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void resendMessage(Message message) {
        if (message.getType() == Message.TYPE_FILE || message.getType() == Message.TYPE_IMAGE) {
            DownloadableFile file = activity.xmppConnectionService.getFileBackend().getFile(message);
            if (!file.exists()) {
                Toast.makeText(activity, R.string.file_deleted, Toast.LENGTH_SHORT).show();
                message.setDownloadable(new DownloadablePlaceholder(Downloadable.STATUS_DELETED));
                return;
            }
        }
        activity.xmppConnectionService.resendFailedMessages(message);
    }

    private void copyUrl(Message message) {
        if (activity.copyTextToClipboard(
                message.getImageParams().url.toString(), R.string.image_url)) {
            Toast.makeText(activity, R.string.url_copied_to_clipboard,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadImage(Message message) {
        activity.xmppConnectionService.getHttpConnectionManager()
                .createNewConnection(message);
    }

    private void cancelTransmission(Message message) {
        Downloadable downloadable = message.getDownloadable();
        if (downloadable != null) {
            downloadable.cancel();
        } else {
            activity.xmppConnectionService.markMessage(message, Message.STATUS_SEND_FAILED);
        }
    }

    protected void privateMessageWith(final Jid counterpart) {
        this.mEditMessage.setText("");
        this.conversation.setNextCounterpart(counterpart);
        updateChatMsgHint();
    }

    protected void highlightInConference(String nick) {
        String oldString = mEditMessage.getText().toString().trim();
        if (oldString.isEmpty() || mEditMessage.getSelectionStart() == 0) {
            mEditMessage.getText().insert(0, nick + ": ");
        } else {
            if (mEditMessage.getText().charAt(
                    mEditMessage.getSelectionStart() - 1) != ' ') {
                nick = " " + nick;
            }
            mEditMessage.getText().insert(mEditMessage.getSelectionStart(),
                    nick + " ");
        }
    }

    @Override
    public void onStop() {
        mDecryptJobRunning = false;
        super.onStop();
        if (this.conversation != null) {
            this.conversation.setNextMessage(mEditMessage.getText().toString());
        }
    }

    public void reInit(Conversation conversation) {
        if (conversation == null) {
            return;
        }
        if (this.conversation != null) {
            this.conversation.setNextMessage(mEditMessage.getText().toString());
            this.conversation.trim();
        }
        this.activity = (ConversationActivity) getActivity();
        this.conversation = conversation;
        this.mDecryptJobRunning = false;
        this.mEncryptedMessages.clear();
        if (this.conversation.getMode() == Conversation.MODE_MULTI) {
            this.conversation.setNextCounterpart(null);
        }
        this.mEditMessage.setText("");
        this.mEditMessage.append(this.conversation.getNextMessage());
        this.messagesView.invalidateViews();
        updateMessages();
        this.messagesLoaded = true;
        int size = this.messageList.size();
        if (size > 0) {
            messagesView.setSelection(size - 1);
        }
        //setting JIds
        if (SharedPrefUtil.getPrefUserInRole(getActivity()).equals(CommonConstants.ROLE_STAFF)) {
            setFlagsWithJids();
            Jid jid = flagsWithJids.get(Message.PARENT_TYPE_F);
            if (!jIds.contains(jid)) {
                jIds.add(jid);
                selectedFlags.add(Message.PARENT_TYPE_F);
                /*((LinearLayout) view.getParent()).setBackgroundColor(
                        getResources().getColor(R.color.refresh_progress_1));*/
            }
            Jid jidM = flagsWithJids.get(Message.PARENT_TYPE_M);
            if (!jIds.contains(jidM)) {
                jIds.add(jidM);
                selectedFlags.add(Message.PARENT_TYPE_M);
            }
        } else if (SharedPrefUtil.getPrefUserInRole(getActivity()).equals(CommonConstants.ROLE_PARENT)) {
            setTeacherJid(conversation);

        } else if (SharedPrefUtil.getPrefUserInRole(getActivity()).equals(CommonConstants.ROLE_STUDENT)) {

        }

    }

    private void setTeacherJid(Conversation conversation) {
        //put teacher JID in the list
        Jid teacherJid = null;

        Uri teacherJidUri = PTAppContract.Staff.CONTENT_STAFF_JID_URI;
        String selection = PTAppDatabase.Tables.DEFAULT_GROUPS + "." + PTAppContract.DefaultGroups._ID + "=?";
        String[] args = new String[]{String.valueOf(conversation.getGroupId())};
        String[] projection = {PTAppDatabase.Tables.STAFF + "." + PTAppContract.Staff.JID};
        Cursor c = getActivity().getContentResolver().query(teacherJidUri, projection, selection, args, null);
        if (c.moveToFirst()) {
            try {
                teacherJid = Jid.fromString(c.getString(0));
            } catch (InvalidJidException e) {
                if (e == null) {
                    LOGW(TAG, "InvalidJidException");
                } else {
                    LOGW(TAG, e.getMessage());
                }
            }
        }
            /*teacherJid = Jid.fromString("x1.e@xmpp.jp");*/
        if (!jIds.contains(teacherJid)) {
            jIds.add(teacherJid);
        }
    }

    public void updateMessages() {
        synchronized (this.messageList) {
            if (getView() == null) {
                return;
            }
            hideSnackbar();
            final ConversationActivity activity = (ConversationActivity) getActivity();
            if (this.conversation != null) {
                final Contact contact = this.conversation.getContact();
                if (this.conversation.isBlocked()) {
                    showSnackbar(R.string.contact_blocked, R.string.unblock,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(final View v) {
                                    v.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            v.setVisibility(View.INVISIBLE);
                                        }
                                    });
                                    if (conversation.isDomainBlocked()) {
                                        //SCH : No need
//                                        BlockContactDialog.show(getActivity(), ((ConversationActivity) getActivity()).xmppConnectionService, conversation);
                                    } else {
                                        ((ConversationActivity) getActivity()).unblockConversation(conversation);
                                    }
                                }
                            });
                } else if (this.conversation.isMuted()) {
                    showSnackbar(R.string.notifications_disabled, R.string.enable,
                            new View.OnClickListener() {

                                @Override
                                public void onClick(final View v) {
                                    activity.unmuteConversation(conversation);
                                }
                            });
                } else if (!contact.showInRoster()
                        && contact
                        .getOption(Contact.Options.PENDING_SUBSCRIPTION_REQUEST)) {
                    //SCH: No need
//                    showSnackbar(R.string.contact_added_you, R.string.add_back,
//                            new View.OnClickListener() {
//
//                                @Override
//                                public void onClick(View v) {
//                                    activity.xmppConnectionService
//                                            .createContact(contact);
//                                    activity.switchToContactDetails(contact);
//                                }
//                            });
                } else if (conversation.getMode() == Conversation.MODE_SINGLE) {
                    makeFingerprintWarning();
                } else if (!conversation.getMucOptions().online()
                        && conversation.getAccount().getStatus() == Account.State.ONLINE) {
                    int error = conversation.getMucOptions().getError();
                    switch (error) {
                        case MucOptions.ERROR_NICK_IN_USE:
                            //SCH: No need
//                            showSnackbar(R.string.nick_in_use, R.string.edit,
//                                    clickToMuc);
                            break;
                        case MucOptions.ERROR_UNKNOWN:
                            showSnackbar(R.string.conference_not_found,
                                    R.string.leave, leaveMuc);
                            break;
                        case MucOptions.ERROR_PASSWORD_REQUIRED:
                            showSnackbar(R.string.conference_requires_password,
                                    R.string.enter_password, enterPassword);
                            break;
                        case MucOptions.ERROR_BANNED:
                            showSnackbar(R.string.conference_banned,
                                    R.string.leave, leaveMuc);
                            break;
                        case MucOptions.ERROR_MEMBERS_ONLY:
                            showSnackbar(R.string.conference_members_only,
                                    R.string.leave, leaveMuc);
                            break;
                        case MucOptions.KICKED_FROM_ROOM:
                            showSnackbar(R.string.conference_kicked, R.string.join,
                                    joinMuc);
                            break;
                        default:
                            break;
                    }
                }
                conversation.populateWithMessages(ConversationFragment.this.messageList);
                for (final Message message : this.messageList) {
                    if (message.getEncryption() == Message.ENCRYPTION_PGP
                            && (message.getStatus() == Message.STATUS_RECEIVED || message
                            .getStatus() >= Message.STATUS_SEND)
                            && message.getDownloadable() == null) {
                        if (!mEncryptedMessages.contains(message)) {
                            mEncryptedMessages.add(message);
                        }
                    }
                }
                decryptNext();
                updateStatusMessages();
                this.messageListAdapter.notifyDataSetChanged();
                updateChatMsgHint();
                if (!activity.isConversationsOverviewVisable() || !activity.isConversationsOverviewHideable()) {
                    activity.sendReadMarkerIfNecessary(conversation);
                }
                this.updateSendButton();
            }
        }
    }

    private void decryptNext() {
        Message next = this.mEncryptedMessages.peek();
        PgpEngine engine = activity.xmppConnectionService.getPgpEngine();

        if (next != null && engine != null && !mDecryptJobRunning) {
            mDecryptJobRunning = true;
            engine.decrypt(next, new UiCallback<Message>() {

                @Override
                public void userInputRequired(PendingIntent pi, Message message) {
                    mDecryptJobRunning = false;
                    askForPassphraseIntent = pi.getIntentSender();
                    showSnackbar(R.string.openpgp_messages_found,
                            R.string.decrypt, clickToDecryptListener);
                }

                @Override
                public void success(Message message) {
                    mDecryptJobRunning = false;
                    try {
                        mEncryptedMessages.remove();
                    } catch (final NoSuchElementException ignored) {

                    }
                    activity.xmppConnectionService.updateMessage(message);
                }

                @Override
                public void error(int error, Message message) {
                    message.setEncryption(Message.ENCRYPTION_DECRYPTION_FAILED);
                    mDecryptJobRunning = false;
                    try {
                        mEncryptedMessages.remove();
                    } catch (final NoSuchElementException ignored) {

                    }
                    activity.xmppConnectionService.updateConversationUi();
                }
            });
        }
    }

    private void messageSent() {
        int size = this.messageList.size();
        messagesView.setSelection(size - 1);
        mEditMessage.setText("");
        updateChatMsgHint();
    }

    public void updateSendButton() {
        Conversation c = this.conversation;
        if (activity.useSendButtonToIndicateStatus() && c != null
                && c.getAccount().getStatus() == Account.State.ONLINE) {
            if (c.getMode() == Conversation.MODE_SINGLE) {
                switch (c.getContact().getMostAvailableStatus()) {
                    case Presences.CHAT:
                        this.mSendButton
                                .setImageResource(R.drawable.ic_action_send_now_online);
                        break;
                    case Presences.ONLINE:
                        this.mSendButton
                                .setImageResource(R.drawable.ic_action_send_now_online);
                        break;
                    case Presences.AWAY:
                        this.mSendButton
                                .setImageResource(R.drawable.ic_action_send_now_away);
                        break;
                    case Presences.XA:
                        this.mSendButton
                                .setImageResource(R.drawable.ic_action_send_now_away);
                        break;
                    case Presences.DND:
                        this.mSendButton
                                .setImageResource(R.drawable.ic_action_send_now_dnd);
                        break;
                    default:
                        this.mSendButton
                                .setImageResource(R.drawable.ic_action_send_now_offline);
                        break;
                }
            } else if (c.getMode() == Conversation.MODE_MULTI) {
                if (c.getMucOptions().online()) {
                    this.mSendButton
                            .setImageResource(R.drawable.ic_action_send_now_online);
                } else {
                    this.mSendButton
                            .setImageResource(R.drawable.ic_action_send_now_offline);
                }
            } else {
                this.mSendButton
                        .setImageResource(R.drawable.ic_action_send_now_offline);
            }
        } else {
            this.mSendButton
                    .setImageResource(R.drawable.ic_action_send_now_offline);
        }
    }

    protected void updateStatusMessages() {
        synchronized (this.messageList) {
            if (conversation.getMode() == Conversation.MODE_SINGLE) {
                for (int i = this.messageList.size() - 1; i >= 0; --i) {
                    if (this.messageList.get(i).getStatus() == Message.STATUS_RECEIVED) {
                        return;
                    } else {
                        if (this.messageList.get(i).getStatus() == Message.STATUS_SEND_DISPLAYED) {
                            this.messageList.add(i + 1, Message.createStatusMessage(conversation));
                            return;
                        }
                    }
                }
            }
        }
    }

    protected void makeFingerprintWarning() {
        if (conversation.smpRequested()) {
            //SCH: No need
//            showSnackbar(R.string.smp_requested, R.string.verify, new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(activity, VerifyOTRActivity.class);
//                    intent.setAction(VerifyOTRActivity.ACTION_VERIFY_CONTACT);
//                    intent.putExtra("contact", conversation.getContact().getJid().toBareJid().toString());
//                    intent.putExtra("account", conversation.getAccount().getJid().toBareJid().toString());
//                    intent.putExtra("mode", VerifyOTRActivity.MODE_ANSWER_QUESTION);
//                    startActivity(intent);
//                }
//            });
        } else if (conversation.hasValidOtrSession() && (conversation.getOtrSession().getSessionStatus() == SessionStatus.ENCRYPTED)
                && (!conversation.isOtrFingerprintVerified())) {
            showSnackbar(R.string.unknown_otr_fingerprint, R.string.verify, clickToVerify);
        }
    }

    protected void showSnackbar(final int message, final int action,
                                final View.OnClickListener clickListener) {
        snackbar.setVisibility(View.VISIBLE);
        snackbar.setOnClickListener(null);
        snackbarMessage.setText(message);
        snackbarMessage.setOnClickListener(null);
        snackbarAction.setVisibility(View.VISIBLE);
        snackbarAction.setText(action);
        snackbarAction.setOnClickListener(clickListener);
    }

    protected void hideSnackbar() {
        snackbar.setVisibility(View.GONE);
    }

    protected void sendPlainTextMessage(Message message) {
        ConversationActivity activity = (ConversationActivity) getActivity();
        activity.xmppConnectionService.sendMessage(message);
        messageSent();

    }

    protected void sendPgpMessage(final Message message) {
        final ConversationActivity activity = (ConversationActivity) getActivity();
        final XmppConnectionService xmppService = activity.xmppConnectionService;
        final Contact contact = message.getConversation().getContact();
        if (activity.hasPgp()) {
            if (conversation.getMode() == Conversation.MODE_SINGLE) {
                if (contact.getPgpKeyId() != 0) {
                    xmppService.getPgpEngine().hasKey(contact,
                            new UiCallback<Contact>() {

                                @Override
                                public void userInputRequired(PendingIntent pi,
                                                              Contact contact) {
                                    activity.runIntent(
                                            pi,
                                            ConversationActivity.REQUEST_ENCRYPT_MESSAGE);
                                }

                                @Override
                                public void success(Contact contact) {
                                    messageSent();
                                    activity.encryptTextMessage(message);
                                }

                                @Override
                                public void error(int error, Contact contact) {

                                }
                            });

                } else {
                    showNoPGPKeyDialog(false,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    conversation
                                            .setNextEncryption(Message.ENCRYPTION_NONE);
                                    xmppService.databaseBackend
                                            .updateConversation(conversation);
                                    message.setEncryption(Message.ENCRYPTION_NONE);
                                    xmppService.sendMessage(message);
                                    messageSent();
                                }
                            });
                }
            } else {
                if (conversation.getMucOptions().pgpKeysInUse()) {
                    if (!conversation.getMucOptions().everybodyHasKeys()) {
                        Toast warning = Toast
                                .makeText(getActivity(),
                                        R.string.missing_public_keys,
                                        Toast.LENGTH_LONG);
                        warning.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        warning.show();
                    }
                    activity.encryptTextMessage(message);
                    messageSent();
                } else {
                    showNoPGPKeyDialog(true,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    conversation
                                            .setNextEncryption(Message.ENCRYPTION_NONE);
                                    message.setEncryption(Message.ENCRYPTION_NONE);
                                    xmppService.databaseBackend
                                            .updateConversation(conversation);
                                    xmppService.sendMessage(message);
                                    messageSent();
                                }
                            });
                }
            }
        } else {
            activity.showInstallPgpDialog();
        }
    }

    public void showNoPGPKeyDialog(boolean plural,
                                   DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIconAttribute(android.R.attr.alertDialogIcon);
        if (plural) {
            builder.setTitle(getString(R.string.no_pgp_keys));
            builder.setMessage(getText(R.string.contacts_have_no_pgp_keys));
        } else {
            builder.setTitle(getString(R.string.no_pgp_key));
            builder.setMessage(getText(R.string.contact_has_no_pgp_key));
        }
        builder.setNegativeButton(getString(R.string.cancel), null);
        builder.setPositiveButton(getString(R.string.send_unencrypted),
                listener);
        builder.create().show();
    }

    //SCH
//    protected void sendOtrMessage(final Message message) {
//        final ConversationActivity activity = (ConversationActivity) getActivity();
//        final XmppConnectionService xmppService = activity.xmppConnectionService;
//        activity.selectPresence(message.getConversation(),
//                new XmppActivity.OnPresenceSelected() {
//
//                    @Override
//                    public void onPresenceSelected() {
//                        /*message.setCounterpart(conversation.getNextCounterpart());
//                        xmppService.sendMessage(message);
//                        messageSent();*/
//                    }
//                });
//    }

    public void appendText(String text) {
        String previous = this.mEditMessage.getText().toString();
        if (previous.length() != 0 && !previous.endsWith(" ")) {
            text = " " + text;
        }
        this.mEditMessage.append(text);
    }
}
