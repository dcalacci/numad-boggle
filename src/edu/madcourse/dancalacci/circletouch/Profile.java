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
  private ArrayList<ProfileBarSegment> mProfileBarSegments = 
    new ArrayList<ProfileBarSegment>();
  private ArrayList<Integer> mSegmentColors = 
    new ArrayList<Integer>();

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.platechart_profile);
  }

  protected void onResume() {
    super.onResume();
    /* setContentView(R.layout.platechart_profile); */
    this.mProfileBar = new ProfileBar(this);
    this.mProfileBarSegments = mProfileBar.averageAllSegments();
    this.setAllSegmentValues();
    /* View v = findViewById (R.id.platechart_profile); */
    /* v.invalidate(); */
    mSegmentColors.add(getResources().getColor(R.color.Protein));
    mSegmentColors.add(getResources().getColor(R.color.Vegetable));
    mSegmentColors.add(getResources().getColor(R.color.Dairy));
    mSegmentColors.add(getResources().getColor(R.color.Grain));
    mSegmentColors.add(getResources().getColor(R.color.Fruit));
    mSegmentColors.add(getResources().getColor(R.color.Protein));
  }


  public void onAddChartClick(View v){
    Intent i = new Intent(this, CategorySelection.class);
    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    startActivity(i);
  }
  private void setAllSegmentValues() {
  HashMap<Integer, Double> hasBeenAdded = 
    new HashMap<Integer, Double>();
  /* HashSet<Integer> hasBeenAdded = new HashSet<Integer>(); */
    for (ProfileBarSegment seg : mProfileBarSegments) {
      hasBeenAdded.put(seg.getColor(), seg.getValue());
    }

    for (int c : mSegmentColors) {
      if (!hasBeenAdded.containsKey(c)) {
        Log.d(TAG, c+" hasn't been added");
        setSegmentValueByColor(c, 0);
      } else {
        setSegmentValueByColor(hasBeenAdded.get(c), c);
      }
    }
    // go through all the segments from the files, and set the value in the 
    // layout according to the value of the segment
    /* for (ProfileBarSegment seg : mProfileBarSegments) { */
    /*   setSegmentValueByColor(seg.getValue(), seg.getColor()); */
    /*   Log.d(TAG, "setting "+seg.getColor()+" to " +seg.getValue()); */
    /*   int c = seg.getColor(); */
    /*   hasBeenAdded.add(c); */
    /* } */
    // for every segment color we can have, loop through and see if it's been
    // added.  If it hasn't, set the view weight to 0.
    /* for (int c : mSegmentColors) { */
    /*   if (!hasBeenAdded.contains(c)) { */
    /*     Log.d(TAG, "@@@@ setting " +c +" to 0"); */
    /*     setSegmentValueByColor(c, 0); */
    /*   } */
    /* } */
  }

  /**
   * Sets all the segment values to zero
   */
  private void setAllSegmentsToZero() {
    setSegmentValueByColor(getResources().getColor(R.color.Protein), 0);
    setSegmentValueByColor(getResources().getColor(R.color.Vegetable), 0);
    setSegmentValueByColor(getResources().getColor(R.color.Dairy), 0);
    setSegmentValueByColor(getResources().getColor(R.color.Grain), 0);
    setSegmentValueByColor(getResources().getColor(R.color.Fruit), 0);
    setSegmentValueByColor(getResources().getColor(R.color.Oil_Sugar), 0);
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
    /* RelativeLayout rl = (RelativeLayout)v; */
    /* LinearLayout.LayoutParams lp = */
    /*   (LinearLayout.LayoutParams)rl.getLayoutParams(); */
    /* lp.weight = (float)val; */
    /* RelativeLayout.LayoutParams lp = */
    /*   (RelativeLayout.LayoutParams)rl.getLayoutParams(); */
    /* lp.addRule(RelativeLayout.ABOVE, val); */
    /* ((RelativeLayout.LayoutParams)rl.getLayoutParams()).weight = (float)val; */
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
