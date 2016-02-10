package joseph.drawpad.utils;

/**
 * Created by 熊纪元 on 2016/2/9.
 */
public enum CurveType {
    Linear(2),
    Quadratic(2),
    Sinusoidal(3);

    int parameterCount;

    private CurveType(int parameterCount) {
        setParameterCount(parameterCount);
    }

    public int getParameterCount() {
        return parameterCount;
    }

    public void setParameterCount(int parameterCount) {
        this.parameterCount = parameterCount;
    }
}
