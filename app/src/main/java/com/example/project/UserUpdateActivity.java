package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class UserUpdateActivity extends AppCompatActivity {

    Button updatepw, btnjuso, btnexit, btnsujung;
    TextView useremail;
    EditText edtnik, edtbirth, edtema;
    private WebView webView;
    private TextView txt_address;
    private Handler handler;
    private WebSettings webSettings;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference reference;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_update);


        useremail = (TextView)findViewById(R.id.userid);
        txt_address = (TextView)findViewById(R.id.txt_address);
        updatepw = (Button)findViewById(R.id.updatepw);
        btnjuso = (Button)findViewById(R.id.btnjuso);
        btnexit = (Button) findViewById(R.id.btn_exit);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        btnsujung = (Button)findViewById(R.id.btnsujung);
        edtnik = (EditText)findViewById(R.id.editnickname);
        edtbirth = (EditText)findViewById(R.id.edtbirth);
        edtema = (EditText)findViewById(R.id.edtemail);




        reference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String key = snapshot1.getKey();
                    if(user.getUid().equals(key)){
                        UserInfo getemail = snapshot1.getValue(UserInfo.class);
                        String email = getemail.email;
                        Log.i("email",email);
                        useremail.setText(email);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "loadPost:onCancelled", error.toException());
            }
        });



        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child("Users").addValueEventListener(new ValueEventListener() {  //Real DB  값 꺼내오기
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String key = snapshot1.getKey();
                            if(user.getUid().equals(key)){
                                reference.child("Users").child(key).setValue(null);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("TAG", "loadPost:onCancelled", error.toException());
                    }
                });

                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"탈퇴 성공",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                }else{
                                    Toast.makeText(getApplicationContext(),"오류 발생",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });



        btnjuso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UserUpdateActivity.this,DaumjusoActivity.class);
                startActivity(intent);
            }
        });

        btnsujung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child("Users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String key = snapshot1.getKey();
                            if(user.getUid().equals(key)){
                                String nick = edtnik.getText().toString().trim();
                                String birth = edtbirth.getText().toString().trim();
                                String email = edtema.getText().toString().trim();
                                DatabaseReference reference2 = reference.child("Users").child(key);
                                Map<String, Object> childUpdates = new HashMap<>();
                                childUpdates.put("nickname", nick);
                                childUpdates.put("birth", birth);
                                childUpdates.put("email", email);
                                reference2.updateChildren(childUpdates);
                                Toast.makeText(v.getContext(), "성공함", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("TAG", "loadPost:onCancelled", error.toException());
                    }
                });
                //주소, 닉네임 받아서 저장 하는건데 DB 설계
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


        updatepw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserUpdateActivity.this, DBPWUPActivity.class);
                startActivity(intent);
            }
        });
    }

    private void revokeAccess() {
        auth.getCurrentUser().delete();
    }
}