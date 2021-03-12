package com.example.project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// 아이템(채팅)에 대한 뷰 생성
public class ChatDataAdapter extends RecyclerView.Adapter<ChatDataAdapter.MyViewHolder> {

    private List<ChatData> mDataset;
    private String myNickname;
    // 새로운 유형 표시
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting,parent,false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // 지정된 위치에 데이터를 표시
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ChatData chat = mDataset.get(position);

        holder.tv_nickname.setText(chat.getNickname());
        holder.tv_message.setText(chat.getMsg());

        if (chat.getNickname().equals(this.myNickname)){
            holder.tv_message.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            holder.tv_nickname.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        } else{
            holder.tv_message.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            holder.tv_nickname.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        }
    }
    // 데이터 세트의 항목 수
    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }

    public ChatData getChat(int position){
        return mDataset != null ? mDataset.get(position) : null;
    }
    // recyclerView 에 대한 서브 뷰
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public View rootView;
        public TextView tv_nickname;
        public TextView tv_message;
        public MyViewHolder(View v){
            // ViewHolder 를 이용하여 UI가 변경 할 때마다 findViewById를 계속 부르지 않아 속도가 빠름
            super(v);
            tv_nickname = v.findViewById(R.id.tv_nickname);
            tv_message = v.findViewById(R.id.tv_message);
            rootView = v;

        }
    }

    public ChatDataAdapter(List<ChatData> myDataset, Context context, String myNickname){
        mDataset = myDataset;
        this.myNickname = myNickname;
        Log.i("myNickname", myNickname);
    }

    public void addChat(ChatData chat){
        mDataset.add(chat);
        notifyItemInserted(mDataset.size()-1); // 갱신
    }
}