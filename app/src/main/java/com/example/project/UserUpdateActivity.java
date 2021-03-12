package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserUpdateActivity extends AppCompatActivity {

    Button updatepw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_update);


        updatepw = (Button)findViewById(R.id.updatepw);

        updatepw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserUpdateActivity.this, DBPWUPActivity.class);
                startActivity(intent);
            }
        });
    }
}