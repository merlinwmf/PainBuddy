package com.example.painbuddy;
import java.util.ArrayList;
import java.util.Calendar;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.TextView.OnEditorActionListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Loginscreen extends Activity{
	
	EditText login;
	LinearLayout layout;
	ArrayList<Integer> backgroundImgList;
	int currentBackground = 0;
	int numLoginsToday;
	SharedPreferences sharedPrefs;
	RelativeLayout screenLayout;
	ArrayList<Integer> backgroundImageList;
	int day, year;
	String date;//year_day format for easy substrings. CHANGE FOR STATISTICS BUTTON TO AVOID PROBLEMS WITH DISPLAYING DATA WITH DIFFERENT YEARS OR MONTHS
	Calendar cal;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginscreen);
        login = (EditText)findViewById(R.id.login_edit);
		screenLayout = (RelativeLayout) (ViewGroup) findViewById(R.id.login_screen_relative_layout);
		backgroundImageList = new ArrayList<Integer>();
		cal = Calendar.getInstance();

        //To hide the keyboard
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        
        //set up date
        day = cal.get(Calendar.DAY_OF_YEAR);
        year = cal.get(Calendar.YEAR);
        date = "" + year + "_" + day;
        
        
        //currently, "current_background" field stores id of a welcome screen background, which has the painbuddy text and not the initial logo
        //THIS WORKS BUT REMOVES FIRST LOGO
        /*backgroundImageList.add(R.drawable.welcome1);
        backgroundImageList.add(R.drawable.welcome2);
        backgroundImageList.add(R.drawable.welcome3);
        sharedPrefs = this.getSharedPreferences("painbuddy_layouts", MODE_PRIVATE);
		currentBackground = sharedPrefs.getInt("current_background", 0);
		screenLayout.setBackgroundResource(backgroundImageList.get(currentBackground));*/
        
		//for "send" button on keyboard
		login.setOnEditorActionListener(new OnEditorActionListener() {
		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) 
		    {
		        boolean handled = false;
		        if (actionId == EditorInfo.IME_ACTION_SEND) 
		        {
		            attemptLogin(findViewById(R.id.login_edit));
		            handled = true;
		        }
		        return handled;
		    }
		});
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        ActionBar actionBar = getActionBar();  
        actionBar.setDisplayShowCustomEnabled(true);
        View customView=getLayoutInflater().inflate(R.layout.title_bar_pictures, null);
        getMenuInflater().inflate(R.menu.loginscreen, menu);
        actionBar.setCustomView(customView);
        return true;
    }
    
    public void attemptLogin(View view)
    {
    	final String loginName = login.getText().toString();
    	
    	if (!loginName.equals("1001"))//change to correct user later. Will multiple users use the same device?
    	{
    		toast("Please input 1001 to log in.");
		}
    	else 
    	{
    		sharedPrefs = this.getSharedPreferences("painbuddy_login_count", MODE_PRIVATE);
    		numLoginsToday = sharedPrefs.getInt(date, 0);
    		sharedPrefs.edit().putInt(date, ++numLoginsToday).commit();
    		//toast("Number of logins today: " + numLoginsToday);
    		Intent intent = new Intent("com.example.painbuddy.WELCOME_SCREEN");//Another way to do this: Intent intent = new Intent (this, Welcomescreen.class);
        	startActivity(intent);
		}
    }

	private void toast(String msg) 
	{
		Toast toast = Toast.makeText (getApplicationContext(), msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 500) ;
		toast.show();
	}
    
	 //hide the keyboard when other touch is registered
	 @Override
	 public boolean onTouchEvent(android.view.MotionEvent event) 
	 {
		 InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		 return imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
	 }
}

/*establish database connection
if(!connected)
{
	try
	{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		con = DriverManager.getConnection("jdbc:mysql://128.195.185.108:3306/painbuddy", "root", "cal2M3Xq0#aPPav");
		toast("Connection to database succeeded.");
		connected = true;
	}
	catch(Exception e)
	{
		System.out.println(e.getMessage());
		toast(e.getMessage() + "\nDevice is currently offline.");
	}

    finally
    {
            if(con != null)
            {
                    try
                    {
                            con.close();
                            System.out.println("Database Connection Terminated");
                    }
                    catch (Exception e)
                    {
                    }
            }
    }
}

}*/
