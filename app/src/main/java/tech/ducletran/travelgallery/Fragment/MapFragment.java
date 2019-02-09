package tech.ducletran.travelgallery.Fragment;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import tech.ducletran.travelgallery.CustomizedClass.CustomClusterRenderer;
import tech.ducletran.travelgallery.ImageData.ImageData;
import tech.ducletran.travelgallery.ImageData.ImageManager;
import tech.ducletran.travelgallery.ImageData.ImageMarker;
import tech.ducletran.travelgallery.R;

public class MapFragment extends Fragment{

    private ClusterManager<ImageMarker> clusterManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_view,container,false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map_fragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                setClusterManager(googleMap,getActivity());
            }
        });

        return view;
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
            }
        }

    }


}
