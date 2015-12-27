package joseph.drawpad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 熊纪元 on 2015/12/25.
 */
public class ResultActivity extends Activity {
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        listView = (ListView)findViewById(R.id.ListView01);
        Intent intent = getIntent();
        Bundle b = intent.getBundleExtra("data");

        List list = new ArrayList();
        list.add(b.getString("username"));
        list.add(b.getString("password"));
        list.add(b.getString("position"));
        list.add(b.getString("gender"));
        list.add(b.getString("hobby"));
        list.add(b.getString("married"));

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_checked,list);
        listView.setAdapter(adapter);
    }
}
