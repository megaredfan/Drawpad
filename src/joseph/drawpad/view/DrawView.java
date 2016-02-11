package joseph.drawpad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import joseph.drawpad.model.Point;
import joseph.drawpad.utils.Calculatable;

/**
 * Created by 熊纪元 on 2016/2/9.
 */
public class DrawView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder holder;
    private Calculatable calculatable;

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);

        holder = this.getHolder();
        holder.addCallback(this);

    }

    public void setCalculatable(Calculatable calculatable) {
        this.calculatable = calculatable;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        new DrawingThread(holder, calculatable).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        new DrawingThread(holder, calculatable).start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public void rePaintCurve(){
        new DrawingThread(holder, calculatable).start();
    }

}

class DrawingThread extends Thread {
    private SurfaceHolder holder;
    private Calculatable calculatable;
    private static Paint axisPaint, curvePaint;

    static {
        axisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        axisPaint.setColor(Color.LTGRAY);
        axisPaint.setStrokeWidth(2);

        curvePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        curvePaint.setColor(Color.GREEN);
        curvePaint.setStrokeWidth(2);
    }

    public DrawingThread(SurfaceHolder holder, Calculatable calculatable) {
        this.holder = holder;
        this.calculatable = calculatable;
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

    private void drawCurve(Canvas canvas, Calculatable calculatable, int height, Paint paint) {
        Point[] points = calculatable.getPoints();
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

                initCanvas(canvas, height, Color.GRAY);
                drawAxis(canvas, height, width, axisPaint);

                if(calculatable != null) {
                    drawCurve(canvas, calculatable, height, curvePaint);
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