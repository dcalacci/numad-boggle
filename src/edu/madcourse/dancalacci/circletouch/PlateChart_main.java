package edu.madcourse.dancalacci.circletouch;

import edu.madcourse.dancalacci.R;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

public class PlateChart_main extends TabActivity implements OnTabChangeListener{
	TabHost tabHost;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.multiplayer_main);
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setOnTabChangedListener(this);
		setTabs() ;
	}

	private void setTabs() {

		// Tab for history
		TabSpec historySpec = tabHost.newTabSpec("History");
		historySpec.setIndicator(this.getResources().getText(R.string.history_label));
		Intent historyIntent = new Intent(this, History.class);
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

	public void onTabChanged(String tabId) {
		for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {
        	tabHost.getTabWidget().getChildAt(i).setBackgroundColor(getResources().getColor(R.color.Tab_Unselected));
        } 
				
		tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(getResources().getColor(R.color.Tab_Selected));
		
	}

}
