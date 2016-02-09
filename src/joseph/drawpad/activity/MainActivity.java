package joseph.drawpad.activity;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import joseph.drawpad.R;
import joseph.drawpad.model.Curve;
import joseph.drawpad.model.Point;
import joseph.drawpad.utils.CurveType;
import joseph.drawpad.view.DrawView;

public class MainActivity extends Activity {

    private MenuInflater menuInflater;
    private static Paint mPaint,pPaint;
    private Button btn1,btn2,btn3,btn4;
    private DrawView drawView;

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        menuInflater = getMenuInflater();

        sfv = (SurfaceView) this.findViewById(R.id.SurfaceView01);
        sfh = sfv.getHolder();


        btn1 = (Button)findViewById(R.id.Button01);
        btn2 = (Button)findViewById(R.id.Button02);
        btn3 = (Button)findViewById(R.id.Button03);
        btn4 = (Button)findViewById(R.id.Button04);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.LTGRAY);
        mPaint.setStrokeWidth(2);

        pPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pPaint.setColor(Color.GREEN);
        pPaint.setStrokeWidth(2);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn2.callOnClick();
                int windowWidth = sfv.getWidth();
                int windowHeight = sfv.getHeight();

                Canvas canvas = sfh.lockCanvas(new Rect(0, 0, windowWidth, windowHeight ));

                canvas.translate(0, windowHeight);
                canvas.rotate(-90);

                //drawAxis(canvas,windowHeight,windowWidth,mPaint);

                *//*canvas.rotate(90);
                canvas.translate(0,-windowHeight);
                canvas.drawText("O",windowWidth/2-10,windowHeight/2+10,mPaint);
                canvas.restore();*//*

                float[] ptsX = new float[windowWidth*100];

                for(int y = 0; y < ptsX.length; y++) {
                    //一次函数
                    ptsX[y] = windowHeight/2 - windowWidth/2 + y;
                    //二次函数
                    //ptsX[y] = ((y-windowWidth/2.0f)*(y-windowWidth/2.0f))/100.0f + windowHeight/2.0f;
                    //正弦函数
                    //ptsX[y] = ((float) Math.sin(((double) (y-windowWidth/2)/50))) * 100 + windowHeight/2;

                }

                for(int i = 0; i < ptsX.length-1; i++) {
                    if(ptsX[i]<=windowHeight)
                        canvas.drawLine(ptsX[i], i, ptsX[i+1], i+1, pPaint);
                }
                sfh.unlockCanvasAndPost(canvas);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn2.callOnClick();
                int windowWidth = sfv.getWidth();
                int windowHeight = sfv.getHeight();

                Canvas canvas = sfh.lockCanvas(new Rect(0, 0, windowWidth, windowHeight ));

                canvas.translate(0, windowHeight);
                canvas.rotate(-90);

                //drawAxis(canvas,windowHeight,windowWidth,mPaint);

                float[] ptsX = new float[windowWidth*100];

                for(int y = 0; y < ptsX.length; y++) {
                    //一次函数
                    //ptsX[y] = windowHeight/2 - windowWidth/2 + y;
                    //二次函数
                    ptsX[y] = ((y-windowWidth/2.0f)*(y-windowWidth/2.0f))/100.0f + windowHeight/2.0f;
                    //正弦函数
                    //ptsX[y] = ((float) Math.sin(((double) (y-windowWidth/2)/50))) * 100 + windowHeight/2;

                }

                for(int i = 0; i < ptsX.length-1; i++) {
                    if(ptsX[i]<=windowHeight)
                        canvas.drawLine(ptsX[i], i, ptsX[i+1], i+1, pPaint);
                }
                sfh.unlockCanvasAndPost(canvas);
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn2.callOnClick();
                int windowWidth = sfv.getWidth();
                int windowHeight = sfv.getHeight();

                Canvas canvas = sfh.lockCanvas(new Rect(0, 0, windowWidth, windowHeight ));

                canvas.translate(0, windowHeight);
                canvas.rotate(-90);

                //drawAxis(canvas,windowHeight,windowWidth,mPaint);

                float[] ptsX = new float[windowWidth*100];

                for(int y = 0; y < ptsX.length; y++) {
                    //一次函数
                    //ptsX[y] = windowHeight/2 - windowWidth/2 + y;
                    //二次函数
                    //
                    //ptsX[y] = ((y-windowWidth/2.0f)*(y-windowWidth/2.0f))/100.0f + windowHeight/2.0f;
                    //正弦函数
                    ptsX[y] = ((float) Math.sin(((double) (y-windowWidth/2)/50))) * 100 + windowHeight/2;

                }

                for(int i = 0; i < ptsX.length-1; i++) {
                    canvas.drawLine(ptsX[i], i, ptsX[i+1], i+1, pPaint);
                }
                sfh.unlockCanvasAndPost(canvas);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int windowWidth = sfv.getWidth();
                int windowHeight = sfv.getHeight();

                Canvas canvas = sfh.lockCanvas(new Rect(0, 0, windowWidth, windowHeight ));

                canvas.drawColor(Color.BLACK);// 清除画布
                drawAxis(canvas, windowHeight, windowWidth, mPaint);

                sfh.unlockCanvasAndPost(canvas);
            }
        });
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        drawView = (DrawView)findViewById(R.id.drawView);

        btn1 = (Button)findViewById(R.id.Button01);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Curve curve = new Curve();
                curve.setWidth(drawView.getWidth());
                curve.setType(CurveType.Linear);
                curve.setPoints(new Point[drawView.getWidth()]);

                Point[] points = curve.calculateCurve(drawView.getHeight());

                curve.setPoints(points);

                drawView.setCurve(curve);
                drawView.rePaintCurve();
            }
        });

        btn2 = (Button)findViewById(R.id.Button02);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setCurve(null);
                drawView.rePaintCurve();
            }
        });

        btn3 = (Button)findViewById(R.id.Button03);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Curve curve = new Curve();
                curve.setWidth(drawView.getWidth());
                curve.setType(CurveType.Quadratic);
                curve.setPoints(new Point[drawView.getWidth()]);

                Point[] points = curve.calculateCurve(drawView.getHeight());

                curve.setPoints(points);

                drawView.setCurve(curve);
                drawView.rePaintCurve();
            }
        });

        btn4 = (Button)findViewById(R.id.Button04);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Curve curve = new Curve();
                curve.setWidth(drawView.getWidth());
                curve.setType(CurveType.Sinusoidal);
                curve.setPoints(new Point[drawView.getWidth()]);

                Point[] points = curve.calculateCurve(drawView.getHeight());

                curve.setPoints(points);

                drawView.setCurve(curve);
                drawView.rePaintCurve();
            }
        });
    }

    /*@Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }*/

}