//package com.example.project;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.ChildEventListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ChatActivity extends AppCompatActivity {
//
//    private RecyclerView mRecyclerView;
//    private RecyclerView.Adapter mAdapter;// 데이터와 아이템(채팅)의 뷰
//    private RecyclerView.LayoutManager mLayoutManager;// 아이템의 항목 배치
//    private List<ChatData> chatList;
//    private String nick = "";
//    private DatabaseReference myRef;
//    FirebaseAuth firebaseAuth;
//    FirebaseUser user;
//    FirebaseDatabase firebaseDatabase;
//
//    private EditText edt_chat;
//    private Button btn_send;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chat);
//
//        btn_send = findViewById(R.id.btn_send);
//        edt_chat = findViewById(R.id.edt_chat);
//
//        user = firebaseAuth.getInstance().getCurrentUser();
//
//        nick = user.getDisplayName();
//        //Log.i("nick",nick);
//
//        btn_send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String msg = edt_chat.getText().toString();
//                edt_chat.setText("");
//                if (msg != null){
//                    ChatData chat = new ChatData();
//                    chat.setNickname(nick);
//                    chat.setMsg(msg);
//                    myRef.push().setValue(chat);
//                    Toast.makeText(ChatActivity.this, nick, Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//
//        mRecyclerView = findViewById(R.id.recyclerview);
//        mRecyclerView.setHasFixedSize(true);
//        mLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        chatList = new ArrayList<>();
//        mAdapter = new ChatDataAdapter(chatList, ChatActivity.this, nick);
//
//        mRecyclerView.setAdapter(mAdapter);
//
//        Intent intent = getIntent();
//        int index = intent.getIntExtra("index",0);
//        Log.i("index값 넘어옴", String.valueOf(index));
//
//        firebaseDatabase = FirebaseDatabase.getInstance();// 선언과 생성
//        myRef = firebaseDatabase.getReference("Chat").child("Room"+index);// 해당 db를 참조
//
//
//        myRef.addChildEventListener(new ChildEventListener() {
//            // 데이터 추가
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
//                // dataSnapshot : 위 메서드가 호출될 때의 data
//                ChatData chat = dataSnapshot.getValue(ChatData.class);
//                ((ChatDataAdapter)mAdapter).addChat(chat);
//            }
//            // 데이터 변화
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
//
//            }
//            // 데이터 제거
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//            // db 리스트 위치 변경
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
//
//            }
//            // 데이터 오류
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }
//}

















package com.example.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import com.example.project.ChatAdapter;
import com.example.project.G;
import com.example.project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class ChatActivity extends AppCompatActivity {

    EditText et;
    ListView listView;

    ArrayList<ChatData> messageItems=new ArrayList<>();
    ChatDataAdapter adapter;

    //Firebase Database 관리 객체참조변수
    FirebaseDatabase firebaseDatabase;

    //'chat'노드의 참조객체 참조변수
    DatabaseReference chatRef;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
    static String profileUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //제목줄 제목글시를 닉네임으로(또는 채팅방)
        //getSupportActionBar().setTitle(G.nickName);

        et=findViewById(R.id.edt_chat);
        listView=findViewById(R.id.listview);
        adapter=new ChatDataAdapter(messageItems,getLayoutInflater());
        listView.setAdapter(adapter);

        //Firebase DB관리 객체와 'chat'노드 참조객체 얻어오기
        firebaseDatabase= FirebaseDatabase.getInstance();
        chatRef= firebaseDatabase.getReference("chat");


        //firebaseDB에서 채팅 메세지들 실시간 읽어오기..
        //'chat'노드에 저장되어 있는 데이터들을 읽어오기
        //chatRef에 데이터가 변경되는 것으 듣는 리스너 추가
        chatRef.addChildEventListener(new ChildEventListener() {
            //새로 추가된 것만 줌 ValueListener는 하나의 값만 바뀌어도 처음부터 다시 값을 줌
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                //새로 추가된 데이터(값 : ChatData객체) 가져오기
                ChatData messageItem= dataSnapshot.getValue(ChatData.class);

                //새로운 메세지를 리스뷰에 추가하기 위해 ArrayList에 추가
                messageItems.add(messageItem);

                //리스트뷰를 갱신
                adapter.notifyDataSetChanged();
                listView.setSelection(messageItems.size()-1); //리스트뷰의 마지막 위치로 스크롤 위치 이동
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void clickSend(View view) {

        //firebase DB에 저장할 값들( 닉네임, 메세지, 프로필 이미지URL, 시간)
        //String nickName = firebaseUser.getDisplayName();
        String message= et.getText().toString();

        //메세지 작성 시간 문자열로
        Calendar calendar= Calendar.getInstance(); //현재 시간을 가지고 있는 객체
        String time=calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE); //14:16

        //String profileUrl = firebaseUser.getPhotoUrl().toString();
        databaseReference = firebaseDatabase.getInstance().getReference();
//        ValueEventListener valueEventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                ChatData chatData = snapshot.getValue(ChatData.class);
//                chatData.getProfileUrl();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        };
        String cuname = firebaseUser.getDisplayName();
        databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    //Log.i("chatData2",snapshot1.getKey());// Users바로 밑에 있는 uid값을 가져옴
                    ChatData chatData = snapshot1.getValue(ChatData.class);
                    String name = chatData.getName();
                    Log.i("name", name); //사용자 이름 가져옴

                    if(name.equals(cuname)){
                        profileUrl = chatData.getProfileUrl();
                        Log.i("profileUrl123123",profileUrl);
                        ChatData messageItem= new ChatData(cuname,message,time,profileUrl);
                        chatRef.push().setValue(messageItem);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //firebase DB에 저장할 값(MessageItem객체) 설정
//        ChatData messageItem= new ChatData(cuname,message,time,profileUrl);
//        //'chat'노드에 MessageItem객체를 통해
//        chatRef.push().setValue(messageItem);

        //EditText에 있는 글씨 지우기
        et.setText("");

        //소프트키패드를 안보이도록..
        InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

        //처음 시작할때 EditText가 다른 뷰들보다 우선시 되어 포커스를 받아 버림
        //즉, 시작부터 소프트 키패드가 올라와 있음

        //그게 싫으면...다른 뷰가 포커스를 가지도록
        //즉, EditText를 감싼 Layout에게 포커스를 가지도록 속성을 추가!![[XML에]
    }
}






















