package tech.ducletran.travelgallery.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.*;
import tech.ducletran.travelgallery.Activities.MainActivity;
import tech.ducletran.travelgallery.Activities.SettingsActivity;
import tech.ducletran.travelgallery.R;


public class SettingsFragment extends PreferenceFragmentCompat  implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.fragment_settings_resource);

        PreferenceScreen preferenceScreen = getPreferenceScreen();
        SharedPreferences sharedPreferences = preferenceScreen.getSharedPreferences();
        int count = preferenceScreen.getPreferenceCount();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        for (int i = 0;i < count;i++) {
            Preference p = preferenceScreen.getPreference(i);
            if (p instanceof ListPreference) {
                String value = sharedPreferences.getString(p.getKey(),"");
                int index = ((ListPreference) p).findIndexOfValue(value);
                if (index >= 0) {
                    p.setSummary(((ListPreference) p).getEntries()[index]);
                }
            }
            if (p instanceof SwitchPreference) {
                boolean value = sharedPreferences.getBoolean(p.getKey(),true);
                if (value) {
                    p.setSummary(getString(R.string.action_settings_sort_on));
                } else {
                    p.setSummary(getString(R.string.action_settings_sort_off));
                }
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.action_settings_sort_key))) {
            boolean sorted = sharedPreferences.getBoolean(key,true);
            if (sorted) {
                findPreference(getString(R.string.action_settings_sort_key))
                        .setSummary(getString(R.string.action_settings_sort_on));
            } else {
                findPreference(getString(R.string.action_settings_sort_key))
                        .setSummary(getString(R.string.action_settings_sort_off));
            }
        }

        if (key.equals(getString(R.string.action_settings_columns_key))) {
            int numColumns = Integer.parseInt(sharedPreferences.getString(key,"4"));
            findPreference(key).setSummary(String.valueOf(numColumns));
        }

        if (key.equals(getString(R.string.action_settings_color_theme_key)) ||
                key.equals(getString(R.string.action_settings_dark_mode_key))) {
            boolean isDarkMode = sharedPreferences.getBoolean(getString(R.string.action_settings_dark_mode_key),false);
            String colorCode = sharedPreferences.getString(getString(R.string.action_settings_color_theme_key),"1");
            MainActivity.setUpAppTheme(Integer.parseInt(colorCode), false);

            Activity mCurrentActivity = getActivity();
            mCurrentActivity.finish();
            mCurrentActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            Intent intent = new Intent(mCurrentActivity,SettingsActivity.class);
            mCurrentActivity.startActivity(intent);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PreferenceManager.getDefaultSharedPreferences(getActivity()).unregisterOnSharedPreferenceChangeListener(this);
    }


}
