package tech.ducletran.travelgallery.Adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import tech.ducletran.travelgallery.Fragment.CityFragment;
import tech.ducletran.travelgallery.Fragment.CountryFragment;

public class CityCountryAdapter extends FragmentPagerAdapter {

    private Context context;
    private FragmentManager fm;
    public CityCountryAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.fm = fm;
        this.context = context;
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return new CityFragment();
        } else if (i == 1) {
            return new CountryFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Cities";
        } else if (position == 1) {
            return "Countries";
        }
        return super.getPageTitle(position);
    }
}
