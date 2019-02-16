package tech.ducletran.travelgallery.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import tech.ducletran.travelgallery.Activities.DisplayImageActivity;
import tech.ducletran.travelgallery.Adapter.PhotosAdapter;
import tech.ducletran.travelgallery.Model.ImageData;
import tech.ducletran.travelgallery.Model.ImageManager;
import tech.ducletran.travelgallery.R;

import java.util.ArrayList;

public class PhotosFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener//, LoaderManager.LoaderCallbacks<ArrayList<ImageData>> {
{
    private static GridView gridView;
    private View view;
    private LinearLayout emptyView;
    private static PhotosAdapter adapter;


    public PhotosFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_photos_view,container,false);
        adapter = new PhotosAdapter(getActivity(),new ArrayList<ImageData>());
        gridView = view.findViewById(R.id.photos_grid_view);
        emptyView = view.findViewById(R.id.photos_empty_view);
        emptyView.setVisibility(View.INVISIBLE);
        
        gridView.setAdapter(adapter);
        setUpNumCols();

        PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent (getActivity(),DisplayImageActivity.class);
                intent.putExtra("position",position);
                DisplayImageActivity.setImageDataList(ImageManager.getImageDataList());
                startActivity(intent);
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return true;
            }
        });

        // Set up adapter
        adapter.clear();
        gridView.setEmptyView(emptyView);
        if (adapter != null) {
            adapter.clear();
        }

        ImageManager.resetImageDataList();
        setUpDateSorting(getContext());
        DisplayImageActivity.setImageDataList(ImageManager.getImageDataList());
        adapter.addAll(ImageManager.getImageDataList());
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
                ImageManager.sortByDate();
            } else {
                ImageManager.shuffle();
            }
            PhotosFragment.setPhotoFragmentChanged(getActivity(),0);
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

    public static void setPhotoFragmentChanged(Context context, int nextPosition) {
        ImageManager.resetImageDataList();
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.action_settings_sort_key),true)) {
            ImageManager.sortByDate();
        } else {
            ImageManager.shuffle();
        }
        adapter.clear();
        for (ImageData image: ImageManager.getImageDataList()) {
            adapter.add(image);
        }
        adapter.notifyDataSetChanged();
        gridView.invalidate();
        gridView.smoothScrollToPosition(nextPosition);
    }

    private static void setUpDateSorting(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isSorted = sharedPreferences.getBoolean(context.getString(R.string.action_settings_sort_key),true);
        if (isSorted) {
            ImageManager.sortByDate();
        } else {
            ImageManager.shuffle();
        }
    }

}
