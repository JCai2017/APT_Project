package ee382apt.thread;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CreateTimeLineActivity extends AppCompatActivity implements
        View.OnClickListener{

    String email = "null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_time_line);

        email = getIntent().getExtras().getString("email");
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.SubmitButton:
                EditText tl = (EditText)findViewById(R.id.TimeLine);
                String timeLine = tl.getText().toString();

                //TODO: Check if TimeLine exists for user and Add TimeLine to database
                break;
            case R.id.backButton:
                finish();
                break;
        }
    }
}
