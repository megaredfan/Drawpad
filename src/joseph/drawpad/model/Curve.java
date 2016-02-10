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

    public Curve(){ super();}

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

    public Point[] calculate(int height, int width) {
        Point[] points = new Point[width];
        switch (getType()) {
            case Linear:
                for (int y = 0; y < width; y++) {
                    points[y] = new Point();
                    points[y].setY(y);
                    float X, Y, A, B;
                    if (this.getParameters() != null && this.getParameters().size() == 2) {
                        Y = -(width / 2f - y);
                        B = height / 2f + this.getParameters().get(1);
                        A = this.getParameters().get(0);
                        X = A * Y + B;
                    } else
                        X = -(width/2f - y) + height/2f;
                    points[y].setX(X);
                }
                break;
            case Quadratic:
                for(int y = 0; y < width; y++) {
                    points[y] = new Point();
                    points[y].setY(y);
                    float Y,X,A,B,C;
                    if(this.getParameters() != null && this.getParameters().size() == 3) {
                        Y = -(width/2f - y);
                        A = this.getParameters().get(0);
                        B = this.getParameters().get(1);
                        C = this.getParameters().get(2) + height/2f;
                        X = A * Y * Y + B * Y + C;
                        points[y].setX(X);
                    }
                    else
                        points[y].setX(((y-width/2f)*(y-width/2f))/100f + height/2f);
                }
                break;
            case Sinusoidal:
                for(int y = 0; y < width; y++) {
                    points[y] = new Point();
                    points[y].setY(y);
                    float Y,X,A,B,C,D;
                    if(this.getParameters() != null && this.getParameters().size() == 4) {
                        Y = -(width/2f - y);
                        A = this.getParameters().get(0);
                        B = this.getParameters().get(1);
                        C = this.getParameters().get(2);
                        Y = B * Y + C;
                        D = this.getParameters().get(3) + height/2f;
                        X = (float)(A * Math.sin((double)Y) + D);
                        points[y].setX(X);
                    } else
                        points[y].setX(((float) Math.sin(((double) (y-width/2)/50))) * 100f + height/2f);
                }
                break;
            default:
                break;
        }
        return points;
    }
}
