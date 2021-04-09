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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class DBPWUPActivity extends AppCompatActivity {

    Button updatepw;
    EditText cupw, afpw, afpwre;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_b_p_w_u_p);

        updatepw = (Button)findViewById(R.id.btn_updatepw);
        cupw = (EditText)findViewById(R.id.edtcupw);
        afpw = (EditText)findViewById(R.id.edtafpw);
        afpwre =(EditText)findViewById(R.id.edtafrepw);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        updatepw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String afpassword = afpw.getText().toString().trim();
                String cupassword = cupw.getText().toString().trim();
                String afpasswordre = afpwre.getText().toString().trim();

                reference.child("Users").addValueEventListener(new ValueEventListener() {  //비밀번호 변경, 업로드
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String key = snapshot1.getKey();
                            if(user.getUid().equals(key)){
                                UserInfo getPw = snapshot1.getValue(UserInfo.class);
                                String dbgetpw = getPw.pw;

                                if(dbgetpw.equals(cupassword) && afpassword.equals(afpasswordre)) {
                                    user.updatePassword(afpassword)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d("여기요", "User password updated.");
                                                        Toast.makeText(v.getContext(), "성공함", Toast.LENGTH_SHORT).show();
                                                        finish();

                                                    } else {
                                                        Toast.makeText(v.getContext(), "실패함", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
//                                }else{
//                                    Toast.makeText(getApplicationContext(),"비밀번호 확인 바람",Toast.LENGTH_SHORT).show();
//                                }

                                Log.i("여기좀 보세요3", cupassword);
                                Log.i("여기좀 보세요2", afpassword);

                                Log.i("여기좀 보세요4", afpasswordre);
                                DatabaseReference reference2 = reference.child("Users").child(key);
                                Map<String, Object> childUpdates = new HashMap<>();
                                childUpdates.put("pw", afpassword);
                                reference2.updateChildren(childUpdates);
                                Log.i("여기좀 보세요", dbgetpw);

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













////
//                user.updatePassword("asd1").continueWith(task -> {
//                    if(task.isSuccessful()){
//                        Toast.makeText(v.getContext(), "변경 됨", Toast.LENGTH_SHORT).show();
//                    }
////
//                    return null;
//                });
////
//////                                        Map<String, Object> postValues = null;
//////                                        UserInfo post = new UserInfo();
//////                                        post.setPw(afpassword);
//////                                        postValues = post.toMap();
////
//////                                        reference.child("Users").child(key).updateChildren(post.toMap());
////
////
////                }
////
//            }

       