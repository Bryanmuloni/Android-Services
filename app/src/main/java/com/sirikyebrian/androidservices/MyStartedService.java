package com.sirikyebrian.androidservices;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Sirikye Brian on 7/17/2019.
 * bryanmuloni@gmail.com
 */
public class MyStartedService extends Service {
    public static final String TAG = MyStartedService.class.getSimpleName();
    public static final String EXTRA_SLEEP_TIME = "Sleep Time";
    public static final String STARTED_SERVICE_RESULT = "startedServiceResult";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate, Thread name " + Thread.currentThread().getName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand, Thread name " + Thread.currentThread().getName());

//        Tasks are performed here in onStartCommand
//        Short duration tasks only to avoid blocking the UI
        int sleepTime = intent.getIntExtra(EXTRA_SLEEP_TIME, 1);
        new MyAsyncTask().execute(sleepTime);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind, Thread name " + Thread.currentThread().getName());
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy, Thread name " + Thread.currentThread().getName());
    }

    private class MyAsyncTask extends AsyncTask<Integer, String, String> {

        private final String TAG = MyAsyncTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(TAG, "onPreExecute, Thread name " + Thread.currentThread().getName());
        }

        @Override
        protected String doInBackground(Integer... voids) {
            Log.i(TAG, "doInBackground, Thread name " + Thread.currentThread().getName());

            int sleepTime = voids[0];
            int ctr = 1;

//            Dummy long operation
            while (ctr <= sleepTime){
                publishProgress("Counter is now " + ctr);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ctr++;
            }
            return "Counter Stopped at " + ctr + " seconds";
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(MyStartedService.this, values[0], Toast.LENGTH_SHORT).show();
            Log.i(TAG,
                    "Counter Value " + values[0]+
                            " onProgressUpdate, Thread name "
                            + Thread.currentThread().getName());
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            Destroy service from within the service class itself
            stopSelf();
            Log.i(TAG, "onPostExecute, Thread name " + Thread.currentThread().getName());

            Intent intent = new Intent("action.service.to.activity");
            intent.putExtra(STARTED_SERVICE_RESULT, result);
            sendBroadcast(intent);
        }
    }
}
