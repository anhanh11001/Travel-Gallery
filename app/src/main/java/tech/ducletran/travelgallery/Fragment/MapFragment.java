package tech.ducletran.travelgallery.Fragment;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;
import org.w3c.dom.Text;
import tech.ducletran.travelgallery.Activities.MainActivity;
import tech.ducletran.travelgallery.CustomizedClass.CustomClusterRenderer;
import tech.ducletran.travelgallery.ImageData.ImageData;
import tech.ducletran.travelgallery.ImageData.ImageManager;
import tech.ducletran.travelgallery.ImageData.ImageMarker;
import tech.ducletran.travelgallery.R;

import java.io.IOException;
import java.util.*;

public class MapFragment extends Fragment{

    private static ClusterManager<ImageMarker> clusterManager;
    private static int countCities = 0;
    private static int countCountries = 0;
    private static Set<String> citiesList = new HashSet<>();
    private static Set<String> countriesList = new HashSet<>();
    private static List<ImageData> imageList = new ArrayList<>();

    private static TextView citiesCountTextView;
    private static TextView countriesCountTextView;

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

        countButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countCitiesCounties(getActivity());
            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
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
                imageList.add(image);
            }
        }

        clusterManager.cluster();

    }

    public static void addNewImageMarker(ImageMarker imageMarker) {
        clusterManager.addItem(imageMarker);
        clusterManager.cluster();
    }

    private static void countCitiesCounties(Context context) {
        citiesCountTextView.setText("Cities: Updating...");
        countriesCountTextView.setText("Countries: Updating...");

        new CountCityCountries(context,imageList,citiesList,countriesList).start();
    }

    private static void updateCitiesCounties(int citiesAdded, int countriesAdded,
                                             Set<String> newCitiesSet, Set<String> newCountriesSet) {
        countCities += citiesAdded;
        countCountries += countriesAdded;
        citiesList = newCitiesSet;
        countriesList = newCountriesSet;
        citiesCountTextView.setText("Cities: " + countCities);
        countriesCountTextView.setText("Countries: " + countCountries);
    }

    private static class CountCityCountries extends Thread {
        private List<ImageData> imageDataList;
        private Set<String> currentCitySet;
        private Set<String> currentCountrySet;
        private int citiesAdded;
        private int countriesAdded;
        private Context context;

        private CountCityCountries(Context context,List<ImageData> imageDataList, Set<String> currentCitySet, Set<String> currentCountrySet) {
            this.imageDataList = imageDataList;
            this.currentCitySet = currentCitySet;
            this.currentCountrySet = currentCountrySet;
            this.citiesAdded = 0;
            this.countriesAdded = 0;
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
                    String cityName = addresses.get(0).getLocality();
                    String countryName = addresses.get(0).getCountryCode();
                    if (!currentCitySet.contains(cityName) && !TextUtils.isEmpty(cityName)) {
                        countCities++;
                        citiesList.add(cityName);
                        Log.d("hey","New city: " + cityName);

                        if (!currentCountrySet.contains(countryName) && !TextUtils.isEmpty(countryName)) {
                            countriesList.add(countryName);
                            countCountries++;
                            Log.d("hey","New country: " + countryName);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            updateCitiesCounties(citiesAdded,countriesAdded,currentCitySet,currentCountrySet);
        }
    }
}
