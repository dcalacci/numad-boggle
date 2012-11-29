package edu.madcourse.dancalacci.circletouch;

import edu.madcourse.dancalacci.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Profile extends Activity{

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.platechart_profile);
	}
	
	public void onAddChartClick(View v){
		Intent i = new Intent(this, AddChart.class);
		startActivity(i);
	}
}
