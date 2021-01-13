package com.example.answered;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

/**
 * Implements the preferences fragment from Android X.
 */
public class SettingsFragment extends PreferenceFragmentCompat {
    private static final String TAG = "SettingsFragment";

    /**
     * Called when preferences are created.
     * @param savedInstanceState unused
     * @param rootKey unused
     */
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        EditTextPreference phonePreference = findPreference(getString(R.string.phoneKey));
        ListPreference delayPreference = findPreference(getString(R.string.delayKey));

        if (phonePreference != null) {
            phonePreference.setSummaryProvider(new Preference.SummaryProvider<EditTextPreference>() {

                /**
                 * Provides a visual summary of the text the user entered.
                 *
                 * @param preference used to get the text
                 * @return a string of the text, or Not set if there isn't any number entered
                 */
                @Override
                public CharSequence provideSummary(EditTextPreference preference) {
                    String text = preference.getText();
                    if (TextUtils.isEmpty(text)) {
                        return "Not set";
                    }
                    return text;
                }
            });
        }

        if (delayPreference != null) {
            delayPreference.setSummaryProvider(new Preference.SummaryProvider<ListPreference>() {

                /**
                 * Provides a visual summary of the delay the user entered.
                 *
                 * @param preference used to get the delay value
                 * @return a string of the delay, or No Delay if there isn't any delay entered
                 */
                @Override
                public CharSequence provideSummary(ListPreference preference) {
                    String delay = preference.getValue();
                    int delayIndex = preference.findIndexOfValue(delay);
                    Log.i(TAG, (String) delay);
                    if (TextUtils.isEmpty(delay)) {
                        return "No Delay";
                    }
                    return preference.getEntries()[delayIndex];
                }
            });
        }
    }
}
