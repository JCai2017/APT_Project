package ee382apt.thread;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainHUBActivity extends AppCompatActivity implements
        View.OnClickListener{
    String email = null;
    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> times = new ArrayList<>();
    ArrayList<String> locations = new ArrayList<>();

    final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    GoogleAccountCredential credential;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_hub);

        findViewById(R.id.cTimeline).setOnClickListener(this);
        findViewById(R.id.AddEvent).setOnClickListener(this);
        findViewById(R.id.DeleteTimeline).setOnClickListener(this);
        findViewById(R.id.ViewTimeLine).setOnClickListener(this);
        findViewById(R.id.ViewCalendar).setOnClickListener(this);
        findViewById(R.id.ImportButton).setOnClickListener(this);

        email = getIntent().getStringExtra("email");
        credential =
                GoogleAccountCredential.usingOAuth2(this, Collections.singleton(CalendarScopes.CALENDAR));
    }

    /*public void importEvents(){
        // Initialize Calendar service with valid OAuth credentials
        Calendar service = new Calendar.Builder(transport, jsonFactory, credential)
                .setApplicationName("ee382apt.thread").build();

        // Iterate over the events in the specified calendar
        String pageToken = null;
        do {
            try {
                Events events = service.events().list("primary").setPageToken(pageToken).execute();
                List<Event> items = events.getItems();
                for (Event event : items) {
                    //System.out.println(event.getSummary());
                    titles.add(event.getSummary() + " (Imported from Google Calendar)");
                    DateTime time = event.getStart().getDateTime();
                    times.add("" + time);
                    locations.add(event.getLocation());
                    pageToken = events.getNextPageToken();
                }
            }catch(IOException e){

            }
        } while (pageToken != null);

        //TODO: Add events to database
    }*/

    public void onClick(View view){
        Intent intent;
        switch(view.getId()){
            case R.id.cTimeline:
                intent = new Intent(this, CreateTimeLineActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
                break;
            case R.id.AddEvent:
                intent = new Intent(this, EditEventActivity.class);
                intent.putExtra("title", "");
                intent.putExtra("time", "");
                intent.putExtra("location", "");
                intent.putExtra("timeline", "");
                intent.putExtra("email", email);
                startActivity(intent);
                break;
            case R.id.DeleteTimeline:
                intent = new Intent(this, DeleteTimelineActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
                break;
            case R.id.ViewTimeLine:
                intent = new Intent(this, PrepareTimeLineActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
                break;
            case R.id.ViewCalendar:
                intent = new Intent(this, CalendarActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
                break;
            /*case R.id.ImportButton:
                importEvents();
                //Toast.makeText(this, "Imported Events from Google Calendar", Toast.LENGTH_LONG)
                //        .show();
                break;*/
        }
    }
}
