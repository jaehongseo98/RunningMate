package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CalenderSaveActivity extends AppCompatActivity {

    EditText edteat, edthealth;
    Button btninit;
    String shared = "file";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_save);

        edteat = (EditText)findViewById(R.id.edteat);
        edthealth = (EditText)findViewById(R.id.edthealth);
        btninit = (Button)findViewById(R.id.btn_in);

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