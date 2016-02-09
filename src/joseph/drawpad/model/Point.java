package joseph.drawpad.model;

/**
 * Created by 熊纪元 on 2016/2/9.
 */
public class Point {
    private float x,y;

    public Point() {}

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
