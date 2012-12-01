package edu.madcourse.dancalacci.circletouch;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class History_View extends View{
	private String TAG = "circletoucn.history";
	private Canvas canvas;
	private RectF mCircleBounds = new RectF();

	private float scrollByX = 0; // x amount to scroll by
	private float scrollByY = 0; // y amount to scroll by

	// circle positions
	private float mCircleX;
	private float mCircleY;
	private float mCircleRadius;
	
	private Paint mCirclePaint;
	private Paint mHistoryLinePaint;
	
	private GestureDetector mGestureDetector;
	private boolean inScroll = false;
	
	private Context mContext;

	// List containing all the charts to be drawn on Canvas
	private ArrayList<Chart> mCharts = new ArrayList<Chart>();


	public History_View(Context context) {
		super(context);
		init();
		mContext = context;
	}
	
	/**
	 * Initializes some values and all of the fancy paints and listeners
	 */
	private void init() {

		// set up the circle paint
		mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mCirclePaint.setStyle(Paint.Style.STROKE);

		// set up the separatorLines paint
		mHistoryLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mHistoryLinePaint.setStyle(Paint.Style.STROKE);

		
		// set up the gesture detector
		mGestureDetector = new GestureDetector(
				History_View.this.getContext(), 
				new GestureListener());

		// Turn off long press--this control doesn't use it, and if long press is
		// enabled, you can't scroll for a bit, pause, then scroll some more 
		// (the pause is interpreted as a long press, apparently)
		mGestureDetector.setIsLongpressEnabled(false);

		invalidate();
	}
	
	/**
	 * doing stuff with touch
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//Log.d(TAG, "ONTOUCHEVENT | received a touchEvent");
		boolean result = mGestureDetector.onTouchEvent(event);

		// if the user lifts their finger, we're not in a scroll.
		if (event.getAction() == MotionEvent.ACTION_UP) {
			inScroll = false;
			//onScrollFinished();
		}

		// return true if mGestureDetector handled the event
		if (result) {
			return result;
		}
		return false;

	}
	
	/**
	 * let's track some gestures
	 */
	private class GestureListener extends GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onScroll(
				MotionEvent e1,
				MotionEvent e2,
				float distanceX,
				float distanceY) {

			float lastX = e2.getX() + distanceX;
			float lastY = e2.getY() + distanceY;
			
			// normal touch handling
			
			return false;
		}
		// we need to return true here so we can actually scroll.
		public boolean onDown(MotionEvent e) {
			return true;
		}
	}
	
	

	public History_View(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
//		canvas.drawLine(
//				mCircleX,
//				mCircleY,
//				touchPointCoords.x,
//				touchPointCoords.y,
//				mSeparatorLinesPaint
//				);
	}

	private void drawChart(){

	}

	private void drawDate(){

	}

	private void drawLine(){

	}


	/**
	 * Represents Chart data for Drawing
	 * @author shuuri
	 *
	 */
	private class Chart{
		String date; //Temp until storage is figured out 
		ArrayList<Category> Categories = new ArrayList<Category>();

		public Chart(String date) {
			super();
			this.date = date;
		}

	}


	/**
	 * Represents Category data for Charts
	 * @author shuuri
	 *
	 */
	private class Category{
		private float pCCW;
		private float pCW;
		private String category;

		public Category(String category, float pCCW, float pCW) {
			super();
			this.pCCW = pCCW;
			this.pCW = pCW;
			this.category = category;
		}

	}

}
