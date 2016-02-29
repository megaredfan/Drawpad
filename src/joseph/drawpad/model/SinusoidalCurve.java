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
        A = parameters.get(0);
        B = parameters.get(1);
        C = parameters.get(2);
        D = parameters.get(3);
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

    public Calculatable scale(float multiple, int width, boolean  zoomIn) {
        float center = (endX + startX)/2;
        if(zoomIn) {
            setStartX(center - (center - startX)/multiple);
            setEndX(center + (endX - center)/multiple);
            scaleTimes = scaleTimes +1;
        }else{
            setStartX(center - (center - startX)*multiple);
            setEndX(center + (endX - center)*multiple);
            scaleTimes = scaleTimes - 1;
        }

        if(parameters == null || parameters.size() != 4)
            return null;

        float Y,A,B,C,D,X;
        A = parameters.get(0);
        B = parameters.get(1);
        C = parameters.get(2);
        D = parameters.get(3);

        Point[] newPoints = new Point[width];

        float delta = (endX - startX)/(width);
        X = startX;
        for(int i = 0; i < width; i++) {
            Y = (float)(A * Math.sin((double)(B * X + C)) + D);
            Y = (float) (Y * Math.pow((double)multiple,(double)scaleTimes));
            newPoints[i] = new Point(X,Y);
            X = X + delta;
        }
        setPoints(newPoints);
        return this;
    }
}
