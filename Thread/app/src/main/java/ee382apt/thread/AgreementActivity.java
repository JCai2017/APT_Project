package ee382apt.thread;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class AgreementActivity extends AppCompatActivity {
    String email = null;
    private String API_URL = "https://apt-fall2017.appspot.com/agree";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);

        email = getIntent().getStringExtra("email");
        Log.i("AgreeActivity", email);
    }

    public boolean checked(){
        CheckBox box = (CheckBox) this.findViewById(R.id.checkBox);
        return box.isChecked();
    }
    
    public void onClick(View view){
        if(checked()){
            RequestParams params = new RequestParams();
            params.put("email", email);
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(API_URL, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    Log.w("async", "success!!!!");
                    Toast.makeText(AgreementActivity.this, "Agree Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AgreementActivity.this, MainHUBActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                    Log.e("Posting_to_blob","There was a problem in retrieving the url : " + e.toString());
                }
            });
        }else{
            Toast.makeText(this, "Please check that you agree to the Terms of Service", Toast.LENGTH_LONG)
                .show();
        }
    }
}
