package joseph.drawpad.model;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 熊纪元 on 2016/2/10.
 */
public abstract class Calculatable {
    protected Point[] points;
    protected String name;
    protected List<Float> parameters;
    protected float startX,endX;

    public abstract String getName();

    public abstract void setName(String name);

    public abstract List<Float> getParameters();

    public abstract void setParameters(List<Float> parameters);

    public abstract Point[] getPoints();

    public abstract void setPoints(Point[] points);

    public abstract View getView(LayoutInflater li);

    public abstract float getEndX();

    public abstract void setEndX(float endX);

    public abstract  float getStartX();

    public abstract void setStartX(float startX);

    public abstract Point[] calculate(int width);

    public abstract Calculatable scale(float times, float height, float width);

    public List<Float> initParameters(LinearLayout layout) {
        List<Float> parameters = new ArrayList<>();
        TableRow tableRow = ((TableRow) ((TableLayout) layout.getChildAt(0)).getChildAt(0));
        int count = tableRow.getChildCount();
        for (int i = 0; i < count; i++) {
            if (tableRow.getChildAt(i) instanceof EditText) {
                EditText et = (EditText) tableRow.getChildAt(i);
                try {
                    parameters.add(Float.valueOf(et.getText().toString()));
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return parameters;
    }

    public Calculatable translate(float dx, float dy) {
        for(Point p : getPoints()) {
            p.setX(p.getX() + dx);
            p.setY(p.getY() + dy);
        }
        return this;
    }

}
