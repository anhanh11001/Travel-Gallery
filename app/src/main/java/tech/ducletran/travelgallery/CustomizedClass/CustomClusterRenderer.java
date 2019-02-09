package tech.ducletran.travelgallery.CustomizedClass;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import tech.ducletran.travelgallery.Activities.DisplayMultipleImageMapActivity;
import tech.ducletran.travelgallery.Activities.DisplaySingleImageMapActivity;
import tech.ducletran.travelgallery.ImageData.ImageData;
import tech.ducletran.travelgallery.ImageData.ImageMarker;
import tech.ducletran.travelgallery.R;

import java.util.ArrayList;


public class CustomClusterRenderer extends DefaultClusterRenderer<ImageMarker> {
    private Context context;
    private final IconGenerator imageMarkerGenerator;
    private IconGenerator clusterMarkerGenerator;
    private final ImageView imageView;
    private final ImageView clusterImageView;
    private final TextView clusterTextView;
    private Bitmap icon;
    private Bitmap clusterIcon;
    private ClusterManager<ImageMarker> clusterManager;


    public CustomClusterRenderer(Context context, GoogleMap map, ClusterManager<ImageMarker> clusterManager) {
        super(context, map, clusterManager);
        Log.d("Hy","New cluster manager made");
        this.context = context;
        this.clusterManager = clusterManager;

        int padding = (int) context.getResources().getDimension(R.dimen.map_marker_padding_dimension);
        int markerDimension = (int) context.getResources().getDimension(R.dimen.map_marker_dimension);


        clusterMarkerGenerator = new IconGenerator(context);
        imageMarkerGenerator = new IconGenerator(context);

        View view = LayoutInflater.from(context).inflate(R.layout.cluster_marker_item_view,null);
        view.setLayoutParams(new ViewGroup.LayoutParams(markerDimension,markerDimension));
        clusterImageView = view.findViewById(R.id.cluster_marker_image_view);
        clusterTextView = view.findViewById(R.id.cluster_marker_text_view);
        clusterMarkerGenerator.setContentView(view);

        imageView = new ImageView(context);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(markerDimension,markerDimension));
        imageView.setPadding(padding, padding, padding, padding);
        imageMarkerGenerator.setContentView(imageView);

    }

    @Override
    protected void onBeforeClusterItemRendered(ImageMarker item, MarkerOptions markerOptions) {
        Glide.with(context).load(item.getImageData().getThumbnail()).into(imageView);
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
                        if (trueMarker != null ) {
                            trueMarker.setIcon(BitmapDescriptorFactory.fromBitmap(icon));
                        }
                    }
                });
    }

    @Override
    public void setOnClusterItemClickListener(ClusterManager.OnClusterItemClickListener<ImageMarker> listener) {
        super.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<ImageMarker>() {
            @Override
            public boolean onClusterItemClick(ImageMarker imageMarker) {
                Intent intent = new Intent(context,DisplaySingleImageMapActivity.class);
                intent.putExtra("image_path",imageMarker.getImageData().getPath());
                context.startActivity(intent);
                return false;
            }
        });
    }

    @Override
    protected void onBeforeClusterRendered(final Cluster<ImageMarker> cluster, final MarkerOptions markerOptions) {
        ImageMarker firstImage = (ImageMarker) cluster.getItems().toArray()[0];
        Glide.with(context).load(firstImage.getImageData().getThumbnail()).into(clusterImageView);
        clusterTextView.setText(cluster.getSize() + " images");
        clusterIcon = clusterMarkerGenerator.makeIcon();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(clusterIcon));
    }

    @Override
    protected void onClusterRendered(final Cluster<ImageMarker> cluster, final Marker marker) {
        ImageMarker firstImage = (ImageMarker) cluster.getItems().toArray()[0];
        Glide.with(context).load(firstImage.getImageData().getThumbnail())
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        clusterImageView.setImageDrawable(resource);
                        clusterTextView.setText(cluster.getSize() + " images");
                        clusterIcon = clusterMarkerGenerator.makeIcon();
                        CustomClusterRenderer render = (CustomClusterRenderer) clusterManager.getRenderer();
                        Marker trueMarker = render.getMarker(cluster);
                        if (trueMarker != null) {
                            trueMarker.setIcon(BitmapDescriptorFactory.fromBitmap(clusterIcon));
                        }
                    }
                });
    }

    @Override
    public void setOnClusterClickListener(ClusterManager.OnClusterClickListener<ImageMarker> listener) {
        super.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<ImageMarker>() {
            @Override
            public boolean onClusterClick(Cluster<ImageMarker> cluster) {
                Intent intent = new Intent(context,DisplayMultipleImageMapActivity.class);
                ArrayList<ImageData> imageList = new ArrayList<>();
                for (ImageMarker marker: cluster.getItems()) {
                    imageList.add(marker.getImageData());
                }
                DisplayMultipleImageMapActivity.setImagesToDisplay(imageList);
                context.startActivity(intent);
                return false;
            }
        });
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster<ImageMarker> cluster) {
        return cluster.getSize() > 2;
    }
}
