package tech.ducletran.travelgallery.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.*;
import android.widget.*;
import tech.ducletran.travelgallery.Adapter.PhotosAdapter;
import tech.ducletran.travelgallery.Fragment.AlbumsFragment;
import tech.ducletran.travelgallery.Model.Album;
import tech.ducletran.travelgallery.Model.AlbumManager;
import tech.ducletran.travelgallery.Model.ImageManager;
import tech.ducletran.travelgallery.R;

public class DisplayAlbumImagesActivity extends AppCompatActivity {
    private Album currentAlbum;
    private PhotosAdapter adapter;
    private static boolean adapterChanged = false;
    private int albumId;

    private static int GET_ALBUM_COVER_REQUEST_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(MainActivity.getAppTheme());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_photos_view);

        Intent intent = getIntent();
        albumId = intent.getIntExtra("album_id",0);
        currentAlbum = AlbumManager.getAlbum(albumId);
        adapter = new PhotosAdapter(this,
                currentAlbum.getAlbumImageList());;
        DisplayImageActivity.setImageDataList(currentAlbum.getAlbumImageList());

        GridView gridView = findViewById(R.id.photos_grid_view);
        gridView.setEmptyView(findViewById(R.id.photos_empty_view));
        gridView.setNumColumns(4);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DisplayAlbumImagesActivity.this, DisplayImageActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("album_come_from_id", albumId);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.display_album_image_activity_menu,menu);

        if (albumId == Album.DEFAULT_PEOPLE_ID || albumId == Album.DEFAULT_FAVORITE_ID
                || albumId == Album.DEFAULT_FOOD_ID) {
            menu.findItem(R.id.action_deleting_album).setVisible(false);
            menu.findItem(R.id.action_album_rename).setVisible(false);
        }
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
            case R.id.action_deleting_album:
                deleteAlbum();
                return true;
            case R.id.action_removing_image_from_album:
                removeImage();
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
                    AlbumsFragment.setAlbumFragmentChanged(currentAlbum.getAlbumType());
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
        intent.putExtra("current_album_id",albumId);
        startActivity(intent);
    }

    private void removeImage() {
        Intent intent = new Intent(this,RemoveImageFromAlbumActivity.class);
        intent.putExtra("current_album_id",albumId);
        startActivity(intent);
    }

    private void deleteAlbum() {
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this);
        deleteDialog.setTitle("Deleting album");
        deleteDialog.setMessage("Are you sure to delete this album?");
        final Activity activty = this;
        deleteDialog.setPositiveButton("delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlbumManager.removeAlbum(DisplayAlbumImagesActivity.this,currentAlbum);
                AlbumsFragment.setAlbumFragmentChanged(currentAlbum.getAlbumType());
                activty.finish();
                dialog.cancel();
            }
        });

        deleteDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        deleteDialog.show();
    }

    private void setAlbumCover() {
        Intent intent = new Intent(this,ImagePickerActivity.class);
        intent.putExtra("current_album_id",currentAlbum.getAlbumId());
        startActivityForResult(intent,GET_ALBUM_COVER_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == GET_ALBUM_COVER_REQUEST_CODE && resultCode == RESULT_OK) {
            int imageCoverId = data.getIntExtra("result_image_id",-1);
            if (imageCoverId != -1) {
                String cover = ImageManager.getImageById(imageCoverId).getThumbnail();
                currentAlbum.setAlbumCover(cover);
                AlbumsFragment.setAlbumFragmentChanged(currentAlbum.getAlbumType());
                if (currentAlbum.getAlbumId() == Album.DEFAULT_FAVORITE_ID) {
                    SharedPreferences.Editor editor = getSharedPreferences(
                            AlbumsFragment.FAVORITE_PREFERENCE_KEY, Context.MODE_PRIVATE).edit();
                    editor.putString(AlbumsFragment.FAVORITE_COVER_STRING_KEY,cover);
                    editor.clear();
                    editor.commit();
                } else if (currentAlbum.getAlbumId() == Album.DEFAULT_FOOD_ID) {
                    SharedPreferences.Editor editor = getSharedPreferences(
                            AlbumsFragment.FOOD_PREFERENCE_KEY, Context.MODE_PRIVATE).edit();
                    editor.putString(AlbumsFragment.FOOD_COVER_STRING_KEY,cover);
                    editor.clear();
                    editor.commit();
                } else if (currentAlbum.getAlbumId() == Album.DEFAULT_PEOPLE_ID) {
                    SharedPreferences.Editor editor = getSharedPreferences(
                            AlbumsFragment.PEOPLE_PREFERENCE_KEY, Context.MODE_PRIVATE).edit();
                    editor.putString(AlbumsFragment.PEOPLE_COVER_STRING_KEY,cover);
                    editor.clear();
                    editor.commit();
                }
            }
        }
    }
}
