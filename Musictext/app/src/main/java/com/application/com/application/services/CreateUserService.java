package com.application.com.application.services;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.application.misc.Domains;
import com.application.musictext.MainActivity;
import com.application.musictext.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by lucagrazioli on 23/05/15.
 */
public class CreateUserService extends AsyncTask<String,Void,Void> {
    private Activity activity;
    private ProgressDialog progressDialog;

    public CreateUserService(Activity activity, ProgressDialog progressDialog) {
        this.activity = activity;
        this.progressDialog = progressDialog;
    }


    @Override
    @SuppressWarnings("deprecation")
    protected Void doInBackground(String... users_names) {
        String userName = users_names[0];
        String url = Domains.generalDomain+Domains.userService+"?"+Domains.userNameParam+"="+userName;

        HttpClient httpClient = new DefaultHttpClient();
        HttpPut httpPut = new HttpPut(url);


        httpPut.setHeader(HTTP.CONTENT_TYPE, "application/json");
        try{
            HttpResponse httpResponse = httpClient.execute(httpPut);
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
                    if(response==201) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle(activity.getString(R.string.user_created));
                        builder.setMessage(activity.getString(R.string.user_created_text));
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                startMainActivty();
                            }
                        });
                        if (progressDialog != null)
                            progressDialog.dismiss();

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }

                    if(response==400){
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle(activity.getString(R.string.user_not_created));
                        builder.setMessage(activity.getString(R.string.user_name_short));
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        if (progressDialog != null)
                            progressDialog.dismiss();

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }

                    if(response==500){
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle(activity.getString(R.string.user_not_created));
                        builder.setMessage(activity.getString(R.string.internal_server_error));
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        if (progressDialog != null)
                            progressDialog.dismiss();

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                }
            });
        }catch(IOException e){
            e.printStackTrace();
        }


        return null;
    }

    private void startMainActivty(){
        Intent intent = new Intent(activity, MainActivity.class);

        activity.startActivity(intent);
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
