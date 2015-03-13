package com.ptapp.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ptapp.entities.Blockable;
import com.ptapp.entities.Contact;
import com.ptapp.entities.Conversation;
import com.ptapp.entities.Message;
import com.ptapp.provider.PTAppContract;
import com.ptapp.app.R;
import com.ptapp.utils.CommonMethods;
import com.ptapp.utils.ExceptionHelper;
import com.ptapp.xmpp.OnUpdateBlocklist;
import com.ptapp.service.XmppConnectionService.OnAccountUpdate;
import com.ptapp.service.XmppConnectionService.OnConversationUpdate;
import com.ptapp.service.XmppConnectionService.OnRosterUpdate;

import java.util.ArrayList;
import java.util.List;

import static com.ptapp.utils.LogUtils.LOGD;
import static com.ptapp.utils.LogUtils.LOGI;

public class ConversationActivity extends XmppActivity
        implements OnAccountUpdate, OnConversationUpdate, OnRosterUpdate, OnUpdateBlocklist {

    public static final String ACTION_DOWNLOAD = "eu.siacs.conversations.action.DOWNLOAD";

    public static final String VIEW_CONVERSATION = "viewConversation";
    public static final String CONVERSATION = "conversationUuid";
    public static final String MESSAGE = "messageUuid";
    public static final String TEXT = "text";
    public static final String NICK = "nick";
    public static final String NAME = "studentName";

    public static final int REQUEST_SEND_MESSAGE = 0x0201;
    public static final int REQUEST_DECRYPT_PGP = 0x0202;
    public static final int REQUEST_ENCRYPT_MESSAGE = 0x0207;
    private static final int ATTACHMENT_CHOICE_CHOOSE_IMAGE = 0x0301;
    private static final int ATTACHMENT_CHOICE_TAKE_PHOTO = 0x0302;
    private static final int ATTACHMENT_CHOICE_CHOOSE_FILE = 0x0303;
    private static final int ATTACHMENT_CHOICE_RECORD_VOICE = 0x0304;
    private static final String STATE_OPEN_CONVERSATION = "state_open_conversation";
    private static final String STATE_PANEL_OPEN = "state_panel_open";
    private static final String STATE_PENDING_URI = "state_pending_uri";

    private String mOpenConverstaion = null;
    private boolean mPanelOpen = true;
    private Uri mPendingImageUri = null;
    private Uri mPendingFileUri = null;

    //private View mContentView;

    private List<Conversation> conversationList = new ArrayList<>();
    private Conversation mSelectedConversation = null;
    //private ListView listView;
    private ConversationFragment mConversationFragment;

    private ArrayAdapter<Conversation> listAdapter;

    private Toast prepareFileToast;

    private boolean mActivityPaused = false;
    private TextView vwStuName;
    //private String studentName;

    /*public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }*/

    public Conversation getSelectedConversation() {
        return this.mSelectedConversation;
    }

    public void setSelectedConversation(Conversation conversation) {
        this.mSelectedConversation = conversation;
    }

    public void showConversationsOverview() {
        LOGD(TAG, "...into showConversationOverview, which  now obsolete");
        /*if (mContentView instanceof SlidingPaneLayout) {
            SlidingPaneLayout mSlidingPaneLayout = (SlidingPaneLayout) mContentView;
            mSlidingPaneLayout.openPane();
        }*/
    }

    @Override
    protected String getShareableUri() {
        Conversation conversation = getSelectedConversation();
        if (conversation != null) {
            return conversation.getAccount().getShareableUri();
        } else {
            return "";
        }
    }

    public void hideConversationsOverview() {
        LOGD(TAG, "...into hideConversationsOverview, which  now obsolete");
        /*if (mContentView instanceof SlidingPaneLayout) {
            SlidingPaneLayout mSlidingPaneLayout = (SlidingPaneLayout) mContentView;
            mSlidingPaneLayout.closePane();
        }*/
    }

    public boolean isConversationsOverviewHideable() {
        LOGD(TAG, "...into isConversationsOverviewHideable, which  now obsolete");
        /*if (mContentView instanceof SlidingPaneLayout) {
            SlidingPaneLayout mSlidingPaneLayout = (SlidingPaneLayout) mContentView;
            return mSlidingPaneLayout.isSlideable();
        } else {
            return false;
        }*/
        return true;
    }

    public boolean isConversationsOverviewVisable() {
        LOGD(TAG, "...into isConversationsOverviewVisable, which  now obsolete");
        /*if (mContentView instanceof SlidingPaneLayout) {
            SlidingPaneLayout mSlidingPaneLayout = (SlidingPaneLayout) mContentView;
            return mSlidingPaneLayout.isOpen();
        } else {
            return true;
        }*/
        return false;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mOpenConverstaion = savedInstanceState.getString(
                    STATE_OPEN_CONVERSATION, null);
            mPanelOpen = savedInstanceState.getBoolean(STATE_PANEL_OPEN, true);
            String pending = savedInstanceState.getString(STATE_PENDING_URI, null);
            if (pending != null) {
                mPendingImageUri = Uri.parse(pending);
            }
        }

        setContentView(R.layout.fragment_conversations_overview);


        this.mConversationFragment = new ConversationFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.selected_conversation, this.mConversationFragment, "conversation");
        transaction.commit();

        /*listView = (ListView) findViewById(R.id.list);
        this.listAdapter = new ConversationAdapter(this, conversationList);
        listView.setAdapter(this.listAdapter);*/

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(false);
            getActionBar().setHomeButtonEnabled(false);
        }

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View clickedView,
                                    int position, long arg3) {
                if (getSelectedConversation() != conversationList.get(position)) {
                    setSelectedConversation(conversationList.get(position));
                    ConversationActivity.this.mConversationFragment.reInit(getSelectedConversation());
                }
                hideConversationsOverview();
                openConversation();
            }
        });*/
        //mContentView = findViewById(R.id.content_view_spl);
        /*if (mContentView == null) {
            mContentView = findViewById(R.id.content_view_ll);
        }*/
        /*if (mContentView instanceof SlidingPaneLayout) {
            SlidingPaneLayout mSlidingPaneLayout = (SlidingPaneLayout) mContentView;
            mSlidingPaneLayout.setParallaxDistance(150);
            mSlidingPaneLayout
                    .setShadowResource(R.drawable.es_slidingpane_shadow);
            mSlidingPaneLayout.setSliderFadeColor(0);
            mSlidingPaneLayout.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {

                @Override
                public void onPanelOpened(View arg0) {
                    updateActionBarTitle();
                    invalidateOptionsMenu();
                    hideKeyboard();
                    if (xmppConnectionServiceBound) {
                        xmppConnectionService.getNotificationService()
                                .setOpenConversation(null);
                    }
                    closeContextMenu();
                }

                @Override
                public void onPanelClosed(View arg0) {
                    openConversation();
                }

                @Override
                public void onPanelSlide(View arg0, float arg1) {


                }
            });
        }*/
    }

    @Override
    public void switchToConversation(Conversation conversation) {
        setSelectedConversation(conversation);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ConversationActivity.this.mConversationFragment.reInit(getSelectedConversation());
                openConversation();
            }
        });
    }

    private void updateActionBarTitle() {
        /*updateActionBarTitle(isConversationsOverviewHideable() && !isConversationsOverviewVisable());*/
        updateActionBarTitle(true);
    }

    private void updateActionBarTitle(boolean titleShouldBeName) {
        final ActionBar ab = getActionBar();
        final Conversation conversation = getSelectedConversation();
        if (ab != null) {
            if (titleShouldBeName && conversation != null) {
                ab.setDisplayHomeAsUpEnabled(true);
                ab.setHomeButtonEnabled(true);
                if (conversation.getMode() == Conversation.MODE_SINGLE || useSubjectToIdentifyConference()) {
                    //ab.setTitle(conversation.getName());
                    /*studentName= name;*/


                    //custom ab
                    View imgContainer = LayoutInflater.from(getActionBar().getThemedContext())
                            .inflate(R.layout.actionbar_image_title, null);
                    ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


                    ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                            | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
                    ab.setCustomView(imgContainer, lp);
                    ab.setTitle(conversation.getStudentName());

                    TextView tvClass = (TextView) imgContainer.findViewById(R.id.actionbar_class_subj);
                    tvClass.setText(conversation.getName());

                    //TODO:need to get the subject from db
                    /*String courseName = "English";*/
                    //CommonMethods.showABarCourseImage(ab, conversation.getName());
                    ab.setIcon(CommonMethods.getSubjectResId(conversation.getName()));
                } else {
                    ab.setTitle("Schoolo");
                    /*ab.setTitle(conversation.getJid().toBareJid().toString());*/
                }
            } else {
                ab.setDisplayHomeAsUpEnabled(false);
                ab.setHomeButtonEnabled(false);
                ab.setTitle("");
                Log.i(TAG, "No title.");
            }
        }
    }

    private void openConversation() {
        this.updateActionBarTitle();
        this.invalidateOptionsMenu();
        if (xmppConnectionServiceBound) {
            final Conversation conversation = getSelectedConversation();
            xmppConnectionService.getNotificationService().setOpenConversation(conversation);
            sendReadMarkerIfNecessary(conversation);
            vwStuName = (TextView) findViewById(R.id.student_name);
            if (conversation.getStudentId() > 0) {
                //set student Name
                Uri studentContentUri = PTAppContract.Student.buildStudentUri(String.valueOf(conversation.getStudentId()));
                String[] Projection = {PTAppContract.Student.FIRST_NAME, PTAppContract.Student.LAST_NAME};
                Cursor c = getContentResolver().query(studentContentUri, Projection, null, null, null);
                if (c.moveToFirst()) {
                    if (vwStuName != null) {
                        vwStuName.setText(c.getString(c.getColumnIndex(PTAppContract.Student.FIRST_NAME))
                                + " " + c.getString(c.getColumnIndex(PTAppContract.Student.LAST_NAME)));
                    }
                }
            }
        }
        //listAdapter.notifyDataSetChanged();
    }

    public void sendReadMarkerIfNecessary(final Conversation conversation) {
        if (!mActivityPaused && conversation != null && !conversation.isRead()) {
            xmppConnectionService.sendReadMarker(conversation);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.conversations, menu);
        return true;
    }

    private void selectPresenceToAttachFile(final int attachmentChoice) {
        selectPresence(getSelectedConversation(), new OnPresenceSelected() {

            @Override
            public void onPresenceSelected() {
                Intent intent = new Intent();
                boolean chooser = false;
                switch (attachmentChoice) {
                    case ATTACHMENT_CHOICE_CHOOSE_IMAGE:
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        chooser = true;
                        break;
                    case ATTACHMENT_CHOICE_TAKE_PHOTO:
                        mPendingImageUri = xmppConnectionService.getFileBackend().getTakePhotoUri();
                        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPendingImageUri);
                        break;
                    case ATTACHMENT_CHOICE_CHOOSE_FILE:
                        chooser = true;
                        intent.setType("*/*");
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        break;
                    case ATTACHMENT_CHOICE_RECORD_VOICE:
                        intent.setAction(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                        break;
                }
                if (intent.resolveActivity(getPackageManager()) != null) {
                    if (chooser) {
                        startActivityForResult(
                                Intent.createChooser(intent, getString(R.string.perform_action_with)),
                                attachmentChoice);
                    } else {
                        startActivityForResult(intent, attachmentChoice);
                    }
                }
            }
        });
    }

    private void attachFile(final int attachmentChoice) {
        final Conversation conversation = getSelectedConversation();
        if (conversation.getNextEncryption(forceEncryption()) == Message.ENCRYPTION_PGP) {
            if (hasPgp()) {
                if (conversation.getContact().getPgpKeyId() != 0) {
                    xmppConnectionService.getPgpEngine().hasKey(
                            conversation.getContact(),
                            new UiCallback<Contact>() {

                                @Override
                                public void userInputRequired(PendingIntent pi,
                                                              Contact contact) {
                                    ConversationActivity.this.runIntent(pi,
                                            attachmentChoice);
                                }

                                @Override
                                public void success(Contact contact) {
                                    selectPresenceToAttachFile(attachmentChoice);
                                }

                                @Override
                                public void error(int error, Contact contact) {
                                    displayErrorDialog(error);
                                }
                            });
                } else {
                    final ConversationFragment fragment = (ConversationFragment) getFragmentManager()
                            .findFragmentByTag("conversation");
                    if (fragment != null) {
                        fragment.showNoPGPKeyDialog(false,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        conversation
                                                .setNextEncryption(Message.ENCRYPTION_NONE);
                                        xmppConnectionService.databaseBackend
                                                .updateConversation(conversation);
                                        selectPresenceToAttachFile(attachmentChoice);
                                        xmppConnectionService.takeDbBackup(ConversationActivity.this);
                                    }
                                });
                    }
                }
            } else {
                showInstallPgpDialog();
            }
        } else if (getSelectedConversation().getNextEncryption(
                forceEncryption()) == Message.ENCRYPTION_NONE) {
            selectPresenceToAttachFile(attachmentChoice);
        } else {
            selectPresenceToAttachFile(attachmentChoice);
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            /*showConversationsOverview();*/
            finish();
            return true;
        }
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public void endConversation(Conversation conversation) {
        showConversationsOverview();
        xmppConnectionService.archiveConversation(conversation);
        if (conversationList.size() > 0) {
            setSelectedConversation(conversationList.get(0));
            this.mConversationFragment.reInit(getSelectedConversation());
        } else {
            setSelectedConversation(null);
        }
    }

    @SuppressLint("InflateParams")
    protected void clearHistoryDialog(final Conversation conversation) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.clear_conversation_history));
        View dialogView = getLayoutInflater().inflate(
                R.layout.dialog_clear_history, null);
        final CheckBox endConversationCheckBox = (CheckBox) dialogView
                .findViewById(R.id.end_conversation_checkbox);
        builder.setView(dialogView);
        builder.setNegativeButton(getString(R.string.cancel), null);
        builder.setPositiveButton(getString(R.string.delete_messages),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ConversationActivity.this.xmppConnectionService.clearConversationHistory(conversation);
                        if (endConversationCheckBox.isChecked()) {
                            endConversation(conversation);
                        } else {
                            updateConversationList();
                            ConversationActivity.this.mConversationFragment.updateMessages();
                        }
                    }
                });
        builder.create().show();
    }

    protected void attachFileDialog() {
        View menuAttachFile = findViewById(R.id.action_attach_file);
        if (menuAttachFile == null) {
            return;
        }
        PopupMenu attachFilePopup = new PopupMenu(this, menuAttachFile);
        attachFilePopup.inflate(R.menu.attachment_choices);
        if (new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION).resolveActivity(getPackageManager()) == null) {
            attachFilePopup.getMenu().findItem(R.id.attach_record_voice).setVisible(false);
        }
        attachFilePopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.attach_choose_picture:
                        attachFile(ATTACHMENT_CHOICE_CHOOSE_IMAGE);
                        break;
                    case R.id.attach_take_picture:
                        attachFile(ATTACHMENT_CHOICE_TAKE_PHOTO);
                        break;
                    case R.id.attach_choose_file:
                        attachFile(ATTACHMENT_CHOICE_CHOOSE_FILE);
                        break;
                    case R.id.attach_record_voice:
                        attachFile(ATTACHMENT_CHOICE_RECORD_VOICE);
                        break;
                }
                return false;
            }
        });
        attachFilePopup.show();
    }

    //SCH
    /*public void verifyOtrSessionDialog(final Conversation conversation, View view) {
        if (!conversation.hasValidOtrSession() || conversation.getOtrSession().getSessionStatus() != SessionStatus.ENCRYPTED) {
            Toast.makeText(this, R.string.otr_session_not_started, Toast.LENGTH_LONG).show();
            return;
        }
        if (view == null) {
            return;
        }
        PopupMenu popup = new PopupMenu(this, view);
        popup.inflate(R.menu.verification_choices);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(ConversationActivity.this, VerifyOTRActivity.class);
                intent.setAction(VerifyOTRActivity.ACTION_VERIFY_CONTACT);
                intent.putExtra("contact", conversation.getContact().getJid().toBareJid().toString());
                intent.putExtra("account", conversation.getAccount().getJid().toBareJid().toString());
                switch (menuItem.getItemId()) {
                    case R.id.scan_fingerprint:
                        intent.putExtra("mode", VerifyOTRActivity.MODE_SCAN_FINGERPRINT);
                        break;
                    case R.id.ask_question:
                        intent.putExtra("mode", VerifyOTRActivity.MODE_ASK_QUESTION);
                        break;
                    case R.id.manual_verification:
                        intent.putExtra("mode", VerifyOTRActivity.MODE_MANUAL_VERIFICATION);
                        break;
                }
                startActivity(intent);
                return true;
            }
        });
        popup.show();
    }
*/

    public void unmuteConversation(final Conversation conversation) {
        conversation.setMutedTill(0);
        this.xmppConnectionService.databaseBackend.updateConversation(conversation);
        updateConversationList();
        ConversationActivity.this.mConversationFragment.updateMessages();
        invalidateOptionsMenu();
    }

    /*@Override
    public void onBackPressed() {

        *//*if (!isConversationsOverviewVisable()) {
            showConversationsOverview();
        } else {
            moveTaskToBack(true);
        }*//*
    }*/

    @Override
    protected void onNewIntent(final Intent intent) {
        if (xmppConnectionServiceBound) {
            if (intent != null && VIEW_CONVERSATION.equals(intent.getType())) {
                handleViewConversationIntent(intent);
            }
        } else {
            setIntent(intent);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.xmppConnectionServiceBound) {
            this.onBackendConnected();
        }
        if (conversationList.size() >= 1) {
            this.onConversationUpdate();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        this.mActivityPaused = true;
        if (this.xmppConnectionServiceBound) {
            this.xmppConnectionService.getNotificationService().setIsInForeground(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        final int theme = findTheme();
        final boolean usingEnterKey = usingEnterKey();
        /*if (this.mTheme != theme || usingEnterKey != mUsingEnterKey) {
            recreate();
        }*/
        if (usingEnterKey != mUsingEnterKey) {
            recreate();
        }
        this.mActivityPaused = false;
        if (this.xmppConnectionServiceBound) {
            this.xmppConnectionService.getNotificationService().setIsInForeground(true);
        }
        if (!isConversationsOverviewVisable() || !isConversationsOverviewHideable()) {
            sendReadMarkerIfNecessary(getSelectedConversation());
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle savedInstanceState) {
        Conversation conversation = getSelectedConversation();
        if (conversation != null) {
            savedInstanceState.putString(STATE_OPEN_CONVERSATION,
                    conversation.getUuid());
        }
        savedInstanceState.putBoolean(STATE_PANEL_OPEN,
                isConversationsOverviewVisable());
        if (this.mPendingImageUri != null) {
            savedInstanceState.putString(STATE_PENDING_URI, this.mPendingImageUri.toString());
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    void onBackendConnected() {
        this.xmppConnectionService.getNotificationService().setIsInForeground(true);
        updateConversationList();
        if (xmppConnectionService.getAccounts().size() == 0) {
            //SCH
            /*startActivity(new Intent(this, EditAccountActivity.class));*/
        } else if (conversationList.size() <= 0) {
            //SCH
//            startActivity(new Intent(this, StartConversationActivity.class));
//            finish();
        } else if (getIntent() != null && VIEW_CONVERSATION.equals(getIntent().getType())) {
            handleViewConversationIntent(getIntent());
        } else if (selectConversationByUuid(mOpenConverstaion)) {
            if (mPanelOpen) {
                showConversationsOverview();
            } else {
                if (isConversationsOverviewHideable()) {
                    openConversation();
                }
            }
            this.mConversationFragment.reInit(getSelectedConversation());
            mOpenConverstaion = null;
        } else if (getSelectedConversation() != null) {

            this.mConversationFragment.updateMessages();
        } else {
            showConversationsOverview();
            mPendingImageUri = null;
            mPendingFileUri = null;
            setSelectedConversation(conversationList.get(0));
            this.mConversationFragment.reInit(getSelectedConversation());
        }

        if (mPendingImageUri != null) {
            attachImageToConversation(getSelectedConversation(), mPendingImageUri);
            mPendingImageUri = null;
        } else if (mPendingFileUri != null) {
            attachFileToConversation(getSelectedConversation(), mPendingFileUri);
            mPendingFileUri = null;
        }
        ExceptionHelper.checkForCrash(this, this.xmppConnectionService);
        setIntent(new Intent());
    }

    private void handleViewConversationIntent(final Intent intent) {
        final String uuid = (String) intent.getExtras().get(CONVERSATION);
        final String downloadUuid = (String) intent.getExtras().get(MESSAGE);
        final String text = intent.getExtras().getString(TEXT, "");
        final String nick = intent.getExtras().getString(NICK, null);
        final String name = intent.getExtras().getString(NAME, "");
        if (selectConversationByUuid(uuid)) {
            this.mConversationFragment.reInit(getSelectedConversation());
            if (nick != null) {
                this.mConversationFragment.highlightInConference(nick);
            } else {
                this.mConversationFragment.appendText(text);
            }
            hideConversationsOverview();
            openConversation();
            /*if (mContentView instanceof SlidingPaneLayout) {
                updateActionBarTitle(true); //fixes bug where slp isn't properly closed yet
            }*/
            if (downloadUuid != null) {
                final Message message = mSelectedConversation.findMessageWithFileAndUuid(downloadUuid);
                if (message != null) {
                    mConversationFragment.messageListAdapter.startDownloadable(message);
                }
            }
        }
    }

    private boolean selectConversationByUuid(String uuid) {
        if (uuid == null) {
            return false;
        }
        for (Conversation aConversationList : conversationList) {
            if (aConversationList.getUuid().equals(uuid)) {
                setSelectedConversation(aConversationList);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void unregisterListeners() {
        super.unregisterListeners();
        xmppConnectionService.getNotificationService().setOpenConversation(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_DECRYPT_PGP) {
                mConversationFragment.hideSnackbar();
                mConversationFragment.updateMessages();
            } else if (requestCode == ATTACHMENT_CHOICE_CHOOSE_IMAGE) {
                mPendingImageUri = data.getData();
                if (xmppConnectionServiceBound) {
                    attachImageToConversation(getSelectedConversation(), mPendingImageUri);
                    mPendingImageUri = null;
                }
            } else if (requestCode == ATTACHMENT_CHOICE_CHOOSE_FILE || requestCode == ATTACHMENT_CHOICE_RECORD_VOICE) {
                mPendingFileUri = data.getData();
                if (xmppConnectionServiceBound) {
                    attachFileToConversation(getSelectedConversation(), mPendingFileUri);
                    mPendingFileUri = null;
                }
            } else if (requestCode == ATTACHMENT_CHOICE_TAKE_PHOTO && mPendingImageUri != null) {
                if (xmppConnectionServiceBound) {
                    attachImageToConversation(getSelectedConversation(), mPendingImageUri);
                    mPendingImageUri = null;
                }
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(mPendingImageUri);
                sendBroadcast(intent);
            }
        } else {
            if (requestCode == ATTACHMENT_CHOICE_TAKE_PHOTO) {
                mPendingImageUri = null;
            }
        }
    }

    private void attachFileToConversation(Conversation conversation, Uri uri) {
        prepareFileToast = Toast.makeText(getApplicationContext(),
                getText(R.string.preparing_file), Toast.LENGTH_LONG);
        prepareFileToast.show();
        xmppConnectionService.attachFileToConversation(conversation, uri, new UiCallback<Message>() {
            @Override
            public void success(Message message) {
                hidePrepareFileToast();
                xmppConnectionService.sendMessage(message);
            }

            @Override
            public void error(int errorCode, Message message) {
                displayErrorDialog(errorCode);
            }

            @Override
            public void userInputRequired(PendingIntent pi, Message message) {

            }
        });
    }

    private void attachImageToConversation(Conversation conversation, Uri uri) {
        prepareFileToast = Toast.makeText(getApplicationContext(),
                getText(R.string.preparing_image), Toast.LENGTH_LONG);
        prepareFileToast.show();
        xmppConnectionService.attachImageToConversation(conversation, uri,
                new UiCallback<Message>() {

                    @Override
                    public void userInputRequired(PendingIntent pi,
                                                  Message object) {
                        hidePrepareFileToast();
                    }

                    @Override
                    public void success(Message message) {
                        xmppConnectionService.sendMessage(message);
                    }

                    @Override
                    public void error(int error, Message message) {
                        hidePrepareFileToast();
                        displayErrorDialog(error);
                    }
                });
    }

    private void hidePrepareFileToast() {
        if (prepareFileToast != null) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    prepareFileToast.cancel();
                }
            });
        }
    }

    public void updateConversationList() {
        xmppConnectionService.populateWithOrderedConversations(conversationList);
        //listAdapter.notifyDataSetChanged();
    }

    public void runIntent(PendingIntent pi, int requestCode) {
        try {
            this.startIntentSenderForResult(pi.getIntentSender(), requestCode,
                    null, 0, 0, 0);
        } catch (final IntentSender.SendIntentException ignored) {
        }
    }

    public void encryptTextMessage(Message message) {
        xmppConnectionService.getPgpEngine().encrypt(message,
                new UiCallback<Message>() {

                    @Override
                    public void userInputRequired(PendingIntent pi,
                                                  Message message) {
                        ConversationActivity.this.runIntent(pi,
                                ConversationActivity.REQUEST_SEND_MESSAGE);
                    }

                    @Override
                    public void success(Message message) {
                        message.setEncryption(Message.ENCRYPTION_DECRYPTED);
                        xmppConnectionService.sendMessage(message);
                    }

                    @Override
                    public void error(int error, Message message) {

                    }
                });
    }

    public boolean forceEncryption() {
        return getPreferences().getBoolean("force_encryption", false);
    }

    public boolean useSendButtonToIndicateStatus() {
        return getPreferences().getBoolean("send_button_status", false);
    }

    public boolean indicateReceived() {
        return getPreferences().getBoolean("indicate_received", false);
    }

    @Override
    public void onAccountUpdate() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                updateConversationList();
                ConversationActivity.this.mConversationFragment.updateMessages();
                updateActionBarTitle();
            }
        });
    }

    @Override
    public void onConversationUpdate() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                updateConversationList();
                if (conversationList.size() == 0) {
                    //SCH: no need
//                    startActivity(new Intent(getApplicationContext(),
//                            StartConversationActivity.class));
//                    finish();
                }
                ConversationActivity.this.mConversationFragment.updateMessages();
                updateActionBarTitle();
            }
        });
    }

    @Override
    public void onRosterUpdate() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                updateConversationList();
                ConversationActivity.this.mConversationFragment.updateMessages();
                updateActionBarTitle();
            }
        });
    }

    @Override
    public void OnUpdateBlocklist(Status status) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                invalidateOptionsMenu();
                ConversationActivity.this.mConversationFragment.updateMessages();
            }
        });
    }

    public void unblockConversation(final Blockable conversation) {
        xmppConnectionService.sendUnblockRequest(conversation);
    }

    public boolean enterIsSend() {
        return getPreferences().getBoolean("enter_is_send", false);
    }
}
