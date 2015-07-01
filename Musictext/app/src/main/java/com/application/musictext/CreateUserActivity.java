package com.application.musictext;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.application.com.application.services.CreateUserService;

/**
 * Created by lucagrazioli on 23/05/15.
 */
public class CreateUserActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null)
            actionBar.setBackgroundDrawable(new ColorDrawable(0xff4caf50));

        setContentView(R.layout.create_user_layout);

        final EditText insertedUser = (EditText) this.findViewById(R.id.insertedUserName);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(this.getString(R.string.waiting));

        Button confirmUser = (Button) this.findViewById(R.id.confirmUserButton);
        confirmUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String name = insertedUser.getText().toString();

                CreateUserService backgroundTask = new CreateUserService(CreateUserActivity.this,progressDialog);
                backgroundTask.execute(name);
            }
        });
    }
}
