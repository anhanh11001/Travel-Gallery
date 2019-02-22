package tech.ducletran.travelgallery.Adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import org.json.JSONArray;
import org.json.JSONException;
import tech.ducletran.travelgallery.Fragment.StoryPageFragment;
import tech.ducletran.travelgallery.Model.StoriesManager;
import tech.ducletran.travelgallery.Model.Story;
import tech.ducletran.travelgallery.R;

import java.util.ArrayList;

public class StoryPageAdapter extends FragmentStatePagerAdapter {
    private Story story;
    private ArrayList<String> storyDetails;
    public StoryPageAdapter(FragmentManager fm, int storyId) throws JSONException {
        super(fm);
        story = StoriesManager.getStoryById(storyId);
        updateStoryDetails();
    }

    public void updateStoryDetails()  throws JSONException {
        if (!TextUtils.isEmpty(story.getDetails())) {
            JSONArray jsonArray = new JSONArray(story.getDetails());
            storyDetails = new ArrayList<>();
            for (int i = 0;i < jsonArray.length(); i++) {
                storyDetails.add(i,jsonArray.getJSONObject(i).toString());
            }
        }
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            FirstPageFragment fragment = new FirstPageFragment();
            Bundle bundle = new Bundle();
            bundle.putString("story_cover",story.getCover());
            bundle.putString("story_title",story.getTitle());
            bundle.putString("story_description",story.getDescription());
            fragment.setArguments(bundle);
            return fragment;
        } else if (i == story.getNumOfPages() + 1) {
            return new FinalPageFragment();
        } else {
            StoryPageFragment fragment = new StoryPageFragment();
            Bundle bundle = new Bundle();
            bundle.putString("page_details", storyDetails.get(i - 1));
            fragment.setArguments(bundle);
            return fragment;
        }
    }

    @Override
    public int getCount() {
        return story.getNumOfPages()+2;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        return super.instantiateItem(container, position);
    }

    public static class FirstPageFragment extends Fragment {
        public FirstPageFragment() {
            super();
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.story_page_front_page_view,container,false);
            Bundle bundle = getArguments();
            String description = bundle.getString("story_description");
            String title = bundle.getString("story_title");
            String cover = bundle.getString("story_cover");
            TextView descriptionTextView = view.findViewById(R.id.story_front_page_description_text_view);

            if (!TextUtils.isEmpty(description)) {
                descriptionTextView.setText(description);
            }
            ((TextView) view.findViewById(R.id.story_front_page_title_text_view)).setText(title);
            Glide.with(getContext()).load(cover).into((ImageView) view.findViewById(R.id.story_front_page_image_view));


            return view;
        }
    }

    public static class FinalPageFragment extends Fragment {
        public FinalPageFragment() {super();}

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.story_page_ending_page_view,container,false);
        }
    }
}
