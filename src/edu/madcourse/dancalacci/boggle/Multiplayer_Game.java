/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband3 for more book information.
 ***/
package edu.madcourse.dancalacci.boggle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import edu.madcourse.dancalacci.R;

public class Multiplayer_Game extends Activity implements OnClickListener {
	private static final 	String TAG = "MultiplayerBoggleGame";

	public static final 	String 	MULTI_PREF 		= "edu.madcourse.dancalacci.multiplayer";
	public static final 	String  PREF_BOGGLE 	= "prefBoggle";
	private static final 	String 	PREF_USER 		= "prefUser";
	private static final 	String PREF_DICE 		= "puzzleDice" ;
	//private static final 	String PREF_SCORE 		= "MutliplayerScore";
	private static final 	String PREF_USED_WORDS 	= "boggleUsedWords";
	private static final 	String PREF_TIME 		= "boggleTime";
	public static final 	String PREF_RESUME 		= "MultipalyerboggleResume";


	private 	SharedPreferences 		sf;
	private 	int 					size 				= 		5;			
	private 	int 					score_p1			= 		0; 	// current game score
	private 	int 					score_p2			= 		0; 	// current game score
	private 	String 					username;
	private 	String 					opponent; 
	private 	String 					letterSet;					// letters used in game
	private 	String					onPauseLetters; 
	private 	ServerAccessor 			sa 					= new ServerAccessor();
	private 	GameWrapper 				game;
	private 	String						current_turn;
	private		Multiplayer_Game_Service 	service;

	private ArrayList<String> wordList= new ArrayList<String>();			// string of accepted words seperated by spaces

	private static List<Integer> current_dice_set = 
			new ArrayList<Integer>(); // set of current dice selected

	private String[] dice_set = new String[]{ 	// strings of all possible dice
			"AAAFRS",
			"AAEEEE",
			"AAFIRS",
			"ADENNN",
			"AEEEEM",
			"AEEGMU",
			"AEGMNN",
			"AFIRSY",
			"BJKQXZ",
			"CCNSTW",
			"CEIILT",
			"CEILPT",
			"CEIPST",
			"DDLNOR",
			"DHHLOR",
			"DHHNOT",
			"DHLNOR",
			"EIIITT",
			"EMOTTT",
			"ENSSSU",
			"FIPRSY",
			"GORRVW",
			"HIPRRY",
			"NOOTUW",
			"OOOTTU"
	};

	private static final int[] BUTTONS = {	// array of dice buttons on the grid
		R.id.button_DiceR1C1,
		R.id.button_DiceR1C2,
		R.id.button_DiceR1C3,
		R.id.button_DiceR1C4,
		R.id.button_DiceR1C5,
		R.id.button_DiceR2C1,
		R.id.button_DiceR2C2,
		R.id.button_DiceR2C3,
		R.id.button_DiceR2C4,
		R.id.button_DiceR2C5,
		R.id.button_DiceR3C1,
		R.id.button_DiceR3C2,
		R.id.button_DiceR3C3,
		R.id.button_DiceR3C4,
		R.id.button_DiceR3C5,
		R.id.button_DiceR4C1,
		R.id.button_DiceR4C2,
		R.id.button_DiceR4C3,
		R.id.button_DiceR4C4,
		R.id.button_DiceR4C5,
		R.id.button_DiceR5C1,
		R.id.button_DiceR5C2,
		R.id.button_DiceR5C3,
		R.id.button_DiceR5C4,
		R.id.button_DiceR5C5
	};

	Handler handler=new Handler() 
	{ 
		@Override 
		public void handleMessage(Message msg) { 
			//get data from msg 

			String result = msg.getData().getString("result"); 
			//tv.setText(result); 
			Log.d("xxxxx", "get data" + result); 
			super.handleMessage(msg); 
		} 
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		requestWindowFeature(Window.FEATURE_NO_TITLE); // no title

		opponent = getIntent().getStringExtra("opponent");
		username = getSharedPreferences(MULTI_PREF, MODE_PRIVATE).getString(PREF_USER, "guest");

		//service = new Multiplayer_Game_Service(username, opponent);

		sendBroadcast(new Intent(this, OnBootReceiver.class));
		
		Toast.makeText(this, R.string.alarms_active,
                Toast.LENGTH_LONG).show();
		
		// Sets to multiplayer views
		setContentView(R.layout.multiplayer_game);

		//doBindService();
		//showServiceData();

		this.game = sa.getGame(username, opponent);

		this.letterSet = this.game.getBoard();
		if (letterSet.isEmpty()){
			letterSet = this.pick_Letters();
			sa.updateBoard(this.username, this.opponent, letterSet);
		}
		this.current_turn = this.game.getCurrentTurn();
		this.wordList = this.game.getEnteredWords();

		this.score_p1 = this.game.getUserScore(username);
		this.score_p2 = this.game.getUserScore(opponent);		

		Log.d(TAG, "current set: "+current_dice_set.toString());
		onPauseLetters = pick_Letters();

		Log.d(TAG, "onCreate Letterset: "+letterSet);
		Log.d(TAG, "onCreate tempLetterset: "+ onPauseLetters);

		View button_DiceR1C1 = findViewById(R.id.button_DiceR1C1);
		button_DiceR1C1.setOnClickListener((OnClickListener) this);

		View button_DiceR1C2 = findViewById(R.id.button_DiceR1C2);
		button_DiceR1C2.setOnClickListener((OnClickListener) this);

		View button_DiceR1C3 = findViewById(R.id.button_DiceR1C3);
		button_DiceR1C3.setOnClickListener((OnClickListener) this);

		View button_DiceR1C4 = findViewById(R.id.button_DiceR1C4);
		button_DiceR1C4.setOnClickListener((OnClickListener) this);

		View button_DiceR1C5 = findViewById(R.id.button_DiceR1C5);
		button_DiceR1C5.setOnClickListener((OnClickListener) this);

		View button_DiceR2C1 = findViewById(R.id.button_DiceR2C1);
		button_DiceR2C1.setOnClickListener((OnClickListener) this);

		View button_DiceR2C2 = findViewById(R.id.button_DiceR2C2);
		button_DiceR2C2.setOnClickListener((OnClickListener) this);

		View button_DiceR2C3 = findViewById(R.id.button_DiceR2C3);
		button_DiceR2C3.setOnClickListener((OnClickListener) this);

		View button_DiceR2C4 = findViewById(R.id.button_DiceR2C4);
		button_DiceR2C4.setOnClickListener((OnClickListener) this);

		View button_DiceR2C5 = findViewById(R.id.button_DiceR2C5);
		button_DiceR2C5.setOnClickListener((OnClickListener) this);

		View button_DiceR3C1 = findViewById(R.id.button_DiceR3C1);
		button_DiceR3C1.setOnClickListener((OnClickListener) this);

		View button_DiceR3C2 = findViewById(R.id.button_DiceR3C2);
		button_DiceR3C2.setOnClickListener((OnClickListener) this);

		View button_DiceR3C3 = findViewById(R.id.button_DiceR3C3);
		button_DiceR3C3.setOnClickListener((OnClickListener) this);

		View button_DiceR3C4 = findViewById(R.id.button_DiceR3C4);
		button_DiceR3C4.setOnClickListener((OnClickListener) this);

		View button_DiceR3C5 = findViewById(R.id.button_DiceR3C5);
		button_DiceR3C5.setOnClickListener((OnClickListener) this);

		View button_DiceR4C1 = findViewById(R.id.button_DiceR4C1);
		button_DiceR4C1.setOnClickListener((OnClickListener) this);

		View button_DiceR4C2 = findViewById(R.id.button_DiceR4C2);
		button_DiceR4C2.setOnClickListener((OnClickListener) this);

		View button_DiceR4C3 = findViewById(R.id.button_DiceR4C3);
		button_DiceR4C3.setOnClickListener((OnClickListener) this);

		View button_DiceR4C4 = findViewById(R.id.button_DiceR4C4);
		button_DiceR4C4.setOnClickListener((OnClickListener) this);

		View button_DiceR4C5 = findViewById(R.id.button_DiceR4C5);
		button_DiceR4C5.setOnClickListener((OnClickListener) this);

		View button_DiceR5C1 = findViewById(R.id.button_DiceR5C1);
		button_DiceR5C1.setOnClickListener((OnClickListener) this);

		View button_DiceR5C2 = findViewById(R.id.button_DiceR5C2);
		button_DiceR5C2.setOnClickListener((OnClickListener) this);

		View button_DiceR5C3 = findViewById(R.id.button_DiceR5C3);
		button_DiceR5C3.setOnClickListener((OnClickListener) this);

		View button_DiceR5C4 = findViewById(R.id.button_DiceR5C4);
		button_DiceR5C4.setOnClickListener((OnClickListener) this);

		View button_DiceR5C5 = findViewById(R.id.button_DiceR5C5);
		button_DiceR5C5.setOnClickListener((OnClickListener) this);

		View tv_Player1 = findViewById(R.id.multiplayer_player1);
		this.setScore(R.id.multiplayer_player1, username, this.score_p1);

		View tv_Player2 = findViewById(R.id.multiplayer_player1);
		this.setScore(R.id.multiplayer_player2, opponent, this.score_p2);

		TextView tv_Turns = (TextView) findViewById(R.id.multiplayer_current_turn);

		setCurrentTurnIcon(this.current_turn);

		View usedWordsList = findViewById(R.id.boggle_used_words);
		usedWordsList.setOnClickListener((OnClickListener) this);

		fillButtons(this.letterSet);

	}

	public void onClick(View v) {
		Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		String letter;
		if(isYourTurn()){
			this.current_turn = sa.getTurn(username, opponent);
			this.game.setCurrentTurn(current_turn);
			this.changeSubmitButtonState();
		}

		switch (v.getId()) {
		case R.id.button_DiceR1C1:	         
			vib.vibrate(50);
			playClickSound(R.id.button_DiceR1C1);
			letter = getLetterFromDice(R.id.button_DiceR1C1);
			if(isSequenceIsValid(R.id.button_DiceR1C1)){
				try {
					process_Click(R.id.button_DiceR1C1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_DiceR1C2:
			vib.vibrate(50);
			playClickSound(R.id.button_DiceR1C2);
			letter = getLetterFromDice(R.id.button_DiceR1C2);
			if(isSequenceIsValid(R.id.button_DiceR1C2)){
				try {
					process_Click(R.id.button_DiceR1C2);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}         
			break;
		case R.id.button_DiceR1C3:
			vib.vibrate(50);
			playClickSound(R.id.button_DiceR1C3);
			letter = getLetterFromDice(R.id.button_DiceR1C3);
			if(isSequenceIsValid(R.id.button_DiceR1C3)){
				try {
					process_Click(R.id.button_DiceR1C3);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_DiceR1C4:
			vib.vibrate(50);
			playClickSound(R.id.button_DiceR1C4);
			letter = getLetterFromDice(R.id.button_DiceR1C4);
			if(isSequenceIsValid(R.id.button_DiceR1C4)){
				try {
					process_Click(R.id.button_DiceR1C4);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_DiceR1C5:
			vib.vibrate(50);
			playClickSound(R.id.button_DiceR1C5);
			letter = getLetterFromDice(R.id.button_DiceR1C5);
			if(isSequenceIsValid(R.id.button_DiceR1C5)){
				try {
					process_Click(R.id.button_DiceR1C5);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_DiceR2C1:
			vib.vibrate(50);
			playClickSound(R.id.button_DiceR2C1);
			letter = getLetterFromDice(R.id.button_DiceR2C1);
			if(isSequenceIsValid(R.id.button_DiceR2C1)){
				try {
					process_Click(R.id.button_DiceR2C1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_DiceR2C2:
			vib.vibrate(50);
			playClickSound(R.id.button_DiceR2C2);
			letter = getLetterFromDice(R.id.button_DiceR2C2);
			if(isSequenceIsValid(R.id.button_DiceR2C2)){
				try {
					process_Click(R.id.button_DiceR2C2);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_DiceR2C3:
			vib.vibrate(50);
			playClickSound(R.id.button_DiceR2C3);
			letter = getLetterFromDice(R.id.button_DiceR2C3);
			if(isSequenceIsValid(R.id.button_DiceR2C3)){
				try {
					process_Click(R.id.button_DiceR2C3);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_DiceR2C4:
			vib.vibrate(50);
			playClickSound(R.id.button_DiceR2C4);
			letter = getLetterFromDice(R.id.button_DiceR2C4);
			if(isSequenceIsValid(R.id.button_DiceR2C4)){
				try {
					process_Click(R.id.button_DiceR2C4);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_DiceR2C5:
			vib.vibrate(50);
			playClickSound(R.id.button_DiceR2C5);
			letter = getLetterFromDice(R.id.button_DiceR2C5);
			if(isSequenceIsValid(R.id.button_DiceR2C5)){
				try {
					process_Click(R.id.button_DiceR2C5);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_DiceR3C1:
			vib.vibrate(50);
			playClickSound(R.id.button_DiceR3C1);
			letter = getLetterFromDice(R.id.button_DiceR3C1);
			if(isSequenceIsValid(R.id.button_DiceR3C1)){
				try {
					process_Click(R.id.button_DiceR3C1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_DiceR3C2:
			vib.vibrate(50);
			playClickSound(R.id.button_DiceR3C2);
			letter = getLetterFromDice(R.id.button_DiceR3C2);
			if(isSequenceIsValid(R.id.button_DiceR3C2)){
				try {
					process_Click(R.id.button_DiceR3C2);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_DiceR3C3:
			vib.vibrate(50);
			playClickSound(R.id.button_DiceR3C3);
			letter = getLetterFromDice(R.id.button_DiceR3C3);
			if(isSequenceIsValid(R.id.button_DiceR3C3)){
				try {
					process_Click(R.id.button_DiceR3C3);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_DiceR3C4:
			vib.vibrate(50);
			playClickSound(R.id.button_DiceR3C4);
			letter = getLetterFromDice(R.id.button_DiceR3C4);
			if(isSequenceIsValid(R.id.button_DiceR3C4)){
				try {
					process_Click(R.id.button_DiceR3C4);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_DiceR3C5:
			vib.vibrate(50);
			playClickSound(R.id.button_DiceR3C5);
			letter = getLetterFromDice(R.id.button_DiceR3C5);
			if(isSequenceIsValid(R.id.button_DiceR3C5)){
				try {
					process_Click(R.id.button_DiceR3C5);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_DiceR4C1:
			vib.vibrate(50);
			playClickSound(R.id.button_DiceR4C1);
			letter = getLetterFromDice(R.id.button_DiceR4C1);
			if(isSequenceIsValid(R.id.button_DiceR4C1)){
				try {
					process_Click(R.id.button_DiceR4C1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_DiceR4C2:
			vib.vibrate(50);
			playClickSound(R.id.button_DiceR4C2);
			letter = getLetterFromDice(R.id.button_DiceR4C2);
			if(isSequenceIsValid(R.id.button_DiceR4C2)){
				try {
					process_Click(R.id.button_DiceR4C2);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_DiceR4C3:
			vib.vibrate(50);
			playClickSound(R.id.button_DiceR4C3);
			letter = getLetterFromDice(R.id.button_DiceR4C3);
			if(isSequenceIsValid(R.id.button_DiceR4C3)){
				try {
					process_Click(R.id.button_DiceR4C3);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_DiceR4C4:
			vib.vibrate(50);
			playClickSound(R.id.button_DiceR4C4);
			letter = getLetterFromDice(R.id.button_DiceR4C4);
			if(isSequenceIsValid(R.id.button_DiceR4C4)){
				try {
					process_Click(R.id.button_DiceR4C4);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_DiceR4C5:
			vib.vibrate(50);
			playClickSound(R.id.button_DiceR4C5);
			letter = getLetterFromDice(R.id.button_DiceR4C5);
			if(isSequenceIsValid(R.id.button_DiceR4C5)){
				try {
					process_Click(R.id.button_DiceR4C5);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_DiceR5C1:
			vib.vibrate(50);
			playClickSound(R.id.button_DiceR5C1);
			letter = getLetterFromDice(R.id.button_DiceR5C1);
			if(isSequenceIsValid(R.id.button_DiceR5C1)){
				try {
					process_Click(R.id.button_DiceR5C1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_DiceR5C2:
			vib.vibrate(50);
			playClickSound(R.id.button_DiceR5C2);
			letter = getLetterFromDice(R.id.button_DiceR5C2);
			if(isSequenceIsValid(R.id.button_DiceR5C2)){
				try {
					process_Click(R.id.button_DiceR5C2);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_DiceR5C3:
			vib.vibrate(50);
			playClickSound(R.id.button_DiceR5C3);
			letter = getLetterFromDice(R.id.button_DiceR5C3);
			if(isSequenceIsValid(R.id.button_DiceR5C3)){
				try {
					process_Click(R.id.button_DiceR5C3);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_DiceR5C4:
			vib.vibrate(50);
			playClickSound(R.id.button_DiceR5C4);
			letter = getLetterFromDice(R.id.button_DiceR5C4);
			if(isSequenceIsValid(R.id.button_DiceR5C4)){
				try {
					process_Click(R.id.button_DiceR5C4);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_DiceR5C5:
			vib.vibrate(50);
			playClickSound(R.id.button_DiceR5C5);
			letter = getLetterFromDice(R.id.button_DiceR5C5);
			if(isSequenceIsValid(R.id.button_DiceR5C5)){
				try {
					process_Click(R.id.button_DiceR5C5);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		}
	}

	public void onMultiplayerGameBackButtonClicked(View v){
		Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		Log.d(TAG,"Back button clicked");
		playClickSound(R.id.multiplayer_game_back);
		vib.vibrate(50);
		clearCurrentSet();
		finish(); 
	}

	public void onClearWordButtonClicked(View v){
		Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		Log.d(TAG,"Clear button clicked");
		playClickSound(R.id.multiplayer_game_clear_word);
		vib.vibrate(50);
		clearCurrentSet();
		enableAllButtons();
		swapAllButtonUnclick();
		clearWordTextView();
	}

	public void onSubmitWordButtonClicked(View v){
		Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		Log.d(TAG,"Submit button clicked");
		playClickSound(R.id.multiplayer_game_submit_word);
		Log.d(TAG, "submit clicked");
		vib.vibrate(50);
		//TODO if number of turns has reached the limit
		if (getBoggleWord().length() >=3 ){
			try {
				isWord();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
		Music.play(this, R.raw.boggle_bgm);

		letterSet = sa.getBooard(this.username, this.opponent);
		Log.d(TAG, "onResume letter set resume: "+ letterSet);
		if (letterSet != null){
			fillButtons(this.letterSet);
		}

		String temp_WordList = sa.getEnteredWords(username, opponent);
		Log.d(TAG, "onResume letter set word list: "+ temp_WordList);
		convert_onPause_wordList(temp_WordList);
		updateUsedWordsList();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
		Music.stop(this);

		//	Stops service
		//unbindService(mConnection);



		// Server side things

		// update turn
		// update scores
		// update turn total
		// update used words


	}

	// Picks a collection of letters from set of dice
	private String  pick_Letters(){
		Log.d(TAG, "Pick Dice");
		int num_dice = size * size;  
		List<Character> set_letters = new ArrayList<Character>(num_dice);
		for (int i = 0; i<num_dice; i++){
			int x = (int) Math.floor(Math.random() * 6);	// pick letter in list of characters
			int d = (int) Math.floor(Math.random() * dice_set.length); // pick dice in collection
			char c = dice_set[d].charAt(x);
			set_letters.add(c);
		}
		Collections.shuffle(set_letters);
		String final_letters = "";
		for (Character s : set_letters){
			final_letters += s;
		}
		//String final_letters = set_letters.toString().replace("," ,"").replace("[", "").replace("]", "").trim();
		Log.d(TAG, "Pick letters: " + final_letters);
		return final_letters;	   
	}

	// Fills the buttons with selected dice
	public void fillButtons(String  Letters){
		Log.d(TAG, Letters.toString());
		int num_dice = size * size; 
		for (int i = 0; i < num_dice; i++){
			Button b; 
			char c = Letters.charAt(i);
			Log.d(TAG, Character.toString(c));
			if (c == 'Q'){
				b = (Button)findViewById(BUTTONS[i]);
				b.setText("Qu");
			}else{
				b = (Button)findViewById(BUTTONS[i]);
				b.setText(Character.toString(c));
			}
		}
	}

	// checks whether the game is out of time
	public void checkGameOver(){
		//Log.d(TAG, "checkGameOver");
		if (this.game.getNumTurns() == 0){
			disableAllButtons();
			View formedWord = findViewById(R.id.boggle_word_textView);
			formedWord.setClickable(false);
			updateUserScore();
			sa.removeGame(username, opponent);			// removes the game from the server
			Toast.makeText(this, "GAME OVER", Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	// processes each button click
	private void process_Click(int diceID) throws IOException{
		int size = current_dice_set.size();
		if(current_dice_set.isEmpty()){
			Log.d(TAG, "process_Click:isEmpty");
			addToCurrentSet(diceID);
			swapButtonClick(diceID);
		}else if(current_dice_set.get(size-1) == diceID && !current_dice_set.isEmpty()){
			Log.d(TAG, "process_Click:peek==diceID");
			swapButtonUnclick(diceID);
			removeFromCurrentSet(diceID);
			enableButton();
		}else if(current_dice_set.get(size-1) != diceID){
			Log.d(TAG, "process_Click:peek!=diceID");
			disableButton();
			addToCurrentSet(diceID);
			swapButtonClick(diceID);
		}
		Log.d(TAG, "process_click: UpdateWordTextView");
		updateWordTextView();


		/*
			if (getBoggleWord().length() >=3 ){
				isWord();
			}
		 */
	}

	// plays click sound
	private void playClickSound(int diceID){
		Log.d(TAG, "playClickSound: "+ diceID);
		Button dice = (Button)findViewById(diceID);
		dice.playSoundEffect(android.view.SoundEffectConstants.CLICK);
	}

	// disable last button in set
	private void disableButton(){
		int size = current_dice_set.size();
		Log.d(TAG, "disableButton");
		if(!current_dice_set.isEmpty()){
			Button dice = (Button)findViewById(current_dice_set.get(size-1));
			dice.setClickable(false);
		}
	}	

	// enable last button in set
	private void enableButton(){
		int size = current_dice_set.size();
		if(!current_dice_set.isEmpty()){
			Button dice = (Button)findViewById(current_dice_set.get(size-1));
			dice.setClickable(true);
		}
	}

	// disables all buttons
	private void disableAllButtons(){
		Log.d(TAG, "disableAllButton");
		int size = BUTTONS.length;
		for (int i = 0; i < size; i++){
			Button dice = (Button)findViewById(BUTTONS[i]);
			dice.setClickable(false);
		}
	}

	// enables all button
	private void enableAllButtons(){
		Log.d(TAG, "enableAllButton");
		int size = BUTTONS.length;
		for (int i = 0; i < size; i++){
			Button dice = (Button)findViewById(BUTTONS[i]);
			dice.setClickable(true);
		}
	}

	// swap button to click state
	private void swapButtonClick(int diceID){
		Log.d(TAG,"swapButonClick");
		Button dice = (Button)findViewById(diceID);
		dice.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_default_pressed));
	}

	// Swap button to unclick state
	private void swapButtonUnclick(int diceID){
		Log.d(TAG,"swapButonUnClick");
		Button dice = (Button)findViewById(diceID);
		dice.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_default_normal));
	}

	// swap All buttons to Unclick state
	private void swapAllButtonUnclick(){
		Log.d(TAG,"swapButonUnClick");
		int size = BUTTONS.length;
		for (int i = 0; i < size; i++){
			Button dice = (Button)findViewById(BUTTONS[i]);
			dice.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_default_normal));
		}
	}

	// generates the onPause word list for storage
	private String generate_onPause_wordList(){
		String line = null;
		Log.d(TAG, "generate_onPause_wordList");
		for (String v : wordList){
			Log.d(TAG, "generate_onPause_wordList: "+ v);
			if (line == null){
				line = v;
			}else{
				line = line + " " + v;
			}
		}
		return line;
	}

	// clears the word text view
	private void clearWordTextView(){
		Log.d(TAG, "clearWordTextView");
		TextView ans = (TextView)findViewById(R.id.boggle_word_textView);
		ans.setText("");
	}

	// clears the word list set
	private void clearWordList(){
		wordList.clear();
	}

	// clears current dice set
	private void clearCurrentSet(){
		Log.d(TAG, "clearCurrentSet()");
		current_dice_set.clear();
	}

	//convert wordlist for getSharedPreferences storage
	private void convert_onPause_wordList(String w){
		String [] convert = w.split(" ");
		int size = convert.length;
		//wordList.clear();
		for (int i = 0; i < size; i++){
			wordList.add(convert[i]);
			Log.d(TAG, "convert_onPause_wordList: "+ wordList.toString());
		}

	}

	// add dice to current dice set
	private void addToCurrentSet(int dice){
		Log.d(TAG, "addToCurrentSet");
		current_dice_set.add(Integer.valueOf(dice));
		Log.d(TAG,"add to current_dice_set :"+Integer.toString(dice));
		//Log.d(TAG,"add to current_dice_set peek()"+Integer.toString(current_dice_set.get(size-1)));
		Log.d(TAG,"add to current_dice_set size()"+Integer.toString(current_dice_set.size()));
	}

	//remove dice from current dice set
	private void removeFromCurrentSet(int dice){
		int size = current_dice_set.size();
		if(current_dice_set.get(size-1) == dice){
			current_dice_set.remove(size-1);
		}
	}

	// updates player 1  score
	private void updateGameScore_P1(String word){
		Log.d(TAG, "updateScore P1");
		int add_points = scorePoints(word);
		this.score_p1 = this.score_p1 + add_points;

		Log.i(TAG, "additional points : " + Integer.toString(this.score_p1));
		Log.i(TAG, "current score : " + Integer.toString(this.score_p1));

		this.game.updateScore(username, this.score_p1);

		updateOverAllScores();
		setScore(R.id.multiplayer_player1, username, this.score_p1);
	}


	// update player 2 score
	private void updateGameScore_P2(String word){
		Log.d(TAG, "updateScore P2");
		int add_points = scorePoints(word);
		this.score_p2 = this.score_p2+ add_points;

		Log.i(TAG, "additional points : " + Integer.toString(this.score_p2));
		Log.i(TAG, "current score : " + Integer.toString(this.score_p2));

		this.game.updateScore(opponent, this.score_p2);

		updateOverAllScores();
		setScore(R.id.multiplayer_player2, opponent, this.score_p2);
	}



	// scoring points
	private int scorePoints(String w){
		int length = w.length();
		if(length == 3 || length == 4){
			return 1;
		}else{
			int points = length - 4;
			return points + 1;
		}
	}

	// get letter from dice
	private String getLetterFromDice(int diceID){
		Button dice = (Button)findViewById(diceID);
		String letter = dice.getText().toString();
		return letter;
	}

	// generates the word from dice set
	private String getStringfromSet(){

		String word = ""; 
		for (int i = 0; i < current_dice_set.size(); i++){
			String letter = getLetterFromDice(current_dice_set.get(i));
			word = word + letter;

		}
		String final_word = word;
		return final_word;
	}

	// sets the current score given the number
	private void setScore(int ID, String user, int s){
		Log.d(TAG, "setScore");
		TextView textView_score = (TextView)findViewById(ID);
		Log.d(TAG, "setScore ID: "+ ID);
		Log.d(TAG, user + "Score : " + textView_score.getText().toString());

		String score = user + "|" + s;
		textView_score.setText(score);
	}

	//gets the word in the word view
	private String getBoggleWord(){
		TextView boggle_word = (TextView)findViewById(R.id.boggle_word_textView);
		String word = boggle_word.getText().toString().trim();

		return word;
	}

	// updates word list set
	private void updateWordList(String word){
		Log.d(TAG, "updateWordList");
		wordList.add(word);
		//this.game.addEnteredWord(word);
		/**
		 * Server Side add word 
		 */
		Log.d(TAG, "updateWordList: " + wordList);
	}

	// updates the used word list
	private void updateUsedWordsList(){
		TextView usedWordsView = (TextView)findViewById(R.id.boggle_used_words);
		String temp = generate_onPause_wordList();
		this.updateUsedWords();
		usedWordsView.setText(temp);
	}

	// updates the word text view
	private void updateWordTextView(){
		Log.d(TAG, "updateWordTextView");
		String final_word = getStringfromSet();
		TextView ans = (TextView)findViewById(R.id.boggle_word_textView);
		ans.setText(final_word.toUpperCase().trim());
	}

	// checks whether the word in word view is valid word in the dictionary
	private void isWord() throws IOException{
		Log.d(TAG,"isWord");

		Dictionary dict = new Dictionary();
		String targetWord = getBoggleWord().trim().toLowerCase();
		int wordLength = targetWord.length();

		boolean check = dict.searchDict(this.getApplicationContext(), wordLength, targetWord);

		if (check){
			Log.d(TAG,"isWord: PASS");
			if(isUsedWord(targetWord) == false){
				Music.playClip(this, R.raw.correct_word);
				if(this.current_turn.equals(this.username)){
					updateGameScore_P1(targetWord);
					this.game.setCurrentTurn(this.opponent);
					this.game.incrementNumTurns();
					setCurrentTurnIcon(this.opponent);
				}else if(this.current_turn.equals(this.opponent)){
					updateGameScore_P2(targetWord);
					this.current_turn = this.username;
					this.game.setCurrentTurn(this.username);
					this.game.incrementNumTurns();
					setCurrentTurnIcon(this.username);
				}
				updateWordList(targetWord);

				clearCurrentSet();

				enableAllButtons();

				swapAllButtonUnclick();

				clearWordTextView();

				updateUsedWordsList();

				changeSubmitButtonState();
			}
		}else{
			Log.d(TAG,"isWord: Fail");
		}
	}

	protected void changeSubmitButtonState(){
		if (! (this.game.getCurrentTurn().equals(username))){
			Log.d(TAG, "Disable Submit Button");
			Button submitButton = (Button)findViewById(R.id.multiplayer_game_submit_word);
			//submitButton.setClickable(false);
			submitButton.setEnabled(false);
		}else{
			Log.d(TAG, "enable Submit Button");
			Button submitButton = (Button)findViewById(R.id.multiplayer_game_submit_word);
			//submitButton.setClickable(true);
			submitButton.setEnabled(true);
		}
	}

	protected void setCurrentTurnIcon(String user){
		TextView tv_Turns =(TextView) findViewById(R.id.multiplayer_current_turn);
		if (this.username.equals(user)){
			tv_Turns.setText("<<");
		}else if(this.opponent.equals(user)){
			tv_Turns.setText(">>");
		}
	}

	//check whether the current dice is already in the current set
	private boolean isInCurrentSet(int dice){
		if (current_dice_set.contains(dice)){
			return true;
		}else{
			return false;
		}
	}

	// checks whether the sequence of letters is valid
	private boolean isSequenceIsValid(int new_Dice){
		Dice gameDice = new Dice();
		Log.d(TAG, "checkSequenceIsValid");

		int size = current_dice_set.size();
		Log.d(TAG, "size :" + Integer.toString(size));
		if (size == 0){
			Log.d(TAG, "size == 0: isValid");
			return true;
		}else if(size > 0){
			int prev_dice = current_dice_set.get(size-1);
			Log.d(TAG, "size > 0: isValid");
			return gameDice.isValid(prev_dice, new_Dice);
		}else{
			return false;
		}
	}

	// checks whether a word is already used
	private boolean isUsedWord(String s){
		Log.d(TAG, "checkUsedWords");
		int size = wordList.size();
		for (int i = 0; i < size; i++){
			Log.d(TAG, "checkUsedWords " + wordList.get(i));
			if (wordList.get(i).equalsIgnoreCase(s)){
				return true;
			}
		}
		return false;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////
	//
	//									ASYNC Tasks
	//
	//
	//
	//
	//
	//
	//
	////////////////////////////////////////////////////////////////////////////////////////////////	
	// updates the server side scoes
	private void updateOverAllScores(){
		/**
		 * 
		 */
		sa.setScoreContent(username, this.score_p1, opponent, this.score_p2);
	}
	// updates the server side current turn
	private void updateCurrentTurn(){
		sa.setTurn(username, opponent, this.game.getCurrentTurn());
	}

	// updates the serverside turn totals
	private void updateTurnTotals(){
		sa.setTurnTotal(username, opponent, this.game.getNumTurns(), new OnBooleanReceivedListener() {
			
			public void run(Boolean exitState) {
				if (!exitState) {
					Log.e(TAG, "uh oh.  Turn totals notupdated");
				}
				
			}
		});
	}

	// updates the serverside user words lists
	private void updateUsedWords(){
		sa.setEnteredWords(username, opponent, generate_onPause_wordList(), new OnBooleanReceivedListener() {
			
			public void run(Boolean exitState) {
				if (!exitState) {
					Log.e(TAG, "Couldn't set the used word list on the server");
				}
			}
		});
	}

	private void updateUserScore(){
		int serverScore_P1 = sa.getHighscore(username);
		int serverScore_P2 = sa.getHighscore(username);

		if(serverScore_P1 < this.score_p1){
			sa.setHighscore(username, this.score_p1);
		}
		if(serverScore_P2 < this.score_p2){
			sa.setHighscore(opponent, this.score_p2);
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////
	//
	//							SERVICE STUFFSERVICE STUFFSERVICE STUFF
	//SERVICE STUFFSERVICE STUFFSERVICE STUFFSERVICE STUFFSERVICE STUFFSERVICE STUFFSERVICE STUFF
	//SERVICE STUFFSERVICE STUFFSERVICE STUFFSERVICE STUFFSERVICE STUFFSERVICE STUFFSERVICE STUFF
	//SERVICE STUFFSERVICE STUFFSERVICE STUFFSERVICE STUFFSERVICE STUFFSERVICE STUFFSERVICE STUFF
	//SERVICE STUFFSERVICE STUFFSERVICE STUFFSERVICE STUFFSERVICE STUFFSERVICE STUFFSERVICE STUFF
	//SERVICE STUFFSERVICE STUFFSERVICE STUFFSERVICE STUFFSERVICE STUFFSERVICE STUFFSERVICE STUFF
	//SERVICE STUFFSERVICE STUFFSERVICE STUFFSERVICE STUFFSERVICE STUFFSERVICE STUFFSERVICE STUFF
	//							SERVICE STUFFSERVICE STUFFSERVICE STUFF
	//
	/////////////////////////////////////////////////////////////////////////////////////////////////	
	/*
	private ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder binder) {
			service = ((Multiplayer_Game_Service.MyBinder) binder).getService();
			Toast.makeText(Multiplayer_Game.this, "Connected",
					Toast.LENGTH_SHORT).show();
		}

		public void onServiceDisconnected(ComponentName className) {
			service = null;
		} 
	};

	public void doBindService() {
		Log.d(TAG, "dobindservice");
		bindService(new Intent(this, Multiplayer_Game_Service.class), mConnection,
				Context.BIND_AUTO_CREATE);
	}

	public void showServiceData() {
		if (service != null) {
			Toast.makeText(this, "Number of elements" + service.getCurrentTurn(),
					Toast.LENGTH_SHORT).show();
		}
	}
 */
	public boolean isYourTurn(){
		//service.getCurrentTurn();
		String serverCurrentTurn = this.game.getCurrentTurn();
		if (this.game.getCurrentTurn().equals(serverCurrentTurn)){
			return false;
		}else{
			return true;
		} 

	}
	


	////////////////////////////////////////////////////////////////////////////////////////////////
	//
	//							BACKGROUND BACKGROUND SERVICE STUFFBACKGROUND 
	//BACKGROUND BACKGROUND SERVICE STUFFBACKGROUND BACKGROUND SERVICE STUFFBACKGROUND BACKGROUND
	//BACKGROUND BACKGROUND SERVICE STUFFBACKGROUND BACKGROUND SERVICE STUFFBACKGROUND BACKGROUND 
	//BACKGROUND BACKGROUND SERVICE STUFFBACKGROUND BACKGROUND SERVICE STUFFBACKGROUND BACKGROUND 
	//BACKGROUND BACKGROUND SERVICE STUFFBACKGROUND BACKGROUND SERVICE STUFFBACKGROUND BACKGROUND 
	//BACKGROUND BACKGROUND SERVICE STUFFBACKGROUND BACKGROUND SERVICE STUFFBACKGROUND BACKGROUND 
	//BACKGROUND BACKGROUND SERVICE STUFFBACKGROUND BACKGROUND SERVICE STUFFBACKGROUND BACKGROUND 
	//							BACKGROUND BACKGROUND SERVICE STUFFBACKGROUND 
	/////////////////////////////////////////////////////////////////////////////////////////////////


	public void foo(){
		Intent i = new Intent(this, Multiplayer_Game.class);
		i.putExtra("username", this.username);
		i.putExtra("opponent", this.opponent);
		i.putExtra("currentTurn", this.game.getCurrentTurn());

	}
}

