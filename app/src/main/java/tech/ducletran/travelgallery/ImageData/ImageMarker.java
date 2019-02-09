package tech.ducletran.travelgallery.ImageData;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class ImageMarker implements ClusterItem {

    private final LatLng imageLocation;
    private String imageTitle;
    private String imageSnippet;
    private ImageData imageInfo;

    public ImageMarker(LatLng imageLocation, ImageData imageInfo) {
        this.imageLocation = imageLocation;
        this.imageInfo = imageInfo;
    }

    public ImageMarker(LatLng imageLocation, String imageTitle, String imageSnippet,ImageData imageInfo) {
        this.imageLocation = imageLocation;
        this.imageTitle = imageTitle;
        this.imageSnippet = imageSnippet;
        this.imageInfo = imageInfo;
    }

    public ImageData getImageData() {return this.imageInfo;}


    @Override
    public LatLng getPosition() {
        return imageLocation;
    }

    @Override
    public String getTitle() {
        return imageTitle;
    }

    @Override
    public String getSnippet() {
        return imageSnippet;
    }
}
