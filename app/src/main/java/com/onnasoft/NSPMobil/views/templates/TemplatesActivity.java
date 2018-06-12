package com.onnasoft.NSPMobil.views.templates;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onnasoft.NSPMobil.R;
import com.onnasoft.NSPMobil.config.config;
import com.onnasoft.NSPMobil.helper.request;
import com.onnasoft.NSPMobil.models.Session;
import com.onnasoft.NSPMobil.models.Template;
import com.onnasoft.NSPMobil.store.Store;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

        mTemplates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onTemplateClick(parent, view, position, id);
            }
        });


        this.componentDidMount();
    }

    public void onTemplateClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String, Object> state = Store.getState();
        List<Template> t = (List<Template>) state.get("templates");
        HashMap<String, Object> select = new HashMap<>();
        select.put("select", t.get(position).getName());
        Store.setState(select);
        try {
            String encode = URLEncoder.encode(t.get(position).getName(), "UTF-8");
            Bitmap image = request.getBitmapFromURL("/api/templates/" + encode + "/0");
            if (image == null) {
                Log.d("template", "Image is null.");
            }
            HashMap<String, Object> template = new HashMap<>();
            template.put("template", image);
            Store.setState(template);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("item click", t.get(position).getName());

        finish();
    }

    private void componentDidMount() {
        Session session = (Session) Store.getState().get("session");
        String url = config.templatesGet + "?token=" + session.token;

        Log.d("templates", url);
        try {
            String response = request.get(url);
            Log.d("templates", response);
            Gson gson = new Gson();
            List<Template> templates = gson.fromJson(response, new TypeToken<List<Template>>(){}.getType());
            ArrayList list = new ArrayList();

            for (int i = 0; i < templates.size(); i++) {
                Template tmp = templates.get(i);
                list.add(tmp.getName());
            }
            ArrayAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);
            HashMap<String, Object> m = new HashMap<>();
            m.put("templates", templates);
            Store.setState(m);
            mTemplates.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
