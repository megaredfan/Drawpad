package joseph.drawpad.model;

import fr.expression4j.basic.MathematicalElement;
import fr.expression4j.core.Expression;
import fr.expression4j.core.Parameters;
import fr.expression4j.core.exception.EvalException;
import fr.expression4j.core.exception.ParsingException;
import fr.expression4j.factory.ExpressionFactory;
import fr.expression4j.factory.NumberFactory;

/**
 * Created by 熊纪元 on 2016/3/1.
 */
public class Curve {
    private Point[] points;
    private String name;
    private float startX, endX;
    private int scaleTimes;
    private String expression;

    public Curve() {
    }

    public Curve(String expression) {
        this.setExpression(expression);
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Point[] getPoints() {
        return this.points;
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

    public int getScaleTimes() {
        return scaleTimes;
    }

    public void setScaleTimes(int scaleTimes) {
        this.scaleTimes = scaleTimes;
    }

    public Point[] calculate(int width) {
        CalculatingThread t = new CalculatingThread(this, width);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return t.getPoints();

    }

    public Curve scale(float multiple, int width, boolean zoomIn) {
        ScalingThread t = new ScalingThread(this, width, multiple, zoomIn);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setPoints(t.getPoints());
        return this;
    }

    public Curve translate(float dx, float dy) {
        for (Point p : getPoints()) {
            p.setX(p.getX() + dx);
            p.setY(p.getY() + dy);
        }
        return this;
    }
}

class CalculatingThread extends Thread {
    private Curve curve;
    private int width;
    private Point[] points;

    public CalculatingThread(Curve curve, int width) {
        this.curve = curve;
        this.width = width;
        this.points = new Point[width];
    }

    public Point[] getPoints() {
        return this.points;
    }

    public void run() {
        Expression expression = null;
        try {
            expression = ExpressionFactory.createExpression(curve.getExpression());
            float delta = (curve.getEndX() - curve.getStartX()) / (width);
            MathematicalElement X = NumberFactory.createReal(curve.getStartX());
            for (int i = 0; i < width; i++) {
                Parameters ps = ExpressionFactory.createParameters();
                ps.addParameter("x", X);
                MathematicalElement Y = expression.evaluate(ps);
                points[i] = new Point(((float) X.getRealValue()), ((float) Y.getRealValue()));
                X = NumberFactory.createReal(X.getRealValue() + delta);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ScalingThread extends Thread {
    private Curve curve;
    private int width;
    private Point[] points;
    private float multiple;
    private boolean zoomIn;

    public ScalingThread(Curve curve, int width, float multiple, boolean zoomIn) {
        this.curve = curve;
        this.width = width;
        this.points = new Point[width];
        this.multiple = multiple;
        this.zoomIn = zoomIn;
    }

    public Point[] getPoints() {
        return this.points;
    }

    public void run() {
        float center = (curve.getStartX() + curve.getEndX()) / 2;
        if (zoomIn) {
            curve.setStartX(center - (center - curve.getStartX()) / multiple);
            curve.setEndX(center + (curve.getEndX() - center) / multiple);
            curve.setScaleTimes(curve.getScaleTimes() + 1);
        } else {
            curve.setStartX(center - (center - curve.getStartX()) * multiple);
            curve.setEndX(center + (curve.getEndX() - center) * multiple);
            curve.setScaleTimes(curve.getScaleTimes() - 1);
        }
        try {
            Expression expression = ExpressionFactory.createExpression(curve.getExpression());
            float delta = (curve.getEndX() - curve.getStartX()) / (width);

            MathematicalElement X = NumberFactory.createReal(curve.getStartX());
            for (int i = 0; i < width; i++) {
                Parameters ps = ExpressionFactory.createParameters();
                ps.addParameter("x", X);
                MathematicalElement Y = expression.evaluate(ps);
                points[i] = new Point(((float) X.getRealValue()), (float) (Y.getRealValue() * Math.pow(multiple, curve.getScaleTimes())));
                X = NumberFactory.createReal(X.getRealValue() + delta);
            }
        } catch (ParsingException | EvalException e) {
            e.printStackTrace();
        }
    }
}