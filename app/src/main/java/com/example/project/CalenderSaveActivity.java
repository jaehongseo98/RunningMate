package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import static android.os.Build.ID;

public class CalenderSaveActivity extends AppCompatActivity {

    EditText edteat, edthealth;
    TextView today;
    Button btninit;
    String shared = "file";
    public Button cha_Btn,del_Btn,save_Btn;
    public TextView diaryTextView,textView2,textView3;
    public EditText contextEditText;

    DatabaseReference mDBReference = null;
    HashMap<String, Object> childUpdates = null;
    Map<String, Object> userValue = null;
    UserInfo userInfo = null;

    UserInfo si = UserInfo.getInstance();
//    public void postFirebaseDatabase(boolean flag){
//        mDBReference = FirebaseDatabase.getInstance().getReference();
//        childUpdates = new HashMap<>();
//        if (flag) {
//            userInfo = new UserInfo(ID, pw, name, birth, gender, height, weight, disease, purpose)
//            userValue = userInfo.toMap();
//        }
//
//        childUpdates.put("/User_info/" + ID, userValue);
//        mDBReference.updateChildren(childUpdates);
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_save);

        edteat = (EditText)findViewById(R.id.edteat);
        edthealth = (EditText)findViewById(R.id.edthealth);
        btninit = (Button)findViewById(R.id.btn_in);
        save_Btn = (Button)findViewById(R.id.ok);
        today = (TextView)findViewById(R.id.date);

        Intent getintent = getIntent();
        int year = getintent.getIntExtra("year",0);
        int month = getintent.getIntExtra("month",0);
        int day = getintent.getIntExtra("day",0);

        String yearo = String.valueOf(year);
        String montho = String.valueOf(month);
        String dayo = String.valueOf(day);

        today.setText(String.format("%s / %s / %s",yearo,montho,dayo));


        save_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dbineat;
                String dbinhealth;

            }
        });



        btninit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalenderSaveActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
        String value = sharedPreferences.getString("eat","");
        edteat.setText(value);
    }

    //임시 저장
    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences sharedPreferences = getSharedPreferences(shared,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String value = edteat.getText().toString();
        editor.putString("eat",value);
        editor.commit();

    }
}
