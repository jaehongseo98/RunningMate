package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// bottomNavigation 에서 매칭된 네번째 fragment
public class Layout4Fragment extends Fragment implements View.OnClickListener{
    FirebaseAuth auth;
    FirebaseUser userof;
    TextView user;
    Button updateuser, logout;
    CalendarView calendar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_layout4,container,false);

        user =(TextView)root.findViewById(R.id.user);
        updateuser = (Button)root.findViewById(R.id.updateuser);
        logout = (Button)root.findViewById(R.id.logout);
        calendar = (CalendarView)root.findViewById(R.id.calendar);
        auth = FirebaseAuth.getInstance();
        userof = auth.getCurrentUser();

        String username = userof.getDisplayName();
        user.setText(username+"님 환영합니다");

        updateuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PWinsertActivity.class);
                startActivity(intent);
            }
        });

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Intent intent = new Intent(getActivity(), CalenderSaveActivity.class);
                intent.putExtra("year",year);
                intent.putExtra("month",month+1);
                intent.putExtra("day",dayOfMonth);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(this);


        return root;
    }

    @Override
    public void onClick(View v) {
        if (v == logout) {
            auth.signOut();
            startActivity(new Intent(getActivity(), MainActivity.class));
        }
    }
}
