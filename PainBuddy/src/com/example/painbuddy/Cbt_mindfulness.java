package com.example.painbuddy;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.VideoView;

public class Cbt_mindfulness extends Activity {

	VideoView vv1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cbt_mindfulness);
		vv1 = (VideoView)findViewById(R.id.video_mindful) ;
		String urlpath = "android.resource://" + getPackageName() + "/" + R.raw.mindful;
		vv1.setVideoURI(Uri.parse(urlpath));
		vv1.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cbt_mindfulness, menu);
		return true;
	}

}
