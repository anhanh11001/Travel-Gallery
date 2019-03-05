package tech.ducletran.travelgallery.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.*;
import tech.ducletran.travelgallery.Activities.MainActivity;
import tech.ducletran.travelgallery.Activities.SettingsActivity;
import tech.ducletran.travelgallery.R;


public class SettingsFragment extends PreferenceFragmentCompat  implements SharedPreferences.OnSharedPreferenceChangeListener {

    private PreferenceScreen preferenceScreen;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.fragment_settings_resource);

        preferenceScreen = getPreferenceScreen();
        SharedPreferences sharedPreferences = preferenceScreen.getSharedPreferences();
        int count = preferenceScreen.getPreferenceCount();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        boolean isDarkMode = sharedPreferences.getBoolean(getString(R.string.action_settings_dark_mode_key),false);

        for (int i = 0;i < count;i++) {
            Preference p = preferenceScreen.getPreference(i);
            if (isDarkMode) {
                p.getIcon().setColorFilter(getContext().getColor(R.color.colorWhitePrimary), PorterDuff.Mode.SRC_IN);
            }
            if (p instanceof ListPreference) {
                String value = sharedPreferences.getString(p.getKey(),"");
                int index = ((ListPreference) p).findIndexOfValue(value);
                if (index >= 0) {
                    p.setSummary(((ListPreference) p).getEntries()[index]);
                }
            }
            if (p instanceof SwitchPreference) {
                String key = p.getKey();
                boolean value = sharedPreferences.getBoolean(key,true);
                if (key.equals(getString(R.string.action_settings_sort_key))) {
                    p.setSummary(getString((value) ?
                            R.string.action_settings_sort_on : R.string.action_settings_sort_off));
                } else if (key.equals(getString(R.string.action_settings_dark_mode_key))) {
                    p.setSummary(getString((value) ?
                            R.string.action_settings_dark_mode_on : R.string.action_settings_dark_mode_off));
                }

            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        boolean isDarkMode = sharedPreferences.getBoolean(getString(R.string.action_settings_dark_mode_key),false);
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
            String colorCode = sharedPreferences.getString(getString(R.string.action_settings_color_theme_key),"1");
            MainActivity.setUpAppTheme(Integer.parseInt(colorCode), isDarkMode);

            Activity mCurrentActivity = getActivity();
            mCurrentActivity.finish();
            mCurrentActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            Intent intent = new Intent(mCurrentActivity,SettingsActivity.class);
            mCurrentActivity.startActivity(intent);
        }

        if (isDarkMode) {
            for (int i=0;i<preferenceScreen.getPreferenceCount();i++) {
                preferenceScreen.getPreference(i).getIcon()
                        .setColorFilter(getContext().getColor(R.color.colorWhitePrimary),PorterDuff.Mode.SRC_IN);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PreferenceManager.getDefaultSharedPreferences(getActivity()).unregisterOnSharedPreferenceChangeListener(this);
    }


}
