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

/**
 * Created by 熊纪元 on 2016/2/9.
 */
public class DrawView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder holder;
    private Curve curve;

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);

    }

    public Curve getCurve() {
        return curve;
    }

    public void setCurve(Curve curve) {
        this.curve = curve;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        new DrawingThread(holder, curve).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        System.out.println("surfaceChanged");
        new DrawingThread(holder, curve).start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        System.out.println("surfaceDestroyed");
    }

    public void rePaintCurve() {
        new DrawingThread(holder, curve).start();
    }

    public void saveToCanvas(Canvas capturedCanvas) {
        DrawingThread saveThread = new DrawingThread(holder, curve, true);
        saveThread.setCapturedCanvas(capturedCanvas);
        saveThread.start();
    }

}

class DrawingThread extends Thread {
    private static Paint axisPaint, curvePaint, textPaint;
    private SurfaceHolder holder;
    private Curve curve;
    private boolean isCapture;
    private Canvas capturedCanvas;

    public DrawingThread(SurfaceHolder holder, Curve curve) {
        this.holder = holder;
        this.curve = curve;
    }

    public DrawingThread(SurfaceHolder holder, Curve curve, boolean isCapture) {
        this.holder = holder;
        this.curve = curve;
        this.isCapture = isCapture;
    }

    public static Paint getAxisPaint() {
        if (axisPaint == null) {
            axisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            axisPaint.setColor(Color.LTGRAY);
            axisPaint.setStrokeWidth(2);
        }
        return axisPaint;
    }

    public static Paint getCurvePaint() {
        if (curvePaint == null) {
            curvePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            curvePaint.setColor(Color.GREEN);
            curvePaint.setStrokeWidth(2);
        }
        return curvePaint;
    }

    public static Paint getTextPaint() {
        if (textPaint == null) {
            textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(12);
        }
        return textPaint;
    }

    public Canvas getCapturedCanvas() {
        return capturedCanvas;
    }

    public void setCapturedCanvas(Canvas capturedCanvas) {
        this.capturedCanvas = capturedCanvas;
    }

    private void initCanvas(Canvas canvas, int height, int color) {
        canvas.translate(0, height);
        canvas.rotate(-90);
        canvas.drawColor(color);
    }


    private void drawText(Canvas canvas, String text, float x, float y, Paint paint, float angle) {
        canvas.rotate(angle, x, y);
        canvas.drawText(text, x, y, paint);
        canvas.rotate(-angle, x, y);
    }

    private void drawAxis(Canvas canvas, int height, int width, Paint paint) {
        canvas.drawLine(height / 2, 0, height / 2, width, paint);
        canvas.drawLine(height / 2, width, height / 2 + 5, width - 5, paint);
        canvas.drawLine(height / 2, width, height / 2 - 5, width - 5, paint);

        canvas.drawLine(0, width / 2, height, width / 2, paint);
        canvas.drawLine(height, width / 2, height - 5, width / 2 - 5, paint);
        canvas.drawLine(height, width / 2, height - 5, width / 2 + 5, paint);

        if(curve != null){
            for (int i = 0; i < width; i += width / 10) {
                drawText(canvas, i / Math.pow(2, curve.getScaleTimes()) + "", height / 2 - 12, i + width / 2 + 2, getTextPaint(), 90);
                drawText(canvas, -i / Math.pow(2, curve.getScaleTimes()) + "", height / 2 - 12, -i + width / 2 + 2, getTextPaint(), 90);
            }
            for (int i = 0; i < height; i += width / 10) {
                drawText(canvas, i / Math.pow(2, curve.getScaleTimes()) + "", i + height / 2 - 12, width / 2 + 2, getTextPaint(), 90);
                drawText(canvas, -i / Math.pow(2, curve.getScaleTimes()) + "", -i + height / 2 - 12, width / 2 + 2, getTextPaint(), 90);
            }
        }
    }

    private void drawCurve(Canvas canvas, Curve curve, int height, int width, Paint paint) {
        Point[] points = curve.getPoints();
        if (points == null)
            return;
        for (int i = 0; i < points.length - 1; i++) {
            if ((Float.isInfinite(points[i].getY())) || (Float.isNaN(points[i].getY())) || (Float.isNaN(points[i + 1].getY())) || (Float.isNaN(points[i + 1].getY())))
                continue;
            if ((points[i].getY() <= height || points[i + 1].getY() <= height) && points[i].getY() >= 0 || points[i + 1].getY() >= 0)
                canvas.drawLine(points[i].getY(), i, points[i + 1].getY(), i + 1, paint);
        }

    }

    @Override
    public void run() {
        Canvas canvas = null;

        if (!isCapture) {
            try {
                synchronized (holder) {
                    canvas = holder.lockCanvas();

                    int height = holder.getSurfaceFrame().height();
                    int width = holder.getSurfaceFrame().width();

                    initCanvas(canvas, height, Color.BLACK);
                    drawAxis(canvas, height, width, getAxisPaint());

                    if (curve != null) {
                        curve.translate(width / 2, height / 2);
                        drawCurve(canvas, curve, height, width, getCurvePaint());
                        curve.translate(-width / 2, -height / 2);
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

            if (curve != null) {
                curve.translate(width / 2, height / 2);
                drawCurve(canvas, curve, height, width, getCurvePaint());
                curve.translate(-width / 2, -height / 2);
            }

        }
    }
}