package joseph.drawpad;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.*;

import java.util.Calendar;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    private Button b1,b2;
    private TextView tv1,tv2;
    private Calendar c;
    private int m_year,m_month,m_day;
    private int m_hour,m_minute;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final Context context = MainActivity.this;
        b1 = (Button)findViewById(R.id.Button01);
        b2 = (Button)findViewById(R.id.Button02);
        c = Calendar.getInstance();
        m_year = c.get(Calendar.YEAR);
        m_month = c.get(Calendar.MONTH);
        m_day = c.get(Calendar.DAY_OF_MONTH);
        m_hour = c.get(Calendar.HOUR);
        m_minute = c.get(Calendar.MINUTE);

        tv1 = (TextView)findViewById(R.id.textView01);
        tv2 = (TextView)findViewById(R.id.textView02);
        tv1.setText(m_year+"-"+(m_month+1)+"-"+m_day);
        tv2.setText(m_hour+":"+m_minute);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        m_year = year;
                        m_month = monthOfYear;
                        m_day = dayOfMonth;
                        tv1.setText(m_year+"-"+(m_month+1)+"-"+m_day);
                    }
                }, m_year, m_month, m_day).show();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        m_hour = hourOfDay;
                        m_minute = minute;
                        tv2.setText(m_hour+":"+m_minute);
                    }
                },m_hour,m_minute,false).show();
            }
        });


    }
}
