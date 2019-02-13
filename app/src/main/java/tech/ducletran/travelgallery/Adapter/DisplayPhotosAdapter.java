package tech.ducletran.travelgallery.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import tech.ducletran.travelgallery.Activities.DisplayImageActivity;
import tech.ducletran.travelgallery.CustomizedClass.CustomImageView;
import tech.ducletran.travelgallery.Model.ImageData;
import tech.ducletran.travelgallery.R;

import java.util.List;


public class DisplayPhotosAdapter extends PagerAdapter {

    private DisplayImageActivity activity;
    private LayoutInflater layoutInflater;
    private List<ImageData> imageDataList;

    public DisplayPhotosAdapter(DisplayImageActivity activity, List<ImageData> imageDataList) {
        this.activity = activity;
        this.imageDataList = imageDataList;
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return imageDataList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view ==  o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = layoutInflater.inflate(R.layout.display_image_view,container,false);
        CustomImageView imageView = itemView.findViewById(R.id.display_photo_image_view);

        imageView.setActivity(activity);
        ImageData currentImage = imageDataList.get(position);
        Glide.with(activity).load(currentImage.getPath()).into(imageView);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (imageDataList.contains(object)) {
            return imageDataList.indexOf(object);
        } else {
            return POSITION_NONE;
        }
    }
}
