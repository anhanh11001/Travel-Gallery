package tech.ducletran.travelgallery.Database;

import android.provider.BaseColumns;

public final class AllImageFeederContract {

    private AllImageFeederContract() {}

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_IMAGE_PATH + " TEXT," +
                    FeedEntry.COLUMN_IMAGE_THUMBNAIL + " TEXT," +
                    FeedEntry.COLUMN_IMAGE_TIMESTAMP + " TEXT," +
                    FeedEntry.COLUMN_IMAGE_LATITUDE + " TEXT," +
                    FeedEntry.COLUMN_IMAGE_LONGTITUDE + " TEXT," +
                    FeedEntry.COLUMN_IMAGE_SIZE + " TEXT," +
                    FeedEntry.COLUMN_IMAGE_TITLE + " TEXT," +
                    FeedEntry.COLUMN_IMAGE_DESCRIPTION + " TEXT," +
                    FeedEntry.COLUMN_IMAGE_IS_FAVORITE + " INTEGER," +
                    FeedEntry.COLUMN_IMAGE_IS_FOOD + " INTEGER," +
                    FeedEntry.COLUMN_IMAGE_IS_PEOPLE + " INTEGER)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "all_images";
        public static final String COLUMN_IMAGE_PATH = "path";
        public static final String COLUMN_IMAGE_THUMBNAIL = "thumbnail";
        public static final String COLUMN_IMAGE_TIMESTAMP = "timestamp";
        public static final String COLUMN_IMAGE_LATITUDE = "latitude";
        public static final String COLUMN_IMAGE_LONGTITUDE = "longtitude";
        public static final String COLUMN_IMAGE_SIZE = "size";
        public static final String COLUMN_IMAGE_TITLE = "title";
        public static final String COLUMN_IMAGE_DESCRIPTION = "description";
        public static final String COLUMN_IMAGE_IS_FAVORITE = "favorite";
        public static final String COLUMN_IMAGE_IS_PEOPLE = "people";
        public static final String COLUMN_IMAGE_IS_FOOD = "food";
    }
}
