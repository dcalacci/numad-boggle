package edu.madcourse.dancalacci.circletouch;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.madcourse.dancalacci.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Profile extends Activity{

  private static final String TAG = "edu.madcourse.dancalacci.circletouch.Profile";
  private ProfileBar mProfileBar;
  HashMap<Integer, Double> allCategoryAverages = new HashMap<Integer, Double>();
  private ArrayList<ProfileBarSegment> mProfileBarSegments = 
    new ArrayList<ProfileBarSegment>();
  private ArrayList<Integer> mSegmentColors = 
    new ArrayList<Integer>();

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.platechart_profile);
    processGraph();
  }

  protected void onResume() {
    super.onResume();
    processGraph();
  }
  
  private void processGraph(){
	  /* setContentView(R.layout.platechart_profile); */
	    this.mProfileBar = new ProfileBar(this);
	    this.mProfileBarSegments = mProfileBar.averageAllSegments();
	    
	    /* for (ProfileBarSegment m : mProfileBarSegments ){ */
	    /* 	Log.d(TAG, "mProfileBarContents: " + m.getString()); */
	    /* } */
	    
	    Log.d(TAG, "### mProfileBarSegments: " + this.mProfileBarSegments.size());
	    //this.setAllSegmentValues();
	    
	    this.setHashMapCategoryAverages();
	    this.updateHashMapCategoryAverages();
	    this.setAllSegmentValues();
	    
	    /* View v = findViewById (R.id.platechart_profile); */
	    /* v.invalidate(); */
	    mSegmentColors.add(getResources().getColor(R.color.Protein));
	    mSegmentColors.add(getResources().getColor(R.color.Vegetable));
	    mSegmentColors.add(getResources().getColor(R.color.Dairy));
	    mSegmentColors.add(getResources().getColor(R.color.Grain));
	    mSegmentColors.add(getResources().getColor(R.color.Fruit));
	    mSegmentColors.add(getResources().getColor(R.color.Oil_Sugar));
  }


  public void onAddChartClick(View v){
    Intent i = new Intent(this, CategorySelection.class);
    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    startActivity(i);
  }
  
  private void setHashMapCategoryAverages(){
	  double base = 0;
	  allCategoryAverages.put(getResources().getColor(R.color.Protein), base);
	  allCategoryAverages.put(getResources().getColor(R.color.Vegetable), base);
	  allCategoryAverages.put(getResources().getColor(R.color.Dairy), base);
	  allCategoryAverages.put(getResources().getColor(R.color.Grain), base);
	  allCategoryAverages.put(getResources().getColor(R.color.Fruit), base);
	  allCategoryAverages.put(getResources().getColor(R.color.Oil_Sugar), base);
  }
  
  private void updateHashMapCategoryAverages() {
	  Log.d(TAG, "profile size: " + mProfileBarSegments.size());
	  Log.d(TAG, "BEFORE HASHMAP content" + allCategoryAverages.toString());
	  if(mProfileBarSegments.size() == 1){
		  ProfileBarSegment p = mProfileBarSegments.get(0);
		  allCategoryAverages.remove(p.getColor());
		  allCategoryAverages.put(p.getColor(), 1.00);
	  }
	  else if(mProfileBarSegments.size() > 1 ){
		  for (ProfileBarSegment p : mProfileBarSegments){
			  Log.d(TAG, "Value : " + p.getValue());
			  allCategoryAverages.remove(p.getColor());
			  allCategoryAverages.put(p.getColor(), p.getValue());
		  }
	  }else {
		  setAllSegmentsToZero();
	  }
	  
	  Log.d(TAG, "AFTER HASHMAP content" + allCategoryAverages.toString());
	  
  }

  /**
   * Sets all the segment values to zero
   */
  private void setAllSegmentsToZero() {
    setSegmentValueByColor(0, getResources().getColor(R.color.Protein));
    setSegmentValueByColor(0, getResources().getColor(R.color.Vegetable));
    setSegmentValueByColor(0, getResources().getColor(R.color.Dairy));
    setSegmentValueByColor(0, getResources().getColor(R.color.Grain));
    setSegmentValueByColor(0, getResources().getColor(R.color.Fruit));
    setSegmentValueByColor(0, getResources().getColor(R.color.Oil_Sugar));
  }
  
  private void setAllSegmentValues(){
	// Protein
	this.setSegmentValueByColor(
			allCategoryAverages.get(getResources().getColor(R.color.Protein)), 
			getResources().getColor(R.color.Protein));
	
	// Vegetable
	this.setSegmentValueByColor(
			allCategoryAverages.get(getResources().getColor(R.color.Vegetable)), 
			getResources().getColor(R.color.Vegetable));
	
	// Dairy
	this.setSegmentValueByColor(
			allCategoryAverages.get(getResources().getColor(R.color.Dairy)), 
			getResources().getColor(R.color.Dairy));
	
	// Grain
	this.setSegmentValueByColor(
			allCategoryAverages.get(getResources().getColor(R.color.Grain)), 
			getResources().getColor(R.color.Grain));
	
	// Fruit
	this.setSegmentValueByColor(
			allCategoryAverages.get(getResources().getColor(R.color.Fruit)), 
			getResources().getColor(R.color.Fruit));
	
	// Oil Sugar
	this.setSegmentValueByColor(
			allCategoryAverages.get(getResources().getColor(R.color.Oil_Sugar)), 
			getResources().getColor(R.color.Oil_Sugar));
		
  }
  
  /**
   * Sets the segment's value to the given val.
   * @param val The value to set the segment to
   * @param c The color identifier for the segment
   */
  private void setSegmentValueByColor(double val, int c) {
    View v = findSegmentViewByColor(c);
    TextView tv = (TextView) v;
    TableRow.LayoutParams lp =
      (TableRow.LayoutParams)tv.getLayoutParams();
    lp.weight = (float)val;
  }
  /**
   * Returns the View that corresponds to the selected segment's 
   * color
   * @param c The color of the segment
   */
  private View findSegmentViewByColor(int c) {
    if (c == getResources().getColor(R.color.Protein)) {
      return findViewById(R.id.profile_protein_block);
    } else if (c == getResources().getColor(R.color.Grain)) {
      return findViewById(R.id.profile_grain_block);
    } else if (c == getResources().getColor(R.color.Dairy)) {
      return findViewById(R.id.profile_dairy_block);
    } else if (c == getResources().getColor(R.color.Fruit)) {
      return findViewById(R.id.profile_fruit_block);
    } else if (c == getResources().getColor(R.color.Vegetable)) {
      return findViewById(R.id.profile_vegetable_block);
    } else {
      return findViewById(R.id.profile_oil_block);
    }
  }
}
