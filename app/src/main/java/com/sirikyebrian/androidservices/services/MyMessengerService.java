package com.sirikyebrian.androidservices.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;

import com.sirikyebrian.androidservices.activities.MessengerActivity;


/**
 * Created by Sirikye Brian on 7/19/2019.
 * bryanmuloni@gmail.com
 */
public class MyMessengerService extends Service {
    public static final String RESPONSE_EXTRA = "response_result";
    public static final int MESSAGE_EXTRA = 43;
    public static final String NUM_ONE = "numOne";
    public static final String NUM_TWO = "numTwo";

    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_EXTRA:
                    Bundle bundle = msg.getData();
                    int num1 = bundle.getInt(NUM_ONE, 0);
                    int num2 = bundle.getInt(NUM_TWO, 0);
                    int result = addNumbers(num1, num2);

                    Toast.makeText(MyMessengerService.this, "Result : " + result, Toast.LENGTH_SHORT).show();


//                    Send data back to the activity [Result]
                    Messenger incomingMessenger = msg.replyTo;
                    Message messageToActivity = Message.obtain(null, MessengerActivity.RESPONSE_CODE);

                    Bundle bundleToActivity = new Bundle();
                    bundleToActivity.putInt(RESPONSE_EXTRA, result);

                    messageToActivity.setData(bundleToActivity);

                    try {
                        incomingMessenger.send(messageToActivity);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    Messenger mMessenger = new Messenger(new IncomingHandler());

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    public int addNumbers(int a, int b) {
        return a + b;
    }
}
