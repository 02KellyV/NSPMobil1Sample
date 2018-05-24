package com.onnasoft.NSPMobil.store;

import com.onnasoft.NSPMobil.session;
import com.onnasoft.store.Store;


public class Session extends Store {
    public static final Session ourInstance = new Session(null);
    public Store store = new Store(null);

    public Session(session state) {
        super(state);
        store.setState(state);
    }


    public static Session getInstance() {
        return ourInstance;
    }

    //@Override
    public void setState(session state) {
        store.setState(state);
    }

    //@Override
    public session getState() {
        return (session)store.getState();
    }
}
