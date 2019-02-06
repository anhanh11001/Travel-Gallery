package tech.ducletran.travelgallery.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import tech.ducletran.travelgallery.Activities.DisplayImageActivity;
import tech.ducletran.travelgallery.Adapter.PhotosAdapter;
import tech.ducletran.travelgallery.ImageData.ImageData;
import tech.ducletran.travelgallery.ImageData.ImageHolder;
import tech.ducletran.travelgallery.R;

import java.util.List;

public class PhotosFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private GridView gridView;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null) {
            return this.view;
        }
        view = inflater.inflate(R.layout.fragment_photos_view,container,false);
        PhotosAdapter adapter = new PhotosAdapter(getActivity(),ImageHolder.getImageDataList());
        DisplayImageActivity.setImageDataList(ImageHolder.getImageDataList());

        gridView = view.findViewById(R.id.photos_grid_view);
        gridView.setAdapter(adapter);
        setUpNumCols();

        PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent (getActivity(),DisplayImageActivity.class);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return true;
            }
        });
        return view;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.action_settings_columns_key))) {
            int numColumns = Integer.parseInt(sharedPreferences.getString(key,"4"));
            gridView.setNumColumns(numColumns);
        }
        if (key.equals(getString(R.string.action_settings_sort_key))) {
            boolean isSorted = sharedPreferences.getBoolean(key,true);
            if (isSorted) {
                ImageHolder.sortByDate();
            } else {
                ImageHolder.shuffle();
            }
        }
    }

    private void setUpNumCols() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String numCols = sharedPreferences.getString(getString(R.string.action_settings_columns_key),"4");
        setGridViewCols(Integer.parseInt(numCols));
    }

    private void setGridViewCols(int numOfColumns) {
        gridView.setNumColumns(numOfColumns);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PreferenceManager.getDefaultSharedPreferences(getActivity()).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        DisplayImageActivity.setImageDataList(ImageHolder.getImageDataList());
    }
}
