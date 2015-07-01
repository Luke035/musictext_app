package com.application.containers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Spinner;

/**
 * Created by lucagrazioli on 27/05/15.
 */
public class FillUsersSpinnerContainer {
    private Activity activity;
    private ProgressDialog progressDialog;
    private Spinner spinner;

    public FillUsersSpinnerContainer(Activity activity, ProgressDialog progressDialog, Spinner spinner) {
        this.activity = activity;
        this.progressDialog = progressDialog;
        this.spinner = spinner;
    }

    public Activity getActivity() {
        return activity;
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public Spinner getSpinner() {
        return spinner;
    }
}
