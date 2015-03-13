package com.ptapp.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ptapp.app.R;

import java.util.concurrent.TimeUnit;

public class AudioPlayerActivity extends Activity {
	private static final String TAG = "PTAppUI - AudioPlayerActivity";
	SeekBar seek_bar;
	ImageButton play_button, close_icon;
	MediaPlayer player;
	Handler seekHandler = new Handler();
	TextView tv_duration, tv_audioname;

	Uri filePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// must be called b4 adding content
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_audioplayer);

		// requires API level 11.
		AudioPlayerActivity.this.setFinishOnTouchOutside(false);

		close_icon = (ImageButton) findViewById(R.id.close_button);
		close_icon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stopPlaying();
				finish();

			}

		});

		try {
			if (getIntent() != null) {
				filePath = Uri.parse(getIntent().getStringExtra("fileUriPath"));
			}

			getInit();
			seekUpdation();
			if (player != null) {
				play_button.setImageResource(R.drawable.player_pause);
				player.start();
			}
		} catch (Exception ex) {
			Log.e(TAG, ex.getMessage());
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void getInit() {
		seek_bar = (SeekBar) findViewById(R.id.seek_bar);
		play_button = (ImageButton) findViewById(R.id.play_button);
		tv_duration = (TextView) findViewById(R.id.tv_duration);
		tv_audioname = (TextView) findViewById(R.id.tv_audioname);

		play_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (player.isPlaying()) {
					play_button.setImageResource(R.drawable.player_play);
					player.pause();
				} else {
					play_button.setImageResource(R.drawable.player_pause);
					player.start();
				}
			}

		});
		// player = MediaPlayer.create(this, R.raw.aashiqui);
		Log.i(TAG, "filePath: " + filePath);
		player = MediaPlayer.create(AudioPlayerActivity.this, filePath);
		seek_bar.setMax(player.getDuration());

		int millis = player.getDuration();
		if (millis > 0) {
			// TimeUnit requires API level 9
			String dur = String.format(
					"%02d:%02d:%02d",
					TimeUnit.MILLISECONDS.toHours(millis),
					TimeUnit.MILLISECONDS.toMinutes(millis)
							- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
									.toHours(millis)),
					TimeUnit.MILLISECONDS.toSeconds(millis)
							- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
									.toMinutes(millis)));
			tv_duration.setText(dur);
			tv_audioname.setText("Schoolo Music Player");
		}
	}

	Runnable run = new Runnable() {

		@Override
		public void run() {
			seekUpdation();
		}
	};

	public void seekUpdation() {

		seek_bar.setProgress(player.getCurrentPosition());
		seekHandler.post(run);
	}

	private void stopPlaying() {
		if (player != null) {
			// without this, seekUpdation method will run again which will cause
			// NULLPointerException.
			seekHandler.removeCallbacks(run);
			player.stop();
			player.release();
			player = null;
		}
	}

}
