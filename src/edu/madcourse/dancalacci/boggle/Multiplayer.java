package edu.madcourse.dancalacci.boggle;

import edu.madcourse.dancalacci.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class Multiplayer extends TabActivity {

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiplayer_main);
 
        TabHost tabHost = getTabHost();
        int iH = getTabWidget().getLayoutParams().height;
 
        // Tab for Received
        TabSpec receivedSpec = tabHost.newTabSpec("Received");
        //receivedSpec.setIndicator(this.getResources().getText(R.string.received_Label));
        receivedSpec.setIndicator("Received", getResources().getDrawable(R.drawable.multiplayer_tabs));
        Intent receivedIntent = new Intent(this, Multiplayer_Received.class);
        receivedSpec.setContent(receivedIntent);
 
        // Tab for Sent
        TabSpec sentSpec = tabHost.newTabSpec("Sent");
        TextView sentTextView = new TextView(this); 
        //sentSpec.setIndicator(this.getResources().getText(R.string.sent_Label));
        sentSpec.setIndicator("Sent", getResources().getDrawable(R.drawable.multiplayer_tabs));
        Intent sentIntent = new Intent(this, Multiplayer_Sent.class);
        sentSpec.setContent(sentIntent);
 
        // Adding all TabSpec to TabHost
        tabHost.addTab(receivedSpec); // Adding sent tab
        tabHost.addTab(sentSpec); // Adding songs tab

    }
	
}
