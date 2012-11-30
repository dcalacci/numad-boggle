package edu.madcourse.dancalacci.circletouch;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class History_View extends View{
	private String TAG = "circletoucn.history";
	private Canvas canvas;
	private RectF mCircleBounds = new RectF();
	
    private float scrollByX = 0; // x amount to scroll by
    private float scrollByY = 0; // y amount to scroll by
	
    
    
    // List containing all the charts to be drawn on Canvas
    private ArrayList<Chart> mCharts = new ArrayList<Chart>();
    
	
	public History_View(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public History_View(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
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
