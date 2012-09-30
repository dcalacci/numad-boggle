/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband3 for more book information.
***/
package edu.madcourse.dancalacci.boggle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import edu.madcourse.dancalacci.R;

public class Boggle extends Activity {
   private static final String TAG = "Sudoku";
   
   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.boggle_main);
      
      setTitle("Boggle");
   }

   @Override
   protected void onResume() {
      super.onResume();
      //Music.play(this, R.raw.sudoku_main);
   }

   @Override
   protected void onPause() {
      super.onPause();
     // Music.stop(this);
   }
   
   public void OnNewGameButtonClicked(View v) {
	   Intent i = new Intent(this, Game.class);
	   startActivity(i);
   }

//   public void onClick(View v) {
//      switch (v.getId()) {
//      case R.id.continue_button:
//      //   startGame(Game.DIFFICULTY_CONTINUE);
//    	  startGame();
//         break;
//         // ...
//      case R.id.about_button:
//         Intent i = new Intent(this, About.class);
//         startActivity(i);
//         break;
//      // More buttons go here (if any) ...
//      case R.id.new_button:
//         openNewGameDialog();
//         break;
//      case R.id.exit_button:
//         finish();
//         break;
//      }
//   }
   
//   @Override
//   public boolean onCreateOptionsMenu(Menu menu) {
//      super.onCreateOptionsMenu(menu);
//      MenuInflater inflater = getMenuInflater();
//      inflater.inflate(R.menu.sudoku_menu, menu);
//      return true;
//   }

//   @Override
//   public boolean onOptionsItemSelected(MenuItem item) {
//      switch (item.getItemId()) {
//      case R.id.settings:
//         startActivity(new Intent(this, Prefs.class));
//         return true;
//      // More items go here (if any) ...
//      }
//      return false;
//   }
}