package com.application.com.application.services;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.application.misc.DBUser;
import com.application.misc.Domains;
import com.application.musictext.R;
import com.google.gson.Gson;

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

/**
 * Created by lucagrazioli on 12/05/15.
 */
public class EditUserService extends AsyncTask<DBUser,Void,Void> {
    private Activity activity;
    private ProgressDialog progressDialog;

    public EditUserService(Activity activity, ProgressDialog progressDialog) {
        this.activity = activity;
        this.progressDialog = progressDialog;
    }

    public EditUserService(Activity activity) {
        this.activity = activity;
    }

    @Override
    @SuppressWarnings("deprecation")
    protected Void doInBackground(DBUser... users) {
        DBUser user = users[0];
        String url = Domains.generalDomain+Domains.userService;
        final ArrayList<String> nodes = new ArrayList<String>();

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        Gson gsonObject = new Gson();

        String userJSON;
        userJSON = gsonObject.toJson(user);

        Log.d("userJson",userJSON);

        StringEntity se = null;
        try {
            se = new StringEntity(userJSON);
            Log.d("String entity",se.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        httpPost.setEntity(se);
        httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json");
        try{
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();


            final int response = httpResponse.getStatusLine().getStatusCode();

            InputStream is = httpEntity.getContent();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

            String line = bufferedReader.readLine();
            while(line!=null){
                Log.d("Response - ", line);
                line=bufferedReader.readLine();
            }


            Log.d("Status code",""+response);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle(activity.getString(R.string.user_modified));
                    if(response==200)
                        builder.setMessage(activity.getString(R.string.user_modified_text));
                    if (response>=400 && response<500)
                        builder.setMessage("Error 400");
                    if(response>=500)
                        builder.setMessage("Error 500");
                    builder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                          dialogInterface.dismiss();
                        }
                    });
                    if(progressDialog!=null)
                        progressDialog.dismiss();

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
        }catch(IOException e){
            e.printStackTrace();
        }


        return null;
    }

    private void setListView(ArrayList<String> values, Activity activity, ListView listView){
        String [] valuesArray = toStringArray(values);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                android.R.layout.simple_list_item_1, valuesArray);
        listView.setAdapter(adapter);
    }

    private String [] toStringArray(ArrayList<String> list){
        String [] toReturn = new String[list.size()];
        for(int i=0; i<list.size(); i++){
            toReturn[i] = list.get(i);
        }
        return toReturn;
    }
}

