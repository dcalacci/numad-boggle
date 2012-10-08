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
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import edu.madcourse.dancalacci.R;
import android.widget.Button;
import java.io.*;
import edu.madcourse.bloomfilter.*;
import java.util.BitSet;


public class Boggle extends Activity {
private static final String PREF_BOARD = "board";

// create a new bloom filter with the optimal number of # of hash functions and bits
// this particular combination of k and m should give us p = 1E-20 probability of a false positive
public static BloomFilter<String> bloom;

   private static final String TAG = "Sudoku";
   
   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.boggle_main);
      
      this.setContinueButtonVis();
      setTitle("Boggle");
      
      this.populateBloomFilter();
   }

   
   protected void populateBloomFilter() {
	   BitSet bs;
	   try {
		   AssetManager assets = getAssets();
		   AssetFileDescriptor afd = assets.openFd("bitset.data");
		   FileInputStream fis = afd.createInputStream();
		   //InputStream iStream = getAssets().open("bitSet.data");
		   ObjectInputStream obj_in = new ObjectInputStream(fis);
		   
		   Object obj = obj_in.readObject();
		   bs = (BitSet)obj;
		   // 33 is the number of hash functions calculated to 
		   // provide the best probability with the wordlist in use.
		   bloom = new BloomFilter<String>(bs.size(), 432334, 432334, bs);
	   } catch (IOException e) {
		   Log.wtf("BITVECTOR_READWRITE", e);
	   } catch (ClassNotFoundException e) {
		   Log.wtf("OBJECT_READ", e);
	   }
   }
   
   @Override
   protected void onResume() {
      super.onResume();
      //Music.play(this, R.raw.sudoku_main);
      this.setContinueButtonVis();
   }

   @Override
   protected void onPause() {
      super.onPause();
     // Music.stop(this);
   }
   
   public void OnNewGameButtonClicked(View v) {
	   Intent i = new Intent(this, Game.class);
	   i.putExtra(Game.TO_CONTINUE, 0);
	   startActivity(i);
   }
   
   public void OnContinueGameButtonClicked(View v) {
	   Intent i = new Intent(this, Game.class);
	   i.putExtra(Game.TO_CONTINUE, 1);
	   startActivity(i);
   }
   
   /**
    * Sets the continue button's visibility based on the saved data 
    * in the application - if there is a saved board, it shows the 
    * continue button.  If there is not, the continue button is set to invisible.
    */
   private void setContinueButtonVis() {
	      Button continue_button = (Button)findViewById(R.id.boggle_continue_game_button);
	      if (!getSharedPreferences(Game.BOGGLE_PREF, MODE_PRIVATE).contains(PREF_BOARD)) {
	    	  continue_button.setVisibility(View.INVISIBLE);
	      } else {
	    	  continue_button.setVisibility(View.VISIBLE);
	      }
   }

}