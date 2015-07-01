package com.application.com.application.services;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.application.misc.DBRequestSugg;
import com.application.misc.DBSuggerimento;
import com.application.misc.DBUser;
import com.application.misc.Domains;
import com.application.musictext.InsertObservationsActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucagrazioli on 18/06/15.
 */

/*

 */
public class SuggestService extends AsyncTask<DBRequestSugg, Void, Void> {
    private ProgressDialog progressDialog;
    private Activity activity;

    public SuggestService(ProgressDialog progressDialog, Activity activity) {
        this.progressDialog = progressDialog;
        this.activity = activity;
    }

    @Override
    @SuppressWarnings("deprecation")
    protected Void doInBackground(DBRequestSugg... params) {
        DBRequestSugg dbRequestSugg = params[0];
        String url = Domains.generalDomain+Domains.suggestService;

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        Gson gsonObject = new Gson();

        String dbRequestJson;
        dbRequestJson = gsonObject.toJson(dbRequestSugg);

        Log.d("dbRequestJson", dbRequestJson);

        StringEntity se = null;
        try {
            se = new StringEntity(dbRequestJson);
            Log.d("String entity",se.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        httpPost.setEntity(se);
        httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json");

        DBSuggerimento suggerimento = null;
        try {
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream is = httpEntity.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String totalAnswer="";
            String line = br.readLine();
            while(line != null){
                Log.d("httpAnswer", line);
                totalAnswer += line;
                line = br.readLine();
            }

            Log.d("complete answer", totalAnswer);

            JsonParser parser = new JsonParser();
            JsonObject jsonObject= (JsonObject) parser.parse(totalAnswer);
            //JsonArray jsonElements = (JsonArray) parser.parse(totalAnswer);
            Gson gson = new Gson();
            suggerimento = gson.fromJson(jsonObject, DBSuggerimento.class);


            Log.d("DBSuggerimento", suggerimento.toString());
            final DBSuggerimento finalSuggerimento = suggerimento;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();

                    if(activity instanceof InsertObservationsActivity){
                        InsertObservationsActivity insertObservationsActivity = (InsertObservationsActivity) activity;
                        insertObservationsActivity.startShowSuggestionsActivity(finalSuggerimento);
                    }
                }
            });
        }catch(IOException e){
            e.printStackTrace();
        }

        return null;
    }

}
