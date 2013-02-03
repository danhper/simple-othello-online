package communication;

import java.awt.Point;


public class MoveContent extends Content {
    protected Point point;
    protected int color;
    
    public MoveContent() {
        this.point = null;
    }
    
    public MoveContent(int color, Point point) {
        this.color = color;
        this.point = point;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public String format() {
        return this.color + "-" + this.point.x + "," + this.point.y;        
    }
    
    @Override
    public boolean isEmpty() {
        return this.point == null;
    }
    
}
