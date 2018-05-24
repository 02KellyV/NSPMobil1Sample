package com.onnasoft.NSPMobil.views.templates;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.onnasoft.NSPMobil.R;
import com.onnasoft.NSPMobil.store.Session;
import com.onnasoft.NSPMobil.session;

import java.util.ArrayList;

public class TemplatesActivity extends Activity {
    ListView mTemplates ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_templates);

        mTemplates = (ListView)findViewById(R.id.templates_list);

        ArrayList list = new ArrayList();

        ArrayAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);
        mTemplates.setAdapter(adapter);
    }

    private void componentDidMount() {
        session s = Session.getInstance().getState();


        //request.get()
    }
}
