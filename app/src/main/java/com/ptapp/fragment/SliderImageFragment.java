package com.ptapp.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ptapp.activity.AudioPlayerActivity;
import com.ptapp.adapter.SliderImagePagerAdapter;
import com.ptapp.app.R;
import com.ptapp.utils.CommonMethods;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.FilenameUtils;

import java.io.File;

public final class SliderImageFragment extends Fragment {

	private static final String TAG = "PTAppUI - SliderImageFragment";

	File[] lstGallery = null;

	public SliderImageFragment() {
		lstGallery = SliderImagePagerAdapter.lstGallery;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstance) {

		View rootView = inflater.inflate(R.layout.fragment_home_imgslider,
				container, false);
		try {
			Bundle args = getArguments();
			final int i = args.getInt("position");
			final String stuRollNum = args.getString("stuRollNum");
			final String folderName = args.getString("folderName");

			Log.d(TAG, "gal size: " + lstGallery.length);
			if (lstGallery != null) {
				final File f = lstGallery[i];

				ImageView img2 = ((ImageView) rootView.findViewById(R.id.img2));

				TextView tv_name = ((TextView) rootView
						.findViewById(R.id.tv_name));
				String name = f.getName();
				if (name.contains("vid_")) {
					name = name.replace("vid_", "");
				}
				if (name.contains("aud_")) {
					name = name.replace("aud_", "");
				}
				if (name.contains("img_")) {
					name = name.replace("img_", "");
				}

				if (name.length() > 13) {
					tv_name.setText(name.substring(0, 8) + "...");
				} else {
					tv_name.setText(name);
				}

				// Trigger loading of the URL asynchronously into imageview.
				// .fit() cannot be used with resize
				Picasso.with(getActivity()) //
						.load(f) //
						.resize(64, 64) //
						.placeholder(R.drawable.placeholder) //
						.error(R.drawable.error_image) //
						.into(img2);

				// set onclick of image in view pager.
				img2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Log.d(TAG, "file: " + f);
						// if it's video file then play video.
						if (f.getName().contains("vid_")) {
							playVideoFile(f);

						} else if (f.getName().contains("aud_")) {
							try {
								playAudioFile(f);

							} catch (Exception ex) {
								Log.e(TAG, ex.getMessage());
							}
						} else if (f.getName().contains("img_")) {
							// show Image in this activity.
							Uri uriImg = null;
							String withoutExt = FilenameUtils.removeExtension(f
                                    .getName());
							// remove the image identifier 'img_'.
							String imgName = withoutExt.replace("img_", "");

							File imgDir = CommonMethods
									.getFolderStorageDir(CommonMethods.folPath
                                            + stuRollNum + File.separatorChar
                                            + folderName);

							if (imgDir.exists()) {
								File[] arrImages = imgDir.listFiles();
								for (File item : arrImages) {

									if (item.getName()
											.equalsIgnoreCase(imgName)) {
										uriImg = Uri.parse(item.getPath());
										break;
									}
								}
							}
							ImageView ivMain = (ImageView) getActivity()
									.findViewById(R.id.iv_main);
							ivMain.setImageURI(uriImg);
						}
					}

					/**
					 * @param f
					 */
					private void playVideoFile(final File f) {
						File fileToPlay = null;
						// get the actual name with extension of video from
						// thumbnail.
						// remove the extension of image(.png) from thumbnail.
						String withoutExt = FilenameUtils.removeExtension(f
                                .getName());
						// remove the video identifier 'vid_'.
						String vidName = withoutExt.replace("vid_", "");

						File vidDir = CommonMethods
								.getFolderStorageDir(CommonMethods.folPath
                                        + stuRollNum + File.separatorChar
                                        + folderName);

						if (vidDir.exists()) {
							File[] lstFiles = vidDir.listFiles();
							for (File item : lstFiles) {
								if (item.getName().equalsIgnoreCase(vidName)) {
									fileToPlay = item;
									break;
								}
							}
							Log.i(TAG, "video to play: " + fileToPlay);

							if (fileToPlay != null) {
								// play the video, start the intent
								Intent intentToPlayVid = new Intent(
										Intent.ACTION_VIEW);
								intentToPlayVid.setDataAndType(
										Uri.parse(fileToPlay.getPath()),
										"video/*");
								startActivity(intentToPlayVid);
							}
						}
					}

					private void playAudioFile(final File f) {
						File fileToPlay = null;
						// get the actual name with extension of audio from
						// thumbnail.
						// remove the extension of image from thumbnail.
						String withoutExt = FilenameUtils.removeExtension(f
                                .getName());
						// remove the video identifier 'aud_'.
						String audName = withoutExt.replace("aud_", "");

						File audDir = CommonMethods
								.getFolderStorageDir(CommonMethods.folPath
                                        + stuRollNum + File.separatorChar
                                        + folderName);

						if (audDir.exists()) {
							File[] lstAudios = audDir.listFiles();
							for (File item : lstAudios) {
								if (item.getName().equalsIgnoreCase(audName)) {
									fileToPlay = item;
									break;
								}
							}
							Log.i(TAG, "audio to play: " + fileToPlay);

							if (fileToPlay != null) {
								// play the audio, start the intent
								Intent intent = new Intent(getActivity(),
										AudioPlayerActivity.class);
								intent.putExtra("fileUriPath",
										Uri.fromFile(fileToPlay).toString());
								startActivity(intent);
							}
						}
					}
				});

			} else {
				Log.d(TAG, "No Gallery files found.");
			}

		} catch (Exception ex) {
			Log.e(TAG, ex.getMessage());
		}
		return rootView;
	}
}
