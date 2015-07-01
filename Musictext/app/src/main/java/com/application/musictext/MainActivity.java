package com.application.musictext;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by lucagrazioli on 23/05/15.
 */
public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null)
            actionBar.setBackgroundDrawable(new ColorDrawable(0xff4caf50));

        setContentView(R.layout.main_menu_layout);

        Button modifyUser = (Button) this.findViewById(R.id.modifyUserButton);
        modifyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSelectArtistActivity();
            }
        });

        Button createUserButton = (Button) this.findViewById(R.id.createUserButton);
        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCreateUserActivity();
            }
        });

        Button startSuggestButton = (Button) this.findViewById(R.id.suggestButton);
        startSuggestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startInsertObservationActivity();
            }
        });
    }

    private void startSelectArtistActivity(){
            Intent intent  = new Intent(this, SelectArtistsActivity.class);
            /*String pkg = this.getPackageName();
            intent.putExtra(pkg+".netName", netName);
            intent.putExtra(pkg+".evidenceNodes", nodes);
            intent.putExtra(pkg+".threshold",threshold);*/
            this.startActivity(intent);
    }

    private void startInsertObservationActivity(){
        Intent intent  = new Intent(this, InsertObservationsActivity.class);
            /*String pkg = this.getPackageName();
            intent.putExtra(pkg+".netName", netName);
            intent.putExtra(pkg+".evidenceNodes", nodes);
            intent.putExtra(pkg+".threshold",threshold);*/
        this.startActivity(intent);
    }

    private void startCreateUserActivity(){
        Intent intent  = new Intent(this, CreateUserActivity.class);
            /*String pkg = this.getPackageName();
            intent.putExtra(pkg+".netName", netName);
            intent.putExtra(pkg+".evidenceNodes", nodes);
            intent.putExtra(pkg+".threshold",threshold);*/
        this.startActivity(intent);
    }
}
