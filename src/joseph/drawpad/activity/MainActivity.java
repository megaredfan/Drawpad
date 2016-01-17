package joseph.drawpad.activity;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn2.callOnClick();
                int windowWidth = sfv.getWidth();
                int windowHeight = sfv.getHeight();

                TextView text1,text2;
                text1 = (TextView)findViewById(R.id.textView3);
                text2 = (TextView)findViewById(R.id.textView4);
                text1.setText(String.valueOf(windowWidth));
                text2.setText(String.valueOf(windowHeight));

                Canvas canvas = sfh.lockCanvas(new Rect(0, 0, windowWidth, windowHeight ));

                Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                mPaint.setColor(Color.WHITE);
                mPaint.setStrokeWidth(1);

                Paint pPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                pPaint.setColor(Color.GREEN);
                pPaint.setStrokeWidth(1);

                canvas.translate(0, windowHeight);
                canvas.rotate(-90);

                canvas.drawLine(windowHeight/2, 0, windowHeight/2, windowWidth, mPaint);
                canvas.drawLine(windowHeight/2, windowWidth , windowHeight/2 + 5, windowWidth - 5, mPaint);
                canvas.drawLine(windowHeight/2, windowWidth , windowHeight/2 - 5, windowWidth - 5, mPaint);

                canvas.drawLine(0, windowWidth/2, windowHeight, windowWidth/2, mPaint);
                canvas.drawLine(windowHeight, windowWidth/2, windowHeight - 5, windowWidth/2 - 5, mPaint);
                canvas.drawLine(windowHeight, windowWidth/2, windowHeight - 5, windowWidth/2 + 5, mPaint);

                float[] ptsX = new float[windowWidth];

                for(int y = 0; y < ptsX.length; y++) {
                    //一次函数
                    ptsX[y] = windowHeight/2 - windowWidth/2 + y;
                    //二次函数
                    //ptsX[y] = ((y-windowWidth/2.0f)*(y-windowWidth/2.0f))/100.0f + windowHeight/2.0f;

                }

                for(int i = 0; i < ptsX.length-1; i++) {
                    if(ptsX[i]<=windowHeight)
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