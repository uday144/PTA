package com.ptapp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ptapp.fragment.HomeSliderFragment;

import java.io.File;

/**
 * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a
 * fragment representing an object in the collection.
 */
public class HomeGalleryPagerAdapter extends FragmentStatePagerAdapter {

	public static File[] lstGallery;
	private String stuRollNum = "";

	public HomeGalleryPagerAdapter(FragmentManager fm, Context context,
                                   String stuRollNum) {
		super(fm);
		lstGallery = new ViewPagerSliderData(context)
				.getHomeGallery(stuRollNum);

		this.stuRollNum = stuRollNum;

	}

	@Override
	public Fragment getItem(int i) {

		Fragment fragment = new HomeSliderFragment();
		Bundle args = new Bundle();
		args.putInt("position", i);
		args.putString("stuRollNum", stuRollNum);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getCount() {
		return lstGallery.length;
	}

	@Override
	public float getPageWidth(int position) {
		return 0.2f;
	}

}
