package com.example.varosok;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;

public class InsertActivity extends AppCompatActivity {

    private LinearLayout linearLayoutInsert;
    private EditText editTextCity2;
    private EditText editTextCountry2;
    private EditText editTextPopulation2;
    private  Button buttonInsert;
    private Button buttonBack2;
    private String url = "https://retoolapi.dev/L0giQy/varosok";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        init();

        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ell, hogy minden ki van-e töltve
                  //-ha igen új város felvétel =) toast sikerült
                varosInsert();
                //-ha nem toast nincs kitöltve vagy nem sikerült
            }
        });

        buttonBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InsertActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void init() {
        linearLayoutInsert = findViewById(R.id.linearLayoutInsert);
        editTextCity2 = findViewById(R.id.editTextCity2);
        editTextCountry2 = findViewById(R.id.editTextCountry2);
        editTextPopulation2 = findViewById(R.id.editTextPopulation2);
        buttonInsert = findViewById(R.id.buttonInsert);
        buttonBack2 = findViewById(R.id.buttonBack2);
        }

    public void varosInsert() {
        String varosNev = editTextCity2.getText().toString();
        String orszag = editTextCountry2.getText().toString();
        String lakossag = editTextPopulation2.getText().toString();

        boolean control = inspection();

        if (control){
            Toast.makeText(this, "MezőK kitöltése kötelező", Toast.LENGTH_SHORT).show();
            return;
        }

        int lakossagok = Integer.parseInt(lakossag);
        Varos varos = new Varos(0,varosNev,orszag,lakossagok);  // átnevezés???
        Gson jsonConverter = new Gson();
        RequestTask task = new RequestTask(url, "POST", jsonConverter.toJson(varos));
        task.execute();
    }

    private boolean inspection(){
        if (editTextCity2.getText().toString().isEmpty()||editTextCountry2.getText().toString().isEmpty()||editTextPopulation2.getText().toString().isEmpty())
            return true;
        else
            return false;

    }

    //urlap alaphelyzetbe állítása
    public void urlapAlaphelyzetbe() {
        editTextCity2.setText("");
        editTextCountry2.setText("");
        editTextPopulation2.setText("");
        linearLayoutInsert.setVisibility(View.GONE);
        buttonInsert.setVisibility(View.GONE);
        buttonBack2.setVisibility(View.VISIBLE);
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

        /* h ???? */
        public RequestTask(String requestUrl, String requestType) {
            this.requestUrl = requestUrl;
            this.requestType = requestType;
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
                Toast.makeText(InsertActivity.this,
                        e.toString(), Toast.LENGTH_SHORT).show();
            }
            return response;
        }

        //onPostExecute metódus létrehozása a válasz feldolgozásához
        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            Gson converter = new Gson();
            if (response.getResponseCode() >= 400) {
                Toast.makeText(InsertActivity.this,
                        "Hiba történt a kérés feldolgozása során", Toast.LENGTH_SHORT).show();
                Log.d("onPostExecuteError:", response.getContent());
            }
            if (requestType.equals("POST")) {
                //varosok.add(0, varos, orszag, lakossag);
                urlapAlaphelyzetbe();
                Toast.makeText(InsertActivity.this, "Sikeres hozzáadás", Toast.LENGTH_SHORT).show();
            }
        }
    }
}