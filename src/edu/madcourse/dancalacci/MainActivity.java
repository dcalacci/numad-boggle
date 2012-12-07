package edu.madcourse.dancalacci;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.content.Intent;
import edu.madcourse.dancalacci.sudoku.*;
import edu.madcourse.dancalacci.boggle.*;
import edu.madcourse.dancalacci.circletouch.PlateChart_main;
import edu.madcourse.dancalacci.circletouch.Plate_chart_project;
import edu.neu.mobileClass.PhoneCheckAPI;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// NUMAD Authorization
    	 PhoneCheckAPI.doAuthorization(this);
    	
    	//starting the activity
        super.onCreate(savedInstanceState);     
        setContentView(R.layout.activity_main);
        setTitle("Dan Calacci");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public void OnTeamMembersButtonClicked(View v) {
    	Intent i = new Intent(this, TeamMembers.class);
    	startActivity(i);
    }
    
    public void OnCreateErrorButtonClicked(View v) {
    	// Throwing an error for create_error button
    	throw new RuntimeException("This intentional Error created by Create Error button.");
    }
    
    public void OnSudokuButtonClicked(View v) {
    	Intent i = new Intent(this, Sudoku.class);
    	startActivity(i);
    }
    
    public void OnBoggleButtonClicked(View v) {
    	Intent i = new Intent(this, Boggle.class);
    	startActivity(i);
    }
    
    public void OnTrickiestPartButtonClicked(View v) {
    	Intent i = new Intent(this, Plate_chart_project.class);
    	i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    	startActivity(i);
    }
}
