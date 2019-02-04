package tech.ducletran.travelgallery.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.jsibbold.zoomage.ZoomageView;
import tech.ducletran.travelgallery.ImageHolder;
import tech.ducletran.travelgallery.R;


public class DisplayPhotosAdapter extends PagerAdapter {

    private Activity activity;
    private LayoutInflater layoutInflater;

    public DisplayPhotosAdapter(Activity activity) {
        this.activity = activity;
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return ImageHolder.getImageDataList().size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view ==  o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = layoutInflater.inflate(R.layout.display_image_view,container,false);

        ZoomageView imageView = itemView.findViewById(R.id.display_photo_image_view);
        Glide.with(activity).load(ImageHolder.getImageDataList().get(position).getPath()).into(imageView);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
