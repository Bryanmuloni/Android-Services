package com.sirikyebrian.androidservices.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sirikyebrian.androidservices.services.MyBoundService;
import com.sirikyebrian.androidservices.R;

public class BoundActivity extends AppCompatActivity {
    private static final String TAG = "BoundActivity";
    private EditText firstNumber;
    private EditText secondNumber;

    boolean isBound = false;

    private TextView calculationResultText;

    private MyBoundService myBoundService;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            MyBoundService.MyLocalBinder myLocalBinder = (MyBoundService.MyLocalBinder) iBinder;
            myBoundService = myLocalBinder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bound);
        firstNumber = findViewById(R.id.firstNumber);
        secondNumber = findViewById(R.id.secondNumber);
        calculationResultText = findViewById(R.id.calculationResult);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(BoundActivity.this, MyBoundService.class);
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
       if (isBound){
           unbindService(mConnection);
           isBound = false;
       }
    }

    public void calculateBtn(View view) {
        int numOne = Integer.valueOf(firstNumber.getText().toString());
        int numTwo = Integer.valueOf(secondNumber.getText().toString());

        String resultStr = "";

        if (isBound) {
            switch (view.getId()) {
                case R.id.btnAdd:
                    resultStr = String.valueOf(myBoundService.add(numOne, numTwo));
                    break;
                case R.id.btnSubtract:
                    resultStr = String.valueOf(myBoundService.subtract(numOne, numTwo));
                    break;
                case R.id.btnMultiply:
                    resultStr = String.valueOf(myBoundService.multiply(numOne, numTwo));
                    break;
                case R.id.btnDivide:
                    resultStr = String.valueOf(myBoundService.divide(numOne, numTwo));
                    break;
            }
            calculationResultText.setText(resultStr);
        }
    }
}
