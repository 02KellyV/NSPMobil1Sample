package com.onnasoft.NSPMobil.views.forms;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.onnasoft.NSPMobil.R;
import com.onnasoft.NSPMobil.helper.request;
import com.onnasoft.NSPMobil.views.canvas.PaperView;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class SaveActivity extends Activity {

    Button bsave;
    EditText tnombre;
    EditText tidentificador;
    EditText tdescripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        bsave = findViewById(R.id.bsave);
        tnombre = findViewById(R.id.nombre);
        tidentificador = findViewById(R.id.identificador);
        tdescripcion = findViewById(R.id.descripcion);

        if (bsave != null) {
            bsave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Salvar();
                }
            });
        }
    }

    private class Response {
        boolean success;
    }

    public String getImage() {
        PaperView paperView = PaperView.getInstance();
        Bitmap image = paperView.getBackgroundImage();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encoded;
    }

    public void Salvar() {
        try {

            HashMap<String, String> params = new HashMap<>();
            params.put("nombre", tnombre.getText().toString());
            params.put("identificacion", tidentificador.getText().toString());
            params.put("description", tdescripcion.getText().toString());
            //String encoded = getImage();
            //params.put("image", encoded);

            params.put("image", "aG9sYSBtdW5kbw==");


            String response = request.post("/api/documents", params);
            Gson gson = new Gson();
            Response r = gson.fromJson(response, Response.class);
            if (r.success) {
                String message = "Guardado satisfactoriamente.";
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                finish();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String message = "Hubo un error con los datos enviados, por favor rellene el formulario.";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
