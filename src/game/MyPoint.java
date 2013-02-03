package game;

import java.awt.Point;

public class MyPoint extends Point implements Comparable<MyPoint> {
  
  public MyPoint(int x, int y) {
    super(x, y);
  }
  
  @Override
  public int compareTo(MyPoint other) {
    if(this.x == other.x && this.y == other.y)
      return 0;
    if(this.x != other.x)
      return this.x - other.x;
    return this.y - other.y;
  }
  
  public void setCoordinates(Point p) {
    this.x = p.x;
    this.y = p.y;
  }
  
  public void setCoordinates(int x, int y) {
    this.x = x;
    this.y = y;
  }
}
