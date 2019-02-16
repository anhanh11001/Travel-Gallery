package tech.ducletran.travelgallery.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tech.ducletran.travelgallery.Database.AllStoriesFeederContract;
import tech.ducletran.travelgallery.Database.AllStoriesReaderDbHelper;

public class Story {

    private String title;
    private String description;
    private String cover;
    private int id;
    private SQLiteDatabase database;
    private int numOfPages;
    private String details;

    public Story (Context context, String title, String description, String cover) {
        this.title = title;
        this.description = description;
        this.cover = cover;
        this.id = StoriesManager.generateId(context,this);
        this.numOfPages = 0;
        this.details = "";
        StoriesManager.registerStory(this,id);

        database = new AllStoriesReaderDbHelper(context).getWritableDatabase();
    }

    public Story (Context context,int id, String title, String description, String cover, int numOfPages, String details) {
        this.title = title;
        this.id = id;
        this.description = description;
        this.cover = cover;
        this.numOfPages = numOfPages;
        this.details = details;
        StoriesManager.registerStory(this,id);
        database = new AllStoriesReaderDbHelper(context).getWritableDatabase();
    }

    // Setters
    public void setNewTitle(String newTitle) {
        this.title = newTitle;

        ContentValues value = new ContentValues();
        value.put(AllStoriesFeederContract.AllStoryFeedEntry.COLUMN_STORY_NAME,newTitle);

        String selection = AllStoriesFeederContract.AllStoryFeedEntry._ID + " LIKE ?";
        String[] selectionArgs = {Integer.toString(id)};

        database.update(AllStoriesFeederContract.AllStoryFeedEntry.TABLE_NAME,
                value,selection,selectionArgs);
    }

    public void setNewDescription(String newDescription) {
        this.description = newDescription;

        ContentValues value = new ContentValues();
        value.put(AllStoriesFeederContract.AllStoryFeedEntry.COLUMN_STORY_DESCRIPTION,newDescription);

        String selection = AllStoriesFeederContract.AllStoryFeedEntry._ID + " LIKE ?";
        String[] selectionArgs = {Integer.toString(id)};

        database.update(AllStoriesFeederContract.AllStoryFeedEntry.TABLE_NAME,
                value,selection,selectionArgs);
    }

    public void setNewCover(String newCover) {
        this.cover = newCover;
        ContentValues value = new ContentValues();
        value.put(AllStoriesFeederContract.AllStoryFeedEntry.COLUMN_STORY_COVER,newCover);

        String selection = AllStoriesFeederContract.AllStoryFeedEntry._ID + " LIKE ?";
        String[] selectionArgs = {Integer.toString(id)};

        database.update(AllStoriesFeederContract.AllStoryFeedEntry.TABLE_NAME,
                value,selection,selectionArgs);
    }

    public void editStoryPage(JSONObject jsonObject, int position) throws JSONException {
        JSONArray currentJSONArray = new JSONArray(details);
        currentJSONArray.put(position,jsonObject);
        details = currentJSONArray.toString();
        ContentValues value = new ContentValues();
        value.put(AllStoriesFeederContract.AllStoryFeedEntry.COLUMN_STORY_DETAILS,details);
        String selection = AllStoriesFeederContract.AllStoryFeedEntry._ID + " LIKE ?";
        String[] selectionArgs = {Integer.toString(id)};
        database.update(AllStoriesFeederContract.AllStoryFeedEntry.TABLE_NAME,
                value,selection,selectionArgs);
    }

    public void addNewStoryPage(JSONObject jsonObject, int position) throws JSONException {
        JSONArray currentJSONArray;
        if (TextUtils.isEmpty(details)) {
            currentJSONArray = new JSONArray();
        } else {
            currentJSONArray = new JSONArray(details);

        }

        if (position == currentJSONArray.length()) {
            currentJSONArray.put(jsonObject);
            details = currentJSONArray.toString();
        } else {
            JSONArray newJSONArray = new JSONArray();
            for (int i = 0; i < currentJSONArray.length();i++) {
                if (i == position) {
                    newJSONArray.put(jsonObject);
                }
                newJSONArray.put(currentJSONArray.getJSONObject(i));
            }
            details = newJSONArray.toString();
        }


        numOfPages++;

        ContentValues value = new ContentValues();
        value.put(AllStoriesFeederContract.AllStoryFeedEntry.COLUMN_STORY_DETAILS,details);
        value.put(AllStoriesFeederContract.AllStoryFeedEntry.COLUMN_STORY_NUMBER_OF_PAGES, numOfPages);

        String selection = AllStoriesFeederContract.AllStoryFeedEntry._ID + " LIKE ?";
        String[] selectionArgs = {Integer.toString(id)};

        database.update(AllStoriesFeederContract.AllStoryFeedEntry.TABLE_NAME,
                value,selection,selectionArgs);
    }

    public void deleteStoryPageByPosition(int position) throws JSONException {
        JSONArray currentJSONArray = new JSONArray(details);


        currentJSONArray.remove(position);
        details = currentJSONArray.toString();
        numOfPages -= 1;

        ContentValues value = new ContentValues();
        value.put(AllStoriesFeederContract.AllStoryFeedEntry.COLUMN_STORY_DETAILS,details);
        value.put(AllStoriesFeederContract.AllStoryFeedEntry.COLUMN_STORY_NUMBER_OF_PAGES, numOfPages);

        String selection = AllStoriesFeederContract.AllStoryFeedEntry._ID + " LIKE ?";
        String[] selectionArgs = {Integer.toString(id)};

        database.update(AllStoriesFeederContract.AllStoryFeedEntry.TABLE_NAME,
                value,selection,selectionArgs);
    }


    // Getters
    public String getDetails() {return details;}
    public int getNumOfPages() {return numOfPages;}
    public String getTitle() {return title;}
    public String getCover() {return cover;}
    public String getDescription() {return description;}
    public int getStoryId() {return id;}

}
