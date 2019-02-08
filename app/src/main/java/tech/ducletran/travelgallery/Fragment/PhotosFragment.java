package tech.ducletran.travelgallery.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import tech.ducletran.travelgallery.Activities.DisplayImageActivity;
import tech.ducletran.travelgallery.Adapter.PhotosAdapter;
import tech.ducletran.travelgallery.ImageData.ImageData;
import tech.ducletran.travelgallery.ImageData.ImageManager;
import tech.ducletran.travelgallery.R;

import java.util.ArrayList;

public class PhotosFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener,
        LoaderManager.LoaderCallbacks<ArrayList<ImageData>> {
    private static boolean arePhotoChanged = false;

    private GridView gridView;
    private LinearLayout loadingLayout;
    private View view;
    private LinearLayout emptyView;
    private PhotosAdapter adapter;

    public PhotosFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_photos_view,container,false);
        adapter = new PhotosAdapter(getActivity(),new ArrayList<ImageData>());
        gridView = view.findViewById(R.id.photos_grid_view);
        loadingLayout = view.findViewById(R.id.photos_loading_layout);
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
                startActivity(intent);
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return true;
            }
        });

        // Setting the LoaderManager
        getLoaderManager().initLoader(1,null,this).forceLoad();

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
            getActivity().recreate();
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

    public static void setArePhotoChanged() {
        arePhotoChanged = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (arePhotoChanged) {
            getActivity().recreate();
            arePhotoChanged = false;
            adapter.notifyDataSetChanged();
        }
        DisplayImageActivity.setImageDataList(ImageManager.getImageDataList());

    }

    // Loader Manager
    @NonNull
    @Override
    public Loader<ArrayList<ImageData>> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new ImageLoader(getActivity());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<ImageData>> loader, ArrayList<ImageData> imageData) {
        gridView.setEmptyView(emptyView);
        loadingLayout.setVisibility(View.GONE);
        if (adapter != null) {
            adapter.clear();
        }

        adapter.addAll(imageData);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<ImageData>> loader) {
        adapter.clear();
    }

    private static class ImageLoader extends AsyncTaskLoader<ArrayList<ImageData>> {

        private ImageLoader(@NonNull Context context) {
            super(context);
        }

        @Nullable
        @Override
        public ArrayList<ImageData> loadInBackground() {
            if (ImageManager.getImageDataList().size() != 0) {
                setUpDateSorting(getContext());
                DisplayImageActivity.setImageDataList(ImageManager.getImageDataList());
                return ImageManager.getImageDataList();
            }
            ImageManager.loadImage(getContext());
            setUpDateSorting(getContext());
            DisplayImageActivity.setImageDataList(ImageManager.getImageDataList());
            return ImageManager.getImageDataList();
        }


    }

    private static void setUpDateSorting(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean isSorted = sharedPreferences.getBoolean(context.getString(R.string.action_settings_sort_key),true);
        if (isSorted) {
            ImageManager.sortByDate();
        } else {
            ImageManager.shuffle();
        }
    }

}
