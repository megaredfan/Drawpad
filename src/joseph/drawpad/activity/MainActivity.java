package joseph.drawpad.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import joseph.drawpad.R;
import joseph.drawpad.model.Curve;
import joseph.drawpad.model.Point;
import joseph.drawpad.utils.CurveType;
import joseph.drawpad.view.DrawView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private MenuInflater menuInflater;
    private DrawView drawView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button btn1,btn2,btn3,btn4;
        menuInflater = getMenuInflater();
        drawView = (DrawView)findViewById(R.id.drawView);

        btn1 = (Button)findViewById(R.id.Button01);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Curve curve = new Curve();
                curve.setType(CurveType.Linear);
                curve.setPoints(new Point[drawView.getWidth()]);

                Point[] points = curve.calculate(drawView.getHeight(), drawView.getWidth());

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
                curve.setType(CurveType.Quadratic);
                curve.setPoints(new Point[drawView.getWidth()]);

                Point[] points = curve.calculate(drawView.getHeight(), drawView.getWidth());

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
                curve.setType(CurveType.Sinusoidal);
                curve.setPoints(new Point[drawView.getWidth()]);

                Point[] points = curve.calculate(drawView.getHeight(), drawView.getWidth());

                curve.setPoints(points);

                drawView.setCurve(curve);
                drawView.rePaintCurve();
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
        Curve curve = new Curve();
        LayoutInflater li = LayoutInflater.from(this);

        switch (item.getItemId()) {
            case R.id.clear :
                drawView.setCurve(null);
                drawView.rePaintCurve();
                break;
            case R.id.newLinear :
                //curve = initCurve(CurveType.Linear);
                curve.setType(CurveType.Linear);
                break;
            case R.id.newQuadratic :
                //curve = initCurve(CurveType.Quadratic);
                curve.setType(CurveType.Quadratic);
                break;
            case R.id.newSinusoidal :
                curve = initCurve(CurveType.Sinusoidal);
                break;
            case R.id.newItem :
                //draw(v, li, CurveType.Linear, drawView);
                curve.setType(CurveType.Quadratic);
                break;
            default:
                return true;
        }
        draw(li, curve, drawView);
        return true;
    }

    private Curve initCurve(CurveType type) {
        Curve curve = new Curve();
        curve.setPoints(new Point[drawView.getWidth()]);

        curve.setType(type);
        Point[] points = curve.calculate(drawView.getHeight(), drawView.getWidth());

        curve.setPoints(points);

        return curve;
    }

    private void draw(LayoutInflater li, Curve curve, DrawView drawView) {
        List<EditText> editTexts = new ArrayList<>();
        if(curve.getType() == null) {
            drawView.setCurve(null);
            drawView.rePaintCurve();
            return;
        }
        View v = null;
        switch (curve.getType()) {
            case Linear:
                v = li.inflate(R.layout.editlinear, null);
                editTexts.add((EditText)v.findViewById(R.id.editText));
                editTexts.add((EditText)v.findViewById(R.id.editText2));
                break;
            case Quadratic:
                v = li.inflate(R.layout.editquadratic, null);
                editTexts.add((EditText)v.findViewById(R.id.editText));
                editTexts.add((EditText)v.findViewById(R.id.editText2));
                editTexts.add((EditText)v.findViewById(R.id.editText3));
                break;
            case Sinusoidal:
                break;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("输入参数");
        builder.setView(v);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                curve.setPoints(new Point[drawView.getWidth()]);
                ArrayList<Float> parameters = new ArrayList<>(curve.getType().getParameterCount());

                try {
                    switch (curve.getType()) {
                        case Linear:
                            parameters.add(Float.valueOf(editTexts.get(0).getText().toString()));
                            parameters.add(Float.valueOf(editTexts.get(1).getText().toString()));
                            break;
                        case Quadratic:
                            parameters.add(Float.valueOf(editTexts.get(0).getText().toString()));
                            parameters.add(Float.valueOf(editTexts.get(1).getText().toString()));
                            parameters.add(Float.valueOf(editTexts.get(2).getText().toString()));
                            break;
                        default:
                            break;
                    }
                } catch(Exception e) {
                    builder.setView(null);
                    builder.setTitle("错误").setMessage("输入有误！").setPositiveButton("确定", null).create().show();
                    return;
                }
                curve.setParameters(parameters);

                Point[] points = curve.calculate(drawView.getHeight(), drawView.getWidth());

                curve.setPoints(points);

                drawView.setCurve(curve);
                drawView.rePaintCurve();
            }
        }).setNegativeButton("取消", null);
        builder.create();
        builder.show();
    }
}