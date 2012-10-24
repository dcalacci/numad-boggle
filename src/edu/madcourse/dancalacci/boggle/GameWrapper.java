package edu.madcourse.dancalacci.boggle;

import java.util.ArrayList;
import java.util.Dictionary;

public class GameWrapper {
	private String currentTurn;
	private ArrayList<String> board;
	private ArrayList<String> enteredWords;
	private Dictionary<String, Integer> scores;
	private int numTurns;
	
	public GameWrapper(
			String currentTurn,
			ArrayList<String> board,
			ArrayList<String> enteredWords,
			Dictionary<String, Integer> scores,
			int numTurns) {
		this.currentTurn 	= currentTurn;
		this.board 			= board;
		this.enteredWords 	= enteredWords;
		this.scores 		= scores;
		this.numTurns 		= numTurns;
	}
	
	
}
