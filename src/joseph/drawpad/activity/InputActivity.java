package joseph.drawpad.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import joseph.drawpad.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by 熊纪元 on 2016/3/2.
 */
public class InputActivity extends Activity {
    private static TextView formula;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input);

        formula = (TextView) findViewById(R.id.input);
        ArrayList<Button> buttons = new ArrayList<>();
        Button[] buttonArray = {
                ((Button) findViewById(R.id.zero)),
                ((Button) findViewById(R.id.one)),
                ((Button) findViewById(R.id.two)),
                ((Button) findViewById(R.id.three)),
                ((Button) findViewById(R.id.four)),
                ((Button) findViewById(R.id.five)),
                ((Button) findViewById(R.id.six)),
                ((Button) findViewById(R.id.seven)),
                ((Button) findViewById(R.id.eight)),
                ((Button) findViewById(R.id.nine)),
                ((Button) findViewById(R.id.add)),
                ((Button) findViewById(R.id.sub)),
                ((Button) findViewById(R.id.mul)),
                ((Button) findViewById(R.id.divide)),
                ((Button) findViewById(R.id.sin)),
                ((Button) findViewById(R.id.cos)),
                ((Button) findViewById(R.id.tan)),
                ((Button) findViewById(R.id.dot)),
                ((Button) findViewById(R.id.sqrt)),
                ((Button) findViewById(R.id.x)),
                ((Button) findViewById(R.id.log)),
                ((Button) findViewById(R.id.ln)),
                ((Button) findViewById(R.id.square)),
                ((Button) findViewById(R.id.left)),
                ((Button) findViewById(R.id.right)),
                ((Button) findViewById(R.id.e))

        };
        buttons = new ArrayList(Arrays.asList(buttonArray));
        setButtonInput(buttons);

        Button btnClear = (Button) findViewById(R.id.c);
        Button btnBksp = (Button) findViewById(R.id.bksp);
        Button btnDone = (Button) findViewById(R.id.done);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formula.setText("f(x)=");
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!formula.getText().toString().equals("f(x)=")){
                    progressDialog = new ProgressDialog(InputActivity.this);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("绘图中。。。");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    Thread t = new Thread(){
                        public void run(){
                            Intent intent = new Intent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.setClass(InputActivity.this, MainActivity.class);
                            String expression = formula.getText().toString().trim();

                            expression = expression.replaceAll("log","1/log(10)*log").replaceAll("ln","log");
                            intent.putExtra("expression", expression);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            //startActivity(intent);
                            startActivityForResult(intent,0);

                        }
                    };
                    t.start();

                    try {
                        t.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btnBksp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = formula.getText().toString();
                if (!text.equals("f(x)="))
                    formula.setText(formula.getText().subSequence(0, formula.getText().length() - 1));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        finish();
    }

    private void setButtonInput(ArrayList<Button> buttonList) {
        for (Button b :
                buttonList) {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    formula.setText(formula.getText().toString() + b.getText());
                }
            });
        }
    }
}