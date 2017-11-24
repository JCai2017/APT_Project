package ee382apt.thread;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DeleteTimelineActivity extends AppCompatActivity implements
        View.OnClickListener{

    String email = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_timeline);

        findViewById(R.id.delete).setOnClickListener(this);
        findViewById(R.id.backButton).setOnClickListener(this);

        email = getIntent().getExtras().getString("email");
        //TODO: Populate Spinner
    }

    public void deleteTimeLine(){
        //TODO: Delete TimeLine and events from database
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.backButton:
                finish();
                break;
            case R.id.delete:
                //TODO: Get Entry from Spinner
                deleteTimeLine();
                break;
        }
    }
}
