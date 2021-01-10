package com.example.answered;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

// Unused - deprecated feature after android 8.0/API 26
public class CallReceiver extends BroadcastReceiver {
    private static final String TAG = "CallReceiver";

    String phoneState;
    String incomingNumber;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.hasExtra(TelephonyManager.EXTRA_STATE)) {
            phoneState = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            if (incomingNumber != null) {
                Toast.makeText(context, incomingNumber, Toast.LENGTH_SHORT).show();
            }
            TelecomManager telecomManager = (TelecomManager)
                    context.getSystemService(Context.TELECOM_SERVICE);
            try {
                if (ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.ANSWER_PHONE_CALLS) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, context.getString(R.string.answerError), Toast.LENGTH_LONG).show();
                }
                // TODO find replacement for deprecated method
                telecomManager.acceptRingingCall();
            } catch (Exception e) {
                Log.v(TAG, e.getMessage());
            }

        }
    }
}
