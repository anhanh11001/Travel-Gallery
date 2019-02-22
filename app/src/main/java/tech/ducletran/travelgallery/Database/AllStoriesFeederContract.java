package tech.ducletran.travelgallery.Database;

import android.provider.BaseColumns;

public class AllStoriesFeederContract {

    private AllStoriesFeederContract() {}

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + AllStoryFeedEntry.TABLE_NAME + " (" +
                    AllStoryFeedEntry._ID + " INTEGER PRIMARY KEY," +
                    AllStoryFeedEntry.COLUMN_STORY_NUMBER_OF_PAGES + " INTEGER," +
                    AllStoryFeedEntry.COLUMN_STORY_NAME + " TEXT," +
                    AllStoryFeedEntry.COLUMN_STORY_DESCRIPTION + " TEXT," +
                    AllStoryFeedEntry.COLUMN_STORY_DETAILS + " TEXT," +
                    AllStoryFeedEntry.COLUMN_STORY_COVER + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + AllStoryFeedEntry.TABLE_NAME;

    public static class AllStoryFeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "all_stories";
        public static final String COLUMN_STORY_NAME = "name";
        public static final String COLUMN_STORY_COVER = "cover";
        public static final String COLUMN_STORY_DESCRIPTION = "description";
        public static final String COLUMN_STORY_NUMBER_OF_PAGES = "numOfPages";
        public static final String COLUMN_STORY_DETAILS = "details";
    }
}
