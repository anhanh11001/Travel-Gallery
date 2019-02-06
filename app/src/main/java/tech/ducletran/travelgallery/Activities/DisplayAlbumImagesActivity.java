package tech.ducletran.travelgallery.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import tech.ducletran.travelgallery.Adapter.PhotosAdapter;
import tech.ducletran.travelgallery.ImageData.Album;
import tech.ducletran.travelgallery.ImageData.ImageHolder;
import tech.ducletran.travelgallery.R;

public class DisplayAlbumImagesActivity extends BaseActivity {
    private Album currentAlbum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_photos_view);

        Intent intent = getIntent();
        int albumPosition = intent.getIntExtra("album_position",0);
        currentAlbum = ImageHolder.getBaseAlbum()[albumPosition];
        PhotosAdapter adapter = new PhotosAdapter(this,
                currentAlbum.getAlbumImageList());
        DisplayImageActivity.setImageDataList(currentAlbum.getAlbumImageList());

        GridView gridView = findViewById(R.id.photos_grid_view);
        gridView.setNumColumns(4);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DisplayAlbumImagesActivity.this, DisplayImageActivity.class);
                intent.putExtra("position",position);
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

    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().setTitle(currentAlbum.getAlbumName());
    }
}
