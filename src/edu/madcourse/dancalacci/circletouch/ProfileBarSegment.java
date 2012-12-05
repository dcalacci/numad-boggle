package edu.madcourse.dancalacci.circletouch;
// Container for segments
public class ProfileBarSegment {
  //  percentage is the percent value of this segment, color is the color
  private double mValue;
  private int mColor;

  public ProfileBarSegment(Category c) {
    this.mValue = (ProfileBar.categoryToPercentage(c));
    this.mColor = c.getColor();
  }

  public ProfileBarSegment(double value, int color) {
    this.mValue = value;
    this.mColor = color;
  }

  public double getValue() {
    return mValue;
  }
  public int getColor() {
    return mColor;
  }
  public void setValue(double val) {
    this.mValue = val;
  }
  public void setColor(int color) {
    this.mColor = color;
  }
  
  public String getString(){
	  return "" + mValue;
  }
}
