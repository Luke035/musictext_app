package com.application.musictext;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.application.misc.Track;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucagrazioli on 18/06/15.
 */
public class ShowSuggestionsActivity extends ActionBarActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null)
            actionBar.setBackgroundDrawable(new ColorDrawable(0xff4caf50));

        setContentView(R.layout.show_suggestions_layout);

        Intent intent = this.getIntent();
        String pkg = this.getPackageName();

        Button main_menu = (Button) this.findViewById(R.id.back_main_menu);
        main_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMainActivity();
            }
        });

        int[] tags = (int[]) intent.getSerializableExtra(pkg+".tags");

        InputStream is = this.getResources().openRawResource(R.raw.track_artist_tags);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        List<Track> trackList = new ArrayList<Track>();
        try {
            String line = reader.readLine(); //Riga di header

            line = reader.readLine();
            while(line !=null){
                String [] splittedLine = line.split(";");

                if(splittedLine[4].length()>=1) {
                    for(int t:tags){
                        if(splittedLine[4].contains(""+t)){
                            //Traccia da suggerire
                            trackList.add(new Track(splittedLine[3],splittedLine[1]));
                            break;
                        }
                    }
                }

                line = reader.readLine();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ListView suggestList = (ListView) this.findViewById(R.id.suggest_list);
        List<String> valuesPrint = new ArrayList<String>();
        for(Track track:trackList){
            valuesPrint.add("Nome: "+track.getName()+"\n Artist: "+track.getArtist());
        }
        setListView(valuesPrint,suggestList);
    }

    private void setListView(List<String> values, ListView listView){
        String [] valuesArray = toStringArray(values);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, valuesArray);
        listView.setAdapter(adapter);
    }

    private String [] toStringArray(List<String> list){
        String [] toReturn = new String[list.size()];
        for(int i=0; i<list.size(); i++){
            toReturn[i] = list.get(i);
        }
        return toReturn;
    }

    private void startMainActivity(){
        Intent intent  = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }
}
