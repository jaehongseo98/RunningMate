package com.example.project;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

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

            findViewById(R.id.btnOk).setOnClickListener(onClickListener);

        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnOk:
                        Update();
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
            Toast.makeText(this, "..",Toast.LENGTH_SHORT).show();
        }
    }
