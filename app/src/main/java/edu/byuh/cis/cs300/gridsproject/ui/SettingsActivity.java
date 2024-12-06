package edu.byuh.cis.cs300.gridsproject.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;

import edu.byuh.cis.cs300.gridsproject.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
    }

    public static boolean getMusicPref(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c).getBoolean("MUSIC_PREF", true);  // Default is true (ON)
    }

    public static String getSpeedPref(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c).getString("SPEED_PREF", "10");  // Default is "10" (Medium)
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            Context context = getPreferenceManager().getContext();
            PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);
            SwitchPreference music = new SwitchPreference(context);
            music.setTitle(R.string.music_pref);
            music.setSummaryOn(R.string.music_on);
            music.setSummaryOff(R.string.music_off);
            music.setDefaultValue(true);
            music.setKey("MUSIC_PREF");
            screen.addPreference(music);
            ListPreference speed = new ListPreference(context);
            speed.setTitle(R.string.speed_pref_title);
            speed.setSummary(R.string.speed_pref_summary);
            speed.setKey("SPEED_PREF");
            String[] values = {"20", "10", "4"};
            speed.setEntries(R.array.speed_entries);
            speed.setEntryValues(values);
            speed.setDefaultValue("10");
            screen.addPreference(speed);
            setPreferenceScreen(screen);
        }
    }
}

