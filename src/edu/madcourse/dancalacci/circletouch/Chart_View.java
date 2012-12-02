package edu.madcourse.dancalacci.circletouch;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import edu.madcourse.dancalacci.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


//TODO: dragging is weird sometimes. fix that.


public class Chart_View extends View {
	private String TAG = "circletouch.circle";
	private Canvas canvas;
	// view boundary for the circle.
	private RectF mCircleBounds = new RectF();
	private ArrayList<TouchPoint> mPoints = new ArrayList<TouchPoint>();

	//Items 
	private ArrayList<Category> mCategories = new ArrayList<Category>();

	// circle positions
	private float mCircleX;
	private float mCircleY;
	private float mCircleRadius;

	// angle stuff
  // 30 degrees in radians
	private final double ANGLE_THRESHOLD = 0.174532*3;
  // 1 degree in radians
  private final double ANGLE_INTERVAL = 0.174532;

	// touchPoint info
	private int mTouchPointRadius;
	private int mTouchPointColor;

	// gesture detection
	private GestureDetector mGestureDetector;
	private boolean inScroll = false;


	//paints
	private Paint mCirclePaint;
	private Paint mTouchPointPaint;
	private Paint mSeparatorLinesPaint;
	private Context mContext;
	// for categories
	private Paint mCategoryPaint;
	/*private Paint mProteinPaint;
  private Paint mVegetablePaint;
  private Paint mDairyPaint;
  private Paint mOilSugarPaint;
  private Paint mFruitPaint;
  private Paint mGrainPaint;*/
	/**
	 * boring constructor with just a context
	 */
	public Chart_View(Context c) {
		super(c);
		init();
		mContext = c;
	}

	/**
	 * constructor with attrs etc.
	 */
	public Chart_View(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);

		mContext = ctx;

		// attrs contains the raw values for the XML attributes
		// that were specified in the layout, which don't include
		// attributes set by styles or themes, and which may have
		// unresolved references. Call obtainStyledAttributes()
		// to get the final values for each attribute.
		//
		// This call uses R.styleable.PieChart, which is an array of
		// the custom attributes that were declared in attrs.xml.
		TypedArray a = ctx.getTheme().obtainStyledAttributes(
				attrs,
				R.styleable.circle,
				0, 0);

		try {
			// resolve values from the typedarray and store into fields
			mTouchPointRadius = 
					a.getInteger(R.styleable.circle_touchPointRadius, 40);
			mTouchPointColor = 
					a.getInteger(R.styleable.circle_touchPointColor,0xffff0000); 

			// mTextWidth = a.getDimension(R.styleable.PieChart_labelWidth,
			// 0.0f);
		} finally {
			// release TypedArray
			a.recycle();

			/*Log.d(TAG,
					"mTouchPointRadius is: " + mTouchPointRadius);
			Log.d(TAG, 
					"mTouchPointColor is: " + mTouchPointColor);*/
		}

		// initialize everything
		init();
	}

	/**
	 * Called when the size of the screen is changed; we describe the size of
	 * the circle and it's boundary area here.
	 */
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		// calculate the total padding size
		float xpad = (float) (getPaddingLeft() + getPaddingRight());
		float ypad = (float) (getPaddingTop() + getPaddingBottom());

		//figure out the "correct" width and height based on the padding
		float ww = (float) w - xpad;
		float hh = (float) h - ypad;

		// let's make the circle as large as it can be for this view
		float diameter = (float) Math.min(ww, hh);

		// make the rectf for the boundary
		mCircleBounds = new RectF(
				0.0f,
				0.0f,
				diameter,
				diameter);

		// offset the boundary rect in accordance with the padding
		mCircleBounds.offsetTo(getPaddingLeft(), getPaddingTop());

		// calculate the circle's coordinates and stuff based on the boundary
		mCircleRadius = diameter/2f;
		mCircleX      = mCircleBounds.left + mCircleBounds.width()/2;
		mCircleY      = mCircleBounds.top + mCircleBounds.height()/2;

		// if the touchpoints are gonna go out of the padding, fix it.
		float farthest = mTouchPointRadius + diameter;
		if (farthest > w - xpad/2 || farthest > h-ypad/2) {
			mTouchPointRadius = (int)Math.min(xpad, ypad)/4;
			Log.d(TAG, 
					"Touchpoints are a little big. reducing to: " +
							mTouchPointRadius);
			Log.d(TAG, "mCircle center is: " +mCircleX +", "+mCircleY);

		}
	}
	/**
	 * Adds an item to the list of points
	 * @param degrees the degree value for the point to add.
	 */
	private void addItem(double rads) {
		// create a new point
		TouchPoint p = new TouchPoint();
		p.mRads = rads;

		// add it to the list of points
		mPoints.add(p);
		sortListCW(mPoints, 0);
		invalidate();
	}

	/**
	 * Adds category to the list of Categories
	 * TODO: COLOR IN SLICES
	 * @param category
	 * @param Color
	 */
	private void addCategory(String category, int color){
		addCategoryHelper(null, null, category, color);
		addPoints();		
	}

	private void addNItems(int n) {
		double radSections = (Math.PI*2)/n;
		double totalRads = 0;
		for (int i = 0; i < n; i++) {
			totalRads = moveRadCW(totalRads, radSections);
			addItem(totalRads);
		}
	}


	/**
	 * Adds points to the charts
	 */
	private void addPoints(){
		int cSize = mCategories.size();		
		clearPoints();
		addNItems(cSize);
		setPointsToCategories();
		/*
    // needs to take into acount -pi and +pi etc.
    double rads = (Math.PI * 2) / (cSize);
    double rads_sum = 0;

    clearPoints();

    if(cSize != 1){
      for (int i = 0; i < cSize; i++){
        rads_sum += rads;
        addItem(rads_sum);
      }
      setPointsToCategories();
    }*/
	}

	/**
	 * Clears all the points 
	 */
	private void clearPoints(){
		mPoints.clear();
	}

	/**
	 * Sets the points associated to the category
	 */
	// this doesn't add the correct points.  REview.
	private void setPointsToCategories(){
		int size = mPoints.size();
		int i = 0;
		int j = i+1;
		for (Category c : mCategories){
			//Log.d(TAG, "Index I:"+i);
			//Log.d(TAG, "Index J:"+j);

			c.setpCCW(mPoints.get(i));
			if (i == (size - 1)){
				c.setpCW(mPoints.get(0));
			}else{
				c.setpCW(mPoints.get(j));
			}

			//Log.d(TAG, "mPoints Size: "+ mPoints.size());
			//Log.d(TAG, "pCCW: "+ c.getpCCW());
			//Log.d(TAG, "pCW: "+ c.getpCW());

			i++;
			j++;
		}      
	}

  private void refreshCategoryPoints() {
    int size = mPoints.size()-1;
    int i = 0;
    int j = i+1;
    
    for (Category c : mCategories) {
      c.setpCCW(mPoints.get(i));
      if (i == size) {
        c.setpCW(mPoints.get(0));
      } else {
        c.setpCW(mPoints.get(j));
      }
      i++;
      j++;
    }
  }

	/**
	 * Removes the specified category from the list
	 * @param category
	 */
	private void removeCategory(String category){
		int index = 0;
		// iterate through category list, skip down if we find category
		for(Category c : mCategories){
			if(c.getCategory().equalsIgnoreCase(category)){
				break;
			}else{
				index = index + 1;
			}
		}
		mCategories.remove(index);
		// if only one category, remove the only remaining one
		if(mCategories.size() < 2){
			clearPoints();
			//Log.d(TAG, "ALL GONE: " + mPoints.toString());
		}else{
			addPoints();
			setPointsToCategories();
		}
	}

	/**
	 * Adds categories Helper
	 * @param pCCW - Counter Clockwise point
	 * @param pCW - Clockwise point
	 * @param category - the Category of the slice
	 * @param color - Color of the category
	 */
	private void addCategoryHelper(TouchPoint pCCW, TouchPoint pCW, String category,  int color){
		Category item = new Category(pCCW, pCW, category, color);

		mCategories.add(item);
	}

	/**
	 * Container for "Items" on the pie chart
	 * @param pCCW - the Counter Clockwise Point
	 * @param pCW - the Clockwise Point
	 * @param category - One of: Protein, Grains, Vegetable, Oil/Fat/Sweets, Dairy, Fruits	
	 * @param color - Color of the slice 
	 */
	private class Category {
		private TouchPoint pCCW;
		private TouchPoint pCW;
		private String category;
		private int color;

		public Category(TouchPoint ccw, TouchPoint cw, String category, int c ){
			this.pCCW = ccw;
			this.pCW = cw;
			this.category = category;
			this.color = c;
		}

		public TouchPoint getpCCW(){
			return this.pCCW;
		}

		public TouchPoint getpCW(){
			return this.pCW;
		}

		public void setpCCW(TouchPoint ccw){
			this.pCCW = ccw;
		}

		public void setpCW(TouchPoint cw){
			this.pCW = cw;
		}

		public String getCategory(){
			return this.category;
		}

		public int getColor(){
			return this.color;
		}

		public void setColor(int color){
			this.color = color;
		}

		public String toString(){
			return this.category;
		}
	}
	/**
	 * Checks whether the category already exists in the list
	 * @param category - name of category (ignores UPPER/LOWER case)
	 * @return inList - exist in list 
	 */
	public boolean isCategoryinList(String category){
		boolean inList = false;

		for(Category c : mCategories){
			if (c.getCategory().equalsIgnoreCase(category)){
				inList = true;
			}
		}

		return inList;
	}
	/**
	 * Clears the Chart of its contents
	 * Clears associated ArrayLists
	 */
	public void clearChart() {
		// TODO Auto-generated method stub
			this.mCategories.clear();
			this.mPoints.clear();	
			invalidate();
	}

	/**
	 * When Protein is clicked it adds or removes from the chart
	 * Adds / Removes from mCategories ArrayList
	 * @param v - View 
	 */
	public void onProteinClicked(View v){
		String category = "Protein";

		boolean inList = isCategoryinList(category);

		if(inList){
			// Deselect
			setProteinDeselect(v);
			removeCategory(category);
		}else{
			// Select
			setProteinSelect(v);
			addCategory(category, getResources().getColor(R.color.Protein));
		}		 
		invalidate();
	}
	
	/**
	 * Updates the color of the protein text and box icon to deselect state
	 * @param v - View
	 */
	public void setProteinDeselect(View v){
		TextView box = (TextView) v.findViewById(R.id.protein_box);
		TextView text = (TextView) v.findViewById(R.id.protein_label);
		
		box.setBackgroundColor(getResources().getColor(R.color.Protein_Grayed));
		text.setTextColor(getResources().getColor(R.color.Protein_Grayed));
	}
	
	/**
	 * Updates the color of the protein text and box icon to select state
	 * @param v - View
	 */
	public void setProteinSelect(View v){
		TextView box = (TextView) v.findViewById(R.id.protein_box);
		TextView text = (TextView) v.findViewById(R.id.protein_label);
		
		box.setBackgroundColor(getResources().getColor(R.color.Protein));
		text.setTextColor(getResources().getColor(R.color.Protein));
	}
	
	/**
	 * When Vegetable is clicked it adds or removes from the chart
	 * Adds / Removes from mCategories ArrayList
	 * @param v
	 */
	public void onVegetableClicked(View v){
		String category = "Vegetable";

		boolean inList = isCategoryinList(category);

		if(inList){
			// Deselect
			setVegetableDeselect(v);
			removeCategory(category);
		}else{
			// Select
			setVegetableSelect(v);
			addCategory(category, getResources().getColor(R.color.Vegetable));
		}	
		invalidate();
	}
	
	/**
	 * Updates the color of the vegetable text and box icon to deselect state
	 * @param v - View
	 */
	public void setVegetableDeselect(View v){
		TextView box = (TextView) v.findViewById(R.id.vegetable_box);
		TextView text = (TextView) v.findViewById(R.id.vegetable_label);
		
		box.setBackgroundColor(getResources().getColor(R.color.Vegetable_Grayed));
		text.setTextColor(getResources().getColor(R.color.Vegetable_Grayed));
	}
	
	/**
	 * Updates the color of the vegetable text and box icon to select state
	 * @param v - View
	 */
	public void setVegetableSelect(View v){
		TextView box = (TextView) v.findViewById(R.id.vegetable_box);
		TextView text = (TextView) v.findViewById(R.id.vegetable_label);
		
		box.setBackgroundColor(getResources().getColor(R.color.Vegetable));
		text.setTextColor(getResources().getColor(R.color.Vegetable));
	}

	/**
	 * When Dairy is clicked it adds or removes from the chart
	 * Adds / Removes from mCategories ArrayList
	 * @param v - View
	 */
	public void onDairyClicked(View v){
		String category = "Dairy";

		boolean inList = isCategoryinList(category);

		if(inList){
			// Deselect
			setDairyDeselect(v);
			removeCategory(category);
		}else{
			// Select
			setDairySelect(v);
			addCategory(category, getResources().getColor(R.color.Dairy));
		}	
		invalidate();
	}
	
	/**
	 * Updates the color of the dairy text and box icon to deselect state
	 * @param v - View
	 */
	public void setDairyDeselect(View v){
		TextView box = (TextView) v.findViewById(R.id.dairy_box);
		TextView text = (TextView) v.findViewById(R.id.dairy_label);
		
		box.setBackgroundColor(getResources().getColor(R.color.Dairy_Grayed));
		text.setTextColor(getResources().getColor(R.color.Dairy_Grayed));
	}
	
	/**
	 * Updates the color of the dairy text and box icon to select state
	 * @param v - View
	 */
	public void setDairySelect(View v){
		TextView box = (TextView) v.findViewById(R.id.dairy_box);
		TextView text = (TextView) v.findViewById(R.id.dairy_label);
		
		box.setBackgroundColor(getResources().getColor(R.color.Dairy));
		text.setTextColor(getResources().getColor(R.color.Dairy));
	}

	/**
	 * When Fruit is clicked it adds or removes from the chart
	 * Adds / Removes from mCategories ArrayList
	 * @param v
	 */
	public void onFruitClicked(View v){
		String category = "Fruit";

		boolean inList = isCategoryinList(category);

		if(inList){
			// Deselect
			setFruitDeselect(v);
			removeCategory(category);
		}else{
			// Select
			setFruitSelect(v);
			addCategory(category, getResources().getColor(R.color.Fruit));
		}
		invalidate();
	}
	
	/**
	 * Updates the color of the fruit text and box icon to deselect state
	 * @param v - View
	 */
	public void setFruitDeselect(View v){
		TextView box = (TextView) v.findViewById(R.id.fruit_box);
		TextView text = (TextView) v.findViewById(R.id.fruit_label);
		
		box.setBackgroundColor(getResources().getColor(R.color.Fruit_Grayed));
		text.setTextColor(getResources().getColor(R.color.Fruit_Grayed));
	}
	
	/**
	 * Updates the color of the fruit text and box icon to select state
	 * @param v - View
	 */
	public void setFruitSelect(View v){
		TextView box = (TextView) v.findViewById(R.id.fruit_box);
		TextView text = (TextView) v.findViewById(R.id.fruit_label);
		
		box.setBackgroundColor(getResources().getColor(R.color.Fruit));
		text.setTextColor(getResources().getColor(R.color.Fruit));
	}

	/**
	 * When Grain is clicked it adds or removes from the chart
	 * Adds / Removes from mCategories ArrayList
	 * @param v
	 */
	public void onGrainClicked(View v){
		String category = "Grain";

		boolean inList = isCategoryinList(category);

		if(inList){
			// Deselect
			setGrainDeselect(v);
			removeCategory(category);
		}else{
			// Select
			setGrainSelect(v);
			addCategory(category, getResources().getColor(R.color.Grain));
		}	
		invalidate();
	}
	
	/**
	 * Updates the color of the grain text and box icon to deselect state
	 * @param v - View
	 */
	public void setGrainDeselect(View v){
		TextView box = (TextView) v.findViewById(R.id.grain_box);
		TextView text = (TextView) v.findViewById(R.id.grain_label);
		
		box.setBackgroundColor(getResources().getColor(R.color.Grain_Grayed));
		text.setTextColor(getResources().getColor(R.color.Grain_Grayed));
	}
	
	/**
	 * Updates the color of the grain text and box icon to select state
	 * @param v - View
	 */
	public void setGrainSelect(View v){
		TextView box = (TextView) v.findViewById(R.id.grain_box);
		TextView text = (TextView) v.findViewById(R.id.grain_label);
		
		box.setBackgroundColor(getResources().getColor(R.color.Grain));
		text.setTextColor(getResources().getColor(R.color.Grain));
	}

	/**
	 * When Oil/Sugar is clicked it adds or removes from the chart
	 * Adds / Removes from mCategories ArrayList
	 * @param v
	 */
	public void onOilSugarClicked(View v){
		String category = "OilSugar";

		boolean inList = isCategoryinList(category);

		if(inList){
			// Deselect
			setOilSugarDeselect(v);
			removeCategory(category);
		}else{
			// Select
			setOilSugarSelect(v);
			addCategory(category, getResources().getColor(R.color.Oil_Sugar));
		}	
		invalidate();
	}
	
	/**
	 * Updates the color of the oil/sugar text and box icon to deselect state
	 * @param v - View
	 */
	public void setOilSugarDeselect(View v){
		TextView box = (TextView) v.findViewById(R.id.oil_box);
		TextView text = (TextView) v.findViewById(R.id.oil_label);
		
		box.setBackgroundColor(getResources().getColor(R.color.Oil_Sugar_Grayed));
		text.setTextColor(getResources().getColor(R.color.Oil_Sugar_Grayed));
	}
	
	/**
	 * Updates the color of the oil/sugar text and box icon to select state
	 * @param v - View
	 */
	public void setOilSugarSelect(View v){
		TextView box = (TextView) v.findViewById(R.id.oil_box);
		TextView text = (TextView) v.findViewById(R.id.oil_label);
		
		box.setBackgroundColor(getResources().getColor(R.color.Oil_Sugar));
		text.setTextColor(getResources().getColor(R.color.Oil_Sugar));
	}


	/**
	 * Draws the view
	 * @param canvas The canvas to draw on, dummy!
	 */
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int size = mCategories.size();
		if (size >= 2){
			for (int i = 0; i < size; i++){
				Category c = mCategories.get(i);
				//for (Category c : mCategories){
				//Log.d(TAG, "mCategories[" + i + "]:" + mCategories.toString());


				//Log.d(TAG, "Category :" + c.toString()); 
				TouchPoint end = c.getpCW();
				TouchPoint start = c.getpCCW();

				PointF touchPointCoordsStart = radsToPointF(start.mRads);
				PointF touchPointCoordsEnd = radsToPointF(end.mRads);

				Paint color = new Paint(Paint.ANTI_ALIAS_FLAG);
				color.setColor(c.getColor());
				color.setStyle(Paint.Style.FILL_AND_STROKE);

				float startAngle = (float) radsToDegree(start.mRads);
				float endAngle = (float) radsToDegree(end.mRads);
				float sweepAngle;
				// get correct rad magnitude
				if (getDifference(start.mRads, end.mRads) < 0) {
					sweepAngle = (float) (Math.PI*2 + getDifference(start.mRads, end.mRads));
				} else {
					sweepAngle = (float) (getDifference(start.mRads, end.mRads));
				}
				// convert to degrees
				sweepAngle = radsToDegree(sweepAngle);

				//float sweepAngle = (float) radsToDegree(Math.abs(getDifference(start.mRads, end.mRads)));
				//Log.d(TAG, ">>>>SWEEP: " + sweepAngle);
				//Log.d(TAG, "Color :" + color); 
				//canvas.drawArc(mCircleBounds, 360 - radsToDegree(startAngle), radsToDegree(angle), true, color);
				canvas.drawArc(mCircleBounds, startAngle, sweepAngle, true, color);

			}	
		} else if (size == 1) { // only one category
			Category c = mCategories.get(0);
			Paint color = new Paint(Paint.ANTI_ALIAS_FLAG);
			color.setColor(c.getColor());
			color.setStyle(Paint.Style.FILL_AND_STROKE);
			canvas.drawArc(mCircleBounds, 0f, 360f, true, color);
		}



		for (TouchPoint point : mPoints) {
			PointF touchPointCoords = radsToPointF(point.mRads);

			// draw the touchPoint on the canvas
			canvas.drawCircle(
					touchPointCoords.x,
					touchPointCoords.y,
					mTouchPointRadius,
					mTouchPointPaint
					);
		}

		// drawing the circle
		canvas.drawCircle(
				mCircleX,
				mCircleY,
				mCircleRadius,
				mCirclePaint
				);

		// drawing the touch-points
		for (TouchPoint point : mPoints) {
			PointF touchPointCoords = radsToPointF(point.mRads);
			canvas.drawLine(
					mCircleX,
					mCircleY,
					touchPointCoords.x,
					touchPointCoords.y,
					mSeparatorLinesPaint
					);
		}

		// drawing the slices
		/*for (Category c : mCategories) {
      mCategoryPaint.setColor(c.getColor());
      canvas.drawArc(
      mCircleBounds,
      (float)Math.toDegrees(c.pCCW.mRads),
      (float)Math.toDegrees(c.pCW.mRads),
      true,
      mCategoryPaint);
      }*/


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
			onScrollFinished();
		}

		// return true if mGestureDetector handled the event
		if (result) {
			return result;
		}
		return false;

	}

	private float radsToDegree(double val){
		float toDegree = (float) Math.toDegrees(val);
		/*Log.d(TAG, 
				val +
				" radians is " +
				toDegree +
				"degrees");*/
		return toDegree;
	}

	/**
	 * converts a radian value to a coordinate on the edge of the circle
	 * @param theta The radian value to convert to a coordinate
	 */
	private PointF radsToPointF(double theta) {
		float y = (float)
				(mCircleY + (Math.sin(theta) * mCircleRadius));
		float x = (float)
				(mCircleX + (Math.cos(theta) * mCircleRadius));
		return new PointF(x, y);
	}

	/**
	 * Same thing as pointFtoDegrees, but separate values for the x and y vals.
	 * @param x the x-value of the coordinate
	 * @param y the y-value of the coordinate
	 * */
	private double coordsToRads(float x, float y) {
		double rads = (double) Math.atan2((y - mCircleX),(x - mCircleX));
		// range of atan2 output is -pi to pi...it's weird.
		return rads;
	}

	/*[>*
	 * determines the number of degrees between two points using the circle's
	 * center point.
	 * @param x1 The x-coordinate of the first point
	 * @param y1 The y-coordinate of the first point
	 * @param x2 The x-coordinate of the second point
	 * @param y2 The y-coordinate of the second point
   <]
   public double radsMovedBetweenPoints(
   float x1, float y1, float x2, float y2) {
   double angle1 = coordsToRads(x1, y1);
   double angle2 = coordsToRads(x2, y2);
   return (double) (angle2-angle1);
   }*/

	/**
	 * Initializes some values and all of the fancy paints and listeners
	 */
	private void init() {

		// set up the circle paint
		mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mCirclePaint.setStyle(Paint.Style.STROKE);

		mCategoryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mCategoryPaint.setStyle(Paint.Style.FILL);

		// set up the touchpoint paint
		mTouchPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mTouchPointPaint.setStyle(Paint.Style.FILL);
		mTouchPointPaint.setColor(mTouchPointColor);

		// set up the separatorLines paint
		mSeparatorLinesPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mSeparatorLinesPaint.setStyle(Paint.Style.STROKE);

		printTouchPoints();
		sortListCW(mPoints, 0);
		printTouchPoints();

		// set up the gesture detector
		mGestureDetector = new GestureDetector(
				Chart_View.this.getContext(), 
				new GestureListener());

		// Turn off long press--this control doesn't use it, and if long press is
		// enabled, you can't scroll for a bit, pause, then scroll some more 
		// (the pause is interpreted as a long press, apparently)
		mGestureDetector.setIsLongpressEnabled(false);

		invalidate();
	}
	private void printTouchPoints() {
    for (int i = 0; i< mPoints.size(); i++) {
      System.out.println(i + ": " + mPoints.get(i).mRads);
    }
		/*for (TouchPoint p : mPoints) {
			System.out.println("" + p.mRads);
		}*/
	}

	/**
	 * Container for touch points
	 */
	private class TouchPoint implements Comparable<TouchPoint> {
		public double mRads;
		public boolean isBeingTouched = false;


		/**
		 * positive if getDifference(this, tp) is positive, 0 if it's 0,
		 * negative if it's negative.
		 * @param tp The touchpoint to compare this to
		 */
		public int compareTo(TouchPoint tp) throws ClassCastException {
			if (getDifference(this.mRads, tp.mRads) == 0) {
				return 0;
			} else {
				return getDifference(this.mRads, tp.mRads) > 0 ? 1 : -1;
			}
		}
	}

	/**
	 * Moves start delta degrees clockwise
	 * @param start The start angle in radians
	 * @param delta The amount to rotate start by
	 */
	public double moveRadCW(double start, double delta) {
		// if we're going from + to - (bottom to top)
		if (start + delta > Math.PI) {
			double radsUntilPI = Math.PI - start;
			delta -= radsUntilPI;
			return -1*(Math.PI - delta);
		} else {
			start += delta;
			return start;
		}
	}

	/**
	 * Moves start by delta degrees counter clockwise
	 * @param start The start angle in radians
	 * @param delta The amount to rotate start by
	 */
	public double moveRadCCW(double start, double delta) {
    if (start - delta < -1*Math.PI) {
      double radsUntilPI = Math.PI + start;
      delta -= radsUntilPI;
      return (Math.PI - delta);
    } else {
      start -= delta;
      return start;
    }
	}

	/**
	 * returns true if the given x and y coords are "inside" of point p - in 
	 * quotes because we're a little lenient to give the users some wiggle room.
	 * @param x The x value of the coordinate to check
	 * @param y The y-value of the coordinate to check
	 * @param p The TouchPoint to check
	 */
	private boolean isTouchingThisPoint(float x, float y, TouchPoint p) {
		PointF pCoords = radsToPointF((double)p.mRads);
		double dist = Math.sqrt( 
				Math.pow( (double)pCoords.x - x, 2) +
				Math.pow( (double)pCoords.y - y, 2));
		// make it * 2 to give users a little breathing room(the dots are small)
		return dist <= mTouchPointRadius*2;
	}

	/**
	 * returns true if d1 is within 30 degrees in front of d2
	 * @param start the reference radian value
	 * @param start the radian value to check the position of
	 */
	private boolean movingClockwise(double start, double end) {
		double diff = getDifference(start, end);
		return diff > 0;
	}

	/**
	 * returns true if the given point has another point in front of it
	 * (clockwise) within ANGLE_THRESHOLD or less - should only be called if 
	 * the touchPoint is being rotated clockwise.
	 * @param p1 the point to check
	 */
	private boolean hasPointInFront(TouchPoint p1) {
		for (TouchPoint point : mPoints) {
			// edge case
			if (point.mRads < 0 && p1.mRads > 0 &&
					((Math.PI + point.mRads + Math.PI - p1.mRads) <= ANGLE_THRESHOLD)) {
				return true;
			} else if (point.mRads > p1.mRads &&
					point.mRads - p1.mRads < ANGLE_THRESHOLD) {
				return true;
			}
		}
		return false;
	}

	/**
	 * returns true if the given point has another point in behind it
	 * (counter-clockwise) within 10 degrees or less - should only be called if 
	 * the touchPoint is being rotated counterclockwise.
	 * @param p1 the point to check
	 */
	private boolean hasPointBehind(TouchPoint p1) {
		for (TouchPoint point : mPoints) {
			// edge case
			if (point.mRads > 0 && p1.mRads < 0 &&
					(Math.PI - point.mRads + Math.PI + p1.mRads <= ANGLE_THRESHOLD)) {
				return true;
			} else if (point.mRads < p1.mRads &&
					p1.mRads - point.mRads <= ANGLE_THRESHOLD) {
				return true;
			}
		}
		return false;
	}

  private boolean isBehind(TouchPoint p1, double rads) {
    if (rads > 0 && p1.mRads < 0 &&
        (Math.PI - rads + Math.PI + p1.mRads <= ANGLE_THRESHOLD)) {
      return true;
        } else if (rads < p1.mRads &&
            p1.mRads - rads <= ANGLE_THRESHOLD) {
          return true;
            }
    return false;
  }

	/**
	 * returns the difference between two radian values, negative if end is
	 * under pi rads away from start, ccw, positive otherwise.
	 * @param start The first angle
	 * @param end The second angle
	 */
	private double getDifference(double start, double end) {
		// if result is greater than pi, subtract it from 2pi.
		double diff;
		//edge case from -pi to pi
		if (end < 0 && start > 0) {
			diff = Math.PI - start + Math.PI + end;
		}
		// reverse case - start is on top, end is on bottom. Also handles reg. case
		else {
			diff = end - start;
		}
		if (diff > Math.PI) {
			diff = diff-Math.PI*2; // negative because it's in ccw rotation
		}
		return diff;
	}

	/**
	 * Returns true if rm is in the arc that is less than pi between r1 and r2.
	 * r1 is start, r2 is end, we're testing to see if rm is in the middle.
	 * @param r1 The first angle
	 * @param rm The angle in question
	 * @param r2 The second angle
	 */
	private boolean isBetween(double r1, double rm, double r2) {
		// if the arc from r1 to r2 is less than pi but greater than 0
		if (movingClockwise(r1, r2)){
			// if the arc from rm to r2 is + but less than the arc from r2 to r1
			if (getDifference(r1, rm) > 0) {
				if( (getDifference(r1, rm) < getDifference(r1, r2))) {
					//Log.d(TAG, rm + " IS BETWEEN " + r1 +" AND " +r2);
					return true;
				}
				else { return false; }
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	private boolean hasPassed(double r1, double rm, double r2) {
		if (movingClockwise(r1, r2)) {
			if (getDifference(r1, rm) > 0 &&
					getDifference(r2, rm) < 0) {
				return true;
			}
		}
		return false;
	}

	/** 
	 * on my anonymous inner class grind - this sorts the given arraylist by
	 * the rotation distance from refRad to each touchPoint, clockwise.
	 * @param pts The arrayList to sort
	 * @param refRad The referance radian measurement
	 */
	private void sortListCW(
			ArrayList<TouchPoint> pts,
			final double refRad) {

		Collections.sort( pts, 
				new Comparator<TouchPoint>() {
			public int compare(TouchPoint a, TouchPoint b) {
				// difference is >0 if clockwise, <0 if not.
				double aDiff = getDifference(refRad, a.mRads);
				double bDiff = getDifference(refRad, b.mRads);
				if (aDiff < 0) {
					aDiff = Math.PI*2 + aDiff;
				}
				if (bDiff < 0) {
					bDiff = Math.PI*2 + bDiff;
				}
				return (aDiff > bDiff ? 1 : (aDiff == bDiff ? 0 : -1));
			}
		}
				);
	}

	/** 
	 * on my anonymous inner class grind - this sorts the given arraylist by
	 * the rotation distance from refRad to each touchPoint, counter-clockwise.
	 * @param pts The arrayList to sort
	 * @param refRad The referance radian measurement
	 */
	private void sortListCCW(
			ArrayList<TouchPoint> pts,
			final double refRad) {

		Collections.sort( pts, 
				new Comparator<TouchPoint>() {
			public int compare(TouchPoint a, TouchPoint b) {
				// difference is >0 if clockwise, <0 if not.
				double aDiff = getDifference(refRad, a.mRads);
				double bDiff = getDifference(refRad, b.mRads);
				if (aDiff < 0) {
					aDiff = Math.PI*2 + aDiff;
				}
				if (bDiff < 0) {
					bDiff = Math.PI*2 + bDiff;
				}
				return (aDiff > bDiff ? -1 : (aDiff == bDiff ? 0 : 1));
			}
		}
				);
    pts.add(0, mPoints.get(mPoints.size()-1));
    pts.remove(mPoints.size()-1);
	}
	/*
  /**
	 * Handles when the scroll movement is finished
	 */
	private void onScrollFinished() {
		for (TouchPoint p : mPoints) {
			p.isBeingTouched = false;
		}
		printTouchPoints();
		inScroll = false;
	}

  /*
   * Fixes errors in the ordering of the points. 
   * i.e:
   * 1 0.5
   * 2 0.23
   * 3 0.72
   * 4 0.89
   * if the ordering if CCW, then it should change to this if the threshold
   * is .1:
   * 1. 0.5
   * 2. 0.6
   * 3. 0.72
   * 4. 0.89
   * or something
   */
  private void fixSkippedPoint(boolean clockwise) {
    ArrayList<Integer> indices = new ArrayList<Integer>();
    for (TouchPoint p : mPoints) {
      for (TouchPoint p2 : mPoints) {

        // if p is the point being touched
        if (mPoints.indexOf(p) == 0) {
          
        }
        if (mPoints.indexOf(p) != 0 &&
            mPoints.indexOf(p2) != 0) {
          // if the both points aren't the one being touched, and 

          // need to keep them IN ORDER.
          if (clockwise) {
            if (getDifference(p.mRads, p2.mRads) >= 0 && p != p2 &&
                mPoints.indexOf(p2) < mPoints.indexOf(p)) {
              Log.d(TAG, "hit CW part, before move: ");
              printTouchPoints();
              Log.d(TAG, "p is " +mPoints.indexOf(p)+", " +p.mRads);
              Log.d(TAG, "p2 is " + mPoints.indexOf(p2)+", " +p2.mRads);
              //Log.d(TAG, "moving " +mPoints.indexOf(p2) + " at " +p2.mRads+ " to " +moveRadCCW(p.mRads, ANGLE_THRESHOLD));
              // this moves it behind the point in question, not to the correct spot.  
              // also this can be called when we only skip over ONE, instead of many.
              // take that into account.
              Log.d(TAG, "moving " +mPoints.indexOf(p) + " at " +p.mRads+ " to " +moveRadCCW(p2.mRads, ANGLE_THRESHOLD));
              p.mRads = moveRadCW(p2.mRads, ANGLE_THRESHOLD);
              //p2.mRads = moveRadCW(p.mRads, ANGLE_THRESHOLD);
              Log.d(TAG, "After move: ");
              printTouchPoints();
            }
          } else {
            if (getDifference(p.mRads, p2.mRads) > 0) {
              Log.d(TAG, "hit CCW part, before move: ");
              printTouchPoints();
              Log.d(TAG, "moving " +mPoints.indexOf(p2) + " at " +p2.mRads+ " to " +moveRadCCW(p.mRads, ANGLE_THRESHOLD));
              p2.mRads = moveRadCCW(p.mRads, ANGLE_THRESHOLD);
              Log.d(TAG, "After move: ");
              printTouchPoints();
            }
          }
        }
      }
    }
  }

  /**
   * Return a list of interpolated radians, given a start and end radian, using
   * the ANGLE_INTERVAL global constant.
   * @param start The start radian
   * @param end The end radian
   * @param cw True if we're moving clockwise
   */
  private ArrayList<Double> interpolate(
      double start, double finish, boolean cw) {
    ArrayList<Double> interpolated = new ArrayList<Double>();
    double diff = getDifference(start, finish);

    // moving CW
    if (cw) {
      int numAngles = (int)(diff/ANGLE_INTERVAL);
      Log.d(TAG, "@@interpolate | numAngles = "+numAngles);
      for (int i=0; i < numAngles; i++) {
        double toAdd = moveRadCW(start, i*ANGLE_INTERVAL);
        interpolated.add(i, toAdd);
        Log.d(TAG, "@@interpolate_ | adding " +toAdd);
      }
      // make sure the last value is in there
      interpolated.add(finish);

    }else {
      diff = Math.abs(diff);
      int numAngles = (int)(diff/ANGLE_INTERVAL);
      for (int i=0; i < numAngles; i++) {
        double toAdd = moveRadCCW(start, i*ANGLE_INTERVAL);
        interpolated.add(i, toAdd);
        Log.d(TAG, "@@interpolate_ | adding " +toAdd);
      }
    }
    return interpolated;
  }

  /**
   * Moves the point being touched to the given radian, moving all other
   * points accordingly.
   * @param rad The radian to move the point being touched to
   * @param cw True if we're moving clockwise, false otherwise
   */
  private void movePointBeingTouched(double rads, boolean cw) {
    // move point being touched to the given radian value
    mPoints.get(0).mRads = rads;
    // move other points
    Log.d(TAG, "@@movePointBeingTouched | before for loop");
    printTouchPoints();
    for (TouchPoint pt : mPoints) {
      if (!pt.isBeingTouched) {
        if (cw) {
          if (hasPointBehind(pt)) {
            double prevPointRads = mPoints.get(mPoints.indexOf(pt)-1).mRads;
            pt.mRads = moveRadCW(prevPointRads, ANGLE_THRESHOLD);
            Log.d(TAG, "@@movePointBeingTouched | CW moving "+mPoints.indexOf(pt) +" to " +moveRadCW(prevPointRads, ANGLE_THRESHOLD));
          }
        } else {
          if (hasPointInFront(pt)) {
            double nextPointRads = mPoints.get(mPoints.indexOf(pt)-1).mRads;
            pt.mRads = moveRadCCW(nextPointRads, ANGLE_THRESHOLD);
            Log.d(TAG, "@@movePointBeingTouched | CCW moving "+mPoints.indexOf(pt) +" to " +moveRadCCW(nextPointRads, ANGLE_THRESHOLD));
          }
        }
      } else { // we're touching it
      }
    }
    Log.d(TAG, "@@movePointBeingTouched | after for loop");
    printTouchPoints();
    invalidate();
  }

  private boolean skippedPoint(double curRad, double lastRad, boolean cw) {
    boolean skipped = false;
    for (TouchPoint pt : mPoints) {
      if (!pt.isBeingTouched) {
        if (cw && !pt.isBeingTouched && hasPassed(lastRad, pt.mRads, curRad)) {
          skipped = true;
        }
        else if (hasPassed(curRad, pt.mRads, lastRad)) {
          skipped = true;
        }
      }
    }
    return skipped;
  }


  /**
   * Move all points in mPoints, given the curRad and lastRad values from an
   * onScrollEvent
   * @param curRad The current radian of the onScroll
   * @param lastRad The last radian of the onScroll
   * @param clockwise True if we're moving clockwise, false otherwise
   */
  private void movePoints(double curRad, double lastRad, boolean clockwise) {
    Log.d(TAG, "@@_movePoints( "+curRad+", "+lastRad+", clockwise: "+clockwise+" )");
    // mPoints is sorted clockwise or ccw from the point being touched(0)
    // Get the radians moved. should be positive if clockwise==true
    if (skippedPoint(curRad, lastRad, clockwise)) {
      Log.d(TAG, "@@_movePoints | >>SKIPPED<<");

    /* double diff = getDifference(lastRad, curRad); */
    // if we've moved more than our designated interval
    /* if (Math.abs(diff) > ANGLE_INTERVAL) { */
      Log.d(TAG, "@@_movePoints | diff > interval. Interpolating...");
      // interpolate the values
      ArrayList<Double> interpolated = interpolate(mPoints.get(0).mRads, curRad, clockwise);
      for (Double rad : interpolated) {
        Log.d(TAG, "@@_movePoints | moving pt being touched to : " +rad +" from " +mPoints.get(0).mRads);
        movePointBeingTouched(rad, clockwise);
      }
      Log.d(TAG, "@@_movePoints | done interpolating");
    } else {
      movePointBeingTouched(curRad, clockwise);
    }
    invalidate();
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

        // calculate the degree values of the last touch event and the 
        // current touch event
        double lastRad = coordsToRads(lastX, lastY);
        double curRad = coordsToRads(e2.getX(), e2.getY());

        // have we moved clockwise?
        boolean clockwise = movingClockwise(lastRad, curRad);

        if (!inScroll) {
          for (TouchPoint p : mPoints) {
            // mark the point being touched
            if (isTouchingThisPoint(e1.getX(), e1.getY(), p)) {
              inScroll = true;
              p.isBeingTouched = true;
            }
          }
        }
        for (TouchPoint p : mPoints) {
          if (p.isBeingTouched) {
            if (clockwise) {
              sortListCW(mPoints, p.mRads);
            } else {
              sortListCCW(mPoints, p.mRads);
            }
          }
        }
        movePoints(curRad, lastRad, clockwise);





    /* //-------------------------------------------- */
    /* // Figure out which one we're touching. */
    /* for ( TouchPoint p : mPoints ){ */
    /*   if (isTouchingThisPoint(e1.getX(), e1.getY(), p)) { */
    /*     inScroll = true; */
    /*     p.isBeingTouched = true; */
    /*   } if (p.isBeingTouched) { */
    /*     p.mRads = coordsToRads(e2.getX(), e2.getY()); */
    /*     invalidate(); */
    /*     if (p.isBeingTouched) { */
    /*       if (clockwise) { */
    /*         sortListCW(mPoints, p.mRads); */
    /*       } else { */
    /*         sortListCCW(mPoints, p.mRads); */
    /*       } */
    /*     } */
    /*     boolean skipped = false; */
    /*     for (TouchPoint pt : mPoints) { */
    /*       if (!pt.isBeingTouched) { */
    /*         // Skipping clockwise */
    /*         if (hasPassed(lastRad, pt.mRads, curRad)) { */
    /*           skipped = true; */
    /*         } */

    /*         // Skipping counter-clockwise */
    /*         if (hasPassed(curRad, pt.mRads, lastRad)) { */
    /*           skipped = true; */
    /*         } */
    /*       } */
    /*     } */

    /*     //  */
    /*     if (skipped) { */
    /*       Log.d(TAG, "@@@Skipped a point!"); */
    /*       //fixSkippedPoint(clockwise); */
    /*       interpolateRads(lastRad, curRad); */
    /*       //refreshCategoryPoints(); */
    /*       invalidate(); */
    /*     } */
    /*   } */
    /*   movePoints(curRad, lastRad, clockwise); */

    /*   inScroll = true; */
    /*   invalidate(); */
    /*   return true; */
    /* } */
    invalidate();
    return true;

  }

  // we need to return true here so we can actually scroll.
  public boolean onDown(MotionEvent e) {
    return true;
  }
}

}


