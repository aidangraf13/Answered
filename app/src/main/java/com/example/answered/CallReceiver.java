package com.example.answered;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

/**
 * This class intercepts incoming calls, determines their number, and answers calls that fit
 * the criteria the user set in the settings.
 */
public class CallReceiver extends BroadcastReceiver {
    private static final String TAG = "CallReceiver";
    private static final String DEFAULT = "default";
    String incomingNumber;

    /**
     * This method is triggered when a call is received. It answers the call if it meets the
     * criteria set in settings.
     * @param context used for toast messages, a telecom manager reference, and checking permissions
     * @param intent used to get the phone number and phone state
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.hasExtra(TelephonyManager.EXTRA_STATE)) {
            // TODO find replacement for deprecated method
            incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            if (incomingNumber != null) {
                Toast.makeText(context, incomingNumber, Toast.LENGTH_SHORT).show();
            }
            TelecomManager telecomManager = (TelecomManager)
                    context.getSystemService(Context.TELECOM_SERVICE);
            try {
                if (ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.ANSWER_PHONE_CALLS) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context,
                            context.getString(R.string.answerError), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                // TODO Add appropriate error handling
                Log.v(TAG, e.getMessage());
            }
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            if (incomingNumber != null && preferences.getBoolean(context.getString(R.string.answerAll), false)) {
                // Adds delay to call
                addDelay(context);
                // TODO find replacement for deprecated method
                telecomManager.acceptRingingCall();
            } else {
                String wantedNumber = preferences.getString(context.getString(R.string.phoneKey), DEFAULT);
                Log.i(TAG, "Incoming number: " + incomingNumber);
                Log.i(TAG, "Wanted number: " + wantedNumber);
                if (!(wantedNumber.equals(DEFAULT)) && wantedNumber.equals(incomingNumber)) {
                    // Adds delay to call
                    addDelay(context);
                    // TODO find replacement for deprecated method
                    telecomManager.acceptRingingCall();
                }
            }

        }
    }

    /**
     * This method when called adds delay depending on the user preference
     * @param context used to get the preference reference and string resources
     */
    private void addDelay(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String delay = preferences.getString(context.getString(R.string.delayKey), DEFAULT);
        if (!(delay.equals(DEFAULT))) {
            int delayVal = Integer.parseInt(delay);
            try {
                Log.i(TAG, "Adding delay to call: " + delayVal);
                Thread.sleep((delayVal * 1000L));
            } catch (InterruptedException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }
}
