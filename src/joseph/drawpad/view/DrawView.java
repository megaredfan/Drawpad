package joseph.drawpad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import joseph.drawpad.model.Curve;

/**
 * Created by 熊纪元 on 2016/2/9.
 * <p>
 * 自定义的surfaceview
 */
public class DrawView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder holder;
    private Curve curve;
    private DrawingThread drawingThread;

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
        System.out.println("setting curve with" + curve);
    }

    public DrawingThread getDrawingThread() {
        return drawingThread;
    }

    public void setDrawingThread(DrawingThread drawingThread) {
        this.drawingThread = drawingThread;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawingThread = new DrawingThread(holder, curve);
        drawingThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        drawingThread = new DrawingThread(holder, curve);
        drawingThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        this.curve = null;
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

