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
import java.util.List;
import java.util.Timer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import edu.madcourse.dancalacci.R;

public class Game extends Activity implements OnClickListener {
	private static final String TAG = "BoggleGame";
	public static final String PREF_BOGGLE = "prefBoggle";
	private static final String PREF_DICE = "puzzleDice" ;
	private static final String PREF_SCORE = "boogleScore";
	private static final String PREF_USED_WORDS = "boggleUsedWords";
	private static final String PREF_TIME = "boggleTime";
	public static final String PREF_RESUME = "boggleResume";

	public static final int DIFFICULTY_EASY = 0;
	public static final int DIFFICULTY_MEDIUM = 1;
	public static final int DIFFICULTY_HARD = 2;
	protected static final int DIFFICULTY_CONTINUE = -1;
	
	private SharedPreferences sf;
	private int size = 5;
	protected int current_score = 0; 	// current game score
	public String letterSet;		// letters used in game
	private String onPauseLetters; 
	private ArrayList<String> wordList= new ArrayList<String>();			// string of accepted words seperated by spaces
	protected int currentTime = 180; // start time in seconds
	protected int what = 1;	// handler id
	private Handler mHandler = new Handler(){	// stop watch handler

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			this.sendMessageDelayed(Message.obtain(this, what),1000);
			if(currentTime == 0 ){
				mHandler.removeMessages(what);
				updateTimeUI();
				checkGameOver();

			}else{
				currentTime -= 1;
				updateTimeUI();
				checkGameOver();
			}
		} 

	};
	Timer myTimer = new Timer();	
	private TextView mTimeLabel;

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

	protected void setLetterSet(String l){
		this.letterSet = l;
	}
	
	protected String getLetterSet(){
		Log.d(TAG, "getLetterSet");
		Log.d(TAG,"Return LetterSet:" +this.letterSet);
		return letterSet;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");

		setContentView(R.layout.boggle_game);
		if(getIntent().getBooleanExtra(PREF_RESUME, false) == false){
			Log.d(TAG, "newGame");
			getSharedPreferences(PREF_BOGGLE, MODE_PRIVATE).edit().clear().commit();
		}
		Log.d(TAG, "current set: "+current_dice_set.toString());
		String letters = pick_Letters();
		setLetterSet(letters);
		onPauseLetters = letters;
		
		sf = this.getSharedPreferences(PREF_BOGGLE, MODE_PRIVATE);
		
		Log.d(TAG, "onCreate Letterset: "+letterSet);
		Log.d(TAG, "onCreate tempLetterset: "+ onPauseLetters);
		// Set Default score;
		setScore(current_score);

		mTimeLabel = (TextView) findViewById(R.id.boggle_time);
		updateTimeUI();
		mHandler.sendMessageDelayed(Message.obtain(mHandler, what), 1000);

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

		View button_Pause = findViewById(R.id.button_boggle_pause);
		button_Pause.setOnClickListener((OnClickListener) this);

		View button_Clear = findViewById(R.id.button_Boggle_Clear);
		button_Clear.setOnClickListener((OnClickListener) this);

		View button_Submit= findViewById(R.id.button_Boggle_Submit);
		button_Submit.setOnClickListener((OnClickListener) this);

		View formedWord = findViewById(R.id.boggle_word_textView);
		formedWord.setOnClickListener((OnClickListener) this);

		View usedWordsList = findViewById(R.id.boggle_used_words);
		usedWordsList.setOnClickListener((OnClickListener) this);

		fillButtons(this.letterSet);

	}

	public void onClick(View v) {
		Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		String letter;

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
		case R.id.button_Boggle_Clear:
			Sounds.playClear(this);
			//playClickSound(R.id.button_Boggle_Clear);
			vib.vibrate(50);
			clearCurrentSet();
			enableAllButtons();
			swapAllButtonUnclick();
			clearWordTextView();
			break;
		case R.id.button_Boggle_Submit:
			playClickSound(R.id.button_Boggle_Submit);
			Log.d(TAG, "submit clicked");
			vib.vibrate(50);
			if (getBoggleWord().length() >=3 ){
				try {
					isWord();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		case R.id.button_boggle_pause:
			playClickSound(R.id.button_boggle_pause);
			vib.vibrate(50);
			mHandler.removeMessages(what);
			clearCurrentSet();
			finish(); 
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
		Music.play(this, R.raw.boggle_bgm);
		
		Log.d(TAG, "onResume letter set resume direct: "+ sf.getString(PREF_DICE, null));	
		
		letterSet = sf.getString(PREF_DICE, null);
		Log.d(TAG, "onResume letter set resume: "+ letterSet);
		if (letterSet != null){
			fillButtons(this.letterSet);
		}
		
		current_score = sf.getInt(PREF_SCORE, 0);
		setScore(current_score);
		Log.d(TAG, "onResume letter set score: "+ current_score);
		
		String temp_WordList = sf.getString(PREF_USED_WORDS, " ");
		Log.d(TAG, "onResume letter set word list: "+ temp_WordList);
		convert_onPause_wordList(temp_WordList);
		updateUsedWordsList();
		
		currentTime = sf.getInt(PREF_TIME, 180);
		updateTimeUI();
		
		sf.getBoolean(PREF_RESUME, false);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
		Music.stop(this);

		// Save the current puzzle

		sf.edit().putString(PREF_DICE, onPauseLetters).commit();
		Log.d(TAG, "temp letter set pause: "+ onPauseLetters);

		sf.edit().putInt(PREF_SCORE, current_score).commit();
		Log.d(TAG, "temp letter set score: "+ current_score);
		
		String wordList_Pref = generate_onPause_wordList();
		sf.edit().putString(PREF_USED_WORDS, wordList_Pref).commit();
		Log.d(TAG, "temp letter set used wrods: "+ wordList_Pref);
		
		sf.edit().putInt(PREF_TIME, currentTime).commit();
		Log.d(TAG, "temp letter set time: "+ currentTime);
		
		sf.edit().putBoolean(PREF_RESUME, true).commit();
		Log.d(TAG, "temp letter set pause: "+ PREF_RESUME);
		//TODO: CURRENT TIME
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


	// Updates time UI
	public void updateTimeUI(){
		int seconds = currentTime;
		int minutes = seconds / 60;
		seconds     = seconds % 60;

		if (seconds < 10) {
			mTimeLabel.setText("" + minutes + ":0" + seconds);
		} else {
			mTimeLabel.setText("" + minutes + ":" + seconds);            
		}
	}

	// checks whether the game is out of time
	public void checkGameOver(){
		//Log.d(TAG, "checkGameOver");
		if (currentTime == 0){
			disableAllButtons();
			View formedWord = findViewById(R.id.boggle_word_textView);
			formedWord.setClickable(false);
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

	// updates the current score
	private void updateScore(String word){
		Log.d(TAG, "updateScore");
		int add_points = scorePoints(word);
		current_score = current_score + add_points;
		Log.i(TAG, "additional points : " + Integer.toString(current_score));
		Log.i(TAG, "current score : " + Integer.toString(current_score));
		setScore(current_score);
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
	private void setScore(int s){
		Log.d(TAG, "setScore");
		TextView textView_score = (TextView)findViewById(R.id.boggle_score);

		Log.i(TAG, "current score : " + textView_score.getText().toString());
		textView_score.setText(Integer.toString(s));
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
		Log.d(TAG, "updateWordList: " + wordList);
	}

	// updates the used word list
	private void updateUsedWordsList(){
		TextView usedWordsView = (TextView)findViewById(R.id.boggle_used_words);
		String temp = generate_onPause_wordList();
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
				Sounds.playRight(this);
				//Music.playClip(this, R.raw.correct_word);
				updateScore(targetWord);
				updateWordList(targetWord);
				clearCurrentSet();
				enableAllButtons();
				swapAllButtonUnclick();
				clearWordTextView();
				updateUsedWordsList();
			}	
		}else{
			// play wrong sound, clear board selected, etc.
			Sounds.playWrong(this);
			clearCurrentSet();
			enableAllButtons();
			swapAllButtonUnclick();
			clearWordTextView();	
			
			Log.d(TAG,"isWord: Fail");
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
}
