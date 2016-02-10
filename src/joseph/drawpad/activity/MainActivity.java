package joseph.drawpad.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import joseph.drawpad.R;
import joseph.drawpad.model.LinearCurve;
import joseph.drawpad.model.Point;
import joseph.drawpad.model.QuadraticCurve;
import joseph.drawpad.model.SinusoidalCurve;
import joseph.drawpad.utils.Calculatable;
import joseph.drawpad.view.DrawView;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {

    private MenuInflater menuInflater;
    private DrawView drawView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button btn1,btn2,btn3,btn4,btn5;
        menuInflater = getMenuInflater();
        drawView = (DrawView)findViewById(R.id.drawView);


        btn5 = (Button)findViewById(R.id.Button05);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US);

                String fname = "/sdcard/"+ sdf.format(new Date()) + ".png";

                View view = v.getRootView();

                view.setDrawingCacheEnabled(true);
                view.buildDrawingCache();

                Bitmap bitmap = view.getDrawingCache();
                if(bitmap != null)
                {
                    try{
                        FileOutputStream out = new FileOutputStream(fname);
                        bitmap.compress(Bitmap.CompressFormat.PNG,100,out);
                        Toast.makeText(getApplicationContext(),"截图已保存到" + fname,Toast.LENGTH_LONG).show();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(e.getStackTrace());
                        Toast.makeText(getApplicationContext(),"出错了",Toast.LENGTH_LONG).show();
                    }

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"出错了",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Calculatable curve = null;
        LayoutInflater li = LayoutInflater.from(this);

        switch (item.getItemId()) {
            case R.id.clear :
                drawView.setCurve(null);
                drawView.rePaintCurve();
                break;
            case R.id.newLinear :
                //curve = initCurve(CurveType.Linear);
                curve = new LinearCurve();
                draw(li, curve, drawView);
                return true;

            case R.id.newQuadratic :
                //curve = initCurve(CurveType.Quadratic);
                curve = new QuadraticCurve();
                draw(li, curve, drawView);
                break;
            case R.id.newSinusoidal :
                //curve = initCurve(CurveType.Sinusoidal);
                curve = new SinusoidalCurve();
                draw(li, curve, drawView);
                break;
            /*case R.id.newItem :
                //draw(v, li, CurveType.Linear, drawView);
                curve.setType(CurveType.Quadratic);
                break;*/
            default:
                return true;
        }
        return true;
    }

    private void draw(LayoutInflater li, Calculatable calculatable, DrawView drawView) {
        List<EditText> editTexts = new ArrayList<>();
        if(calculatable == null) {
            drawView.setCurve(null);
            drawView.rePaintCurve();
            return;
        }
        View v = calculatable.getView(li);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("输入参数");
        builder.setView(v);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                calculatable.setPoints(new Point[drawView.getWidth()]);
                List<Float> parameters = null;
                parameters = calculatable.initParameters((LinearLayout)v);

                if(parameters == null){
                    builder.setView(null);
                    builder.setTitle("错误").setMessage("输入有误！").setPositiveButton("确定", null).create().show();
                    return;
                }
                calculatable.setParameters(parameters);

                Point[] points = calculatable.calculate(drawView.getHeight(), drawView.getWidth());

                calculatable.setPoints(points);

                drawView.setCurve(calculatable);
                drawView.rePaintCurve();
            }
        }).setNegativeButton("取消", null);
        builder.create();
        builder.show();
    }
}
