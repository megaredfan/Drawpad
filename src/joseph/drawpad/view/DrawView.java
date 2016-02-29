package joseph.drawpad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import joseph.drawpad.model.Point;
import joseph.drawpad.model.Calculatable;

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

    public Calculatable getCalculatable() {
        return calculatable;
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

    public void rePaintCurve() {
        new DrawingThread(holder, calculatable).start();
    }

    public void saveToCanvas(Canvas capturedCanvas) {
        DrawingThread saveThread = new DrawingThread(holder, calculatable, true);
        saveThread.setCapturedCanvas(capturedCanvas);
        saveThread.start();
    }

}

class DrawingThread extends Thread {
    private static Paint axisPaint, curvePaint;
    private SurfaceHolder holder;
    private Calculatable calculatable;
    private boolean isCapture;
    private Canvas capturedCanvas;

    public Canvas getCapturedCanvas() {
        return capturedCanvas;
    }

    public void setCapturedCanvas(Canvas capturedCanvas) {
        this.capturedCanvas = capturedCanvas;
    }

    public static Paint getAxisPaint() {
        if(axisPaint == null){
            axisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            axisPaint.setColor(Color.LTGRAY);
            axisPaint.setStrokeWidth(2);
        }
        return axisPaint;
    }

    public static Paint getCurvePaint() {
        if(curvePaint == null){
            curvePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            curvePaint.setColor(Color.GREEN);
            curvePaint.setStrokeWidth(2);
        }
        return curvePaint;
    }

    public DrawingThread(SurfaceHolder holder, Calculatable calculatable) {
        this.holder = holder;
        this.calculatable = calculatable;
    }

    public DrawingThread(SurfaceHolder holder, Calculatable calculatable, boolean isCapture) {
        this.holder = holder;
        this.calculatable = calculatable;
        this.isCapture = isCapture;
    }

    private void initCanvas(Canvas canvas, int height, int color) {
        canvas.translate(0, height);
        canvas.rotate(-90);
        canvas.drawColor(color);
    }

    private void drawAxis(Canvas canvas, int height, int width, Paint paint) {
        canvas.drawLine(height / 2, 0, height / 2, width, paint);
        canvas.drawLine(height / 2, width, height / 2 + 5, width -  5, paint);
        canvas.drawLine(height / 2, width, height / 2 - 5, width - 5, paint);

        canvas.drawLine(0, width / 2, height, width / 2, paint);
        canvas.drawLine(height, width / 2, height - 5, width / 2 - 5, paint);
        canvas.drawLine(height, width / 2, height - 5, width / 2 + 5, paint);
    }

    private void drawCurve(Canvas canvas, Calculatable calculatable, int height, int width, Paint paint) {
        Point[] points = calculatable.getPoints();
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].getY() <= height || points[i+1].getY() <= height)
                canvas.drawLine(points[i].getY(), i, points[i + 1].getY(), i + 1, paint);
        }

    }

    @Override
    public void run() {
        Canvas canvas = null;

        if(!isCapture) {
            try {
                synchronized (holder) {
                    canvas = holder.lockCanvas();

                    int height = holder.getSurfaceFrame().height();
                    int width = holder.getSurfaceFrame().width();

                    initCanvas(canvas, height, Color.BLACK);
                    drawAxis(canvas, height, width, getAxisPaint());

                    if (calculatable != null) {
                        calculatable.translate(width/2, height/2);
                        drawCurve(canvas, calculatable, height, width, getCurvePaint());
                        calculatable.translate(-width/2, -height/2);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }
        } else {
            canvas = capturedCanvas;

            int height = holder.getSurfaceFrame().height();
            int width = holder.getSurfaceFrame().width();

            initCanvas(canvas, height, Color.BLACK);
            drawAxis(canvas, height, width, getAxisPaint());

            if (calculatable != null) {
                calculatable.translate(width/2, height/2);
                drawCurve(canvas, calculatable, height, width, getCurvePaint());
                calculatable.translate(-width/2, -height/2);
            }

        }
    }
}