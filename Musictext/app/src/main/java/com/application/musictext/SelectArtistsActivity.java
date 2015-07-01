package com.application.musictext;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.application.com.application.services.EditUserService;
import com.application.com.application.services.FillUsersSpinnerService;
import com.application.containers.FillUsersSpinnerContainer;
import com.application.misc.DBArtist;
import com.application.misc.DBUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class SelectArtistsActivity extends ActionBarActivity {
    List<DBUser> usersList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null)
            actionBar.setBackgroundDrawable(new ColorDrawable(0xff4caf50));

        setContentView(R.layout.select_artists_activity);

        InputStream is = this.getResources().openRawResource(R.raw.top_ranked_artists);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        LinearLayout layout = (LinearLayout) this.findViewById(R.id.artistsScrollLayout);
        final ArrayList<CheckBox> boxes = new ArrayList<CheckBox>();
        final ArrayList<DBArtist> artistList = new ArrayList<DBArtist>();

        /*
          final ArrayList<CheckBox> boxes = new ArrayList<CheckBox>();
         for(int i=0; i<nodes.size(); i++){
            CheckBox box = new CheckBox(this);
            box.setText(nodes.get(i));
            linearLayout.addView(box);
            boxes.add(box);
        }
         */

        try {
            reader.readLine(); //Riga di header
            String line = reader.readLine();
            while(line!=null){
                String [] splittedLine = line.split(",");
                String artist = splittedLine[2].replace('"','\0');

                CheckBox box = new CheckBox(this);
                box.setText(artist);
                box.setTextSize(20);
                layout.addView(box);
                boxes.add(box);

                DBArtist toAdd = new DBArtist(Integer.parseInt(splittedLine[0]),artist,Integer.parseInt(splittedLine[1]));

                artistList.add(toAdd);

                Log.d("Added artist",toAdd.toString());

                line = reader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        final Spinner usersSpinner = (Spinner) this.findViewById(R.id.usersSpinner);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(this.getString(R.string.waiting));
        progressDialog.show();
        FillUsersSpinnerContainer container = new FillUsersSpinnerContainer(this,progressDialog,usersSpinner);
        FillUsersSpinnerService fillUsersSpinnerService = new FillUsersSpinnerService();
        fillUsersSpinnerService.execute(container);

        //final DBUser user = new DBUser(2, null, 19, "m");

        Button confirmSelectionButton = (Button) this.findViewById(R.id.confirmArtistsSelectionButton);
        confirmSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBUser user = usersList.get(usersSpinner.getSelectedItemPosition());

                //DBUser userProva = new DBUser(user.getId(), null, 19, "m");

                Log.d("Selected user",user.toString());

                List<DBArtist> selectedArtists = getSelectedArtists(boxes,artistList);
                user.setArtists(selectedArtists);

                for(DBArtist dbArtist:selectedArtists){
                    Log.d("Selected artist",dbArtist.toString());
                }

                ProgressDialog progressDialog = new ProgressDialog(SelectArtistsActivity.this);
                progressDialog.setMessage(SelectArtistsActivity.this.getString(R.string.waiting));
                progressDialog.show();

                EditUserService editUserService = new EditUserService(SelectArtistsActivity.this,progressDialog);
                editUserService.execute(user);
            }
        });

        //List<DBArtist> artists = new ArrayList<DBArtist>();
       // user.addArtists(new DBArtist(668));

        //EditUserService editUserService = new EditUserService(this);
        //editUserService.execute(user);



    }

    public List<DBArtist> getSelectedArtists(List<CheckBox> boxes, List<DBArtist> artistList){
        ArrayList<DBArtist> selectedArtists = new ArrayList<DBArtist>();

       for(int i=0; i<boxes.size(); i++){
           CheckBox box = boxes.get(i);

           if(box.isChecked()){
               selectedArtists.add(artistList.get(i));
           }
       }

        return selectedArtists;
    }

    public void setUsersList(List<DBUser> usersList) {
        this.usersList = usersList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_artists, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
