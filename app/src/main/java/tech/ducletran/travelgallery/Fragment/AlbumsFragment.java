package tech.ducletran.travelgallery.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;
import tech.ducletran.travelgallery.Activities.DisplayAlbumImagesActivity;
import tech.ducletran.travelgallery.Adapter.BaseAlbumAdapter;
import tech.ducletran.travelgallery.R;

public class AlbumsFragment extends Fragment {
    private HorizontalGridView baseGridView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums_view,container,false);

        baseGridView = view.findViewById(R.id.base_album_grid_view);
        HorizontalGridView locationGridView = view.findViewById(R.id.location_album_grid_view);
        HorizontalGridView othersGridView = view.findViewById(R.id.others_album_grid_view);

        BaseAlbumAdapter baseAlbumAdapter = new BaseAlbumAdapter(getActivity());
        baseGridView.setAdapter(baseAlbumAdapter);

        baseAlbumAdapter.setOnItemClickListener(new BaseAlbumAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent = new Intent(getActivity(),DisplayAlbumImagesActivity.class);
                intent.putExtra("album_position",position);
                startActivity(intent);
            }
        });

        return view;
    }


}