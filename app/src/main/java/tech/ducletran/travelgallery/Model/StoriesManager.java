package tech.ducletran.travelgallery.Model;

import android.content.ContentValues;
import android.content.Context;
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
        StoriesFracment.setStoryFracmentChanged();
    }

    public static List<Story> getStoryList() {return storyList;}

    public static int generateId(Context context, Story story) {
        ContentValues values = new ContentValues();
        values.put(AllStoriesFeederContract.AllStoryFeedEntry.COLUMN_STORY_NAME, story.getTitle());
        values.put(AllStoriesFeederContract.AllStoryFeedEntry.COLUMN_STORY_DESCRIPTION, story.getDescription());
        values.put(AllStoriesFeederContract.AllStoryFeedEntry.COLUMN_STORY_COVER, story.getCover());
        return (int) new AllStoriesReaderDbHelper(context).getWritableDatabase()
                .insert(AllStoriesFeederContract.AllStoryFeedEntry.TABLE_NAME,null,values);
    }
}
