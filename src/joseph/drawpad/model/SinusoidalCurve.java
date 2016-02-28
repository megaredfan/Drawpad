package joseph.drawpad.model;

import android.view.LayoutInflater;
import android.view.View;
import joseph.drawpad.R;

import java.util.List;

/**
 * Created by 熊纪元 on 2016/2/10.
 */
public class SinusoidalCurve extends Calculatable {
    public List<Float> getParameters() {
        return parameters;
    }

    public void setParameters(List<Float> parameters) {
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

    public float getEndX() {
        return endX;
    }

    public void setEndX(float endX) {
        this.endX = endX;
    }

    public float getStartX() {
        return startX;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    @Override
    public View getView(LayoutInflater li) {
        return li.inflate(R.layout.editsinusoidal, null);
    }

    @Override
    public Point[] calculate(int width) {
        if(parameters == null || parameters.size() != 4)
            return null;

        float Y,A,B,C,D,X;
        A = this.getParameters().get(0);
        B = this.getParameters().get(1);
        C = this.getParameters().get(2);
        D = this.getParameters().get(3);
        Point[] points = new Point[width];

        float delta = (endX - startX)/(width);
        X = startX;
        for(int i = 0; i < width; i++) {
            Y = (float)(A * Math.sin((double)(B * X + C)) + D);
            points[i] = new Point(X,Y);
            X = X + delta;
        }
        return points;
    }

    @Override
    public SinusoidalCurve scale(float times, float height, float width) {
        for (Point p : points) {
            p.setY((p.getY() - width/2)*times + width/2);
            p.setX((p.getX() - height/2)*times + height/2);
        }
        return this;
    }
}
