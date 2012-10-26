package edu.madcourse.dancalacci.boggle;

import edu.neu.mobileclass.apis.KeyValueAPI;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import edu.madcourse.dancalacci.R;
import edu.madcourse.dancalacci.R.color;
// go_ogle_me
// googleme

public class Multiplayer_Game extends Activity {
	//constants
	// tag for this activity
	private static final 	String 	TAG 			= "Multiplayer_Game";
	public static final 	String 	MULTI_PREF 		= "edu.madcourse.dancalacci.multiplayer";
	protected static final 	String 	NO_GAME			= "no_game";
	protected static final 	String 	PREF_IWORD 		= "iword";
	protected static final 	String 	PREF_WORDLIST 	= "wordlist";
	private static final 	String 	PREF_SELECTED 	= "selected";
	protected static final 	int 	size 			= 5;

	private 	ServerAccessor 		sa 			= new ServerAccessor();
	private 	ArrayList<Boolean> 	selected 	= new ArrayList<Boolean>();
	protected 	StringBuffer 		iWord 		= new StringBuffer();
	protected	int					score_P1	= 0;
	protected	int					score_P2	= 0;
	protected	String				player1;					// Player1 Name
	protected	String				player2;					// Player2 Name
	// view for this puzzle.
	private 	Multiplayer_Game_View 	gameView;
	private 	GameWrapper 			game;
	private 	String 					username;

	////////////////////////////////////////////////////////////////////

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate");
		requestWindowFeature(Window.FEATURE_NO_TITLE); // no title

		String opponent = getIntent().getStringExtra("opponent");
		// set the layout, start the view.
		username = getSharedPreferences(MULTI_PREF, MODE_PRIVATE).getString(username, "guest");
		
		this.game = sa.getGame(username, opponent);

		// if the game exists, get it.  if not, make a new one.
		if (sa.doesGameExist(username, opponent)) {
			Log.v(TAG, "Game Exists!");
			this.game = sa.getGame(username, opponent);
		} else {
			Log.v(TAG, "game doesn't exist");
			sa.createNewGame(username, opponent);
			this.game = sa.getGame(username, opponent);
		}

		gameView = new Multiplayer_Game_View(this);

		setContentView(R.layout.multiplayer_game);
		FrameLayout boardFrame = (FrameLayout)findViewById(R.id.multiplayer_game_tiles);
		boardFrame.addView(gameView);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// get selected words and iword back
		SharedPreferences pref = getSharedPreferences(MULTI_PREF, MODE_PRIVATE);
		this.selected = this.stringToSelected((pref.getString(PREF_SELECTED, "")));
		this.iWord = new StringBuffer(pref.getString(PREF_IWORD, ""));
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");

		SharedPreferences pref = getSharedPreferences(MULTI_PREF, MODE_PRIVATE);
		SharedPreferences.Editor edit = pref.edit();
		edit.putString(PREF_SELECTED, selectedToString(selected));

		Log.d(TAG, "Current word to be saved: " +iWord.toString());
		edit.putString(PREF_IWORD, iWord.toString());
	}

	protected Boolean isWord(String word) {
		if (word.length() == 0) {
			return false;
		}
		int length = word.length();
		char c = word.charAt(0);
		AssetManager am = getAssets();
		try {
			InputStreamReader reader = 
					new InputStreamReader(am.open(length + "/" + c + ".jpg"), "UTF-8");
			BufferedReader br = new BufferedReader(reader); 
			String line;

			line = br.readLine();

			while (line !=null) {
				if (line.equals(word)) {
					return true;
				} else {
					line = br.readLine();
				}
			}
		} catch (Exception e) {
			Log.e("isWord", "Reading file", e);
		}
		//if it doesn't work, return false.
		return false;
	}


	// Conversions for saving and resuming from saved preferences
	/**
	 * Convert a list representation of selected tiles to a string
	 * @param sel	The list representation of selected tiles
	 * @return		The string representation of selected tiles
	 */
	static private String selectedToString(List<Boolean> sel) {
		StringBuilder builder = new StringBuilder();
		for (Boolean b : sel) {
			builder.append( b ? '1' : '0'); // 1 if true, 0 if false
		}
		return builder.toString();
	}

	/**
	 * Convert a string to a list of selected tiles
	 * @param rep 	The given string representation of the board
	 * @return		The list representation of the selected tiles
	 */
	protected ArrayList<Boolean> stringToSelected(String rep) {
		ArrayList<Boolean> sel = new ArrayList<Boolean>();
		for (int i=0; i< rep.length(); i++) {
			sel.add( rep.charAt(i) == '1' ?
					true : false);
		}
		return sel;
	}

	/**
	 * Returns the index of the character at the given coordinate
	 * @param i	the x-coordinate of the character
	 * @param j	the y-coordinate of the character
	 * @return	An Integer representing the index in the board
	 */
	int getTileIndex(int i, int j) {
		return i + (this.size * j);
	}

	/**
	 * Returns the letter at the given index.
	 * @param	i	the index of the desired letter
	 * @return	The character at the given index
	 */
	private Character getLetter(int i) {
		return game.getBoard().get(i);
	}

	/**
	 * Returns the string representation of the character at the given coord
	 * @param i	the x-coordinate of the desired character
	 * @param j	the y-coordinate of the desired character
	 * @return	A string containing the letter at the given coordnate
	 */
	protected String getLetterString(int i, int j) {
		int index = getTileIndex(i, j);  // converts coordinates into list index
		return this.game.getBoard().get(index).toString();
	}

	/**
	 * Renders the letter at the given coordinate position as "selected":
	 * sets the corresponding index in the selected list to true, and 
	 * adds the selected characer to the iWord stringbuffer.
	 * @param i	the x-coordinate of the selected character
	 * @param j	the y-coodinate of the selected character
	 */
	protected void selectTile(int i, int j) {
		int index = getTileIndex(i, j);
		Log.d(TAG, "Setting tile at " + index + " to true");
		selected.set(index, true);
		this.iWord.append(getLetter(index));
	}

	/**
	 * Code to execute when the submit word button is pressed
	 * @param v view parameter from button
	 */
	public void onSubmitWordButtonClicked(View v) {

		String word = iWord.toString();
		Log.d(TAG, "User entered word: " + word);
		// if it's a word and it hasn't been entered yet
		if (isWord(word) && !game.getEnteredWords().contains(word)) {
			this.game.getEnteredWords().add(word);
			Log.d(TAG, "It's a word! Entered words is now: " + game.getEnteredWords().toString());
			updateListView();
			game.incrementScore(username, scoreWord(word));
			Sounds.playRight(this);
		} else {
			Log.d(TAG, "It's not a word. Entered words is still: "+ game.getEnteredWords().toString());
			Sounds.playWrong(this);
		}
		Log.d(TAG, "User just clicked submit word.  Clearing all selected tiles");
		this.clearSelectedTiles();
	}

	private int scoreWord(String word) {
		if (word.length() >= 3) {
			return word.length() - 2;
		} else {
			return 0;
		}
	}

	private void updateListView() {
		ArrayList<String> entered = game.getEnteredWords();
		Collections.reverse(entered);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, entered);
		ListView listview = (ListView)findViewById(R.id.boggle_entered_words);
		listview.setAdapter(adapter);
		listview.setCacheColorHint(color.boggle_boardColor);
	}

	public void onMultiplayerGameBackButtonClicked(View v) {
		finish();
	}

	public void onClearWordButtonClicked(View v) {
		Sounds.playClear(this);
		this.clearSelectedTiles();
	}
	
	/**
	 * Sets Game Over specifics
	 */
	public void onGameOver(){
		SharedPreferences pref = getSharedPreferences(MULTI_PREF, MODE_PRIVATE);
		SharedPreferences.Editor edit = pref.edit();
		Log.d(TAG, "Clear Shared Prefs");
		edit.clear();
		edit.putBoolean(NO_GAME, true);
		edit.commit();
		finish();
	}

	/**
	 * Clears the arraylist<Boolean> that represents the selected tiles,
	 * and removes all characters in the string buffer
	 */
	private void clearSelectedTiles() {
		this.selected.clear();
		for (int i = 0; i <= size*size; i++) {
			this.selected.add(false);
		}
		//TODO: not sure if this should stay
		this.iWord = new StringBuffer();
		if (gameView != null) {
			this.gameView.setSelectedDice(selected);
		}
		Log.d(TAG, "Cleared Selected Tiles");
	}

}
