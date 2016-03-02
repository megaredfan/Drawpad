package joseph.drawpad.activity;

import android.app.Activity;
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
                Intent intent = new Intent();
                intent.setClass(InputActivity.this, MainActivity.class);
                intent.putExtra("expression", formula.getText().toString().trim());
                startActivity(intent);
                InputActivity.this.finish();
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