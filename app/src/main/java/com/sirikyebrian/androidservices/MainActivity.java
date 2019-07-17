package com.sirikyebrian.androidservices;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startService(View view) {
        Intent intent = new Intent(this, MyStartedService.class);
        intent.putExtra(MyStartedService.EXTRA_SLEEP_TIME, 10);
        startService(intent);
    }

    public void stopService(View view) {
        Intent intent = new Intent(this, MyStartedService.class);
        stopService(intent);
    }

    public void startIntentService(View view) {
        Intent intent = new Intent(this, MyIntentService.class);
        intent.putExtra(MyIntentService.EXTRA_SLEEP_TIME, 10);
        startService(intent);
    }
}
