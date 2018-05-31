package com.onnasoft.NSPMobil.store;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Store {
    private static ArrayList<Subscribe> list = new ArrayList<>();
    private static HashMap<String, Object> state = new HashMap<>();

    public static void setState(HashMap<String, Object> state) {
        //Store.state = state;
        for(Map.Entry<String, Object> entry : state.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            Store.state.put(key, value);
        }
        for (Subscribe method : list) {
            method.reload();
        }
    }
    public static HashMap<String, Object> getState() {
        return Store.state;
    }
    public static void subscribe(Subscribe method) {
        if(method == null || Store.list.indexOf(method) >= 0) return;
        Store.list.add(method);
    }
    public void unsubscribe(Method method) {
        for (int i = Store.list.size(); i >= 0 ; i--) {
            Store.list.remove(method);
            return;
        }
    }
}
