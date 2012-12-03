package edu.madcourse.dancalacci.circletouch;


public  class Category {
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
