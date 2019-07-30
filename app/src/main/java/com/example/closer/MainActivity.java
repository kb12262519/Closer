package com.example.closer;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listview = findViewById(R.id.listview);
        ListViewAdapter adapter = new ListViewAdapter();
        listview.setAdapter(adapter);

        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.kakaologo), "KAKAO", "kakao map");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.naverlogo), "NAVER", "naver map");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.googlelogo), "GOOGLE", "google map");

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View clickedView, int position, long id) {

                final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    //GPS 설정화면으로 이동
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivity(intent);
                } else {

                    Intent intent1;

                    String kind = ((ListViewItem) adapterView.getItemAtPosition(position)).getTitle();
                    Toast.makeText(getApplicationContext(), kind, Toast.LENGTH_LONG).show();

                    if (kind.equals("KAKAO")) {
                        intent1 = new Intent(MainActivity.this, KakaoMapActivity.class);
                    } else if (kind.equals("NAVER")) {
                        intent1 = new Intent(MainActivity.this, MapsNaver.class);
                    } else {
                        intent1 = new Intent(MainActivity.this, MapsGoogle.class);
                    }
                    startActivity(intent1);
                }
            }
        });

        /*
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, LIST_MENU);
        ListView listview = (ListView) findViewById(R.id.listview1) ;
        listview.setAdapter(adapter) ;
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent1 = new Intent(MainActivity.this, MapsGoogle.class);
                startActivity(intent1);
                finish();
            }
        });
        */

        /*
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String key = new String(Base64.encode(md.digest(), 0));
                Log.d("Hash key:", "!!!!!!!" + key + "!!!!!!");
            }
        } catch (Exception e) {
            Log.e("name not found", e.toString());
        }
        */
    }
}
