package tech.ducletran.travelgallery.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tech.ducletran.travelgallery.R;

import java.util.Arrays;
import java.util.List;

public class BaseAlbumAdapter extends RecyclerView.Adapter<BaseAlbumAdapter.SimpleViewHolder> {
    private static ClickListener clickListener;
    private Context context;
    private List<String> albumName = Arrays.asList("Favorite","Food","Friends/People");

    public BaseAlbumAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.album_item_view,viewGroup,false);

        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder viewHolder, int position) {
        viewHolder.getTitleTextView().setText(albumName.get(position));
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        BaseAlbumAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textView;

        public SimpleViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.textView = view.findViewById(R.id.album_item_title_text_view);
        }
        public TextView getTitleTextView() {
            return this.textView;
        }


        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(),v);
        }
    }
}
