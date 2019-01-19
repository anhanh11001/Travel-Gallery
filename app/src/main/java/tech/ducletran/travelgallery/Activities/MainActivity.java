package tech.ducletran.travelgallery.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import tech.ducletran.travelgallery.CategoryStatePageAdapter;
import tech.ducletran.travelgallery.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = findViewById(R.id.view_pager);
        CategoryStatePageAdapter adapter = new CategoryStatePageAdapter(getSupportFragmentManager(),this);

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

    }
}
