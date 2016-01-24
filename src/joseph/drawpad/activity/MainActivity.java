package joseph.drawpad.activity;

import android.app.Activity;
import android.graphics.*;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import joseph.drawpad.R;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
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
                canvas.save();

                canvas.drawLine(windowHeight/2, 0, windowHeight/2, windowWidth, mPaint);
                canvas.drawLine(windowHeight/2, windowWidth , windowHeight/2 + 5, windowWidth - 5, mPaint);
                canvas.drawLine(windowHeight/2, windowWidth , windowHeight/2 - 5, windowWidth - 5, mPaint);

                canvas.drawLine(0, windowWidth/2, windowHeight, windowWidth/2, mPaint);
                canvas.drawLine(windowHeight, windowWidth/2, windowHeight - 5, windowWidth/2 - 5, mPaint);
                canvas.drawLine(windowHeight, windowWidth/2, windowHeight - 5, windowWidth/2 + 5, mPaint);

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

                canvas.drawLine(windowHeight/2, 0, windowHeight/2, windowWidth, mPaint);
                canvas.drawLine(windowHeight/2, windowWidth , windowHeight/2 + 5, windowWidth - 5, mPaint);
                canvas.drawLine(windowHeight/2, windowWidth , windowHeight/2 - 5, windowWidth - 5, mPaint);

                canvas.drawLine(0, windowWidth/2, windowHeight, windowWidth/2, mPaint);
                canvas.drawLine(windowHeight, windowWidth/2, windowHeight - 5, windowWidth/2 - 5, mPaint);
                canvas.drawLine(windowHeight, windowWidth/2, windowHeight - 5, windowWidth/2 + 5, mPaint);

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

                canvas.drawLine(windowHeight/2, 0, windowHeight/2, windowWidth, mPaint);
                canvas.drawLine(windowHeight/2, windowWidth , windowHeight/2 + 5, windowWidth - 5, mPaint);
                canvas.drawLine(windowHeight/2, windowWidth , windowHeight/2 - 5, windowWidth - 5, mPaint);

                canvas.drawLine(0, windowWidth/2, windowHeight, windowWidth/2, mPaint);
                canvas.drawLine(windowHeight, windowWidth/2, windowHeight - 5, windowWidth/2 - 5, mPaint);
                canvas.drawLine(windowHeight, windowWidth/2, windowHeight - 5, windowWidth/2 + 5, mPaint);

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
                Canvas canvas = sfh.lockCanvas(null);
                canvas.drawColor(Color.BLACK);// 清除画布
                sfh.unlockCanvasAndPost(canvas);

            }
        });
    }
}