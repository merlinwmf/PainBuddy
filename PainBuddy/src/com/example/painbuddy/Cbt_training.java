package com.example.painbuddy;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class Cbt_training extends Activity {

	boolean drag_switch;
	ViewGroup _root;
	ImageButton ib_enter, 
	ib_exit,button_drag_switch, button_change_background;
	
	RelativeLayout layout;
	ArrayList<Integer> backgroundImgList;
	int currentBG = 0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cbt_training);
		
		drag_switch = false;
		_root = (ViewGroup)findViewById(R.id.cbt_welcome_layout_relative);
		button_drag_switch = (ImageButton)findViewById(R.id.cbt_welcome_drag_switch);
		button_change_background = (ImageButton)findViewById(R.id.cbt_welcome_change_background);
		ib_enter = (ImageButton)findViewById(R.id.ib_enter);
		ib_exit = (ImageButton)findViewById(R.id.ib_exit);
		
		//Change the background image
	    layout = (RelativeLayout)findViewById(R.id.cbt_welcome_layout_relative);
		backgroundImgList = new ArrayList<Integer>();
		backgroundImgList.add(R.drawable.cbt_welcome_background2);
		backgroundImgList.add(R.drawable.cbt_welcome_background3);
		backgroundImgList.add(R.drawable.cbt_welcome_background1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cbt_training, menu);
		return true;
	}
	
	public void onClick(View v)
	{
		if(!drag_switch)
		{
			if(v.getId() == R.id.ib_enter)
			{
				Intent intent1 = new Intent(this, CbtTools.class);
				startActivity(intent1);
				finish();
			}
			else if(v.getId() == R.id.ib_exit)
			{
				Intent intent2 = new Intent(this, Welcomescreen.class);
				startActivity(intent2);
			}
		}
	}
	
	
}
