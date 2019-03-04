package tech.ducletran.travelgallery.Activities;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.*;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import tech.ducletran.travelgallery.Model.Album;
import tech.ducletran.travelgallery.Model.AlbumManager;
import tech.ducletran.travelgallery.Model.ImageData;
import tech.ducletran.travelgallery.Model.ImageManager;
import tech.ducletran.travelgallery.R;

import java.util.ArrayList;

public class AddImageToAlbumActivity extends AppCompatActivity {
    private MenuItem doneItem;
    private Album currentAlbum;
    private ArrayList<Integer> imageAddedPositions = new ArrayList<Integer>();
    private GradientDrawable drawable;
    private int currentAlbumId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(MainActivity.getAppTheme());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_photos_view);

        findViewById(R.id.photos_empty_view).setVisibility(View.GONE);

        GridView allPhotos = findViewById(R.id.photos_grid_view);
        allPhotos.setNumColumns(3);
        AllPhotosAdapter adapter = new AllPhotosAdapter(this);
        allPhotos.setAdapter(adapter);

        currentAlbumId = getIntent().getIntExtra("current_album_id",0);
        currentAlbum = AlbumManager.getAlbum(currentAlbumId);

        TypedValue typedValueColor = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorAccent,typedValueColor,true);
        @ColorInt int color = typedValueColor.data;

        drawable = (GradientDrawable) getDrawable(R.drawable.helper_drawable_border);
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
                    currentAlbum.addToAlbum(imageToAdd,true);
                    if (currentAlbumId == Album.DEFAULT_FAVORITE_ID) {
                        imageToAdd.setFavorite();
                    } else if (currentAlbumId == Album.DEFAULT_PEOPLE_ID) {
                        imageToAdd.setPeople();
                    } else if (currentAlbumId == Album.DEFAULT_FOOD_ID) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false);

            ImageView imageView = convertView.findViewById(R.id.photo_item_image_view);
            Glide.with(context)
                    .load(ImageManager.getImageDataList().get(position).getThumbnail())
                    .into(imageView);

            if (imageAddedPositions.contains(position)) {
                convertView.setForeground(drawable);
            }

            return convertView;
        }
    }
}
