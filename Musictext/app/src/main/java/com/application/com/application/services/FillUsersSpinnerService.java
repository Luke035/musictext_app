package com.application.com.application.services;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.application.containers.FillUsersSpinnerContainer;
import com.application.misc.DBUser;
import com.application.misc.Domains;
import com.application.musictext.InsertObservationsActivity;
import com.application.musictext.SelectArtistsActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucagrazioli on 27/05/15.
 */
public class FillUsersSpinnerService extends AsyncTask<FillUsersSpinnerContainer,Void,Void> {

    @Override
    @SuppressWarnings("deprecation")
    protected Void doInBackground(FillUsersSpinnerContainer... params) {
        final FillUsersSpinnerContainer container = params[0];
        String url = Domains.generalDomain + Domains.listUsersService;

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpPost = new HttpGet(url);


        httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json");
        List<DBUser> userList = null;
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
            JsonArray jsonElements = (JsonArray) parser.parse(totalAnswer);

            userList = parseJson(jsonElements);
        }catch(IOException e){
            e.printStackTrace();
        }

        if(userList!=null){
            final Activity activity = container.getActivity();
            if(activity instanceof SelectArtistsActivity){
                final SelectArtistsActivity callingActivity = (SelectArtistsActivity) activity;
                callingActivity.setUsersList(userList);

            }

            if(activity instanceof InsertObservationsActivity){
                final InsertObservationsActivity callingActivity = (InsertObservationsActivity) activity;
                callingActivity.setUsersList(userList);
            }

            final List<DBUser> finalUserList = userList;

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String [] names = toStringArray(finalUserList);
                    Spinner spinner  = container.getSpinner();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                            android.R.layout.simple_spinner_item, names);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                    container.getProgressDialog().dismiss();
                }
            });
        }

        return null;
    }

    private String[] toStringArray(List<DBUser> toParse){
        String [] toReturn  = new String[toParse.size()];

        for(int i=0; i<toParse.size(); i++){
            toReturn[i] = toParse.get(i).getName();
        }

        return toReturn;
    }

    private List<DBUser> parseJson(JsonArray jsonElements){
        List<DBUser> users = new ArrayList<DBUser>();
        for(int i=0; i<jsonElements.size();i++){
            Log.d("jsonElement",jsonElements.get(i).toString());
            Gson gson = new Gson();
            DBUser user = gson.fromJson(jsonElements.get(i), DBUser.class);
            Log.d("user",user.toString());
            users.add(user);
        }
        return users;
    }
}
