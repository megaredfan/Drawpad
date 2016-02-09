package joseph.drawpad.model;

import joseph.drawpad.utils.CurveType;

import java.util.List;

/**
 * Created by 熊纪元 on 2016/2/9.
 */
public class Curve {
    private Point[] points;
    private String name;
    private CurveType type;
    private List<Float> parameters;
    private int width;

    public Curve(){ super();}

    public Curve(Point[] points, String name, CurveType type, List<Float> parameters) {
        this.points = points;
        this.name = name;
        this.type = type;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Point[] getPoints() {
        return points;
    }

    public void setPoints(Point[] points) {
        this.points = points;
    }

    public CurveType getType() {
        return type;
    }

    public void setType(CurveType type) {
        this.type = type;
    }

    public List<Float> getParameters() {
        return parameters;
    }

    public void setParameters(List<Float> parameters) {
        this.parameters = parameters;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Point[] calculateCurve(int height) {
        if(this == null)
            return null;
        int length = width;
        Point[] points = new Point[length];
        switch (getType()) {
            case Linear:
                for(int y = 0; y < length; y++) {
                    //一次函数
                    points[y] = new Point();
                    points[y].setY(y);
                    points[y].setX(height/2f - width/2f + y);
                    //ptsX[y] = height/2f - width/2f + y;
                    //二次函数
                    //ptsX[y] = ((y-windowWidth/2.0f)*(y-windowWidth/2.0f))/100.0f + windowHeight/2.0f;
                    //正弦函数
                    //ptsX[y] = ((float) Math.sin(((double) (y-windowWidth/2)/50))) * 100 + windowHeight/2;

                }
                break;
            case Quadratic:
                for(int y = 0; y < length; y++) {
                    //一次函数
                    //ptsX[y] = height/2f - width/2f + y;
                    //二次函数
                    points[y] = new Point();
                    points[y].setY(y);
                    points[y].setX(((y-width/2f)*(y-width/2f))/100f + height/2f);
                    //ptsX[y] = ((y-width/2f)*(y-width/2f))/100f + height/2f;
                    //正弦函数
                    //ptsX[y] = ((float) Math.sin(((double) (y-windowWidth/2)/50))) * 100 + windowHeight/2;

                }
                break;
            case Sinusoidal:
                for(int y = 0; y < length; y++) {
                    //一次函数
                    //ptsX[y] = height/2f - width/2f + y;
                    //二次函数
                    //ptsX[y] = ((y-width/2f)*(y-width/2f))/100f + height/2f;
                    //正弦函数
                    points[y] = new Point();
                    points[y].setY(y);
                    points[y].setX(((float) Math.sin(((double) (y-width/2)/50))) * 100f + height/2f);
                    //ptsX[y] = ((float) Math.sin(((double) (y-width/2)/50))) * 100f + height/2f;
                }
                break;
            default:
                break;
        }
        return points;
    }
}
