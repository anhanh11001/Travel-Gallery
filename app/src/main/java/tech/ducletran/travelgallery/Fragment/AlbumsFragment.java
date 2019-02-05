package tech.ducletran.travelgallery.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import tech.ducletran.travelgallery.Adapter.BaseAlbumAdapter;
import tech.ducletran.travelgallery.R;

public class AlbumsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums_view,container,false);

        HorizontalGridView baseGridView = view.findViewById(R.id.base_album_grid_view);
        HorizontalGridView locationGridView = view.findViewById(R.id.location_album_grid_view);
        HorizontalGridView othersGridView = view.findViewById(R.id.others_album_grid_view);

        BaseAlbumAdapter baseAlbumAdapter = new BaseAlbumAdapter(getActivity());
        baseGridView.setAdapter(baseAlbumAdapter);

        return view;
    }
}