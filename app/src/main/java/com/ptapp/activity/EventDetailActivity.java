package com.ptapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.ptapp.dao.CalendarEventMapperDAO;
import com.ptapp.app.R;

public class EventDetailActivity extends BaseActivity {
	private static String TAG = "PTApp-EventListItemDetailActivity ";

	TextView tv_name, tv_itemDescription, tv_event_from, tv_event_to, tv_class;
	ImageView iv_photo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_detail);

		tv_name = (TextView) findViewById(R.id.name);
		tv_itemDescription = (TextView) findViewById(R.id.itemDescription);
		tv_event_from = (TextView) findViewById(R.id.tv_event_from);
		tv_event_to = (TextView) findViewById(R.id.tv_event_to);
		tv_class = (TextView) findViewById(R.id.tv_class);

		iv_photo = (ImageView) findViewById(R.id.photo);

		try {
			if (getIntent() != null) {
				String serverEventId = getIntent().getStringExtra(
						"serverEventId");
				Log.i(TAG, "id: " + serverEventId);

				CalendarEventMapperDAO cemDAO = new CalendarEventMapperDAO(
						EventDetailActivity.this);
		/*		CalendarEventMapperBean cem = cemDAO
						.getCalendarEventMapperByServerEventId(serverEventId);
				if (cem != null) {
					tv_name.setText(cem.getEventTitle());
					tv_itemDescription.setText(cem.getEventDescription());

					tv_event_from.setText("From: " + cem.getStartDatetime());
					tv_event_to.setText("To: " + cem.getEndDatetime());
					iv_photo.setImageResource(CommonMethods.getImgForEvent(cem
                            .getEvtType()));
					tv_class.setText(cem.getForClasses());

				}*/
			}
		} catch (Exception ex) {
			Log.e(TAG, ex.getMessage());
		}
	}

}
