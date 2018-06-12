package com.onnasoft.NSPMobil.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class request {
    public static byte[] ImageRequest(String sUrl)  throws Exception {
        String host = config.getHost();
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

        return content;
    }



    public static Bitmap getBitmapFromURL(String sUrl) {
        try {
            Session session = (Session) Store.getState().get("session");
            String host = config.getHost();
            Log.d("url", host + sUrl + "?token=" + session.token);
            URL url = new URL(host + sUrl + "?token=" + session.token);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream in = connection.getInputStream();
            Bitmap result = BitmapFactory.decodeStream(in);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    public static String get(String sUrl) throws Exception {
        String host = config.getHost();
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
        String secure = "";
        if (Store.getState().get("session") != null) {
            Session session = (Session) Store.getState().get("session");
            secure = "?token=" + session.token;
        }
        String host = config.getHost();
        URL url = new URL(host + sUrl + secure);
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

    public static boolean ping(String host) {
        try {
            URL url = new URL(host );
            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
            urlc.setRequestProperty("User-Agent", "Android Application: NSPMobil");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(1000 * 30); // mTimeout is in seconds
            urlc.connect();

            if (urlc.getResponseCode() == 200) {
                return true;
            }
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
