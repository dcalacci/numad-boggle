package edu.madcourse.dancalacci.circletouch;


import java.util.ArrayList;

import edu.madcourse.dancalacci.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class Profile extends Activity{

  private static final String TAG = "edu.madcourse.dancalacci.circletouch.Profile";
  private ProfileBar mProfileBar;
  private ArrayList<ProfileBarSegment> mProfileBarSegments = 
    new ArrayList<ProfileBarSegment>();

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
  }


  public void onAddChartClick(View v){
    Intent i = new Intent(this, CategorySelection.class);
    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    startActivity(i);
  }

  private void setAllSegmentValues() {
    Log.d(TAG, "Setting segment values");
    for (ProfileBarSegment seg : mProfileBarSegments) {
      setSegmentValueByColor(seg.getValue(), seg.getColor());
      Log.d(TAG, "setting "+seg.getColor()+" to " +seg.getValue());
    }
    // if nothing has been tracked
    if (mProfileBarSegments.isEmpty()) {
      Log.d(TAG, "mProfileBarSegments is empty");
      setAllSegmentsToZero();
    }
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
    RelativeLayout rl = (RelativeLayout)v;
    LinearLayout.LayoutParams lp =
      (LinearLayout.LayoutParams)rl.getLayoutParams();
    lp.weight = (float)val;
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
