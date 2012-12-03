package edu.madcourse.dancalacci.circletouch;


/**
 * Container for touch points
 */
public class TouchPoint implements Comparable<TouchPoint> {
  public double mRads;
  public boolean isBeingTouched = false;


  public TouchPoint() {
    super();
    this.mRads = mRads;
  }

  public TouchPoint(double mRads) {
    super();
    this.mRads = mRads;
  }

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

  public double getmRads(){
    return this.mRads;
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

}
