package tech.ducletran.travelgallery.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import tech.ducletran.travelgallery.Activities.DisplayAlbumImagesActivity;
import tech.ducletran.travelgallery.Adapter.AlbumAdapter;
import tech.ducletran.travelgallery.ImageData.AlbumManager;
import tech.ducletran.travelgallery.R;

public class AlbumsFragment extends Fragment {
    private static boolean isAlbumFragmentChanged = false;

    private AlbumAdapter specialAlbumAdapter;
    private AlbumAdapter locationAlbumAdapter;
    private AlbumAdapter othersAlbumAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums_view,container,false);

        HorizontalGridView specialAlbumGridView = view.findViewById(R.id.base_album_grid_view);
        HorizontalGridView locationAlbumGridView = view.findViewById(R.id.location_album_grid_view);
        HorizontalGridView othersAlbumGridView = view.findViewById(R.id.others_album_grid_view);

        specialAlbumAdapter = new AlbumAdapter(getActivity(),AlbumManager.getSpecialAlbum());
        locationAlbumAdapter = new AlbumAdapter(getActivity(),AlbumManager.getLocationAlbum());
        othersAlbumAdapter = new AlbumAdapter(getActivity(),AlbumManager.getOthersAlbum());
        specialAlbumGridView.setAdapter(specialAlbumAdapter);
        locationAlbumGridView.setAdapter(locationAlbumAdapter);
        othersAlbumGridView.setAdapter(othersAlbumAdapter);

        AlbumAdapter.ClickListener clickListener = new AlbumAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent = new Intent(getActivity(),DisplayAlbumImagesActivity.class);
                intent.putExtra("album_position",position);
                startActivity(intent);
            }
        };

        specialAlbumAdapter.setOnItemClickListener(clickListener);
        locationAlbumAdapter.setOnItemClickListener(clickListener);
        othersAlbumAdapter.setOnItemClickListener(clickListener);

        return view;
    }

    public static void setAlbumFragmentChanged() {
        isAlbumFragmentChanged = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isAlbumFragmentChanged) {
            getActivity().recreate();
            isAlbumFragmentChanged = false;

            locationAlbumAdapter.notifyDataSetChanged();
            specialAlbumAdapter.notifyDataSetChanged();
            othersAlbumAdapter.notifyDataSetChanged();

        }
    }

}