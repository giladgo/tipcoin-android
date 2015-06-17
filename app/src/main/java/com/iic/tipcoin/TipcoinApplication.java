package com.iic.tipcoin;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

/**
 * Created by ggoldberg on 10/6/15.
 */
public class TipcoinApplication extends Application {

    private static String PARSE_APPLICATION_ID = "8BhP3CPxuMhtKpSNrsS61XkXYliBccBDqkum5clm";
    private static String PARSE_CLIENT_KEY     = "Ls5bQdEyhIeTbLSooSYICtXmFjOCWhW9QHu70DPM";

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(getApplicationContext());
        Parse.initialize(getApplicationContext(), PARSE_APPLICATION_ID, PARSE_CLIENT_KEY);
        FacebookSdk.sdkInitialize(getApplicationContext());
        ParseFacebookUtils.initialize(getApplicationContext());

    }
}
