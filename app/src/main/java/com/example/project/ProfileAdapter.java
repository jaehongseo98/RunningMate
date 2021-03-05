package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
// 3번째 fragment에 붙일 adpater(프로필과 이름)
public class ProfileAdapter extends BaseAdapter {

    ArrayList<Profile> dataset = new ArrayList<>();

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
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) root.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.profile,parent,false);
        }
        Profile profile = dataset.get(position);

        ImageView ivIcon = convertView.findViewById(R.id.ivIcon);
        TextView tvName = convertView.findViewById(R.id.tvName);

        ivIcon.setImageResource(profile.getIcon());
        tvName.setText(profile.getName());
        return convertView;
    }

    public void addItem(int icon, String name){
        Profile profile = new Profile(icon,name);
        dataset.add(profile);
    }
}
