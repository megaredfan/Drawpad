package joseph.drawpad.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ZoomControls;
import joseph.drawpad.R;
import joseph.drawpad.model.Curve;
import joseph.drawpad.model.Point;
import joseph.drawpad.view.DrawView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {

    private MenuInflater menuInflater;
    private DrawView drawView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button btnCapture, btnInput;
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("截图成功");
                        builder.setMessage("分享给其他人吗？");
                        builder.setPositiveButton("分享", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                shareCapturedCurve(fname);
                            }
                        });
                        builder.setNegativeButton("取消",null);
                        builder.show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "截图出错了", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "截图出错了", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnInput = (Button) findViewById(R.id.btnInput);
        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("joseph.drawpad.activity.InputActivity");
                startActivity(intent);
                MainActivity.this.finish();
            }
        });

        ZoomControls zoomControls = (ZoomControls) findViewById(R.id.zoomControls);
        zoomControls.setIsZoomInEnabled(true);
        zoomControls.setIsZoomOutEnabled(true);

        zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomControls.setIsZoomInEnabled(false);
                if (drawView != null && drawView.getCurve() != null) {
                    Curve c = drawView.getCurve();
                    drawView.setCurve(c.scale(2f, drawView.getWidth(), true));
                    drawView.rePaintCurve();
                }
                zoomControls.setIsZoomInEnabled(true);
            }
        });

        zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomControls.setIsZoomOutEnabled(false);
                if (drawView != null && drawView.getCurve() != null) {
                    Curve c = drawView.getCurve();
                    drawView.setCurve(c.scale(2f, drawView.getWidth(), false));
                    drawView.rePaintCurve();
                }
                zoomControls.setIsZoomOutEnabled(true);
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
        Curve curve = null;
        LayoutInflater li = LayoutInflater.from(this);

        switch (item.getItemId()) {
            case R.id.clear:
                drawView.setCurve(null);
                drawView.rePaintCurve();
                break;
            case R.id.exit:
                this.finish();
                break;
            default:
                return true;
        }
        return true;
    }

    public void onResume() {
        super.onResume();
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）

        Intent intent = this.getIntent();
        if (intent != null && intent.getStringExtra("expression") != null) {
            String expression = intent.getStringExtra("expression");
            Curve curve = new Curve(expression);

            curve.setStartX(-width / 2);
            curve.setEndX(width / 2);
            Point[] points = new Point[0];
            try {
                points = curve.calculate(width);
            } catch (Exception e) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("输入有误！");
                builder.show();
            }
            curve.setPoints(points);
            drawView.setCurve(curve);

            return;
        }
    }

    private void shareCapturedCurve(String fileName) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("image/png");

        share.putExtra(Intent.EXTRA_SUBJECT, "分享");
        share.putExtra(Intent.EXTRA_TEXT, "看我画的漂亮曲线~");
        File pic = new File(fileName);
        if(!pic.exists()){
            return;
        }
        Uri uri = Uri.fromFile(pic);
        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "分享一下"));
    }
}
