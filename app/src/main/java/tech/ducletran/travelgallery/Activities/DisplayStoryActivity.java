package tech.ducletran.travelgallery.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.bumptech.glide.Glide;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tech.ducletran.travelgallery.Adapter.StoryPageAdapter;
import tech.ducletran.travelgallery.Fragment.StoriesFracment;
import tech.ducletran.travelgallery.Model.ImageManager;
import tech.ducletran.travelgallery.Model.StoriesManager;
import tech.ducletran.travelgallery.Model.Story;
import tech.ducletran.travelgallery.R;

public class DisplayStoryActivity extends BaseActivity {
    private int REQUEST_CODE_UPDATE_STORY_COVER = 10;
    private int REQUEST_CODE_FOR_IMAGE = 11;

    private Story currentStory;
    private ImageButton coverImageButton;
    private String newCover;
    private static StoryPageAdapter adapter = null;
    private static ViewPager viewPager;

    private MenuItem editPageMenuItem;
    private MenuItem addPageMenuItem;
    private ImageButton imageButton;
    private String imageButtonImage;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_story);

        viewPager = findViewById(R.id.display_story_view_pager);
        int storyId = getIntent().getIntExtra("story_id",-1);
        currentStory = StoriesManager.getStoryById(storyId);
        setTitle(currentStory.getTitle());
        try {
            adapter = new StoryPageAdapter(getSupportFragmentManager(),storyId);
        } catch (JSONException e) {
            Toast.makeText(this,"JSON format error, please report.",Toast.LENGTH_SHORT).show();
        }
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) { }

            @Override
            public void onPageSelected(int position) {
                int lastPageIndex = viewPager.getAdapter().getCount() - 1;
                if (position == 0 ) {
                    editPageMenuItem.setVisible(false);
                    addPageMenuItem.setVisible(true);
                } else if (position == lastPageIndex) {
                    editPageMenuItem.setVisible(false);
                    addPageMenuItem.setVisible(false);
                } else {
                    editPageMenuItem.setVisible(true);
                    addPageMenuItem.setVisible(true);
                }
            }
            @Override
            public void onPageScrollStateChanged(int i) { }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.display_story_activity_menu,menu);
        editPageMenuItem = menu.findItem(R.id.action_edit_story_page);
        addPageMenuItem = menu.findItem(R.id.action_add_page_to_story);

        editPageMenuItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_delete_story:
                deleteStory();
                return true;
            case R.id.action_organize_story:
                organizeStory();
                return true;
            case R.id.action_add_page_to_story:
                addNewPageToStory();
                return true;
            case R.id.action_edit_story_page:
                try {
                    editCurrentPage();
                } catch (JSONException e) {
                   Toast.makeText(this,"JSON Exception, please report",Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_edit_story:
                editStory();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_UPDATE_STORY_COVER) {
                int imageId = data.getIntExtra("result_image_id",-1);
                if (imageId != -1) {
                    newCover = ImageManager.getImageById(imageId).getThumbnail();
                    Glide.with(DisplayStoryActivity.this)
                            .load(ImageManager.getImageById(imageId).getPath())
                            .into(coverImageButton);
                }
            }

            if (requestCode == REQUEST_CODE_FOR_IMAGE) {
                int imageId = data.getIntExtra("result_image_id",-1);
                if (imageId != -1) {
                    imageButtonImage = ImageManager.getImageById(imageId).getPath();
                    Glide.with(DisplayStoryActivity.this).load(imageButtonImage).into(imageButton);
                }
            }
        }
    }


    private void deleteStory() {
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this);
        deleteDialog.setTitle("Deleting story");
        deleteDialog.setMessage("Are you sure to delete this story?");

        deleteDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StoriesManager.removeStory(DisplayStoryActivity.this,currentStory);
                StoriesFracment.setStoryFracmentChanged();
                DisplayStoryActivity.this.finish();
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
    private void organizeStory() {}
    private void addNewPageToStory() {
        Intent intent = new Intent (this,AddNewPageToStoryActivity.class);
        intent.getIntExtra("storyId",-1);
        intent.getIntExtra("positionInStory",-1);
        intent.putExtra("storyId",currentStory.getStoryId());
        intent.putExtra("positionInStory",viewPager.getCurrentItem());
        startActivity(intent);
    }
    private void editStory() {
        AlertDialog.Builder editStoryDialog = new AlertDialog.Builder(this);
        editStoryDialog.setTitle("Edit this story");

        View storyView = LayoutInflater.from(this).inflate(R.layout.dialog_create_story_layout,null);
        editStoryDialog.setView(storyView);

        final String currentTitle = currentStory.getTitle();
        final String currentDescription = currentStory.getDescription();
        final String currentCover = currentStory.getCover();

        final EditText storyTitleEditText = storyView.findViewById(R.id.create_story_title_edit_text);
        final EditText storyDescriptionEditText = storyView.findViewById(R.id.create_story_description_edit_text);
        coverImageButton = storyView.findViewById(R.id.create_story_image_button);
        storyTitleEditText.setText(currentTitle);
        storyDescriptionEditText.setText(currentDescription);
        if (!TextUtils.isEmpty(currentCover)) {
            Glide.with(this).load(currentStory.getCover()).into(coverImageButton);
        }

        coverImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do some thing to change imageCover
                Intent intent = new Intent (DisplayStoryActivity.this,ImagePickerActivity.class);
                startActivityForResult(intent,REQUEST_CODE_UPDATE_STORY_COVER);
            }
        });

        editStoryDialog.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newTitle = storyTitleEditText.getText().toString();
                String newDescription = storyDescriptionEditText.getText().toString();
                boolean isEditted = false;

                if (!newDescription.equals(currentDescription)) {
                    currentStory.setNewDescription(newDescription);
                    isEditted = true;
                }

                if (!TextUtils.isEmpty(newTitle) && !newTitle.equals(currentTitle)) {
                    currentStory.setNewTitle(newTitle);
                    isEditted = true;
                }

                if (!TextUtils.isEmpty(newCover) && !newCover.equals(currentCover)) {
                    currentStory.setNewCover(newCover);
                    isEditted = true;
                }

                if (isEditted) {
                    StoriesFracment.setStoryFracmentChanged();
                }

                dialog.cancel();
                Toast.makeText(DisplayStoryActivity.this,"Story information editted",Toast.LENGTH_SHORT).show();
            }
        });

        editStoryDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        editStoryDialog.show();
    }
    private void editCurrentPage() throws JSONException {
        final int position = viewPager.getCurrentItem() - 1;
        JSONObject jsonObject = new JSONArray(currentStory.getDetails()).getJSONObject(position);
        final int pageType = jsonObject.getInt("page_type");
        final String normalText = jsonObject.getString("page_normal_text");
        final String specialText = jsonObject.getString("page_special_text");
        imageButtonImage = jsonObject.getString("page_image");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_new_page_to_story,null);
        imageButton = view.findViewById(R.id.add_new_page_to_story_image_button);
        final EditText normalEditText = view.findViewById(R.id.add_new_page_to_story_normal_text_edit_text);
        final EditText specialEditText = view.findViewById(R.id.add_new_page_to_story_special_text_edit_text);
        final LinearLayout normalTextLayout = view.findViewById(R.id.dialog_add_new_page_normal_text_layout);
        final LinearLayout specialTextLayout = view.findViewById(R.id.dialog_add_new_page_special_text_layout);
        final LinearLayout imageLayout = view.findViewById(R.id.dialog_add_new_page_image_layout);

        if (pageType == 1) {
            normalTextLayout.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(imageButtonImage)) {
                Glide.with(DisplayStoryActivity.this).load(imageButtonImage).into(imageButton);
            } else {
                imageLayout.setVisibility(View.GONE);
            }
            specialEditText.setText(specialText);
        } else if (pageType == 2) {
            if (!TextUtils.isEmpty(imageButtonImage)) {
                Glide.with(DisplayStoryActivity.this).load(imageButtonImage).into(imageButton);
            } else {
                imageLayout.setVisibility(View.GONE);
            }
            specialEditText.setText(specialText);
            normalEditText.setText(normalText);
        } else {
            specialTextLayout.setVisibility(View.GONE);
            normalEditText.setText(normalText);
            Glide.with(DisplayStoryActivity.this).load(imageButtonImage).into(imageButton);
        }
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayStoryActivity.this,ImagePickerActivity.class);
                startActivityForResult(intent,REQUEST_CODE_FOR_IMAGE);
            }
        });


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Edit your page");
        alertDialog.setView(view);

        alertDialog.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newNormalText = (normalTextLayout.getVisibility() == View.GONE) ?
                        normalText:normalEditText.getText().toString();
                String newSpecialText = (specialTextLayout.getVisibility() == View.GONE) ?
                        specialText:specialEditText.getText().toString();
                JSONObject newJSONObject = new JSONObject();
                try {
                    newJSONObject.put("page_type",pageType);
                    newJSONObject.put("page_normal_text",newNormalText);
                    newJSONObject.put("page_special_text",newSpecialText);
                    newJSONObject.put("page_image",imageButtonImage);
                    currentStory.editStoryPage(newJSONObject,position);
                    DisplayStoryActivity.setDisplayStoryChanged();
                } catch (JSONException e) {}
                dialog.cancel();
            }
        });

        alertDialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog.Builder areYouSureDialog = new AlertDialog.Builder(DisplayStoryActivity.this);
                areYouSureDialog.setMessage("Are you sure to delete this page?");
                areYouSureDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            currentStory.deleteStoryPageByPosition(position);
                            DisplayStoryActivity.setDisplayStoryChanged();
                            dialog.cancel();
                        } catch (JSONException e) {}
                    }
                });

                areYouSureDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                areYouSureDialog.show();
            }
        });

        alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    public static void setDisplayStoryChanged() {
        try {
            adapter.updateStoryDetails();
        } catch (JSONException e) {}
        adapter.notifyDataSetChanged();
        viewPager.invalidate();
    }



}
