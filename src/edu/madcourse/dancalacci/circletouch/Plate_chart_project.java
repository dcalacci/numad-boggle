package edu.madcourse.dancalacci.circletouch;

import edu.madcourse.dancalacci.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class Plate_chart_project extends Activity{

	@Override
  protected void onCreate(Bundle savedInstanceState) {
	  // TODO Auto-generated method stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	  super.onCreate(savedInstanceState);
    setContentView(R.layout.plate_chart_project_requirements);
  }

	@Override
  protected void onResume() {
	  // TODO Auto-generated method stub
	  super.onResume();
  }
	
	public void OnProjectStartClicked(View v){
		Intent i = new Intent(this, PlateChart_main.class);
    startActivity(i);
	}

}
