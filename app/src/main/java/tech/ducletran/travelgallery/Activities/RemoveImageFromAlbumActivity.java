package tech.ducletran.travelgallery.Activities;

import android.content.Context;
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
import tech.ducletran.travelgallery.Model.Album;
import tech.ducletran.travelgallery.Model.AlbumManager;
import tech.ducletran.travelgallery.Model.ImageData;
import tech.ducletran.travelgallery.Model.ImageManager;
import tech.ducletran.travelgallery.R;

import java.util.ArrayList;

public class RemoveImageFromAlbumActivity extends BaseActivity {
    private MenuItem doneItem;
    private Album currentAlbum;
    private ArrayList<Integer> imageRemovedPosition = new ArrayList<Integer>();
    private GradientDrawable drawable;
    private int currentAlbumId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_photos_view);

        findViewById(R.id.photos_empty_view).setVisibility(View.GONE);
        currentAlbumId = getIntent().getIntExtra("current_album_id",0);
        currentAlbum = AlbumManager.getAlbum(currentAlbumId);

        GridView allPhotos = findViewById(R.id.photos_grid_view);
        allPhotos.setNumColumns(3);
        final AlbumPhotosAdapter adapter = new AlbumPhotosAdapter(this,currentAlbum.getAlbumImageList());
        allPhotos.setAdapter(adapter);



        TypedValue typedValueColor = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorAccent,typedValueColor,true);
        @ColorInt int color = typedValueColor.data;

        drawable = (GradientDrawable) getDrawable(R.drawable.helper_drawable_border);
        drawable.setStroke(6,color);

        allPhotos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int imageId = ((ImageData) adapter.getItem(position)).getImageId();
                int index = imageRemovedPosition.indexOf(imageId);
                if (index == -1) {
                    imageRemovedPosition.add(imageId);
                    view.setForeground(drawable);
                    doneItem.setVisible(true);
                } else {
                    view.setForeground(null);
                    imageRemovedPosition.remove(index);
                    if (imageRemovedPosition.size() == 0) {
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
                for (Integer id:imageRemovedPosition) {
                    ImageData imageToRemove = ImageManager.getImageById(id);
                    currentAlbum.removeFromAlbum(imageToRemove);
                    if (currentAlbumId == Album.DEFAULT_FAVORITE_ID) {
                        imageToRemove.setFavorite();
                    } else if (currentAlbumId == Album.DEFAULT_PEOPLE_ID) {
                        imageToRemove.setPeople();
                    } else if (currentAlbumId == Album.DEFAULT_FOOD_ID) {
                        imageToRemove.setFood();
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

    private class AlbumPhotosAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<ImageData> imageList;
        private AlbumPhotosAdapter(Context context, ArrayList<ImageData> imageList) {
            this.imageList = imageList;
            this.context = context;
        }
        @Override
        public int getCount() {
            return imageList.size();
        }

        @Override
        public Object getItem(int position) {
            return imageList.get(position);
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

            if (imageRemovedPosition.contains(position)) {
                convertView.setForeground(drawable);
            }

            return convertView;
        }
    }

}
