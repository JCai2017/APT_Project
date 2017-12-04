package ee382apt.thread;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PrepareTimeLineActivity extends AppCompatActivity {

    Button[] btnWord = new Button[num];
    LinearLayout linear;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.PrepareTimeLineActivity);
        test();
    }
    private void test() {
        linear = (LinearLayout) findViewById(R.id.linear);
        for (int i = 0; i < btnWord.length; i++) {
            btnWord[i] = new Button(this);
            btnWord[i].setHeight(50);
            btnWord[i].setWidth(50);
            btnWord[i].setTag(i);
            btnWord[i].setOnClickListener(btnClicked);
            linear.addView(btnWord[i]);
        }
    }
    View.OnClickListener btnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object tag = v.getTag();
            Toast.makeText(getApplicationContext(), "clicked button", Toast.LENGTH_SHORT).show();
        }
    };
}
