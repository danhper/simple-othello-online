package game;

import java.awt.Point;

public class Move {
    private MyPoint point;
    private int before;
    private int after;

    public Move(Point point, int before, int after) {
        this.point = new MyPoint(point.x, point.y);
        this.before = before;
        this.after = after;
    }

    public MyPoint getPoint() {
        return point;
    }

    public int getBefore() {
        return before;
    }

    public int getAfter() {
        return after;
    }
}
