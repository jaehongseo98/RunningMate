package com.example.project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.daum.mf.map.api.MapView;

// bottomNavigation 에서 매칭된 두번째 fragment
public class Layout2Fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_layout2,container,false);

        MapView mapViewAPI = new MapView(getActivity());
        //this, getAcitity로 사용
        ViewGroup mapViewContainer = (ViewGroup)root.findViewById(R.id.map_view);
        mapViewContainer.addView(mapViewAPI);


        return root;
    }


}
