package tech.ducletran.travelgallery.Database;

import android.provider.BaseColumns;

public class AllAlbumFeederContract {

    private AllAlbumFeederContract() {}

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + AllAlbumFeederContract.AllAlbumFeedEntry.TABLE_NAME + " (" +
                    AllAlbumFeederContract.AllAlbumFeedEntry._ID + " INTEGER PRIMARY KEY," +
                    AllAlbumFeederContract.AllAlbumFeedEntry.COLUMN_ALBUM_NAME + " TEXT," +
                    AllAlbumFeederContract.AllAlbumFeedEntry.COLUMN_ALBUM_COVER + " TEXT," +
                    AllAlbumFeederContract.AllAlbumFeedEntry.COLUMN_ALBUM_TYPE + " INTEGER)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + AllImageFeederContract.FeedEntry.TABLE_NAME;

    public static class AllAlbumFeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "all_album";
        public static final String COLUMN_ALBUM_NAME = "name";
        public static final String COLUMN_ALBUM_COVER = "cover";
        public static final String COLUMN_ALBUM_TYPE = "type";
    }
}
