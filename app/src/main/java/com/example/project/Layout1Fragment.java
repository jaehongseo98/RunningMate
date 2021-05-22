package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


// bottomNavigation 에서 매칭된 첫번째 fragment
public class Layout1Fragment extends Fragment {

    FloatingActionButton WriteBtn;
    ListView lvBoard;
    BoarditemAdapter boarditemAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_layout1,container,false);
        lvBoard = root.findViewById(R.id.lvBoard);
        WriteBtn = (FloatingActionButton) root.findViewById(R.id.btnWrite);

        WriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(root.getContext(), WritePostActivity.class);
                startActivity(intent);
            }
        });
        // adapter 객체 생성
        boarditemAdapter = new BoarditemAdapter();

        // adpater에 각각의 게시글 붙임
        //=========================================================================================

        // 여기에다가 firestore에 있는 내용가져와서 붙이면 됨
        boarditemAdapter.addItem("제목 부분 입니다.","내용 부분 입니다.","2021/05/21");
        boarditemAdapter.addItem("제목 부분 입니다2.","내용 부분 입니다2.","2021/05/21");
        boarditemAdapter.addItem("제목 부분 입니다3.","내용 부분 입니다3.","2021/05/21");

        //=========================================================================================
        // 리스트뷰에 어댑터 붙임
        lvBoard.setAdapter(boarditemAdapter);

        return root;
    }
}
