package com.iic.tipcoin;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


/**
 * A placeholder fragment containing a simple view.
 */
public class SplashFragment extends Fragment {

    private static final String LOG_TAG = "SplashFragment";
    private ValueAnimator mBackgroundAnimator;

    public SplashFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_splash, container, false);
        view.setBackground(new TranslatableBitmapDrawable(getResources(),
                BitmapFactory.decodeResource(getResources(), R.drawable.bg_tile)
        ));
        setupAnimator(view);
        refreshLogin(view);

        return view;
    }

    private void loggedIn(ParseUser user) {
        Log.d(LOG_TAG, "We're in! " + user.getUsername());
        View view = getView();
        if (view != null) {
            final Button loginButton = (Button) view.findViewById(R.id.login_button);
            loginButton.setVisibility(View.GONE);
        }
    }

    private void refreshLogin() {
        refreshLogin(getView());
    }

    private void refreshLogin(final View view) {

        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser == null) {
            Log.d(LOG_TAG, "Not found before fetch");
            showLoginButton(view);
        } else {
            mBackgroundAnimator.start();
            currentUser.fetchInBackground(new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser parseObject, ParseException e) {
                    mBackgroundAnimator.cancel();
                    if (parseObject == null) {
                        Log.d(LOG_TAG, "Not found after fetch");
                        showLoginButton(view);
                    } else {
                        Log.d(LOG_TAG, "Found after fetch");
                        loggedIn(parseObject);
                    }
                }
            });
        }
    }

    private void showLoginButton(View view) {
        final Button loginButton = (Button) view.findViewById(R.id.login_button);
        loginButton.setVisibility(View.VISIBLE);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doLogin();
            }
        });
    }

    private void doLogin() {
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, Collections.singletonList("public_profile"), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    Log.e(LOG_TAG, "Error", err);
                    Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                } else {
                    refreshLogin();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);

    }

    private void setupAnimator(View view) {
        final TranslatableBitmapDrawable drawable = (TranslatableBitmapDrawable)view.getBackground();
        mBackgroundAnimator = ValueAnimator.ofInt(0, drawable.getBitmap().getWidth());
        mBackgroundAnimator.setDuration(2000);
        mBackgroundAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                drawable.setTranslateX(value);
            }
        });
        mBackgroundAnimator.setInterpolator(new LinearInterpolator());
        mBackgroundAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mBackgroundAnimator.setRepeatMode(ValueAnimator.RESTART);

    }

}
