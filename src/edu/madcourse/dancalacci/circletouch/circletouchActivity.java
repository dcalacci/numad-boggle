package edu.madcourse.dancalacci.circletouch;

import edu.madcourse.dancalacci.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class circletouchActivity extends Activity {
    /** Called when the activity is first created. */
	circle cir;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circletouch_main);
        final circle c = (circle) this.findViewById(R.id.circle);
        cir = c;
    }
    
    public void onProteinClicked(View v){
    	cir.onProteinClicked(v);
	}

	public void onVegetableClicked(View v){
		cir.onVegetableClicked(v);
	}

	public void onDairyClicked(View v){
		cir.onDairyClicked(v);
	}

	public void onFruitClicked(View v){
		cir.onFruitClicked(v);
	}

	public void onGrainClicked(View v){
		cir.onGrainClicked(v);
	}

	public void onOilSugarClicked(View v){
		cir.onOilSugarClicked(v);
	}
}