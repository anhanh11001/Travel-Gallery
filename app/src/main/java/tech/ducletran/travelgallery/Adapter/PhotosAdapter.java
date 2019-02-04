package tech.ducletran.travelgallery.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import tech.ducletran.travelgallery.ImageData;
import tech.ducletran.travelgallery.ImageHolder;
import tech.ducletran.travelgallery.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

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
        Glide.with(context).load(ImageHolder.getImageDataList().get(position).getPath()).into(imageView);
        return convertView;
    }


    class LoadPhotos extends AsyncTask<Void,Void, Void> {

        @Override
        protected  Void doInBackground(Void... voids) {
            Uri uriExternal = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String[] projection = {MediaStore.MediaColumns.DATA,
                                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,MediaStore.Images.Media.DATE_TAKEN};

            Cursor cursor = context.getContentResolver().query(uriExternal,projection,
                    null, null,null);

            while (cursor.moveToNext() ) {
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
                String timestamp = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN));
                String albumName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                if (albumName.equals("Facebook") || albumName.equals("Camera")) {
                    ImageHolder.addImage(new ImageData(path,timestamp,albumName));
                }

            }
            cursor.close();
            ImageHolder.sortByDate();
            return null;
        }

    }
}
