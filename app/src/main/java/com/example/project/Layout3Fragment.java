package com.example.project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// bottomNavigation 에서 매칭된 세번째 fragment
public class


Layout3Fragment extends Fragment{

    ListView lvBoard;
    ProfileAdapter profileAdapter;
    ChatAdapter chatAdapter;
    Button btnAllF, btnChat, btnDelete, btnBlock, btnPlusChat;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    ArrayList<Profile> list;
    ArrayList<Chat> list2;
    int roomCnt = -1;
    EditText search;
    ImageView clickSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_layout3,container,false);
        lvBoard = root.findViewById(R.id.lvBoard);
        btnAllF = (Button)root.findViewById(R.id.btnAllF);
        btnChat = (Button)root.findViewById(R.id.btnChat);
        btnDelete = root.findViewById(R.id.btnDelete);
        btnBlock = root.findViewById(R.id.btnBlock);
        btnPlusChat = root.findViewById(R.id.btnPlusChat);
        profileAdapter = new ProfileAdapter();
        chatAdapter = new ChatAdapter();
        search = root.findViewById(R.id.search);
        clickSearch = root.findViewById(R.id.clickSearch);

        btnPlusChat.setVisibility(View.GONE);

        //btnPlusChat.setVisibility(View.GONE);

        // 1.현재 로그인한 사용자를 가져옴
        firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        //Log.i("userdisplayname" ,firebaseUser.getDisplayName());

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        // storage에서 이미지 가져오기
        firebaseStorage  = firebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("images/"+firebaseUser.getDisplayName());

        ImageView imageView = root.findViewById(R.id.imageView);
        //Glide.with(getContext()).




        /////////////////
//        int bundle = getArguments().getInt("index");
//        if (getArguments() != null){
//            Toast.makeText(root.getContext(), String.valueOf(bundle), Toast.LENGTH_SHORT).show();
//            Log.i("bundel", String.valueOf(bundle));
//        } else {
//            Log.i("bundel error", String.valueOf(bundle));
//        }
        /////////////////
        // 2-1 친구 목록
        //final int[] i = {0};
        list = new ArrayList<>();
        Query allFQuery = databaseReference.child("Users");//.orderByChild("uid");

        allFQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    //profileAdapter.isEnabled(i[0]);
                    //Log.i("i의 카운트", String.valueOf(i[0]));
                    //i[0]++;
                    String key = snapshot1.getKey();
                    Log.i("key", key);
                    Profile get = snapshot1.getValue(Profile.class);
                    //String name = get.name;
                    //String profileUrl = get.profileUrl;
                    list.add(get);
                    //profileAdapter.addItem(profileUrl,name);
                    profileAdapter.addItem(list);
                    profileAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "loadPost:onCancelled", error.toException());
            }
        });

        btnPlusChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                //builder.setTitle("비밀번호 입력");
//                builder.setMessage("방 비밀번호를 입력해주세요. ");
//                EditText roomPw = new EditText(getContext());
//                builder.setView(roomPw);
//                builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        String input = roomPw.getText().toString();
//                        Toast.makeText(getActivity(), input, Toast.LENGTH_SHORT).show();
//                    }
//                });
//                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getActivity(), "취소를 누름", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                AlertDialog dialog = builder.create();
//                dialog.show();
                roomCnt+=1;
                chatAdapter.addItem(R.drawable.ic_baseline_message_24, "Room"+roomCnt, "");
            }
        });

        // 2-2 채팅 목록
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        list2 = new ArrayList<>();
        Query chatQuery = databaseReference.child("chat").orderByChild("nickname");

        chatQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list2.clear();
                roomCnt = -1;
                for(DataSnapshot snapshot2 : snapshot.getChildren()){
                    String key = snapshot2.getKey();
                    Chat get = snapshot2.getValue(Chat.class);
                    ChatData get2 = snapshot2.getValue(ChatData.class);
                    //list2.add(key);
                    String name = get.getName();
                    String msg = get.getMsg();
                    list2.add(get);
                    //chatAdapter.addItem(list2);
                    chatAdapter.addItem(R.drawable.ic_baseline_message_24, key, msg);
                    roomCnt++;
                    //chatAdapter.addItem(R.drawable.ic_baseline_account_circle_24,list2);
                    chatAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "loadPost:onCancelled", error.toException());
            }
        });

        //chatAdapter.addItem(R.drawable.ic_baseline_account_circle_24, "test", "sample view");

        lvBoard.setAdapter(profileAdapter);

        // 친구목록 검색 기능
        clickSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            Profile get = snapshot1.getValue(Profile.class);
                            if(get.name.contains(search.getText().toString().trim())){
                                list.add(get);
                                profileAdapter.addItem(list);
                                profileAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        // 상단 전체친구 창
        btnAllF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lvBoard.setAdapter(profileAdapter);
                btnPlusChat.setVisibility(View.GONE);
            }
        });

        // 상단 채팅 창
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lvBoard.setAdapter(chatAdapter);
                btnPlusChat.setVisibility(View.VISIBLE);
            }
        });

        lvBoard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(),ChatActivity.class);
//                intent.putExtra("list name",list.get(position).name);
//                intent.putExtra("list message", list.get(position).message);
                intent.putExtra("index",position);
                startActivity(intent);
            }
        });

        return root;
    }


}
