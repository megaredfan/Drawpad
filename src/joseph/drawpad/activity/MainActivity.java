package joseph.drawpad.activity;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import joseph.drawpad.R;

public class MainActivity extends Activity {

    private MenuInflater menuInflater;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        menuInflater = getMenuInflater();

        SurfaceView sfv = (SurfaceView) this.findViewById(R.id.SurfaceView01);
        SurfaceHolder sfh = sfv.getHolder();


        Button btn1 = (Button)findViewById(R.id.Button01);
        Button btn2 = (Button)findViewById(R.id.Button02);
        Button btn3 = (Button)findViewById(R.id.Button03);
        Button btn4 = (Button)findViewById(R.id.Button04);



        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.LTGRAY);
        mPaint.setStrokeWidth(2);

        Paint pPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
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

                /*canvas.rotate(90);
                canvas.translate(0,-windowHeight);
                canvas.drawText("O",windowWidth/2-10,windowHeight/2+10,mPaint);
                canvas.restore();*/

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }


    private void drawAxis(Canvas canvas, int height, int width, Paint mPaint) {
        canvas.translate(0, height);
        canvas.rotate(-90);

        canvas.drawLine(height/2, 0, height/2, width, mPaint);
        canvas.drawLine(height/2, width , height/2 + 5, width - 5, mPaint);
        canvas.drawLine(height/2, width , height/2 - 5, width - 5, mPaint);

        canvas.drawLine(0, width/2, height, width/2, mPaint);
        canvas.drawLine(height, width/2, height - 5, width/2 - 5, mPaint);
        canvas.drawLine(height, width/2, height - 5, width/2 + 5, mPaint);
    }
}