package tech.ducletran.travelgallery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import tech.ducletran.travelgallery.ImageData.StoriesManager;
import tech.ducletran.travelgallery.ImageData.Story;
import tech.ducletran.travelgallery.R;


public class StoriesAdapter extends BaseAdapter {

    private Context context;

    public StoriesAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return StoriesManager.getStoryList().size();
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
        convertView = LayoutInflater.from(context).inflate(R.layout.stories_item_view, parent, false);

        TextView textView = convertView.findViewById(R.id.stories_title_text_view);
        ImageView imageView = convertView.findViewById(R.id.stories_cover_image_view);

        Story story = StoriesManager.getStoryList().get(position);
        textView.setText(story.getTitle());
        if (story.getCover() != null) {
            Glide.with(context).load(story.getCover()).into(imageView);
        }

        return convertView;
    }
}
