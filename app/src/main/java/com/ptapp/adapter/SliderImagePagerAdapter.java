package com.ptapp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ptapp.fragment.SliderImageFragment;

import java.io.File;

/**
 * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a
 * fragment representing an object in the collection.
 */
public class SliderImagePagerAdapter extends FragmentStatePagerAdapter {

	public static File[] lstGallery;
	private String stuRollNum;
	private String folderName;

	public SliderImagePagerAdapter(FragmentManager fm, Context context,
                                   String stuRollNum, String folderName) {
		super(fm);
		lstGallery = new ViewPagerSliderData(context).getSliderImage(
				stuRollNum, folderName);
		this.stuRollNum = stuRollNum;
		this.folderName = folderName;

	}

	@Override
	public Fragment getItem(int i) {

		Fragment fragment = new SliderImageFragment();
		Bundle args = new Bundle();
		args.putInt("position", i);
		args.putString("stuRollNum", stuRollNum);
		args.putString("folderName", folderName);
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
