package joseph.drawpad.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import joseph.drawpad.model.Point;

import java.util.List;

/**
 * Created by 熊纪元 on 2016/2/10.
 */
public interface Calculatable {
    void setName(String name);
    String getName();
    void setParameters(List<Float> parameters);
    List<Float> getParameters();
    void setPoints(Point[] points);
    Point[] getPoints();
    View getView(LayoutInflater li);
    List<Float> initParameters(LinearLayout layout);
    Point[] calculate(int height, int width);
}
