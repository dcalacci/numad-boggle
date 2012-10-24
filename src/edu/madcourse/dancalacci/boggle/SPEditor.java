package edu.madcourse.dancalacci.boggle;

import android.content.SharedPreferences;

public abstract class SPEditor {
	public static final 	String 	BOGGLE_PREF = "edu.madcourse.dancalacci.boggle";
	private static final 	String 	PREF_BOARD = "board";
	private static final 	String 	PREF_SELECTED = "selected";
	private static final 	String 	PREF_TIME = "time";
	protected static final 	String 	NO_GAME = "no_game";
	protected static final 	String 	PREF_IWORD = "iword";
	protected static final 	String 	PREF_WORDLIST = "wordlist";
	protected static final 	String 	PREF_SCORE = "score";
	
	public static void saveGame(Multiplayer_Game game) {
		   SharedPreferences pref = game.getSharedPreferences(
				   BOGGLE_PREF, 
				   Multiplayer_Game.MODE_PRIVATE);
		   SharedPreferences.Editor editor = pref.edit();
		   
	}
		
	}

