//
//package com.example.project;
//
//import android.content.Context;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.List;
//
//// 아이템(채팅)에 대한 뷰 생성
//public class ChatDataAdapter extends RecyclerView.Adapter<ChatDataAdapter.MyViewHolder> {
//
//    private List<ChatData> mDataset;
//    private String myNickname;
//    // 새로운 유형 표시
//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting,parent,false);
//        MyViewHolder vh = new MyViewHolder(v);
//        return vh;
//    }
//
//    // 지정된 위치에 데이터를 표시
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        ChatData chat = mDataset.get(position);
//
////        if (chat.getNickname()== null){
////            holder.tv_nickname.setText("알수없음");
////        } else{
////            holder.tv_nickname.setText(chat.getNickname());
////        }
//        holder.tv_nickname.setText(chat.getNickname());
//        holder.tv_message.setText(chat.getMsg());
//
//        if (chat.getNickname().equals(this.myNickname)){
//            holder.tv_message.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
//            holder.tv_nickname.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
//        } else{
//            holder.tv_message.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
//            holder.tv_nickname.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
//        }
//    }
//    // 데이터 세트의 항목 수
//    @Override
//    public int getItemCount() {
//        return mDataset == null ? 0 : mDataset.size();
//    }
//
//    public ChatData getChat(int position){
//        return mDataset != null ? mDataset.get(position) : null;
//    }
//    // recyclerView 에 대한 서브 뷰
//    public static class MyViewHolder extends RecyclerView.ViewHolder{
//        public View rootView;
//        public TextView tv_nickname;
//        public TextView tv_message;
//        public MyViewHolder(View v){
//            // ViewHolder 를 이용하여 UI가 변경 할 때마다 findViewById를 계속 부르지 않아 속도가 빠름
//            super(v);
//            tv_nickname = v.findViewById(R.id.tv_nickname);
//            tv_message = v.findViewById(R.id.tv_message);
//            rootView = v;
//
//        }
//    }
//
//    public ChatDataAdapter(List<ChatData> myDataset, Context context, String myNickname){
//        mDataset = myDataset;
//        this.myNickname = myNickname;
//        Log.i("myNickname", myNickname);
//    }
//
//    public void addChat(ChatData chat){
//        mDataset.add(chat);
//        notifyItemInserted(mDataset.size()-1); // 갱신
//    }
//}

































package com.example.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.project.G;
import com.example.project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.ArrayList;


public class ChatDataAdapter extends BaseAdapter {

    ArrayList<ChatData> messageItems;
    LayoutInflater layoutInflater;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();

    public ChatDataAdapter(ArrayList<ChatData> messageItems, LayoutInflater layoutInflater) {
        this.messageItems = messageItems;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public int getCount() {
        return messageItems.size();
    }

    @Override
    public Object getItem(int position) {
        return messageItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        //현재 보여줄 번째의(position)의 데이터로 뷰를 생성
        ChatData item = messageItems.get(position);

        //재활용할 뷰는 사용하지 않음!!
        View itemView=null;

        //메세지가 내 메세지인지??
        if(item.getName().equals(firebaseUser.getDisplayName())){
            itemView= layoutInflater.inflate(R.layout.my_msgbox,viewGroup,false);
        }else{
            itemView= layoutInflater.inflate(R.layout.other_msgbox,viewGroup,false);
        }

        //만들어진 itemView에 값들 설정
        ImageView iv= itemView.findViewById(R.id.iv);
        TextView tvName= itemView.findViewById(R.id.tv_name);
        TextView tvMsg= itemView.findViewById(R.id.tv_msg);
        TextView tvTime= itemView.findViewById(R.id.tv_time);

        tvName.setText(item.getName());
        tvMsg.setText(item.getMsg());
        tvTime.setText(item.getTime());

        Glide.with(itemView).load(item.getProfileUrl()).into(iv);

        return itemView;
    }
}