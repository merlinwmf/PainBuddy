package com.example.painbuddy;

import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class Statistics extends Activity 
{
	Calendar cal;
	Date date;
	TextView coinCountViewer, loginCountViewer, diaryCountViewer;
	SharedPreferences sharedPrefs;
	String TextForTextView, Key, DateOfData, username;
	int numCoins, currentDayInYear, currentYear;
	int numDaysOfData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics);
	
		cal = Calendar.getInstance();
		currentDayInYear = cal.get(Calendar.DAY_OF_YEAR);
		currentYear = cal.get(Calendar.YEAR);
		
		//how many days of data to get
		sharedPrefs = this.getSharedPreferences("painbuddy_user_settings", MODE_PRIVATE);
		String setup_date = sharedPrefs.getString("setup_date", currentYear + "_" + currentDayInYear);
		int dayOfSetup = Integer.parseInt(setup_date.substring(5));
		numDaysOfData = currentDayInYear - dayOfSetup + 1;
		
				
		
		//find all TextViews
		coinCountViewer = (TextView) findViewById(R.id.coinCountViewer);
		loginCountViewer = (TextView) findViewById(R.id.loginCountViewer);
		diaryCountViewer = (TextView) findViewById(R.id.diaryCountViewer);
		
		//make login- and diary-counters scrollable
		loginCountViewer.setMovementMethod(new ScrollingMovementMethod());
		diaryCountViewer.setMovementMethod(new ScrollingMovementMethod());
		
		//set number of coins
		sharedPrefs = getSharedPreferences("painbuddy_user_settings", MODE_PRIVATE);
		numCoins = sharedPrefs.getInt("current_num_coins", 0);
		username = sharedPrefs.getString("username", "");
		if(username.equals(""))
			TextForTextView = "You have " + numCoins + " coins!";
		else
			TextForTextView = username + ", you have " + numCoins + " coins!";
		coinCountViewer.setText(TextForTextView);
		
		//get login times
		sharedPrefs = getSharedPreferences("painbuddy_login_count", MODE_PRIVATE);
		int day = currentDayInYear;
		int year = currentYear;
		int count;
		TextForTextView = "";
		for(int i = 0; i < numDaysOfData; i++)
		{
			//takes care of looking for last 7 days' data if that includes last year
			if(day == 0)
			{
				year--;
				if(year % 4 == 0)
					day = 366;
				else
					day = 365;
			}
			//get data
			Key = "" + year + "_" + day;
			count = sharedPrefs.getInt(Key, 0);
			//get and format DateOfData
			cal.set(Calendar.DAY_OF_YEAR, day);
			cal.set(Calendar.YEAR, year);
			DateOfData = (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR) + ":";
			//add extra spaces for spacing purposes
			int spacesNeeded = 12 - DateOfData.length();
			for(int j = spacesNeeded; j > 0; j--)
			{
				DateOfData+= " ";
			}
			TextForTextView += "   " + DateOfData + count + "\n";
			day--;
		}
		TextForTextView.substring(0, TextForTextView.length() - 10);//remove last newline character
		loginCountViewer.setText(TextForTextView);
		
		//get diary times
			sharedPrefs = getSharedPreferences("painbuddy_diary_count", MODE_PRIVATE);
			day = currentDayInYear;
			year = currentYear;
			TextForTextView = "";
			for(int i = 0; i < numDaysOfData; i++)
			{
				//for data that includes last year
				if(day == 0)
				{
					year--;
					if(year % 4 == 0)
						day = 366;
					else
						day = 365;
				}
				//get data
				Key = "" + year + "_" + day;
				count = sharedPrefs.getInt(Key, 0);
				//get and format DateOfData
				cal.set(Calendar.DAY_OF_YEAR, day);
				cal.set(Calendar.YEAR, year);
				DateOfData = (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR) + ":";
				//add extra spaces for spacing purposes
				int spacesNeeded = 12 - DateOfData.length();
				for(int j = spacesNeeded; j > 0; j--)
				{
					DateOfData+= " ";
				}
				TextForTextView += "   " + DateOfData + count + "\n";
				day--;
			}
			diaryCountViewer.setText(TextForTextView);
	}
	
	public void onClick(View view)
	{
		if(view.getId() == R.id.back_button_statistics)
		{
			finish();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.statistics, menu);
		return true;
	}

}
