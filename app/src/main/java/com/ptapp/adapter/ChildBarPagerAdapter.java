package com.ptapp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ptapp.bo.StudentBO;
import com.ptapp.fragment.ChildBarFragment;

import java.util.ArrayList;

/**
 * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a
 * fragment representing an object in the collection.
 */
public class ChildBarPagerAdapter extends FragmentStatePagerAdapter {

	public static ArrayList<StudentBO> lstSBInfo;

	public ChildBarPagerAdapter(FragmentManager fm, Context context,
                                ArrayList<StudentBO> listOfSBInfo) {
		super(fm);
		lstSBInfo = listOfSBInfo;
	}

	@Override
	public Fragment getItem(int i) {

		Fragment fragment = new ChildBarFragment();
		Bundle args = new Bundle();
		args.putInt("position", i);
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public int getCount() {
		return lstSBInfo.size();
	}

	@Override
	public float getPageWidth(int position) {
		return 0.2f;
	}

}
