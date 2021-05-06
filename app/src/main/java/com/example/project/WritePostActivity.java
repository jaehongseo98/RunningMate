package com.example.project;

import android.app.Activity;
import android.app.TaskStackBuilder;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

//게시글 파이어베이스에 저장
public class WritePostActivity extends BasicActivity {
    private static final String TAG = "WritePostActivity";
    private FirebaseUser user;
    private ArrayList<String> pathList = new ArrayList<>();
    private LinearLayout parent;
    int pathCount, successCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

        parent = findViewById(R.id.contentsLayout);

        findViewById(R.id.btnWrite).setOnClickListener(onClickListener);
        findViewById(R.id.btnImg).setOnClickListener(onClickListener);
        findViewById(R.id.btnVideo).setOnClickListener(onClickListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0: {
                if (resultCode == Activity.RESULT_OK) {
                    String profilePath = data.getStringExtra("profilePath");
                    pathList.add(profilePath);

                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    ImageView imageView = new ImageView(WritePostActivity.this);
                    imageView.setLayoutParams(layoutParams);
                    Glide.with(this).load(profilePath).override(1000).into(imageView);
                    parent.addView(imageView);

                    EditText editText = new EditText(WritePostActivity.this);
                    editText.setLayoutParams(layoutParams);
                    editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
                    editText.setHint("내용");
                    parent.addView(editText);
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
                    storageUpload();
                    break;
                case R.id.btnImg:
                    myStartActivity(GalleryActivity.class, "image");
                    break;
                case R.id.btnVideo:
                    myStartActivity(GalleryActivity.class, "video");
                    break;
            }
        }

        ;

        private void storageUpload() {
            final String title = ((EditText) findViewById(R.id.edtTitle)).getText().toString();

            if (title.length() > 0) {
                final ArrayList<String> contentsList = new ArrayList<>();
                user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                final DocumentReference documentReference = firebaseFirestore.collection("cities").document();
                for (int i = 0; i < parent.getChildCount(); i++) {
                    View view = parent.getChildAt(i);
                    if (view instanceof EditText) {
                        String text = ((EditText) view).getText().toString();
                        if (text.length() > 0) {
                            contentsList.add(text);
                        }
                    } else {
                        contentsList.add(pathList.get(pathCount));
                        final StorageReference mountainImagesRef = storageRef.child("user/" + user.getUid() + "/" + pathCount + ".jpg");
                        try {
                            InputStream stream = new FileInputStream(new File(pathList.get(pathCount)));
                            StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata("index", "" + pathCount).build();
                            UploadTask uploadTask = mountainImagesRef.putStream(stream, metadata);
                            uploadTask.addOnFailureListener(exception -> {
                                // Handle unsuccessful uploads
                            }).addOnSuccessListener(taskSnapshot -> {
                                final int index = Integer.parseInt(taskSnapshot.getMetadata().getCustomMetadata("index"));
                                mountainImagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                    contentsList.set(index, uri.toString());
                                    successCount++;
                                    if (pathList.size() == successCount) {
                                        //완료
                                        WriteInfo writeInfo = new WriteInfo(title, contentsList, user.getUid(), new Date());
                                        storeUpload(documentReference, writeInfo);
                                    }
                                });
                            });
                        } catch (FileNotFoundException e) {
                            Log.e("로그", "에러" + e.toString());
                        }
                        pathCount++;
                    }
                }

            } else {
                startToast("내용을 입력해주세요.");
            }
        }

        private void storeUpload(DocumentReference documentReference, WriteInfo writeInfo) {
            documentReference.set(writeInfo)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully writen!"))
                    .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
        }

        private void startToast(String msg) {
            Toast.makeText(WritePostActivity.this, "", Toast.LENGTH_SHORT).show();
        }

        //GalleryActivity에 보냄
        private void myStartActivity(Class c, String media) {
            Intent intent = new Intent(WritePostActivity.this, c);
            intent.putExtra("media", media);
            startActivityForResult(intent, 0);
        }
    };
}
