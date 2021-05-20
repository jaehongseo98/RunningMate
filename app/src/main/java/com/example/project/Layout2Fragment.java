package com.example.project;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// bottomNavigation 에서 매칭된 두번째 fragment
public class Layout2Fragment extends Fragment {

    Button btnGps;
    TextView tvGps;
    LinearLayout linearLayoutTmap;
    TMapView tMapView;
    TMapMarkerItem markerItem1;
    TMapMarkerItem markerItem2;
    String provider;
    double longitude;
    double latitude;
    double altitude;
    double otherlongitude;
    double otherlatitude;
    LocationManager lm;
    Bitmap bitmap;
    TMapPoint tMapPoint1;
    TMapPoint tMapPoint2;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    ArrayList alTMapPoint = new ArrayList();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_layout2, container, false);
        btnGps = (Button) root.findViewById(R.id.btn_Gps);
        markerItem1 = new TMapMarkerItem(); //지도에 마커 설정
        markerItem2 = new TMapMarkerItem(); //지도에 마커 설정
        linearLayoutTmap = (LinearLayout) root.findViewById(R.id.linearLayoutTmap);
        tMapView = new TMapView(root.getContext());


        tMapView.setSKTMapApiKey("l7xxbe621f3bb7e5409bbc8dd77c60a81ca3");
        linearLayoutTmap.addView(tMapView);

        lm = (LocationManager) root.getContext().getSystemService(Context.LOCATION_SERVICE);
        btnGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission(root.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            0);
                } else {
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    if(location == null){

                        longitude = 0.0;
                        latitude = 0.0;

                        //location 자체에 마지막 위치 정보가 저장이 안 돼 있으면 NPE 가 뜨니 초기에 선언을 해줘야 함.
                    }else {
                        provider = location.getProvider();
                        longitude = location.getLongitude();
                        latitude = location.getLatitude();
                        altitude = location.getAltitude();
                    }

                    tvGps.setText(
                            "위도 : " + longitude + "\n" +
                            "경도 : " + latitude + "\n"
                            );



                    bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.border);
                    tMapPoint1 = new TMapPoint(latitude, longitude);//내 위치 포인트 지정
                    markerItem1.setTMapPoint(tMapPoint1); //마커에 Point 지정
                    markerItem1.setVisible(TMapMarkerItem.VISIBLE); //마커의 VISIBLE 설정
                    markerItem1.setIcon(bitmap); //마커의 이미지 설정
                    tMapView.setCenterPoint(longitude, latitude); //맵의 중심이 마커로 이동
                    tMapView.addMarkerItem("내 위치", markerItem1); //맵에 마커 표시



                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1,gpsLocationListener);
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,1,gpsLocationListener);

                }

            }
        });
        return root;

    }



    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            provider = location.getProvider();
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            altitude = location.getAltitude();

            tvGps.setText(
                    "위도 : " + longitude + "\n" +
                    "경도 : " + latitude + "\n"
                    );

            bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.border);
            tMapPoint1 = new TMapPoint(latitude, longitude);//내 위치 포인트 지정
            markerItem1.setTMapPoint(tMapPoint1); //마커에 Point 지정
            markerItem1.setVisible(TMapMarkerItem.VISIBLE); //마커의 VISIBLE 설정
            markerItem1.setIcon(bitmap); //마커의 이미지 설정
            tMapView.setCenterPoint(longitude, latitude); //맵의 중심이 마커로 이동
            tMapView.addMarkerItem("내 위치", markerItem1); //맵에 마커 표시

            firebaseUser = firebaseAuth.getInstance().getCurrentUser();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            databaseReference = database.getReference("Users");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot snapshot1 : snapshot.getChildren()){
                        Profile profile = snapshot1.getValue(Profile.class);
                        if(profile.getName().equals(firebaseUser.getDisplayName())){ //profile = DB에서 가져 온 이름, firebaseUser = 현재 앱 사용자
                            String key = snapshot1.getKey(); //현재 profile에 저장된 UID 값
                            DatabaseReference hopperRef = databaseReference.child(key);
                            Map<String, Object> hopperUpdates = new HashMap<>();
                            hopperUpdates.put("longitude", longitude);
                            hopperUpdates.put("latitude", latitude);
                            hopperRef.updateChildren(hopperUpdates);
                        }


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            databaseReference = database.getReference("Users");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot snapshot1 : snapshot.getChildren()){
                        Profile profile = snapshot1.getValue(Profile.class);

                        otherlongitude = profile.getLongitude();
                        otherlatitude = profile.getLatitude();


                        alTMapPoint.add(new TMapPoint(otherlatitude,otherlongitude));


                        for(int i=0; i<alTMapPoint.size(); i++){
                            TMapMarkerItem markerItem1 = new TMapMarkerItem();
                            // 마커 아이콘 지정
                            markerItem1.setIcon(bitmap);
                            // 마커의 좌표 지정
                            markerItem1.setTMapPoint((TMapPoint) alTMapPoint.get(i));
                            //지도에 마커 추가
                            tMapView.addMarkerItem("markerItem"+i, markerItem1);

                        }

                    }
                    alTMapPoint.clear();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };


    /* final ArrayList alTMapPoint = new ArrayList();
    alTMapPoint.add(new TMapPoint(37.576016, 126.976867));//광화문
    alTMapPoint.add(new TMapPoint(37.570432, 126.992169));//종로3가
    alTMapPoint.add(new TMapPoint(37.570194, 126.983045));//종로5가

    final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pin_r_m_a);

    for(int i=0; i<alTMapPoint.size(); i++){
        TMapMarkerItem markerItem1 = new TMapMarkerItem();
        // 마커 아이콘 지정
        markerItem1.setIcon(bitmap);
        // 마커의 좌표 지정
        markerItem1.setTMapPoint(alTMapPoint.get(i));
        //지도에 마커 추가
        tmap.addMarkerItem("markerItem"+i, markerItem1);

    }*/
}



