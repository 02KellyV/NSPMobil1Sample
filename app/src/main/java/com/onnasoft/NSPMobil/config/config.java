package com.onnasoft.NSPMobil.config;

import com.onnasoft.NSPMobil.helper.request;

public class config {
    private static String host = "";
    private final static String host1 = "http://192.168.1.11:8000";
    private final static String host2 = "http://onnasoft.com:4000";

    public static String getHost() {
        if (host == "") {
            if (request.ping(host1)) {
                host = host1;
            } else {
                host = host2;
            }
        }
        return host;
    }

    public final static String authLogin = "/api/auth/sign-in";

    //templates
    public final static String templatesGet = "/api/templates";
}
