/***

 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband3 for more book information.
 ***/

package edu.madcourse.dancalacci.boggle;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Parcelable;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.util.TypedValue;
import edu.madcourse.dancalacci.R;
import android.graphics.Point;


public class Multiplayer_Game_View extends View {

	private static final String TAG = "PuzzleView";

	private static final String VIEW_STATE = "viewState";
	private static final int ID = 42; 


	private float width;    // width of one tile
	private float height;   // height of one tile
	private int selX;       // X index of selection
	private int selY;       // Y index of selection

	private final Multiplayer_Game game;
	private static ArrayList<Rect> selRects = new ArrayList<Rect>();
	private static ArrayList<Point> selDie = new ArrayList<Point>();
	private static Point lastSelected;
	private int time = 180;
	private String timeText = "Time Remaning: 180";
	private boolean gameOver = false;

	public Multiplayer_Game_View(Context context) {

		super(context);
		this.game = (Multiplayer_Game) context;
		setFocusable(true);
		setFocusableInTouchMode(true);

		setId(ID);
		setConstants();
	}

	
	protected Parcelable onSaveInstanceState() {
		Parcelable p = super.onSaveInstanceState();
		Log.d(TAG, "onSaveInstanceState"); // log the activity

		// need to store the collection of selected tiles.
		// first decide how to display what is selected
		Bundle bundle = new Bundle();
		bundle.putParcelable(VIEW_STATE, p);
		return bundle;
	}

	
	protected void onRestoreInstanceState(Parcelable state) {
		Log.d(TAG, "onRestoreInstanceState");
		Bundle bundle = (Bundle) state;
		super.onRestoreInstanceState(bundle.getParcelable(VIEW_STATE));
	}
	
	private int boardSize = this.game.size;

	float boardBottomSide;
	float boardTopSide;
	float boardLeftSide;
	float boardRightSide;
	float boardHeight;
	float boardWidth;

	float dieWidth;
	float dieHeight;

	
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		setConstants();
		super.onSizeChanged(w, h, oldw, oldh);
	}


	protected void setTime(int time) {
		this.time = time;
		this.timeText = ("Time Remaning: " +this.time);
		invalidate();
	}

	protected int getTime() {
		return this.time;
	}

	/**
	 * Sets all the constants for the view.
	 */
	protected void setConstants() {		
		
	    // get the default display object for the game's window manager
	    Display display = game.getWindowManager().getDefaultDisplay();

	    // get the DisplayMetrics object for the default display 
	    DisplayMetrics outMetrics = new DisplayMetrics ();
	    display.getMetrics(outMetrics);

	    // get density, Height of the window, Width of the window, etc in dips
	    // from the display metrics resource we just created.
	    float density  = getResources().getDisplayMetrics().density;
	    float dpHeight = outMetrics.heightPixels / density;
	    float dpWidth  = outMetrics.widthPixels / density;
	    
		Log.d(TAG, "Screen Width (dp): " + dpWidth);
		Log.d(TAG, "Screen Height (dp): " + dpHeight);
		
		// We want the buffers on the right and left sides to be 1/8th of the
		// screen width:
		
		boardLeftSide = dpWidth / 8f;
		boardRightSide = boardLeftSide * 7f;
		
		// and the board is a square, so we want the other values to be pretty
		// much the same:
		boardTopSide = boardLeftSide;
		boardBottomSide = boardRightSide;
		
		// these values correspond to the x or y value of a specific side of the board
		// in pixels
		
		Log.d(TAG, "boardLeftSide in dp: " + boardLeftSide);
		Log.d(TAG, "boardRightSide in dp: " + boardRightSide);
		Log.d(TAG, "boardTopSide in dp: " + boardTopSide);
		Log.d(TAG, "boardBottomSide in dp: " + boardBottomSide);
		
		boardWidth = boardRightSide - boardLeftSide; 	// the width of the board in pixels
		boardHeight = boardBottomSide - boardTopSide;	// the height of the board in pixels  

		// the width of each die
		dieWidth 			= boardWidth/(float)boardSize;
		Log.d(TAG, "dieWidth is now: " + dieWidth);
		// the height of each die
		dieHeight			= boardHeight/(float)boardSize;
		Log.d(TAG, "dieHeight is now: " + dieHeight);

		Log.d(TAG, "onSizeChanged: width " + width + ", height "
				+ height); 
	}
	
	
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

		// draw the board background
		canvas.drawRect(	boardLeftSide, 
				boardTopSide, 
				boardRightSide, 
				boardBottomSide, 
				boardColor);

		// Draw the board lines

		// Define colors for the grid lines
		Paint dark = new Paint();
		dark.setColor(getResources().getColor(R.color.boggle_dark));

		Paint hilite = new Paint();
		hilite.setColor(getResources().getColor(R.color.boggle_hilite));

		Paint light = new Paint();
		light.setColor(getResources().getColor(R.color.boggle_light));

		// Draw the major grid lines
		for (int i = 0; i < boardSize+1; i++) {
			// vertical lines
			canvas.drawLine(boardLeftSide + i*(boardWidth/(float)boardSize), boardTopSide, 
					boardLeftSide + i*(boardWidth/(float)boardSize), boardBottomSide, hilite);
			// horizontal lines
			canvas.drawLine(boardLeftSide, boardTopSide + i*(boardHeight/(float)boardSize), boardRightSide, 
					boardTopSide + i*(boardHeight/(float)boardSize), hilite);
		}

		// Draw the minor grid lines
		for (int i = 0; i < boardSize+1; i++) {

			canvas.drawLine(	boardLeftSide + i*(boardWidth/(float)boardSize)+1, boardTopSide, 
					boardLeftSide + i*(boardWidth/(float)boardSize)+1, boardBottomSide, 
					light);
			canvas.drawLine(	boardLeftSide, boardTopSide + i*(boardHeight/(float)boardSize)+1,
					boardRightSide, boardTopSide + i*(boardHeight/(float)boardSize)+1,
					light);
			canvas.drawLine(	boardLeftSide, boardTopSide + i*(boardHeight/boardSize)-1,
					boardRightSide, boardTopSide + i*(boardHeight/(float)boardSize)-1,
					light);
			canvas.drawLine(	boardLeftSide + i*(boardWidth/(float)boardSize)-1, boardTopSide, 
					boardLeftSide + i*(boardWidth/(float)boardSize)-1, boardBottomSide, 
					light);
		}

		// draw the score and time remaning
		Paint score = new Paint(Paint.ANTI_ALIAS_FLAG);
		score.setColor(getResources().getColor(R.color.boggle_foreground));
		score.setStyle(Style.FILL);
		score.setTextSize(boardTopSide * .4f);
		score.setTextAlign(Paint.Align.LEFT);

		//TODO: SCORING
		canvas.drawText("Score: " + game.score_P1, 0, boardTopSide/2f, score);
		canvas.drawText("Score: " + game.score_P2, 0, boardTopSide/2f, score);

		// draw the time
		score.setTextAlign(Paint.Align.RIGHT);
		canvas.drawText(timeText, boardRightSide+boardLeftSide, boardTopSide/2f, score);
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

		// center values for the dies
		float x = dieWidth / 2;
		float y = dieHeight / 2 ;

		//drawing the letters  		  										  
		for (int i = 0; i<boardSize; i++) {
			for (int j=0; j<boardSize; j++) {
				canvas.drawText(	this.game.getLetterString(i, j),
						x+boardLeftSide+(2*x*i),
						y+boardTopSide+(2*y*j) - (fm.ascent + fm.descent)/2,
						foreground);
			}
		}

		Paint selected = new Paint();
		selected.setColor(getResources().getColor(R.color.boggle_selectedDieColor));

		if (!selRects.isEmpty()) {
			Log.d(TAG, "Selected rects in onDraw:" + selRects.toString());
			for (Rect rect : selRects) {
				canvas.drawRect(rect, selected);
			}
		} else {

		}

		if (gameOver) {
			canvas.drawText("GAME OVER" +game.score_P1, boardWidth/2+boardLeftSide,
					boardHeight/2+boardTopSide, foreground);
			canvas.drawText("GAME OVER" +game.score_P2, boardWidth/2+boardLeftSide,
					boardHeight/2+boardTopSide, foreground);
			final Handler handler = new Handler();
			final Runnable r = new Runnable()
			{
				public void run() 
				{
					handler.postDelayed(this, 3000);
				}
			};
			handler.postDelayed(r, 4000);
			
			game.onGameOver();
		}


	}

	
	public boolean onTouchEvent(MotionEvent event) {

		if (event.getAction() != MotionEvent.ACTION_DOWN) {
			return super.onTouchEvent(event);
		}

		float x = event.getX();
		float y = event.getY();
		int xIndex;
		int yIndex;

		// if the indices are within the board borders
		if (x > boardLeftSide && x < boardRightSide) {
			if (y > boardTopSide && y < boardBottomSide) {
				// xIndex and yIndex are the indices of the
				// selected die on the screen.
				yIndex = getDieYIndex(y);
				xIndex = getDieXIndex(x);
				// if there is no selected die with this xindex and yindex
				// and it is adjacent to the last selected die
				if ((!isDieSelected(xIndex, yIndex)) && (canSelect(new Point(xIndex, yIndex)))) {

					// make the vibrator for tactile feedback
					Log.d(TAG, "User selected a tile - vibrating for 50ms");
					Vibrator v = (Vibrator) game.getSystemService(Context.VIBRATOR_SERVICE);
					v.vibrate(50);

					MediaPlayer mp = MediaPlayer.create(this.game, R.raw.button);
					mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

						public void onCompletion(MediaPlayer mp) {
							// TODO Auto-generated method stub
							mp.release();
						}
					});
					mp.start();

					// generate the selected rectangle, add it to  selRects
					Rect selected = getDieRect(xIndex, yIndex);
					selRects.add(selected);
					// make a point with the x and y indices,
					// add the generated point to selDie
					Point selectedP = new Point(xIndex, yIndex);
					selDie.add(selectedP);
					// make lastSelected this most recently selected point
					lastSelected = selectedP;
					// log it
					game.selectTile(xIndex, yIndex);
					Log.d("onTouchEvent", "added rect at: " + xIndex + ", " + yIndex);
					// refresh screen
					invalidate();
				}
			}
		}
		return true;
	}


	/**
	 * Given a representation of the selected tiles, sets the selected tiles
	 * in this view, and creates the list of rectangles to draw.
	 * @param sel the representation of the selected tiles
	 */
	protected void setSelectedDice(ArrayList<Boolean> sel) {
		clearSelectedDie();
		clearSelectedRects();
		Log.d(TAG, "Selected die from game: " + sel.toString());
		for (int i = 0; i < sel.size(); i++) {
			if (sel.get(i) == true) {
				Log.d(TAG, "Adding coord " + i + "To selected Die");
				this.selDie.add(getCoord(i));
			}
		}
		Log.d(TAG, "Selected die populated from game to: " + selDie.toString());
		for (Point p : selDie) {
			Log.d(TAG, "Trying to add rect with these indices: " + p.x + ", " + p.y);
			Log.d(TAG, "Trying to add rect with these coords: " + getxCord(p.x) + ", " + getyCord(p.y));
			Log.d(TAG, "Adding rect: " + getDieRect(p.x, p.y).toString());
			Rect r = getDieRect(p.x, p.y);
			selRects.add(r);
		}
		Log.d(TAG, "Selected rects populated from game to: " + selRects.toString());

		// handling the lastSelected counter
		// if there are no dice selected
		if (this.selDie.isEmpty()) {
			// make sure there's no last selected object
			this.lastSelected = null;
		} else {
			// otherwise make the last selected index the last tile the user selected
			this.lastSelected = this.selDie.get(this.selDie.size()-1);
		}
		// draw the updated selected board
		invalidate();
	}


	/**
	 * gets the tile coordinate of the tile at the given index
	 * @param index the index of the tile to get the coordinate of
	 * @return a Point that represents the coordinate of the tile
	 */
	//TODO: redo this 
	private Point getCoord(int index) {
		if (index <= 3) {
			return new Point(index, 0);
		} else if (index <= 7) {
			return new Point(index-4, 1);
		} else if (index <= 11) {
			return new Point(index-8, 2);
		} else {
			return new Point(index-12, 3);
		}
	}

	/**
	 * Clears the list of selected rectangles
	 */
	protected void clearSelectedRects() {
		selRects.clear();
	}

	/**
	 * Clears the list of selected dice
	 */
	protected void clearSelectedDie() {
		selDie.clear();
	}

	/**
	 * Returns true if the user can select the tile at the given coordinate
	 * @param n the point that represents the coordinate in question
	 * @return True if the user can select it, false otherwise
	 */
	private boolean canSelect(Point n) {
		if (lastSelected != null) {
			return isAdjacent(n, lastSelected);
		} else {
			return true;
		}
	}

	/**
	 * Returns true if point a and b are adjacent on the board
	 * @param a the coordinate of the first tile
	 * @param b the coordinate of the second tile
	 * @return True if the two tiles are adjacent, false otherwise
	 */
	private boolean isAdjacent(Point a, Point b) {
		if ((Math.abs( a.x - b.x) > 1) || ((Math.abs( a.y - b.y)) > 1)) {
			return false;
		} else {
			return true;
		}
	}


	/**
	 * Returns true if the die at the given coordinates is currently selected
	 * @param x The x-coordinate of the tile
	 * @param y The y-coordinate of the tile
	 * @return True if the die at the given coordinates is currently selected
	 */
	private boolean isDieSelected(int x, int y) {
		for (Point point : selDie) {
			if (point.equals(x, y)) {
				return true;
			} 
		}
		return false;
	}


	/**
	 * Returns the x-index of a position on the screen
	 * @param x the x-coordinate of a position on the screen
	 * @return The x-index (0-3) of the tile that occupies that position
	 */
	private int getDieXIndex(float x) {
		
		float start = boardLeftSide;
		for (int i = 1; i< boardSize; i++) {
			if (x <= start + i*dieWidth) {
				return i-1;
			}
		}
		return boardSize-1;
	}

	/**
	 * Returns the y-index of a position on the screen
	 * @param y the y-coordinate of a position on the screen
	 * @return the y-index (0-3) of the tile that occupies that position
	 */
	private int getDieYIndex(float y) {
		float start = boardTopSide;
		for (int i = 1; i< boardSize; i++) {
			if (y <= start + i*dieHeight) {
				return i-1;
			}
		}
		return boardSize-1;
	}

	/**
	 * Adds the selected rectangle for the given die index to SelRect
	 * @param x The x-index of the die
	 * @param y The y-index of the die
	 */
	private void select(int x, int y) {
		selRects.add(getDieRect(x, y));
	}

	/**
	 * Returns a rect object that covers the die at the given index
	 * @param i the index of the die (1-16)
	 */
	private Rect getDieRect(int x, int y) {

		Log.d(TAG, "making rect from given indices " + x + ", " + y);

		float width = dieWidth / 2;
		float height = dieHeight / 2;
		float xcord = getxCord(x);
		float ycord = getyCord(y);
		float leftSide = xcord - width;
		float rightSide = xcord + width;
		float topSide = ycord + height;
		float botSide = ycord - height;

		Log.d(TAG, "calculated coordinates: " + xcord + ", " + ycord);

		Log.d(TAG, "making new rect with coords:  " + (int)leftSide + ", " +(int)topSide +
				" - " + (int)rightSide + ", " + (int)botSide);

		return new Rect((int)leftSide, (int)topSide, (int)rightSide, (int)botSide);
	}

	/**
	 * Returns the x-coordinate of the center of the die at the given x-index
	 * @param x The x-index of the die
	 * @return A float representing the center of the die at x
	 */
	private float getxCord(int x) {
		Log.d(TAG, "getting x-coordinate of index: " + x);
		float width = dieWidth / 2;
		Log.d(TAG, "width is calculated to be: " +dieWidth +"/2: " + width);

		Log.d(TAG, "returning: " + width + " + " + boardLeftSide + " + " + "(2 * " +width + "*" + x +")");
		return width+boardLeftSide+(2*width*x);
	}

	/**
	 * Returns the y-coordinate of the center of the die at the given y-index
	 * @param y The y-index of the die
	 * @return A float representing the center of the die at y
	 */
	private float getyCord(int y) {
		float height = dieHeight / 2;
		return height+boardTopSide+(2*height*y);
	}

	protected void finishGame() {
		this.gameOver = true;
		invalidate();
	}

}

