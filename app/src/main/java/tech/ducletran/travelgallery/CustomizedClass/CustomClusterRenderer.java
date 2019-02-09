package tech.ducletran.travelgallery.CustomizedClass;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.ClusterRenderer;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import tech.ducletran.travelgallery.ImageData.ImageMarker;
import tech.ducletran.travelgallery.R;


public class CustomClusterRenderer extends DefaultClusterRenderer<ImageMarker> {
    private Context context;
    private final IconGenerator imageMarkerGenerator;
    private IconGenerator clusterMarkerGenerator;
    private final ImageView imageView;
    private final int markerDimension;
    private Bitmap icon;
    private ClusterManager<ImageMarker> clusterManager;

    public CustomClusterRenderer(Context context, GoogleMap map, ClusterManager<ImageMarker> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
        this.clusterManager = clusterManager;

        imageMarkerGenerator = new IconGenerator(context);
        imageView = new ImageView(context);
        markerDimension = (int) context.getResources().getDimension(R.dimen.map_marker_dimension);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(markerDimension,markerDimension));
        int padding = (int) context.getResources().getDimension(R.dimen.map_marker_padding_dimension);
        imageView.setPadding(padding, padding, padding, padding);
        imageMarkerGenerator.setContentView(imageView);

    }

    @Override
    protected void onBeforeClusterItemRendered(ImageMarker item, MarkerOptions markerOptions) {
        icon = imageMarkerGenerator.makeIcon();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).snippet(item.getSnippet());
    }

    @Override
    protected void onClusterItemRendered(final ImageMarker clusterItem, final Marker marker) {

        Glide.with(context).load(clusterItem.getImageData().getThumbnail())
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        imageView.setImageDrawable(resource);
                        icon = imageMarkerGenerator.makeIcon();
                        CustomClusterRenderer render = (CustomClusterRenderer) clusterManager.getRenderer();
                        Marker trueMarker = render.getMarker(clusterItem);
                        if (trueMarker != null) {
                            trueMarker.setIcon(BitmapDescriptorFactory.fromBitmap(icon));
                        }
                    }
                });
    }
}
