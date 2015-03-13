package com.ptapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ptapp.activity.EventsMonthlyActivity;
import com.ptapp.io.model.Event;
import com.ptapp.bo.StudentContextBO;
import com.ptapp.app.R;
import com.ptapp.utils.CommonConstants;
import com.ptapp.utils.CommonMethods;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeEventsLvAdapter extends BaseAdapter {
	// private static final String TAG = "PTAppUI-HomeEventListLvAdapter";
	private static ArrayList<Event> lstEvents;
	private final Context context;
	private LayoutInflater l_Inflater;
	private StudentContextBO stuContext;

	public HomeEventsLvAdapter(Context context,
                               ArrayList<Event> listOfEvents,
                               StudentContextBO stuContext) {

		lstEvents = listOfEvents;
		l_Inflater = LayoutInflater.from(context);
		this.context = context;
		this.stuContext = stuContext;
	}

	@Override
	public int getCount() {
		return lstEvents.size();
	}

	@Override
	public Object getItem(int position) {
		return lstEvents.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = l_Inflater.inflate(R.layout.activity_home_events_listitem,
					null);

			holder = new ViewHolder();
			holder.txt_itemName = (TextView) convertView
					.findViewById(R.id.name);
			holder.txt_itemEventFrom = (TextView) convertView
					.findViewById(R.id.tv_event_from);
			holder.itemImage = (ImageView) convertView.findViewById(R.id.photo);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Date stDate = CommonMethods.getDateFromFormat(lstEvents.get(position)
                .getStartDatetime(), "yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.setTime(stDate);
		SimpleDateFormat formatter = new SimpleDateFormat("MMM dd", Locale.US);

		holder.txt_itemName.setText(lstEvents.get(position).getEventTitle());
		holder.txt_itemEventFrom.setText(formatter.format(stDate));
		Picasso.with(context)
				//
				.load(CommonMethods.getImgForEvent(lstEvents.get(position)
                        .getEvtType()))
				// .resize(64, 64) //
				.centerInside().fit().placeholder(CommonConstants.LOADING) //
				.error(CommonConstants.NO_PHOTO_AVAILABLE) //
				.into(holder.itemImage);

		// highlight the event specific to Child's class
		//String activeStuId = stuContext.getStudent().getId();
		String evForClasses = lstEvents.get(position).getForClasses();
		if (evForClasses.equalsIgnoreCase("all")) {
			// TODO: for now, do nothing

		} else {
			if (evForClasses.contains(stuContext.getSchoolClass()
					.getClassName())) {

				/*holder.itemImage.setBackgroundColor(CommonMethods
						.getChildColor(activeStuId, context));*/
				holder.itemImage.setPadding(1, 1, 1, 1);

			}
		}

		/**
		 * Set onClickListener to listen the click event of the Imageview.
		 * needed final variable to pass into the onClick method.
		 */

		final Calendar eventTime = Calendar.getInstance();
		String dateFormat = "yyyy-MM-dd HH:mm:ss";
		eventTime.setTime(CommonMethods.getDateFromFormat(
                lstEvents.get(position).getStartDatetime(), dateFormat));

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Bundle b = new Bundle();
				b.putInt("eventMonth", eventTime.get(Calendar.MONTH));
				b.putInt("eventYear", eventTime.get(Calendar.YEAR));

				// open the event detail Activity.
				Intent intent = new Intent(context, EventsMonthlyActivity.class);
				intent.putExtra("com.ptapp.activity.ShowHomeEvent", b);
				// intent.putExtra("serverEventId", serverEventId);
				// Log.v(TAG, "serverEventId tapped: " + serverEventId);
				context.startActivity(intent);
			}

		});

//		convertView.setOnTouchListener(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View view, MotionEvent event) {
//				switch (event.getAction()) {
//				case MotionEvent.ACTION_DOWN:
//					Log.d("tagcheck", "acton down");
//					view.setBackgroundColor(color.holo_blue_bright);
//					return true;
//				case MotionEvent.ACTION_UP:
//					Log.d("tagcheck", "acton up");
//					view.setBackgroundColor(color.transparent);
//					return true;
//				default:
//					view.performClick();
//					return true;
//				}
//
//				
//			}
//
//		});

		return convertView;
	}

	/**
	 * To keep reference of the views as obtained by findViewById(int id). Data
	 * is filled by the adapter/getView method corresponding to the position.
	 */
	private static class ViewHolder {
		TextView txt_itemName;
		TextView txt_itemEventFrom;
		ImageView itemImage;

	}

}
