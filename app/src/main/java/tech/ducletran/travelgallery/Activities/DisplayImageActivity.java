package tech.ducletran.travelgallery.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import tech.ducletran.travelgallery.Adapter.DisplayPhotosAdapter;
import tech.ducletran.travelgallery.R;

public class DisplayImageActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        Intent intent = getIntent();
        int position = intent.getIntExtra("position",0);

        ViewPager viewPager = findViewById(R.id.display_image_view_pager);
        DisplayPhotosAdapter adapter = new DisplayPhotosAdapter(this);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);

    }
}
