package com.application.musictext;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
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
import java.util.Collections;
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
                    int matchedTags = 0;
                    for(int t:tags){
                        matchedTags = 0;
                        if(splittedLine[4].contains(""+t)){
                            //Traccia da suggerire
                            matchedTags++;
                            //trackList.add(new Track(splittedLine[3],splittedLine[1]));
                            //break;
                        }
                    }
                    if(matchedTags>0) {
                        double rank = (double) matchedTags/tags.length;
                        Log.d("Rank", "Matched tags: "+matchedTags+" Overall tags: "+tags.length+" Rank: "+rank);
                        trackList.add(new Track(splittedLine[3], splittedLine[1],rank));
                    }
                }

                line = reader.readLine();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ListView suggestList = (ListView) this.findViewById(R.id.suggest_list);
        List<String> valuesPrint = new ArrayList<String>();
        List<Track> sortedTracks = sortTracksByRank(trackList);
        for(Track track:sortedTracks){
            valuesPrint.add("Nome: "+track.getName()+"\n Artist: "+track.getArtist()+"\n Rank: "+track.getRank());
        }
        setListView(valuesPrint,suggestList);
    }

    private List<Track> sortTracksByRank(List<Track> tracks){
        List<Track> sortedTrack = new ArrayList<Track>();

        Track min_track = new Track("","",Double.MIN_VALUE);
        Track max = min_track;
        int max_index = -1;
        while(tracks.size() > 0) {
            for (int i = 0; i < tracks.size(); i++) {
                if (tracks.get(i).getRank() > max.getRank()) {
                    max = tracks.get(i);
                    max_index = i;
                }
            }
            sortedTrack.add(max);
            tracks.remove(max_index);
            max = min_track;
        }

        return sortedTrack;
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
