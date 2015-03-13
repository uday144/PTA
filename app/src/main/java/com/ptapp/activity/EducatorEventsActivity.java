package com.ptapp.activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ptapp.app.R;
import com.ptapp.utils.UIUtils;
import com.ptapp.widget.CollectionView;
import com.ptapp.widget.DrawShadowFrameLayout;

import static com.ptapp.utils.LogUtils.LOGD;
import static com.ptapp.utils.LogUtils.LOGI;
import static com.ptapp.utils.LogUtils.makeLogTag;

// TODO: highlight the current happening event.(how to do)
public class EducatorEventsActivity extends BaseActivity {
    private static final String TAG = makeLogTag(EducatorEventsActivity.class);

    private DrawShadowFrameLayout mDrawShadowFrameLayout;

    // From, stop showing popup again & again, and pushing to calendar.
    boolean isEventPushedToCalendar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isFinishing()) {
            return;
        }

        setContentView(R.layout.activity_educator_events);
        mDrawShadowFrameLayout = (DrawShadowFrameLayout) findViewById(R.id.main_content);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        enableActionBarAutoHide((CollectionView) findViewById(R.id.events_collection_view));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_VIDEO_LIBRARY;
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();

        Fragment frag = getFragmentManager().findFragmentById(R.id.events_fragment);
        if (frag != null) {
            // configure events fragment's top clearance to take our overlaid controls (Action Bar
            // and spinner box) into account.
            int actionBarSize = UIUtils.calculateActionBarSize(this);
            /*int filterBarSize = getResources().getDimensionPixelSize(R.dimen.filterbar_height);*/
            int filterBarSize = 0;
            mDrawShadowFrameLayout.setShadowTopOffset(actionBarSize + filterBarSize);
            ((EducatorEventsFragment) frag).setContentTopClearance(actionBarSize + filterBarSize
                    + getResources().getDimensionPixelSize(R.dimen.explore_grid_padding));
        }
    }


    @Override
    protected void onActionBarAutoShowOrHide(boolean shown) {
        super.onActionBarAutoShowOrHide(shown);
        mDrawShadowFrameLayout.setShadowVisible(shown, shown);
    }

    @Override
    protected void onNavDrawerStateChanged(boolean isOpen, boolean isAnimating) {
        super.onNavDrawerStateChanged(isOpen, isAnimating);
        updateActionBarNavigation();
    }

    private void updateActionBarNavigation() {
        boolean show = !isNavDrawerOpen();
        if (getLPreviewUtils().shouldChangeActionBarForDrawer()) {
            ActionBar ab = getActionBar();
            ab.setDisplayShowTitleEnabled(show);
            ab.setDisplayUseLogoEnabled(!show);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.educator_events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
