package edu.madcourse.dancalacci.boggle;

import edu.madcourse.dancalacci.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class Multiplayer_New_requests extends Activity{

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.multiplayer_requests);
	}
	
	/* 
	 * Creates a list of Potential players based on Web Call
	 */
	private void generate_request_list(){
		// stuff
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
