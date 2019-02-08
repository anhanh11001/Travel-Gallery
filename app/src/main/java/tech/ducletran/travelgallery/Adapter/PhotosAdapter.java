package tech.ducletran.travelgallery.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import tech.ducletran.travelgallery.ImageData.ImageData;
import tech.ducletran.travelgallery.ImageData.ImageManager;
import tech.ducletran.travelgallery.R;

import java.util.ArrayList;
import java.util.List;

public class PhotosAdapter extends ArrayAdapter<ImageData> {
    private Context context;

    public PhotosAdapter(Context context, ArrayList<ImageData> imageDataList) {
        super(context,0,imageDataList);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.photo_item_view,parent,false);

        ImageData image = getItem(position);
        ImageView imageView = convertView.findViewById(R.id.photo_item_image_view);
        Glide.with(context).load(image.getThumbnail()).into(imageView);
        return convertView;
    }

}
