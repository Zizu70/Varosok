package com.example.varosok;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;

public class UpdateActivity extends AppCompatActivity {

    private ProgressBar progressBar2;
    private LinearLayout linearLayoutUpdate;
    private EditText editTextId;
    private EditText editTextCity;
    private EditText editTextCountry;
    private EditText editTextPopulation;
    private Button buttonModify;
    private String url = "https://retoolapi.dev/L0giQy/varosok";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        init();

        buttonModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //város hozzáadása
                varosModify();
// kész                //átlépés a ListaA-ba
                Intent intent = new Intent(UpdateActivity.this, ListActivity.class);
                startActivity(intent);
                //finish fontos lesz, hogy megszüntessük a backstack-et és ne lehessen visszalépni
                finish();
            }
        });
    }
    public void init() {
        editTextId = findViewById(R.id.editTextId);
        editTextCity = findViewById(R.id.editTextCity);
        editTextCountry = findViewById(R.id.editTextCountry);
        editTextPopulation = findViewById(R.id.editTextPopulation);
        buttonModify = findViewById(R.id.buttonModify);

     }

    public void varosModify() {
        int id = Integer.parseInt(editTextId.getText().toString());
        String varosNev = editTextCity.getText().toString();
        String orszag = editTextCountry.getText().toString();
        int lakossag = Integer.parseInt(editTextPopulation.getText().toString());

  //urlap adatainak lekérése
                //új ember létrehozása
        Varos varos = new Varos(id, varosNev, orszag, lakossag);
        //Gson létrehozása a jsonConverter-hez
        Gson jsonConverter = new Gson();
        //PUT kérés elküldése
        RequestTask task = new RequestTask(url + "/" + id, "PUT", jsonConverter.toJson(varos));
        //kérés elküldése
        task.execute();

    }

    private class RequestTask extends AsyncTask<Void, Void, Response> {
        String requestUrl;
        String requestType;
        String requestParams;

        public RequestTask(String requestUrl, String requestType, String requestParams) {
            this.requestUrl = requestUrl;
            this.requestType = requestType;
            this.requestParams = requestParams;
        }


        //doInBackground metódus létrehozása a kérés elküldéséhez
        @Override
        protected Response doInBackground(Void... voids) {
            Response response = null;
            try {
                switch (requestType) {
                    case "POST":
                        response = RequestHandler.post(requestUrl, requestParams);
                        break;
                }
            } catch (IOException e) {
                Toast.makeText(UpdateActivity.this,
                        e.toString(), Toast.LENGTH_SHORT).show();
            }
            return response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            buttonModify.setEnabled(false);
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            buttonModify.setEnabled(true);
            if (response.getResponseCode() >= 400) {
                Toast.makeText(UpdateActivity.this, "Hiba történt a kérés feldolgozása során", Toast.LENGTH_SHORT).show();
                return;
            }
            if (requestType.equals("PUT")) {
                Toast.makeText(UpdateActivity.this, "Sikeres módosítás", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}