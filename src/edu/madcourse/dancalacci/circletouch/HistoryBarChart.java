package edu.madcourse.dancalacci.circletouch;
// Container for segments
public class HistoryBarChart {
  //  percentage is the percent value of this segment, color is the color
  private double mValue;
  private String mCategory;

  public HistoryBarChart(Category c) {
    this.mValue = (ProfileBar.categoryToPercentage(c));
    this.mCategory = c.getCategory();
  }

  public HistoryBarChart(double value, String color) {
    this.mValue = value;
    this.mCategory = color;
  }

  public double getValue() {
    return mValue;
  }
  public String getCategory() {
    return mCategory;
  }
  public void setValue(double val) {
    this.mValue = val;
  }
  public void setCategory(String color) {
    this.mCategory = color;
  }
  
  public String getString(){
	  return "" + mValue;
  }
}
