package edu.madcourse.dancalacci.boggle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import edu.madcourse.dancalacci.R;
import edu.madcourse.bloomfilter.*;


public class Game extends Activity {
	// tag for this activity
	private static final String TAG = "Boggle";
	
	public static final String BOGGLE_PREF = "edu.madcourse.dancalacci.boggle";
	private static final String PREF_BOARD = "board";
	private static final String PREF_SELECTED = "selected";
	private static final String PREF_TIME = "time";
	
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
   protected List<Character> brd = new ArrayList<Character>();
  
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

   // List that represents what tiles have been selected
   private List<Boolean> selected = new ArrayList<Boolean>();

   @Override
   protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     Log.d(TAG, "onCreate"); // log the event

     // set the layout, start the view.
     puzzleView = new PuzzleView(this);
     
     setContentView(R.layout.boggle_game);
     FrameLayout boardFrame = (FrameLayout)findViewById(R.id.boggle_board);
     boardFrame.addView(puzzleView);
     
     // if TO_CONTINUE is 1, continue the game.  if not, start a new one.
     int diff = getIntent().getIntExtra(TO_CONTINUE, CONTINUE);
     this.startBoard(diff);
     
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
	   
	   // save boggle board
	   getSharedPreferences(BOGGLE_PREF, MODE_PRIVATE).edit().putString(PREF_BOARD, 
			   boardToString(brd)).commit();
	   // save the selected characters
	   getSharedPreferences(BOGGLE_PREF, MODE_PRIVATE).edit().putString(PREF_SELECTED, 
			   selectedToString(selected)).commit();
	   
   }
   
   protected Boolean isWord(String word) {
	   //Boggle.bloom.add(word);
	   return Boggle.bloom.contains(word);
   }
   
   
   /**
    * Either restores a saved board, or creates a new one, depending on the given state.
    * @param state 	1 signifies that there is a saved board, 0 signifies there is not.
    */
   private void startBoard(int state) {
	   if (state == 1) {
		   String board = getSharedPreferences(BOGGLE_PREF, MODE_PRIVATE).getString(PREF_BOARD, "false");
		   this.brd = Game.stringToBoard(board);
		   String sel = getSharedPreferences(BOGGLE_PREF, MODE_PRIVATE).getString(PREF_SELECTED, "false");
		   this.selected = Game.stringToSelected(sel);
	   } else {
		   getSharedPreferences(BOGGLE_PREF, MODE_PRIVATE).edit().remove(PREF_BOARD).commit();
		   getSharedPreferences(BOGGLE_PREF, MODE_PRIVATE).edit().remove(PREF_SELECTED).commit();
		   this.generateTiles();
	   }
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
   static protected List<Boolean> stringToSelected(String rep) {
	   List<Boolean> sel = new ArrayList<Boolean>();
	   for (int i=0; i< rep.length(); i++) {
		   sel.add( rep.charAt(i) == '1' ?
				   sel.add(true) :
					   sel.add(false));
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
   static protected List<Character> stringToBoard(String rep) {
	   List<Character> board = new ArrayList<Character>();
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
   private int getTileIndex(int i, int j) {
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
    * sets the corresponding index in the selected list to true.
    * @param i	the x-coordinate of the selected character
    * @param j	the y-coodinate of the selected character
    */
   protected void selectTile(int i, int j) {
	   int index = getTileIndex(i, j);
	   selected.set(index, true);
   }
   
   

}
