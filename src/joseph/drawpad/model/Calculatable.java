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
    protected int scaleTimes;

    public abstract String getName();

    public abstract void setName(String name);

    public abstract List<Float> getParameters();

    public abstract void setParameters(List<Float> parameters);

    public abstract Point[] getPoints();

    public abstract void setPoints(Point[] points);

    public abstract View getView(LayoutInflater li);

    public abstract Point[] calculate(int width);

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

    public int getScaleTimes() {
        return scaleTimes;
    }

    public void setScaleTimes(int scaleTimes) {
        this.scaleTimes = scaleTimes;
    }

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

    public abstract Calculatable scale(float multiple, int width, boolean zoomIn);
}
