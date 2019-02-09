package tech.ducletran.travelgallery.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.view.menu.MenuBuilder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import tech.ducletran.travelgallery.Adapter.DisplayPhotosAdapter;
import tech.ducletran.travelgallery.Fragment.PhotosFragment;
import tech.ducletran.travelgallery.ImageData.AlbumManager;
import tech.ducletran.travelgallery.ImageData.ImageData;
import tech.ducletran.travelgallery.ImageData.ImageManager;
import tech.ducletran.travelgallery.R;

import java.util.List;

public class DisplayImageActivity extends BaseActivity {

    private Menu menu;
    private ViewPager viewPager;
    private DisplayPhotosAdapter adapter;
    private static boolean dataChanged = false;
    private static List<ImageData> imageDataList;
    private int albumComeFrom;

    public static void setImageDataList(List<ImageData> imageList) {
        imageDataList = imageList;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        int position = intent.getIntExtra("position",0);
        albumComeFrom = intent.getIntExtra("album_come_from_id",-1);

        viewPager = findViewById(R.id.display_image_view_pager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {}
            @Override
            public void onPageSelected(int i) {
                setActionBarItemView();
            }
            @Override
            public void onPageScrollStateChanged(int i) {}
        });
        adapter = new DisplayPhotosAdapter(this,imageDataList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
    }

    public void toggleActionBar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        View decorView = getWindow().getDecorView();
        if (actionBar.isShowing()) {
            int visibility = View.SYSTEM_UI_FLAG_FULLSCREEN;
            actionBar.hide();
            decorView.setSystemUiVisibility(visibility);
        } else {
            int visibility = View.SYSTEM_UI_FLAG_VISIBLE;
            actionBar.show();
            decorView.setSystemUiVisibility(visibility);
        }
    }


    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.display__image_activity_menu,menu);
        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        this.menu = menu;
        setActionBarItemView();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            if (!(imageDataList == ImageManager.getImageDataList()) && dataChanged) {
                DisplayAlbumImagesActivity.setAdapterChanged();
                dataChanged = false;
            }
            return true;
        }
        int currentPosition = viewPager.getCurrentItem();
        ImageData current = imageDataList.get(currentPosition);
        switch (item.getItemId()) {
            case R.id.action_bar_button_info:
                Intent intent = new Intent(this,DisplayImageInfoActivity.class);
                intent.putExtra("image_info_date",current.getDateFormatted());
                intent.putExtra("image_info_size",current.getSize());
                intent.putExtra("image_info_longtitude",current.getLongtitude());
                intent.putExtra("image_info_latitude",current.getLatitude());
                intent.putExtra("image_info_display_image",current.getPath());
                startActivity(intent);
                break;
            case R.id.action_bar_button_delete: // this might need some changes
                viewPager.setCurrentItem((currentPosition == 0) ?
                        1 : currentPosition - 1);
                PhotosFragment.setPhotoFragmentChanged();
                DisplayAlbumImagesActivity.setAdapterChanged();
                ImageManager.removeImage(current,this);
                imageDataList.remove(currentPosition);
                adapter.notifyDataSetChanged();
                dataChanged = true;

                if (currentPosition == 0) {
                    viewPager.setCurrentItem(0);
                }
                if (imageDataList.size() == 0) {
                    this.finish();

                }

                Toast.makeText(this,"Image deleted" ,Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_bar_button_food:
                current.setFood();
                if (current.getIsFood()) {
                    item.setIcon(getDrawable(R.drawable.ic_food_filled_icon));
                    AlbumManager.getAlbum(AlbumManager.ALBUM_FOOD_CODE).addToAlbum(current);
                } else {
                    item.setIcon(getDrawable(R.drawable.ic_food_icon));
                    AlbumManager.getAlbum(AlbumManager.ALBUM_FOOD_CODE).removeFromAlbum(current);
                    if (albumComeFrom == AlbumManager.ALBUM_FOOD_CODE) {
                        dataChanged = true;
                        imageDataList.remove(currentPosition);
                        adapter.notifyDataSetChanged();
                        if (imageDataList.size() > 0) {
                            if (currentPosition == 0) {
                                item.setIcon(getDrawable(R.drawable.ic_food_filled_icon));
                            } else {
                                viewPager.setCurrentItem(currentPosition-1);
                            }
                        } else {
                            finish();
                            DisplayAlbumImagesActivity.setAdapterChanged();
                        }
                    }
                }
                break;
            case R.id.action_bar_button_favorite:
                current.setFavorite();
                if (current.getIsFavorite()) {
                    item.setIcon(getDrawable(R.drawable.ic_favorite_filled_icon));
                    AlbumManager.getAlbum(AlbumManager.ALBUM_FAVORITE_CODE).addToAlbum(current);
                } else {
                    item.setIcon(getDrawable(R.drawable.ic_favorite_icon));
                    AlbumManager.getAlbum(AlbumManager.ALBUM_FAVORITE_CODE).removeFromAlbum(current);
                    if (albumComeFrom == AlbumManager.ALBUM_FAVORITE_CODE) {
                        dataChanged = true;
                        imageDataList.remove(currentPosition);
                        adapter.notifyDataSetChanged();
                        if (imageDataList.size() > 0) {
                            if (currentPosition == 0) {
                                item.setIcon(getDrawable(R.drawable.ic_favorite_filled_icon));
                            } else {
                                viewPager.setCurrentItem(currentPosition-1);
                            }
                        } else {
                            finish();
                            DisplayAlbumImagesActivity.setAdapterChanged();
                        }
                    }
                }
                break;
            case R.id.action_bar_button_people:
                current.setPeople();
                if (current.getIsPeople()) {
                    item.setIcon(getDrawable(R.drawable.ic_people_filled_icon));
                    AlbumManager.getAlbum(AlbumManager.ALBUM_PEOPLE_CODE).addToAlbum(current);
                } else {
                    item.setIcon(getDrawable(R.drawable.ic_people_icon));
                    AlbumManager.getAlbum(AlbumManager.ALBUM_PEOPLE_CODE).removeFromAlbum(current);
                    if (albumComeFrom == AlbumManager.ALBUM_PEOPLE_CODE) {
                        dataChanged = true;
                        imageDataList.remove(currentPosition);
                        adapter.notifyDataSetChanged();
                        if (imageDataList.size() > 0) {
                            if (currentPosition == 0) {
                                item.setIcon(getDrawable(R.drawable.ic_people_filled_icon));
                            } else {
                                viewPager.setCurrentItem(currentPosition-1);
                            }
                        } else {
                            finish();
                            DisplayAlbumImagesActivity.setAdapterChanged();
                        }
                    }
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void setActionBarItemView() {
        if (menu != null) {
            int currentPosition = viewPager.getCurrentItem();
            ImageData current = imageDataList.get(currentPosition);
            menu.findItem(R.id.action_bar_button_favorite).setIcon(getDrawable(
                    (current.getIsFavorite()) ? R.drawable.ic_favorite_filled_icon:R.drawable.ic_favorite_icon
            ));

            menu.findItem(R.id.action_bar_button_food).setIcon(getDrawable(
                    (current.getIsFood()) ? R.drawable.ic_food_filled_icon:R.drawable.ic_food_icon
            ));
            menu.findItem(R.id.action_bar_button_people).setIcon(getDrawable(
                    (current.getIsPeople()) ? R.drawable.ic_people_filled_icon:R.drawable.ic_people_icon
            ));
        }
    }
}
