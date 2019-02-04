package tech.ducletran.travelgallery.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import tech.ducletran.travelgallery.Activities.DisplayImageActivity;
import tech.ducletran.travelgallery.Adapter.PhotosAdapter;
import tech.ducletran.travelgallery.ImageHolder;
import tech.ducletran.travelgallery.R;

public class PhotosFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private GridView gridView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photos_view,container,false);

        gridView = view.findViewById(R.id.photos_grid_view);
        PhotosAdapter adapter = new PhotosAdapter(getActivity());
        gridView.setAdapter(adapter);
        setUpGridView();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent (getActivity(),DisplayImageActivity.class);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.action_settings_sort_key))) {
            boolean isSorted = sharedPreferences.getBoolean(key,true);
            if (isSorted) {
                ImageHolder.sortByDate();
            } else {
                ImageHolder.shuffle();
            }
        }

        if (key.equals(getString(R.string.action_settings_columns_key))) {
            int numColumns = Integer.parseInt(sharedPreferences.getString(key,"4"));
            gridView.setNumColumns(numColumns);
        }
    }

    private void setUpGridView() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String numCols = sharedPreferences.getString(getString(R.string.action_settings_columns_key),"4");
        setGridViewCols(Integer.parseInt(numCols));
    }

    private void setGridViewCols(int numOfColumns) {
        gridView.setNumColumns(numOfColumns);
    }
}
