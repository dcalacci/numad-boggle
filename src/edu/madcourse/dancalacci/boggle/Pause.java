package edu.madcourse.dancalacci.boggle;

import edu.madcourse.dancalacci.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class Pause extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.boggle_pause);
	}
	
	public void onResumeButtonClicked(View v) {
		finish();
	}
}