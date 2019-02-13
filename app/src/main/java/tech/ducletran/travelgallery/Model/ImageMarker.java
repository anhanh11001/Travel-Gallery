package tech.ducletran.travelgallery.Model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class ImageMarker implements ClusterItem {

    private final LatLng imageLocation;
    private ImageData imageInfo;

    public ImageMarker(LatLng imageLocation, ImageData imageInfo) {
        this.imageLocation = imageLocation;
        this.imageInfo = imageInfo;

    }

    public ImageData getImageData() {return this.imageInfo;}


    @Override
    public LatLng getPosition() {
        return imageLocation;
    }

    @Override
    public String getTitle() {
        return imageInfo.getTitle();
    }

    @Override
    public String getSnippet() {
        return imageInfo.getDescription();
    }
}
