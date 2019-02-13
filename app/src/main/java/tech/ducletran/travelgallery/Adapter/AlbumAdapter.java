package tech.ducletran.travelgallery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import tech.ducletran.travelgallery.Model.Album;
import tech.ducletran.travelgallery.R;

import java.util.List;

public class AlbumAdapter extends BaseAdapter {
    private Context context;
    private List<Album> albumList;

    public AlbumAdapter(Context context, List<Album> albumList) {
        this.context = context;
        this.albumList = albumList;
    }

    @Override
    public int getCount() {
        return albumList.size();
    }

    @Override
    public Album getItem(int position) {
        return albumList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return albumList.get(position).getAlbumId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.album_item_view,parent,false);
            holder.textView = convertView.findViewById(R.id.album_item_title_text_view);
            holder.imageView = convertView.findViewById(R.id.album_item_cover_image);
            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }

        Album album = getItem(position);

        holder.textView.setText(album.getAlbumName());
        Glide.with(context).load(album.getAlbumCover()).into(holder.imageView);

        return convertView;
    }

    private class ViewHolder {
        private TextView textView;
        private ImageView imageView;
    }
}