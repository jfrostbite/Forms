package com.e_eduspace.forms.module.start;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.e_eduspace.forms.module.guide.GuideActivity;
import com.e_eduspace.forms.utils.KUtils;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(KUtils.getApp(), GuideActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);
        timer.purge();
    }
}
