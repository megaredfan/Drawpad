package joseph.drawpad.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import joseph.drawpad.model.Curve;
import joseph.drawpad.model.Point;

/**
 * Created by 熊纪元 on 2016/3/3.
 *
 * 用于绘制函数图像，在后台线程中绘图以提高相应速度
 */
public class DrawingThread extends Thread {
    private static Paint axisPaint, curvePaint, textPaint;
    private final ThreadLocal<SurfaceHolder> holder;
    private Curve curve;
    private boolean isCapture;
    private Canvas capturedCanvas;

    public DrawingThread(SurfaceHolder holder, Curve curve) {
        this.holder = new ThreadLocal<SurfaceHolder>() {
            @Override
            protected SurfaceHolder initialValue() {
                return holder;
            }
        };
        this.curve = curve;
    }

    public DrawingThread(SurfaceHolder holder, Curve curve, boolean isCapture) {
        this.holder = new ThreadLocal<SurfaceHolder>() {
            @Override
            protected SurfaceHolder initialValue() {
                return holder;
            }
        };
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

    public void changeCurveColor(int color) {
        getCurvePaint().setColor(color);
    }

    public static Paint getTextPaint(int textSize) {
        if (textPaint == null) {
            textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(textSize);
        }
        return textPaint;
    }

    public void setCapturedCanvas(Canvas capturedCanvas) {
        this.capturedCanvas = capturedCanvas;
    }

    /**
     * 调整画布，对坐标轴进行调整
     *
     * @param canvas 画布
     * @param height 因为要将原点从顶部移动到地步，所以需要画布的高度
     * @param color 背景颜色
     */
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

    /**
     * 绘制坐标轴和刻度标识
     *
     * @param canvas 画布
     * @param height 画布高度
     * @param width 画布宽度
     * @param paint 画笔
     */
    private void drawAxis(Canvas canvas, int height, int width, Paint paint) {
        canvas.drawLine(height / 2, 0, height / 2, width, paint);
        canvas.drawLine(height / 2, width, height / 2 + 5, width - 5, paint);
        canvas.drawLine(height / 2, width, height / 2 - 5, width - 5, paint);

        canvas.drawLine(0, width / 2, height, width / 2, paint);
        canvas.drawLine(height, width / 2, height - 5, width / 2 - 5, paint);
        canvas.drawLine(height, width / 2, height - 5, width / 2 + 5, paint);

        if(curve != null){
            int TEXT_SIZE = Math.round(width/40);
            for (int i = 0; i < width; i += width / 10) {
                drawText(canvas, i / Math.pow(2, curve.getScaleTimes()) + "", height / 2 - TEXT_SIZE, i + width / 2 + 2, getTextPaint(TEXT_SIZE), 90);
                drawText(canvas, -i / Math.pow(2, curve.getScaleTimes()) + "", height / 2 - TEXT_SIZE, -i + width / 2 + 2, getTextPaint(TEXT_SIZE), 90);
            }
            for (int i = 0; i < height; i += width / 10) {
                drawText(canvas, i / Math.pow(2, curve.getScaleTimes()) + "", i + height / 2 - TEXT_SIZE, width / 2 + 2, getTextPaint(TEXT_SIZE), 90);
                drawText(canvas, -i / Math.pow(2, curve.getScaleTimes()) + "", -i + height / 2 - TEXT_SIZE, width / 2 + 2, getTextPaint(TEXT_SIZE), 90);
            }
        }
    }

    private void drawCurve(Canvas canvas, Curve curve, int height, Paint paint) {
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
                synchronized (holder.get()) {
                    canvas = holder.get().lockCanvas();

                    int height = holder.get().getSurfaceFrame().height();
                    int width = holder.get().getSurfaceFrame().width();

                    initCanvas(canvas, height, Color.BLACK);
                    drawAxis(canvas, height, width, getAxisPaint());

                    if (curve != null) {
                        curve.translate(width / 2, height / 2);
                        drawCurve(canvas, curve, height, getCurvePaint());
                        curve.translate(-width / 2, -height / 2);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null)
                    holder.get().unlockCanvasAndPost(canvas);
            }
        } else {
            canvas = capturedCanvas;

            int height = holder.get().getSurfaceFrame().height();
            int width = holder.get().getSurfaceFrame().width();

            initCanvas(canvas, height, Color.BLACK);
            drawAxis(canvas, height, width, getAxisPaint());

            if (curve != null) {
                curve.translate(width / 2, height / 2);
                drawCurve(canvas, curve, height, getCurvePaint());
                curve.translate(-width / 2, -height / 2);
            }

        }
    }
}
