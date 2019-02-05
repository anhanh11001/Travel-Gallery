package tech.ducletran.travelgallery.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import tech.ducletran.travelgallery.ImageData.ImageData;
import tech.ducletran.travelgallery.ImageData.ImageHolder;
import tech.ducletran.travelgallery.R;

public class PhotosAdapter extends BaseAdapter {
    private Context context;

    public PhotosAdapter(Context context) {
        this.context = context;
        if(ImageHolder.getImageDataList().size() == 0) {
            new LoadPhotos().execute();
        }
    }

    @Override
    public int getCount() {
        return ImageHolder.getImageDataList().size();
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
        convertView = LayoutInflater.from(context).inflate(R.layout.photo_item_view,parent,false);

        ImageView imageView = convertView.findViewById(R.id.photo_item_image_view);
        Glide.with(context).load(ImageHolder.getImageDataList().get(position).getThumbnail()).into(imageView);
        return convertView;
    }


    class LoadPhotos extends AsyncTask<Void,Void, Void> {

        @Override
        protected  Void doInBackground(Void... voids) {
            Uri uriExternal = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String[] projection = { MediaStore.MediaColumns.DATA, // File path
                                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // Album name
                                    MediaStore.Images.Media.DATE_TAKEN,          // Date taken
                                    MediaStore.Images.Thumbnails.DATA,          // Thumbnail link
                                    MediaStore.Images.Media.LATITUDE,           // Latitude
                                    MediaStore.Images.Media.LONGITUDE,          // Longtitude
                                    MediaStore.Images.Media.SIZE                // Size
            };

            Cursor cursor = context.getContentResolver().query(uriExternal,projection,
                    null, null,null);

            while (cursor.moveToNext() ) {
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
                String timestamp = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN));
                String albumName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                String thumbnail = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA));
                String latitude = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.LATITUDE));
                String longtitude = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.LONGITUDE));
                String size = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE));
                if (albumName.equals("Facebook") || albumName.equals("Camera")) {
                    ImageHolder.addImage(new ImageData(path,timestamp,thumbnail,latitude,longtitude,size));
                }

            }
            cursor.close();
            setUpDateSorting();
            return null;
        }

        private void setUpDateSorting() {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            Boolean isSorted = sharedPreferences.getBoolean(context.getString(R.string.action_settings_sort_key),true);
            if (isSorted) {
                ImageHolder.sortByDate();
            } else {
                ImageHolder.shuffle();
            }
        }
    }
}
