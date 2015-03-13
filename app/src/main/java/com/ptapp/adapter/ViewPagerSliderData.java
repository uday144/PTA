package com.ptapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.ptapp.utils.CommonMethods;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class ViewPagerSliderData {

	private static final String TAG = "PTAppUI - Gallery";
	private Context context;

	public ViewPagerSliderData(Context context) {
		this.context = context;
	}

	public File[] getHomeGallery(String stuRollNum) {

		// String gallPath = CommonMethods.gallPath + stuRollNum;
		String folPath = CommonMethods.folPath + stuRollNum;
		String ImgPath = CommonMethods.ImgPath + stuRollNum;
		String VidPath = CommonMethods.VidPath + stuRollNum;
		String AudPath = CommonMethods.AudPath + stuRollNum;
		File[] arrImages = null;
		File[] arrVideos = null;
		File[] arrFolders = null;
		File[] arrAudios = null;

		ArrayList<File> lstFiles = null;
		File[] arrFiles = null;

        //TODO: Handle if external storage not available
		// Images dir.
		if (CommonMethods.isExternalStorageWritable()) {
			File imgDir = CommonMethods.getImagesStorageDir(ImgPath);
			File vidDir = CommonMethods.getVideosStorageDir(VidPath);
			File folDir = CommonMethods.getFolderStorageDir(folPath);
			File audDir = CommonMethods.getAudiosStorageDir(AudPath);
			File imgthbDir = CommonMethods.getImagesThumbStorageDir(ImgPath);
			File vidthbDir = CommonMethods.getVideosThumbStorageDir(VidPath);
			File folthbDir = CommonMethods.getFolderThumbStorageDir(folPath);
			File audthbDir = CommonMethods.getAudiosThumbStorageDir(AudPath);

			arrImages = imgDir.listFiles();
			arrVideos = vidDir.listFiles();
			arrFolders = folDir.listFiles();
			arrAudios = audDir.listFiles();
			Log.d(TAG, "Number of img files:" + arrImages.length);
			Log.d(TAG, "Number of vid files:" + arrVideos.length);
			Log.d(TAG, "Number of folder files:" + arrFolders.length);
			Log.d(TAG, "Number of audio files:" + arrAudios.length);

			lstFiles = new ArrayList<File>();

			// generate thumb nail for video files with play icon on it.
			for (File item : arrVideos) {
				if (item.isFile()) {

					// 'vid_' helps in recognizing that thumb nail is of video,
					// inside viewpager.
					File thumb = new File(vidthbDir, "vid_" + item.getName()
							+ ".png");

					if (!thumb.exists()) {
						Log.d(TAG,
								"thumbnail doesn't exists, so create one for this video file.");
						// create thumb nail if it doesn't exist.
						CommonMethods.createVideoIcon(context, item, thumb);

					}
					lstFiles.add(thumb);
				}
			}

			// generate thumb nail for folder
			for (File item : arrFolders) {
				if (item.isDirectory()
						&& !item.getName().equalsIgnoreCase("thumbnails")) {

					// 'fol_' helps in recognizing that thumb nail is of folder
					// in Slider/ViewPager.
					File thumb = new File(folthbDir, "fol_" + item.getName()
							+ ".png");

					if (!thumb.exists()) {
						Log.d(TAG,
								"thumbnail doesn't exists, so create one for this folder.");
						// create thumb nail if it doesn't exist.
						CommonMethods.createFolderIcon(context, thumb);
					}
					lstFiles.add(thumb);
				}
			}

			// generate thumb nail for audio
			for (File item : arrAudios) {
				if (item.isFile()) {

					// 'aud_' helps in recognizing that thumb nail is of audio.
					File thumb = new File(audthbDir, "aud_" + item.getName()
							+ ".png");

					if (!thumb.exists()) {
						Log.d(TAG,
								"thumbnail doesn't exists, so create one for this audio file.");
						// create thumb nail if it doesn't exist.
						CommonMethods.createAudioIcon(context, thumb);
					}
					lstFiles.add(thumb);
				}
			}

			// generate thumb nail or reduced size for Images
			for (File item : arrImages) {
				if (item.isFile()) {

					// 'img_' helps in recognizing that thumb nail is of image.
					File thumb = new File(imgthbDir, "img_" + item.getName()
							+ ".png");

					if (!thumb.exists()) {
						Log.d(TAG,
								"thumbnail doesn't exists, so create one for this image file.");
						// create thumb nail if it doesn't exist.
						Bitmap bm = CommonMethods.decodeSampledBitmapFromFile(
                                item.getPath(), 100, 100);
						CommonMethods.createImageThumbnail(bm, thumb);
					}
					lstFiles.add(thumb);
				}
			}

			// add Images array to list.
			// lstFiles.addAll(Arrays.asList(arrImages));
			// Collections.sort(list)
			arrFiles = lstFiles.toArray(new File[lstFiles.size()]);
			Arrays.sort(arrFiles,
					LastModifiedFileComparator.LASTMODIFIED_REVERSE);
		}

		return arrFiles;
	}

	/**
	 * Assuming there are only audio,video or image files and no folders, inside
	 * the Album Folder.
	 */
	public File[] getSliderImage(String stuRollNum, String folderName) {

		String folPath = CommonMethods.folPath + stuRollNum
				+ File.separatorChar + folderName;

		File[] arrFolderFiles = null;

		ArrayList<File> lstFiles = null;
		File[] arrFiles = null;

		if (CommonMethods.isExternalStorageWritable()) {

			File folDir = CommonMethods.getFolderStorageDir(folPath);
			File folthbDir = CommonMethods.getFolderThumbStorageDir(folPath);

			arrFolderFiles = folDir.listFiles();
			Log.d(TAG, "Number of files in folder:" + arrFolderFiles.length);

			lstFiles = new ArrayList<File>();

			for (File item : arrFolderFiles) {
				if (item.isFile()) {
					// get the extension of the file, to check if it's video,
					// audio or a image.
					String ext = FilenameUtils.getExtension(item.getName());

					// generate thumb nail for video files with play icon on it.
					if (ext.equalsIgnoreCase("flv")
							|| ext.equalsIgnoreCase("mp4")
							|| ext.equalsIgnoreCase("mkv")
							|| ext.equalsIgnoreCase("3gp")) {
						// 'vid_' helps in recognizing that thumb nail is of
						// video.
						File thumb = new File(folthbDir, "vid_"
								+ item.getName() + ".png");

						if (!thumb.exists()) {
							Log.d(TAG,
									"thumbnail doesn't exists, so create one for this video file.");
							// create thumb nail if it doesn't exist.
							CommonMethods.createVideoIcon(context, item, thumb);
						}
						lstFiles.add(thumb);

					} else if (ext.equalsIgnoreCase("png")
							|| ext.equalsIgnoreCase("jpg")
							|| ext.equalsIgnoreCase("jpeg")) {

						// 'img_' helps in recognizing that thumb nail is of
						// image.
						File thumb = new File(folthbDir, "img_"
								+ item.getName() + ".png");

						if (!thumb.exists()) {
							Log.d(TAG,
									"thumbnail doesn't exists, so create one for this image file.");
							// create thumb nail if it doesn't exist.
							// TODO: Change hardcoded size 100 by 100 to width
							// of the imageview.
							Bitmap bm = CommonMethods
									.decodeSampledBitmapFromFile(
                                            item.getPath(), 100, 100);
							CommonMethods.createImageThumbnail(bm, thumb);
						}
						lstFiles.add(thumb);

					} else if (ext.equalsIgnoreCase("mp3")) {

						// 'aud_' helps in recognizing that thumb nail is of
						// audio.
						File thumb = new File(folthbDir, "aud_"
								+ item.getName() + ".png");

						if (!thumb.exists()) {
							Log.d(TAG,
									"thumbnail doesn't exists, so create one for this audio file.");
							// create thumb nail if it doesn't exist.
							CommonMethods.createAudioIcon(context, thumb);
						}
						lstFiles.add(thumb);

					}
				}
			}

			arrFiles = lstFiles.toArray(new File[lstFiles.size()]);
			// sort by last modified - latest first.
			Arrays.sort(arrFiles,
					LastModifiedFileComparator.LASTMODIFIED_REVERSE);
		}

		return arrFiles;
	}
}