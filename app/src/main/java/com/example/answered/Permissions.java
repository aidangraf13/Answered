package com.example.answered;

import android.app.Activity;
import android.content.Context;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

/**
 * This class is used to check and ask for required permissions by the app. One must call
 * check to add required permissions to check before calling request.
 */
public class Permissions {
    private static final ArrayList<String> listPermissionsNeeded;
    private static final int REQUEST_CODE_CALL = 42;

    static {
        listPermissionsNeeded = new ArrayList<>();
    }

    /**
     * This method checks if the app has the necessary permissions.
     *
     * @param context the application's context
     * @return true if no permissions are needed, false otherwise
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean check(Context context) {
        int readPhoneState = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
        int readCallLog = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG);
        int answerPhoneCalls = ContextCompat.checkSelfPermission(context, Manifest.permission.ANSWER_PHONE_CALLS);

        if (readPhoneState != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (readCallLog!= PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CALL_LOG);
        }

        if (answerPhoneCalls != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ANSWER_PHONE_CALLS);
        }

        return listPermissionsNeeded.isEmpty();
    }

    /**
     * Requests the permissions needed that are listed by check().
     *
     * @param activity requests permissions in this activity
     */
    public static void request(Activity activity) {
        if (listPermissionsNeeded != null && !listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(activity,
                    listPermissionsNeeded.toArray(new String[0]),
                    REQUEST_CODE_CALL);
        }
    }
}
