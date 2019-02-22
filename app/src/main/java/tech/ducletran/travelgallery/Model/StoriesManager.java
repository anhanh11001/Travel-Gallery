package tech.ducletran.travelgallery.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import tech.ducletran.travelgallery.Database.AllStoriesFeederContract;
import tech.ducletran.travelgallery.Database.AllStoriesReaderDbHelper;
import tech.ducletran.travelgallery.Fragment.StoriesFracment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoriesManager {

    private static Map<Integer,Story> storyHashMap = new HashMap<>();
    private static List<Story> storyList = new ArrayList<>();

    public static void registerStory(Story story, int id) {
        storyList.add(story);
        storyHashMap.put(id,story);
    }

    public static List<Story> getStoryList() {return storyList;}
    public static Story getStoryById(int id) {return storyHashMap.get(id);}

    public static int generateId(Context context, Story story) {
        ContentValues values = new ContentValues();
        values.put(AllStoriesFeederContract.AllStoryFeedEntry.COLUMN_STORY_NAME, story.getTitle());
        values.put(AllStoriesFeederContract.AllStoryFeedEntry.COLUMN_STORY_DESCRIPTION, story.getDescription());
        values.put(AllStoriesFeederContract.AllStoryFeedEntry.COLUMN_STORY_COVER, story.getCover());
        return (int) AllStoriesReaderDbHelper.getInstance(context).getWritableDatabase()
                .insert(AllStoriesFeederContract.AllStoryFeedEntry.TABLE_NAME,null,values);
    }

    public static void removeStory(Context context, Story story) {
        storyList.remove(story);
        storyHashMap.remove(story.getStoryId());

        String selection = AllStoriesFeederContract.AllStoryFeedEntry._ID + " LIKE ?";
        String[] selectionArgs = {Integer.toString(story.getStoryId())};
        AllStoriesReaderDbHelper.getInstance(context)
                .getWritableDatabase()
                .delete(AllStoriesFeederContract.AllStoryFeedEntry.TABLE_NAME,selection,selectionArgs);

    }
}
