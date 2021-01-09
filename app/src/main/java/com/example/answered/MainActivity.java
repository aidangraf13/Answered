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

public class MainActivity extends AppCompatActivity {
    private boolean enabledService;
    static final String START_SERVICE = "Start Service Intent";
    static final String STOP_SERVICE = "Stop Service Intent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        enabledService = false;
        // Sets up toolbar to access settings
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Button enabled = findViewById(R.id.enableService);
        final Intent startServiceIntent = new Intent(this, CallService.class);
        startServiceIntent.setAction(START_SERVICE);
        final Intent stopServiceIntent = new Intent(this, CallService.class);
        startServiceIntent.setAction(STOP_SERVICE);
        // checks permissions
        try {
            if (!(Permissions.check(this.getApplicationContext()))) {
                Permissions.request(this);
            }
        } catch (Exception e) {
            // TODO Add appropriate error handling
            Log.i("Main", e.getMessage());
        }
        // Triggers service to be enabled
        enabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enabledService = !enabledService;
                // starts foreground service
                if (enabledService) {
                    ContextCompat.startForegroundService(MainActivity.this, startServiceIntent);
                    enabled.setText("Disable");
                } else {
                    ContextCompat.startForegroundService(MainActivity.this, stopServiceIntent);
                    enabled.setText("Enable");
                }
            }
        });
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
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}