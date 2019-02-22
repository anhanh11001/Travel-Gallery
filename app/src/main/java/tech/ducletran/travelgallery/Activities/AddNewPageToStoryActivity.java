package tech.ducletran.travelgallery.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.bumptech.glide.Glide;
import org.json.JSONException;
import org.json.JSONObject;
import tech.ducletran.travelgallery.Model.ImageManager;
import tech.ducletran.travelgallery.Model.StoriesManager;
import tech.ducletran.travelgallery.R;

public class AddNewPageToStoryActivity extends BaseActivity  {
    private static int REQUEST_CODE_FOR_IMAGE = 123;

    private String imagePicked = "";
    private ImageButton imageButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_page_to_story);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent  = getIntent();
        final int storyId = intent.getIntExtra("storyId",-1);
        final int positionInStory = intent.getIntExtra("positionInStory",-1);

        GridView gridView = findViewById(R.id.add_new_page_to_story_grid_view);
        gridView.setAdapter(new AddPageAdapter(this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(AddNewPageToStoryActivity.this);
                dialog.setTitle("Enter the details of story page");

                view = getLayoutInflater().inflate(R.layout.dialog_add_new_page_to_story,parent,false);
                dialog.setView(view);

                imageButton = view.findViewById(R.id.add_new_page_to_story_image_button);
                final EditText normalEditText = view.findViewById(R.id.add_new_page_to_story_normal_text_edit_text);
                final EditText specialEditText = view.findViewById(R.id.add_new_page_to_story_special_text_edit_text);
                LinearLayout normalTextLayout = view.findViewById(R.id.dialog_add_new_page_normal_text_layout);
                LinearLayout specialTextLayout = view.findViewById(R.id.dialog_add_new_page_special_text_layout);
                LinearLayout imagelayout = view.findViewById(R.id.dialog_add_new_page_image_layout);

                switch (position) {
                    case 0:
                        normalTextLayout.setVisibility(View.GONE);
                        imagelayout.setVisibility(View.GONE);
                        break;
                    case 1:
                        specialTextLayout.setVisibility(View.GONE);
                        break;
                    case 2:
                        normalTextLayout.setVisibility(View.GONE);
                        break;
                    case 4:
                        imagelayout.setVisibility(View.GONE);
                        break;
                }

                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AddNewPageToStoryActivity.this,ImagePickerActivity.class);
                        startActivityForResult(intent,REQUEST_CODE_FOR_IMAGE);
                    }
                });

                dialog.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();


                        JSONObject jsonObject = new JSONObject();
                        String specialText = specialEditText.getText().toString();
                        String normalText = normalEditText.getText().toString();
                        int pageType = -1;
                        boolean addPageSuccess = true;
                        switch (position) {
                            case 0:
                                if (TextUtils.isEmpty(specialText)) {
                                    Toast.makeText(AddNewPageToStoryActivity.this, "You should write something.",
                                            Toast.LENGTH_SHORT).show();
                                    addPageSuccess = false;
                                }
                                pageType = 1;
                                break;
                            case 1:
                                if (TextUtils.isEmpty(imagePicked) || TextUtils.isEmpty(normalText)) {
                                    Toast.makeText(AddNewPageToStoryActivity.this, "You should write something and pick an image.",
                                            Toast.LENGTH_SHORT).show();
                                    addPageSuccess = false;
                                }
                                pageType = 3;
                                break;
                            case 2:
                                if (TextUtils.isEmpty(specialText) || TextUtils.isEmpty(imagePicked)) {
                                    Toast.makeText(AddNewPageToStoryActivity.this, "You should write something and pick an image.",
                                            Toast.LENGTH_SHORT).show();
                                    addPageSuccess = false;
                                }
                                pageType = 1;
                                break;
                            case 3:
                                if (TextUtils.isEmpty(imagePicked) || TextUtils.isEmpty(normalText) || TextUtils.isEmpty(specialText)) {
                                    Toast.makeText(AddNewPageToStoryActivity.this, "You should write something and pick an image.",
                                            Toast.LENGTH_SHORT).show();
                                    addPageSuccess = false;
                                }
                                pageType = 2;
                                break;
                            case 4:
                                if (TextUtils.isEmpty(specialText) || TextUtils.isEmpty(normalText)) {
                                    Toast.makeText(AddNewPageToStoryActivity.this, "You should write something.",
                                            Toast.LENGTH_SHORT).show();
                                    addPageSuccess = false;
                                }
                                pageType = 2;
                                break;
                        }
                        if (addPageSuccess) {
                            try {
                                jsonObject.put("page_type",pageType);
                                jsonObject.put("page_normal_text",normalText);
                                jsonObject.put("page_image",imagePicked);
                                jsonObject.put("page_special_text",specialText);
                                StoriesManager.getStoryById(storyId).addNewStoryPage(jsonObject, positionInStory);
                                AddNewPageToStoryActivity.this.finish();
                                DisplayStoryActivity.setDisplayStoryChanged();
                            } catch (JSONException e) {
                                Toast.makeText(AddNewPageToStoryActivity.this,"JSONException, please report.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                dialog.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_FOR_IMAGE) {
            // Do some thing here
            int imageId = data.getIntExtra("result_image_id",-1);
            if (imageId != -1) {
                imagePicked = ImageManager.getImageById(imageId).getPath();
                Glide.with(AddNewPageToStoryActivity.this).load(imagePicked).into(imageButton);
            }
        }
    }

    private class AddPageAdapter extends BaseAdapter {
        private Integer[] imageSrc = {R.drawable.story_page_1, R.drawable.story_page_2, R.drawable.story_page_3,
                                    R.drawable.story_page_4, R.drawable.story_page_5};
        private Context context;
        private AddPageAdapter(Context context) {this.context = context;}

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.item_add_new_page_to_story,parent,false);

            ImageView imageView = convertView.findViewById(R.id.add_new_page_to_story_image_view);
            imageView.setImageDrawable(context.getDrawable(imageSrc[position]));
            return convertView;
        }
    }
}
