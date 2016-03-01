package joseph.drawpad.model;

import android.view.LayoutInflater;
import android.view.View;
import fr.expression4j.basic.MathematicalElement;
import fr.expression4j.core.Expression;
import fr.expression4j.core.Parameters;
import fr.expression4j.core.exception.EvalException;
import fr.expression4j.core.exception.ParsingException;
import fr.expression4j.factory.ExpressionFactory;
import fr.expression4j.factory.NumberFactory;
import joseph.drawpad.R;

import java.util.List;

/**
 * Created by 熊纪元 on 2016/3/1.
 */
public class UniverseCurve extends Calculatable {
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<Float> getParameters() {
        return this.parameters;
    }

    @Override
    public void setParameters(List<Float> parameters) {
        this.parameters = parameters;
    }

    @Override
    public Point[] getPoints() {
        return this.points;
    }

    @Override
    public void setPoints(Point[] points) {
        this.points = points;
    }

    @Override
    public View getView(LayoutInflater li) {
        return li.inflate(R.layout.edituniverse,null);
    }

    @Override
    public Point[] calculate(int width) {
        try {
            Expression expression = ExpressionFactory.createExpression("f(x)=x");

            Point[] points = new Point[width];

            float delta = (endX - startX)/(width);

            MathematicalElement X = NumberFactory.createReal(startX);
            for(int i = 0; i < width; i++) {
                Parameters ps = ExpressionFactory.createParameters();
                ps.addParameter("x", X);
                MathematicalElement Y = expression.evaluate(ps);
                points[i] = new Point(((float) X.getRealValue()), ((float) Y.getRealValue()));
                X = NumberFactory.createReal(X.getRealValue() + delta);
            }
            return points;
        } catch (ParsingException | EvalException e) {
            e.printStackTrace();
        }
        return points;
    }

    @Override
    public Calculatable scale(float multiple, int width, boolean zoomIn) {
        float center = (startX + endX)/2;
        if(zoomIn) {
            setStartX(center - (center - startX)/multiple);
            setEndX(center + (endX - center)/multiple);
            scaleTimes = scaleTimes +1;
        }else{
            setStartX(center - (center - startX)*multiple);
            setEndX(center + (endX - center)*multiple);
            scaleTimes = scaleTimes - 1;
        }
        Point[] newPoints = new Point[width];
        try {
            Expression expression = ExpressionFactory.createExpression("f(x)=x");
            float delta = (endX - startX)/(width);

            MathematicalElement X = NumberFactory.createReal(startX);
            for(int i = 0; i < width; i++) {
                Parameters ps = ExpressionFactory.createParameters();
                ps.addParameter("x", X);
                MathematicalElement Y = expression.evaluate(ps);
                newPoints[i] = new Point(((float) X.getRealValue()), ((float) Y.getRealValue()));
                X = NumberFactory.createReal(X.getRealValue() + delta);
            }
        } catch (ParsingException | EvalException e) {
            e.printStackTrace();
        }
        setPoints(newPoints);
        return this;
    }

    public static void main(String[] args) {
        Calculatable c = new UniverseCurve();
        c.setStartX(-5);
        c.setEndX(5);
        Point[] p = c.calculate(10);
        c.setPoints(p);

        for (Point ps : c.getPoints()) {
            System.out.println("( " + ps.getX() + "," + ps.getY() + " )");
        }
        c = c.scale(2,20,true);
        for (Point ps : c.getPoints()) {
            System.out.println("( " + ps.getX() + "," + ps.getY() + " )");
        }
    }
}
