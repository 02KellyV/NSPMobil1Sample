package com.onnasoft.store;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class Store {
    private ArrayList<Method> list;
    private Object state;
    public Store(Object state) {
        list = new ArrayList<>();
        this.state = state;
    }
    public void setState(Object state) {
        this.state = state;
        for (Method method : list) {
            try {
                method.invoke(this);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
    public Object getState() {
        return state;
    }
    public void subscribe(Method method) {
        if(list.indexOf(method) >= 0) {
            return;
        }
        list.add(method);
    }
    public void unsubscribe(Method method) {
        for (int i = list.size(); i >= 0 ; i--) {
            list.remove(method);
            return;
        }
    }
}
