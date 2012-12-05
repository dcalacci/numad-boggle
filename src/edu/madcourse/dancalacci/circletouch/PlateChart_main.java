package edu.madcourse.dancalacci.circletouch;

import edu.madcourse.dancalacci.R;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

public class PlateChart_main extends TabActivity implements OnTabChangeListener{
	private static final String PLATE_PREF = "edu.madcourse.dancalacci.circletouch";
	private static final String PREF_INITIAL_RUN = "initial_run";

	TabHost tabHost;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.multiplayer_main); // Dont mind this
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setOnTabChangedListener(this);
	
		setTabs() ;
	}

	public void onResume(){
		super.onResume();
		
		SharedPreferences pref = getSharedPreferences(PLATE_PREF, MODE_PRIVATE);
		boolean initial_run = pref.getBoolean(PREF_INITIAL_RUN, false);

		if(initial_run == false){
			alerts();
		}

	}

	private void alerts(){

		SharedPreferences prefs = getSharedPreferences(PLATE_PREF, MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putBoolean(PREF_INITIAL_RUN, true);
		editor.commit();

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);

		// set title
		alertDialogBuilder.setTitle("Welcome!");

		// set dialog message
		alertDialogBuilder
		.setMessage(getResources().getText(R.string.welcome_instructions))
				.setCancelable(false)
				.setPositiveButton("Got it!",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
						// current activity
						vibrate(25);
						dialog.dismiss();
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}


	private void setTabs() {

		// Tab for  history
		TabSpec historySpec = tabHost.newTabSpec("History");
		historySpec.setIndicator(this.getResources().getText(R.string.history_label));
		Intent historyIntent = new Intent(this, History_byDate.class);
		historySpec.setContent(historyIntent);

		// Tab for profile
		TabSpec profileSpec = tabHost.newTabSpec("Profile"); 
		profileSpec.setIndicator(this.getResources().getText(R.string.profile_label));
		Intent profileIntent = new Intent(this, Profile.class);
		profileSpec.setContent(profileIntent);

		// Adding all TabSpec to TabHost
		tabHost.addTab(historySpec); // Adding history tab
		tabHost.addTab(profileSpec); // Adding profile tab

		for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
		{
			tabHost.getTabWidget().getChildAt(i).setBackgroundColor(getResources().getColor(R.color.Tab_Unselected));
		}
		tabHost.getTabWidget().setCurrentTab(0);
		tabHost.getTabWidget().getChildAt(0).setBackgroundColor(getResources().getColor(R.color.Tab_Selected));

	}
	
  private void vibrate(int ms) {
    Vibrator v = 
      (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    v.vibrate(ms);
  }	

	public TabHost getMyTabHost(){
		return tabHost;
	}

	public void onTabChanged(String tabId) {
		for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
		{
			tabHost.getTabWidget().getChildAt(i).setBackgroundColor(getResources().getColor(R.color.Tab_Unselected));
		} 

		tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(getResources().getColor(R.color.Tab_Selected));

	}

}
