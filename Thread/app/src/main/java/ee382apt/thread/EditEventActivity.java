package ee382apt.thread;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class EditEventActivity extends AppCompatActivity implements
        View.OnClickListener{

    JSONArray timelineList = new JSONArray();
    ArrayList<String> timelineName = new ArrayList<String>();

    String email = null;
    String title = null;
    String time = null;
    String location = null;
    String timeLine = null;
    ArrayList<String> timeLines = new ArrayList<String>();
    private String API_URL = "https://apt-fall2017.appspot.com/setevent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        email= getIntent().getStringExtra("email");
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
                    Spinner s = (Spinner)findViewById(R.id.TimeLine);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditEventActivity.this,
                            android.R.layout.simple_spinner_item, timelineName);
                    s.setAdapter(adapter);
                } catch (JSONException j) {
                    System.out.println("JSON Error");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("TimeLineRetrieve", "There was a problem in retrieving the url : " + error.toString());
            }
        });
        findViewById(R.id.SubmitButton).setOnClickListener(this);
        findViewById(R.id.CancelButton).setOnClickListener(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.SubmitButton:
                EditText et = (EditText)findViewById(R.id.EventEntry);
                title = et.getText().toString();

                // get date
                DatePicker datePicker = (DatePicker)findViewById(R.id.TimePicker);
                String month = "";
                int m = datePicker.getMonth() + 1;
                if (m < 10)
                    month = "0" + Integer.toString(m);
                else
                    month = Integer.toString(m);
                String day = "";
                int d = datePicker.getDayOfMonth();
                if (d < 10)
                    day = "0" + Integer.toString(d);
                else
                    day = Integer.toString(d);
                time = "" + datePicker.getYear() + month + day;
                // end get date

                Spinner tl = (Spinner)findViewById(R.id.TimeLine);
                timeLine = tl.getSelectedItem().toString();

                EditText loc = (EditText)findViewById(R.id.LocationEntry);
                location = loc.getText().toString();

                //TODO: Add data to Database
                RequestParams params = new RequestParams();
                params.put("email", email);
                params.put("title", title);
                params.put("time", time);
                params.put("timeLine", timeLine);
                params.put("location", location);
                AsyncHttpClient client = new AsyncHttpClient();
                client.post(API_URL, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        Log.w("async", "success!!!!");
                        Toast.makeText(EditEventActivity.this, "Create Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditEventActivity.this, MainHUBActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                        Log.e("Posting_to_blob","There was a problem in retrieving the url : " + e.toString());
                    }
                });
                break;
            case R.id.CancelButton:
                finish();
                break;
        }
    }
}
