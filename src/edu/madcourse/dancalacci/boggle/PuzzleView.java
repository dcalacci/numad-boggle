/***

 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband3 for more book information.
***/

package edu.madcourse.dancalacci.boggle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import edu.madcourse.dancalacci.R;


public class PuzzleView extends View {
   
   private static final String TAG = "Boggle";

   private static final String VIEW_STATE = "viewState";
   private static final int ID = 42; 

   
   private float width;    // width of one tile
   private float height;   // height of one tile
   private int selX;       // X index of selection
   private int selY;       // Y index of selection
   private final Rect selRect = new Rect();

   private final Game game;
   
   public PuzzleView(Context context) {
      
      super(context);
      this.game = (Game) context;
      setFocusable(true);
      setFocusableInTouchMode(true);
      
      // ...
      setId(ID); 
   }
   
   @Override
   protected Parcelable onSaveInstanceState() {
	   Parcelable p = super.onSaveInstanceState();
	   Log.d(TAG, "onSaveInstanceState"); // log the activity
	   
	   // need to store the collection of selected tiles.
	   // first decide how to display what is selected
	   Bundle bundle = new Bundle();
	   bundle.putParcelable(VIEW_STATE, p);
	   return bundle;
   }
   
   @Override
   protected void onRestoreInstanceState(Parcelable state) {
	   Log.d(TAG, "onRestoreInstanceState");
	   Bundle bundle = (Bundle) state;
	   super.onRestoreInstanceState(bundle.getParcelable(VIEW_STATE));
   }
   
   float boardWidth;
   float boardHeight;
   int boardOffSetX = 20;
   int boardOffSetY = 20;
   int boardBottomBuffer = 200;
   float boardBottomBorder;
   float boardRightBorder;
   float boardLeftBorder;
   float boardTopBorder;
   float dieWidth;
   float dieHeight;
   
   @Override
   protected void onSizeChanged(int w, int h, int oldw, int oldh) {
      width = (w-( 2 * boardOffSetX)) / 4f;
      height = (h - boardBottomBuffer) / 4f;
      
      boardWidth = getWidth() - (2*boardOffSetX);
      boardHeight = getHeight() - (2*boardOffSetY) - boardBottomBuffer;
      
      
      Log.d(TAG, "onSizeChanged: width " + width + ", height "
            + height);
      boardBottomBorder = getHeight() - boardBottomBuffer - boardOffSetY;
      boardRightBorder  = getWidth() - boardOffSetX;
      boardLeftBorder   = boardOffSetX;
      boardTopBorder    = boardOffSetY;
      
      dieWidth 			= boardWidth/4f;
      dieHeight			= boardHeight/4f;

      
      super.onSizeChanged(w, h, oldw, oldh);
   }
   
   @Override
   protected void onDraw(Canvas canvas) {

      // board background color
      Paint boardColor = new Paint();
      boardColor.setColor(getResources().getColor(
            R.color.boggle_boardColor));
      // game background color
      Paint background = new Paint();
      background.setColor(getResources().getColor(R.color.boggle_background));
      // draw game background
      canvas.drawRect(0, 0, getWidth(), getHeight(), background);
      //draw board background - i'm nervous about using regular numbers here.
      canvas.drawRect(boardOffSetX, boardOffSetY, getWidth() - boardOffSetX, getHeight() - boardBottomBuffer - boardOffSetY, boardColor);
      
      // draw background for entered word area
      canvas.drawRect(	boardOffSetX, 
    		  			boardBottomBorder+boardOffSetY,
    		  			getWidth()-boardOffSetX,
    		  			getHeight()-boardOffSetY,
    		  			boardColor);
 
      
      // Draw the board...
      // Define colors for the grid lines
      Paint dark = new Paint();
      dark.setColor(getResources().getColor(R.color.boggle_dark));

      Paint hilite = new Paint();
      hilite.setColor(getResources().getColor(R.color.boggle_hilite));

      Paint light = new Paint();
      light.setColor(getResources().getColor(R.color.boggle_light));

      // Draw the major grid lines
      for (int i = 0; i < 5; i++) {
    	  
    	  canvas.drawLine(boardLeftBorder + i*(boardWidth/4), boardTopBorder, 
    			          boardLeftBorder + i*(boardWidth/4), boardBottomBorder, 
    			          hilite);
    	  canvas.drawLine(boardLeftBorder, boardTopBorder + i*(boardHeight/4),
    			  		  boardRightBorder, boardTopBorder + i*(boardHeight/4),
    			  		  hilite);
      }

      // Draw the minor grid lines (trying to make them look like real buttons, or something)
      for (int i = 0; i < 5; i++) {
    	  
    	  canvas.drawLine(	boardLeftBorder + i*(boardWidth/4)+1, boardTopBorder, 
		          			boardLeftBorder + i*(boardWidth/4)+1, boardBottomBorder, 
		          			light);
    	  canvas.drawLine(	boardLeftBorder, boardTopBorder + i*(boardHeight/4)+1,
		  		  			boardRightBorder, boardTopBorder + i*(boardHeight/4)+1,
		  		  			light);
    	  canvas.drawLine(	boardLeftBorder, boardTopBorder + i*(boardHeight/4)-1,
		  					boardRightBorder, boardTopBorder + i*(boardHeight/4)-1,
		  					light);
    	  canvas.drawLine(	boardLeftBorder + i*(boardWidth/4)-1, boardTopBorder, 
        					boardLeftBorder + i*(boardWidth/4)-1, boardBottomBorder, 
        					light);
      }
      
      

      // Draw the numbers...
      // Define color and style for numbers
      Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
      foreground.setColor(getResources().getColor(
            R.color.boggle_foreground));
      foreground.setStyle(Style.FILL);
      foreground.setTextSize(dieHeight * 0.7f);
      foreground.setTextScaleX(dieWidth / dieHeight);
      foreground.setTextAlign(Paint.Align.CENTER);


      
      // Draw the number in the center of the tile
      FontMetrics fm = foreground.getFontMetrics();
      // Centering in X: use alignment (and X at midpoint)
      float x = dieWidth / 2;
      // Centering in Y: measure ascent/descent first
      float y = dieHeight / 2 ;
      
      //drawing letters    		  										  
      for (int i = 0; i<4; i++) {
    	  for (int j=0; j<4; j++) {
    		  canvas.drawText(	this.game.getLetterString(i, j),
    				  			x+boardOffSetX+(2*x*i),
    				  			y+boardOffSetY+(2*y*j) - (fm.ascent + fm.descent)/2,
    				  			foreground);
    	  }
      }
      
      // Draw the selected 
      
   }
   
   // ...
}

