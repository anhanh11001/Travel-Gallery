package tech.ducletran.travelgallery.Model;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import tech.ducletran.travelgallery.Database.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AlbumManager {
    // initialize everything
    private static Map<Integer,Album> albumHashMap = new HashMap<Integer, Album>() {};
    private static ArrayList<Album> specialAlbum = new ArrayList<Album>();
    private static ArrayList<Album> locationAlbum = new ArrayList<Album>();
    private static ArrayList<Album> othersAlbum = new ArrayList<>();

    // Album method
    public static Album getAlbum(int albumId) {return albumHashMap.get(albumId);}
    public static ArrayList<Album> getSpecialAlbum() {return specialAlbum;}
    public static ArrayList<Album> getLocationAlbum() {return locationAlbum;}
    public static ArrayList<Album> getOthersAlbum() {return othersAlbum;}

    // Register album to album manager
    public static void registerAlbum(Album album) {
        albumHashMap.put(album.getAlbumId(),album);
    }
    public static void registerSpecialAlbum(Album album) {specialAlbum.add(album);}
    public static void registerLocationAlbum(Album album) {locationAlbum.add(album);}
    public static void registerOthersAlbum(Album album) {othersAlbum.add(album);}


    // Remove data
    public static void removeImage(ImageData image) {
        for (Album album: albumHashMap.values()) {
            album.removeFromAlbum(image);
        }
    }

    public static void removeAlbum(Context context, Album album) {
        albumHashMap.remove(album.getAlbumId());
        if (album.getAlbumType() == Album.ALBUM_TYPE_OTHER) {
            othersAlbum.remove(album);
        } else if (album.getAlbumType() == Album.ALBUM_TYPE_LOCATION) {
            locationAlbum.remove(album);
        }
        SQLiteDatabase singleAlbumDatabase = SingleAlbumReaderDbHelper.getInstance(context,album.getAlbumId()).getWritableDatabase();
        singleAlbumDatabase.execSQL(SingleAlbumReaderDbHelper.getSQLDeleteEntries(album.getAlbumId()));
        context.deleteDatabase(SingleAlbumReaderDbHelper.getDatabaseName(album.getAlbumId()));

        String selection = AllAlbumFeederContract.AllAlbumFeedEntry._ID + " LIKE ?";
        String[] selectionArgs = {Integer.toString(album.getAlbumId())};

        AllAlbumReaderDbHelper.getInstance(context).getWritableDatabase()
                .delete(AllAlbumFeederContract.AllAlbumFeedEntry.TABLE_NAME,selection,selectionArgs);

    }

}
