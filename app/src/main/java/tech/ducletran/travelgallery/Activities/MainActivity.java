package tech.ducletran.travelgallery.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import tech.ducletran.travelgallery.Adapter.CategoryStatePageAdapter;
import tech.ducletran.travelgallery.CustomizedClass.CustomViewPager;
import tech.ducletran.travelgallery.Fragment.PhotosFragment;
import tech.ducletran.travelgallery.ImageData.ImageData;
import tech.ducletran.travelgallery.ImageData.ImageManager;
import tech.ducletran.travelgallery.R;

public class MainActivity extends BaseActivity {
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
        viewPager.setOffscreenPageLimit(1);

        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        setUpTabLayoutIcon();

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

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_item_settings:
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        case R.id.menu_item_adding_albums:
            return true;
        case R.id.menu_item_adding_images:
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/jpeg");
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
            startActivityForResult(Intent.createChooser(intent,"Complete action using"),
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
            Uri imageUri = data.getData();
            if (imageUri != null) {
                Cursor returnCursor = getContentResolver()
                        .query(imageUri, ImageManager.projection, null, null, null);
                while (returnCursor.moveToNext() ) {
                    String path = returnCursor.getString(returnCursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
                    String timestamp = returnCursor.getString(returnCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN));
                    String thumbnail = returnCursor.getString(returnCursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA));
                    String latitude = returnCursor.getString(returnCursor.getColumnIndexOrThrow(MediaStore.Images.Media.LATITUDE));
                    String longtitude = returnCursor.getString(returnCursor.getColumnIndexOrThrow(MediaStore.Images.Media.LONGITUDE));
                    String size = returnCursor.getString(returnCursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE));
                    ImageManager.addImage(new ImageData(path,timestamp,thumbnail,latitude,longtitude,size));
                }
                PhotosFragment.setArePhotoChanged();
                returnCursor.close();
            }
        }
    }

    private void setUpTabLayoutIcon() {
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

        //initial setUp
        tabLayout.getTabAt(0).getIcon().setColorFilter(colorPrimary, PorterDuff.Mode.SRC_IN);


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
