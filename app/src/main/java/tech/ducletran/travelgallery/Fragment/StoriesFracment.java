package tech.ducletran.travelgallery.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import tech.ducletran.travelgallery.Adapter.StoriesAdapter;
import tech.ducletran.travelgallery.ImageData.StoriesManager;
import tech.ducletran.travelgallery.ImageData.Story;
import tech.ducletran.travelgallery.R;

public class StoriesFracment extends Fragment {

    private static StoriesAdapter adapter;
    private static ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stories_view,container,false);

        listView = view.findViewById(R.id.stories_fragment_list_view);
        adapter = new StoriesAdapter(getActivity());
        LinearLayout emptyView = view.findViewById(R.id.stories_empty_view);

        listView.setEmptyView(emptyView);
        listView.setAdapter(adapter);

        return view;
    }


    public static void setStoryFracmentChanged() {
        adapter.notifyDataSetChanged();
        listView.invalidate();
    }

}
