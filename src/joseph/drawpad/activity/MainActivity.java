package joseph.drawpad.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.*;
import joseph.drawpad.R;
import joseph.drawpad.model.Curve;
import joseph.drawpad.model.Point;
import joseph.drawpad.view.DrawView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends Activity {

    private static HashMap<String, Integer> colorsMap;

    static {
        colorsMap = new HashMap<>();
        colorsMap.put("黑色", 0xFF000000);
        colorsMap.put("暗灰色", 0xFF444444);
        colorsMap.put("灰色", 0xFF888888);
        colorsMap.put("亮灰色", 0xFFCCCCCC);
        colorsMap.put("白色", 0xFFFFFFFF);
        colorsMap.put("红色", 0xFFFF0000);
        colorsMap.put("绿色", 0xFF00FF00);
        colorsMap.put("蓝色", 0xFF0000FF);
        colorsMap.put("黄色", 0xFFFFFF00);
        colorsMap.put("青色", 0xFF00FFFF);
        colorsMap.put("紫红色", 0xFFFF00FF);

    }

    private MenuInflater menuInflater;
    private DrawView drawView;
    private String EXP;

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
                        builder.setNegativeButton("取消", null);
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
                intent.putExtra("message", "input");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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
                ProgressDialog progressDialog = null;
                if (drawView != null && drawView.getCurve() != null) {
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("计算绘图中。。。");
                    progressDialog.show();
                    Curve c = drawView.getCurve();
                    drawView.setCurve(c.scale(2f, drawView.getWidth(), false));
                    drawView.rePaintCurve();

                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                }
                zoomControls.setIsZoomOutEnabled(true);
            }
        });

        if (savedInstanceState != null)
            EXP = savedInstanceState.getString("expression");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear:
                drawView.setCurve(null);
                drawView.rePaintCurve();
                break;
            case R.id.exit:
                //System.exit(0);
                finish();
                break;
            case R.id.changeCurveColor:
                drawView.getDrawingThread().changeCurveColor(Color.RED);
                ArrayAdapter<CharSequence> colors = ArrayAdapter.createFromResource(this, R.array.colors, android.R.layout.simple_spinner_dropdown_item);
                LayoutInflater layoutInflater = getLayoutInflater();
                LinearLayout ll = (LinearLayout) layoutInflater.inflate(R.layout.colorpicker, null);
                Spinner colorSpinner = ((Spinner) ll.findViewById(R.id.colorSpinner));
                colorSpinner.setPrompt("请选择颜色");
                colorSpinner.setAdapter(colors);
                colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int chosedColor = colorsMap.get(((TextView) view).getText().toString());
                        drawView.getDrawingThread().changeCurveColor(chosedColor);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("选择颜色");
                builder.setView(ll);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        drawView.rePaintCurve();
                    }
                });
                builder.show();

                break;
            default:
                return true;
        }
        return true;
    }

    public void onResume() {
        super.onResume();
        ProgressDialog progressDialog = null;

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        Intent intent = this.getIntent();
        if (intent != null && intent.getStringExtra("expression") != null) {
            String expression = intent.getStringExtra("expression");
            EXP = expression;

            Curve curve = new Curve(expression);

            curve.setStartX(-width / 2);
            curve.setEndX(width / 2);
            Point[] points = new Point[0];
            try {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("计算绘图中。。。");
                progressDialog.show();
                points = curve.calculate(width);
            } catch (Exception e) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("输入有误！");
                builder.show();
            }
            curve.setPoints(points);
            drawView.setCurve(curve);

            setResult(RESULT_OK);
        }
        if (EXP != null) {
            String expression = EXP;
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
        }
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
        return;
    }


    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("expression", EXP);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        EXP = savedInstanceState.getString("expression");
    }

    private void shareCapturedCurve(String fileName) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("image/png");

        share.putExtra(Intent.EXTRA_SUBJECT, "分享");
        share.putExtra(Intent.EXTRA_TEXT, "看我画的漂亮曲线~");
        File pic = new File(fileName);
        if (!pic.exists()) {
            return;
        }
        Uri uri = Uri.fromFile(pic);
        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "分享一下"));
    }
}
