package com.example.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

//게시글 파이어베이스에 저장
public class WritePostActivity extends BasicActivity {
        private static final String TAG = "WritePostActivity";
        private FirebaseUser user;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_write_post);

            findViewById(R.id.btnWrite).setOnClickListener(onClickListener);
            findViewById(R.id.btnImg).setOnClickListener(onClickListener);
            findViewById(R.id.btnVideo).setOnClickListener(onClickListener);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode){
                case 0: {
                    if (resultCode == Activity.RESULT_OK) {
                        String profilePath = data.getStringExtra("profilePath");

                        LinearLayout parent = findViewById(R.id.contentsLayout);
                        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                        ImageView imageView = new ImageView(WritePostActivity.this);
                        imageView.setLayoutParams(layoutParams);
                        Glide.with(this).load(profilePath).override(1000).into(imageView);
                        parent.addView(imageView);

                        EditText editText = new EditText(WritePostActivity.this);
                        editText.setLayoutParams(layoutParams);
                        editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
                    }
                    break;
                }
            }
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnWrite:
                        Update();
                        break;
                    case R.id.btnImg:
                        myStartActivity(GalleryActivity.class, "image");
                        break;
                    case R.id.btnVideo:
                        myStartActivity(GalleryActivity.class, "video");
                        break;
                }
            }
        };

        private void Update() {
            final String title = ((EditText) findViewById(R.id.edtTitle)).getText().toString();
            final String contents = ((EditText) findViewById(R.id.edtContents)).getText().toString();

            if (title.length() > 0 && contents.length() > 0) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                WriteInfo writeInfo = new WriteInfo(title, contents, user.getUid());
                uploader(writeInfo);
            } else {
                startToast("내용을 입력해주세요.");
            }
        }

        private void uploader(WriteInfo writeInfo) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("posts").add(writeInfo)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "Document Snapshot written with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        }

        private void startToast(String s) {
            Toast.makeText(this, "..", Toast.LENGTH_SHORT).show();
        }

    //GalleryActivity에 보냄
    private void myStartActivity(Class c, String media) {
        Intent intent = new Intent(this,c);
        intent.putExtra("media", media);
        startActivityForResult(intent, 0);
    }
}
