package com.example.closer;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;


public class KakaoMapActivity extends AppCompatActivity {

    private MapView mapView = null;
    ViewGroup mapViewContainer = null;
    MapPoint myMapPoint = null;
    MapPoint desMapPoint = null;
    private Location location = null;
    private int cnt = 0;
    private MapPOIItem posMarker = null;
    private MapPOIItem desMarker = null;
    Handler handler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kakao_maps);

        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        handler = new Handler();

        try {
            if (Build.VERSION.SDK_INT >= 23
                    && ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            } else {

                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, gpsLocationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, gpsLocationListener);

                mapView = new MapView(this);
                mapViewContainer = findViewById(R.id.map_view);

                myMapPoint = MapPoint.mapPointWithGeoCoord(location.getLatitude(), location.getLongitude());

                mapView.setMapCenterPoint(myMapPoint, false);
                //true면 앱 실행 시 애니메이션 효과가 나오고 false면 애니메이션이 나오지않음.
                mapViewContainer.addView(mapView);

                posMarker = new MapPOIItem();
                newMarker("내 위치", myMapPoint, mapView, posMarker);

                desMapPoint = MapPoint.mapPointWithGeoCoord(37.576947, 126.976830);
                desMarker = new MapPOIItem();
                newMarker("상대 위치", desMapPoint, mapView, desMarker);

            }
        } catch (Exception e) {
            //this.finish();
            Log.d("??", e.getMessage());
            //Toast.makeText(getApplicationContext(), "GPS 연결에 실패하였습니다.", Toast.LENGTH_LONG).show();
        }

        ImageView refresh = findViewById(R.id.refresh);
        refresh.setImageResource(R.drawable.refe);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshPosition();
            }
        });
        Log.d("^^*", "되나여");

    }

    private void refreshPosition() {
        Log.d("?!?", "1");
        try {

            Log.d("?!?", "2");
            mapView.setShowCurrentLocationMarker(true);

            posMarker.setMapPoint(MapPoint.mapPointWithGeoCoord(location.getLatitude(), location.getLongitude()));


            Log.d("?!?", "3");
            final TextView cntText = findViewById(R.id.cnt_txt);
            final TextView txt = findViewById(R.id.txt);
            final TextView hiddenMsg = findViewById(R.id.hiddenMsg);

            String phoneNum = "01037523482";
            String getPhone = "01028763482";
            String testText = null;


            Task task = new Task(handler, hiddenMsg, posMarker, desMarker);
            cntText.setText("cnt :" + (++cnt) + "\n" + desMarker.getMapPoint().getMapPointGeoCoord().latitude + "\n" + desMarker.getMapPoint().getMapPointGeoCoord().longitude + "\n");
            task.execute();

            txt.setText(location.getLatitude() + " , " + location.getLongitude());

        } catch (Exception e) {
        }
    }


    /*
                TelephonyManager telManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                String PhoneNum = telManager.getLine1Number();
                if (PhoneNum.startsWith("+82")) {
                    PhoneNum = PhoneNum.replace("+82", "0");
                }
                */
    private void newMarker(String name, MapPoint mapPoint, MapView mapView, MapPOIItem marker) {
        try {
            marker.setItemName(name);
        } catch (Exception e100) {
            Log.e("?", "E1");
        }
        try {

            marker.setTag(0);
        } catch (Exception e101) {
            Log.e("?", "E2");
        }
        try {
            marker.setMapPoint(mapPoint);
        } catch (Exception e102) {
            Log.e("?", "E3");
        }
        try {
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        } catch (Exception e103) {
            Log.e("?", "E4");
        }
        try {
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
        } catch (
                Exception e104) {
            Log.e("?", "E5");
        }
        try {
            mapView.addPOIItem(marker);
        } catch (
                Exception e105) {
            Log.e("?", "E6");
        }
        try {
            mapView.setZoomLevel(1, true);
        } catch (
                Exception e106) {
            Log.e("?", "E7");
        }
    }

    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            refreshPosition();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };

    public void onPause() {
        super.onPause();
        finish();
    }
}