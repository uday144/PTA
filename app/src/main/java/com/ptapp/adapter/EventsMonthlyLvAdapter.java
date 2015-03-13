package com.ptapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ptapp.activity.EventDetailActivity;
import com.ptapp.io.model.Event;
import com.ptapp.bo.StudentBO;
import com.ptapp.app.R;
import com.ptapp.utils.CommonMethods;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EventsMonthlyLvAdapter extends BaseAdapter {
	private static final String TAG = "PTAppUI-EventListLvAdapter";
	private static ArrayList<Event> lstEvents;
	private LayoutInflater l_Inflater;
	private final Context context;

	ArrayList<StudentBO> lstChildInfo;

	public EventsMonthlyLvAdapter(Context context,
                                  ArrayList<Event> listOfEvents,
                                  ArrayList<StudentBO> listOfChildInfo) {
		lstEvents = listOfEvents;
		l_Inflater = LayoutInflater.from(context);
		this.context = context;

		try {

			lstChildInfo = listOfChildInfo;

		} catch (Exception ex) {
			if (ex.getMessage() != null) {
				Log.e(TAG, ex.getMessage());
			} else {
				Log.e(TAG, "No exception message for this error.");
			}
		}

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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = l_Inflater.inflate(R.layout.activity_monthly_events_listitem, null);
			holder = new ViewHolder();
			holder.txt_itemName = (TextView) convertView
					.findViewById(R.id.name);
			holder.txt_itemDescription = (TextView) convertView
					.findViewById(R.id.itemDescription);
			holder.txt_itemEventFrom = (TextView) convertView
					.findViewById(R.id.tv_event_from);
			holder.txt_itemClass = (TextView) convertView
					.findViewById(R.id.tv_class);
			holder.itemImage = (ImageView) convertView.findViewById(R.id.photo);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.txt_itemName.setText(lstEvents.get(position).getEventTitle());
		holder.txt_itemDescription.setText(lstEvents.get(position)
				.getEventDescription());

		Date stDate = CommonMethods.getDateFromFormat(lstEvents.get(position)
                .getStartDatetime(), "yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.setTime(stDate);
		SimpleDateFormat formatter = new SimpleDateFormat(
				"MMM dd, yyyy HH:mm aa EEE", Locale.US);
		holder.txt_itemEventFrom.setText(formatter.format(stDate));

		holder.itemImage.setImageResource(CommonMethods
				.getImgForEvent(lstEvents.get(position).getEvtType()));
		// holder.txt_itemClass.setText(lstEvents.get(position).getForClasses());

		holder.txt_itemClass.setText("");
		String evForClasses = lstEvents.get(position).getForClasses();
		if (evForClasses.equalsIgnoreCase("all")) {
			// for now, do nothing
			holder.txt_itemClass.setText("all");
		} else {
			if (lstChildInfo.size() > 0) {

				for (StudentBO item : lstChildInfo) {

					if (evForClasses.contains(item.getStuClass())) {

						if (holder.txt_itemClass.length() > 0) {
							holder.txt_itemClass.append(", "
									+ Character.toString(item.getName().charAt(
											0)));
						} else {
							holder.txt_itemClass.setText(Character
									.toString(item.getName().charAt(0)));
						}
					}
				}
			}
		}

		final String serverEventId = lstEvents.get(position).getServerEventId();
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// open the event detail Activity.
				Intent intent = new Intent(context, EventDetailActivity.class);
				intent.putExtra("serverEventId", serverEventId);
				Log.v(TAG, "serverEventId tapped: " + serverEventId);
				context.startActivity(intent);
			}

		});
		return convertView;
	}

	/**
	 * To keep reference of the views as obtained by findViewById(int id). Data
	 * is filled by the adapter/getView method corresponding to the position.
	 */
	private static class ViewHolder {
		TextView txt_itemName;
		TextView txt_itemDescription;
		TextView txt_itemEventFrom;
		TextView txt_itemClass;
		ImageView itemImage;
	}

}
