package com.sirikyebrian.androidservices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView startedServiceResult;
    private TextView intentServiceResult;

    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startedServiceResult = findViewById(R.id.startedServiceResult);
        intentServiceResult = findViewById(R.id.intentServiceResult);
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

        ResultReceiver myResultReceiver = new MyResultReceiver(null);

        Intent intent = new Intent(this, MyIntentService.class);
        intent.putExtra(MyIntentService.EXTRA_SLEEP_TIME, 10);
        intent.putExtra(MyIntentService.EXTRA_RECEIVER, myResultReceiver);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.service.to.activity");
        registerReceiver(myStartedServiceReceiver, intentFilter);
    }

    private BroadcastReceiver myStartedServiceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(MyStartedService.STARTED_SERVICE_RESULT)) {
                String result = intent.getStringExtra(MyStartedService.STARTED_SERVICE_RESULT);
                startedServiceResult.setText(result);
                startedServiceResult.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myStartedServiceReceiver);
    }

    public void openBoundService(View view) {
        startActivity(new Intent(this, SecondActivity.class));
    }

    //    To receive the data back from MyIntentService.java using ResultReceiver
    private class MyResultReceiver extends ResultReceiver {

        public MyResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);

            Log.i("MyResultReceiver", Thread.currentThread().getName());

            if (resultCode == MyIntentService.RESULT_CODE && resultData != null) {
                final String result = resultData.getString(MyIntentService.RESULT_INTENT_SERVICE);

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("MyHandler", Thread.currentThread().getName());
                        if (!result.isEmpty()) {
                            intentServiceResult.setText(result);
                            intentServiceResult.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(MainActivity.this, "Result is null", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }
}
