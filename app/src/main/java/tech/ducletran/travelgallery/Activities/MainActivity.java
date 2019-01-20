package tech.ducletran.travelgallery.Activities;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import tech.ducletran.travelgallery.CategoryStatePageAdapter;
import tech.ducletran.travelgallery.R;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = findViewById(R.id.view_pager);
        CategoryStatePageAdapter adapter = new CategoryStatePageAdapter(getSupportFragmentManager(),this);

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);

        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        setUpTabLayoutIcon();


    }

    private void setUpTabLayoutIcon() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_photos_tab_icon);
        tabLayout.getTabAt(0).getIcon().setColorFilter(getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_album_tab_icon);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_stories_tab_icon);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_map_tab_icon);
        tabLayout.setTabTextColors(getColor(R.color.colorBlackPrimary),getColor(R.color.colorPrimary));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
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
