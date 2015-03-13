package com.ptapp.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;

import com.google.gson.Gson;
import com.ptapp.activity.ConversationActivity;
import com.ptapp.bean.api.RolesBean;
import com.ptapp.bo.Role;
import com.ptapp.bo.StudentBO;
import com.ptapp.dao.EducatorDAO;
import com.ptapp.dao.ParentDAO;
import com.ptapp.entities.Conversation;
import com.ptapp.app.R;
import com.ptapp.app.SchooloApp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CommonMethods {

    private static final String TAG = "PTAppUI - CommonMethods";


    public static final String ImgProfilePath = "Schoolo/Profile";
    public static final String folPath = "Schoolo/Media/Folders/Child_";
    public static final String VidPath = "Schoolo/Media/Video/Child_";
    public static final String ImgPath = "Schoolo/Media/Images/Child_";
    public static final String AudPath = "Schoolo/Media/Audio/Child_";

    public CommonMethods() {

    }

    /**
     * Notifies UI to display a message.
     * <p/>
     * This method is defined in the common helper because it's used both by the
     * UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(CommonConstants.DISPLAY_MESSAGE_ACTION);
        intent.putExtra(CommonConstants.EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }

    // Bitmaps can quickly consume your available memory budget leading to an
    // application crash due to the dreaded exception:
    // java.lang.OutofMemoryError: bitmap size exceeds VM budget.

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
            // A power of two value is calculated because the decoder uses a
            // final value by rounding down to the nearest power of two.
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromFile(String pathName,
                                                     int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(pathName, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, options);

        // Setting the inJustDecodeBounds property to true while decoding avoids
        // memory allocation, returning null for the bitmap object but setting
        // outWidth, outHeight and outMimeType. This technique allows you to
        // read the dimensions and type of the image data prior to construction
        // (and memory allocation) of the bitmap.
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res,
                                                         int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);

        // Setting the inJustDecodeBounds property to true while decoding avoids
        // memory allocation, returning null for the bitmap object but setting
        // outWidth, outHeight and outMimeType. This technique allows you to
        // read the dimensions and type of the image data prior to construction
        // (and memory allocation) of the bitmap.
    }

    public static void createImageThumbnail(Bitmap bm, File thumb) {
        FileOutputStream out = null;
        try {

            if (bm != null) {
                out = new FileOutputStream(thumb);
                bm.compress(CompressFormat.PNG, 100, out);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }
        }
    }

    public static void createAudioIcon(Context context, File thumb) {
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.audio);
        FileOutputStream out = null;
        try {

            if (bm != null) {
                out = new FileOutputStream(thumb);
                bm.compress(CompressFormat.PNG, 100, out);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }
        }
    }

    /**
     * @param thumb   - thumb nail/icon file
     * @param context
     */
    public static void createFolderIcon(Context context, File thumb) {
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.applefolder);
        FileOutputStream out = null;
        try {

            if (bm != null) {
                out = new FileOutputStream(thumb);
                bm.compress(CompressFormat.PNG, 100, out);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }
        }
    }

    /**
     * @param vidFile - video file to create icon.
     * @param vidFile - video file to play
     * @param thumb   - thumb nail/icon file
     * @param context
     */
    public static void createVideoIcon(Context context, File vidFile, File thumb) {
        Bitmap bm = ThumbnailUtils.createVideoThumbnail(vidFile.getPath(),
                MediaStore.Video.Thumbnails.MICRO_KIND);
        FileOutputStream out = null;
        try {
            if (bm != null) {
                // LayerDrawable applied on Bitmap with custom
                // image
                // on it.
                Log.i(TAG, "context: " + context);
                Resources r = context.getResources();

                Drawable[] layers = new Drawable[2];
                layers[0] = new BitmapDrawable(r, bm);
                layers[1] = r.getDrawable(android.R.drawable.ic_media_play);
                LayerDrawable layerDrawable = new LayerDrawable(layers);

                // LayerDrawable to Drawable
                Drawable drw = CommonMethods.getSingleDrawable(context,
                        layerDrawable);

                Bitmap bmVid = CommonMethods.drawableToBitmap(drw);

                if (bmVid != null) {
                    out = new FileOutputStream(thumb);
                    bmVid.compress(CompressFormat.PNG, 100, out);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }
        }
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Log.d(TAG, "external storage is available for read and write.");
            return true;
        } else {
            Log.d(TAG, "external storage is NOT available for read and write.");
            return false;
        }
    }

    // make folder in Gallery.
    public static File getImagesStorageDir(String ImgPath) {
        // Get the directory for the user's public pictures directory.
        File f = new File(Environment.getExternalStorageDirectory(), ImgPath);
        // Create the storage directory if it does not exist
        if (!f.exists()) {
            if (!f.mkdirs()) {
                Log.d(TAG,
                        "failed to create directory. Pls check if app has permission to write and read.");
            }
        }
        return f;
    }

    public static File getImagesThumbStorageDir(String ImgPath) {
        // Get the directory for the user's pictures directory.
        File f = new File(Environment.getExternalStorageDirectory(), ImgPath
                + "/.thumbnails");
        // Create the storage directory if it does not exist
        if (!f.exists()) {
            if (!f.mkdirs()) {
                Log.d(TAG,
                        "failed to create directory. Pls check if app has permission to write and read.");
            }
        }
        return f;
    }

    public static File getVideosStorageDir(String VidPath) {
        // Get the directory for the user's public videos directory.
        File f = new File(Environment.getExternalStorageDirectory(), VidPath);
        // Create the storage directory if it does not exist
        if (!f.exists()) {
            if (!f.mkdirs()) {
                Log.d(TAG,
                        "failed to create directory. Pls check if app has permission to write and read.");
            }
        }
        return f;
    }

    public static File getVideosThumbStorageDir(String VidPath) {
        // Get the directory for the user's public pictures directory.
        File f = new File(Environment.getExternalStorageDirectory(), VidPath
                + "/.thumbnails");
        // Create the storage directory if it does not exist
        if (!f.exists()) {
            if (!f.mkdirs()) {
                Log.d(TAG,
                        "failed to create directory. Pls check if app has permission to write and read.");
            }
        }
        return f;
    }

    // Folder Directory
    public static File getFolderStorageDir(String folPath) {
        // Get the directory for the user's folder directory.
        File f = new File(Environment.getExternalStorageDirectory(), folPath);
        // Create the storage directory if it does not exist
        if (!f.exists()) {
            if (!f.mkdirs()) {
                Log.d(TAG,
                        "failed to create directory. Pls check if app has permission to write and read.");
            }
        }
        return f;
    }

    // Folder thumb nail Directory
    public static File getFolderThumbStorageDir(String folPath) {
        // Get the directory for the user's folder directory.
        File f = new File(Environment.getExternalStorageDirectory(), folPath
                + "/.thumbnails");
        // Create the storage directory if it does not exist
        if (!f.exists()) {
            if (!f.mkdirs()) {
                Log.d(TAG,
                        "failed to create directory. Pls check if app has permission to write and read.");
            }
        }
        return f;
    }

    public static File getAudiosStorageDir(String AudPath) {
        // Get the directory for the Schoolo audios.
        File f = new File(Environment.getExternalStorageDirectory(), AudPath);
        // Create the storage directory if it does not exist
        if (!f.exists()) {
            if (!f.mkdirs()) {
                Log.d(TAG,
                        "failed to create directory. Pls check if app has permission to write and read.");
            }
        }
        return f;
    }

    public static File getAudiosThumbStorageDir(String AudPath) {
        // Get the directory for the user's public pictures directory.
        File f = new File(Environment.getExternalStorageDirectory(), AudPath
                + "/.thumbnails");
        // Create the storage directory if it does not exist
        if (!f.exists()) {
            if (!f.mkdirs()) {
                Log.d(TAG,
                        "failed to create directory. Pls check if app has permission to write and read.");
            }
        }
        return f;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    // Converting LayerDrawable into BitMap:-
    // LayerDrawable -> Drawable -> BitMap
    public static Drawable getSingleDrawable(Context context,
                                             LayerDrawable layerDrawable) {
        int resourceBitmapHeight = 136;
        int resourceBitmapWidth = 153;
        float widthInInches = 0.9f;

        int widthInPixels = (int) (widthInInches * context.getResources()
                .getDisplayMetrics().densityDpi);
        int heightInPixels = widthInPixels * resourceBitmapHeight
                / resourceBitmapWidth; //

        int insetLeft = 10, insetTop = 10, insetRight = 10, insetBottom = 10;

        layerDrawable.setLayerInset(1, insetLeft, insetTop, insetRight,
                insetBottom);

        Bitmap bitmap = Bitmap.createBitmap(widthInPixels, heightInPixels,
                Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        layerDrawable.setBounds(0, 0, widthInPixels, heightInPixels);
        layerDrawable.draw(canvas);

        BitmapDrawable bitmapDrawable = new BitmapDrawable(
                context.getResources(), bitmap);
        bitmapDrawable.setBounds(0, 0, widthInPixels, heightInPixels);
        return bitmapDrawable;
    }

    /* Maybe not in use yet. */
    // url = file path or whatever suitable URL you want.
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }

    /**
     * Whether the device is connected to Internet or not
     *
     * @return true or false
     */
    public static boolean isInternetConnected(Context context, String tag) {
        boolean isConnected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            isConnected = activeNetwork != null
                    && activeNetwork.isConnectedOrConnecting();

            boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
            boolean isMobile = activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;

            if (isWiFi) {
                Log.i(tag, "WiFi is connected.");
            } else if (isMobile) {
                Log.i(tag, "Mobile data connection is connected.");
            } else {
                Log.i(tag,
                        "activeNetwork.getType() is: "
                                + activeNetwork.getType());
            }
        } catch (Exception ex) {
            // Log.e(tag, ex.getMessage());
        }

        return isConnected;
    }

    /**
     * Takes database backup to sd card, from where we can copy to pc and open
     * in SQLite viewer.
     */
    public static void takeDbBackup(Context context) {
        try {
            Log.i(TAG, "Taking Database backup...");
            File sdCard = Environment.getExternalStorageDirectory();
            // File data = Environment.getDataDirectory();

            if (sdCard.canWrite()) {
                String currentDBPath = context.getDatabasePath("PTApp.db").getPath();
                Log.v(TAG, "Database path: " + currentDBPath);
                String backupDBPath = "PTApp.db";
                File currentDB = new File(currentDBPath);
                File backupDB = new File(sdCard, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = null;
                    FileChannel dst = null;
                    try {
                        src = new FileInputStream(currentDB).getChannel();
                        dst = new FileOutputStream(backupDB).getChannel();
                        dst.transferFrom(src, 0, src.size());
                        Log.i(TAG, "Backup saved to sd card with name PTApp.db");
                    } catch (Exception ex) {
                        Log.e(TAG, ex.getMessage());
                    } finally {
                        src.close();
                        dst.close();
                    }
                } else {
                    Log.i(TAG, "current Db path not exists/found.");
                }
            } else {
                Log.i(TAG, "cannot write/save to SD-Card");
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    /** Events common methods */
    /**
     * @param evtType
     * @return
     */
    public static int getImgForEvent(String evtType) {

        if (evtType.equalsIgnoreCase("SPRT")) {
            return R.drawable.sports;
        } else if (evtType.equalsIgnoreCase("DATESHT")) {
            return R.drawable.datesht;
        } else {

            return R.drawable.calendar50x50; // return default image.
        }
    }

    /**
     * @param evtType
     * @return
     */
    public static Bitmap getBitmapForEvent(Context context, String evtType) {

        if (evtType.equalsIgnoreCase("SPRT")) {
            return BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.sports);
        } else if (evtType.equalsIgnoreCase("DATESHT")) {
            return BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.datesht);
        } else {

            return BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.calendar50x50); // return default image.
        }
    }

    /**
     * returns Date object, from date in some format
     * <p/>
     * dateFormat - format, in which the date is.
     */
    public static Date getDateFromFormat(String date, String dateFormat) {
        Date newDate = null;

        if (!date.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

            try {
                newDate = sdf.parse(date);

            } catch (ParseException ex) {
                Log.e(TAG, "date format error: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        return newDate;
    }

    // Student Image.

    /**
     * @param rollNum
     * @return File in which kid's profile image is saved.
     */
    public static File getFileImgKidProfile(String rollNum) {

        File imgFile = null;
        try {
            File profileStorageDir = new File(
                    Environment.getExternalStorageDirectory(),
                    CommonMethods.ImgProfilePath);
            // Create the storage directory if it does not exist
            if (!profileStorageDir.exists()) {
                if (!profileStorageDir.mkdirs()) {
                    Log.d(TAG, "failed to create directory");
                }
            }

            // Create a media file name
            imgFile = new File(profileStorageDir.getPath() + File.separator
                    + "Child_" + rollNum + ".jpg");
            Log.i(TAG, "imgPath: " + imgFile.getPath());

        } catch (Exception ex) {
            Log.e(TAG, "getFileImgKidProfile: " + ex.getMessage());
        }
        return imgFile;
    }

    public static void writeToMyLogFile(String text) {

        try {
            File sdCard = Environment.getExternalStorageDirectory();

            if (sdCard.canWrite()) {

                File logFile = new File(sdCard, "mylog.txt");
                FileOutputStream fos;
                byte[] data = new String(text).getBytes();

                try {
                    fos = new FileOutputStream(logFile);
                    fos.write(data);
                    fos.flush();
                    fos.close();
                    Log.i(TAG,
                            "log text has been saved to sdcard with name mylog");
                } catch (FileNotFoundException ex) {
                    Log.e(TAG, ex.getMessage());
                } catch (IOException ex) {
                    Log.e(TAG, ex.getMessage());
                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage());
                } finally {

                }
            } else {
                Log.i(TAG, "cannot write/save to SD-Card");
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }

		/*
         * try { // BufferedWriter for performance, true to set append to file
		 * flag BufferedWriter buf = new BufferedWriter(new FileWriter(logFile,
		 * true)); buf.append(text); buf.newLine(); buf.close(); } catch
		 * (IOException e) { e.printStackTrace(); }
		 */
    }

    /**
     * Shows an error dialog.
     *
     * @param message the message to show.
     * @param title   the title to show
     */
    public static void showOkDialog(final String title, final String message,
                                    Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("OK", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });
        builder.create().show();
    }

    public static String getApplicationName(Context context) {
        return context.getResources().getString(R.string.app_name);
    }

    public static int getChildColor(String studentId, Context context) {
        SharedPreferences sharedPref = SchooloApp.sharedPref;

        return sharedPref.getInt(studentId, 0);
    }

    //TODO:perhaps, this method isn't required anymore
    public static void storeAppUserId(Context context) {
        //get email from the shared preferences and check it against the table according to the selected type,
        // to get the ParentId or EducatorId, which will become the UserId to use for chatting.
        String appUserEmail = SharedPrefUtil.getPrefEmailAccount(context);
        String userType = SharedPrefUtil.getPrefUserType(context);

        if (userType.equals(CommonConstants.USER_TYPE_EDUCATOR)) {
            EducatorDAO daoEdu = new EducatorDAO(context);
            //Educator eb = daoEdu.getEducatorByEmailId(appUserEmail);

            /*if (eb != null) {
                Log.i(TAG, "Pref_app user type: " + userType);
                Log.i(TAG, "Pref_app userId: " + eb.getId());
                SharedPrefUtil.setPrefAppUserId(context, eb.getId());
            }*/
        } else if (userType.equals(CommonConstants.USER_TYPE_PARENT)) {
            ParentDAO daoPar = new ParentDAO(context);
            //ParentBean par = daoPar.getParentByEmailId(appUserEmail);
            Log.i(TAG, "userEmail: " + appUserEmail);
            /*if (par != null) {
                Log.i(TAG, "Pref_app user type: " + userType);
                Log.i(TAG, "Pref_app userId: " + par.getId());
                SharedPrefUtil.setPrefAppUserId(context, par.getId());
            }*/
        }
    }

    //close the soft keyboard
    public static void closeKeyboard(EditText editText, Context context) {
        InputMethodManager inputMgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    //Returns 'RolesBean' object after converting the JSON string stored in the shared preferences
    public static RolesBean getRolesBean(Context context) {
        return new Gson().fromJson(SharedPrefUtil.getPrefApiRolesJsonString(context), RolesBean.class);
    }

    //Returns list of roles the logged-in user has
    public static ArrayList<Role> getAppUserRoles(Context context) {
        return getRolesBean(context).getRoles();
    }

    public static ArrayList<StudentBO> getParentKids(Context context) {
        ArrayList<StudentBO> kids = null;
        ArrayList<Role> userRoles = getAppUserRoles(context);
        for (Role r : userRoles) {
            if (r.getRole().equals(CommonConstants.ROLE_PARENT)) {
                kids = r.getKids();
            }
        }
        return kids;
    }

    //Returns userId of the logged-in user
    public static int getAppUserId(Context context) {
        return getRolesBean(context).getUserId();
    }

    //Returns Jid of the logged-in user
    public static String getAppUserJid(Context context) {
        //return getRolesBean(context).getUserJid();
        return "";
    }

    public static int getSubjectResId(String courseName) {
        if (courseName.contains("Math")) {
            // ab.setLogo was not working on ICS
            return R.drawable.logo_math;
        } else if (courseName.contains("English")) {
            return R.drawable.logo_english;
        } else if (courseName.contains("French")) {
            return R.drawable.course_french;
        } else if (courseName.contains("Science")) {
            return R.drawable.course_science;
        } else if (courseName.contains("Punjabi")) {
            return R.drawable.course_punjabi;
        } else if (courseName.contains("History")) {
            return R.drawable.course_hindi;
        } else {
            return R.drawable.nophotoavailable;
        }
    }

    //TODO:Remove after testing
    /*public static void showABarCourseImage(ActionBar ab, String courseName) {
        // show subject icon
        if (courseName.contains("Math")) {
            // ab.setLogo was not working on ICS
            ab.setIcon(R.drawable.logo_math);
        } else if (courseName.contains("English")) {
            ab.setIcon(R.drawable.logo_english);
        } else if (courseName.contains("French")) {
            ab.setIcon(R.drawable.course_french);
        } else if (courseName.contains("Science")) {
            ab.setIcon(R.drawable.course_science);
        } else if (courseName.contains("Punjabi")) {
            ab.setIcon(R.drawable.course_punjabi);
        } else if (courseName.contains("History")) {
            ab.setIcon(R.drawable.course_hindi);
        } else {
            ab.setIcon(R.drawable.nophotoavailable);
        }
    }*/

    public static void switchToConversation(Context context, Conversation conversation) {
        switchToConversation(context, conversation, null, false);
    }

    public static void switchToConversation(Context context, Conversation conversation, String text,
                                     boolean newTask) {
        CommonMethods.switchToConversation(context, conversation, text, null, newTask);
    }

    public static void switchToConversation(Context context, Conversation conversation, String text, String nick, boolean newTask) {
        Intent viewConversationIntent = new Intent(context, ConversationActivity.class);
        //viewConversationIntent.setAction(Intent.ACTION_VIEW);
        viewConversationIntent.putExtra(ConversationActivity.CONVERSATION, conversation.getUuid());
        if (text != null) {
            viewConversationIntent.putExtra(ConversationActivity.TEXT, text);
        }
        if (nick != null) {
            viewConversationIntent.putExtra(ConversationActivity.NICK, nick);
        }
        viewConversationIntent.setType(ConversationActivity.VIEW_CONVERSATION);
        /*if (newTask) {
            viewConversationIntent.setFlags(viewConversationIntent.getFlags()
                    | Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        } else {
            viewConversationIntent.setFlags(viewConversationIntent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }*/
        context.startActivity(viewConversationIntent);
        //finish();
    }


}
