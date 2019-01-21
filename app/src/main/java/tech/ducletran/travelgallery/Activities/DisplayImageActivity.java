package tech.ducletran.travelgallery.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.jsibbold.zoomage.ZoomageView;
import tech.ducletran.travelgallery.R;

public class DisplayImageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        ZoomageView imageDisplayed = findViewById(R.id.display_photo_image_view);

        Glide.with(this).load(getIntent().getIntExtra("id",0)).into(imageDisplayed);
    }
}
