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

    String ptitle = null;
    String ptime = null;
    String plocation = null;
    String ptimeLine = null;

    ArrayList<String> timeLines = new ArrayList<String>();
    private String API_URL = "https://apt-fall2017.appspot.com/setevent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        email= getIntent().getStringExtra("email");
        title = getIntent().getStringExtra("title");
        time = getIntent().getStringExtra("time");
        location = getIntent().getStringExtra("location");
        timeLine = getIntent().getStringExtra("timeLine");

        if(title != null && !title.equals("")){
            EditText eventEntry = (EditText) findViewById(R.id.EventEntry);
            eventEntry.setText(title);
        }

        if(time != null && !time.equals("")){
            DatePicker datePicker = (DatePicker)findViewById(R.id.TimePicker);
            int year = Integer.parseInt(time.substring(0,4));
            int month = Integer.parseInt(time.substring(4,6))-1;
            int day = Integer.parseInt(time.substring(6,8));
            datePicker.init(year, month, day, null);
            EditText hour = (EditText) findViewById(R.id.StartTime);
            hour.setText(time.substring(8,10) + ":" + time.substring(10,12));
        }

        if(location != null && !location.equals("")){
            EditText locationEntry = (EditText) findViewById(R.id.LocationEntry);
            locationEntry.setText(location);
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
                    if(timeLine != null && !timeLine.equals("")){
                        int pos = timelineName.indexOf(timeLine);
                        Spinner tl = (Spinner)findViewById(R.id.TimeLine);
                        tl.setSelection(pos);
                    }
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
                ptitle = et.getText().toString();

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

                EditText hour = (EditText)findViewById(R.id.StartTime);
                String[] h = hour.getText().toString().split(":");

                ptime = "" + datePicker.getYear() + month + day + h[0] + h[1];
                // end get date

                Spinner tl = (Spinner)findViewById(R.id.TimeLine);
                ptimeLine = tl.getSelectedItem().toString();

                EditText loc = (EditText)findViewById(R.id.LocationEntry);
                plocation = loc.getText().toString();

                RequestParams params = new RequestParams();
                params.put("email", email);
                params.put("title", title);
                params.put("time", time);
                params.put("timeLine", timeLine);
                params.put("location", location);
                params.put("ptitle", ptitle);
                params.put("ptime", ptime);
                params.put("ptimeLine", ptimeLine);
                params.put("plocation", plocation);
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
