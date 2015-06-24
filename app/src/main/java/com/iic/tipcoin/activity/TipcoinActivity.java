package com.iic.tipcoin.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

/**
 * Created by ggoldberg on 10/6/15.
 */
public class TipcoinActivity extends ActionBarActivity {

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("FF", "ASDF");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setElevation(0);
        }
    }

}
