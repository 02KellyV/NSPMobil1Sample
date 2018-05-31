package com.onnasoft.NSPMobil.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Log;

import com.google.gson.Gson;
import com.onnasoft.NSPMobil.config.config;
import com.onnasoft.NSPMobil.models.Session;
import com.onnasoft.NSPMobil.store.Store;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class request {
    public static byte[] ImageRequest(String sUrl)  throws Exception {
        String host = config.host;
        Log.d("request", host + sUrl);
        URL url = new URL(host + sUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        InputStream in = connection.getInputStream();
        InputStreamReader isw = new InputStreamReader(in);
        BufferedReader rd = new BufferedReader(isw);


        int off = 0;
        int len = 1024;
        byte[] content = new byte[1024*1024*50];
        byte[] buff = new byte[len];

        in.read(buff, off, len);
        for (int i = 0; i < buff.length; i++) {
            content[off+i] = buff[i];
        }

        //in.read(buff);
        //Log.w("buffer", String.valueOf(buff));


        return content;
    }



    public static Bitmap getBitmapFromURL(String sUrl) {
        try {
            Session session = (Session) Store.getState().get("session");
            String host = config.host;
            URL url = new URL(host + sUrl + "?token=" + session.token);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    public static String get(String sUrl) throws Exception {
        String host = config.host;
        Log.d("request", host + sUrl);
        URL url = new URL(host + sUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        InputStream in = connection.getInputStream();
        InputStreamReader isw = new InputStreamReader(in);
        BufferedReader bf = new BufferedReader(isw);

        String content = "", line;

        while ((line = bf.readLine()) != null) {
            content += line + "\n";
        }
        return content;
    }

    public static String post(String sUrl, HashMap<String, String> params) throws Exception {
        String host = config.host;
        URL url = new URL(host + sUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        Gson p = new Gson();
        String body = p.toJson(params);
        OutputStream os = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(body);
        writer.flush();
        writer.close();
        os.close();


        //connection.connect();
        BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String content = "", line;
        while ((line = rd.readLine()) != null) {
            content += line + "\n";
        }
        return content;
    }
}
