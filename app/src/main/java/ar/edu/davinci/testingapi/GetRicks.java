package ar.edu.davinci.testingapi;

import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class Character {
    public String name;
}

class Data {
    public List<Character> results;
}

public class GetRicks extends AsyncTask<String, Integer, String> {
    OkHttpClient client = new OkHttpClient();

    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        String url = strings[0];
        String response = "";
        try {
            response = run(url);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(
                    DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            Data respuesta = objectMapper.readValue(response, Data.class);
            //response = respuesta.results.get(0).name;
            List<Character> personajes = respuesta.results;

            for(int i = 0; i< personajes.size(); i++) {
                Log.i("personajes", personajes.get(i).name);
            }
            response =  "funciona";

            /*JSONObject character = new JSONObject(response);
            JSONArray results = (JSONArray) character.get("results");
            JSONObject character0 = (JSONObject) results.get(3);
            String nameCharacter0 = (String) character0.get("name");
            response = nameCharacter0;*/
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("probando_url", s);

    }
}
