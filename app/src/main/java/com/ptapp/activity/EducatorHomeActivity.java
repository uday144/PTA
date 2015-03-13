package com.ptapp.activity;

import android.app.ActionBar;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ptapp.dao.EducatorDAO;
import com.ptapp.entities.Account;
import com.ptapp.app.R;
import com.ptapp.service.XmppConnectionService;
import com.ptapp.utils.CommonConstants;
import com.ptapp.utils.CommonMethods;
import com.ptapp.utils.CryptoHelper;
import com.ptapp.utils.ExceptionHelper;
import com.ptapp.utils.UIHelper;
import com.ptapp.widget.CollectionView;
import com.ptapp.widget.DrawShadowFrameLayout;
import com.ptapp.xmpp.XmppConnection;
import com.ptapp.xmpp.jid.InvalidJidException;
import com.ptapp.xmpp.jid.Jid;
import com.ptapp.xmpp.pep.Avatar;
import com.squareup.picasso.Picasso;

import static com.ptapp.utils.LogUtils.LOGD;
import static com.ptapp.utils.LogUtils.LOGE;
import static com.ptapp.utils.LogUtils.LOGI;
import static com.ptapp.utils.LogUtils.makeLogTag;

public class EducatorHomeActivity extends XmppActivity implements
        SessionsFragment.Callbacks,
        XmppConnectionService.OnAccountUpdate {

    private static final String TAG = makeLogTag(EducatorHomeActivity.class);

    private SessionsFragment mSessionsFrag = null;
    private DrawShadowFrameLayout mDrawShadowFrameLayout;

    // How is this Activity being used?
    private static final int MODE_EXPLORE = 0; // as top-level "Explore" screen
    private int mMode = MODE_EXPLORE;

    /*xmpp*/
    private Account mAccount;
    //private Jid jidToEdit;
    private boolean mFetchingAvatar = false;

    //temp
    TextView online;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_educator_home);

        String s="Debug-infos:";
        s += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
        s += "\n OS API Level: " + android.os.Build.VERSION.SDK_INT;
        s += "\n Device: " + android.os.Build.DEVICE;
        s += "\n Model (and Product): " + android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")";
        s += "\n getWidth: " + getWindow().getWindowManager().getDefaultDisplay().getWidth();
        s += "\n getHeight: " + getWindow().getWindowManager().getDefaultDisplay().getHeight();

        Log.d("dd", s);


        mDrawShadowFrameLayout = (DrawShadowFrameLayout) findViewById(R.id.main_content);
        overridePendingTransition(0, 0);

        ActionBar ab = getActionBar();
        if (ab != null) {
            if (mMode == MODE_EXPLORE) {
                // no title (to make more room for navigation and actions)
                // unless Nav Drawer opens
                ab.setTitle(getString(R.string.app_name));
                ab.setDisplayShowTitleEnabled(false);

                //Spinner has been removed, just a image
                trySetUpActionBarSpinner();
            }
        }
        //mButterBar = findViewById(R.id.butter_bar);
        //registerHideableHeaderView(mButterBar);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        CollectionView collectionView = (CollectionView) findViewById(R.id.sessions_collection_view);
        if (collectionView != null) {
            enableActionBarAutoHide(collectionView);
        }

        mSessionsFrag = (SessionsFragment) getFragmentManager().findFragmentById(R.id.sessions_fragment);
        if (mSessionsFrag != null && savedInstanceState == null) {
            //getting staffId
            /*Uri staffUri = PTAppContract.Staff.CONTENT_URI;
            String[] Projection = {PTAppContract.Staff._ID};*/
            Bundle args = intentToFragmentArguments(getIntent());
            mSessionsFrag.reloadFromArguments(args);
        }

        //registerHideableHeaderView(findViewById(R.id.headerbar));
    }

    @Override
    protected int getSelfNavDrawerItem() {
        Log.v(TAG, "..into getSelfNavDrawerItem");
        // we only have a nav drawer if we are in top-level Explore mode.
        /*return mMode == MODE_EXPLORE ? NAVDRAWER_ITEM_EXPLORE : NAVDRAWER_ITEM_INVALID;*/

        //Disabling the nav drawer for now
        return NAVDRAWER_ITEM_INVALID;
    }

    @Override
    public boolean canSwipeRefreshChildScrollUp() {
        if (mSessionsFrag != null) {
            return mSessionsFrag.canCollectionViewScrollUp();
        }
        return super.canSwipeRefreshChildScrollUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_educator, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            //SCH: not used now
            /*case R.id.menu_search:
                //AnalyticsManager.sendEvent(SCREEN_LABEL, "launchsearch", "");
                //startActivity(new Intent(this, SearchActivity.class));
                return true;*/
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSessionSelected(String classSubjectId, String courseName,
                                  String groupJid, int groupId, View clickedView) {

        Log.i(TAG, "classSubjectId= " + classSubjectId);
        // open the messages screen
        Intent intent = new Intent(EducatorHomeActivity.this, EducatorMsgsActivity.class);
        intent.putExtra(EducatorMsgsActivity.BUNDLE_CLASS_SUBJECT_ID, classSubjectId);
        /*intent.putExtra(EducatorMsgsActivity.BUNDLE_CLASS_NAME, className);*/
        intent.putExtra(EducatorMsgsActivity.BUNDLE_COURSE_NAME, courseName);
        intent.putExtra(EducatorMsgsActivity.BUNDLE_GROUP_ID, groupId);
        intent.putExtra(EducatorMsgsActivity.BUNDLE_GROUP_JID, groupJid);
        startActivity(intent);
    }

    private void trySetUpActionBarSpinner() {
        ActionBar ab = getActionBar();
        Log.i(TAG, "into trySetupAction bar Spinner. mode= " + mMode + " spinnerConfigured= " + " tagmetadata= " + " ab= " + ab);
        if (mMode != MODE_EXPLORE || ab == null) {
            // already done it, or not ready yet, or don't need to do
            LOGD(TAG, "Not configuring Action Bar spinner.");
            return;
        }

        View spinnerContainer = LayoutInflater.from(getActionBar().getThemedContext())
                .inflate(R.layout.actionbar_spinner, null);
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //Add teacher image & spinner in the Action bar
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        ab.setCustomView(spinnerContainer, lp);

        /*ImageView ivTeacher = (ImageView)ab.getCustomView().findViewById(R.id.actionbar_edu_img);*/
        FrameLayout icon_events = (FrameLayout) spinnerContainer.findViewById(R.id.actionbar_events);
        online = (TextView) spinnerContainer.findViewById(R.id.ab_online);
        ImageView ivTeacher = (ImageView) spinnerContainer.findViewById(R.id.actionbar_edu_img);
        if (ivTeacher != null) {
            //Change in requirements
            ivTeacher.setVisibility(View.GONE);
            //showActionBarTeacherImage(ivTeacher);
        }

        if (icon_events != null) {

            //For now, hide this
            icon_events.setVisibility(View.GONE);
            /*
            icon_events.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(EducatorHomeActivity.this, EducatorEventsActivity.class);
                    startActivity(intent);
                }
            });*/
        }

        //showSecondaryFilters();
        //updateActionBarNavigation();
    }


    private void showActionBarTeacherImage(ImageView ivTeacher) {

        //Cursor c = null;
        String imgPath = "";

        /*try {

            Uri educatorUri = PTAppContract.Educators.buildEducatorUri(SharedPrefUtil.getPrefTeacherUserId(EducatorHomeActivity.this));
            Log.d(TAG, "educatorUri: " + educatorUri);
            c = getContentResolver().query(educatorUri, null, null, null, null);
            if (c.moveToFirst()) {

                imgPath = c.getString(c.getColumnIndex(PTAppContract.Educators.EDUCATOR_IMAGE_PATH));
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            c.close(); // Closing the cursor
        }*/

        if (!imgPath.isEmpty()) {
            Picasso.with(EducatorHomeActivity.this)
                    .load(imgPath) //
                    .placeholder(CommonConstants.LOADING) //
                    .error(CommonConstants.NO_PHOTO_AVAILABLE) //
                    .fit() //
                    .into(ivTeacher);
        } else {
            ivTeacher.setImageResource(R.drawable.nophotoavailable);
            Log.w(TAG, "Teacher image not found.");
        }


        //Get image path of the Logged-in user(teacher)
        EducatorDAO daoEdu = new EducatorDAO(EducatorHomeActivity.this);
        /*Educator teacher = daoEdu.getEducator(SharedPrefUtil.getPrefTeacherUserId(EducatorHomeActivity.this));
        if (teacher != null) {
            Picasso.with(EducatorHomeActivity.this)
                    .load(teacher.getImagePath()) //
                    .placeholder(CommonConstants.LOADING) //
                    .error(CommonConstants.NO_PHOTO_AVAILABLE) //
                    .fit() //
                    .into(ivTeacher);
        } else {
            ivTeacher.setImageResource(R.drawable.nophotoavailable);
            Log.w(TAG, "Couldn't load image because no Educator found.");
        }*/
    }

    private void setJabberIdAndConnect() {
        if (mAccount != null && mAccount.getStatus() == Account.State.DISABLED) {
            mAccount.setOption(Account.OPTION_DISABLED, false);
            xmppConnectionService.updateAccount(mAccount);
            return;
        }

        //TODO:Fetch this JID from the db or sharedPref, instead of hardcoded strings

        /*String makeJid = "jassie3@xmpp.jp";*/
        String makeJid = CommonMethods.getAppUserId(EducatorHomeActivity.this) + ".e@" + getString(R.string.chat_server);
        final Jid jid;
        try {
            jid = Jid.fromString(makeJid);
        } catch (final InvalidJidException e) {
            LOGE(TAG, getString(R.string.invalid_jid));
            return;
        }
        if (jid.isDomainJid()) {
            LOGE(TAG, getString(R.string.invalid_jid));
            return;
        }
        final String password = "1234";
        /*if (mAccount != null) {
            try {
                mAccount.setUsername(jid.hasLocalpart() ? jid.getLocalpart() : "");
                mAccount.setServer(jid.getDomainpart());
            } catch (final InvalidJidException ignored) {
                return;
            }
            mAccount.setPassword(password);
            *//*mAccount.setOption(Account.OPTION_REGISTER, registerNewAccount);*//*
            xmppConnectionService.updateAccount(mAccount);
        } else {*/
        try {
            mAccount = xmppConnectionService.findAccountByJid(Jid.fromString(makeJid));
            if (mAccount != null) {
                LOGI(TAG, getString(R.string.account_already_exists));
                xmppConnectionService.updateAccount(mAccount);
            } else {
                mAccount = new Account(jid.toBareJid(), password);
                mAccount.setOption(Account.OPTION_USETLS, true);
                mAccount.setOption(Account.OPTION_USECOMPRESSION, true);
                    /*mAccount.setOption(Account.OPTION_REGISTER, registerNewAccount);*/
                xmppConnectionService.createAccount(mAccount);
            }
        } catch (final InvalidJidException e) {
            return;
        }
        /*}*/
        updateSaveButton();
        //updateAccountInformation();
    }

    protected void updateSaveButton() {
        if (mAccount != null && mAccount.getStatus() == Account.State.CONNECTING) {
            LOGI(TAG, getString(R.string.account_status_connecting));
            updateStatusUI(getString(R.string.account_status_connecting));

        } else if (mAccount != null && mAccount.getStatus() == Account.State.DISABLED) {
            LOGI(TAG, getString(R.string.enable));

        } else if (mAccount.getStatus() == Account.State.NO_INTERNET) {

            updateStatusUI(getString(R.string.account_status_no_internet));
        } else {

            LOGI(TAG, getString(R.string.next)); //----
        }
    }

    private void updateAccountInformation() {

        if (this.mAccount.isOptionSet(Account.OPTION_REGISTER)) {
            //this.mRegisterNew.setVisibility(View.VISIBLE);
            //this.mRegisterNew.setChecked(true);
            //this.mPasswordConfirm.setText(this.mAccount.getPassword());
        } else {
            //this.mRegisterNew.setVisibility(View.GONE);
            //this.mRegisterNew.setChecked(false);
        }
        if (this.mAccount.isOnlineAndConnected() && !this.mFetchingAvatar) {
            LOGD(TAG, UIHelper.readableTimeDifferenceFull(this, this.mAccount.getXmppConnection().getLastSessionEstablished()));
            XmppConnection.Features features = this.mAccount.getXmppConnection().getFeatures();
            if (features.rosterVersioning()) {
                LOGD(TAG, getString(R.string.server_info_available));
                //this.mServerInfoRosterVersion.setText(R.string.server_info_available);
            } else {
                LOGD(TAG, getString(R.string.server_info_unavailable));
                //this.mServerInfoRosterVersion.setText(R.string.server_info_unavailable);
            }
            if (features.carbons()) {
                LOGD(TAG, getString(R.string.server_info_available));
                //this.mServerInfoCarbons.setText(R.string.server_info_available);
            } else {
                LOGD(TAG, getString(R.string.server_info_unavailable));
                //this.mServerInfoCarbons.setText(R.string.server_info_unavailable);
            }
            if (features.mam()) {
                LOGD(TAG, getString(R.string.server_info_available));
                //this.mServerInfoMam.setText(R.string.server_info_available);
            } else {
                LOGD(TAG, getString(R.string.server_info_unavailable));
                //this.mServerInfoMam.setText(R.string.server_info_unavailable);
            }
            if (features.csi()) {
                LOGD(TAG, getString(R.string.server_info_available));
                //this.mServerInfoCSI.setText(R.string.server_info_available);
            } else {
                LOGD(TAG, getString(R.string.server_info_unavailable));
                //this.mServerInfoCSI.setText(R.string.server_info_unavailable);
            }
            if (features.blocking()) {
                LOGD(TAG, getString(R.string.server_info_available));
                //this.mServerInfoBlocking.setText(R.string.server_info_available);
            } else {
                LOGD(TAG, getString(R.string.server_info_unavailable));
                //this.mServerInfoBlocking.setText(R.string.server_info_unavailable);
            }
            if (features.sm()) {
                LOGD(TAG, getString(R.string.server_info_available));
                //this.mServerInfoSm.setText(R.string.server_info_available);
            } else {
                LOGD(TAG, getString(R.string.server_info_unavailable));
                //this.mServerInfoSm.setText(R.string.server_info_unavailable);
            }
            if (features.pubsub()) {
                LOGD(TAG, getString(R.string.server_info_available));
                //this.mServerInfoPep.setText(R.string.server_info_available);
            } else {
                LOGD(TAG, getString(R.string.server_info_unavailable));
                //this.mServerInfoPep.setText(R.string.server_info_unavailable);
            }
            final String fingerprint = this.mAccount.getOtrFingerprint();
            if (fingerprint != null) {
                //this.mOtrFingerprintBox.setVisibility(View.VISIBLE);
                LOGD(TAG, CryptoHelper.prettifyFingerprint(fingerprint));
                //this.mOtrFingerprint.setText(CryptoHelper.prettifyFingerprint(fingerprint));
                //this.mOtrFingerprintToClipboardButton.setVisibility(View.VISIBLE);
                /*this.mOtrFingerprintToClipboardButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(final View v) {

                        if (copyTextToClipboard(fingerprint, R.string.otr_fingerprint)) {
                            Toast.makeText(
                                    EditAccountActivity.this,
                                    R.string.toast_message_otr_fingerprint,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/
            } else {
                //this.mOtrFingerprintBox.setVisibility(View.GONE);
            }
        } else {
            if (this.mAccount.errorStatus()) {
                LOGE(TAG, getString(this.mAccount.getStatus().getReadableId()));
                /*this.mAccountJid.setError(getString(this.mAccount.getStatus().getReadableId()));
                this.mAccountJid.requestFocus();*/
            }
            //this.mStats.setVisibility(View.GONE);
        }
    }

    protected boolean accountInfoEdited() {
        return (!this.mAccount.getJid().toBareJid().toString().equals("jassie2@xmpp.jp"))
                || (!this.mAccount.getPassword().equals("12345"));
    }

    private final UiCallback<Avatar> mAvatarFetchCallback = new UiCallback<Avatar>() {

        @Override
        public void userInputRequired(final PendingIntent pi, final Avatar avatar) {
            finishInitialSetup(avatar);
        }

        @Override
        public void success(final Avatar avatar) {
            finishInitialSetup(avatar);
        }

        @Override
        public void error(final int errorCode, final Avatar avatar) {
            finishInitialSetup(avatar);
        }
    };

    protected void finishInitialSetup(final Avatar avatar) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                final Intent intent;

                //TODO:What to do here?
                if (mAccount.getStatus() == Account.State.ONLINE) {

                    updateStatusUI(getString(R.string.account_status_online));
                }
                //TODO: next thing to do- check how new contact is added in convapp & when it select a user for chat


                /*if (avatar != null) {
                    *//*intent = new Intent(getApplicationContext(),
                            StartConversationActivity.class);*//*
                } else {
                    intent = new Intent(getApplicationContext(),
                            PublishProfilePictureActivity.class);
                    intent.putExtra("account", mAccount.getJid().toBareJid().toString());
                    intent.putExtra("setup", true);
                    startActivity(intent);
                }*/

                //finish();
            }
        });
    }

    private void updateStatusUI(String status) {
        if (online != null) {
            online.setText(status);
        }
    }

    @Override
    public void onAccountUpdate() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                invalidateOptionsMenu();
                if (mAccount != null
                        && mAccount.getStatus() != Account.State.ONLINE
                        && mFetchingAvatar) {
                    //SCH: No need
//                    startActivity(new Intent(getApplicationContext(),
//                            ManageAccountActivity.class));
//                    finish();
                } else if (mAccount != null && mAccount.getStatus() == Account.State.ONLINE) {
                    finishInitialSetup(null);
                    /*if (!mFetchingAvatar) {
                        mFetchingAvatar = true;
                        xmppConnectionService.checkForAvatar(mAccount,
                                mAvatarFetchCallback);
                    }*/
                } else {
                    updateSaveButton();
                }
                /*if (mAccount != null) {
                    updateAccountInformation();
                }*/
            }
        });
    }


    @Override
    void onBackendConnected() {
        this.xmppConnectionService.getNotificationService().setIsInForeground(true);

        /*if (xmppConnectionService.getAccounts().size() == 0) {*/

        setJabberIdAndConnect();
        /*}*/
        ExceptionHelper.checkForCrash(this, this.xmppConnectionService);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (this.xmppConnectionServiceBound) {
            this.onBackendConnected();
        }


        //updateActionBarNavigation();
    }

    @Override
    public void onResume() {
        super.onResume();
        //checkShowStaleDataButterBar();
    }
}