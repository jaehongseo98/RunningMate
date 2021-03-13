package com.example.project;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;// 데이터와 아이템(채팅)의 뷰
    private RecyclerView.LayoutManager mLayoutManager;// 아이템의 항목 배치
    private List<ChatData> chatList;
    private String nick = "";
    private DatabaseReference myRef;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    ChatAdapter chatAdapter;

    private EditText edt_chat;
    private Button btn_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        btn_send = findViewById(R.id.btn_send);
        edt_chat = findViewById(R.id.edt_chat);

        user = firebaseAuth.getInstance().getCurrentUser();

//        firebaseDatabase = FirebaseDatabase.getInstance();
//        myRef = firebaseDatabase.getReference();
//        Query chatQuery = myRef.child("Message").orderByChild("nickname");
//        chatQuery.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot snapshot2 : snapshot.getChildren()){
//                    String key = snapshot2.getKey();
//                    ChatData get = snapshot2.getValue(ChatData.class);
//                    if(get.getNickname().equals(user.getUid())){// Message의 nickname과 현재 사용자의 uid가 같다면
//                        nick = user.getDisplayName();
//                    }
//                    else {
//                        nick = user.getUid();
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.w("TAG", "loadPost:onCancelled", error.toException());
//            }
//        });

        nick = user.getDisplayName();
        Log.i("nick",nick);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = edt_chat.getText().toString();
                edt_chat.setText("");
                if (msg != null){
                    ChatData chat = new ChatData();
                    chat.setNickname(nick);
                    chat.setMsg(msg);
                    myRef.push().setValue(chat);
                    Toast.makeText(ChatActivity.this, nick, Toast.LENGTH_SHORT).show();
                }

            }
        });

        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        chatList = new ArrayList<>();
        mAdapter = new ChatDataAdapter(chatList, ChatActivity.this, nick);

        mRecyclerView.setAdapter(mAdapter);

        firebaseDatabase = FirebaseDatabase.getInstance();// 선언과 생성
        myRef = firebaseDatabase.getReference("Message");// 해당 db를 참조


        myRef.addChildEventListener(new ChildEventListener() {
            // 데이터 추가
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // dataSnapshot : 위 메서드가 호출될 때의 data
                ChatData chat = dataSnapshot.getValue(ChatData.class);
                ((ChatDataAdapter)mAdapter).addChat(chat);
            }
            // 데이터 변화
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {

            }
            // 데이터 제거
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }
            // db 리스트 위치 변경
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {

            }
            // 데이터 오류
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // 1. recyclerView - 반복
        // 2. db 내용을 넣음
        // 3. 상대방 채팅 내용이 보임
    }
}