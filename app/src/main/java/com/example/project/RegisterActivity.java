package com.example.project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
// 회원가입 화면(firebase-authentication 등록)
public class RegisterActivity extends AppCompatActivity {

    EditText edtInputName, edtInputID, edtInputPW, edtInputRepeat;
    Button btnRegister, btnLogin;
    private FirebaseAuth firebaseAuth;
    //private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtInputName = findViewById(R.id.edtInputName);
        edtInputID = findViewById(R.id.edtInputID);
        edtInputPW = findViewById(R.id.edtInputPW);
        edtInputRepeat = findViewById(R.id.edtInputRepeat);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

//        //액션 바 등록하기
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setTitle("Create Account");
//
//        actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기버튼
//        actionBar.setDisplayShowHomeEnabled(true); //홈 아이콘


        //파이어베이스 접근 설정
        // user = firebaseAuth.getCurrentUser();
        firebaseAuth =  FirebaseAuth.getInstance();
        //firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        //파이어베이스 user 로 접근

        //가입버튼 클릭리스너   -->  firebase에 데이터를 저장한다.
        btnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //가입 정보 가져오기
                final String email = edtInputID.getText().toString().trim();
                String pwd = edtInputPW.getText().toString().trim();
                String pwdcheck = edtInputRepeat.getText().toString().trim();

                if(pwd.equals(pwdcheck)) {
                    //Log.d(TAG, "등록 버튼 " + email + " , " + pwd);
                    final ProgressDialog mDialog = new ProgressDialog(RegisterActivity.this);
                    mDialog.setMessage("가입중입니다...");
                    mDialog.show();

                    //파이어베이스에 신규계정 등록하기
                    firebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //가입 성공시
                            if (task.isSuccessful()) {
                                mDialog.dismiss();
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                String email = user.getEmail();
                                String uid = user.getUid();
                                String name = edtInputName.getText().toString().trim();
                                String pw = edtInputPW.getText().toString().trim();

                                // 프로필 수정하기(displayName을 사용하기 위해)
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name).build();
                                user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Log.i("tag","user profile update");
                                            String displayName = user.getDisplayName();
                                        }
                                    }
                                });
                                String displayName = user.getDisplayName();
                                //Log.i("displayname",displayName);

                                //해쉬맵 테이블을 파이어베이스 데이터베이스에 저장
                                HashMap<Object,String> hashMap = new HashMap<>();

                                hashMap.put("uid",uid);
                                hashMap.put("email",email);
                                hashMap.put("name",name);
                                hashMap.put("displayName",displayName);
                                hashMap.put("pw",pw);
                                hashMap.put("ProfileUrl","");


                                // db접근 권한
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                // db에 Users 인스턴스 가짐
                                DatabaseReference reference = database.getReference("Users");
                                // 그 자식에 hashMap 넣기
                                reference.child(uid).setValue(hashMap);

                                //가입이 이루어졌을시 가입 화면을 빠져나감.
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                mDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "회원가입에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                                return;  //해당 메소드 진행을 멈추고 빠져나감.
                            }
                        }
                    });

                    //비밀번호 오류시
                }else{
                    Toast.makeText(RegisterActivity.this, "비밀번호가 틀렸습니다. 다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
    public boolean onSupportNavigateUp(){
        onBackPressed();; // 뒤로가기 버튼이 눌렸을시
        return super.onSupportNavigateUp(); // 뒤로가기 버튼
    }
}