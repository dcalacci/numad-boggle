package edu.madcourse.dancalacci;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.content.Intent;
import edu.madcourse.dancalacci.sudoku.*;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);     
        setContentView(R.layout.activity_main);
        setTitle("Dan Calacci");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    // Team Members Button etc. TODO
    public void OnTeamMembersButtonClicked(View v) {
    	Intent i = new Intent(this, TeamMembers.class);
    	startActivity(i);
    }
    
    public void OnCreateErrorButtonClicked(View v) {
    	throw new RuntimeException("This is an Error");
    }
    
    public void OnSudokuButtonClicked(View v) {
    	Intent i = new Intent(this, Sudoku.class);
    	startActivity(i);
    }
}
