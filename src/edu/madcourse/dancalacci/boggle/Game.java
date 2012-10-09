package edu.madcourse.dancalacci.boggle;

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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import edu.madcourse.dancalacci.R;
import edu.madcourse.dancalacci.R.color;


public class Game extends Activity {
	// tag for this activity
	private static final String TAG = "Boggle_Game";
	
	public static final String BOGGLE_PREF = "edu.madcourse.dancalacci.boggle";
	private static final String PREF_BOARD = "board";
	private static final String PREF_SELECTED = "selected";
	private static final String PREF_TIME = "time";
	protected static final String NO_GAME = "no_game";
	protected static final String PREF_IWORD = "iword";
	protected static final String PREF_WORDLIST = "wordlist";
	protected static final String PREF_SCORE = "score";
	
	public static final String TO_CONTINUE = "boggle_continue";
	public static final int CONTINUE = 1;
	public static final int NEW = 0;

	// view for this puzzle.
	private PuzzleView puzzleView;
	

	// the collection of tiles is represented by an array of characters
	// such that an index refers to a square on the board:
	// 1  2  3  4
	// 5  6  7  8
	// 9  10 11 12
	// 13 14 15 16
	// List that represents the tiles on the board
   protected ArrayList<Character> brd = new ArrayList<Character>();
   protected ArrayList<String> enteredWords = new ArrayList<String>();
   

   // List that represents what tiles have been selected
   private ArrayList<Boolean> selected = new ArrayList<Boolean>();
   
   protected int score = 0;
  
   // Each list in the top array represents a die
   // Each element of the bottom array represents a side of a die
   // this collection of boggle dies is the new, easy game.
   private final List<List<Character>> dice = new ArrayList<List<Character>>() {{
     add( Arrays.asList( 'a', 'a', 'e', 'e', 'g', 'n' ));
     add( Arrays.asList( 'e', 'l', 'r', 't', 't', 'y' ));
     add( Arrays.asList( 'a', 'o', 'o', 't', 't', 'w' ));
     add( Arrays.asList( 'a', 'b', 'b', 'j', 'o', 'o' ));
     add( Arrays.asList( 'e', 'h', 'r', 't', 'v', 'w' ));
     add( Arrays.asList( 'c', 'i', 'm', 'o', 't', 'v' ));
     add( Arrays.asList( 'd', 'i', 's', 't', 't', 'y' ));
     add( Arrays.asList( 'e', 'i', 'o', 's', 's', 't' ));
     add( Arrays.asList( 'd', 'e', 'l', 'r', 'v', 'y' ));
     add( Arrays.asList( 'a', 'c', 'h', 'o', 'p', 's' ));
     add( Arrays.asList( 'h', 'i', 'm', 'n', 'q', 'u' ));
     add( Arrays.asList( 'e', 'e', 'i', 'n', 's', 'u' ));
     add( Arrays.asList( 'e', 'e', 'g', 'h', 'n', 'w' ));
     add( Arrays.asList( 'a', 'f', 'f', 'k', 'p', 's' ));
     add( Arrays.asList( 'h', 'l', 'n', 'n', 'r', 'z' ));
     add( Arrays.asList( 'd', 'e', 'i', 'l', 'r', 'x' ));
   }};
   // TODO: find the older dice distribution, make a new list for another difficulty
   
   // timer code
//   private int currentTime = 180;
//   private Handler mHandler = new Handler() {
//	   int what = 1;
//	   public void handleMessage(Message msg) {
//		   super.handleMessage(msg);
//		   this.sendMessageDelayed(Message.obtain(this.what), 1000);
//		   currentTime--;
//	   }
//   };
   
   protected StringBuffer iWord = new StringBuffer();
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     Log.d(TAG, "onCreate"); // log the event

	SharedPreferences pref = getSharedPreferences(BOGGLE_PREF, MODE_PRIVATE);
	SharedPreferences.Editor edit = pref.edit();
	edit.putBoolean(NO_GAME, false);
	edit.commit();

     // if TO_CONTINUE is 1, continue the game.  if not, start a new one.
     int cont = getIntent().getIntExtra(TO_CONTINUE, CONTINUE);
     this.clearSelectedTiles();
     
     // set the layout, start the view.
     puzzleView = new PuzzleView(this);
     setContentView(R.layout.boggle_game);
     FrameLayout boardFrame = (FrameLayout)findViewById(R.id.boggle_board);
     boardFrame.addView(puzzleView);
     
     // make the board based on the continue state
     this.startBoard(cont);
     
     
     
   }
   
   //TODO: gray out the screen, display a "resume" and "quit" buttons
//   public void OnPauseButtonClicked(View v) {
//   }
   
   @Override
   protected void onResume() {
	   super.onResume();
	   //Music.play(this, R.raw.game);
//	   if (getSharedPreferences(BOGGLE_PREF, MODE_PRIVATE).contains(PREF_BOARD)) {
//		   String board = getPreferences(MODE_PRIVATE).getString(PREF_BOARD, "false");
//		   this.brd = Game.stringToBoard(board);
//	   }
//	   if (getSharedPreferences(BOGGLE_PREF, MODE_PRIVATE).contains(PREF_SELECTED)) {
//		   String sel = getPreferences(MODE_PRIVATE).getString(PREF_SELECTED, "false");
//		   this.selected = Game.stringToSelected(sel);
//	   }
   }
   
   
   @Override
   protected void onPause() {
	   super.onPause();
	   Log.d(TAG, "onPause");
	   // Music.stop(this);
	   
	   SharedPreferences pref = getSharedPreferences(BOGGLE_PREF, MODE_PRIVATE);
	   SharedPreferences.Editor edit = pref.edit();
	   
	   // if 
	   if (pref.getBoolean(NO_GAME, false)) {
		   Log.d(TAG, "onPause called from Quit button, not saving anything");
	   } else {
		   Log.d(TAG, "Saving board and selected tiles to SharedPrefs");
		   Log.d(TAG, "Selected tiles to be saved: " + selected.toString());
		   edit.putString(PREF_BOARD, boardToString(brd));
		   edit.putString(PREF_SELECTED, selectedToString(selected));
		   
		   Log.d(TAG, "Current word to be saved: " +iWord.toString());
		   edit.putString(PREF_IWORD, iWord.toString());
		   
		   Log.d(TAG, "Current list of entered words to be saved: "+enteredWords.toString());
		   edit.putString(PREF_WORDLIST, enteredWords.toString());
		   
		   Log.d(TAG, "Current score: " +score);
		   edit.putInt(PREF_SCORE, score);
		   
		   edit.commit();
		   Log.d(TAG, "What is actually in sharedprefs: " + pref.getString(PREF_SELECTED, "nothing"));
	   }
	   
   }
   
   protected Boolean isWord(String word) {
	   if (word.isEmpty()) {
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
   
   
   /**
    * Either restores a saved board, or creates a new one, depending on the given state.
    * @param state 	1 signifies that there is a saved board, 0 signifies there is not.
    */
   private void startBoard(int state) {
	   // if it's a continue game
	   if (state == 1) {
		   String iword = getSharedPreferences(BOGGLE_PREF, MODE_PRIVATE).getString(PREF_IWORD, "");
		   this.iWord = new StringBuffer(iword);
		   Log.d(TAG, "Current working word is: " +iword);
		   
		   String entered = getSharedPreferences(BOGGLE_PREF, MODE_PRIVATE).getString(PREF_WORDLIST, "");
		   // convert the string representation to a list
		   List<String> enteredList = Arrays.asList(entered.substring(1, entered.length() - 1).split(", "));
		   // make an arraylist from that list
		   this.enteredWords = new ArrayList<String>(enteredList);
		   Log.d(TAG, "Entered words loaded from sharedPrefs: " + enteredWords.toString());
		   
		   String board = getSharedPreferences(BOGGLE_PREF, MODE_PRIVATE).getString(PREF_BOARD, "false");
		   this.brd = Game.stringToBoard(board);
		   
		   Log.d(TAG, "Score loaded from sharedPrefs: " +score);
		   this.score = getSharedPreferences(BOGGLE_PREF, MODE_PRIVATE).getInt(PREF_SCORE, 0);
		   
		   String sel = getSharedPreferences(BOGGLE_PREF, MODE_PRIVATE).getString(PREF_SELECTED, "false");
		   Log.d(TAG, "Selected tiles loaded from sharedprefs: " + sel);
		   this.selected = stringToSelected(sel);
		   Log.d(TAG, "Selected tiles is now: " + selected.toString());
		   updateListView();
		   this.puzzleView.setSelectedDice(this.selected);
	   } 
	   // if it's a new game
	   else {
		   // clear all the shared preferences
		   SharedPreferences pref = getSharedPreferences(BOGGLE_PREF, MODE_PRIVATE);
		   SharedPreferences.Editor edit = pref.edit();
		   Log.d(TAG, "Clear Shared Prefs");
		   edit.clear();
		   edit.commit();
		   
		   // generate the board
		   this.generateTiles();
		   clearSelectedTiles();
		   Log.d(TAG, "setting selected die in startBoard view");
		   Log.d(TAG, "Selected tiles in startBoard: " +this.selected.toString());
		   this.puzzleView.setSelectedDice(this.selected);
	   }
   }
   
   
   /**
    * Clears the arraylist<Boolean> that represents the selected tiles,
    * and removes all characters in the string buffer
    */
   private void clearSelectedTiles() {
	   this.selected.clear();
	   for (int i = 0; i <= 15; i++) {
		   this.selected.add(false);
	   }
	   this.iWord = new StringBuffer();
	   if (puzzleView != null) {
		   this.puzzleView.setSelectedDice(selected);
	   }
	   Log.d(TAG, "Cleared Selected Tiles");
	   
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
    * Build a string representation of the board for state saving
    * @param board	The list representation of the board
    * @return		The string representation of the board
    */
   static private String boardToString(List<Character> board) {
	   StringBuilder builder = new StringBuilder();
	   for (Character ch : board) {
		   builder.append(ch);
	   }
	   return builder.toString();
   }
   
   /**
    * Convert a string to a board
    * @param rep	The string representation of the board
    * @return		The list representation of the board
    */
   static protected ArrayList<Character> stringToBoard(String rep) {
	   ArrayList<Character> board = new ArrayList<Character>();
	   for (int i = 0; i < rep.length(); i++) {
		   board.add(rep.charAt(i));
	   }
	   return board;
   }
   
   
   /**
    *  Populates brd, the list that represents the board.
    *  "rolls" the boggle dice and places them into random positions
    */
   protected void generateTiles() {
	   // populate the arraylist
	   ArrayList<Character> tiles = new ArrayList<Character>();
	   for (int i=0; i<16; i++) {
		   tiles.add(' ');
	   }
	   // "randomly" generate the board
	   
	   // step one: assign a position to each die
	   // a position is a number, 1-16:
	   // 1  2  3  4
	   // 5  6  7  8
	   // 9  10 11 12
	   // 13 14 15 16
	   List<Integer> positions = new ArrayList<Integer>();
	   for (int i = 0; i < (this.dice.size()); i++) {
		   positions.add(i);  // positions now contains all numbers 1-16
	   }
	   // Randomize the positions, index n in positions corresponds to
	   // index n in dice
	   Collections.shuffle(positions); //shuffle positions
	   Collections.shuffle(dice);      //shuffle the dice themselves
	   // now we have a list with the randomized positions whose indices
	   // correspond to the list of dice.
	 
	   // step two: randomly assign a character to each space on the board
	   for (Integer pos : positions) {
		   Character c = dice.get(pos).get((int)(Math.random() * 5));
		   //  c is now a random face of the current die
		   tiles.set(pos, c);
	   }
	   brd = tiles;
	   
   }
   
   /**
    * Returns the index of the character at the given coordinate
    * @param i	the x-coordinate of the character
    * @param j	the y-coordinate of the character
    * @return	An Integer representing the index in the board
    */
   int getTileIndex(int i, int j) {
	   return i + (4 * j);
   }
   
   
   /**
    * Returns the letter at the given index.
    * @param	i	the index of the desired letter
    * @return	The character at the given index
    */
   private Character getLetter(int i) {
	   return brd.get(i);
   }
   
   /**
    * Returns the string representatino of the character at the given coord
    * @param i	the x-coordinate of the desired character
    * @param j	the y-coordinate of the desired character
    * @return	A string containing the letter at the given coordnate
    */
   protected String getLetterString(int i, int j) {
	   int index = getTileIndex(i, j);  // converts coordinates into list index
	   return this.brd.get(index).toString();
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
	   if (isWord(word) && !enteredWords.contains(word)) {
		   this.enteredWords.add(word);
		   Log.d(TAG, "It's a word! Entered words is now: " + enteredWords.toString());
		   updateListView();
		   score = score + scoreWord(word);
		   //TODO: play RIGHT sound
	   } else {
		   Log.d(TAG, "It's not a word. Entered words is still: "+ enteredWords.toString());
		   //TODO: play WRONG sound, don't add word
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
	   ArrayList entered = enteredWords;
	   Collections.reverse(entered);
	   ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, entered);
	   
	   ListView listview = (ListView)findViewById(R.id.boggle_entered_words);
	   listview.setAdapter(adapter);
	   listview.setCacheColorHint(color.boggle_boardColor);
   }
   
   public void onPauseButtonClicked(View v) {
	   Intent i = new Intent(this, Pause.class);
	   startActivity(i);
   }
   
   public void onClearWordButtonClicked(View v) {
	   this.clearSelectedTiles();
   }
   
   /**
    * Code to execute when the quit button is pressed
    * @param v view parameter from button
    */
   public void onQuitButtonClicked(View v) {
	   SharedPreferences pref = getSharedPreferences(BOGGLE_PREF, MODE_PRIVATE);
	   SharedPreferences.Editor edit = pref.edit();
	   Log.d(TAG, "Clear Shared Prefs");
	   edit.clear();
	   edit.putBoolean(NO_GAME, true);
	   edit.commit();
	   finish();
   }
}
