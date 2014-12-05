package com.example.painbuddy;

import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class Welcomescreen extends Activity implements OnTouchListener
{
	ViewGroup _root;
	ImageButton cbtButton, coinBankButton, messageCenterButton, settingsButton, penguinButton, speechBubbleButton;
	RelativeLayout screenLayout;
	ArrayList<Integer> backgroundImageList;
	ArrayList<ImageButton> movableButtonList;
	SharedPreferences sharedPrefs;
	int screenWidth, screenHeight;
	DisplayMetrics metrics = new DisplayMetrics();
	boolean draggable;
	float oldXvalue, oldYvalue = 0;
	int newXvalue, newYvalue = 0;
	long lastPressTime = -1;
	long minPressDuration = 300;
	int currentBackground = 0;
	int day, year, numDiariesToday;
	String date;//year_day format for easy substrings. CHANGE FOR STATISTICS BUTTON TO AVOID PROBLEMS WITH DISPLAYING DATA WITH DIFFERENT YEARS OR MONTHS
	Calendar cal;
	String username;
	TextView helloUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_welcomescreen);
		setupActionBar();//show the "up" button in the action bar
		
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);		
		screenWidth = this.getResources().getDisplayMetrics().widthPixels;
		screenHeight = this.getResources().getDisplayMetrics().heightPixels;
		
		//assign variables
		_root = (ViewGroup)findViewById(R.id.welcome_screen_relative_layout);
		movableButtonList = new ArrayList<ImageButton>();
		backgroundImageList = new ArrayList<Integer>();
		screenLayout = (RelativeLayout) _root;
		helloUser = (TextView) findViewById(R.id.helloUser);
		
		//set user greeting
		sharedPrefs = getSharedPreferences("painbuddy_user_settings", MODE_PRIVATE);
		username = (sharedPrefs.getString("username", "")).trim();
		if(username.equals(""))
			helloUser.setText("Hello!");
		else
			helloUser.setText("Hi " + username + "!");
		
		//assign buttons
		messageCenterButton = (ImageButton) findViewById(R.id.message_center);
		settingsButton = (ImageButton) findViewById(R.id.settings);
		cbtButton = (ImageButton) findViewById(R.id.welcome_screen_button_cbt_training);
		coinBankButton = (ImageButton) findViewById(R.id.coin_bank);
		penguinButton = (ImageButton) findViewById(R.id.welcome_penguin);
		speechBubbleButton = (ImageButton) findViewById(R.id.speech_bubble);
		
		//add desired buttons to movableButtonList
		movableButtonList.add(messageCenterButton);
		movableButtonList.add(settingsButton);
		movableButtonList.add(coinBankButton);
		movableButtonList.add(cbtButton);
		movableButtonList.add(penguinButton);
		movableButtonList.add(speechBubbleButton);
		
		//set onTouch listeners for movable buttons
		for(int i = 0; i < movableButtonList.size(); i++)
		{
			movableButtonList.get(i).setOnTouchListener(this);
			movableButtonList.get(i).setId(i);
		}
		
		sharedPrefs = this.getSharedPreferences("painbuddy_user_settings", Context.MODE_PRIVATE);
		if(!sharedPrefs.getBoolean("DefaultButtonPlacementSet", false))
		{
			setupDefaultButtonPositions();
			//toast("Default button positions set.");
		}
		
		//add backgrounds to list
		backgroundImageList.add(R.drawable.welcome1);
		backgroundImageList.add(R.drawable.welcome2);
		backgroundImageList.add(R.drawable.welcome3);
				
		currentBackground = sharedPrefs.getInt("current_background", 0);
		screenLayout.setBackgroundResource(backgroundImageList.get(currentBackground)); 
		
		//assign positions to movable buttons
		for(int i = 0; i < movableButtonList.size(); i++)
		{
			ImageButton button = movableButtonList.get(i);
			RelativeLayout.LayoutParams placement = getButtonPlacement(button);
			button.setLayoutParams(placement);
		}	    
		
		//set up date
		cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_YEAR);
        year = cal.get(Calendar.YEAR);
        date = "" + year + "_" + day;       
	}
	
	/*******************************************************
	 * START OTHER ACTIVITIES
	 */
	public void startCBT(final View view)
	{
		Animation anim = AnimationUtils.loadAnimation(Welcomescreen.this, R.anim.animation);
		cbtButton.startAnimation(anim);
		final MediaPlayer mpButtonClick = MediaPlayer.create(this, R.raw.buttonsound);
		mpButtonClick.start();
		
		Intent intent = new Intent(this, CbtTools.class);
		startActivity(intent);
	}
	
	public void startMessageCenter(final View view)
	{
		Animation anim = AnimationUtils.loadAnimation(Welcomescreen.this, R.anim.animation);
		messageCenterButton.startAnimation(anim);
		final MediaPlayer mpButtonClick = MediaPlayer.create(this, R.raw.buttonsound);
		mpButtonClick.start();
		
		if(!username.equals(""))
			toast(username + ", you have no messages.");
		else
			toast("You have no messages.");
	}
	
	public void startSettings(final View view)
	{
		Animation anim = AnimationUtils.loadAnimation(Welcomescreen.this, R.anim.animation);
		settingsButton.startAnimation(anim);
		final MediaPlayer mpButtonClick = MediaPlayer.create(this, R.raw.buttonsound);
		mpButtonClick.start();
		
		AlertDialog.Builder dialog  = new AlertDialog.Builder(Welcomescreen.this);  
        dialog.setTitle("Please input a new name.");   

        final EditText text = new EditText(getBaseContext()); 
        text.setTextColor(Color.BLACK); 
        text.setText(username);
        dialog.setView(text); 
          
        dialog.setPositiveButton("Accept", new OnClickListener() {  
            @Override
            public void onClick(DialogInterface dialog, int which) 
            { 
                // TODO Auto-generated method stub 
                username = text.getText().toString();
                if(!username.equals(""))
                {
                	helloUser.setText("Hi " + username + "!");
                	sharedPrefs = getSharedPreferences("painbuddy_user_settings", MODE_PRIVATE);
                	sharedPrefs.edit().putString("username", username).commit();
                }
                else
                {
                	helloUser.setText("Hello!");
                	sharedPrefs = getSharedPreferences("painbuddy_user_settings", MODE_PRIVATE);
                	sharedPrefs.edit().putString("username", username).commit();
                }
            } 
        }); 
        dialog.setNegativeButton("Cancel",  new OnClickListener() { 
            @Override
            public void onClick(DialogInterface dialog, int which) { 
            } 
        }); 
        AlertDialog alertDialog = dialog.create();   
        alertDialog.show(); 
	}

	public void startDiary(final View view)
	{
		Intent intent = new Intent(this, DiaryWebView.class);
		
		sharedPrefs = this.getSharedPreferences("painbuddy_diary_count", MODE_PRIVATE);
		numDiariesToday = sharedPrefs.getInt(date, 0);
		sharedPrefs.edit().putInt(date, ++numDiariesToday).commit();
		//toast("Number of diary logins today: " + numDiariesToday);
		
		startActivity(intent);
	}
	
	public void startStatistics(final View view)
	{
		Animation anim = AnimationUtils.loadAnimation(Welcomescreen.this, R.anim.animation);
		coinBankButton.startAnimation(anim);
		final MediaPlayer mpButtonClick = MediaPlayer.create(this, R.raw.buttonsound);
		mpButtonClick.start();
		
		Intent intent = new Intent(this, Statistics.class);
		startActivity(intent);
	}
	
	
	
	/*******************************************************
	 * OTHER METHODS
	 */
	private void toast(String msg) 
	{
		Toast toast = Toast.makeText (getApplicationContext(), msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 500) ;
		toast.show();
	}	
	
	private RelativeLayout.LayoutParams getButtonPlacement(ImageButton button)
	{
		int leftMargin, topMargin;
		RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(300, 300);
		sharedPrefs = this.getSharedPreferences("painbuddy_layouts", MODE_PRIVATE);
		
		leftMargin = sharedPrefs.getInt("" + button.getId()+".L", 0);
		topMargin = sharedPrefs.getInt("" + button.getId()+".T", 0);
		
		layout.leftMargin = leftMargin;
		layout.topMargin = topMargin;
		
		return layout;
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
	
	private void setupDefaultButtonPositions()
	{
		sharedPrefs = this.getSharedPreferences("painbuddy_layouts", MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPrefs.edit();
		
		editor.putInt("" + messageCenterButton.getId() + ".L", 0);//2
		editor.putInt("" + messageCenterButton.getId() + ".T", (int) (screenHeight * .5));//600
		
		editor.putInt("" + settingsButton.getId() + ".L", (int) (screenWidth * .42));
		editor.putInt("" + settingsButton.getId() + ".T", (int) (screenHeight * .637));
		
		editor.putInt("" + coinBankButton.getId() + ".L", (int) (screenWidth * .6));
		editor.putInt("" + coinBankButton.getId() + ".T", (int) (screenHeight * .51));
		
		editor.putInt("" + cbtButton.getId() + ".L", (int) (screenWidth * .185));
		editor.putInt("" + cbtButton.getId() + ".T", (int) (screenHeight * .632));
		
		editor.putInt("" + speechBubbleButton.getId() + ".L", (int) (screenWidth * .52));
		editor.putInt("" + speechBubbleButton.getId() + ".T", (int) (screenHeight * .16));
		
		editor.putInt("" + penguinButton.getId() + ".L", (int) (screenWidth * .3));
		editor.putInt("" + penguinButton.getId() + ".T", (int) (screenHeight * .36));
		editor.commit();
		
		sharedPrefs = this.getSharedPreferences("painbuddy_user_settings", MODE_PRIVATE);
		sharedPrefs.edit().putBoolean("DefaultButtonPlacementSet", true).commit();
		
		//mark first day of login
		sharedPrefs = this.getSharedPreferences("painbuddy_user_settings", MODE_PRIVATE);
		sharedPrefs.edit().putString("setup_date", date).commit();
	}
	
	
	
	/*******************************************************
	 * OVERRIDDEN METHODS
	 */
	@SuppressLint("NewApi")
	@Override
	public boolean onTouch(View v, MotionEvent ev)
	{		
		int action = ev.getAction();
		String temp;

        if (action == MotionEvent.ACTION_DOWN)
        {
            oldXvalue = ev.getX();
            oldYvalue = ev.getY();
            Log.i("TOUCH EVENT", "Action Down " + oldXvalue + "," + oldYvalue);
            if(lastPressTime == -1)
            	lastPressTime = System.currentTimeMillis();
        }
        else if (action == MotionEvent.ACTION_MOVE)
        {
        	if((System.currentTimeMillis() - lastPressTime) > minPressDuration)
        	{
        		LayoutParams params = new LayoutParams(300, 300);
        		
        		newXvalue = (int)(ev.getRawX() - 130);
        		if(newXvalue < 0)//prevents dragging over left edge
        			newXvalue = 0;
        		else if(newXvalue > (screenWidth - 290))//prevents dragging over right edge. CHECK WITH OTHER TABLET. shrinks penguin slightly. implement better version with boundary control later.
        			newXvalue = screenWidth - 290;
        		
        		newYvalue = (int)(ev.getRawY()) - 260;
        		if(newYvalue < 0)
        			newYvalue = 0;
        		else if(newYvalue > screenHeight - 450)
        			newYvalue = screenHeight - 450;
        		
	        	params.leftMargin = newXvalue;
	        	params.topMargin = newYvalue;
	        	v.setLayoutParams(params);
	        	
	        	temp = "" + v.getId() + ".L";
	        	sharedPrefs.edit().putInt(temp, params.leftMargin).commit();
	        	temp = "" + v.getId() + ".T";
	        	sharedPrefs.edit().putInt(temp, params.topMargin).commit();
        	}
        }
        else if (action == MotionEvent.ACTION_UP)
        {
        	if((System.currentTimeMillis() - lastPressTime) < minPressDuration)
        	{
        		v.callOnClick();
        	}
        	else//after a long press
        	{
        		temp = "" + v.getId() + ".L";
	        	sharedPrefs.edit().putInt(temp, newXvalue).commit();
	        	temp = "" + v.getId() + ".T";
	        	sharedPrefs.edit().putInt(temp, newYvalue).commit();
        	}
        	lastPressTime = -1;
        }
        return true;
    }
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)//Set up the {@link android.app.ActionBar}, if the API is available.
	private void setupActionBar() 
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) 
		{
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcomescreen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
		}
		return super.onOptionsItemSelected(item);
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