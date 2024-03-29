package tech.ducletran.travelgallery.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.*;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.*;
import android.widget.*;
import com.bumptech.glide.Glide;
import tech.ducletran.travelgallery.Adapter.CategoryStatePageAdapter;
import tech.ducletran.travelgallery.CustomizedClass.CustomViewPager;
import tech.ducletran.travelgallery.Fragment.AlbumsFragment;
import tech.ducletran.travelgallery.Fragment.MapFragment;
import tech.ducletran.travelgallery.Fragment.PhotosFragment;
import tech.ducletran.travelgallery.Fragment.StoriesFracment;
import tech.ducletran.travelgallery.Model.*;
import tech.ducletran.travelgallery.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_PERMISSION_KEY = 1;
    private static final int REQUEST_CODE_FOR_NEW_IMAGE = 10;
    private static final int REQUEST_CODE_FOR_STORY_COVER = 11;


    private TabLayout tabLayout;
    private MenuItem addAlbum;
    private MenuItem addImage;
    private MenuItem addStory;

    // For story dialog creator
    private String imageCover = null;
    private ImageButton coverImageButton = null;

    private static int appTheme = -1;
    private static boolean isDarkMode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setUpTheme();
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
        addStory = menu.findItem(R.id.menu_item_adding_story);
        addStory.setVisible(false);
        addAlbum.setVisible(false);
        setUpTabLayout();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Dialog dialog;
        switch (item.getItemId()) {
        case R.id.menu_item_settings:
            Intent intentSettings = new Intent(this, SettingsActivity.class);
            startActivity(intentSettings);
            this.overridePendingTransition(R.anim.fade_in_slow, R.anim.fade_out_slow);
            return true;
        case R.id.menu_item_adding_albums:
            AlertDialog.Builder addAlbumAlertDialog = new AlertDialog.Builder(this);
            addAlbumAlertDialog.setTitle("Create Album");
            addAlbumAlertDialog.setMessage("What's this album about?");

            View albumView = LayoutInflater.from(this).inflate(R.layout.dialog_create_album_layout,null);
            addAlbumAlertDialog.setView(albumView);

            final EditText editText = albumView.findViewById(R.id.create_album_edit_text);
            final RadioGroup radioGroup = albumView.findViewById(R.id.create_album_radio_group);
            if (isDarkMode) {
                ((RadioButton) radioGroup.findViewById(R.id.create_location_album_radio_button))
                        .setTextColor(getColor(R.color.colorWhitePrimary));
                ((RadioButton) radioGroup.findViewById(R.id.create_others_album_radio_button))
                        .setTextColor(getColor(R.color.colorWhitePrimary));
            }

            addAlbumAlertDialog.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String albumName = editText.getText().toString();
                    if (albumName.length()==0 || albumName.length() > 20) {
                        Toast.makeText(MainActivity.this,"That's not a good album name",Toast.LENGTH_SHORT).show();
                    } else {
                        int checkedRadioId = radioGroup.getCheckedRadioButtonId();
                        if (checkedRadioId == R.id.create_location_album_radio_button) {
                            AlbumManager.registerAlbum(new Album(MainActivity.this,albumName,Album.ALBUM_TYPE_LOCATION));
                            AlbumsFragment.setAlbumFragmentChanged(Album.ALBUM_TYPE_LOCATION);
                        } else if (checkedRadioId == R.id.create_others_album_radio_button) {
                            AlbumManager.registerAlbum(new Album(MainActivity.this,albumName,Album.ALBUM_TYPE_OTHER));
                            AlbumsFragment.setAlbumFragmentChanged(Album.ALBUM_TYPE_OTHER);
                        }
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

            dialog = addAlbumAlertDialog.create();
            if (isDarkMode) {
                dialog.getWindow().setBackgroundDrawableResource(R.color.backgroundColorDarkMode);
            }
            dialog.show();
            return true;
        case R.id.menu_item_adding_story:
            AlertDialog.Builder addStoryAlertDialog = new AlertDialog.Builder(this);
            addStoryAlertDialog.setTitle("Create Album");
            addStoryAlertDialog.setMessage("What's this album about?");

            View storyView = LayoutInflater.from(this).inflate(R.layout.dialog_create_story_layout,null);
            addStoryAlertDialog.setView(storyView);

            final EditText storyTitleEditText = storyView.findViewById(R.id.create_story_title_edit_text);
            final EditText storyDescriptionEditText = storyView.findViewById(R.id.create_story_description_edit_text);
            coverImageButton = storyView.findViewById(R.id.create_story_image_button);


            coverImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Do some thing to change imageCover
                    Intent intent = new Intent (MainActivity.this,ImagePickerActivity.class);
                    startActivityForResult(intent,REQUEST_CODE_FOR_STORY_COVER);
                }
            });

            addStoryAlertDialog.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String storyTitle = storyTitleEditText.getText().toString();
                    if (storyTitle.length() == 0 || storyTitle.length() > 30) {
                        Toast.makeText(MainActivity.this,"The story title length is not good",Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    } else {
                        new Story(MainActivity.this,storyTitle,storyDescriptionEditText.getText().toString(),imageCover);
                        StoriesFracment.setStoryFracmentChanged();
                        dialog.cancel();
                    }
                }
            });

            addStoryAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
             dialog = addStoryAlertDialog.create();
            if (isDarkMode) {
                dialog.getWindow().setBackgroundDrawableResource(R.color.backgroundColorDarkMode);
            }
            dialog.show();
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
        if (resultCode != RESULT_CANCELED  && requestCode == REQUEST_CODE_FOR_NEW_IMAGE) {
            ClipData clipData = data.getClipData();
            Toast.makeText(this,"Loading new photos",Toast.LENGTH_SHORT).show();
            new NewPhotoLoader(this).execute(clipData);

        }
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_FOR_STORY_COVER) {
            // Do some thing here
            int imageId = data.getIntExtra("result_image_id",-1);
            if (imageId != -1) {
                imageCover = ImageManager.getImageById(imageId).getPath();
                Glide.with(MainActivity.this).load(imageCover).into(coverImageButton);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTheme(appTheme);
    }

    private void setUpTabLayout() {
        //Set up layout icon
        TypedValue typedValuePrimary = new TypedValue();
        TypedValue typedValuePrimaryDark = new TypedValue();
        TypedValue typedValueAccent = new TypedValue();
        Resources.Theme theme = this.getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValuePrimary, true);
        @ColorInt int colorPrimary = typedValuePrimary.data;
        theme.resolveAttribute(R.attr.colorPrimaryDark,typedValuePrimaryDark,true);
        @ColorInt final int colorPrimaryDark = typedValuePrimaryDark.data;
        theme.resolveAttribute(R.attr.colorAccent,typedValueAccent,true);
        @ColorInt final int colorAccent = typedValueAccent.data;


        tabLayout.getTabAt(0).setIcon(R.drawable.ic_photos_tab_icon);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_album_tab_icon);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_stories_tab_icon);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_map_tab_icon);
        if (isDarkMode) {
            tabLayout.setBackgroundColor(getColor(R.color.colorBlackPrimary));
            tabLayout.setTabTextColors(getColor(R.color.colorWhitePrimary), colorAccent);
            tabLayout.getTabAt(0).getIcon().setColorFilter(getColor(R.color.colorWhitePrimary),PorterDuff.Mode.SRC_IN);
            tabLayout.getTabAt(1).getIcon().setColorFilter(getColor(R.color.colorWhitePrimary),PorterDuff.Mode.SRC_IN);
            tabLayout.getTabAt(2).getIcon().setColorFilter(getColor(R.color.colorWhitePrimary),PorterDuff.Mode.SRC_IN);
            tabLayout.getTabAt(3).getIcon().setColorFilter(getColor(R.color.colorWhitePrimary),PorterDuff.Mode.SRC_IN);
        } else {
            tabLayout.setTabTextColors(getColor(R.color.colorBlackPrimary),colorPrimary);
        }


        //initial setup
        int currentTab = tabLayout.getSelectedTabPosition();
        tabLayout.getTabAt(currentTab).getIcon().setColorFilter((isDarkMode) ?
                colorAccent : colorPrimary, PorterDuff.Mode.SRC_IN);
        addAlbum.setVisible(false);
        addImage.setVisible(false);
        addStory.setVisible(false);
        switch (currentTab) {
            case 0:
                addImage.setVisible(true);
                break;
            case 1:
                addAlbum.setVisible(true);
                break;
            case 2:
                addStory.setVisible(true);
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
                tab.getIcon().setColorFilter((isDarkMode) ? colorAccent : colorPrimaryDark, PorterDuff.Mode.SRC_IN);

                addAlbum.setVisible(false);
                addImage.setVisible(false);
                addStory.setVisible(false);

                if (tab.getPosition() == 0) {
                    addImage.setVisible(true);
                } else if (tab.getPosition() == 1) {
                    addAlbum.setVisible(true);
                } else if (tab.getPosition() == 2) {
                    addStory.setVisible(true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(isDarkMode) {
                    tab.getIcon().setColorFilter(getColor(R.color.colorWhitePrimary), PorterDuff.Mode.SRC_IN);
                } else {
                    tab.getIcon().setColorFilter(getColor(R.color.colorBlackPrimary), PorterDuff.Mode.SRC_IN);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private static class NewPhotoLoader extends AsyncTask<ClipData,Void, ArrayList<ImageData>> {
        private Context context;

        private NewPhotoLoader(Context context) {
            this.context = context;
        }

        @Override
        protected ArrayList<ImageData> doInBackground(ClipData... data) {
            ClipData clipData = data[0];
            ArrayList<ImageData> newData = new ArrayList<>();
            if (clipData != null) {

                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Cursor returnCursor = context.getContentResolver().query(clipData.getItemAt(i).getUri(),
                            ImageManager.projection, null, null, null);
                    returnCursor.moveToNext();
                    String path = returnCursor.getString(returnCursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
                    String timestamp = returnCursor.getString(returnCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN));
                    String thumbnail = returnCursor.getString(returnCursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA));
                    String latitude = returnCursor.getString(returnCursor.getColumnIndexOrThrow(MediaStore.Images.Media.LATITUDE));
                    String longtitude = returnCursor.getString(returnCursor.getColumnIndexOrThrow(MediaStore.Images.Media.LONGITUDE));
                    String size = returnCursor.getString(returnCursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE));
                    newData.add(new ImageData(context,path,timestamp,thumbnail,latitude,longtitude,size));

                    returnCursor.close();
                }

            }
            return newData;
        }

        @Override
        protected void onPostExecute(ArrayList<ImageData> newData) {
            super.onPostExecute(newData);
            for (ImageData data : newData) {
                ImageManager.addImage(data);
                if (!TextUtils.isEmpty(data.getLatitude()) && !TextUtils.isEmpty(data.getLongtitude())) {
                    MapFragment.addNewImageMarker(data.getImageMarker());
                }
            }
            PhotosFragment.setPhotoFragmentChanged(context,0);
            Toast.makeText(context,"Loaded " + newData.size() + " images",Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpTheme() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String colorThemeCode = sharedPreferences.getString(getString(R.string.action_settings_color_theme_key),"1");
        isDarkMode = sharedPreferences.getBoolean(getString(R.string.action_settings_dark_mode_key),false);

        setUpAppTheme(Integer.parseInt(colorThemeCode), isDarkMode);
        setTheme(appTheme);
    }

    public static void setUpAppTheme(int code, boolean isDarkMode) {
        switch (code) {
            case 1: // Blue
                appTheme = (isDarkMode) ? R.style.DarkCrystalBlueAppTheme: R.style.CrystalBlueAppTheme;
                break;
            case 2: // Orange
                appTheme = (isDarkMode) ? R.style.DarkSunshineOrangeAppTheme : R.style.SunshineOrangeAppTheme;
                break;
            case 3: // Green
                appTheme = (isDarkMode) ? R.style.DarkCoralGreenAppTheme : R.style.CoralGreenAppTheme;
                break;
            case 4: // Gray
                appTheme = (isDarkMode) ? R.style.DarkSilverGrayAppTheme : R.style.SilverGrayAppTheme;
                break;
            case 5: // Red
                appTheme = (isDarkMode) ? R.style.DarkBloodyRedAppTheme : R.style.BloodyRedAppTheme;
                break;
            default:
                appTheme = (isDarkMode) ? R.style.DarkCrystalBlueAppTheme : R.style.CrystalBlueAppTheme;
                break;
        }
    }

    public static int getAppTheme() {return appTheme;}
    public static boolean isDarkMode() {return isDarkMode;}
}
