package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BoarditemAdapter extends BaseAdapter {
    ArrayList<BoardItem>  dataset = new ArrayList<>();

    // dataset 에 있는 항목 수
    @Override
    public int getCount() {
        return dataset.size();
    }

    // position 을 이용하여 dataset 에 있는 항목을 가져옴
    @Override
    public Object getItem(int position) {
        return dataset.get(position);
    }

    // position 을 이용하여 관련 행 id를 가져옴
    @Override
    public long getItemId(int position) {
        return position;
    }

    // dataset 의 지정된 위치에 데이터 표시
    // - position : 위치
    // - convertView : 각 항목들
    // - parent : convertView 가 담길 ListView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context root = parent.getContext();
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) root.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.boarditem,parent,false);
        }
        BoardItem item = dataset.get(position);

        // 화면에 보여질 위젯에 대한 참조 획득
        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
        TextView tvContent = convertView.findViewById(R.id.tvContent);
        TextView tvTime = convertView.findViewById(R.id.tvTime);

        // 위젯에 데이터 반영
        tvTitle.setText(item.getTitle());
        tvContent.setText(item.getContent());
        tvTime.setText(item.getTime());

        return convertView;
    }

    public void addItem(String title, String content, String time){
        BoardItem item = new BoardItem(title, content, time);
        dataset.add(item);
    }
}
