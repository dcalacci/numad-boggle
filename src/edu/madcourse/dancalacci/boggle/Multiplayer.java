package edu.madcourse.dancalacci.boggle;

import edu.madcourse.dancalacci.R;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;

public class Multiplayer extends TabActivity {
	TabHost tabHost;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.multiplayer_main);
		tabHost = (TabHost) findViewById(android.R.id.tabhost);

		// Tab for Sent
		TabSpec currentGamesSpec = tabHost.newTabSpec("Current_Games");
		TextView currentGamesTextView = new TextView(this); 
		currentGamesSpec.setIndicator(this.getResources().getText(R.string.multiplayer_tab_label_current_games));
		Intent currentGamesIntent = new Intent(this, Multiplayer_Sent.class);
		currentGamesSpec.setContent(currentGamesIntent);

		// Tab for Received
		TabSpec receivedSpec = tabHost.newTabSpec("Received");
		receivedSpec.setIndicator(this.getResources().getText(R.string.multiplayer_tab_label_received));
		Intent receivedIntent = new Intent(this, Multiplayer_Received.class);
		receivedSpec.setContent(receivedIntent);

		// Tab for Sent
		TabSpec sentSpec = tabHost.newTabSpec("Sent");
		TextView sentTextView = new TextView(this); 
		sentSpec.setIndicator(this.getResources().getText(R.string.multiplayer_tab_label_sent));
		Intent sentIntent = new Intent(this, Multiplayer_Sent.class);
		sentSpec.setContent(sentIntent);

		// Tab for Sent
		TabSpec highscoresSpec = tabHost.newTabSpec("High Scores");
		TextView highScoresTextView = new TextView(this); 
		highscoresSpec.setIndicator(this.getResources().getText(R.string.multiplayer_tab_label_high_scores));
		Intent highScoresIntent = new Intent(this, Multiplayer_Sent.class);
		highscoresSpec.setContent(highScoresIntent);

		// Adding all TabSpec to TabHost
		tabHost.addTab(currentGamesSpec); // Adding current games tab
		tabHost.addTab(receivedSpec); // Adding received tab
		tabHost.addTab(sentSpec); // Adding sent tab
		tabHost.addTab(highscoresSpec); // Adding High Scores tab

	}

	public void OnMultiplayerBackClicked(View v) {
		finish();
	}

	public void OnMultiplayerNewRequestClicked(View v) {
		Intent i = new Intent(this, Multiplayer_New_requests.class);
		startActivity(i);
	}

}
