package com.sirikyebrian.androidservices;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Sirikye Brian on 7/19/2019.
 * bryanmuloni@gmail.com
 */
public class MyBoundService extends Service {
    private static final String TAG = "MyBoundService";
    private MyLocalBinder myLocalBinder = new MyLocalBinder();

    public class MyLocalBinder extends Binder {
        MyBoundService getService() {
            return MyBoundService.this;
        }
    }

    public int add(int first, int second) {
        return first + second;
    }

    public int subtract(int first, int second) {
        return first - second;
    }

    public int multiply(int first, int second) {
        return first * second;
    }

    public float divide(int first, int second) {
        return (float) first / (float) second;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: Thread name is:" + Thread.currentThread().getName());
        return myLocalBinder;
    }
}
