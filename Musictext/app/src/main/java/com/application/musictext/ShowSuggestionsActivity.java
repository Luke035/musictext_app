package com.application.musictext;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

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
    private String [] maxArtistsNumber = {"3","5","8","10","15"};
    private String [] maxTracksNumber = {"3","5","8","10","15"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null)
            actionBar.setBackgroundDrawable(new ColorDrawable(0xff4caf50));

        setContentView(R.layout.show_suggestions_layout);

        final List<Track> trackList = new ArrayList<Track>();
        Intent intent = this.getIntent();
        String pkg = this.getPackageName();

        final ListView suggestList = (ListView) this.findViewById(R.id.suggest_list);
        final Spinner maxTracks = (Spinner) this.findViewById(R.id.max_tracks_spinner);
        final Spinner maxArtists = (Spinner) this.findViewById(R.id.max_artists_spinner);
        setSpinners(maxArtists,maxTracks);

        maxTracks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ProgressDialog progressDialog = new ProgressDialog(ShowSuggestionsActivity.this);
                progressDialog.setMessage(getApplicationContext().getString(R.string.waiting));
                progressDialog.show();

                int selectedMaxTracks = Integer.parseInt(maxTracks.getSelectedItem().toString());
                int selectedMaxArtists = Integer.parseInt(maxArtists.getSelectedItem().toString());

                Log.d("ArtistsListener", "Called, maxTracks:"+selectedMaxTracks+" maxArtists:"+selectedMaxArtists+" trackList:"+trackList.size());

                List<Track> filteredTracks = filterSuggestion(trackList, selectedMaxArtists, selectedMaxTracks);
                List<String> valuesPrintTracks = new ArrayList<String>();

                for (Track track : filteredTracks) {
                    valuesPrintTracks.add("Nome: " + track.getName() + "\n Artist: " + track.getArtist());
                }
                setListView(valuesPrintTracks, suggestList);

                progressDialog.dismiss();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        maxArtists.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ProgressDialog progressDialog = new ProgressDialog(ShowSuggestionsActivity.this);
                progressDialog.setMessage(getApplicationContext().getString(R.string.waiting));
                progressDialog.show();

                int selectedMaxTracks = Integer.parseInt(maxTracks.getSelectedItem().toString());
                int selectedMaxArtists = Integer.parseInt(maxArtists.getSelectedItem().toString());

                Log.d("ArtistsListener", "Called, maxTracks:"+selectedMaxTracks+" maxArtists:"+selectedMaxArtists+" trackList:"+trackList.size());

                List<Track> filteredTracks = filterSuggestion(trackList, selectedMaxArtists, selectedMaxTracks);
                List<String> valuesPrintArtists = new ArrayList<String>();

                for (Track track : filteredTracks) {
                    valuesPrintArtists.add("Nome: " + track.getName() + "\n Artist: " + track.getArtist());
                }
                setListView(valuesPrintArtists, suggestList);

                progressDialog.dismiss();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
                        //Log.d("Rank", "Matched tags: "+matchedTags+" Overall tags: "+tags.length+" Rank: "+rank);
                        trackList.add(new Track(splittedLine[3], splittedLine[1],rank));
                    }
                }

                line = reader.readLine();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        List<String> valuesPrint = new ArrayList<String>();
        //List<Track> sortedTracks = sortTracksByRank(trackList);
        for(Track track:trackList){
            valuesPrint.add("Nome: "+track.getName()+"\n Artist: "+track.getArtist());
        }
        Log.d("Dimensions","valuesPrint: "+valuesPrint.size()+" sortedTracks: "+trackList.size());
        setListView(valuesPrint, suggestList);
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
        Log.d("Total suggestion", ""+values.size());
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

    private void setSpinners(Spinner maxArtists, Spinner maxTracks){

        //Spinner spinner  = container.getSpinner();
        ArrayAdapter<String> artistsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, maxArtistsNumber);
        artistsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        maxArtists.setAdapter(artistsAdapter);

        ArrayAdapter<String> tracksAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, maxArtistsNumber);
        artistsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        maxTracks.setAdapter(tracksAdapter);
    }

    private List<Track> filterSuggestion(List<Track> trackList, int maxArtists, int maxTracks){
        List<Track> filteredTracks = new ArrayList<Track>();
        int temp_tracks = 0;
        int temp_artists = 0;

        for(int i=0; i<trackList.size(); i++){
            if(temp_tracks < maxTracks && temp_artists < maxArtists){
                filteredTracks.add(trackList.get(i));
                temp_tracks++;
            }
            if(i>0){
                if(!(trackList.get(i-1).getArtist().equals(trackList.get(i).getArtist()))){
                    temp_tracks = 0;
                    temp_artists++;
                }
            }

        }

        return filteredTracks;
    }

    private void startMainActivity(){
        Intent intent  = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }
}
