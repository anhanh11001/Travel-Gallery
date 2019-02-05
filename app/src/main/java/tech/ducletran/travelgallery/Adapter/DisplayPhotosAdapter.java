package tech.ducletran.travelgallery.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.jsibbold.zoomage.ZoomageView;
import tech.ducletran.travelgallery.Activities.DisplayImageActivity;
import tech.ducletran.travelgallery.ImageData.ImageData;
import tech.ducletran.travelgallery.ImageData.ImageHolder;
import tech.ducletran.travelgallery.R;


public class DisplayPhotosAdapter extends PagerAdapter {

    private DisplayImageActivity activity;
    private LayoutInflater layoutInflater;

    public DisplayPhotosAdapter(DisplayImageActivity activity) {
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
        imageView.setOnTouchListener(new View.OnTouchListener() {
            private float x = 0;
            private float y = 0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    x = event.getX();
                    y = event.getY();
                }

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (x < event.getX() + 50 && x > event.getX() - 50
                            && y < event.getY() + 50 && y > event.getY() - 50) {
                        activity.toggleActionBar();
                    }
                }
                return true;
            }
        });

        ImageData currentImage = ImageHolder.getImageDataList().get(position);
        Glide.with(activity).load(currentImage.getPath()).into(imageView);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }


}
