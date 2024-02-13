package com.example.varosok;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ListActivity extends AppCompatActivity {
    private List<Varos> varosok = new ArrayList<>();
    private ListView listView;
    private Button buttonBack;
    private String url = "https://retoolapi.dev/L0giQy/varosok";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        init();
        // törlés törölje
        //módosításra nyomva menjen az UpdateAct.

        /*RequestTask task = new RequestTask(url, "GET");
        task.execute();*/

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void init() {
        listView = findViewById(R.id.listView);
        buttonBack = findViewById(R.id.buttonBack);
        //listView.setAdapter(new MenuAdapter());
    }

    private class MenuAdapter extends ArrayAdapter<Varos> {

        public MenuAdapter() {
            super(ListActivity.this, R.layout.varos_list_items, varosok);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //inflater létrehozása
            LayoutInflater inflater = getLayoutInflater();
            //view létrehozása a varos_list_items.xml-ből
            View view = inflater.inflate(R.layout.varos_list_items, null, false);
            //varos_list_items.xml-ben lévő elemek inicializálása
            TextView textViewCity = view.findViewById(R.id.textViewCity);
            TextView textViewCountry = view.findViewById(R.id.textViewCountry);
            TextView textViewPopulation = view.findViewById(R.id.textViewPopulation);
            //actualVaros étrehozása a varosok listából
            Varos actualVaros = varosok.get(position);

            textViewCity.setText(actualVaros.getVaros());
            textViewCountry.setText(actualVaros.getOrszag());
            textViewPopulation.setText(String.valueOf(actualVaros.getLakossag()));

            return view;
        }
/*
        Varos varos = new Varos(0,varos,orszag,lakossag);
        Gson jsonConverter = new Gson();
        ListActivity.RequestTask task = new ListActivity.RequestTask(url, "POST", jsonConverter.toJson(varos));
        task.execute();
*/
    /*
    public void varosModositas() {
        //urlap adatainak lekérése
        int id = Integer.parseInt(editTextId.getText().toString());
        String varos = editTextCity.getText().toString();
        String orszag = editTextCountry.getText().toString();
        int lakossag = Integer.parseInt(editTextPopulation.getText().toString());
        //új ember létrehozása
        Varos varos = new Varos(id, varos, orszag, lakossag);
        //Gson létrehozása a jsonConverter-hez
        Gson jsonConverter = new Gson();
        //PUT kérés elküldése
        RequestTask task = new RequestTask(url + "/" + id, "PUT", jsonConverter.toJson(person));
        //kérés elküldése
        task.execute(); */
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
                    case "GET":
                        response = RequestHandler.get(requestUrl);
                        break;
                    case "PUT":
                        response = RequestHandler.put(requestUrl, requestParams);
                        break;
                    case "DELETE":
                        response = RequestHandler.delete(requestUrl + "/" + requestParams);
                        break;
                }
            } catch (IOException e) {
                Toast.makeText(ListActivity.this,
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
                Toast.makeText(ListActivity.this,
                        "Hiba történt a kérés feldolgozása során", Toast.LENGTH_SHORT).show();
                Log.d("onPostExecuteError:", response.getContent());
            }
            switch (requestType) {
                case "GET":
                    Varos[] peopleArray = converter.fromJson(
                            response.getContent(), Varos[].class);
                    //lista frissítése a GET válaszban kapott elemekkel
                    varosok.clear();
                    varosok.addAll(Arrays.asList(peopleArray));
                    Toast.makeText(ListActivity.this, "Sikeres adatlekérdezés", Toast.LENGTH_SHORT).show();
                    break;
                case "DELETE":
                    int id = Integer.parseInt(requestParams);
                    //lista frissítése a törölt elem nélkül
                    varosok.removeIf(person1 -> person1.getId() == id);
                    Toast.makeText(ListActivity.this, "Sikeres törlés", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
