package tech.ducletran.travelgallery.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import tech.ducletran.travelgallery.Model.AlbumManager;
import tech.ducletran.travelgallery.Model.ImageData;
import tech.ducletran.travelgallery.Model.ImageManager;
import tech.ducletran.travelgallery.R;

import java.util.List;

public class ImagePickerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(MainActivity.getAppTheme());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_photos_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        int currentAlbumId = getIntent().getIntExtra("current_album_id",-1);
        final List<ImageData> imageDataList;
        if (currentAlbumId == -1) {
            imageDataList = ImageManager.getImageDataList();
        } else {
            imageDataList = AlbumManager.getAlbum(currentAlbumId).getAlbumImageList();
        }

        findViewById(R.id.photos_empty_view).setVisibility(View.GONE);

        GridView imageGridView = findViewById(R.id.photos_grid_view);
        imageGridView.setNumColumns(3);
        ImagePickerAdapter adapter = new ImagePickerAdapter(this,imageDataList);
        imageGridView.setAdapter(adapter);

        imageGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageData imageClicked = imageDataList.get(position);
                Intent returnIntent = getIntent();
                returnIntent.putExtra("result_image_id",imageClicked.getImageId());
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });
    }


    private class ImagePickerAdapter extends BaseAdapter {
        private Context context;
        private List<ImageData> imageList;

        private ImagePickerAdapter(Context context, List<ImageData> imageList) {
            this.context = context;
            this.imageList = imageList;
        }
        @Override
        public int getCount() {
            return imageList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false);

            ImageView imageView = convertView.findViewById(R.id.photo_item_image_view);
            Glide.with(context)
                    .load(imageList.get(position).getThumbnail())
                    .into(imageView);
            return convertView;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED,getIntent());
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
