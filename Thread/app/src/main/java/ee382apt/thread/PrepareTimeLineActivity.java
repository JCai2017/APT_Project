package ee382apt.thread;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.vipulasri.timelineview.sample.model.Orientation;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PrepareTimeLineActivity extends AppCompatActivity implements
        View.OnClickListener{

    private String timeline_pos = null;
    private static String email = null;
    JSONArray timelineList = new JSONArray();
    ArrayList<String> timelineName = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    private static Spinner mSpinner;
    public final static String EXTRA_ORIENTATION = "EXTRA_ORIENTATION";
    public final static String EXTRA_WITH_LINE_PADDING = "EXTRA_WITH_LINE_PADDING";
    private String API_URL = "https://apt-fall2017.appspot.com/setevent";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_time_line);

        findViewById(R.id.backButton).setOnClickListener(this);
        findViewById(R.id.switchView).setOnClickListener(this);
        findViewById(R.id.submit).setOnClickListener(this);
        email = getIntent().getStringExtra("email");

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(API_URL+"?owner="+email, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject jObject = new JSONObject(new String(responseBody));
                    timelineList = jObject.getJSONArray("timelineList");

                    for (int i = 0; i < timelineList.length(); i++) {
                        timelineName.add(timelineList.getString(i));
                    }
                    Log.i("Prepare", String.valueOf(timelineName.size()));
                    mSpinner = (Spinner)findViewById(R.id.spinner);
                    adapter = new ArrayAdapter<String>(PrepareTimeLineActivity.this,
                            android.R.layout.simple_spinner_item, timelineName);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSpinner.setAdapter(adapter);
                    mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                            Log.i("Prepare", "something being selected");
                            timeline_pos = parent.getItemAtPosition(pos).toString();
                            //startIntent(timeline_pos);
                        }
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                } catch (JSONException j) {
                    System.out.println("JSON Error");
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("TimeLineRetrieve", "There was a problem in retrieving the url : " + error.toString());
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
            case R.id.switchView:
                Intent intent = new Intent(this, CalendarActivity.class);
                intent.putExtra("email", email);
            case R.id.submit:
                startIntent(timeline_pos);
            case R.id.backButton:
                finish();
                break;
        }
    }
}