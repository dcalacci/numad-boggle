package edu.madcourse.dancalacci.circletouch;

import java.util.ArrayList;

import edu.madcourse.dancalacci.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Profile extends Activity{

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.platechart_profile);
  }

  public void onAddChartClick(View v){
    Intent i = new Intent(this, AddChart.class);
    startActivity(i);
  }

  /**
   * Converts an ArrayList of categories to an ArrayList of segments
   * @param cats The list of categories to convert
   */
  private ArrayList<Segment> categoriesToSegments(ArrayList<Category> cats) {
    ArrayList<Segment> segments = new ArrayList<Segment>();
    // iterate through cats, calculating percentage for each
    for (Category cat : cats) {
      segments.add(new Segment(cat));
    }
    return segments;
  }

  /**
   * Uses the rad values of the given category to calculate the percentage
   * of the circle it takes up
   * @param cat The category we're using to calculate the percentages
   */
  private double categoryToPercentage(Category cat) {
    double diff = TouchPoint.getDistCW(cat.getpCCW(), cat.getpCW());
    double frac = diff/(2*Math.PI);
    return frac;
  }

  // Container for segments
  private class Segment {
    // percentage is the percent value of this segment, color is the color
    double mPercentage;
    int mColor;

    public Segment(Category c) {
      this.mPercentage = (categoryToPercentage(c));
      this.mColor = c.getColor();
    }

    public Segment(double percentage, int color) {
      this.mPercentage = percentage;
      this.mColor = color;
    }
  }
}
