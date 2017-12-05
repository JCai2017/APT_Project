package ee382apt.thread;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ListEventActivity extends AppCompatActivity implements
        View.OnClickListener{
    JSONArray titleList = new JSONArray();
    JSONArray locList = new JSONArray();
    private static ArrayList<String> titles, locations;
    private static String email = null;
    private static String time = null;
    private static ListView mListView;
    private static ListAdapter mListAdapter;
    private String API_URL = "https://apt-fall2017.appspot.com/getevent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_event);
        email = getIntent().getStringExtra("email");
        time = getIntent().getStringExtra("time");
        titles = new ArrayList<String>();
        locations = new ArrayList<String>();
        titles.clear();
        locations.clear();

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(API_URL+"?email="+email+"&time="+time, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject jObject = new JSONObject(new String(responseBody));
                    titleList = jObject.getJSONArray("titles");
                    for (int i = 0; i < titleList.length(); i++) {
                        titles.add(titleList.getString(i));
                    }

                    locList = jObject.getJSONArray("locations");
                    for (int i = 0; i < locList.length(); i++) {
                        locations.add(locList.getString(i));
                    }
                    Log.i("ListEvent", "titleList: "+titleList.length() + ", locList: "+locList.length());
                    int cap = 1;
                    if(titles.size() > 0){
                        cap = titles.size();
                    }
                    for(int i = 0; i < cap; i ++){
                        mListAdapter = new ListAdapter(ListEventActivity.this, titles, locations);
                        mListView.setAdapter(mListAdapter);
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

        findViewById(R.id.backButton).setOnClickListener(this);
        mListView = (ListView)findViewById(R.id.list);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView title = (TextView)view.findViewById(R.id.str_title);
                Intent it = new Intent(view.getContext(), ViewEventActivity.class);
                it.putExtra("user_email", email);
                it.putExtra("title", title.getText().toString());
                it.putExtra("timewithouthour", time);
                startActivity(it);
            }
        });

        //populateListView();
    }

    public void populateListView(){
        int cap = 1;
        if(titles.size() > 0){
            cap = titles.size();
        }
        for(int i = 0; i < cap; i ++){
            mListAdapter = new ListAdapter(this, titles, locations);
            mListView.setAdapter(mListAdapter);
        }

    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.backButton:
                finish();
                break;
        }
    }
}