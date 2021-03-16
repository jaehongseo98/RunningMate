package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

// bottomNavigation 에서 매칭된 세번째 fragment
public class


Layout3Fragment extends Fragment{

    ListView lvBoard;
    ProfileAdapter profileAdapter;
    ChatAdapter chatAdapter;
    Button btnAllF, btnChat, btnDelete, btnBlock;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_layout3,container,false);
        lvBoard = root.findViewById(R.id.lvBoard);
        btnAllF = (Button)root.findViewById(R.id.btnAllF);
        btnChat = (Button)root.findViewById(R.id.btnChat);
        btnDelete = root.findViewById(R.id.btnDelete);
        btnBlock = root.findViewById(R.id.btnBlock);
        profileAdapter = new ProfileAdapter();
        chatAdapter = new ChatAdapter();

        // 1.현재 로그인한 사용자를 가져옴
        firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Log.i("userdisplayname" ,firebaseUser.getDisplayName());

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
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
        Query allFQuery = databaseReference.child("Users").orderByChild("uid");

        allFQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    String key = snapshot1.getKey();
                    //Log.i("key", key);
                    Profile get = snapshot1.getValue(Profile.class);
                    String name = get.name;
                    profileAdapter.addItem(R.drawable.ic_baseline_account_circle_24,name);
                    profileAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "loadPost:onCancelled", error.toException());
            }
        });

        // 2-2 채팅 목록
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        Query chatQuery = databaseReference.child("Message").orderByChild("nickname");

        chatQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot2 : snapshot.getChildren()){
                    String key = snapshot2.getKey();
                    ChatData get = snapshot2.getValue(ChatData.class);
                    String name = get.getNickname();
                    String msg = get.getMsg();
                    chatAdapter.addItem(R.drawable.ic_baseline_account_circle_24, name, msg);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "loadPost:onCancelled", error.toException());
            }
        });

        chatAdapter.addItem(R.drawable.ic_baseline_account_circle_24, "test", "sample view");

        lvBoard.setAdapter(profileAdapter);

        btnAllF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lvBoard.setAdapter(profileAdapter);
            }
        });

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lvBoard.setAdapter(chatAdapter);
            }
        });

        lvBoard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(),ChatActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

}
