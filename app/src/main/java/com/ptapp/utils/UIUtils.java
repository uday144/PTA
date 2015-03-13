package com.ptapp.utils;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.format.DateUtils;
import android.text.style.StyleSpan;
import android.view.View;

import com.ptapp.app.BuildConfig;
import com.ptapp.app.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;

/**
 * An assortment of UI helpers.
 */
public class UIUtils {
    private static final String TAG = "PTApp - UIUtils";

    private static final int[] RES_IDS_ACTION_BAR_SIZE = { android.R.attr.actionBarSize };

    /**
     * Factor applied to session color to derive the background color on panels and when
     * a session photo could not be downloaded (or while it is being downloaded)
     */
    public static final float SESSION_BG_COLOR_SCALE_FACTOR = 0.65f;
    public static final float SESSION_PHOTO_SCRIM_ALPHA = 0.75f;

    private static SimpleDateFormat sDayOfWeekFormat = new SimpleDateFormat("E");
    private static DateFormat sShortTimeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);

    /**
     * Flags used with {@link DateUtils#formatDateRange}.
     */
    private static final int TIME_FLAGS = DateUtils.FORMAT_SHOW_TIME
            | DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_ABBREV_WEEKDAY;

    public static void setAccessibilityIgnore(View view) {
        view.setClickable(false);
        view.setFocusable(false);
        view.setContentDescription("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
        }
    }

    /**
     * Given a snippet string with matching segments surrounded by curly
     * braces, turn those areas into bold spans, removing the curly braces.
     */
    public static Spannable buildStyledSnippet(String snippet) {
        final SpannableStringBuilder builder = new SpannableStringBuilder(snippet);

        // Walk through string, inserting bold snippet spans
        int startIndex, endIndex = -1, delta = 0;
        while ((startIndex = snippet.indexOf('{', endIndex)) != -1) {
            endIndex = snippet.indexOf('}', startIndex);

            // Remove braces from both sides
            builder.delete(startIndex - delta, startIndex - delta + 1);
            builder.delete(endIndex - delta - 1, endIndex - delta);

            // Insert bold style
            builder.setSpan(new StyleSpan(Typeface.BOLD),
                    startIndex - delta, endIndex - delta - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //builder.setSpan(new ForegroundColorSpan(0xff111111),
            //        startIndex - delta, endIndex - delta - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            delta += 2;
        }

        return builder;
    }

    public static boolean shouldShowLiveSessionsOnly(final Context context) {
        /*return !PrefUtils.isAttendeeAtVenue(context)
                && getCurrentTime(context) < Config.CONFERENCE_END_MILLIS;*/
        return false;
    }

    private static final long sAppLoadTime = System.currentTimeMillis();
    public static long getCurrentTime(final Context context) {
        if (BuildConfig.DEBUG) {
            return context.getSharedPreferences("mock_data", Context.MODE_PRIVATE)
                    .getLong("mock_current_time", System.currentTimeMillis())
                    + System.currentTimeMillis() - sAppLoadTime;
//            return ParserUtils.parseTime("2012-06-27T09:44:45.000-07:00")
//                    + System.currentTimeMillis() - sAppLoadTime;
        } else {
            return System.currentTimeMillis();
        }
    }

    public static String getLiveBadgeText(final Context context, long start, long end) {
        long now = getCurrentTime(context);

        if (now < start) {
            // Will be live later
            return context.getString(R.string.live_available);
        } else if (start <= now && now <= end) {
            // Live right now!
            // Indicated by a visual live now badge
            return "";
        } else {
            // Too late.
            return "";
        }
    }

    public static int setColorAlpha(int color, float alpha) {
        int alpha_int = Math.min(Math.max((int)(alpha * 255.0f), 0), 255);
        return Color.argb(alpha_int, Color.red(color), Color.green(color), Color.blue(color));
    }
    public static int scaleColor(int color, float factor, boolean scaleAlpha) {
        return Color.argb(scaleAlpha ? (Math.round(Color.alpha(color) * factor)) : Color.alpha(color),
                Math.round(Color.red(color) * factor), Math.round(Color.green(color) * factor),
                Math.round(Color.blue(color) * factor));
    }
    public static int scaleSessionColorToDefaultBG(int color) {
        return scaleColor(color, SESSION_BG_COLOR_SCALE_FACTOR, false);
    }

    public static String formatSessionSubtitle(long intervalStart, long intervalEnd, String roomName, StringBuilder recycle,
                                               Context context) {
        return formatSessionSubtitle(intervalStart, intervalEnd, roomName, recycle, context, false);
    }
    /**
     * Format and return the given session time and {@ Rooms} values using
     * {@link Config#CONFERENCE_TIMEZONE}.
     */
    public static String formatSessionSubtitle(long intervalStart, long intervalEnd, String roomName, StringBuilder recycle,
                                               Context context, boolean shortFormat) {

        // Determine if the session is in the past
        long currentTimeMillis = UIUtils.getCurrentTime(context);
        boolean conferenceEnded = currentTimeMillis > Config.CONFERENCE_END_MILLIS;
        boolean sessionEnded = currentTimeMillis > intervalEnd;
        if (sessionEnded && !conferenceEnded) {
            return context.getString(R.string.session_finished);
        }

        if (roomName == null) {
            roomName = context.getString(R.string.unknown_room);
        }

        if (shortFormat) {
            Date intervalStartDate = new Date(intervalStart);
            sDayOfWeekFormat.setTimeZone(SharedPrefUtil.getDisplayTimeZone(context));
            sShortTimeFormat.setTimeZone(SharedPrefUtil.getDisplayTimeZone(context));
            return sDayOfWeekFormat.format(intervalStartDate) + " "
                    + sShortTimeFormat.format(intervalStartDate);
        } else {
            return context.getString(R.string.session_subtitle,
                    formatIntervalTimeString(intervalStart, intervalEnd, recycle, context), roomName);
        }
    }

    /**
     * Format and return the given time interval using {@link Config#CONFERENCE_TIMEZONE}
     * (unless local time was explicitly requested by the user).
     */
    public static String formatIntervalTimeString(long intervalStart, long intervalEnd,
                                                  StringBuilder recycle, Context context) {
        if (recycle == null) {
            recycle = new StringBuilder();
        } else {
            recycle.setLength(0);
        }
        Formatter formatter = new Formatter(recycle);
        return DateUtils.formatDateRange(context, formatter, intervalStart, intervalEnd, TIME_FLAGS,
                SharedPrefUtil.getDisplayTimeZone(context).getID()).toString();
    }

    /** Calculates the Action Bar height in pixels. */
    public static int calculateActionBarSize(Context context) {
        if (context == null) {
            return 0;
        }

        Resources.Theme curTheme = context.getTheme();
        if (curTheme == null) {
            return 0;
        }

        TypedArray att = curTheme.obtainStyledAttributes(RES_IDS_ACTION_BAR_SIZE);
        if (att == null) {
            return 0;
        }

        float size = att.getDimension(0, 0);
        att.recycle();
        return (int) size;
    }
}
