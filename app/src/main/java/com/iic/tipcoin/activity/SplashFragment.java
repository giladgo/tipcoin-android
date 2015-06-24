package com.iic.tipcoin.activity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

import com.iic.tipcoin.R;
import com.iic.tipcoin.utils.TranslatableBitmapDrawable;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.Collections;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


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
        ButterKnife.inject(this, view);
        view.setBackground(new TranslatableBitmapDrawable(getResources(),
                BitmapFactory.decodeResource(getResources(), R.drawable.bg_tile)
        ));
        setupAnimator(view);
        refreshLogin(view);

        return view;
    }

    @InjectView(R.id.login_button)
    Button mLoginButton;

    private void loggedIn(ParseUser user) {
        Intent intent = new Intent(getActivity(), Groups.class);
        intent.putExtra("user", user.getObjectId());
        startActivity(intent);

        Log.d(LOG_TAG, "We're in! " + user.getUsername());
    }

    private void refreshLogin() {
        refreshLogin(getView());
    }

    private void refreshLogin(final View view) {

        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser == null) {
            Log.d(LOG_TAG, "Not found before fetch");
            showLoginButton();
        } else {
            mBackgroundAnimator.start();
            currentUser.fetchInBackground(new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser parseObject, ParseException e) {
                    mBackgroundAnimator.cancel();
                    if (parseObject == null) {
                        Log.d(LOG_TAG, "Not found after fetch");
                        showLoginButton();
                    } else {
                        Log.d(LOG_TAG, "Found after fetch");
                        loggedIn(parseObject);
                    }
                }
            });
        }
    }

    private void showLoginButton() {
        mLoginButton.setVisibility(View.VISIBLE);
    }

    private void hideLoginButton() {
        mLoginButton.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.login_button)
    @SuppressWarnings("unused")
    void doLogin() {
        mBackgroundAnimator.start();
        hideLoginButton();
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, Collections.singletonList("public_profile"), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                mBackgroundAnimator.cancel();
                if (user == null) {
                    Log.d(LOG_TAG, "Uh oh. The user cancelled the Facebook login.");
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
