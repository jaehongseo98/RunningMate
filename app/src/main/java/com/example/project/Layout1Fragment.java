package com.example.project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


// bottomNavigation 에서 매칭된 첫번째 fragment
public class Layout1Fragment extends Fragment {

    FloatingActionButton WriteBtn;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_layout1,container,false);

        WriteBtn = (FloatingActionButton) root.findViewById(R.id.btnWrite);

        return root;



    }
}
