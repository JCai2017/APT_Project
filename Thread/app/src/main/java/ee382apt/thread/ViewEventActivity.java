package ee382apt.thread;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ViewEventActivity extends AppCompatActivity implements
        View.OnClickListener{
    String email = null;
    String title = null;
    String time = null;
    String location = null;
    String timeLine = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        findViewById(R.id.EditButton).setOnClickListener(this);
        findViewById(R.id.DeleteButton).setOnClickListener(this);
        findViewById(R.id.backButton).setOnClickListener(this);
        email = getIntent().getStringExtra("email");

        //TODO: Get Values for Event from Database
        TextView event = (TextView)findViewById(R.id.EventName);
        event.setText(title);

        TextView timeView = (TextView)findViewById(R.id.Time);
        timeView.setText(time);

        TextView loc = (TextView)findViewById(R.id.Location);
        loc.setText(location);

        TextView tl = (TextView)findViewById(R.id.TimeLine);
        tl.setText(timeLine);
    }

    public void edit(){
        Intent intent = new Intent(this, EditEventActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("title", title);
        intent.putExtra("time", time);
        intent.putExtra("location", location);
        intent.putExtra("timeLine", timeLine);
        startActivity(intent);
    }

    //TODO: Remove Event from Database
    public void delete(){
        finish();
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.EditButton:
                edit();
                break;
            case R.id.DeleteButton:
                delete();
                break;
            case R.id.backButton:
                this.finish();
                break;
        }
    }
}
