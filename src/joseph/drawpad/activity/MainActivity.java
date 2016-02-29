package joseph.drawpad.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ZoomControls;
import joseph.drawpad.R;
import joseph.drawpad.model.*;
import joseph.drawpad.view.DrawView;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
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
        Button btnCapture, btnShare;
        menuInflater = getMenuInflater();
        drawView = (DrawView) findViewById(R.id.drawView);

        btnCapture = (Button) findViewById(R.id.btnCapture);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US);

                String fname = "/sdcard/" + sdf.format(new Date()) + ".png";

                drawView.setDrawingCacheEnabled(true);
                drawView.buildDrawingCache();

                Bitmap bitmap = drawView.getDrawingCache();
                Canvas capturedCanvas = new Canvas(bitmap);
                drawView.saveToCanvas(capturedCanvas);
                if (bitmap != null) {
                    try {
                        FileOutputStream out = new FileOutputStream(fname);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        Toast.makeText(getApplicationContext(), "截图已保存到" + fname, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(e.getStackTrace());
                        Toast.makeText(getApplicationContext(), "出错了", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "出错了", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnShare = (Button) findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("image/*");
                String title = "标题";
                String extraText = "share";
                share.putExtra(Intent.EXTRA_TEXT, extraText);
                if (title != null) {
                    share.putExtra(Intent.EXTRA_SUBJECT, title);
                }
                startActivity(Intent.createChooser(share, "分享一下"));
            }
        });

        ZoomControls zoomControls = (ZoomControls)findViewById(R.id.zoomControls);
        zoomControls.setIsZoomInEnabled(true);
        zoomControls.setIsZoomOutEnabled(true);

        zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawView != null && drawView.getCalculatable() != null) {
                    Calculatable c = drawView.getCalculatable();
                    //drawView.setCalculatable(c.scale(2f, drawView.getHeight(), drawView.getWidth()));
                    drawView.setCalculatable(c.scale(2f,drawView.getWidth(), true));
                    drawView.rePaintCurve();
                }
            }
        });

        zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawView != null && drawView.getCalculatable() != null) {
                    Calculatable c = drawView.getCalculatable();
                    //drawView.setCalculatable(c.scale(2f, drawView.getHeight(), drawView.getWidth()));
                    drawView.setCalculatable(c.scale(2f,drawView.getWidth(), false));
                    drawView.rePaintCurve();
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
            case R.id.clear:
                drawView.setCalculatable(null);
                drawView.rePaintCurve();
                break;
            case R.id.newLinear:
                curve = new LinearCurve();
                draw(li, curve, drawView);
                return true;
            case R.id.newQuadratic:
                curve = new QuadraticCurve();
                draw(li, curve, drawView);
                break;
            case R.id.newSinusoidal:
                curve = new SinusoidalCurve();
                draw(li, curve, drawView);
                break;
            default:
                return true;
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Toast.makeText(this, "point( " + event.getX() + " , " + event.getY() + " )touched!", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        drawView.rePaintCurve();
    }

    private void draw(LayoutInflater li, Calculatable calculatable, DrawView drawView) {
        if (calculatable == null) {
            drawView.setCalculatable(null);
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
                List<Float> parameters = calculatable.initParameters((LinearLayout) v);

                if (parameters == null) {
                    builder.setView(null);
                    builder.setTitle("错误").setMessage("输入有误！").setPositiveButton("确定", null).create().show();
                    return;
                }
                calculatable.setParameters(parameters);

                int width = drawView.getWidth();
                calculatable.setStartX(-width/2);
                calculatable.setEndX(width/2);
                Point[] points = calculatable.calculate(width);

                calculatable.setPoints(points);
                drawView.setCalculatable(calculatable);
                drawView.rePaintCurve();
            }
        }).setNegativeButton("取消", null);
        builder.create();
        builder.show();
    }
}
