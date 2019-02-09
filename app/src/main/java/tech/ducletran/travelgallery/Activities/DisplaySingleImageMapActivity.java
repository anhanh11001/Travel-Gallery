package tech.ducletran.travelgallery.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import com.bumptech.glide.Glide;
import tech.ducletran.travelgallery.CustomizedClass.CustomizedImageView;
import tech.ducletran.travelgallery.R;

public class DisplaySingleImageMapActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_image_view);

        CustomizedImageView imageView = findViewById(R.id.display_photo_image_view);
        Glide.with(this).load(getIntent().getStringExtra("image_path")).into(imageView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
