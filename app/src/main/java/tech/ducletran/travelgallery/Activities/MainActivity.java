package tech.ducletran.travelgallery.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import tech.ducletran.travelgallery.Adapter.CategoryStatePageAdapter;
import tech.ducletran.travelgallery.R;

public class MainActivity extends BaseActivity {
    private TabLayout tabLayout;
    static final int REQUEST_PERMISSION_KEY = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ViewPager viewPager = findViewById(R.id.main_view_pager);
        CategoryStatePageAdapter adapter = new CategoryStatePageAdapter(getSupportFragmentManager(),this);

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);

        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        setUpTabLayoutIcon();

        String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!(ActivityCompat.checkSelfPermission(this,PERMISSIONS[0]) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSION_KEY);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_item_settings:
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }

    }

    private void setUpTabLayoutIcon() {
        TypedValue typedValuePrimary = new TypedValue();
        TypedValue typedValuePrimaryDark = new TypedValue();
        Resources.Theme theme = this.getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValuePrimary, true);
        @ColorInt int colorPrimary = typedValuePrimary.data;
        theme.resolveAttribute(R.attr.colorPrimaryDark,typedValuePrimaryDark,true);
        @ColorInt final int colorPrimaryDark = typedValuePrimaryDark.data;

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_photos_tab_icon);
        tabLayout.getTabAt(0).getIcon().setColorFilter(colorPrimary, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_album_tab_icon);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_stories_tab_icon);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_map_tab_icon);
        tabLayout.setTabTextColors(getColor(R.color.colorBlackPrimary),colorPrimary);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(colorPrimaryDark, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(getColor(R.color.colorBlackPrimary), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
