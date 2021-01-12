package com.example.answered;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Main activity of the app; starts the toolbar, nav host, services, and provides a button.
 */
public class MainActivity extends AppCompatActivity {
    private boolean enabledService;
    static final String START_SERVICE = "Start Service Intent";
    static final String STOP_SERVICE = "Stop Service Intent";
    private static final String TAG = "Main";

    AppBarConfiguration appBarConfiguration;
    Toolbar toolbar;
    SharedPreferences preferences;

    /**
     * Called when the app is opened and does a majority of the work setting up UI.
     * TODO: Move UI generation off the main thread to improve performance
     *
     * @param savedInstanceState used to set the view
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Sets up toolbar to access settings
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Sets up toolbar with navHost
        NavHostFragment navHostFragment = (NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // Sets night mode to system default
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        // Sets up service intents
        final Intent startServiceIntent = new Intent(getApplicationContext(), CallService.class);
        startServiceIntent.setAction(START_SERVICE);
        final Intent stopServiceIntent = new Intent(getApplicationContext(), CallService.class);
        stopServiceIntent.setAction(STOP_SERVICE);

        // Sets up shared preference if the service is enabled
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (preferences.contains(getString(R.string.enabledKey))) {
            // defValue unused as we are certain there exists the preference
            enabledService = preferences.getBoolean(getString(R.string.enabledKey), false);
        } else {
            // Adds preference if the enabled service setting hasn't been added
            Log.i(TAG, "Service preference added");
            preferences.edit().putBoolean(getString(R.string.enabledKey), false).commit();
            enabledService = false;
        }

        // Changes FAB to reflect if service is enabled
        final FloatingActionButton fab = findViewById(R.id.fab);
        if (enabledService) {
            fab.setImageDrawable(getDrawable(R.drawable.ic_clear));
        } else {
            fab.setImageDrawable(getDrawable(R.drawable.ic_done));

            // checks permissions
            try {
                if (!(Permissions.check(this.getApplicationContext()))) {
                    Permissions.request(this);
                }
            } catch (Exception e) {
                // TODO Add appropriate error handling
                Log.i(TAG, e.getMessage());
            }

            // Changes button to enable service
            fab.setOnClickListener(new View.OnClickListener() {
                /**
                 * Enables or disables the service, and changes the UI and settings accordingly.
                 *
                 * @param v unused
                 */
                @Override
                public void onClick(View v) {
                    enabledService = preferences.getBoolean(getString(R.string.enabledKey), false);
                    Log.i(TAG, "Service enabled is " + enabledService);

                    // starts foreground service
                    if (enabledService) {
                        ContextCompat.startForegroundService(MainActivity.this, stopServiceIntent);
                        fab.setImageDrawable(getDrawable(R.drawable.ic_done));
                        preferences.edit().putBoolean(getString(R.string.enabledKey), false).commit();
                    } else {
                        ContextCompat.startForegroundService(MainActivity.this, startServiceIntent);
                        fab.setImageDrawable(getDrawable(R.drawable.ic_clear));
                        preferences.edit().putBoolean(getString(R.string.enabledKey), true).commit();
                    }
                }
            });
        }
    }


    /**
     * Overrides the navigate up button on the settings to give control to the nav host.
     *
     * @return true if the config was successful, false otherwise
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        // Adds options menu back
        invalidateOptionsMenu();
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * Creates the options menu to access settings.
     *
     * @param menu the menu that contains the layout for the options
     * @return true always
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_buttons, menu);
        return true;
    }

    /**
     * This method runs when an option is selected and acts appropriately.
     *
     * @param item the item selected on the menu
     * @return true if menu needs to be consumed, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                Navigation.findNavController(
                        findViewById(R.id.nav_host_fragment)).navigate(R.id.main_to_settings);
                toolbar.getMenu().clear();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}