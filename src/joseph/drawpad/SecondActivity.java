package joseph.drawpad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by 熊纪元 on 2015/12/25.
 */
public class SecondActivity extends Activity {
    private Button b;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);
        TextView textView = (TextView)findViewById(R.id.textView);
        Intent intent = getIntent();
        String action = intent.getAction();
        textView.setText(action);
    }
}
