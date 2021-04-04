package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PWinsertActivity extends AppCompatActivity {

    EditText pwline;
    Button ok;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference reference;
    private List<String> uidlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_winsert);


        pwline = (EditText) findViewById(R.id.pwline);
        ok = (Button) findViewById(R.id.ok);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pw = pwline.getText().toString().trim();

                reference.child("Users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String key = snapshot1.getKey();
                            if(user.getUid().equals(key)){
                                UserInfo getPw = snapshot1.getValue(UserInfo.class);
                                String dbgetpw = getPw.pw;

                                if(dbgetpw.equals(pw)){
                                    Intent intent = new Intent(PWinsertActivity.this, UserUpdateActivity.class);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(getApplicationContext(),"pw 오류 다시 입력 바랍니다",Toast.LENGTH_SHORT).show();
                                    Log.i("실패","비밀번호 다시 입력");
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("TAG", "loadPost:onCancelled", error.toException());
                    }
                });
            }
        });
    }
}