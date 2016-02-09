package joseph.drawpad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import joseph.drawpad.model.Curve;
import joseph.drawpad.model.Point;
import joseph.drawpad.utils.CurveType;

/**
 * Created by 熊纪元 on 2016/2/9.
 */
public class DrawView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder holder;
    private CurveType curveType;
    private Curve curve;

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);

        holder = this.getHolder();
        holder.addCallback(this);

    }

    public void setCurveType(CurveType curveType) {
        this.curveType = curveType;
    }

    public void setCurve(Curve curve) {
        this.curve = curve;
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        new DrawingThread(holder, curveType).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        new DrawingThread(holder, curveType).start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public void rePaintCurve(){
        new DrawingThread(holder, curve).start();
    }
}

class DrawingThread extends Thread {
    private SurfaceHolder holder;
    private CurveType curveType;
    private Curve curve;
    private static Paint axisPaint, curvePaint;

    static {
        axisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        axisPaint.setColor(Color.LTGRAY);
        axisPaint.setStrokeWidth(2);

        curvePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        curvePaint.setColor(Color.GREEN);
        curvePaint.setStrokeWidth(2);
    }

    public DrawingThread(SurfaceHolder holder, CurveType curveType) {
        this.holder = holder;
        this.curveType = curveType;
    }

    public DrawingThread(SurfaceHolder holder, Curve curve) {
        this.holder = holder;
        this.curve = curve;
    }

    public void setCurveType(CurveType curveType) {
        this.curveType = curveType;
    }

    public void setCurve(Curve curve) {
        this.curve = curve;
    }
    private void initCanvas(Canvas canvas, int height, int color) {
        canvas.translate(0, height);
        canvas.rotate(-90);
        canvas.drawColor(color);
    }

    private void drawAxis(Canvas canvas, int height, int width, Paint paint) {


        canvas.drawLine(height/2, 0, height/2, width, paint);
        canvas.drawLine(height/2, width , height/2 + 5, width - 5, paint);
        canvas.drawLine(height/2, width , height/2 - 5, width - 5, paint);

        canvas.drawLine(0, width/2, height, width/2, paint);
        canvas.drawLine(height, width/2, height - 5, width/2 - 5, paint);
        canvas.drawLine(height, width/2, height - 5, width/2 + 5, paint);
    }

    private float[] calculateCurve(int length, CurveType curveType, int height, int width) {
        float[] ptsX = new float[length];
        switch (curveType) {
            case Linear:
                for(int y = 0; y < length; y++) {
                    //一次函数
                    ptsX[y] = height/2f - width/2f + y;
                    //二次函数
                    //ptsX[y] = ((y-windowWidth/2.0f)*(y-windowWidth/2.0f))/100.0f + windowHeight/2.0f;
                    //正弦函数
                    //ptsX[y] = ((float) Math.sin(((double) (y-windowWidth/2)/50))) * 100 + windowHeight/2;

                }
                break;
            case Quadratic:
                for(int y = 0; y < ptsX.length; y++) {
                    //一次函数
                    //ptsX[y] = height/2f - width/2f + y;
                    //二次函数
                    ptsX[y] = ((y-width/2f)*(y-width/2f))/100f + height/2f;
                    //正弦函数
                    //ptsX[y] = ((float) Math.sin(((double) (y-windowWidth/2)/50))) * 100 + windowHeight/2;

                }
                break;
            case Sinusoidal:
                for(int y = 0; y < ptsX.length; y++) {
                    //一次函数
                    //ptsX[y] = height/2f - width/2f + y;
                    //二次函数
                    //ptsX[y] = ((y-width/2f)*(y-width/2f))/100f + height/2f;
                    //正弦函数
                    ptsX[y] = ((float) Math.sin(((double) (y-width/2)/50))) * 100f + height/2f;
                }
                break;
            default:
                break;
        }
        return ptsX;
    }

    private void drawCurve(Canvas canvas, float[] ptsX, int height, Paint paint) {
        for(int i = 0; i < ptsX.length-1; i++) {
            if(ptsX[i]<=height)
                canvas.drawLine(ptsX[i], i, ptsX[i+1], i+1, paint);
        }
    }

    private void drawCurve(Canvas canvas, Curve curve, int height, Paint paint) {
        Point[] points = curve.getPoints();
        for(int i = 0; i< points.length-1; i++) {
            if(points[i].getY() <= height)
                canvas.drawLine(points[i].getX(), points[i].getY(), points[i+1].getX(), points[i+1].getY(), paint);
        }
    }

    @Override
    public void run() {
        Canvas canvas = null;

        try {
            synchronized (holder) {
                canvas = holder.lockCanvas();

                int height = holder.getSurfaceFrame().height();
                int width = holder.getSurfaceFrame().width();
                float[] ptsX = new float[width*100];

                initCanvas(canvas, height, Color.BLACK);
                drawAxis(canvas, height, width, axisPaint);

                if(curveType != null && curveType != CurveType.Clear) {
                    ptsX = calculateCurve(ptsX.length, curveType,height,width);
                    drawCurve(canvas, ptsX, height, curvePaint);
                }

                if(curve != null) {
                    drawCurve(canvas, curve, height, curvePaint);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(canvas != null)
                holder.unlockCanvasAndPost(canvas);
        }
    }
}