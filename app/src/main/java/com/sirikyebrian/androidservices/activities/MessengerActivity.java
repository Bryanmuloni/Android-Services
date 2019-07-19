package com.sirikyebrian.androidservices.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sirikyebrian.androidservices.R;
import com.sirikyebrian.androidservices.services.MyMessengerService;

/**
 * Created by Sirikye Brian on 7/19/2019.
 * bryanmuloni@gmail.com
 */
public class MessengerActivity extends Activity {


    public static final int RESPONSE_CODE = 44;
    boolean mIsBound = false;
    private EditText etFirst;
    private EditText etSecond;

    private TextView tVResult;

    private class IncomingResponseHandler extends Handler {
        @Override
        public void handleMessage(Message messageFromService) {
            switch (messageFromService.what) {
                case RESPONSE_CODE:
                    Bundle bundle = messageFromService.getData();
                    int result = bundle.getInt(MyMessengerService.RESPONSE_EXTRA, 0);

                    tVResult.setText("Result: " + result);
                    break;
                default:
                    super.handleMessage(messageFromService);
            }
        }
    }

    private Messenger incomingMessenger = new Messenger(new IncomingResponseHandler());
    private Messenger mService = null;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            mIsBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIsBound = false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        etFirst = findViewById(R.id.etFirst);
        etSecond = findViewById(R.id.etSecond);
        tVResult = findViewById(R.id.additionResult);
    }

    public void btnAddOperation(View view) {
        int numOne = Integer.valueOf(etFirst.getText().toString());
        int numTwo = Integer.valueOf(etSecond.getText().toString());

        Message messageToService = Message.obtain(null, MyMessengerService.MESSAGE_EXTRA);

        Bundle bundle = new Bundle();
        bundle.putInt(MyMessengerService.NUM_ONE, numOne);
        bundle.putInt(MyMessengerService.NUM_TWO, numTwo);

        messageToService.setData(bundle);
        messageToService.replyTo = incomingMessenger;

        try {
            mService.send(messageToService);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void btnBindService(View view) {
        Intent intent = new Intent(this, MyMessengerService.class);
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }

    public void btnUnbindService(View view) {
        if (mIsBound) {
            unbindService(mConnection);
            mIsBound = false;
        }
    }
}
