package joseph.drawpad.model;

import fr.expression4j.basic.MathematicalElement;
import fr.expression4j.core.Expression;
import fr.expression4j.core.Parameters;
import fr.expression4j.core.exception.EvalException;
import fr.expression4j.core.exception.ParsingException;
import fr.expression4j.factory.ExpressionFactory;
import fr.expression4j.factory.NumberFactory;

import java.util.List;

/**
 * Created by 熊纪元 on 2016/3/1.
 */
public class Curve {
    protected Point[] points;
    protected String name;
    protected List<Float> parameters;
    protected float startX, endX;
    protected int scaleTimes;
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

    public List<Float> getParameters() {
        return this.parameters;
    }

    public void setParameters(List<Float> parameters) {
        this.parameters = parameters;
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

    public Point[] calculate(int width) throws ParsingException, EvalException {
        Expression expression = ExpressionFactory.createExpression(this.getExpression());

        Point[] points = new Point[width];

        float delta = (endX - startX) / (width);

        MathematicalElement X = NumberFactory.createReal(startX);
        for (int i = 0; i < width; i++) {
            Parameters ps = ExpressionFactory.createParameters();
            ps.addParameter("x", X);
            MathematicalElement Y = expression.evaluate(ps);
            points[i] = new Point(((float) X.getRealValue()), ((float) Y.getRealValue()));
            X = NumberFactory.createReal(X.getRealValue() + delta);
        }
        return points;

    }

    public Curve scale(float multiple, int width, boolean zoomIn) {
        float center = (startX + endX) / 2;
        if (zoomIn) {
            setStartX(center - (center - startX) / multiple);
            setEndX(center + (endX - center) / multiple);
            scaleTimes = scaleTimes + 1;
        } else {
            setStartX(center - (center - startX) * multiple);
            setEndX(center + (endX - center) * multiple);
            scaleTimes = scaleTimes - 1;
        }
        Point[] newPoints = new Point[width];
        try {
            Expression expression = ExpressionFactory.createExpression(this.getExpression());
            float delta = (endX - startX) / (width);

            MathematicalElement X = NumberFactory.createReal(startX);
            for (int i = 0; i < width; i++) {
                Parameters ps = ExpressionFactory.createParameters();
                ps.addParameter("x", X);
                MathematicalElement Y = expression.evaluate(ps);
                newPoints[i] = new Point(((float) X.getRealValue()), (float) (Y.getRealValue() * Math.pow(multiple, scaleTimes)));
                X = NumberFactory.createReal(X.getRealValue() + delta);
            }
        } catch (ParsingException | EvalException e) {
            e.printStackTrace();
        }
        setPoints(newPoints);
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
