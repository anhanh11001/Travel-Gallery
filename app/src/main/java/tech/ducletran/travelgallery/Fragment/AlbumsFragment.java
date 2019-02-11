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
import tech.ducletran.travelgallery.ImageData.Album;
import tech.ducletran.travelgallery.ImageData.AlbumManager;
import tech.ducletran.travelgallery.R;

public class AlbumsFragment extends Fragment {
    private static AlbumAdapter specialAlbumAdapter;
    private static AlbumAdapter locationAlbumAdapter;
    private static AlbumAdapter othersAlbumAdapter;
    private static HorizontalGridView specialAlbumGridView;
    private static HorizontalGridView locationAlbumGridView;
    private static HorizontalGridView othersAlbumGridView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums_view,container,false);

        specialAlbumGridView = view.findViewById(R.id.base_album_grid_view);
        locationAlbumGridView = view.findViewById(R.id.location_album_grid_view);
        othersAlbumGridView = view.findViewById(R.id.others_album_grid_view);

        specialAlbumAdapter = new AlbumAdapter(getActivity(),AlbumManager.getSpecialAlbum());
        locationAlbumAdapter = new AlbumAdapter(getActivity(),AlbumManager.getLocationAlbum());
        othersAlbumAdapter = new AlbumAdapter(getActivity(),AlbumManager.getOthersAlbum());
        specialAlbumGridView.setAdapter(specialAlbumAdapter);
        locationAlbumGridView.setAdapter(locationAlbumAdapter);
        othersAlbumGridView.setAdapter(othersAlbumAdapter);

        AlbumAdapter.ClickListener specialClickListener = new AlbumAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent = new Intent(getActivity(),DisplayAlbumImagesActivity.class);
                intent.putExtra("album_id",AlbumManager.getSpecialAlbum().get(position).getAlbumId());
                startActivity(intent);
            }
        };
        AlbumAdapter.ClickListener locationClickListener = new AlbumAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent = new Intent(getActivity(),DisplayAlbumImagesActivity.class);
                intent.putExtra("album_id",AlbumManager.getLocationAlbum().get(position).getAlbumId());
                startActivity(intent);
            }
        };
        AlbumAdapter.ClickListener othersClickListener = new AlbumAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent = new Intent(getActivity(),DisplayAlbumImagesActivity.class);
                intent.putExtra("album_id",AlbumManager.getOthersAlbum().get(position).getAlbumId());
                startActivity(intent);
            }
        };

        specialAlbumAdapter.setOnItemClickListener(specialClickListener);
        locationAlbumAdapter.setOnItemClickListener(locationClickListener);
        othersAlbumAdapter.setOnItemClickListener(othersClickListener);

        return view;
    }

    public static void setAlbumFragmentChanged(int albumType) {
        if (albumType == Album.ALBUM_TYPE_LOCATION) {
            locationAlbumAdapter.notifyDataSetChanged();
            locationAlbumGridView.invalidate();
        } else if (albumType == Album.ALBUM_TYPE_SPECIAL) {
            specialAlbumAdapter.notifyDataSetChanged();
            specialAlbumGridView.invalidate();
        } else {
            othersAlbumAdapter.notifyDataSetChanged();
            othersAlbumGridView.invalidate();
        }



    }

}