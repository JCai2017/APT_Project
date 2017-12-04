package ee382apt.thread;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainHUBActivity extends AppCompatActivity implements
        View.OnClickListener{
    String email = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_hub);

        findViewById(R.id.cTimeline).setOnClickListener(this);
        findViewById(R.id.AddEvent).setOnClickListener(this);
        findViewById(R.id.DeleteTimeline).setOnClickListener(this);
        findViewById(R.id.ViewTimeLine).setOnClickListener(this);
        findViewById(R.id.ViewCalendar).setOnClickListener(this);

        email = getIntent().getStringExtra("email");
    }

    public void onClick(View view){
        Intent intent = new Intent(this, CreateTimeLineActivity.class);
        switch(view.getId()){
            case R.id.cTimeline:
                intent = new Intent(this, CreateTimeLineActivity.class);
                break;
            case R.id.AddEvent:
                intent = new Intent(this, EditEventActivity.class);
                intent.putExtra("title", "");
                intent.putExtra("time", "");
                intent.putExtra("location", "");
                intent.putExtra("timeline", "");
                break;
            case R.id.DeleteTimeline:
                intent = new Intent(this, DeleteTimelineActivity.class);
                break;
            case R.id.ViewTimeLine:
                intent = new Intent(this, PrepareTimeLineActivity.class);
                break;
            case R.id.ViewCalendar:
                intent = new Intent(this, CalendarActivity.class);
                break;
        }
        intent.putExtra("email", email);
        startActivity(intent);
    }
}
