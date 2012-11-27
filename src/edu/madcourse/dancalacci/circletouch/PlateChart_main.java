package edu.madcourse.dancalacci.circletouch;

import edu.madcourse.dancalacci.R;
import edu.madcourse.dancalacci.multiplayer.Multiplayer_CurrentGames;
import edu.madcourse.dancalacci.multiplayer.Multiplayer_Received_Requests;
import edu.madcourse.dancalacci.multiplayer.Multiplayer_Sent_Requests;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

public class PlateChart_main extends TabActivity{
	TabHost tabHost;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.platechart_main);
		tabHost = (TabHost) findViewById(android.R.id.tabs);
		setTabs() ;
	}

	private void setTabs() {

		// Tab for add chart
		TabSpec addChartSpec = tabHost.newTabSpec("addChart");
		addChartSpec.setIndicator(this.getResources().getText(R.string.addChlart_label));
		Intent addChartIntent = new Intent(this, AddChart.class);
		addChartSpec.setContent(addChartIntent);

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
		tabHost.addTab(addChartSpec); // Adding add chart tab
		tabHost.addTab(historySpec); // Adding history tab
		tabHost.addTab(profileSpec); // Adding profile tab
		
	}

}
