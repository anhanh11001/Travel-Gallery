package tech.ducletran.travelgallery.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SingleAlbumReaderDbHelper extends SQLiteOpenHelper {

    public static final String ID = "id";
    private static final String TABLE_NAME = "album_";

    private static final int DATABASE_VERSION = 1;
    private int albumId;
    private static SingleAlbumReaderDbHelper helper;

    private SingleAlbumReaderDbHelper(Context context, int albumId) {
        super(context, getDatabaseName(albumId), null, DATABASE_VERSION);
        this.albumId = albumId;
    }

    public static SingleAlbumReaderDbHelper getInstance(Context context, int albumId) {
        if (helper == null || helper.albumId != albumId) {
            helper = new SingleAlbumReaderDbHelper(context,albumId);
        }
        return helper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DATABASE", "New table created " + albumId);
        db.execSQL(SingleAlbumReaderDbHelper.getSQLCreateEntries(albumId));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SingleAlbumReaderDbHelper.getSQLDeleteEntries(albumId));
        Log.d("DATABASE", "New table updated " + albumId);
        onCreate(db);
    }

    public static String getTableName(int albumId) {return TABLE_NAME + albumId;}
    public static String getDatabaseName(int albumId) { return "AllImageReader_" + albumId + ".db"; }
    private static String getSQLCreateEntries(int albumId) {
        return "CREATE TABLE " + getTableName(albumId) + " (" + ID + " INTEGER)"; }
    public static String getSQLDeleteEntries(int albumId) {
        return "DROP TABLE IF EXISTS " + getTableName(albumId); }

}
