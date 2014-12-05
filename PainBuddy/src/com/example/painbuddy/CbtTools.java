package com.example.painbuddy;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class CbtTools extends Activity {

	ImageButton ib1,ib2,ib3,ib4,ib5;
	ArrayList<Integer> backgroundImageList;
	ViewGroup screenLayout;
	int currentBackground;
	SharedPreferences sharedPrefs;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cbt_training_age);
		ib1 = (ImageButton)findViewById(R.id.cbt_mindfulness);
		ib2 = (ImageButton)findViewById(R.id.cbt_guided_imagery);
		ib3 = (ImageButton)findViewById(R.id.cbt_distraction_tech);
		ib4 = (ImageButton)findViewById(R.id.cbt_belly_breathing);
		ib5 = (ImageButton)findViewById(R.id.cbt_body_relax);
		
		screenLayout = (ViewGroup) findViewById(R.id.cbt_syn_layout_relative);
		
		backgroundImageList = new ArrayList<Integer>();
		//add backgrounds to list
		backgroundImageList.add(R.drawable.cbt_welcome_background1);
		backgroundImageList.add(R.drawable.cbt_welcome_background2);
		backgroundImageList.add(R.drawable.cbt_welcome_background3);
				
		sharedPrefs = this.getSharedPreferences("painbuddy_layouts", MODE_PRIVATE);
		currentBackground = sharedPrefs.getInt("current_background", 0);
		screenLayout.setBackgroundResource(backgroundImageList.get(currentBackground)); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cbt_training_age, menu);
		return true;
	}
	
	public void onClick(View view)
	{
		if(view.getId() == (R.id.cbt_mindfulness))
		{
			Intent intent1 = new Intent(this, Cbt_mindfulness.class);
			startActivity(intent1);
		}
		else if(view.getId() == R.id.back_button)
		{
			finish();
		}
	}
	
	public void changeBackground(View v)
	{
		if(currentBackground < (backgroundImageList.size() - 1))//if the number of current background is less than the pos of the last background in the arraylist
		{
			currentBackground++;
		}
		else//reset background number
		{
			currentBackground = 0;
		}
		
		screenLayout.setBackgroundResource(backgroundImageList.get(currentBackground)); 
		sharedPrefs = this.getSharedPreferences("painbuddy_layouts", MODE_PRIVATE);
		sharedPrefs.edit().putInt("current_background", currentBackground).commit();
	}
	
	@Override
	public void onResume()
	{
		sharedPrefs = this.getSharedPreferences("painbuddy_layouts", MODE_PRIVATE);
		currentBackground = sharedPrefs.getInt("current_background", 0);
		screenLayout.setBackgroundResource(backgroundImageList.get(currentBackground));
		super.onResume();
	}
}
