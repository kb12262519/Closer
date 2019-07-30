package com.example.closer;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.naver.maps.map.NaverMapSdk;

public class MapsNaver extends AppCompatActivity {
     @Override
     public void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.naver_maps);

          NaverMapSdk.getInstance(this).setClient( new NaverMapSdk.NaverCloudPlatformClient("u23bcgf0st"));
     }
     public void onPause() {
          super.onPause();
          finish();
     }
}
