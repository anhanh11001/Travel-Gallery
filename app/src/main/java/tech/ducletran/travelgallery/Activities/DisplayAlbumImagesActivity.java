package tech.ducletran.travelgallery.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.*;
import android.widget.*;
import tech.ducletran.travelgallery.Adapter.PhotosAdapter;
import tech.ducletran.travelgallery.ImageData.Album;
import tech.ducletran.travelgallery.ImageData.AlbumManager;
import tech.ducletran.travelgallery.R;

public class DisplayAlbumImagesActivity extends BaseActivity {
    private Album currentAlbum;
    private PhotosAdapter adapter;
    private static boolean adapterChanged = false;
    private int albumPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_photos_view);

        Intent intent = getIntent();
        albumPosition = intent.getIntExtra("album_position",0);
        currentAlbum = AlbumManager.getAlbum(albumPosition);
        adapter = new PhotosAdapter(this,
                currentAlbum.getAlbumImageList());;
        DisplayImageActivity.setImageDataList(currentAlbum.getAlbumImageList());

        findViewById(R.id.photos_loading_layout).setVisibility(View.GONE);

        GridView gridView = findViewById(R.id.photos_grid_view);
        gridView.setEmptyView(findViewById(R.id.photos_empty_view));
        gridView.setNumColumns(4);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DisplayAlbumImagesActivity.this, DisplayImageActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("album_come_from",albumPosition);
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.display_album_image_activity_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_adding_image_to_album:
                addNewImage();
                return true;
            case R.id.action_album_rename:
                renameAlbum();
                return true;
            case R.id.action_setting_album_cover:
                setAlbumCover();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().setTitle(currentAlbum.getAlbumName());
        if (adapterChanged) {
            recreate();
            adapterChanged = false;
        }

    }

    public static void setAdapterChanged() {
        adapterChanged = true;
    }

    private void renameAlbum() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Rename Album");
        alertDialog.setMessage("Please enter a valid album name");
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_rename_album_layout,null);
        final EditText albumNameEditText = view.findViewById(R.id.album_rename_edit_text);
        albumNameEditText.setSingleLine();
        albumNameEditText.setText(currentAlbum.getAlbumName());

        alertDialog.setView(view);

        final Context context = DisplayAlbumImagesActivity.this;
        alertDialog.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (currentAlbum.rename(albumNameEditText.getText().toString())) {
                    getSupportActionBar().setTitle(currentAlbum.getAlbumName());
                    dialog.cancel();
                } else {
                    Toast.makeText(context,"This is not a good name for album", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    private void addNewImage() {
        Intent intent = new Intent(this,AddImageToAlbumActivity.class);
        intent.putExtra("current_album_id",currentAlbum.getAlbumId());
        if (albumPosition == AlbumManager.ALBUM_FAVORITE_CODE ||
                albumPosition == AlbumManager.ALBUM_FOOD_CODE ||
                albumPosition == AlbumManager.ALBUM_PEOPLE_CODE) {
            intent.putExtra("special_album",albumPosition);
        }
        startActivity(intent);
    }

    private void setAlbumCover() {
        Intent intent = new Intent(this,SetAlbumCoverActivity.class);
        intent.putExtra("current_album_id",currentAlbum.getAlbumId());
        startActivity(intent);
    }
}
