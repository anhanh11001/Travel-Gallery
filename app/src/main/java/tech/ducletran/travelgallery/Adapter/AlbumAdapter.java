package tech.ducletran.travelgallery.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import tech.ducletran.travelgallery.ImageData.Album;
import tech.ducletran.travelgallery.R;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.SimpleViewHolder> {
    private ClickListener clickListener;
    private Context context;
    private List<Album> albumList;

    public AlbumAdapter(Context context, List<Album> albumList) {
        this.context = context;
        this.albumList = albumList;
    }

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.album_item_view,viewGroup,false);

        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder viewHolder, int position) {
        Album currentAlbum = albumList.get(position);
        viewHolder.getTitleTextView().setText(currentAlbum.getAlbumName());
        String albumCover = currentAlbum.getAlbumCover();
        if (albumCover != null) {
            Glide.with(context).load(albumCover).into(viewHolder.getImageView());
        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textView;
        private ImageView imageView;

        private SimpleViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.textView = view.findViewById(R.id.album_item_title_text_view);
            this.imageView = view.findViewById(R.id.album_item_cover_image);
        }
        private TextView getTitleTextView() {
            return this.textView;
        }

        private ImageView getImageView() {
            return this.imageView;
        }


        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(),v);
        }
    }
}
