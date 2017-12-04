package ee382apt.thread;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity implements
        View.OnClickListener{

    private static String email = null;
    private static String time = null;
    private Calendar today = Calendar.getInstance();
    DatePicker mDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        email = getIntent().getStringExtra("email");
        findViewById(R.id.switchView).setOnClickListener(this);
        findViewById(R.id.backButton).setOnClickListener(this);
        findViewById(R.id.AddEvent).setOnClickListener(this);

        mDatePicker = (DatePicker)findViewById(R.id.datePicker);
        String month = "";
        int m = today.get(Calendar.MONTH) + 1;
        if(m < 10){
            month = "0" + m;
        }else{
            month = Integer.toString(m);
        }

        String day = "";
        int d = today.get(Calendar.DAY_OF_MONTH);
        if(d < 10){
            day = "0" + d;
        }else{
            day = Integer.toString(d);
        }

        time = today.get(Calendar.YEAR) + "" + month + day;

        mDatePicker.init(today.get(Calendar.YEAR), m, d,new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                String m = "";
                if(month < 10){
                    m = "0" + month;
                }else{
                    m = Integer.toString(month);
                }

                String day = "";
                if(dayOfMonth < 10){
                    day = "0" + dayOfMonth;
                }else{
                    day = Integer.toString(dayOfMonth);
                }

                time = Integer.toString(year) + "" + m + day;
                listEvents();
            }
        });
    }

    public void listEvents(){
        Intent intent = new Intent(this, ListEventActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("time", time);
        startActivity(intent);
    }

    public void onClick(View view){
        Intent intent = new Intent(this, PrepareTimeLineActivity.class);
        switch (view.getId()){
            case R.id.switchView:
                intent.putExtra("email", email);
                startActivity(intent);
                break;
            case R.id.backButton:
                finish();
                break;
            case R.id.AddEvent:
                intent = new Intent(this, EditEventActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("title", "");
                intent.putExtra("time", "");
                intent.putExtra("location", "");
                intent.putExtra("timeline", "");
                startActivity(intent);
                break;
        }

    }
}
