package tech.ducletran.travelgallery.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.maps.android.clustering.ClusterManager;
import tech.ducletran.travelgallery.Activities.ShowCitiesCountriesActivity;
import tech.ducletran.travelgallery.CustomizedClass.CustomClusterRenderer;
import tech.ducletran.travelgallery.ImageData.ImageData;
import tech.ducletran.travelgallery.ImageData.ImageManager;
import tech.ducletran.travelgallery.ImageData.ImageMarker;
import tech.ducletran.travelgallery.R;

import java.io.IOException;
import java.util.*;

public class MapFragment extends Fragment{
    private static int UPDATE_CITY_COUNTRY_DONE = 191;
    private static final String CITY_FILE_KEY = "faosihr9hrf12";
    private static final String COUNTRY_FILE_KEY = "fjoaisnfiodaf";
    private static final String CITY_PREFERENCE_KEY = "fjildajfl";
    private static final String COUNTRY_PREFERENCE_KEY = "fakldsfl";

    private static ClusterManager<ImageMarker> clusterManager;
    private static Set<String> citiesList;
    private static Set<String> countriesList;
    private static List<ImageData> imageList = new ArrayList<>();

    private static boolean isUpdating = false;

    private static TextView citiesCountTextView;
    private static TextView countriesCountTextView;

    private static  Handler handler;
    private static SharedPreferences citySharePref;
    private static SharedPreferences countrySharePref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_view,container,false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map_fragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                setClusterManager(googleMap,getContext());
            }
        });

        citiesCountTextView = view.findViewById(R.id.map_cities_number_text_view);
        countriesCountTextView = view.findViewById(R.id.map_countries_number_text_view);
        LinearLayout linearLayout = view.findViewById(R.id.map_cities_countries_detail);
        Button countButton = view.findViewById(R.id.map_update_button);

        citySharePref = getActivity().getSharedPreferences(CITY_PREFERENCE_KEY,Context.MODE_PRIVATE);
        countrySharePref = getActivity().getSharedPreferences(COUNTRY_PREFERENCE_KEY,Context.MODE_PRIVATE);

        citiesList = citySharePref.getStringSet(CITY_FILE_KEY, null);
        countriesList = countrySharePref.getStringSet(COUNTRY_FILE_KEY,null);

        if (citiesList == null) {
            citiesList = new HashSet<>();
            updateCityPref(citiesList);
        }

        if (countriesList == null) {
            countriesList = new HashSet<>();
            updateCountryPref(countriesList);
        }

        citiesCountTextView.setText("Cities: " + citiesList.size());
        countriesCountTextView.setText("Countries: " + countriesList.size());

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == UPDATE_CITY_COUNTRY_DONE) {
                    citiesCountTextView.setText("Cities " + citiesList.size());
                    countriesCountTextView.setText("Countries: " + countriesList.size());
                }

                super.handleMessage(msg);
            }
        };

        countButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Setting internet connection
                NetworkInfo activeNetwork = ((ConnectivityManager) getActivity().
                        getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
                if (activeNetwork == null || !activeNetwork.isConnectedOrConnecting()) {
                    Toast.makeText(getActivity(),"There is no internet connection",Toast.LENGTH_SHORT).show();
                } else if (isUpdating) {
                    Toast.makeText(getActivity(),"Updating...",Toast.LENGTH_SHORT).show();
                } else if (imageList.size() == 0) {
                    Toast.makeText(getActivity(),"Already updated",Toast.LENGTH_SHORT).show();
                } else {
                    countCitiesCountries(getActivity());
                }
            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUpdating) {
                    Toast.makeText(getActivity(),"Updating...",Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getActivity(), ShowCitiesCountriesActivity.class);

                    startActivity(intent);
                }
            }
        });

        return view;
    }

    private static void updateCityPref(Set<String> citiesSet) {
        SharedPreferences.Editor editor = citySharePref.edit();
        editor.putStringSet(CITY_FILE_KEY,citiesSet);
        Log.d("hey","Added new city" + citiesSet);
        editor.clear();
        editor.commit();
    }
    private static void updateCountryPref(Set<String> countriesSet) {
        SharedPreferences.Editor editor = countrySharePref.edit();
        editor.putStringSet(COUNTRY_FILE_KEY,countriesSet);
        Log.d("hey","Added new country" + countriesSet);
        editor.clear();
        editor.commit();
    }
    public static Set<String> getCitiesList() {return citiesList;}
    public static Set<String> getCountriesList() {return countriesList;}
    public static void editCity(String oldName, String newName) {
        citiesList.remove(oldName);
        citiesList.add(newName);
        updateCityPref(citiesList);
    }

    public static void addCity(String cityName) {
        citiesList.add(cityName);
        updateCityPref(citiesList);
        citiesCountTextView.setText("Cities: " + citiesList.size());
    }

    public static void deleteCity(String cityName) {
        citiesList.remove(cityName);
        updateCityPref(citiesList);
        citiesCountTextView.setText("Cities: " + citiesList.size());
    }

    public static void editCountry(String oldName, String newName) {
        countriesList.remove(oldName);
        countriesList.add(newName);
        updateCountryPref(countriesList);
    }

    public static void addCountry(String countryName) {
        countriesList.add(countryName);
        updateCountryPref(countriesList);
        countriesCountTextView.setText("Countries: " + countriesList.size());
    }

    public static void deleteCountry(String countryName) {
        countriesList.remove(countryName);
        updateCountryPref(countriesList);
        countriesCountTextView.setText("Countries: " + countriesList.size());
    }

    private void setClusterManager(GoogleMap map, Context context) {
        clusterManager = new ClusterManager<>(context,map);
        clusterManager.setRenderer(new CustomClusterRenderer(context,map,clusterManager));

        map.setOnCameraIdleListener(clusterManager);
        map.setOnMarkerClickListener(clusterManager);

        addImageMarkers();
    }

    private void addImageMarkers() {
        for (ImageData image:ImageManager.getImageDataList()) {
            if (image.getImageMarker() != null) {
                clusterManager.addItem(image.getImageMarker());
                if (!image.getIsLocationCounted()) {
                    imageList.add(image);
                }
            }
        }

        clusterManager.cluster();

    }

    public static void addNewImageMarker(ImageMarker imageMarker) {
        ImageData image = imageMarker.getImageData();
        if (!image.getIsLocationCounted()) {
            imageList.add(image);
        }

        clusterManager.addItem(imageMarker);
        clusterManager.cluster();
    }

    private static void countCitiesCountries(Context context) {
        citiesCountTextView.setText("Cities: Updating...");
        countriesCountTextView.setText("Countries: Updating...");

        isUpdating = true;
        new CountCityCountries(context,imageList,citiesList,countriesList).start();
    }


    private static void updateCitiesCounties(Set<String> newCitiesSet, Set<String> newCountriesSet) {
        citiesList = newCitiesSet;
        countriesList = newCountriesSet;
        isUpdating = false;
        imageList.clear();
        updateCountryPref(countriesList);
        updateCityPref(citiesList);

        Message message = handler.obtainMessage();
        message.what = UPDATE_CITY_COUNTRY_DONE;
        handler.sendMessage(message);

    }

    private static class CountCityCountries extends Thread {
        private List<ImageData> imageDataList;
        private Set<String> currentCitySet;
        private Set<String> currentCountrySet;
        private Context context;

        private CountCityCountries(Context context,List<ImageData> imageDataList, Set<String> currentCitySet, Set<String> currentCountrySet) {
            this.imageDataList = imageDataList;
            this.currentCitySet = currentCitySet;
            this.currentCountrySet = currentCountrySet;
            this.context = context;
        }

        @Override
        public void run() {
            for(ImageData image: imageDataList) {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                List<Address> addresses = null;
                Double latitude = Double.valueOf(image.getLatitude());
                Double longitude = Double.valueOf(image.getLongtitude());
                try {
                    addresses = geocoder.getFromLocation(latitude,longitude, 1);
                    if (addresses.size() == 1) {
                        String cityName = addresses.get(0).getLocality();
                        String countryName = addresses.get(0).getCountryName();
                        if (!currentCitySet.contains(cityName) && !TextUtils.isEmpty(cityName)) {
                            currentCitySet.add(cityName);

                            if (!currentCountrySet.contains(countryName) && !TextUtils.isEmpty(countryName)) {
                                currentCountrySet.add(countryName);
                            }
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                image.setIsLocationCounted();
            }

            updateCitiesCounties(currentCitySet,currentCountrySet);
        }
    }
}
