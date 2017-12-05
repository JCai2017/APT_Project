package ee382apt.thread;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ViewEventActivity extends AppCompatActivity implements
        View.OnClickListener{
    String email = null;
    String title = null;
    String time = null;
    String location = null;
    String timeLine = null;
    private String API_URL = "https://apt-fall2017.appspot.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        findViewById(R.id.EditButton).setOnClickListener(this);
        findViewById(R.id.DeleteButton).setOnClickListener(this);
        findViewById(R.id.backButton).setOnClickListener(this);
        email = getIntent().getStringExtra("user_email");
        title = getIntent().getStringExtra("title");
        time = getIntent().getStringExtra("timewithouthour");

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(API_URL+"/viewevent?email="+email+"&title="+title+"&date="+time, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject jObject = new JSONObject(new String(responseBody));
                    time = jObject.getString("time");
                    location = jObject.getString("location");
                    timeLine = jObject.getString("timeline");
                    TextView event = (TextView)findViewById(R.id.EventName);
                    event.setText(title);

                    TextView timeView = (TextView)findViewById(R.id.Time);
                    timeView.setText(time);

                    TextView loc = (TextView)findViewById(R.id.Location);
                    loc.setText(location);

                    TextView tl = (TextView)findViewById(R.id.TimeLine);
                    tl.setText(timeLine);
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

    public void edit(){
        Intent intent = new Intent(this, EditEventActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("title", title);
        intent.putExtra("time", time);
        intent.putExtra("location", location);
        intent.putExtra("timeLine", timeLine);
        startActivity(intent);
    }

    public void delete(){
        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("title", title);
        params.put("time", time);
        params.put("timeLine", timeLine);
        params.put("location", location);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(API_URL+"/deleteevent", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.w("async", "success!!!!");
                Toast.makeText(ViewEventActivity.this, "Delete Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ViewEventActivity.this, MainHUBActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.e("Posting_to_blob","There was a problem in retrieving the url : " + e.toString());
            }
        });
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