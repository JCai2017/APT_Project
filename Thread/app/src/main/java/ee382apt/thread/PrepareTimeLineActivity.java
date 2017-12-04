package ee382apt.thread;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.vipulasri.timelineview.sample.model.Orientation;

import java.util.ArrayList;

public class PrepareTimeLineActivity extends AppCompatActivity implements
        View.OnClickListener{

    private static String email = null;
    private static ArrayList<String> timelines = new ArrayList<>();
    private static Spinner mSpinner;
    public final static String EXTRA_ORIENTATION = "EXTRA_ORIENTATION";
    public final static String EXTRA_WITH_LINE_PADDING = "EXTRA_WITH_LINE_PADDING";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_time_line);

        findViewById(R.id.backButton).setOnClickListener(this);
        email = getIntent().getExtras().getString("email");

        //TODO: populate ArrayList with timelines from database

        mSpinner=(Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, timelines);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner) findViewById(R.id.spinner);
        sItems.setAdapter(adapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String timeline = parent.getItemAtPosition(pos).toString();
                startIntent(timeline);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    public void startIntent(String timeline){
        Intent intent = new Intent(this, TimeLineActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("timeline", timeline);
        intent.putExtra(EXTRA_ORIENTATION, Orientation.HORIZONTAL);
        intent.putExtra(EXTRA_WITH_LINE_PADDING, true);
        startActivity(intent);
    }

    public void onClick(View view){
        switch(view.getId()){
            case R.id.backButton:
                finish();
                break;
        }
    }
}
