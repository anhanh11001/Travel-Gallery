package tech.ducletran.travelgallery.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import tech.ducletran.travelgallery.Activities.DisplayStoryActivity;
import tech.ducletran.travelgallery.Adapter.StoriesAdapter;
import tech.ducletran.travelgallery.Model.StoriesManager;
import tech.ducletran.travelgallery.R;

public class StoriesFracment extends Fragment {

    private static StoriesAdapter adapter;
    private static ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stories_view,container,false);

        listView = view.findViewById(R.id.stories_fragment_list_view);
        adapter = new StoriesAdapter(getActivity(),StoriesManager.getStoryList());
        LinearLayout emptyView = view.findViewById(R.id.stories_empty_view);

        listView.setEmptyView(emptyView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int storyId = adapter.getItem(position).getStoryId();

                Intent intent = new Intent(getActivity(), DisplayStoryActivity.class);
                intent.putExtra("story_id",storyId);
                startActivity(intent);
            }
        });

        return view;
    }


    public static void setStoryFracmentChanged() {
        adapter.notifyDataSetChanged();
        listView.invalidate();
    }

}
