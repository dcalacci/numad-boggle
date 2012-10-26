/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband3 for more book information.
 ***/
package edu.madcourse.dancalacci.boggle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import edu.madcourse.dancalacci.R;
import android.widget.Button;
import java.io.*;
import java.util.BitSet;


public class Boggle extends Activity {
	private static final String PREF_BOARD = "board";
	public static final String BOGGLE_PREF = "edu.madcourse.dancalacci.boggle";
	private static final String  MULTI_PREF = "edu.madcourse.dancalacci.multiplayer";
	private static final String PREF_SELECTED = "selected";
	private static final String PREF_USER = "prefUser";
	private static final String PREF_PASS = "prefPass";

	// create a new bloom filter with the optimal number of # of hash functions and bits
	// this particular combination of k and m should give us p = 1E-20 probability of a false positive

	private static final String TAG = "Boggle";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.boggle_main);

		this.setContinueButtonVis();
		setTitle("Boggle");
	}


	@Override
	protected void onResume() {
		super.onResume();
		//Music.play(this, R.raw.sudoku_main);
		this.setContinueButtonVis();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Music.stop(this);
	}

	public void OnNewGameButtonClicked(View v) {
		Intent i = new Intent(this, Game.class);
		i.putExtra(Game.TO_CONTINUE, 0);
		startActivity(i);
	}

	public void OnContinueGameButtonClicked(View v) {
		Intent i = new Intent(this, Game.class);
		i.putExtra(Game.TO_CONTINUE, 1);
		startActivity(i);
	}

	public void OnRulesButtonClicked(View v) {
		Intent i = new Intent(this, Rules.class);
		startActivity(i);
	}

	public void OnAcknowledgementsButtonClicked(View v) {
		Intent i = new Intent(this, Acknowledgements.class);
		startActivity(i);
	}

	public void OnMultiplayerButtonClicked(View v){

		if(isUserInfoAvailable()){
			Log.d(TAG, "onMultiplayerButtonClicked: new user");
			Intent intentLogin = new Intent(this, Multiplayer_Login_Form.class);
			startActivity(intentLogin);
		}else{
			Log.d(TAG, "onMultiplayerButtonClicked: exists");
			Intent intentMultiplayer = new Intent(this, Multiplayer.class);
			startActivity(intentMultiplayer);
		}
	}

	public boolean isUserInfoAvailable(){
		SharedPreferences pref = getSharedPreferences(MULTI_PREF, MODE_PRIVATE);
		String username = pref.getString(PREF_USER, "null");
		String password = pref.getString(PREF_PASS, "null");
		Log.d(TAG, "isUserInfoAvailabe: " + username);
		boolean result = (username.equalsIgnoreCase("null") && password.equalsIgnoreCase("null"));
		Log.d(TAG, "isUserInfoAvailabe: " + result);
		return result;
	}

	/**
	 * Sets the continue button's visibility based on the saved data 
	 * in the application - if there is a saved board, it shows the 
	 * continue button.  If there is not, the continue button is set to GONE.
	 */
	private void setContinueButtonVis() {
		Button continue_button = (Button)findViewById(R.id.boggle_continue_game_button);
		if (!getSharedPreferences(BOGGLE_PREF, MODE_PRIVATE).contains(PREF_BOARD)) {
			continue_button.setVisibility(View.GONE);
		} else {
			continue_button.setVisibility(0);
		}
	}

}