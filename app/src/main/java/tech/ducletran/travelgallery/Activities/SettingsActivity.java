package tech.ducletran.travelgallery.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import tech.ducletran.travelgallery.R;

public class SettingsActivity extends BaseActivity  {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
