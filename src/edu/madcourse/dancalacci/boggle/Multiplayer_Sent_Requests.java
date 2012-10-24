package edu.madcourse.dancalacci.boggle;

import java.util.ArrayList;

import edu.madcourse.dancalacci.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;

public class Multiplayer_Sent_Requests extends Activity{

	ServerAccessor sa;
	String USERNAME = "user1";
	String TAG = "Multiplayer_Sent_Requests";
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.multiplayer_requests);
		sa = new ServerAccessor();
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this, 
				android.R.id.list, 
				this.generate_request_list());
	}
	
	/* 
	 * Creates a list of Potential players based on Web Call
	 */
	private ArrayList<String> generate_request_list(){
		sa.addRequest("user1", "user2");
		Log.d(TAG, "request list: " + sa.getRequests(USERNAME).toString());
		return sa.getRequests(USERNAME);
	}
	
	
	/*	
	 * Starts new Multiplayer Boggle Game	 
	 */
	public void onMultiplayerRequestsOkButtonClicked(View v) {
		
		//Intent i = new Intent(this, .class);
		//startActivity(i);
	}
	
	public void onMultiplayerRequestsCancelButtonClicked(View v) {
		finish();
	}
}
