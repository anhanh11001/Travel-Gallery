package tech.ducletran.travelgallery.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import tech.ducletran.travelgallery.Database.AllStoriesFeederContract;
import tech.ducletran.travelgallery.Database.AllStoriesReaderDbHelper;

public class Story {

    private String title;
    private String description;
    private String cover;
    private int id;
    private SQLiteDatabase database;

    public Story (Context context, String title, String description, String cover) {
        this.title = title;
        this.description = description;
        this.cover = cover;
        this.id = StoriesManager.generateId(context,this);
        StoriesManager.registerStory(this,id);

        database = new AllStoriesReaderDbHelper(context).getWritableDatabase();
    }

    public Story (Context context,int id, String title, String description, String cover) {
        this.title = title;
        this.id = id;
        this.description = description;
        this.cover = cover;
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


    // Getters
    public String getTitle() {return title;}
    public String getCover() {return cover;}
    public String getDescription() {return description;}
    public int getStoryId() {return id;}
}
