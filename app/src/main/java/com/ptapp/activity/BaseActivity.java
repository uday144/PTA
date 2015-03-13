package com.ptapp.activity;

/**About 'static import' - http://docs.oracle.com/javase/1.5.0/docs/guide/language/static-import.html*/

import android.accounts.Account;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.ptapp.provider.PTAppContract;
import com.ptapp.app.R;
import com.ptapp.sync.PTAppDataHandler;
import com.ptapp.utils.AccountUtils;
import com.ptapp.utils.ApiMethodsUtils;
import com.ptapp.utils.CommonMethods;
import com.ptapp.utils.Config;
import com.ptapp.utils.LPreviewUtils;
import com.ptapp.utils.LPreviewUtilsBase;
import com.ptapp.utils.SharedPrefUtil;
import com.ptapp.utils.UIUtils;
import com.ptapp.widget.MultiSwipeRefreshLayout;
import com.ptapp.widget.SwipeRefreshLayout;

import java.io.IOException;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.ptapp.utils.LogUtils.LOGD;
import static com.ptapp.utils.LogUtils.LOGE;
import static com.ptapp.utils.LogUtils.LOGI;
import static com.ptapp.utils.LogUtils.LOGW;

/**
 * A base activity that handles common functionality in the app.
 */

public class BaseActivity extends FragmentActivity implements MultiSwipeRefreshLayout.CanChildScrollUpCallback {

    public static String TAG = "PTApp-BaseActivity ";


    // data bootstrap thread. Data bootstrap is the process of initializing the database
    // with the data cache that ships with the app.
    Thread mDataBootstrapThread = null;

    // When set, these components will be shown/hidden in sync with the action bar
    // to implement the "quick recall" effect (the Action Bar and the header views disappear
    // when you scroll down a list, and reappear quickly when you scroll up).
    private ArrayList<View> mHideableHeaderViews = new ArrayList<View>();
    private ArrayList<View> mScrollUpViews = new ArrayList<View>();

    // variables that control the Action Bar auto hide behavior (aka "quick recall")
    private boolean mActionBarAutoHideEnabled = false;
    private int mActionBarAutoHideSensivity = 0;
    private int mActionBarAutoHideMinY = 0;
    private int mActionBarAutoHideSignal = 0;
    private boolean mActionBarShown = true;

    // Durations for certain animations we use:
    private static final int HEADER_HIDE_ANIM_DURATION = 300;
    private static final int ACCOUNT_BOX_EXPAND_ANIM_DURATION = 200;

    // allows access to L-Preview APIs through an abstract interface so we can compile with
    // both the L Preview SDK and with the API 19 SDK
    private LPreviewUtilsBase mLPreviewUtils;

    private ObjectAnimator mStatusBarColorAnimator;

    private int mThemedStatusBarColor;
    private static final TypeEvaluator ARGB_EVALUATOR = new ArgbEvaluator();

    // Navigation drawer:
    protected DrawerLayout mDrawerLayout;
    private LPreviewUtilsBase.ActionBarDrawerToggleWrapper mDrawerToggle;

    /*private ViewGroup mDrawerItemsListContainer;*/
    protected ViewGroup mDrawerItemsListContainer;
    /*private LinearLayout mAccountListContainer;*/
    protected LinearLayout mAccountListContainer;
    protected Handler mHandler;

    // symbols for navdrawer items (indices must correspond to array below). This is
    // not a list of items that are necessarily *present* in the Nav Drawer; rather,
    // it's a list of all possible items.
    protected static final int NAVDRAWER_ITEM_MY_SCHEDULE = 0;
    protected static final int NAVDRAWER_ITEM_EXPLORE = 1;
    protected static final int NAVDRAWER_ITEM_MAP = 2;
    protected static final int NAVDRAWER_ITEM_SOCIAL = 3;
    protected static final int NAVDRAWER_ITEM_VIDEO_LIBRARY = 4;
    protected static final int NAVDRAWER_ITEM_SIGN_IN = 5;
    protected static final int NAVDRAWER_ITEM_SETTINGS = 6;
    protected static final int NAVDRAWER_ITEM_EXPERTS_DIRECTORY = 7;
    protected static final int NAVDRAWER_ITEM_PEOPLE_IVE_MET = 8;
    protected static final int NAVDRAWER_ITEM_INVALID = -1;
    protected static final int NAVDRAWER_ITEM_SEPARATOR = -2;
    protected static final int NAVDRAWER_ITEM_SEPARATOR_SPECIAL = -3;

    // titles for navdrawer items (indices must correspond to the above)
    private static final int[] NAVDRAWER_TITLE_RES_ID = new int[]{
            R.string.navdrawer_item_my_schedule,
            R.string.navdrawer_item_explore,
            R.string.navdrawer_item_map,
            R.string.navdrawer_item_social,
            R.string.navdrawer_item_video_library,
            R.string.navdrawer_item_sign_in,
            R.string.navdrawer_item_settings,
            R.string.navdrawer_item_experts_directory,
            R.string.navdrawer_item_people_ive_met
    };

    // icons for navdrawer items (indices must correspond to above array)
    private static final int[] NAVDRAWER_ICON_RES_ID = new int[]{
            R.drawable.ic_drawer_my_schedule,  // My Schedule
            R.drawable.ic_drawer_explore,  // Explore
            R.drawable.ic_drawer_map, // Map
            R.drawable.ic_drawer_social, // Social
            R.drawable.ic_drawer_video_library, // Video Library
            0, // Sign in
            R.drawable.ic_drawer_settings,
            R.drawable.ic_drawer_experts,
            R.drawable.ic_drawer_people_met,
    };

    // delay to launch nav drawer item, to allow close animation to play
    protected static final int NAVDRAWER_LAUNCH_DELAY = 250;

    // fade in and fade out durations for the main content when switching between
    // different Activities of the app through the Nav Drawer
    protected static final int MAIN_CONTENT_FADEOUT_DURATION = 150;
    private static final int MAIN_CONTENT_FADEIN_DURATION = 250;

    // list of navdrawer items that were actually added to the navdrawer, in order
    private ArrayList<Integer> mNavDrawerItems = new ArrayList<Integer>();

    // views that correspond to each navdrawer item, null if not yet created
    protected View[] mNavDrawerItemViews = null; //--

    // A Runnable that we should execute when the navigation drawer finishes its closing animation
    private Runnable mDeferredOnDrawerClosedRunnable;

    //private ImageView mExpandAccountBoxIndicator;
    protected ImageView mExpandAccountBoxIndicator;
    private boolean mAccountBoxExpanded = false;

    private int mProgressBarTopWhenActionBarShown;

    // SwipeRefreshLayout allows the user to swipe the screen down to trigger a manual refresh
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private boolean mManualSyncRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new Handler();

        if (savedInstanceState == null) {

        }

        mLPreviewUtils = LPreviewUtils.getInstance(this);

    }

    /**
     * Initializes the Action Bar auto-hide (aka Quick Recall) effect.
     */
    private void initActionBarAutoHide() {
        mActionBarAutoHideEnabled = true;
        mActionBarAutoHideMinY = getResources().getDimensionPixelSize(
                R.dimen.action_bar_auto_hide_min_y);
        mActionBarAutoHideSensivity = getResources().getDimensionPixelSize(
                R.dimen.action_bar_auto_hide_sensivity);
    }

    /**
     * Indicates that the main content has scrolled (for the purposes of showing/hiding
     * the action bar for the "action bar auto hide" effect). currentY and deltaY may be exact
     * (if the underlying view supports it) or may be approximate indications:
     * deltaY may be INT_MAX to mean "scrolled forward indeterminately" and INT_MIN to mean
     * "scrolled backward indeterminately".  currentY may be 0 to mean "somewhere close to the
     * start of the list" and INT_MAX to mean "we don't know, but not at the start of the list"
     */
    private void onMainContentScrolled(int currentY, int deltaY) {
        if (deltaY > mActionBarAutoHideSensivity) {
            deltaY = mActionBarAutoHideSensivity;
        } else if (deltaY < -mActionBarAutoHideSensivity) {
            deltaY = -mActionBarAutoHideSensivity;
        }

        if (Math.signum(deltaY) * Math.signum(mActionBarAutoHideSignal) < 0) {


            // deltaY is a motion opposite to the accumulated signal, so reset signal
            mActionBarAutoHideSignal = deltaY;
        } else {
            // add to accumulated signal
            mActionBarAutoHideSignal += deltaY;
        }

        boolean shouldShow = currentY < mActionBarAutoHideMinY ||
                (mActionBarAutoHideSignal <= -mActionBarAutoHideSensivity);
        autoShowOrHideActionBar(shouldShow);
    }

    protected void autoShowOrHideActionBar(boolean show) {
        if (show == mActionBarShown) {
            return;
        }

        mActionBarShown = show;
        //appears/disappears actionbar
        //getLPreviewUtils().showHideActionBarIfPartOfDecor(show);
        onActionBarAutoShowOrHide(show);
    }

    protected void onActionBarAutoShowOrHide(boolean shown) {
        if (mStatusBarColorAnimator != null) {
            mStatusBarColorAnimator.cancel();
        }
        mStatusBarColorAnimator = ObjectAnimator.ofInt(mLPreviewUtils, "statusBarColor",
                shown ? mThemedStatusBarColor : Color.BLACK).setDuration(250);
        mStatusBarColorAnimator.setEvaluator(ARGB_EVALUATOR);
        mStatusBarColorAnimator.start();

        //updateSwipeRefreshProgressBarTop();

        for (View view : mHideableHeaderViews) {
            if (shown) {
                view.animate()
                        .translationY(0)
                        .alpha(1)
                        .setDuration(HEADER_HIDE_ANIM_DURATION)
                        .setInterpolator(new DecelerateInterpolator());
            } else {
                view.animate()
                        .translationY(-view.getBottom())
                        .alpha(0)
                        .setDuration(HEADER_HIDE_ANIM_DURATION)
                        .setInterpolator(new DecelerateInterpolator());
            }
        }
        for (View view : mScrollUpViews) {
            if (shown) {
                Log.v(TAG, "s-view getTop: " + view.getTop());
                Log.v(TAG, "s-view getBottom: " + view.getBottom());
                view.animate()
                        .translationY(0)
                        .alpha(1)
                        .setDuration(HEADER_HIDE_ANIM_DURATION)
                        .setInterpolator(new DecelerateInterpolator());
            } else {
                Log.v(TAG, "view getTop: " + view.getTop());
                Log.v(TAG, "view getBottom: " + view.getBottom());
                view.animate()
                        //.translationY(-view.getBottom())
                        .translationY(-view.getTop())
                        .alpha(1)
                        .setDuration(HEADER_HIDE_ANIM_DURATION)
                        .setInterpolator(new DecelerateInterpolator());
                Log.v(TAG, "view paramsheight: " + view.getLayoutParams().height);
                Log.v(TAG, "height: " + view.getHeight());
                Log.v(TAG, "measured height: " + view.getMeasuredHeight());
                Log.v(TAG, "measured height and state: " + view.getMeasuredHeightAndState());
                view.getLayoutParams().height = (371 + 371) - 60;
                Log.v(TAG, "view paramsheight: " + view.getLayoutParams().height);
                Log.v(TAG, "height: " + view.getHeight());
                Log.v(TAG, "measured height: " + view.getMeasuredHeight());
                Log.v(TAG, "measured height and state: " + view.getMeasuredHeightAndState());

                //view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                /*ResizeAnimation ra = new ResizeAnimation(view, 500);
                ra.setDuration(5000);
                view.startAnimation(ra);*/
            }
        }
    }

    public class ResizeAnimation extends Animation {
        View view;
        int startH;
        int endH;
        int diff;

        public ResizeAnimation(View v, int newh) {
            view = v;
            startH = v.getLayoutParams().height;
            endH = newh;
            diff = endH - startH;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            view.getLayoutParams().height = startH + (int) (diff * interpolatedTime);
            view.requestLayout();
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }

    protected void enableActionBarAutoHide(final ListView listView) {
        initActionBarAutoHide();
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            final static int ITEMS_THRESHOLD = 0;
            int lastFvi = 0;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                onMainContentScrolled(firstVisibleItem <= ITEMS_THRESHOLD ? 0 : Integer.MAX_VALUE,
                        lastFvi - firstVisibleItem > 0 ? Integer.MIN_VALUE :
                                lastFvi == firstVisibleItem ? 0 : Integer.MAX_VALUE
                );
                lastFvi = firstVisibleItem;
            }
        });
    }

    protected void registerHideableHeaderView(View hideableHeaderView) {
        if (!mHideableHeaderViews.contains(hideableHeaderView)) {
            mHideableHeaderViews.add(hideableHeaderView);
        }
    }

    protected void deregisterHideableHeaderView(View hideableHeaderView) {
        if (mHideableHeaderViews.contains(hideableHeaderView)) {
            mHideableHeaderViews.remove(hideableHeaderView);
        }
    }

    protected void registerScrollUpView(View scrollUpView) {
        if (!mScrollUpViews.contains(scrollUpView)) {
            mScrollUpViews.add(scrollUpView);
        }
    }

    protected void deregisterScrollUpView(View scrollUpView) {
        if (mScrollUpViews.contains(scrollUpView)) {
            mScrollUpViews.remove(scrollUpView);
        }
    }

    public LPreviewUtilsBase getLPreviewUtils() {
        return mLPreviewUtils;
    }

    /**
     * Returns the navigation drawer item that corresponds to this Activity. Subclasses
     * of BaseActivity override this to indicate what nav drawer item corresponds to them
     * Return NAVDRAWER_ITEM_INVALID to mean that this Activity should not have a Nav Drawer.
     */
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_INVALID;
    }

    /**
     * Sets up the navigation drawer as appropriate. Note that the nav drawer will be
     * different depending on whether the attendee indicated that they are attending the
     * event on-site vs. attending remotely.
     */
    private void setupNavDrawer() {
        // What nav drawer item should be selected?
        int selfItem = getSelfNavDrawerItem();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout == null) {
            return;
        }
        if (selfItem == NAVDRAWER_ITEM_INVALID) {
            // do not show a nav drawer
            Log.v(TAG, "..inside do not show a nav drawer");
            View navDrawer = mDrawerLayout.findViewById(R.id.navdrawer);
            if (navDrawer != null) {
                ((ViewGroup) navDrawer.getParent()).removeView(navDrawer);
            }
            mDrawerLayout = null;
            return;
        }

        mDrawerToggle = mLPreviewUtils.setupDrawerToggle(mDrawerLayout, new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                // run deferred action, if we have one
                if (mDeferredOnDrawerClosedRunnable != null) {
                    mDeferredOnDrawerClosedRunnable.run();
                    mDeferredOnDrawerClosedRunnable = null;
                }
                if (mAccountBoxExpanded) {
                    mAccountBoxExpanded = false;
                    setupAccountBoxToggle();
                }
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                updateStatusBarForNavDrawerSlide(0f);
                onNavDrawerStateChanged(false, false);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                updateStatusBarForNavDrawerSlide(1f);
                onNavDrawerStateChanged(true, false);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                invalidateOptionsMenu();
                onNavDrawerStateChanged(isNavDrawerOpen(), newState != DrawerLayout.STATE_IDLE);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                updateStatusBarForNavDrawerSlide(slideOffset);
                onNavDrawerSlide(slideOffset);
            }
        });
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // populate the nav drawer with the correct items
        populateNavDrawer();

        mDrawerToggle.syncState();

        // When the user runs the app for the first time, we want to land them with the
        // navigation drawer open. But just the first time.
        if (SharedPrefUtil.getPrefFirstTimeLaunch(BaseActivity.this)) {
            // first run of the app starts with the nav drawer open
            mDrawerLayout.openDrawer(Gravity.START);
        }
        /*if (!PrefUtils.isWelcomeDone(this)) {
            // first run of the app starts with the nav drawer open
            PrefUtils.markWelcomeDone(this);
            mDrawerLayout.openDrawer(Gravity.START);
        }*/

    }

    private void setupAccountBoxToggle() {
        int selfItem = getSelfNavDrawerItem();
        if (mDrawerLayout == null || selfItem == NAVDRAWER_ITEM_INVALID) {
            // this Activity does not have a nav drawer
            return;
        }
        mExpandAccountBoxIndicator.setImageResource(mAccountBoxExpanded
                ? R.drawable.ic_drawer_accounts_collapse
                : R.drawable.ic_drawer_accounts_expand);
        int hideTranslateY = -mAccountListContainer.getHeight() / 4; // last 25% of animation
        if (mAccountBoxExpanded && mAccountListContainer.getTranslationY() == 0) {
            // initial setup
            mAccountListContainer.setAlpha(0);
            mAccountListContainer.setTranslationY(hideTranslateY);
        }

        AnimatorSet set = new AnimatorSet();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mDrawerItemsListContainer.setVisibility(mAccountBoxExpanded
                        ? View.INVISIBLE : View.VISIBLE);
                mAccountListContainer.setVisibility(mAccountBoxExpanded
                        ? View.VISIBLE : View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                onAnimationEnd(animation);
            }
        });

        if (mAccountBoxExpanded) {
            mAccountListContainer.setVisibility(View.VISIBLE);
            AnimatorSet subSet = new AnimatorSet();
            subSet.playTogether(
                    ObjectAnimator.ofFloat(mAccountListContainer, View.ALPHA, 1)
                            .setDuration(ACCOUNT_BOX_EXPAND_ANIM_DURATION),
                    ObjectAnimator.ofFloat(mAccountListContainer, View.TRANSLATION_Y, 0)
                            .setDuration(ACCOUNT_BOX_EXPAND_ANIM_DURATION));
            set.playSequentially(
                    ObjectAnimator.ofFloat(mDrawerItemsListContainer, View.ALPHA, 0)
                            .setDuration(ACCOUNT_BOX_EXPAND_ANIM_DURATION),
                    subSet);
            set.start();
        } else {
            mDrawerItemsListContainer.setVisibility(View.VISIBLE);
            AnimatorSet subSet = new AnimatorSet();
            subSet.playTogether(
                    ObjectAnimator.ofFloat(mAccountListContainer, View.ALPHA, 0)
                            .setDuration(ACCOUNT_BOX_EXPAND_ANIM_DURATION),
                    ObjectAnimator.ofFloat(mAccountListContainer, View.TRANSLATION_Y,
                            hideTranslateY)
                            .setDuration(ACCOUNT_BOX_EXPAND_ANIM_DURATION));
            set.playSequentially(
                    subSet,
                    ObjectAnimator.ofFloat(mDrawerItemsListContainer, View.ALPHA, 1)
                            .setDuration(ACCOUNT_BOX_EXPAND_ANIM_DURATION));
            set.start();
        }

        set.start();
    }

    private void updateStatusBarForNavDrawerSlide(float slideOffset) {
        if (mStatusBarColorAnimator != null) {
            mStatusBarColorAnimator.cancel();
        }

        if (!mActionBarShown) {
            mLPreviewUtils.setStatusBarColor(Color.BLACK);
            return;
        }

        mLPreviewUtils.setStatusBarColor((Integer) ARGB_EVALUATOR.evaluate(slideOffset,
                mThemedStatusBarColor, Color.BLACK));
    }

    // Subclasses can override this for custom behavior
    protected void onNavDrawerStateChanged(boolean isOpen, boolean isAnimating) {
        if (mActionBarAutoHideEnabled && isOpen) {
            autoShowOrHideActionBar(true);
        }
    }

    protected void onNavDrawerSlide(float offset) {
    }

    protected boolean isNavDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(Gravity.START);
    }

    /**
     * Populates the navigation drawer with the appropriate items.
     */

    private void populateNavDrawer() {
        Log.v(TAG, "..into populateNavDrawer");
        /*boolean attendeeAtVenue = PrefUtils.isAttendeeAtVenue(this);*/
        boolean attendeeAtVenue = false;
        mNavDrawerItems.clear();

        // decide which items will appear in the nav drawer
        /*if (AccountUtils.hasActiveAccount(this)) {
            // Only logged-in users can save sessions, so if there is no active account,
            // there is no My Schedule
            mNavDrawerItems.add(NAVDRAWER_ITEM_MY_SCHEDULE);
        } else {*/
        // If no active account, show Sign In
        //mNavDrawerItems.add(NAVDRAWER_ITEM_SIGN_IN);
        //}
        mNavDrawerItems.add(NAVDRAWER_ITEM_MY_SCHEDULE);

        // Explore is always shown
        mNavDrawerItems.add(NAVDRAWER_ITEM_EXPLORE);

        // If the attendee is on-site, show Map on the nav drawer
        if (attendeeAtVenue) {
            mNavDrawerItems.add(NAVDRAWER_ITEM_MAP);
        }
        mNavDrawerItems.add(NAVDRAWER_ITEM_SEPARATOR);

        // If attendee is on-site, show the People I've Met item
        if (attendeeAtVenue) {
            mNavDrawerItems.add(NAVDRAWER_ITEM_PEOPLE_IVE_MET);
        }

        // If the experts directory hasn't expired, show it
        //if (!Config.hasExpertsDirectoryExpired()) {
        mNavDrawerItems.add(NAVDRAWER_ITEM_EXPERTS_DIRECTORY);
        //}

        // Other items that are always in the nav drawer irrespective of whether the
        // attendee is on-site or remote:
        mNavDrawerItems.add(NAVDRAWER_ITEM_SOCIAL);
        mNavDrawerItems.add(NAVDRAWER_ITEM_VIDEO_LIBRARY);
        mNavDrawerItems.add(NAVDRAWER_ITEM_SEPARATOR_SPECIAL);
        mNavDrawerItems.add(NAVDRAWER_ITEM_SETTINGS);

        createNavDrawerItems();
    }

    //Subclasses can override this for custom behavior
    protected void createNavDrawerItems() {
        Log.v(TAG, "..into createNavDrawerItems");
        mDrawerItemsListContainer = (ViewGroup) findViewById(R.id.navdrawer_items_list);
        if (mDrawerItemsListContainer == null) {
            return;
        }

        mNavDrawerItemViews = new View[mNavDrawerItems.size()];
        mDrawerItemsListContainer.removeAllViews();
        int i = 0;
        for (int itemId : mNavDrawerItems) {
            mNavDrawerItemViews[i] = makeNavDrawerItem(itemId, mDrawerItemsListContainer);
            mDrawerItemsListContainer.addView(mNavDrawerItemViews[i]);
            ++i;
        }
    }

    private View makeNavDrawerItem(final int itemId, ViewGroup container) {
        Log.v(TAG, "..into makeNavDrawerItem");
        boolean selected = getSelfNavDrawerItem() == itemId;
        int layoutToInflate = 0;
        if (itemId == NAVDRAWER_ITEM_SEPARATOR) {
            layoutToInflate = R.layout.navdrawer_separator;
        } else if (itemId == NAVDRAWER_ITEM_SEPARATOR_SPECIAL) {
            layoutToInflate = R.layout.navdrawer_separator;
        } else {
            layoutToInflate = R.layout.navdrawer_item;
        }
        View view = getLayoutInflater().inflate(layoutToInflate, container, false);

        if (isSeparator(itemId)) {
            // we are done
            UIUtils.setAccessibilityIgnore(view);
            return view;
        }

        ImageView iconView = (ImageView) view.findViewById(R.id.icon);
        TextView titleView = (TextView) view.findViewById(R.id.title);
        int iconId = itemId >= 0 && itemId < NAVDRAWER_ICON_RES_ID.length ?
                NAVDRAWER_ICON_RES_ID[itemId] : 0;
        int titleId = itemId >= 0 && itemId < NAVDRAWER_TITLE_RES_ID.length ?
                NAVDRAWER_TITLE_RES_ID[itemId] : 0;

        // set icon and text
        iconView.setVisibility(iconId > 0 ? View.VISIBLE : View.GONE);
        if (iconId > 0) {
            iconView.setImageResource(iconId);
        }
        titleView.setText(getString(titleId));

        formatNavDrawerItem(view, itemId, selected);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNavDrawerItemClicked(itemId);
            }
        });

        return view;
    }

    protected boolean isSeparator(int itemId) { //--
        return itemId == NAVDRAWER_ITEM_SEPARATOR || itemId == NAVDRAWER_ITEM_SEPARATOR_SPECIAL;
    }

    private boolean isSpecialItem(int itemId) {
        return itemId == NAVDRAWER_ITEM_SETTINGS;
    }

    private void formatNavDrawerItem(View view, int itemId, boolean selected) {
        if (isSeparator(itemId)) {
            // not applicable
            return;
        }

        ImageView iconView = (ImageView) view.findViewById(R.id.icon);
        TextView titleView = (TextView) view.findViewById(R.id.title);

        // configure its appearance according to whether or not it's selected
        titleView.setTextColor(selected ?
                getResources().getColor(R.color.navdrawer_text_color_selected) :
                getResources().getColor(R.color.navdrawer_text_color));
        iconView.setColorFilter(selected ?
                getResources().getColor(R.color.navdrawer_icon_tint_selected) :
                getResources().getColor(R.color.navdrawer_icon_tint));
    }

    private void onNavDrawerItemClicked(final int itemId) {
        if (itemId == getSelfNavDrawerItem()) {
            mDrawerLayout.closeDrawer(Gravity.START);
            return;
        }

        if (isSpecialItem(itemId)) {
            goToNavDrawerItem(itemId);
        } else {
            // launch the target Activity after a short delay, to allow the close animation to play
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToNavDrawerItem(itemId);
                }
            }, NAVDRAWER_LAUNCH_DELAY);

            // change the active item on the list so the user can see the item changed
            setSelectedNavDrawerItem(itemId);
            // fade out the main content
            View mainContent = findViewById(R.id.main_content);
            if (mainContent != null) {
                mainContent.animate().alpha(0).setDuration(MAIN_CONTENT_FADEOUT_DURATION);
            }
        }

        mDrawerLayout.closeDrawer(Gravity.START);
    }

    private void goToNavDrawerItem(int item) {
        Intent intent;
        switch (item) {
            case NAVDRAWER_ITEM_MY_SCHEDULE:
                /*intent = new Intent(this, MyScheduleActivity.class);
                startActivity(intent);
                finish();*/
                Log.v(TAG, "Clicked My schedule");
                break;
            case NAVDRAWER_ITEM_EXPLORE:
                /*intent = new Intent(this, BrowseSessionsActivity.class);
                startActivity(intent);
                finish();*/
                Log.v(TAG, "Clicked Explore");
                break;
            case NAVDRAWER_ITEM_MAP:
                /*intent = new Intent(this, UIUtils.getMapActivityClass(this));
                startActivity(intent);
                finish();*/
                Log.v(TAG, "Clicked Map");
                break;
            case NAVDRAWER_ITEM_SOCIAL:
                /*intent = new Intent(this, SocialActivity.class);
                startActivity(intent);
                finish();*/
                Log.v(TAG, "Clicked Social");
                break;
            case NAVDRAWER_ITEM_EXPERTS_DIRECTORY:
                /*intent = new Intent(this, ExpertsDirectoryActivity.class);
                startActivity(intent);
                finish();*/
                Log.v(TAG, "Clicked Experts");
                break;
            case NAVDRAWER_ITEM_PEOPLE_IVE_MET:
                /*intent = new Intent(this, PeopleIveMetActivity.class);
                startActivity(intent);
                finish();*/
                Log.v(TAG, "Clicked people i ve met");
                break;
            case NAVDRAWER_ITEM_SIGN_IN:
                /*signInOrCreateAnAccount();*/
                Log.v(TAG, "Clicked sign in");
                break;
            case NAVDRAWER_ITEM_SETTINGS:
                /*intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);*/
                Log.v(TAG, "Clicked Settings");
                break;
            case NAVDRAWER_ITEM_VIDEO_LIBRARY:
                /*intent = new Intent(this, VideoLibraryActivity.class);
                startActivity(intent);
                finish();*/
                Log.v(TAG, "Clicked video library");
                break;
        }
    }

    /**
     * Sets up the given navdrawer item's appearance to the selected state. Note: this could
     * also be accomplished (perhaps more cleanly) with state-based layouts.
     */
    private void setSelectedNavDrawerItem(int itemId) {
        if (mNavDrawerItemViews != null) {
            for (int i = 0; i < mNavDrawerItemViews.length; i++) {
                if (i < mNavDrawerItems.size()) {
                    int thisItemId = mNavDrawerItems.get(i);
                    formatNavDrawerItem(mNavDrawerItemViews[i], thisItemId, itemId == thisItemId);
                }
            }
        }
    }

    /**
     * Sets up the account box. The account box is the area at the top of the nav drawer that
     * shows which account the user is logged in as, and lets them switch accounts. It also
     * shows the user's Google+ cover photo as background.
     */
    //Subclasses can override this for custom behavior
    protected void setupAccountBox() {
        mAccountListContainer = (LinearLayout) findViewById(R.id.account_list);

        if (mAccountListContainer == null) {
            //This activity does not have an account box
            return;
        }

        final View chosenAccountView = findViewById(R.id.chosen_account_view);
        /*Account chosenAccount = AccountUtils.getActiveAccount(this);
        if (chosenAccount == null) {
            // No account logged in; hide account box
            chosenAccountView.setVisibility(View.GONE);
            mAccountListContainer.setVisibility(View.GONE);
            return;
        } else {
            chosenAccountView.setVisibility(View.VISIBLE);
            mAccountListContainer.setVisibility(View.INVISIBLE);
        }

        AccountManager am = AccountManager.get(this);
        Account[] accountArray = am.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        List<Account> accounts = new ArrayList<Account>(Arrays.asList(accountArray));
        accounts.remove(chosenAccount);*/

        ImageView coverImageView = (ImageView) chosenAccountView.findViewById(R.id.profile_cover_image);
        ImageView profileImageView = (ImageView) chosenAccountView.findViewById(R.id.profile_image);
        TextView nameTextView = (TextView) chosenAccountView.findViewById(R.id.profile_name_text);
        TextView email = (TextView) chosenAccountView.findViewById(R.id.profile_email_text);
        TextView phoneTextView = (TextView) chosenAccountView.findViewById(R.id.profile_phone_text);
        mExpandAccountBoxIndicator = (ImageView) findViewById(R.id.expand_account_box_indicator);

        /*String name = AccountUtils.getPlusName(this);
        if (name == null) {
            nameTextView.setVisibility(View.GONE);
        } else {
            nameTextView.setText(name);
        }

        String imageUrl = AccountUtils.getPlusImageUrl(this);
        if (imageUrl != null) {
            mImageLoader.loadImage(imageUrl, profileImageView);
        }

        String coverImageUrl = AccountUtils.getPlusCoverUrl(this);
        if (coverImageUrl != null) {
            mImageLoader.loadImage(coverImageUrl, coverImageView);
        } else {
            coverImageView.setImageResource(R.drawable.default_cover);
        }

        email.setText(chosenAccount.name);

        if (accounts.isEmpty()) {
            // There's only one account on the device, so no need for a switcher.
            mExpandAccountBoxIndicator.setVisibility(View.GONE);
            mAccountListContainer.setVisibility(View.GONE);
            chosenAccountView.setEnabled(false);
            return;
        }*/

        chosenAccountView.setEnabled(true);

        mExpandAccountBoxIndicator.setVisibility(View.VISIBLE);
        chosenAccountView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAccountBoxExpanded = !mAccountBoxExpanded;
                setupAccountBoxToggle();
            }
        });
        setupAccountBoxToggle();

        //populateAccountList(accounts);
    }

    private void trySetupSwipeRefresh() {
        /*mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setColorScheme(
                    R.color.refresh_progress_1,
                    R.color.refresh_progress_2,
                    R.color.refresh_progress_3,
                    R.color.refresh_progress_4);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    requestDataRefresh();
                }
            });

            //need to solve.
            if (mSwipeRefreshLayout instanceof MultiSwipeRefreshLayout) {
                MultiSwipeRefreshLayout mswrl = (MultiSwipeRefreshLayout) mSwipeRefreshLayout;
                mswrl.setCanChildScrollUpCallback(this);
            }
        }*/
    }

    private void updateSwipeRefreshProgressBarTop() {
        if (mSwipeRefreshLayout == null) {
            return;
        }

        if (mActionBarShown) {
            mSwipeRefreshLayout.setProgressBarTop(mProgressBarTopWhenActionBarShown);
        } else {
            mSwipeRefreshLayout.setProgressBarTop(0);
        }
    }

    protected void requestDataRefresh() {
        Account activeAccount = AccountUtils.getActiveAccount(this);
        ContentResolver contentResolver = getContentResolver();
        if (contentResolver.isSyncActive(activeAccount, PTAppContract.CONTENT_AUTHORITY)) {
            LOGD(TAG, "Ignoring manual sync request because a sync is already in progress.");
            return;
        }
        mManualSyncRequest = true;
        LOGD(TAG, "Requesting manual data refresh.");
        //SyncHelper.requestManualSync(activeAccount);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.v(TAG, "...inside onPostCreate");

        setupNavDrawer();
        setupAccountBox();

        /*trySetupSwipeRefresh();*/
        /*updateSwipeRefreshProgressBarTop();*/

        View mainContent = findViewById(R.id.main_content);
        if (mainContent != null) {
            mainContent.setAlpha(0);
            mainContent.animate().alpha(1).setDuration(MAIN_CONTENT_FADEIN_DURATION);
        } else {
            LOGW(TAG, "No view with ID main_content to fade in.");
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mDrawerToggle != null) {
            mDrawerToggle.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (id) {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean canSwipeRefreshChildScrollUp() {
        return false;
    }

    //Educator home code ***

    /**
     * Converts an intent into a {@link Bundle} suitable for use as fragment arguments.
     */
    public static Bundle intentToFragmentArguments(Intent intent) {
        Bundle arguments = new Bundle();
        if (intent == null) {
            return arguments;
        }

        final Uri data = intent.getData();
        if (data != null) {
            arguments.putParcelable("_uri", data);
        }

        final Bundle extras = intent.getExtras();
        if (extras != null) {
            arguments.putAll(intent.getExtras());
        }

        return arguments;
    }

    /**
     * Converts a fragment arguments bundle into an intent.
     */
    public static Intent fragmentArgumentsToIntent(Bundle arguments) {
        Intent intent = new Intent();
        if (arguments == null) {
            return intent;
        }

        final Uri data = arguments.getParcelable("_uri");
        if (data != null) {
            intent.setData(data);
        }

        intent.putExtras(arguments);
        intent.removeExtra("_uri");
        return intent;
    }

    @Override
    protected void onStart() {
        LOGD(TAG, "onStart");
        super.onStart();

        //Perform api call to fetch first-time data
        if (!SharedPrefUtil.isDataBootstrapDone(this) && mDataBootstrapThread == null) {
            LOGD(TAG, "One-time data bootstrap not done yet. Doing now.");
            //no bootstrap step in this app. Get the load then fill the database.

            new FTLoadTask().execute("");
            /*performDataBootstrap();*/
        }
    }

    /**
     * Performs the one-time data bootstrap. This means taking our prepackaged conference data
     * from the R.raw.bootstrap_data resource, and parsing it to populate the database. This
     * data contains the sessions, speakers, etc.
     */
    private void performDataBootstrap(final String json) {
        final Context appContext = getApplicationContext();
        LOGD(TAG, "Starting data bootstrap background thread.");


        mDataBootstrapThread = new Thread(new Runnable() {
            @Override
            public void run() {
                LOGD(TAG, "Starting data bootstrap process.");
                try {
                    // Load data from bootstrap raw resource
                    //String bootstrapJson = JSONHandler.parseResource(appContext, R.raw.bootstrap_data3);

                    String bootstrapJson = json;

                    /*//Convert string into BO
                    StudentsBO result = new Gson().fromJson(bootstrapJson, StudentsBO.class);
                    FTLoadSH.getInstance(BaseActivity.this).processFTFeed(result);*/

                    // Apply the data we read to the database with the help of the ConferenceDataHandler
                    PTAppDataHandler dataHandler = new PTAppDataHandler(appContext);
                    dataHandler.applyConferenceData(new String[]{bootstrapJson},
                            Config.BOOTSTRAP_DATA_TIMESTAMP, false);
                    //SyncHelper.performPostSyncChores(appContext);
                    LOGI(TAG, "End of bootstrap -- successful. Marking boostrap as done.");
                    //SharedPrefUtil.markSyncSucceededNow(appContext);
                    SharedPrefUtil.markDataBootstrapDone(appContext);
                    getContentResolver().notifyChange(Uri.parse(PTAppContract.CONTENT_AUTHORITY),
                            null, false);
                    CommonMethods.takeDbBackup(appContext);
                } catch (IOException ex) {
                    // This is serious -- if this happens, the app won't work :-(
                    // This is unlikely to happen in production, but IF it does, we apply
                    // this workaround as a fallback: we pretend we managed to do the bootstrap
                    // and hope that a remote sync will work.
                    LOGE(TAG, "*** ERROR DURING BOOTSTRAP! Problem in bootstrap data?");
                    LOGE(TAG, "Applying fallback -- marking boostrap as done; sync might fix problem.");
                    SharedPrefUtil.markDataBootstrapDone(appContext);
                }

                mDataBootstrapThread = null;

                // Request a manual sync immediately after the bootstrapping process, in case we
                // have an active connection. Otherwise, the scheduled sync could take a while.
                //SyncHelper.requestManualSync(AccountUtils.getActiveAccount(appContext));
            }
        });
        mDataBootstrapThread.start();
    }


    private class FTLoadTask extends
            AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... args) {

            try {
                ApiMethodsUtils.apiFTLoad(
                        getResources().getString(R.string.first_part_api_link),
                        CommonMethods.getAppUserId(BaseActivity.this),
                        new Callback<JsonElement>() {
                            @Override
                            public void success(JsonElement jsonElement, Response response) {

                                //String json = jsonElement.getAsString();  - UnSupportedOperationException
                                String json = jsonElement.toString();
                                if (!json.isEmpty()) {
                                    LOGD(TAG, "json received: " + json);
                                    performDataBootstrap(json);
                                } else {
                                    LOGW(TAG, "json string is empty.");
                                }
                            }

                            @Override
                            public void failure(RetrofitError retrofitError) {

                                final String exMsg = retrofitError.getMessage();
                                Log.w(TAG, "Retrofit error: " + exMsg);
                                final Response r = retrofitError.getResponse();

                                //TODO:Handle Error scenarios...
                                if (r != null) {
                                    if (r.getStatus() == 401) { //OTP didn't match
                                        Log.e(TAG, "Error code: 401");
                                    }
                                    if (r.getStatus() == 404) { //Api link not found.
                                        Log.e(TAG, "Error code: 404");
                                        //showMsg.setText("Oops! :( Error encountered: 404.");
                                    }
                                } else {
                                    Log.wtf(TAG, "Unknown retrofit error: " + exMsg);
                                    //showMsg.setText("Oops! :( Error encountered.");
                                }
                            }
                        });
            } catch (Exception ex) {
                Log.w(TAG, "Retrofit error(SendOTPTask): " + ex.getMessage());
            }
            return null;
        }
    }



}
