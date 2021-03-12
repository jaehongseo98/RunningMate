package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PWinsertActivity extends AppCompatActivity {

    EditText pwline;
    Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_winsert);


        pwline = (EditText)findViewById(R.id.pwline);
        ok = (Button)findViewById(R.id.ok);


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(비밀번호가 일치하면){
                Intent intent = new Intent(PWinsertActivity.this, UserUpdateActivity.class);
                startActivity(intent);
//                }
            }
        });
    }
}