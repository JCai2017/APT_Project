package ee382apt.thread;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ListEventActivity extends AppCompatActivity implements
        View.OnClickListener{

    private static ArrayList<String> titles, locations;
    private static String email = null;
    private static String time = null;
    private static ListView mListView;
    private static ListAdapter mListAdapter;
    //TODO: Replace with API url to get all events for date
    private String API_URL = "https://apt-fall2017.appspot.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_event);
        email = getIntent().getExtras().getString("email");
        time = getIntent().getExtras().getString("time");
        titles = new ArrayList<String>();
        locations = new ArrayList<String>();
        titles.clear();
        locations.clear();

        //TODO: Insert code to populate ArrayList with titles and locations of events for a given
        // time and email

        findViewById(R.id.backButton).setOnClickListener(this);
        mListView = (ListView)findViewById(R.id.list);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent it = new Intent(view.getContext(), ViewEventActivity.class);
                it.putExtra("user_email", email);
                startActivity(it);
            }
        });

        populateListView();
    }

    public void populateListView(){
        int cap = 1;
        if(titles.size() > 0){
            cap = titles.size();
        }
        for(int i = 0; i < cap; i ++){
            //TODO: populate ListView with data from API response
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
