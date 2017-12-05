package ee382apt.thread;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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

public class DeleteTimelineActivity extends AppCompatActivity implements
        View.OnClickListener{

    String email = null;
    private String API_URL = "https://apt-fall2017.appspot.com";
    JSONArray timelineList = new JSONArray();
    ArrayList<String> timelineName = new ArrayList<String>();
    String timeLine = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_timeline);

        findViewById(R.id.delete).setOnClickListener(this);
        findViewById(R.id.backButton).setOnClickListener(this);

        email = getIntent().getStringExtra("email");

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(API_URL+"/setevent"+"?owner="+email, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject jObject = new JSONObject(new String(responseBody));
                    timelineList = jObject.getJSONArray("timelineList");

                    for (int i = 0; i < timelineList.length(); i++) {
                        timelineName.add(timelineList.getString(i));
                    }
                    Spinner s = (Spinner)findViewById(R.id.TimeLines);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(DeleteTimelineActivity.this,
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
    }

    public void deleteTimeLine(){
        Spinner tl = (Spinner)findViewById(R.id.TimeLines);
        timeLine = tl.getSelectedItem().toString();

        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("timeLine", timeLine);
        AsyncHttpClient client = new AsyncHttpClient();

        client.post(API_URL+"/deletetl", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.w("async", "success!!!!");
                Toast.makeText(DeleteTimelineActivity.this, "Delete successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DeleteTimelineActivity.this, MainHUBActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.e("Posting_to_blob","There was a problem in retrieving the url : " + e.toString());
            }
        });
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.backButton:
                finish();
                break;
            case R.id.delete:
                deleteTimeLine();
                break;
        }
    }
}
