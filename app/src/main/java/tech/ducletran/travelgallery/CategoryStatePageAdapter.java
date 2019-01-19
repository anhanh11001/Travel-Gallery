package tech.ducletran.travelgallery;

import android.content.Context;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import tech.ducletran.travelgallery.Fragment.AlbumsFragment;
import tech.ducletran.travelgallery.Fragment.MapFragment;
import tech.ducletran.travelgallery.Fragment.PhotosFragment;
import tech.ducletran.travelgallery.Fragment.StoriesFracment;

public class CategoryStatePageAdapter extends FragmentPagerAdapter {
    private Context context;
    public CategoryStatePageAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }
    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new PhotosFragment();
            case 1:
                return new AlbumsFragment();
            case 2:
                return new StoriesFracment();
            case 3:
                return new MapFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getResources().getString(R.string.photos_fragment_title);
            case 1:
                return context.getResources().getString(R.string.albums_fragment_title);
            case 2:
                return context.getResources().getString(R.string.stories_fragment_title);
            case 3:
                return context.getResources().getString(R.string.map_fragment_title);
            default:
                return null;
        }
    }
}
