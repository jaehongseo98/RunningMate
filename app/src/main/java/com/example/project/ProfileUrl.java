package com.example.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

public class ProfileUrl extends AppCompatActivity {

    ImageView ivProfile;
    EditText etName;

    Uri imgUri;//선택한 프로필 이미지 경로 Uri

    boolean isFirst= true; //앱을 처음 실행한 것인가?
    boolean isChanged= false; //프로필을 변경한 적이 있는가?

    Profile profile;
    String name = "";

    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    Layout3Fragment layout3Fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_url);

        ivProfile=findViewById(R.id.iv_profile);
        etName = findViewById(R.id.et_name);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");

        //폰에 저장되어 있는 프로필 읽어오기
        loadData();
        Log.i("여기요","여기요111111111111111111111111111111111111111111111");
        if(G.nickName!=null) {
            Log.i("여기요","여기요2222222222222222222222222222222222222222222222");
            G.nickName = name;
            etName.setText(G.nickName);
            Picasso.get().load(G.profileUrl).into(ivProfile);

            //처음이 아니다, 즉, 이미 접속한 적이 있다.
            isFirst = false;

        }
    }

    public void clickImage(View view) {
        //프로필 이미지 선택하도록 Gallery 앱 실행
        Intent intent= new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,10);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 10:
                if(resultCode==RESULT_OK){
                    imgUri= data.getData();
                    //Glide.with(this).load(imgUri).into(ivProfile);
                    //Glide는 이미지를 읽어와서 보여줄때 내 device의 외장메모리에 접근하는 퍼미션이 요구됨.
                    //(퍼미션이 없으면 이미지가 보이지 않음.)
                    //Glide를 사용할 때는 동적 퍼미션 필요함.

                    //Picasso 라이브러리는 퍼미션 없어도 됨.
                    Picasso.get().load(imgUri).into(ivProfile);

                    //변경된 이미지가 있다.
                    isChanged=true;
                }
                break;
        }
    }
    public void clickBtn(View view) {
        //바꾼것도 없고, 처음 접속도 아니고..
        if(!isChanged && !isFirst){
//            fragmentManager = getSupportFragmentManager();
//            fragmentTransaction = fragmentManager.beginTransaction();
//            layout3Fragment = new Layout3Fragment();
//            fragmentTransaction.replace(R.id.relFragmentBox,layout3Fragment);
//            Intent intent= new Intent(this, Layout3Fragment.class);
//            startActivity(intent);
            finish();
        }else{
            saveData();
        }
    }

    void saveData(){
        //EditText의 닉네임 가져오기 [전역변수에]
        G.nickName = etName.getText().toString();

        //이미지를 선택하지 않았을 수도 있으므로
        if(imgUri==null) return;

        //Firebase storage에 이미지 저장하기 위해 파일명 만들기(날짜를 기반으로)
        SimpleDateFormat sdf= new SimpleDateFormat("yyyMMddhhmmss"); //20191024111224
        String fileName= sdf.format(new Date())+".png";

        //Firebase storage에 저장하기
        FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
        StorageReference imgRef= firebaseStorage.getReference("profileImages/"+fileName);


        //파일 업로드
        UploadTask uploadTask=imgRef.putFile(imgUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //이미지 업로드가 성공되었으므로
                //곧바로 firebase storage의 이미지 파일 다운로드 URL을 얻어오기
                Toast.makeText(ProfileUrl.this, "업로드 성공", Toast.LENGTH_SHORT).show();
                imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //파라미터로 firebase의 저장소에 저장되어 있는
                        //이미지에 대한 다운로드 주소(URL)을 문자열로 얻어오기
                        G.profileUrl= uri.toString();
                        Toast.makeText(getApplicationContext(), "프로필 저장 완료", Toast.LENGTH_SHORT).show();

                        //1. Firebase Database에 nickName, profileUrl을 저장
                        //firebase DB관리자 객체 소환
                        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                        //'profiles'라는 이름의 자식 노드 참조 객체 얻어오기
                        DatabaseReference profileRef= firebaseDatabase.getReference("Users");

//                        //닉네임을 key 식별자로 하고 프로필 이미지의 주소를 값으로 저장
//                        profileRef.child(G.nickName).setValue(G.profileUrl);

                        // 프로필 사진 db에 저장(PhotoUri)
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setPhotoUri(imgUri).build();
                        FirebaseAuth firebaseAuth = null;
                        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
                        firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(ProfileUrl.this, "프로필 등록", Toast.LENGTH_SHORT).show();
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference reference = database.getReference("Users").child(firebaseUser.getUid());
                                    Map<String, Object> hopperUpdates = new HashMap<>();
                                    hopperUpdates.put("profileUrl",G.profileUrl);
                                    reference.updateChildren(hopperUpdates);
                                }

                                // push 사용시 랜덤 키값 생성되므로 사용 X
//                                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                                DatabaseReference reference = database.getReference("Users").child(firebaseUser.getUid()).push();
//                                reference.setValue(firebaseUser.getPhotoUrl().toString());
                            }
                        });

                        // push()로 넣은값 가져오는 방법
                        //DatabaseReference pushedPostRef = postsRef.push();
                        //String postId = pushedPostRef.getKey();

                        //2. 내 phone에 nickName, profileUrl을 저장
                        SharedPreferences preferences= getSharedPreferences("account",MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferences.edit();

                        editor.putString("nickName",G.nickName);
                        editor.putString("profileUrl", G.profileUrl);

                        editor.commit();
                        //저장이 완료
//                        fragmentManager = getSupportFragmentManager();
//                        fragmentTransaction = fragmentManager.beginTransaction();
//                        layout3Fragment = new Layout3Fragment();
//                        fragmentTransaction.replace(R.id.relFragmentBox,layout3Fragment);
//                        Intent intent=new Intent(getApplicationContext(), Layout3Fragment.class);
//                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }

    //내 phone에 저장되어 있는 프로필정보 읽어오기
    void loadData(){
        SharedPreferences preferences = getSharedPreferences("account",MODE_PRIVATE);
        G.nickName=preferences.getString("nickName", null);
        G.profileUrl=preferences.getString("profileUrl", null);
    }
}