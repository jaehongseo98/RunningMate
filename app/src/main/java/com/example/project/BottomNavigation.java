package com.example.project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
// bottomNavigation 에서 각각의 fragment 들을 매칭
public class BottomNavigation extends AppCompatActivity {

    Button btnPage1, btnPage2, btnPage3, btnPage4;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Layout1Fragment layout1Fragment;
    Layout2Fragment layout2Fragment;
    Layout3Fragment layout3Fragment;
    Layout4Fragment layout4Fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        btnPage1 = findViewById(R.id.btnPage1);
        btnPage2 = findViewById(R.id.btnPage2);
        btnPage3 = findViewById(R.id.btnPage3);
        btnPage4 = findViewById(R.id.btnPage4);

        // 객체 생성
        layout1Fragment = new Layout1Fragment();
        layout2Fragment = new Layout2Fragment();
        layout3Fragment = new Layout3Fragment();
        layout4Fragment = new Layout4Fragment();

        fragmentManager = getSupportFragmentManager();// 관리할 객체 생성

        fragmentTransaction = fragmentManager.beginTransaction();// 작업하는 객체
        btnPage1.setOnClickListener(fragmentListener);
        btnPage2.setOnClickListener(fragmentListener);
        btnPage3.setOnClickListener(fragmentListener);
        btnPage4.setOnClickListener(fragmentListener);

        fragmentTransaction.replace(R.id.relFragmentBox,layout1Fragment);// 보여질 화면

        fragmentTransaction.commitAllowingStateLoss();// 수정사항 반영
    }

    View.OnClickListener fragmentListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            fragmentTransaction = fragmentManager.beginTransaction();
            switch (v.getId()){
                case R.id.btnPage1:
                    fragmentTransaction.replace(R.id.relFragmentBox,layout1Fragment);
                    fragmentTransaction.commitAllowingStateLoss();
                    break;
                case R.id.btnPage2:
                    fragmentTransaction.replace(R.id.relFragmentBox,layout2Fragment);
                    fragmentTransaction.commitAllowingStateLoss();
                    break;
                case R.id.btnPage3:
                    fragmentTransaction.replace(R.id.relFragmentBox,layout3Fragment);
                    fragmentTransaction.commitAllowingStateLoss();
                    break;
                case R.id.btnPage4:
                    fragmentTransaction.replace(R.id.relFragmentBox,layout4Fragment);
                    fragmentTransaction.commitAllowingStateLoss();
                    break;
            }
        }
    };
}