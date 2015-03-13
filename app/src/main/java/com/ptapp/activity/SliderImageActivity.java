package com.ptapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import com.ptapp.adapter.HomeGalleryPagerAdapter;
import com.ptapp.adapter.SliderImagePagerAdapter;
import com.ptapp.app.R;
import com.ptapp.utils.CommonMethods;

import org.apache.commons.io.FilenameUtils;

import java.io.File;

public class SliderImageActivity extends BaseActivity {

	private static final String TAG = "PTAppUI - SliderImageActivity";
	String clickType;
	String rollNum;
	ViewPager vPagerGallery, vPagerFolder;
	ImageView ivMain = null;
	int i;
	File[] lstFiles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slider_image_view);

		vPagerGallery = (ViewPager) findViewById(R.id.gallery_pager);
		vPagerFolder = (ViewPager) findViewById(R.id.folder_pager);
		ivMain = (ImageView) findViewById(R.id.iv_main);

		Intent intent = getIntent();
		if (intent != null) {
			clickType = intent.getStringExtra("clickType");
			rollNum = intent.getStringExtra("stuRollNum");
			i = intent.getIntExtra("i", 0);
			lstFiles = HomeGalleryPagerAdapter.lstGallery;

			// load home img slider
			HomeGalleryPagerAdapter cpadapter = new HomeGalleryPagerAdapter(
					getSupportFragmentManager(), SliderImageActivity.this,
					rollNum);
			vPagerGallery.setAdapter(cpadapter);

			if (clickType.equalsIgnoreCase("image")) {

				vPagerFolder.setVisibility(ViewPager.GONE);

				String name = lstFiles[i].getName();
				if (name.contains("img_")) {
					name = name.replace("img_", "");
				}
				name = FilenameUtils.removeExtension(name);

				String ImgPath = CommonMethods.ImgPath + rollNum;
				File imgDir = CommonMethods.getImagesStorageDir(ImgPath);
				Uri uriImg = null;
				if (imgDir.exists()) {
					File[] arrImages = imgDir.listFiles();
					for (File item : arrImages) {
						Log.d(TAG, "itemName: " + item.getName());
						Log.d(TAG, "imgName: " + name);
						if (item.getName().equalsIgnoreCase(name)) {
							uriImg = Uri.parse(item.getPath());
							break;
						}
					}
					// BitmapFactory.decodeFile(lstFiles[i].getPath());

					ivMain.setImageURI(uriImg);
				}

			} else if (clickType.equalsIgnoreCase("folder")) {

				vPagerFolder.setVisibility(ViewPager.VISIBLE);

				String name = lstFiles[i].getName();
				if (name.contains("fol_")) {
					name = name.replace("fol_", "");
				}
				name = FilenameUtils.removeExtension(name);

				SliderImagePagerAdapter siadapter = new SliderImagePagerAdapter(
						getSupportFragmentManager(), SliderImageActivity.this,
						rollNum, name);
				vPagerFolder.setAdapter(siadapter);
			}
		} else {
			Log.i(TAG, "getIntent() is null.");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClickBackBtn(View view) {
		if (vPagerFolder.getVisibility() == ViewPager.VISIBLE) {
			vPagerFolder.setVisibility(ViewPager.GONE);
			ivMain.setImageURI(null);
		} else {
			Intent intent = new Intent(SliderImageActivity.this,
					HomeActivity.class);
			startActivity(intent);
		}
	}

}
