package edu.madcourse.dancalacci.circletouch;

import edu.madcourse.dancalacci.R;
import edu.madcourse.dancalacci.boggle.Game;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class History extends Activity{

	History_View hist;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.platechart_history);
        final History_View h = (History_View) this.findViewById(R.id.historyView1);
        hist = h;
	}

	public void onAddChartClick(View v){
		Intent i = new Intent(this, AddChart.class);
		startActivity(i);
	}
	
}
