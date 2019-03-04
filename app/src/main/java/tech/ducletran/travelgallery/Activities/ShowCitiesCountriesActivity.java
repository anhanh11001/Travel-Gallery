package tech.ducletran.travelgallery.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.*;
import android.widget.EditText;
import tech.ducletran.travelgallery.Adapter.CityCountryAdapter;
import tech.ducletran.travelgallery.Fragment.CityFragment;
import tech.ducletran.travelgallery.Fragment.CountryFragment;
import tech.ducletran.travelgallery.Fragment.MapFragment;
import tech.ducletran.travelgallery.R;

public class ShowCitiesCountriesActivity extends AppCompatActivity {
    private TabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(MainActivity.getAppTheme());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cities_countries);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager viewPager = findViewById(R.id.activity_city_country_view_pager);
        CityCountryAdapter adapter = new CityCountryAdapter(getSupportFragmentManager(),this);

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);

        tabLayout = findViewById(R.id.activity_city_country_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.show_cities_countries_activity_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_adding_city_country) {
            addNewItem(tabLayout.getSelectedTabPosition() == 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addNewItem(final boolean isCity) {
        AlertDialog.Builder addItemDialog = new AlertDialog.Builder(this);
        if (isCity) {
            addItemDialog.setTitle("Add a new city");
            addItemDialog.setMessage("Enter the name of the new city");
        } else {
            addItemDialog.setTitle("Add a new country");
            addItemDialog.setMessage("Enter the name of the new country");
        }


        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_rename_album_layout,null);
        final EditText dialogEditText = dialogView.findViewById(R.id.album_rename_edit_text);

        addItemDialog.setView(dialogView);

        addItemDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = dialogEditText.getText().toString();

                if (!TextUtils.isEmpty(name)) {
                    if (isCity) {
                        MapFragment.addCity(name);
                        CityFragment.addNewCity(name);
                    } else {
                        MapFragment.addCountry(name);
                        CountryFragment.addCountry(name);
                    }
                }

                dialog.cancel();
            }
        });

        addItemDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        addItemDialog.show();
    }

}
