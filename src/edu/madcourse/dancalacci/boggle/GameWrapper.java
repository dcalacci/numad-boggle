package edu.madcourse.dancalacci.boggle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import android.util.Log;

public class GameWrapper {
	private String currentTurn;
	private String board;
	private ArrayList<String> enteredWords;
	private Hashtable<String, Integer> scores;
	private int numTurns;
	private int turnP1;
	private int turnP2;
	
	public GameWrapper(
			String currentTurn,
			String board,
			ArrayList<String> enteredWords,
			Hashtable<String, Integer> scores,
			int numTurns) {
		this.board 			= board; 
		this.currentTurn 	= currentTurn;
		this.enteredWords 	= enteredWords;
		this.scores 		= scores;
		this.numTurns 		= numTurns;
		this.turnP1 = this.numTurns / 2;
		this.turnP1 = this.numTurns / 2;
	}
	
	/**
	 * Gets the username of the other player in the game
	 * @param user 	The name of the requesting user
	 * @return 		A string that contains the name of users' opponent
	 */
	public String getOtherPlayer(String user) {
		List<String> users = Collections.list(scores.keys());
		for (String username : users) {
			if (!username.equals(user)) {
				return username;
			}
		}
		// we should never be here
		return "NoUser";
	}
	
	// NumTurns
	/**
	 * Increments the number of turns by 1
	 */
	public void incrementNumTurns() {
		this.numTurns++;
	}
	
	/**
	 * Gets the total number of turns passed
	 * @return 	The total number of turns passed
	 */
	public int getNumTurns() {
		return this.numTurns;
	}
	
	/**
	 * Sets the number of turns to a specific number
	 * @param n 	The number of turns to set numTurns to
	 */
	public void setNumTurns(int n) {
		this.numTurns = n;
	}
	
	// EnteredWords
	
	/**
	 * Adds a word to the entered word list
	 * @param word 	The word to add to the entered word list
	 */
	public void addEnteredWord(String word) {
		this.enteredWords.add(word);
	}
	
	/**
	 * Checks if a word has been entered or not
	 * @param word 	The word to check
	 * @return 	True if the word has been entered, false otherwise
	 */
	public boolean isWordEntered(String word) {
		return this.enteredWords.contains(word);
	}
	/**
	 * Gets an arraylist of the words entered in this game
	 * @return 	An arraylist representation of all the words entered so far
	 */
	public ArrayList<String> getEnteredWords() {
		return this.enteredWords;
	}

	// currentTurn
	
	/**
	 * Gets the name of the user whose turn it is
	 * @return 	A string that contains the name of the user whose turn it is
	 */
	public String getCurrentTurn() {
		return this.currentTurn;
	}
	
	/**
	 * Determines it's the given users' turn or not
	 * @param user 	The user to check
	 * @return 	True if it's users' turn, false otherwise
	 */
	public boolean isTurn(String user) {
		return user.equals(currentTurn);
	}
	
	/**
	 * Sets the current turn to the given user
	 * @param user The user to set the current turn to
	 */
	public void setCurrentTurn(String user) {
		this.currentTurn = user;
	}

	// score
	
	/**
	 * Returns the scores for this game
	 * @return 	A hashtable<String, Integer> that contains the current scores.
	 * FORMAT: Username => Score
	 */
	public Hashtable<String, Integer> getScores() {
		return this.scores;
	}
	/**
	 * Updates the given users' score
	 * @param user 	The user whose score we're updating
	 * @param score The score to change users' score to 
	 */
	public void updateScore(String user, Integer score) {
		this.scores.put(user, score);
	}
	
	/**
	 * Returns the score of the given user
	 * @param user The user whose score we're getting
	 * @return	Int, score of user
	 */
	public int getUserScore(String user){
		return this.scores.get(user);
	}
	
	/**
	 * Increments the given users' score by a given amount
	 * @param user 	The user whose score we're updating
	 * @param delta The amount to increment his score by
	 */
	public void incrementScore(String user, int delta) {
		this.scores.put(user, this.scores.get(user)+delta);
	}
	/**
	 * Determines if the given user is winning
	 * @param user 	The user to check
	 * @return		True, if the given user is winning, false otherwise
	 */
	public boolean isWinning(String user) {
		return (this.scores.get(user) > 
				this.scores.get(this.getOtherPlayer(user)));
	}

	/**
	 * Gets this games' board
	 * @return An arraylist implementation of the board.
	 */
	public String getBoard() {
		Log.d("GameWrapper", "getBoard: "+ this.board);
		return this.board;
	}
}
