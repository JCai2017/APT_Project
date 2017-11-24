package ee382apt.thread;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class AgreementActivity extends AppCompatActivity {
    String email = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);

        email = getIntent().getExtras().getString("email");
        //TODO: Check for agreement on database
    }

    public boolean checked(){
        CheckBox box = (CheckBox) this.findViewById(R.id.checkBox);
        return box.isChecked();
    }

    //TODO: Store agreement to database
    public void onClick(View view){
        if(checked()){
            Intent intent = new Intent(this, MainHUBActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Please check that you agree to the Terms of Service", Toast.LENGTH_LONG)
                .show();
        }
    }
}
