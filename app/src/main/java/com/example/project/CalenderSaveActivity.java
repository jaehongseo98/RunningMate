package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class PicassoTransformations { //picasso 라이브러리 화면 비율 자동 조정

    public static int targetWidth = 200;

    public static Transformation resizeTransformation = new Transformation() {
        @Override
        public Bitmap transform(Bitmap source) {
            double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
            int targetHeight = (int) (targetWidth * aspectRatio);
            Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override
        public String key() {
            return "resizeTransformation#" + System.currentTimeMillis();
        }
    };
}

public class CalenderSaveActivity extends AppCompatActivity {

    EditText edteat, edthealth;
    TextView today;
    Button btninit, btncha, btndel;
    //String shared = "file";
    ImageView toim;
    Uri uri1;
    //public int iw,jh;
    public Button cha_Btn,del_Btn,save_Btn;
    //public TextView diaryTextView,textView2,textView3;
    //public EditText contextEditText;

//    DatabaseReference mDBReference = null;
//    HashMap<String, Object> childUpdates = null;
//    Map<String, Object> userValue = null;
//    UserInfo userInfo = null;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();


    @SuppressLint("ClickableViewAccessibility")
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
        btncha = (Button)findViewById(R.id.cha_Btn);
        btndel = (Button)findViewById(R.id.del_Btn);

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

        //Firestore 이미지 url가져오기
        FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
        StorageReference imgRef= firebaseStorage.getReference("images/");

        imgRef.child(user.getDisplayName()+"/"+da+"/"+"health.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                uri1 = uri;
                Picasso.get().load(uri).transform(PicassoTransformations.resizeTransformation).into(toim);
                toim.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
//                      iw = (int)event.getX();
//                      jh = (int)event.getY();

                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN: //확대부분
                                Picasso.get().load(uri1).into(toim);
//                          case MotionEvent.ACTION_UP: 원상태 코드 추가해야함
//                              Picasso.get().load(uri1).transform(PicassoTransformations.resizeTransformation).into(toim);
                        }
                        return false;
                    }
                });
                Log.e("sdfsa", String.valueOf(uri));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.e("error",exception.toString());
            }
        });




        save_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dbineat = edteat.getText().toString().trim();
                String dbinhealth = edthealth.getText().toString().trim();
                String displayName = user.getDisplayName();
                String[] eat = dbineat.split("\\,");
                String[] health = dbinhealth.split("\\,");
                ArrayList<String> list = new ArrayList<>();


                //해쉬맵 테이블을 파이어베이스 데이터베이스에 저장
                HashMap<String, String> hashMap = new HashMap<>();
                HashMap<String, String> hashMap2 = new HashMap<>();

                for(int i=0; i<eat.length; i++){
                    hashMap.put((i+1)+"",eat[i]);
                }

                for(int i=0; i<health.length; i++){
                    hashMap2.put((i+1)+"",health[i]);
                }

                System.out.print(hashMap.values());

                // db접근 권한
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                // db에 Users 인스턴스 가짐
                DatabaseReference reference = database.getReference("Calender");
                // 그 자식에 hashMap 넣기
                reference.child(displayName).child(da).child("eat").setValue(hashMap);
                reference.child(displayName).child(da).child("health").setValue(hashMap2);
                finish();
                //startActivity(intent);

//                save_Btn.setVisibility(View.INVISIBLE);
//                btncha.setVisibility(View.VISIBLE);
//                btndel.setVisibility(View.VISIBLE);
            }
        });

//        btncha.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        btndel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


        btninit.setOnClickListener(new View.OnClickListener() { //사진 저장 액티비티 넘어가는 곳
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalenderSaveActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });

//        SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
//        String value = sharedPreferences.getString("eat","");
//        edteat.setText(value);
    }

//    public void init(){
//
//    }

    //임시 저장
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        SharedPreferences sharedPreferences = getSharedPreferences(shared,0);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        String value = edteat.getText().toString();
//        editor.putString("eat",value);
//        editor.commit();
//
//    }



}
