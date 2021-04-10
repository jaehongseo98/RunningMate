package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// 3번째 fragment에 붙일 adpater(프로필과 이름)
public class ProfileAdapter extends BaseAdapter {

    ArrayList<Profile> dataset = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth fireAuth;
    FirebaseUser fireUser;

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

        // 화면에 보여질 위젯에 대한 참조 획득
        ImageView ivIcon = convertView.findViewById(R.id.ivIcon);
        TextView tvName = convertView.findViewById(R.id.tvName);

        // 위젯에 데이터 반영
        Glide.with(convertView).load(profile.getProfileUrl()).into(ivIcon);
        tvName.setText(profile.getName());

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        fireUser = fireAuth.getInstance().getCurrentUser();
        String currentName = fireUser.getDisplayName();

        // 해당 리스트뷰 클릭 이벤트
        RelativeLayout relativeCmdArea = convertView.findViewById(R.id.relativeCmdArea);
        relativeCmdArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), position+"번째를 클릭함", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(v.getContext(),Layout3Fragment.class);
//                intent.putExtra("index",dataset.get(position).getName());
//                v.getContext().startActivity(intent);

                Intent intent = new Intent(v.getContext(),ProfileUrl.class);
                intent.putExtra("name",currentName);
                v.getContext().startActivity(intent);

            }
        });
        
        // 친구 삭제 이벤트
        String name = dataset.get(position).getName();// 현재 위치의 이름값을 가져옴
        Button btnDelete = convertView.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fireUser.updatePassword("changePw").continueWith(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(v.getContext(), "변경 됨", Toast.LENGTH_SHORT).show();
                    }
                    return null;
                });
                databaseReference.child("Users").addValueEventListener(new ValueEventListener() {//Users 밑에 있는 자식들의 이벤트 메서드
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {// Users 밑의 자식들을 snapshot1이 돌면서
                            Profile profile1 = snapshot1.getValue(Profile.class);
                            if(profile1.getName().equals(name)){// Users 밑의 자식들중 value 값중 현재 위치의 이름값과 같다면
                                String key = snapshot1.getKey();// 해당하는 key값을 가져와

//                                DatabaseReference hopperRef = databaseReference.child("Users").child(key);
//                                Map<String, Object> hopperUpdates = new HashMap<>();
//                                hopperUpdates.put("pw", "changePw");
//
//                                hopperRef.updateChildren(hopperUpdates);
                                databaseReference.child("Users").child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {// 삭제이벤트 실행
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(v.getContext(), "delete success", Toast.LENGTH_SHORT).show();
                                        //deleteItem(position);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(v.getContext(), "delete fail", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Log.i("snapshot error","snapshot error");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("TAG", "loadPost:onCancelled", error.toException());
                    }
                });
            }
        });
        return convertView;
    }

    public void addItem(String profileUrl, String name){
        Profile profile = new Profile(profileUrl,name);
        this.notifyDataSetChanged();
        dataset.add(profile);
    }

    public void addItem(ArrayList<Profile> list) {
        this.dataset = list;
    }

//    public void deleteItem(int position){
//        String name = dataset.get(position).getName();
//        int icon = dataset.get(position).getIcon();
//        Profile profile = new Profile(icon, name);
//        dataset.remove(profile);
//    }
}
