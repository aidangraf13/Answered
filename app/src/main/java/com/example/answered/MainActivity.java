package com.example.answered;

import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.navigation.fragment.NavHostFragment;

public class MainActivity extends AppCompatActivity {
    private boolean enabledService;
    static final String START_SERVICE = "Start Service Intent";
    static final String STOP_SERVICE = "Stop Service Intent";
    private static final String TAG = "Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Sets up toolbar to access settings
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        enabledService = false;

        // checks permissions
        try {
            if (!(Permissions.check(this.getApplicationContext()))) {
                Permissions.request(this);
            }
        } catch (Exception e) {
            // TODO Add appropriate error handling
            Log.i(TAG, e.getMessage());
        }

    }

    protected boolean enabledService (Button enabled) {
        enabledService = !enabledService;
        final Intent startServiceIntent = new Intent(this, CallService.class);
        startServiceIntent.setAction(START_SERVICE);
        final Intent stopServiceIntent = new Intent(this, CallService.class);
        startServiceIntent.setAction(STOP_SERVICE);

        // starts foreground service
        if (enabledService) {
            ContextCompat.startForegroundService(MainActivity.this, startServiceIntent);
            enabled.setText("Disable");
        } else {
            ContextCompat.startForegroundService(MainActivity.this, stopServiceIntent);
            enabled.setText("Enable");
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_buttons, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_settings:
                // TODO replace start activity with fragment to fix bug?

//                NavHostFragment.findNavController(FirstFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}