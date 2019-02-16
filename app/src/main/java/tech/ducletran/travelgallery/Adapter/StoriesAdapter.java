package tech.ducletran.travelgallery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import tech.ducletran.travelgallery.Model.Story;
import tech.ducletran.travelgallery.R;

import java.util.List;


public class StoriesAdapter extends BaseAdapter {

    private Context context;
    private List<Story> storyList;

    public StoriesAdapter(Context context, List<Story> storyList) {
        this.storyList = storyList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return storyList.size();
    }

    @Override
    public Story getItem(int position) {
        return storyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_stories, parent, false);

        TextView textView = convertView.findViewById(R.id.stories_title_text_view);
        ImageView imageView = convertView.findViewById(R.id.stories_cover_image_view);

        Story story = storyList.get(position);
        textView.setText(story.getTitle());
        if (story.getCover() != null) {
            Glide.with(context).load(story.getCover()).into(imageView);
        }

        return convertView;
    }
}
