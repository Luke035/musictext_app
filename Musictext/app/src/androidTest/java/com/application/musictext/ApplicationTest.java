package com.application.musictext;

import android.test.ActivityInstrumentationTestCase2;

import java.io.InputStream;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ActivityInstrumentationTestCase2<SelectArtistsActivity> {
    private SelectArtistsActivity mFirstTestActivity;

    public ApplicationTest() {
        super(SelectArtistsActivity.class);
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mFirstTestActivity = getActivity();

       //Open raw file
       InputStream is = mFirstTestActivity.getResources().openRawResource(R.raw.top_ranked_artists);

    }
}