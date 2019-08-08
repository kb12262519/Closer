package com.example.closer;

import android.os.AsyncTask;
import android.os.Handler;
import android.widget.TextView;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Task extends AsyncTask {
    Handler handler;
    TextView hiddenMsg;
    MapPOIItem posMarker;
    MapPOIItem desMarker;
    String receiveMsg = null;

    public Task(Handler h, TextView t, MapPOIItem p1, MapPOIItem p2) {
        super();
        handler = h;
        hiddenMsg = t;
        posMarker = p1;
        desMarker = p2;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try{
            String str;
            String sendMsg = "ph=01037523482&lat=1";
            //sendMsg += posMarker.getMapPoint().getMapPointGeoCoord().latitude;
            sendMsg += "&lon=1";
            //sendMsg += posMarker.getMapPoint().getMapPointGeoCoord().longitude;
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
                    desMarker.setMapPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(desPos[1]), Double.parseDouble(desPos[2])));
                } catch (Exception e) {
                }
            }
        });

        return null;

    }
}