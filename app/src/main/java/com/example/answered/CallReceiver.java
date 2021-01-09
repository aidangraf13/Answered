package com.example.answered;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import androidx.annotation.RequiresApi;

// Unused - deprecated feature after android 8.0/API 26
public class CallReceiver extends BroadcastReceiver {
    String phoneState;
    String incomingNumber;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.hasExtra(TelephonyManager.EXTRA_STATE)) {
            phoneState = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Toast.makeText(context, "It works!", Toast.LENGTH_SHORT).show();
            if (incomingNumber != null) {
                Toast.makeText(context, incomingNumber, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
