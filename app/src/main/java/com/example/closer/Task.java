package com.example.closer;

import android.os.AsyncTask;
import android.os.Handler;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Task extends AsyncTask {
    Handler handler;
    MapPOIItem myMarker;
    MapPOIItem[] desMarker;
    String receiveMsg = null;

    public Task(Handler h, MapPOIItem p1,  MapPOIItem[] p2) {
        super();
        handler = h;
        myMarker = p1;
        desMarker = p2;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try{
            String str;
            String sendMsg = "ph=01037523482&lat=";
            sendMsg += myMarker.getMapPoint().getMapPointGeoCoord().latitude;
            sendMsg += "&lon=";
            sendMsg += myMarker.getMapPoint().getMapPointGeoCoord().longitude;
            sendMsg += "&getph=01028763482";

            String ip = "http://iclab.andong.ac.kr/by/closer/index.jsp";
            URL url = new URL(ip);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            OutputStreamWriter osw= null;
            try {
                osw = new OutputStreamWriter(conn.getOutputStream());
            }catch(Exception e){
            }
            osw.write(sendMsg);
            osw.flush();

            if (conn.getResponseCode() == conn.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
            } else {

            }
        }catch(Exception e){

        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    String[] desPos = receiveMsg.split("/");
                    desMarker[0].setMapPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(desPos[1]), Double.parseDouble(desPos[2])));
                } catch (Exception e) {
                }
            }
        });

        return null;

    }
}