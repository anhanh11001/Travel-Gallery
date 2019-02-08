package tech.ducletran.travelgallery.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import tech.ducletran.travelgallery.Fragment.AlbumsFragment;
import tech.ducletran.travelgallery.ImageData.Album;
import tech.ducletran.travelgallery.ImageData.AlbumManager;
import tech.ducletran.travelgallery.ImageData.ImageData;
import tech.ducletran.travelgallery.R;

public class SetAlbumCoverActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_photos_view);
        int currentAlbumId = getIntent().getIntExtra("current_album_id",0);
        final Album currentAlbum = AlbumManager.getAlbum(currentAlbumId);

        findViewById(R.id.photos_loading_layout).setVisibility(View.GONE);
        findViewById(R.id.photos_empty_view).setVisibility(View.GONE);

        GridView albumPhotos = findViewById(R.id.photos_grid_view);
        albumPhotos.setNumColumns(3);
        AlbumPhotosAdapter adapter = new AlbumPhotosAdapter(this,currentAlbum);
        albumPhotos.setAdapter(adapter);

        albumPhotos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageData imageClicked = currentAlbum.getAlbumImageList().get(position);
                currentAlbum.setAlbumCover(imageClicked.getThumbnail());
                AlbumsFragment.setAlbumFragmentChanged();
                finish();
            }
        });
    }


    private class AlbumPhotosAdapter extends BaseAdapter {
        private Context context;
        private Album currentAlbum;

        private AlbumPhotosAdapter(Context context, Album currentAlbum) {
            this.context = context;
            this.currentAlbum = currentAlbum;
        }
        @Override
        public int getCount() {
            return currentAlbum.getAlbumImageList().size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.photo_item_view, parent, false);

            ImageView imageView = convertView.findViewById(R.id.photo_item_image_view);
            Glide.with(context)
                    .load(currentAlbum.getAlbumImageList().get(position).getThumbnail())
                    .into(imageView);
            return convertView;
        }
    }
}
