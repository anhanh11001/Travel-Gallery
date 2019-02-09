package tech.ducletran.travelgallery.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.util.TypedValue;
import android.view.*;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import tech.ducletran.travelgallery.Adapter.CategoryStatePageAdapter;
import tech.ducletran.travelgallery.CustomizedClass.CustomViewPager;
import tech.ducletran.travelgallery.Fragment.AlbumsFragment;
import tech.ducletran.travelgallery.Fragment.PhotosFragment;
import tech.ducletran.travelgallery.ImageData.Album;
import tech.ducletran.travelgallery.ImageData.ImageData;
import tech.ducletran.travelgallery.ImageData.ImageManager;
import tech.ducletran.travelgallery.R;

public class MainActivity extends BaseActivity  {
    static final int REQUEST_PERMISSION_KEY = 1;
    private static final int REQUEST_CODE_FOR_NEW_IMAGE = 10;


    private TabLayout tabLayout;
    private MenuItem addAlbum;
    private MenuItem addImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomViewPager viewPager = findViewById(R.id.main_view_pager);
        CategoryStatePageAdapter adapter = new CategoryStatePageAdapter(getSupportFragmentManager(),this);

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);

        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!(ActivityCompat.checkSelfPermission(this,PERMISSIONS[0]) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSION_KEY);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu,menu);
        addAlbum = menu.findItem(R.id.menu_item_adding_albums);
        addImage = menu.findItem(R.id.menu_item_adding_images);
        addAlbum.setVisible(false);
        setUpTabLayout();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_item_settings:
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        case R.id.menu_item_adding_albums:
            AlertDialog.Builder addAlbumAlertDialog = new AlertDialog.Builder(this);
            addAlbumAlertDialog.setTitle("Create Album");
            addAlbumAlertDialog.setMessage("What's this album about?");

            View view = LayoutInflater.from(this).inflate(R.layout.dialog_create_album_layout,null);
            addAlbumAlertDialog.setView(view);

            final EditText editText = view.findViewById(R.id.create_album_edit_text);
            final RadioGroup radioGroup = view.findViewById(R.id.create_album_radio_group);

            final Context context = MainActivity.this;
            addAlbumAlertDialog.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String albumName = editText.getText().toString();
                    if (albumName.length()==0 || albumName.length() > 20) {
                        Toast.makeText(context,"That's not a good album name",Toast.LENGTH_SHORT).show();
                    } else {
                        int checkedRadioId = radioGroup.getCheckedRadioButtonId();
                        if (checkedRadioId == R.id.create_location_album_radio_button) {
                            new Album(albumName,Album.ALBUM_TYPE_LOCATION);
                        } else if (checkedRadioId == R.id.create_others_album_radio_button) {
                            new Album(albumName,Album.ALBUM_TYPE_OTHER);
                        }
                        AlbumsFragment.setAlbumFragmentChanged();
                        dialog.cancel();
                    }
                }
            });

            addAlbumAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            addAlbumAlertDialog.show();
            return true;
        case R.id.menu_item_adding_images:
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
            startActivityForResult(Intent.createChooser(intent,"Select photos"),
                    REQUEST_CODE_FOR_NEW_IMAGE);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FOR_NEW_IMAGE) {
            if (resultCode == RESULT_CANCELED) {
                return;
            }

            ClipData clipData = data.getClipData();
            if (clipData != null) {
                for (int i = 0;i < clipData.getItemCount();i++) {
                    Cursor returnCursor = getContentResolver().query(clipData.getItemAt(i).getUri(),
                            ImageManager.projection,null,null,null);
                    returnCursor.moveToFirst();
                    String path = returnCursor.getString(returnCursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
                    String timestamp = returnCursor.getString(returnCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN));
                    String thumbnail = returnCursor.getString(returnCursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA));
                    String latitude = returnCursor.getString(returnCursor.getColumnIndexOrThrow(MediaStore.Images.Media.LATITUDE));
                    String longtitude = returnCursor.getString(returnCursor.getColumnIndexOrThrow(MediaStore.Images.Media.LONGITUDE));
                    String size = returnCursor.getString(returnCursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE));
                    ImageManager.addImage(new ImageData(this,path,timestamp,thumbnail,latitude,longtitude,size));
                    returnCursor.close();
                }
                PhotosFragment.setPhotoFragmentChanged();
            }
        }
    }

    private void setUpTabLayout() {
        //Set up layout icon
        TypedValue typedValuePrimary = new TypedValue();
        TypedValue typedValuePrimaryDark = new TypedValue();
        Resources.Theme theme = this.getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValuePrimary, true);
        @ColorInt int colorPrimary = typedValuePrimary.data;
        theme.resolveAttribute(R.attr.colorPrimaryDark,typedValuePrimaryDark,true);
        @ColorInt final int colorPrimaryDark = typedValuePrimaryDark.data;

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_photos_tab_icon);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_album_tab_icon);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_stories_tab_icon);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_map_tab_icon);
        tabLayout.setTabTextColors(getColor(R.color.colorBlackPrimary),colorPrimary);

        //initial setup
        int currentTab = tabLayout.getSelectedTabPosition();
        tabLayout.getTabAt(currentTab).getIcon().setColorFilter(colorPrimary, PorterDuff.Mode.SRC_IN);
        addAlbum.setVisible(false);
        addImage.setVisible(false);
        switch (currentTab) {
            case 0:
                addImage.setVisible(true);
                break;
            case 1:
                addAlbum.setVisible(true);
                break;
            case 2:
                break;
            case 3:
                break;
            default:
                break;
        }

        // Tab Layout Listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(colorPrimaryDark, PorterDuff.Mode.SRC_IN);

                addAlbum.setVisible(false);
                addImage.setVisible(false);

                if (tab.getPosition() == 0) {
                    addImage.setVisible(true);
                } else if (tab.getPosition() == 1) {
                    addAlbum.setVisible(true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(getColor(R.color.colorBlackPrimary), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
