package ee382apt.thread;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class EditEventActivity extends AppCompatActivity implements
        View.OnClickListener{
    String email = null;
    String title = null;
    String time = null;
    String location = null;
    String timeLine = null;
    ArrayList<String> timeLines = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        email= getIntent().getExtras().getString("email");
        title = getIntent().getExtras().getString("title");
        time = getIntent().getExtras().getString("time");
        location = getIntent().getExtras().getString("location");
        timeLine = getIntent().getExtras().getString("timeline");

        if(!title.equals("") && title != null){
            EditText eventEntry = (EditText) findViewById(R.id.EventEntry);
            eventEntry.setText(title);
        }

        if(!time.equals("") && time != null){
            DatePicker datePicker = (DatePicker)findViewById(R.id.TimePicker);
            int year = Integer.parseInt(time.substring(0,4));
            int month = Integer.parseInt(time.substring(4,6));
            int day = Integer.parseInt(time.substring(6));
            datePicker.init(year, month, day, null);
        }

        if(!location.equals("") && location != null){
            EditText locationEntry = (EditText) findViewById(R.id.LocationEntry);
            locationEntry.setText(location);
        }

        if(!timeLine.equals("") && timeLine != null){
            int pos = timeLines.indexOf(timeLine);
            Spinner tl = (Spinner)findViewById(R.id.TimeLine);
            tl.setSelection(pos);
        }

        findViewById(R.id.SubmitButton).setOnClickListener(this);
        findViewById(R.id.CancelButton).setOnClickListener(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.SubmitButton:
                EditText et = (EditText)findViewById(R.id.EventEntry);
                title = et.getText().toString();

                DatePicker datePicker = (DatePicker)findViewById(R.id.TimePicker);
                time = "" + datePicker.getYear() + datePicker.getMonth() + datePicker.getDayOfMonth();

                Spinner tl = (Spinner)findViewById(R.id.TimeLine);
                timeLine = tl.getSelectedItem().toString();

                EditText loc = (EditText)findViewById(R.id.LocationEntry);
                location = loc.getText().toString();

                //TODO: Add data to Database
                break;

            case R.id.CancelButton:
                finish();
                break;
        }
    }
}
