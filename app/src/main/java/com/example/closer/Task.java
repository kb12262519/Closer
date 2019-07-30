package com.example.closer;

import android.os.AsyncTask;

import net.daum.mf.map.api.MapPOIItem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Task extends AsyncTask {

    MapPOIItem posMarker = null;

    public Task(MapPOIItem poi){
        super();
        posMarker = poi;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            String str = null;
            String sendMsg = "id=by&pw=11&usx=1";
            //sendMsg += posMarker.getMapPoint().getMapPointGeoCoord().longitude;
            sendMsg += "&usy=1";
            //sendMsg += posMarker.getMapPoint().getMapPointGeoCoord().latitude;

            String receiveMsg = null;
            String ip = "http://iclab.andong.ac.kr/by/closer/index.jsp";
            URL url = new URL(ip);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
            osw.write(sendMsg);
            osw.flush();

            if(conn.getResponseCode() == conn.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();

            } else {

            }
            return receiveMsg;
        }catch(Exception e){
e.getLocalizedMessage()
        }
        return null;
    }
}
