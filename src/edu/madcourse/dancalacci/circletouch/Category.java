package edu.madcourse.dancalacci.circletouch;

import edu.madcourse.dancalacci.R;
import android.content.Context;



public class Category {
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

    public Category() {
    }

    /** Returns the integer color(Grayed) of the given category
     * @param c The category to get the color of
     */
    public static int getGrayedColor(String c, Context ctx) {
      if (c.equals("Protein")) {
        return ctx.getResources().getColor(R.color.Protein_Grayed);
      } else if (c.equals("Vegetable")) {
        return ctx.getResources().getColor(R.color.Vegetable_Grayed);
      } else if ( c.equals("Fruit")) {
        return ctx.getResources().getColor(R.color.Fruit_Grayed);
      } else if (c.equals("Dairy")) {
        return ctx.getResources().getColor(R.color.Dairy_Grayed);
      } else if (c.equals("Grain")) {
        return ctx.getResources().getColor(R.color.Grain_Grayed);
      } else {
        return ctx.getResources().getColor(R.color.Oil_Sugar_Grayed);
      }
    }

    /** Returns the integer color of the given category
     * @param c The category to get the color of
     */
    public static int getColor(String c, Context ctx) {
      if (c.equals("Protein")) {
        return ctx.getResources().getColor(R.color.Protein);
      } else if (c.equals("Vegetable")) {
        return ctx.getResources().getColor(R.color.Vegetable);
      } else if ( c.equals("Fruit")) {
        return ctx.getResources().getColor(R.color.Fruit);
      } else if (c.equals("Dairy")) {
        return ctx.getResources().getColor(R.color.Dairy);
      } else if (c.equals("Grain")) {
        return ctx.getResources().getColor(R.color.Grain);
      } else {
        return ctx.getResources().getColor(R.color.Oil_Sugar);
      }
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
