package tech.ducletran.travelgallery.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import tech.ducletran.travelgallery.R;

public abstract class BaseActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setUpTheme();
        super.onCreate(savedInstanceState);
    }

    private void setUpTheme() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String colorThemeCode = sharedPreferences.getString(getString(R.string.action_settings_color_theme_key),"1");

        setUpAppTheme(Integer.parseInt(colorThemeCode));
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void setUpAppTheme(int code) {
        switch (code) {
            case 1: // Blue
                setTheme(R.style.CrystalBlueAppTheme);
                break;
            case 2: // Orange
                setTheme(R.style.SunshineOrangeAppTheme);
                break;
            case 3: // Green
                setTheme(R.style.CoralGreenAppTheme);
                break;
            case 4: // Gray
                setTheme(R.style.SilverGrayAppTheme);
                break;
            case 5: // Red
                setTheme(R.style.BloodyRedAppTheme);
                break;
            default:
                setTheme(R.style.CrystalBlueAppTheme);
                break;
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.action_settings_color_theme_key))) {
            String colorCode = sharedPreferences.getString(key,"1");
            setUpAppTheme(Integer.parseInt(colorCode));
            recreate();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }
}
