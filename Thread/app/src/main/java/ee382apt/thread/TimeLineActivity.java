package ee382apt.thread;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.github.vipulasri.timelineview.sample.model.OrderStatus;
import com.github.vipulasri.timelineview.sample.model.Orientation;
import com.github.vipulasri.timelineview.sample.model.TimeLineModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TimeLineActivity extends AppCompatActivity implements
        View.OnClickListener{
    private RecyclerView mRecyclerView;
    private TimeLineAdapter mTimeLineAdapter;
    private List<TimeLineModel> mDataList = new ArrayList<>();
    private Orientation mOrientation;
    private boolean mWithLinePadding;
    JSONArray times = new JSONArray();
    ArrayList<String> timeList = new ArrayList<String>();
    JSONArray titles = new JSONArray();
    ArrayList<String> titleList = new ArrayList<String>();

    private static String email = null;
    private static String timeline = null;
    private String API_URL = "https://apt-fall2017.appspot.com/geteventbytl";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.backButton).setOnClickListener(this);

        email = getIntent().getStringExtra("email");
        timeline = getIntent().getStringExtra("timeline");

        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mOrientation = (Orientation) getIntent().getSerializableExtra(PrepareTimeLineActivity.EXTRA_ORIENTATION);
        mWithLinePadding = getIntent().getBooleanExtra(PrepareTimeLineActivity.EXTRA_WITH_LINE_PADDING, false);

        setTitle(mOrientation == Orientation.HORIZONTAL ? getResources().getString(R.string.horizontal_timeline) : getResources().getString(R.string.vertical_timeline));

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(getLinearLayoutManager());
        mRecyclerView.setHasFixedSize(true);

        initView();
    }

    private LinearLayoutManager getLinearLayoutManager() {
        if (mOrientation == Orientation.HORIZONTAL) {
            return new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        } else {
            return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        }
    }

    private void initView() {
        setDataListItems();
        /*mTimeLineAdapter = new TimeLineAdapter(mDataList, mOrientation, mWithLinePadding);
        mRecyclerView.setAdapter(mTimeLineAdapter);*/
    }

    private void setDataListItems(){
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(API_URL+"?email="+email+"&timeline="+timeline, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject jObject = new JSONObject(new String(responseBody));
                    titles = jObject.getJSONArray("titles");
                    for (int i = 0; i < titles.length(); i++) {
                        titleList.add(titles.getString(i));
                    }

                    times = jObject.getJSONArray("times");
                    for (int i = 0; i < times.length(); i++) {
                        timeList.add(times.getString(i));
                    }
                    Log.i("ListEvent", "titleList: "+titleList.size() + ", timeList: "+timeList.size());
                    for (int i=0;i<titleList.size();i++) {
                        String year = timeList.get(i).substring(0, 4);
                        String month = timeList.get(i).substring(4, 6);
                        String day = timeList.get(i).substring(6, 8);
                        String mtime = timeList.get(i).substring(8, 10);
                        String stime = timeList.get(i).substring(10, 12);

                        mDataList.add(new TimeLineModel(titleList.get(i), year+"-"+month+"-"+day+" "+mtime+":"+stime, OrderStatus.ACTIVE));
                    }
                    mTimeLineAdapter = new TimeLineAdapter(mDataList, mOrientation, mWithLinePadding);
                    mRecyclerView.setAdapter(mTimeLineAdapter);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Menu
        switch (item.getItemId()) {
            //When home is clicked
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        if(mOrientation!=null)
            savedInstanceState.putSerializable(PrepareTimeLineActivity.EXTRA_ORIENTATION, mOrientation);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(PrepareTimeLineActivity.EXTRA_ORIENTATION)) {
                mOrientation = (Orientation) savedInstanceState.getSerializable(PrepareTimeLineActivity.EXTRA_ORIENTATION);
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.backButton:
                finish();
                break;
        }
    }
}