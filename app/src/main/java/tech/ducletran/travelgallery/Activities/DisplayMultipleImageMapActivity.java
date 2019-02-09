package tech.ducletran.travelgallery.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import tech.ducletran.travelgallery.Adapter.PhotosAdapter;
import tech.ducletran.travelgallery.ImageData.ImageData;
import tech.ducletran.travelgallery.R;

import java.util.ArrayList;

public class DisplayMultipleImageMapActivity extends BaseActivity {

    private static ArrayList<ImageData> multipleImageList;

    public static void setImagesToDisplay(ArrayList<ImageData> multipleImage) {
        multipleImageList = multipleImage;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_photos_view);

        GridView gridView = findViewById(R.id.photos_grid_view);
        findViewById(R.id.photos_loading_layout).setVisibility(View.GONE);
        findViewById(R.id.photos_empty_view).setVisibility(View.GONE);

        PhotosAdapter adapter = new PhotosAdapter(this,multipleImageList);
        gridView.setAdapter(adapter);
        gridView.setNumColumns(3);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Do something here
                Intent intent = new Intent(DisplayMultipleImageMapActivity.this, DisplayImageActivity.class);
                intent.putExtra("position",position);
                DisplayImageActivity.setImageDataList(multipleImageList);
                startActivity(intent);
            }
        });
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