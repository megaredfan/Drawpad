package joseph.drawpad.activity;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
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

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        Button btn1 = (Button)findViewById(R.id.Button01);
        Button btn2 = (Button)findViewById(R.id.Button02);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn2.callOnClick();
                int x = dm.widthPixels;
                int y = dm.heightPixels;
                Canvas canvas = sfh.lockCanvas(new Rect(0, 0, x, y ));

                Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                mPaint.setColor(Color.WHITE);
                mPaint.setStrokeWidth(1);

                canvas.drawLine(0, y/2, x, y/2,mPaint);
                canvas.drawLine(x/2, 0, x/2, y,mPaint);
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