package tech.ducletran.travelgallery.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import tech.ducletran.travelgallery.R;

public class DisplayImageInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image_info);

        Intent intent = getIntent();
        ImageView imageView = findViewById(R.id.display_info_image_view);
        TextView dateTextView = findViewById(R.id.display_info_date_text_view);
        TextView longtitudeTextView = findViewById(R.id.display_info_longtitude_text_view);
        TextView latitudeTextView = findViewById(R.id.display_info_latitude_text_view);
        TextView sizeTextView = findViewById(R.id.display_info_size_text_view);

        Glide.with(this).load(intent.getStringExtra("image_info_display_image")).into(imageView);
        dateTextView.setText("DATE: " + intent.getStringExtra("image_info_date"));
        sizeTextView.setText("SIZE: " + intent.getStringExtra("image_info_size"));
        String latitude = intent.getStringExtra("image_info_longtitude");
        String longtitude = intent.getStringExtra("image_info_latitude");
        if (!TextUtils.isEmpty(latitude)) {
            latitudeTextView.setText("Latitude: " + latitude);
        }
        if (!TextUtils.isEmpty(longtitude)) {
            longtitudeTextView.setText("Longtitude: " + longtitude);
        }
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
