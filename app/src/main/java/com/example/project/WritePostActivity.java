package com.example.project;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static com.example.project.Util.isStorageUrl;

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
    private StorageReference storageRef;
    private ArrayList<String> pathList = new ArrayList<>();
    private LinearLayout parent;
    private PostInfo postInfo;
    int pathCount, successCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

        parent = findViewById(R.id.contentsLayout);

        findViewById(R.id.btnWrite).setOnClickListener(onClickListener);
        findViewById(R.id.btnImg).setOnClickListener(onClickListener);
        findViewById(R.id.btnVideo).setOnClickListener(onClickListener);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        postInfo = (PostInfo) getIntent().getSerializableExtra("postInfo");
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
                final ArrayList<String> formatList = new ArrayList<>();
                user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                final DocumentReference documentReference = firebaseFirestore.collection("cities").document();
                final Date date = postInfo == null ? new Date() : postInfo.getCreatedAt();
                for (int i = 0; i < parent.getChildCount(); i++) {
                    LinearLayout linearLayout = (LinearLayout) parent.getChildAt(i);
                    for (int ii = 0; ii < linearLayout.getChildCount(); ii++) {
                        View view = linearLayout.getChildAt(ii);
                        if (view instanceof EditText) {
                            String text = ((EditText) view).getText().toString();
                            if (text.length() > 0) {
                                contentsList.add(text);
                                formatList.add("text");
                            }
                        } else if (!isStorageUrl(pathList.get(pathCount))) {
                            String path = pathList.get(pathCount);
                            successCount++;
                            contentsList.add(path);
                            String[] pathArray = path.split("\\.");
                            final StorageReference mountainImagesRef = storageRef.child("post/" + documentReference.getId() + "/" + pathCount + "." + pathArray[pathArray.length - 1]);
                            try {
                                InputStream stream = new FileInputStream(new File(pathList.get(pathCount)));
                                StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata("index", "" + (contentsList.size() - 1)).build();
                                UploadTask uploadTask = mountainImagesRef.putStream(stream, metadata);
                                uploadTask.addOnFailureListener(exception -> {
                                }).addOnSuccessListener(taskSnapshot -> {
                                    final int index = Integer.parseInt(taskSnapshot.getMetadata().getCustomMetadata("index"));
                                    mountainImagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                        successCount--;
                                        contentsList.set(index, uri.toString());
                                        if (successCount == 0) {
                                            PostInfo postInfo = new PostInfo(title, contentsList, formatList, user.getUid(), date);
                                            storeUpload(documentReference, postInfo);
                                        }
                                    });
                                });
                            } catch (FileNotFoundException e) {
                                Log.e("로그", "에러" + e.toString());
                            }
                            pathCount++;
                        }
                    }
                }
                if (successCount == 0) {
                    storeUpload(documentReference, new PostInfo(title, contentsList, formatList, user.getUid(), date));
                } else {
                    startToast("제목을 입력해주세요.");
                }
            }
        }

        private void storeUpload(DocumentReference documentReference, PostInfo PostInfo) {
            documentReference.set(PostInfo)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully writen!"))
                    .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
        }

        private void startToast(String msg) {
            Toast.makeText(WritePostActivity.this, msg, Toast.LENGTH_SHORT).show();
        }

        //GalleryActivity에 보냄
        private void myStartActivity(Class c, String media) {
            Intent intent = new Intent(WritePostActivity.this, c);
            intent.putExtra("media", media);
            startActivityForResult(intent, 0);
        }
    };
}
