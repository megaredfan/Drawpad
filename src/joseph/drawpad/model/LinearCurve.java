package joseph.drawpad.model;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import joseph.drawpad.R;
import joseph.drawpad.utils.Calculatable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 熊纪元 on 2016/2/10.
 */
public class LinearCurve implements Calculatable {
    private Point[] points;
    private String name;
    private List<Float> parameters;

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

    @Override
    public View getView(LayoutInflater li) {
        return li.inflate(R.layout.editlinear,null);
    }

    public void setPoints(Point[] points) {
        this.points = points;
    }

    @Override
    public List<Float> initParameters(LinearLayout layout) {
        List<Float> parameters = new ArrayList<>();
        TableRow tableRow = ((TableRow) ((TableLayout) layout.getChildAt(0)).getChildAt(0));
        int count = tableRow.getChildCount();
        for (int i = 0; i < count; i++) {
            if(tableRow.getChildAt(i) instanceof EditText){
                EditText et = (EditText)tableRow.getChildAt(i);
                try{
                    parameters.add(Float.valueOf(et.getText().toString()));
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return parameters;
    }
    @Override
    public Point[] calculate(int height, int width) {
        Point[] points = new Point[width];
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
                return null;
            points[y].setX(X);
        }
        return points;
    }
}
