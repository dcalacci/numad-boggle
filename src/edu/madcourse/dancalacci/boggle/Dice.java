package edu.madcourse.dancalacci.boggle;

import android.app.Activity;
import android.util.Log;
import edu.madcourse.dancalacci.R;

public class Dice extends Activity{

	private static final String TAG = "BoggleDice";

	private int[] DiceR1C1 = {
			R.id.button_DiceR1C2,
			R.id.button_DiceR2C1,
			R.id.button_DiceR2C2
	};

	private int[] DiceR1C2 = {
			R.id.button_DiceR1C1,
			R.id.button_DiceR1C3,
			R.id.button_DiceR2C1,
			R.id.button_DiceR2C2,
			R.id.button_DiceR2C3
	};

	private int[] DiceR1C3 = {
			R.id.button_DiceR1C2,
			R.id.button_DiceR1C4,
			R.id.button_DiceR2C2,
			R.id.button_DiceR2C3,
			R.id.button_DiceR2C4
	};

	private int[] DiceR1C4 = {
			R.id.button_DiceR1C3,
			R.id.button_DiceR1C5,
			R.id.button_DiceR2C3,
			R.id.button_DiceR2C4,
			R.id.button_DiceR2C5
	};

	private int[] DiceR1C5 = {
			R.id.button_DiceR1C4,
			R.id.button_DiceR2C4,
			R.id.button_DiceR2C5,
	};

	private int[] DiceR2C1 = {
			R.id.button_DiceR1C1,
			R.id.button_DiceR1C2,
			R.id.button_DiceR2C2,
			R.id.button_DiceR3C1,
			R.id.button_DiceR3C2
	};

	private int[] DiceR2C2 = {
			R.id.button_DiceR1C1,
			R.id.button_DiceR1C2,
			R.id.button_DiceR1C3,
			R.id.button_DiceR2C1,
			R.id.button_DiceR2C3,
			R.id.button_DiceR3C1,
			R.id.button_DiceR3C2,
			R.id.button_DiceR3C3
	};

	private int[] DiceR2C3 = {
			R.id.button_DiceR1C2,
			R.id.button_DiceR1C3,
			R.id.button_DiceR1C4,
			R.id.button_DiceR2C2,
			R.id.button_DiceR2C4,
			R.id.button_DiceR3C2,
			R.id.button_DiceR3C3,
			R.id.button_DiceR3C4
	};

	private int[] DiceR2C4 = {
			R.id.button_DiceR1C3,
			R.id.button_DiceR1C4,
			R.id.button_DiceR1C5,
			R.id.button_DiceR2C3,
			R.id.button_DiceR2C5,
			R.id.button_DiceR3C3,
			R.id.button_DiceR3C4,
			R.id.button_DiceR3C5
	};

	private int[] DiceR2C5 = {
			R.id.button_DiceR1C4,
			R.id.button_DiceR1C5,
			R.id.button_DiceR2C4,
			R.id.button_DiceR3C4,
			R.id.button_DiceR3C5
	};

	private int[] DiceR3C1 = {
			R.id.button_DiceR2C1,
			R.id.button_DiceR2C2,
			R.id.button_DiceR3C2,
			R.id.button_DiceR4C1,
			R.id.button_DiceR4C2
	};

	private int[] DiceR3C2 = {
			R.id.button_DiceR2C1,
			R.id.button_DiceR2C2,
			R.id.button_DiceR2C3,
			R.id.button_DiceR3C1,
			R.id.button_DiceR3C3,
			R.id.button_DiceR4C1,
			R.id.button_DiceR4C2,
			R.id.button_DiceR4C3
	};

	private int[] DiceR3C3 = {
			R.id.button_DiceR2C2,
			R.id.button_DiceR2C3,
			R.id.button_DiceR2C4,
			R.id.button_DiceR3C2,
			R.id.button_DiceR3C4,
			R.id.button_DiceR4C2,
			R.id.button_DiceR4C3,
			R.id.button_DiceR4C4
	};

	private int[] DiceR3C4 = {
			R.id.button_DiceR2C3,
			R.id.button_DiceR2C4,
			R.id.button_DiceR2C5,
			R.id.button_DiceR3C3,
			R.id.button_DiceR3C5,
			R.id.button_DiceR4C3,
			R.id.button_DiceR4C4,
			R.id.button_DiceR4C5
	};

	private int[] DiceR3C5 = {
			R.id.button_DiceR2C4,
			R.id.button_DiceR2C5,
			R.id.button_DiceR3C4,
			R.id.button_DiceR4C4,
			R.id.button_DiceR4C5
	};

	private int[] DiceR4C1 = {
			R.id.button_DiceR4C2,
			R.id.button_DiceR3C1,
			R.id.button_DiceR3C2,
			R.id.button_DiceR5C1,
			R.id.button_DiceR5C2,
	};

	private int[] DiceR4C2 = {
			R.id.button_DiceR4C1,
			R.id.button_DiceR4C3,
			R.id.button_DiceR3C1,
			R.id.button_DiceR3C2,
			R.id.button_DiceR3C3,
			R.id.button_DiceR5C1,
			R.id.button_DiceR5C2,
			R.id.button_DiceR5C3,
	};

	private int[] DiceR4C3 = {
			R.id.button_DiceR4C2,
			R.id.button_DiceR4C4,
			R.id.button_DiceR3C2,
			R.id.button_DiceR3C3,
			R.id.button_DiceR3C4,
			R.id.button_DiceR5C2,
			R.id.button_DiceR5C3,
			R.id.button_DiceR5C4,
	};

	private int[] DiceR4C4 = {
			R.id.button_DiceR4C3,
			R.id.button_DiceR4C5,
			R.id.button_DiceR3C3,
			R.id.button_DiceR3C4,
			R.id.button_DiceR3C5,
			R.id.button_DiceR5C3,
			R.id.button_DiceR5C4,
			R.id.button_DiceR5C5
	};

	private int[] DiceR4C5 = {
			R.id.button_DiceR4C4,
			R.id.button_DiceR3C4,
			R.id.button_DiceR3C5,
			R.id.button_DiceR5C4,
			R.id.button_DiceR5C5
	};
	
	private int[] DiceR5C1 = {
			R.id.button_DiceR4C1,
			R.id.button_DiceR4C2,
			R.id.button_DiceR5C2
	};

	private int[] DiceR5C2 = {
			R.id.button_DiceR5C1,
			R.id.button_DiceR5C3,
			R.id.button_DiceR4C1,
			R.id.button_DiceR4C2,
			R.id.button_DiceR4C3
	};

	private int[] DiceR5C3 = {
			R.id.button_DiceR5C2,
			R.id.button_DiceR5C4,
			R.id.button_DiceR4C2,
			R.id.button_DiceR4C3,
			R.id.button_DiceR4C4
	};

	private int[] DiceR5C4 = {
			R.id.button_DiceR5C3,
			R.id.button_DiceR5C5,
			R.id.button_DiceR4C3,
			R.id.button_DiceR4C4,
			R.id.button_DiceR4C5,
	};

	private int[] DiceR5C5 = {
			R.id.button_DiceR5C4,
			R.id.button_DiceR4C4,
			R.id.button_DiceR4C5,
	};

	public Dice() {
		// TODO Auto-generated constructor stub
	}

	// checks whether the dice is valid
	public boolean isValid (int previous_Dice, int current_Dice){
		Log.d(TAG, "isValid");
		int[] diSet = getDiceSet(previous_Dice);
		Log.d(TAG, Integer.toString(previous_Dice));
		Log.d(TAG, Integer.toString(current_Dice));

		if (previous_Dice == current_Dice){
			return true;
		}else{
			for (int i = 0; i < diSet.length; i++){
				if (diSet[i] == current_Dice){
					Log.d(TAG, "PASS");
					return true;
				}
			}
		}
		return false;
	}

	// gets the the dice based on the dice ID 
	private int[] getDiceSet(int dice_ID){
		switch (dice_ID) {
		case R.id.button_DiceR1C1:
			return DiceR1C1;
		case R.id.button_DiceR1C2:
			return DiceR1C2;
		case R.id.button_DiceR1C3:
			return DiceR1C3;
		case R.id.button_DiceR1C4:
			return DiceR1C4;
		case R.id.button_DiceR1C5:
			return DiceR1C5;
		case R.id.button_DiceR2C1:
			return DiceR2C1;
		case R.id.button_DiceR2C2:
			return DiceR2C2;
		case R.id.button_DiceR2C3:
			return DiceR2C3;
		case R.id.button_DiceR2C4:
			return DiceR2C4;
		case R.id.button_DiceR2C5:
			return DiceR2C5;
		case R.id.button_DiceR3C1:
			return DiceR3C1;
		case R.id.button_DiceR3C2:
			return DiceR3C2;
		case R.id.button_DiceR3C3:
			return DiceR3C3;
		case R.id.button_DiceR3C4:
			return DiceR3C4;
		case R.id.button_DiceR3C5:
			return DiceR3C5;
		case R.id.button_DiceR4C1:
			return DiceR4C1;
		case R.id.button_DiceR4C2:
			return DiceR4C2;
		case R.id.button_DiceR4C3:
			return DiceR4C3;
		case R.id.button_DiceR4C4:
			return DiceR4C4;
		case R.id.button_DiceR4C5:
			return DiceR4C5;
		case R.id.button_DiceR5C1:
			return DiceR5C1;
		case R.id.button_DiceR5C2:
			return DiceR5C2;
		case R.id.button_DiceR5C3:
			return DiceR5C3;
		case R.id.button_DiceR5C4:
			return DiceR5C4;
		case R.id.button_DiceR5C5:
			return DiceR5C5;
		}
		return null;

	}


}
