package com.example.booklat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import androidx.annotation.Nullable;

public class SettingsActivity extends PreferenceActivity {

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;
    public static final String PREF_DEFAULT_SORT_ORDER = "pref_default_sort_order";
    public static final String PREF_THEME = "pref_theme";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        preferenceChangeListener = (sharedPreferences, key) -> {
            if (key.equals(PREF_DEFAULT_SORT_ORDER)) {
                Preference sortPreference = findPreference(key);
                sortPreference.setSummary(String.format("Sort by: %s", sharedPreferences.getString(key, "Title ASC")));
            }

            if (key.equals(PREF_THEME)) {
                Preference themePreference = findPreference(key);
                String mode = sharedPreferences.getString(key, "System");
                themePreference.setSummary(String.format("Current Theme: %s", mode));
                HomeActivity.changeTheme(mode);
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(preferenceChangeListener);

        Preference themePreference = findPreference(PREF_THEME);
        String mode = getPreferenceScreen().getSharedPreferences().getString(PREF_THEME, "System");
        themePreference.setSummary(String.format("Current Theme: %s", mode));


        Preference sortPreference = findPreference(PREF_DEFAULT_SORT_ORDER);
        String a = getPreferenceScreen().getSharedPreferences().getString(PREF_DEFAULT_SORT_ORDER, "Title ASC");
        sortPreference.setSummary(String.format("Sort by: %s", a));

    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);

    }
}
