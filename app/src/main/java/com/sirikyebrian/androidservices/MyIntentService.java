package com.sirikyebrian.androidservices;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;


/**
 * Created by Sirikye Brian on 7/17/2019.
 * bryanmuloni@gmail.com
 */
public class MyIntentService extends IntentService {
    public static final String TAG = MyIntentService.class.getSimpleName();
    public static final String EXTRA_SLEEP_TIME = "Sleep Time";
    public static final String EXTRA_RECEIVER = "receiver";
    public static final String RESULT_INTENT_SERVICE = "resultIntentService";
    public static final int RESULT_CODE = 18;

    public MyIntentService() {
        super("MyWorkerThread");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate, Thread name " + Thread.currentThread().getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent, Thread name " + Thread.currentThread().getName());

        int sleepTime = intent.getIntExtra(EXTRA_SLEEP_TIME, 1);

        ResultReceiver resultReceiver = intent.getParcelableExtra(EXTRA_RECEIVER);

        int ctr = 1;

//            Dummy long operation
        while (ctr <= sleepTime) {
            Log.i(TAG, "Counter is now " + ctr);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ctr++;
        }

        Bundle bundle = new Bundle();
        bundle.putString(RESULT_INTENT_SERVICE, "Counter stopped at " + ctr + " seconds.");

        resultReceiver.send(RESULT_CODE, bundle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy, Thread name " + Thread.currentThread().getName());
    }
}
