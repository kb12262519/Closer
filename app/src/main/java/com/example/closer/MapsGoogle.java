package com.example.closer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsGoogle extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng presentPosition;
    private Marker marker;
    private Circle circle;
    private int zoom = 18;
    private int countSet = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final CheckBox satellite = findViewById(R.id.satellite);
        final SeekBar zoomBar = findViewById(R.id.zoombar);
        final TextView zoomLevel = findViewById(R.id.zoomlevel);

        satellite.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (satellite.isChecked()) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    mMap.setBuildingsEnabled(true);
                } else {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    mMap.setBuildingsEnabled(false);
                }
            }
        });
        zoomBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                zoom = progress;
                zoomLevel.setText("zoomLevel : " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mMap.moveCamera(CameraUpdateFactory.zoomTo(zoom));
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a posMarker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        presentPosition = new LatLng(36.5412403, 128.7976163);
        marker = mMap.addMarker(new MarkerOptions().position(presentPosition).title("Marker in Init"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(presentPosition, zoom));

        circle = mMap.addCircle(new CircleOptions()
                .center(presentPosition)
                .fillColor(Color.parseColor("#88ffeeee"))
                .radius(10));
        circle.setCenter(new LatLng(36.5412403, 128.7976163));

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //GPS 설정화면으로 이동
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivity(intent);
        }

        try {
            if (Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        0);
            } else {
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        1000,
                        1,
                        gpsLocationListener);
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        1000,
                        1,
                        gpsLocationListener);
            }
        } catch (Exception e) {

        }

    }

    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            String providerValue = location.getProvider();
            double longitudeValue = location.getLongitude();
            double latitudeValue = location.getLatitude();
            double altitudeValue = location.getAltitude();

            final TextView longitude = findViewById(R.id.longitude);
            final TextView latitude = findViewById(R.id.latitude);
            final TextView altitude = findViewById(R.id.altitude);
            final TextView provider = findViewById(R.id.provider);
            final TextView cnt = findViewById(R.id.cnt);

            provider.setText("Provider : " + providerValue);
            longitude.setText("Longitude : " + longitudeValue);
            latitude.setText("Latitude : " + latitudeValue);
            altitude.setText("Altitude : " + altitudeValue);

            presentPosition = new LatLng(latitudeValue, longitudeValue);
            marker.setPosition(presentPosition);

            if (((CheckBox) findViewById(R.id.navigation)).isChecked()) {
                CameraPosition camPos = CameraPosition
                        .builder(mMap.getCameraPosition())
                        .bearing(location.getBearing())
                        .target(presentPosition)
                        .tilt(45)
                        .zoom(zoom)
                        .build();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(presentPosition, zoom));
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camPos));
            }

            cnt.setText("cnt : " + (++countSet));
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
