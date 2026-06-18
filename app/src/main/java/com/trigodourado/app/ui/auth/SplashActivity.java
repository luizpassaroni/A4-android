package com.trigodourado.app.ui.auth;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.trigodourado.app.util.SessionNavigator;

@SuppressLint("CustomSplashScreen")
public final class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        SessionNavigator.openResolvedDestination(this);
    }
}
