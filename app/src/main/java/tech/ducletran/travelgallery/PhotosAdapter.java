package tech.ducletran.travelgallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

public class PhotosAdapter extends BaseAdapter {
    private int[] test_image_id = {R.drawable.testphoto0, R.drawable.testphoto1, R.drawable.testphoto2, R.drawable.testphoto3,
            R.drawable.testphoto4, R.drawable.testphoto5, R.drawable.testphoto6, R.drawable.testphoto7, R.drawable.testphoto8,
            R.drawable.testphoto9};
    private Context context;

    public PhotosAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 10;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.photo_item_view,parent,false);

            ImageView imageView = convertView.findViewById(R.id.photo_item_image_view);
            Glide.with(context).load(test_image_id[position]).into(imageView);
        }

        return convertView;
    }
}
