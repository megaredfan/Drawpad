package joseph.drawpad.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import joseph.drawpad.R;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private Button b;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        b = (Button)findViewById(R.id.button1);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"hello world",Toast.LENGTH_SHORT).show();
            }
        });
    }

}