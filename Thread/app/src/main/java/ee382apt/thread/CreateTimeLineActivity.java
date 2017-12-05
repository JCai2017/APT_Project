package ee382apt.thread;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class CreateTimeLineActivity extends AppCompatActivity implements
        View.OnClickListener{
    private String API_URL = "https://apt-fall2017.appspot.com/timeline";
    String email = "";
    String timeLine = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_time_line);

        findViewById(R.id.SubmitButton).setOnClickListener(this);
        findViewById(R.id.BackButton).setOnClickListener(this);

        email = getIntent().getStringExtra("email");
        Log.i("TimeLineActivity", email);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.SubmitButton:
                EditText tl = (EditText)findViewById(R.id.TimeLine);
                timeLine = tl.getText().toString();

                RequestParams params = new RequestParams();
                params.put("email", email);
                params.put("timeLine", timeLine);
                AsyncHttpClient client = new AsyncHttpClient();

                client.post(API_URL, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        Log.w("async", "success!!!!");
                        Toast.makeText(CreateTimeLineActivity.this, "Create Success!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CreateTimeLineActivity.this, MainHUBActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                        Log.e("Posting_to_blob","There was a problem in retrieving the url : " + e.toString());
                    }
                });
                break;
            case R.id.BackButton:
                finish();
                break;
        }
    }
}
