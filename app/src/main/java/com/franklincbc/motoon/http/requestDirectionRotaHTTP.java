package com.franklincbc.motoon.http;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Priscila on 20/12/2016.
 */

public class requestDirectionRotaHTTP {

    public static JSONObject requestDirectionRotaHTTP(String url) {

        JSONObject resultado = null;

        // Abre a conexão com o servidor
        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = newClientOkHttp();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        try {
            // Realiza a chamada ao servidor
            response = client.newCall(request).execute();

            // response.body retorna o corpo da resposta, que no nosso caso é JSON
            String json = response.body().string();

            resultado = new JSONObject(json);
            return resultado;

        } catch (UnsupportedEncodingException e) {
            Log.e("ProjetoMapas", e.getMessage());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;

    }


    private static OkHttpClient newClientOkHttp() {
        try {
            OkHttpClient.Builder b = new OkHttpClient.Builder();
            b.readTimeout(15000, TimeUnit.MILLISECONDS);
            b.writeTimeout(15000, TimeUnit.MILLISECONDS);
            return b.build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
