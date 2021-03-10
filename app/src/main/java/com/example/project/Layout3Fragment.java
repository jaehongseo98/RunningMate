package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
// bottomNavigation 에서 매칭된 세번째 fragment
public class Layout3Fragment extends Fragment {

    ListView lvBoard;
    ProfileAdapter profileAdapter;
    ChatAdapter chatAdapter;
    Button btnAllF, btnChat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_layout3,container,false);
        lvBoard = root.findViewById(R.id.lvBoard);
        btnAllF = (Button)root.findViewById(R.id.btnAllF);
        btnChat = (Button)root.findViewById(R.id.btnChat);
        profileAdapter = new ProfileAdapter();
        chatAdapter = new ChatAdapter();

        profileAdapter.addItem(R.drawable.ic_baseline_account_circle_24,"이정환");
        profileAdapter.addItem(R.drawable.ic_baseline_account_circle_24,"공대표");
        profileAdapter.addItem(R.drawable.ic_baseline_account_circle_24,"안정희");
        profileAdapter.addItem(R.drawable.ic_baseline_account_circle_24,"서재홍");

        chatAdapter.addItem(R.drawable.ic_baseline_account_circle_24,"이정환","낼 장미공원 7시 ㄱ");
        chatAdapter.addItem(R.drawable.ic_baseline_account_circle_24,"공대표","와이 ㅇㄱㄴ;");
        chatAdapter.addItem(R.drawable.ic_baseline_account_circle_24,"서재홍","같이.. 운동하실래요..?");

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
