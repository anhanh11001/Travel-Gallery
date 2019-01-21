package tech.ducletran.travelgallery.Fragment;

import android.content.Intent;
import android.os.Bundle;
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
import tech.ducletran.travelgallery.PhotosAdapter;
import tech.ducletran.travelgallery.R;

public class PhotosFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photos_view,container,false);

        GridView gridView = view.findViewById(R.id.photos_grid_view);
        PhotosAdapter adapter = new PhotosAdapter(getActivity());
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent (getActivity(),DisplayImageActivity.class);
                intent.putExtra("id",PhotosAdapter.test_image_id[position]);
                startActivity(intent);
            }
        });

        return view;
    }
}
