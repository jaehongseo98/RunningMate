package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.os.Build.ID;

public class CalenderSaveActivity extends AppCompatActivity {

    EditText edteat, edthealth;
    TextView today;
    Button btninit;
    String shared = "file";
    ImageView toim;
    public Button cha_Btn,del_Btn,save_Btn;
    public TextView diaryTextView,textView2,textView3;
    public EditText contextEditText;

    DatabaseReference mDBReference = null;
    HashMap<String, Object> childUpdates = null;
    Map<String, Object> userValue = null;
    UserInfo userInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_save);

        edteat = (EditText)findViewById(R.id.edteat);
        edthealth = (EditText)findViewById(R.id.edthealth);
        btninit = (Button)findViewById(R.id.btn_in);
        save_Btn = (Button)findViewById(R.id.ok);
        today = (TextView)findViewById(R.id.date);
        toim = (ImageView)findViewById(R.id.toim);

        Intent getintent = getIntent();
        int year = getintent.getIntExtra("year",0);
        int month = getintent.getIntExtra("month",0);
        int day = getintent.getIntExtra("day",0);

        String yearo = String.valueOf(year);
        String montho = String.valueOf(month);
        String dayo = String.valueOf(day);

        String da = yearo+montho+dayo;
        today.setText(String.format("%s / %s / %s",yearo,montho,dayo));
        SaveCalDTO ss = SaveCalDTO.getInstance();
        ss.setDate(da);
        Image sss = ss.getTodaypic();
        Log.e("여기요 여깅ㅇㅇ", String.valueOf(sss));
        //Picasso.get().load(sss).into(toim);

        save_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dbineat = edteat.getText().toString();
                String dbinhealth = edthealth.getText().toString();


//                    //Firebase storage에 이미지 저장하기 위해 파일명 만들기(날짜를 기반으로)
//                    SimpleDateFormat sdf= new SimpleDateFormat("yyyMMddhhmmss"); //20191024111224
//                    String fileName= sdf.format(new Date())+".png";
//
//                    //Firebase storage에 저장하기
//                    FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
//                    StorageReference imgRef= firebaseStorage.getReference("calender/"+String.format("%s / %s / %s",yearo,montho,dayo)+"/"+fileName);
//
//
//                    //파일 업로드
//                    UploadTask uploadTask=imgRef.putFile(imgUri);
//                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            //이미지 업로드가 성공되었으므로
//                            //곧바로 firebase storage의 이미지 파일 다운로드 URL을 얻어오기
//                            Toast.makeText(ProfileUrl.this, "업로드 성공", Toast.LENGTH_SHORT).show();
//                            imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    //파라미터로 firebase의 저장소에 저장되어 있는
//                                    //이미지에 대한 다운로드 주소(URL)을 문자열로 얻어오기
//                                    G.profileUrl= uri.toString();
//                                    Toast.makeText(getApplicationContext(), "프로필 저장 완료", Toast.LENGTH_SHORT).show();
//
//                                    //1. Firebase Database에 nickName, profileUrl을 저장
//                                    //firebase DB관리자 객체 소환
//                                    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
//                                    //'profiles'라는 이름의 자식 노드 참조 객체 얻어오기
//                                    DatabaseReference profileRef= firebaseDatabase.getReference("Users");
//
////                        //닉네임을 key 식별자로 하고 프로필 이미지의 주소를 값으로 저장
////                        profileRef.child(G.nickName).setValue(G.profileUrl);
//
//                                    // 프로필 사진 db에 저장(PhotoUri)
//                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setPhotoUri(imgUri).build();
//                                    FirebaseAuth firebaseAuth = null;
//                                    FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
//                                    firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if(task.isSuccessful()){
//                                                Toast.makeText(ProfileUrl.this, "프로필 등록", Toast.LENGTH_SHORT).show();
//                                                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                                                DatabaseReference reference = database.getReference("Users").child(firebaseUser.getUid());
//                                                Map<String, Object> hopperUpdates = new HashMap<>();
//                                                hopperUpdates.put("profileUrl",G.profileUrl);
//                                                reference.updateChildren(hopperUpdates);
//                                            }
//
//                                            // push 사용시 랜덤 키값 생성되므로 사용 X
////                                FirebaseDatabase database = FirebaseDatabase.getInstance();
////                                DatabaseReference reference = database.getReference("Users").child(firebaseUser.getUid()).push();
////                                reference.setValue(firebaseUser.getPhotoUrl().toString());
//                                        }
//                                    });
//
//                                    // push()로 넣은값 가져오는 방법
//                                    //DatabaseReference pushedPostRef = postsRef.push();
//                                    //String postId = pushedPostRef.getKey();
//
//                                    //2. 내 phone에 nickName, profileUrl을 저장
//                                    SharedPreferences preferences= getSharedPreferences("account",MODE_PRIVATE);
//                                    SharedPreferences.Editor editor=preferences.edit();
//
//                                    editor.putString("nickName",G.nickName);
//                                    editor.putString("profileUrl", G.profileUrl);
//
//                                    editor.commit();
//                                    //저장이 완료
////                        fragmentManager = getSupportFragmentManager();
////                        fragmentTransaction = fragmentManager.beginTransaction();
////                        layout3Fragment = new Layout3Fragment();
////                        fragmentTransaction.replace(R.id.relFragmentBox,layout3Fragment);
////                        Intent intent=new Intent(getApplicationContext(), Layout3Fragment.class);
////                        startActivity(intent);
//                                    finish();
//                                }
//                            });
//                        }
//                    });
//                }
//
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
