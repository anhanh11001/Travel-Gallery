package tech.ducletran.travelgallery.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import tech.ducletran.travelgallery.Model.ImageData;
import tech.ducletran.travelgallery.Model.ImageManager;
import tech.ducletran.travelgallery.R;

public class DisplayImageInfoActivity extends BaseActivity {
    private static boolean isChanged = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image_info);

        Intent intent = getIntent();
        final int imageId = intent.getIntExtra("image_info_id",0);
        ImageData currentImage = ImageManager.getImageById(imageId);
        ImageView imageView = findViewById(R.id.display_info_image_view);
        TextView dateTextView = findViewById(R.id.display_info_date_text_view);
        TextView locationTextView = findViewById(R.id.display_info_location_text_view);
        TextView sizeTextView = findViewById(R.id.display_info_size_text_view);
        TextView descriptionTextView = findViewById(R.id.display_info_description_text_view);
        TextView titleTextView = findViewById(R.id.display_info_title_text_view);

        Glide.with(this).load(currentImage.getPath()).into(imageView);
        dateTextView.setText("DATE: " + currentImage.getDateFormatted());
        sizeTextView.setText("SIZE: " + currentImage.getSize());
        String latitude = currentImage.getLatitude();
        String longtitude = currentImage.getLongtitude();
        String description = currentImage.getDescription();
        String title = currentImage.getTitle();

        if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longtitude)) {
            locationTextView.setText("Location: " + latitude + " | " + longtitude);
        }
        if (!TextUtils.isEmpty(description)) {
            descriptionTextView.setText("Description: " + description);
        }
        if (!TextUtils.isEmpty(title)) {
            titleTextView.setText(title);
        }

        ImageButton imageButton = findViewById(R.id.display_info_image_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayImageInfoActivity.this,EditInfoActivity.class);
                intent.putExtra("edit_info_id",imageId);

                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void setInfoChanged() {
        isChanged = true;
    }
    @Override
    protected void onResume() {
        if (isChanged) {
            recreate();
            isChanged = false;
        }

        super.onResume();
    }
}
