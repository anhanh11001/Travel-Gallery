package tech.ducletran.travelgallery.Activities;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.*;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import tech.ducletran.travelgallery.ImageData.Album;
import tech.ducletran.travelgallery.ImageData.AlbumManager;
import tech.ducletran.travelgallery.ImageData.ImageData;
import tech.ducletran.travelgallery.ImageData.ImageManager;
import tech.ducletran.travelgallery.R;

import java.util.ArrayList;

public class AddImageToAlbumActivity extends BaseActivity {
    private MenuItem doneItem;
    private Album currentAlbum;
    private ArrayList<Integer> imageAddedPositions = new ArrayList<Integer>();
    private int specialAlbumPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_photos_view);

        findViewById(R.id.photos_loading_layout).setVisibility(View.GONE);
        findViewById(R.id.photos_empty_view).setVisibility(View.GONE);

        GridView allPhotos = findViewById(R.id.photos_grid_view);
        allPhotos.setNumColumns(3);
        AllPhotosAdapter adapter = new AllPhotosAdapter(this);
        allPhotos.setAdapter(adapter);

        specialAlbumPosition = getIntent().getIntExtra("special_album",-1);
        final int currentAlbumId = getIntent().getIntExtra("current_album_id",0);
        currentAlbum = AlbumManager.getAlbum(currentAlbumId);

        TypedValue typedValueColor = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorAccent,typedValueColor,true);
        @ColorInt int color = typedValueColor.data;

        final GradientDrawable drawable = (GradientDrawable) getDrawable(R.drawable.helper_drawable_border);
        drawable.setStroke(6,color);

        allPhotos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int index = imageAddedPositions.indexOf(position);
                if (index == -1) {
                    imageAddedPositions.add(position);
                    view.setForeground(drawable);
                    doneItem.setVisible(true);
                } else {
                    view.setForeground(null);
                    imageAddedPositions.remove(index);
                    if (imageAddedPositions.size() == 0) {
                        doneItem.setVisible(false);
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_image_to_album_activity_menu,menu);
        doneItem = menu.findItem(R.id.action_return_new_image_to_album);
        doneItem.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_return_new_image_to_album:
                for (Integer i:imageAddedPositions) {
                    ImageData imageToAdd = ImageManager.getImageDataList().get(i);
                    currentAlbum.addToAlbum(imageToAdd);
                    if (specialAlbumPosition == AlbumManager.ALBUM_FAVORITE_CODE) {
                        imageToAdd.setFavorite();
                    } else if (specialAlbumPosition == AlbumManager.ALBUM_PEOPLE_CODE) {
                        imageToAdd.setPeople();
                    } else if (specialAlbumPosition == AlbumManager.ALBUM_FOOD_CODE) {
                        imageToAdd.setFood();
                    }

                }
                DisplayAlbumImagesActivity.setAdapterChanged();

                finish();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class AllPhotosAdapter extends BaseAdapter {
        private Context context;
        private AllPhotosAdapter(Context context) {
            this.context = context;
        }
        @Override
        public int getCount() {
            return ImageManager.getImageDataList().size();
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
                    .load(ImageManager.getImageDataList().get(position).getThumbnail())
                    .into(imageView);

            return convertView;
        }
    }
}
