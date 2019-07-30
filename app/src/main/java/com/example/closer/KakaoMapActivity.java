package com.example.closer;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.LatLng;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;


public class KakaoMapActivity extends AppCompatActivity {

    private MapView mapView = null;
    ViewGroup mapViewContainer = null;
    MapPoint mapPoint = null;
    private LatLng presentPosition = null;
    private Location location = null;
    private int cnt = 0;
    private MapPOIItem posMarker = null;
    private MapPOIItem desMarker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kakao_maps);

        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            if (Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            } else {

                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, gpsLocationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, gpsLocationListener);

                mapView = new MapView(this);
                mapViewContainer = findViewById(R.id.map_view);
                try {
                    mapPoint = MapPoint.mapPointWithGeoCoord(location.getLatitude(), location.getLongitude());
                } catch (Exception e) {
                    mapPoint = MapPoint.mapPointWithGeoCoord(0, 0);
                }
                mapView.setMapCenterPoint(mapPoint, false);
                //true면 앱 실행 시 애니메이션 효과가 나오고 false면 애니메이션이 나오지않음.
                mapViewContainer.addView(mapView);

                posMarker = new MapPOIItem();
                newMarker("내 위치", mapPoint, mapView, posMarker);
                mapPoint = MapPoint.mapPointWithGeoCoord(36.543235, 128.793878);
                desMarker = new MapPOIItem();
                newMarker("상대 위치", mapPoint, mapView, desMarker);


            }


        } catch (IllegalArgumentException e1) {
            Toast.makeText(getApplicationContext(), "IllegalArgumentException", Toast.LENGTH_LONG).show();
        } catch (RuntimeException e2) {
            Toast.makeText(getApplicationContext(), e2.getMessage(), Toast.LENGTH_LONG).show();
            Log.d("?", e2.getMessage());
        }

        /*catch (Exception e) {
            Toast.makeText(getApplicationContext(), "GPS 연결에 실패하였습니다.", Toast.LENGTH_LONG).show();
            this.finish();
        }*/
        ImageView refresh = findViewById(R.id.refresh);
        refresh.setImageResource(R.drawable.refe);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshMyPosition();
            }
        });

    }

    private void refreshMyPosition() {
        try {
            mapView.setShowCurrentLocationMarker(true);
            String providerValue = location.getProvider();
            double longitudeValue = 0;
            double latitudeValue = 0;

            try {
                longitudeValue = location.getLongitude();
                latitudeValue = location.getLatitude();
            } catch (Exception e) {
                longitudeValue = 0;
                latitudeValue = 0;
            }

            posMarker.setMapPoint(MapPoint.mapPointWithGeoCoord(latitudeValue, longitudeValue));

            final TextView cntText = findViewById(R.id.cnt_txt);
            final TextView txt = findViewById(R.id.txt);

            Task test = new Task(posMarker);
            cntText.setText("cnt :" + (++cnt) + "\n" + providerValue);
            txt.setText((String) test.execute().get());
            //txt.setText(latitudeValue + "\n" + longitudeValue);
        } catch (Exception e) {
        }
    }

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
            refreshMyPosition();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };
/*
    public MapPoint findPos(String name) {
        try {
            MapPoint mp = null;


        } catch (Exception e) {
        }
        return null;
    }
    */

    public void onPause() {
        super.onPause();
        finish();
    }
}