package tech.ducletran.travelgallery.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import tech.ducletran.travelgallery.Activities.DisplayAlbumImagesActivity;
import tech.ducletran.travelgallery.Adapter.AlbumAdapter;
import tech.ducletran.travelgallery.CustomizedClass.HorizontalListView;
import tech.ducletran.travelgallery.Model.Album;
import tech.ducletran.travelgallery.Model.AlbumManager;
import tech.ducletran.travelgallery.Model.ImageManager;
import tech.ducletran.travelgallery.R;

public class AlbumsFragment extends Fragment {
    public static final String PEOPLE_PREFERENCE_KEY = "asfdva";
    public static final String PEOPLE_COVER_STRING_KEY = "afasvafd";
    public static final String FOOD_PREFERENCE_KEY = "asdbabs";
    public static final String FOOD_COVER_STRING_KEY = "asdvas";
    public static final String FAVORITE_PREFERENCE_KEY = "dsavvvas";
    public static final String FAVORITE_COVER_STRING_KEY = "dfavs";

    private static SharedPreferences peopleSharePref;
    private static SharedPreferences foodSharePref;
    private static SharedPreferences favoriteSharePref;

    private static AlbumAdapter specialAlbumAdapter;
    private static AlbumAdapter locationAlbumAdapter;
    private static AlbumAdapter othersAlbumAdapter;
    private static HorizontalListView specialAlbumListView;
    private static HorizontalListView locationAlbumListView;
    private static HorizontalListView othersAlbumListView;

    private static boolean albumLoaded = false;
    private static boolean albumLoaded2 = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums_view,container,false);

        setUpCoreAlbumCover();

        specialAlbumListView = view.findViewById(R.id.base_album_grid_view);
        locationAlbumListView = view.findViewById(R.id.location_album_grid_view);
        othersAlbumListView = view.findViewById(R.id.others_album_grid_view);
        specialAlbumAdapter = new AlbumAdapter(getActivity(),AlbumManager.getSpecialAlbum());
        locationAlbumAdapter = new AlbumAdapter(getActivity(),AlbumManager.getLocationAlbum());
        othersAlbumAdapter = new AlbumAdapter(getActivity(),AlbumManager.getOthersAlbum());
        specialAlbumListView.setAdapter(specialAlbumAdapter);
        locationAlbumListView.setAdapter(locationAlbumAdapter);
        othersAlbumListView.setAdapter(othersAlbumAdapter);

        specialAlbumListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),DisplayAlbumImagesActivity.class);
                intent.putExtra("album_id",AlbumManager.getSpecialAlbum().get(position).getAlbumId());
                startActivity(intent);
            }
        });

        locationAlbumListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),DisplayAlbumImagesActivity.class);
                intent.putExtra("album_id",AlbumManager.getLocationAlbum().get(position).getAlbumId());
                startActivity(intent);
            }
        });

        othersAlbumListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),DisplayAlbumImagesActivity.class);
                intent.putExtra("album_id",AlbumManager.getOthersAlbum().get(position).getAlbumId());
                startActivity(intent);
            }
        });

        if (!albumLoaded) {
            new LoadAlbumAsyncTask(getActivity()).execute();
            albumLoaded = true;
        }
        return view;
    }

    public static void setAlbumFragmentChanged(int albumType) {
        if (albumType == Album.ALBUM_TYPE_LOCATION) {
            locationAlbumAdapter.notifyDataSetChanged();
            locationAlbumListView.invalidate();
        } else if (albumType == Album.ALBUM_TYPE_SPECIAL) {
            specialAlbumAdapter.notifyDataSetChanged();
            specialAlbumListView.invalidate();
        } else {
            othersAlbumAdapter.notifyDataSetChanged();
            othersAlbumListView.invalidate();
        }
    }

    private void setUpCoreAlbumCover() {
        foodSharePref = getActivity().getSharedPreferences(FOOD_PREFERENCE_KEY, Context.MODE_PRIVATE);
        favoriteSharePref = getActivity().getSharedPreferences(FAVORITE_PREFERENCE_KEY,Context.MODE_PRIVATE);
        peopleSharePref = getActivity().getSharedPreferences(PEOPLE_PREFERENCE_KEY,Context.MODE_PRIVATE);

        String foodCover = foodSharePref.getString(FOOD_COVER_STRING_KEY,null);
        String favoriteCover = favoriteSharePref.getString(FAVORITE_COVER_STRING_KEY,null);
        String peopleCover = peopleSharePref.getString(PEOPLE_COVER_STRING_KEY,null);

        AlbumManager.getAlbum(Album.DEFAULT_FAVORITE_ID).setSpecialAlbumCover(favoriteCover);
        AlbumManager.getAlbum(Album.DEFAULT_FOOD_ID).setSpecialAlbumCover(foodCover);
        AlbumManager.getAlbum(Album.DEFAULT_PEOPLE_ID).setSpecialAlbumCover(peopleCover);
    }

    private class LoadAlbumAsyncTask extends AsyncTask<Void,Void,Void> {
        private Context context;
        private boolean done = false;
        private LoadAlbumAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ImageManager.loadAlbum(context);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            locationAlbumAdapter.notifyDataSetChanged();
            othersAlbumAdapter.notifyDataSetChanged();
            specialAlbumAdapter.notifyDataSetChanged();
            locationAlbumListView.invalidate();
            specialAlbumListView.invalidate();
            othersAlbumListView.invalidate();
            Toast.makeText(context,"Albums and stories uploaded",Toast.LENGTH_LONG).show();
            if (!albumLoaded2) {
                albumLoaded2 = true;
                new LoadAlbumAsyncTask(context).execute();
            }
        }
    }
}