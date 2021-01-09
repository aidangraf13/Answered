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

public class Permissions {
    private static final ArrayList<String> listPermissionsNeeded;
    private static final int REQUEST_CODE_CALL = 42;

    static {
        listPermissionsNeeded = new ArrayList<>();
    }

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

    public static void request(Activity activity) {
        if (listPermissionsNeeded != null && !listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(activity,
                    listPermissionsNeeded.toArray(new String[0]),
                    REQUEST_CODE_CALL);
        }
    }
}
