package tech.ducletran.travelgallery.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import tech.ducletran.travelgallery.Database.AllAlbumFeederContract;
import tech.ducletran.travelgallery.Database.AllAlbumReaderDbHelper;
import tech.ducletran.travelgallery.Database.SingleAlbumReaderDbHelper;

import java.util.*;

public class Album {
    public static int ALBUM_TYPE_SPECIAL = 0;
    public static int ALBUM_TYPE_LOCATION = 1;
    public static int ALBUM_TYPE_OTHER = 2;

    public static int DEFAULT_FAVORITE_ID = 9191;
    public static int DEFAULT_FOOD_ID = 9292;
    public static int DEFAULT_PEOPLE_ID = 9393;

    private String albumName;
    private HashMap<Integer,ImageData> imageHashMap;
    private int albumId;
    private String albumCover;
    private SQLiteDatabase allAlbumDatabase;
    private SQLiteDatabase singleAlbumDatabase;
    private int type;


    public Album(Context context, String name, int type) {
        this.imageHashMap = new HashMap<Integer,ImageData>();
        allAlbumDatabase = AllAlbumReaderDbHelper.getInstance(context).getWritableDatabase();

        this.type = type;
        this.albumName = name;
        this.albumCover = null;
        this.albumId = insertNewAlbum(albumName,albumCover,type);
        if (type == ALBUM_TYPE_SPECIAL) {
            AlbumManager.registerSpecialAlbum(this);
        } else if (type == ALBUM_TYPE_LOCATION) {
            AlbumManager.registerLocationAlbum(this);
        } else if (type == ALBUM_TYPE_OTHER) {
            AlbumManager.registerOthersAlbum(this);
        }
        singleAlbumDatabase = SingleAlbumReaderDbHelper.getInstance(context, albumId).getWritableDatabase();
        Log.d("DATABASE", "New album added with id: " + albumId);
    }

    public Album(Context context, int albumId,String name, String albumCover, int type) {
        allAlbumDatabase = AllAlbumReaderDbHelper.getInstance(context).getWritableDatabase();

        // Create constructor here
        this.imageHashMap = new HashMap<Integer,ImageData>();
        this.albumCover = albumCover;
        this.albumId = albumId;
        this.albumName = name;
        this.type = type;

        if (type == ALBUM_TYPE_SPECIAL) {
            AlbumManager.registerSpecialAlbum(this);
        } else if (type == ALBUM_TYPE_LOCATION) {
            AlbumManager.registerLocationAlbum(this);
        } else if (type == ALBUM_TYPE_OTHER) {
            AlbumManager.registerOthersAlbum(this);
        }
        singleAlbumDatabase = SingleAlbumReaderDbHelper.getInstance(context, albumId).getWritableDatabase();
    }

    // Setter
    public void setSpecialAlbumCover(String albumCover) {
        if (type == ALBUM_TYPE_SPECIAL) {
            this.albumCover = albumCover;
        }
    }
    public void setAlbumCover(String albumCover) {
        this.albumCover = albumCover;
        ContentValues value = new ContentValues();
        value.put(AllAlbumFeederContract.AllAlbumFeedEntry.COLUMN_ALBUM_COVER,albumCover);

        String selection = AllAlbumFeederContract.AllAlbumFeedEntry._ID + " LIKE ?";
        String[] selectionArgs = {Integer.toString(albumId)};

        allAlbumDatabase.update(AllAlbumFeederContract.AllAlbumFeedEntry.TABLE_NAME,
                value,selection,selectionArgs);
    }
    public void addToAlbum(ImageData image, int codeAdd) {
        imageHashMap.put(image.getImageId(),image);
        if (codeAdd == 1) {
            ContentValues value = new ContentValues();
            value.put(SingleAlbumReaderDbHelper.ID,image.getImageId());
            singleAlbumDatabase.insert(SingleAlbumReaderDbHelper.getTableName(albumId),null,value);
        }

    }
    public void removeFromAlbum(ImageData image) {
        int imageId = image.getImageId();
        imageHashMap.remove(imageId);

        String selection = SingleAlbumReaderDbHelper.ID + " LIKE ?";
        String[] selectionArgs = {Integer.toString(imageId)};

        singleAlbumDatabase.delete(SingleAlbumReaderDbHelper.getTableName(albumId),selection,selectionArgs);
    }
    // To rename, a new name must be different and has the number of character from 1-20
    public boolean rename(String newName) {
        if (newName.length() == 0 || newName.length() > 20) {
            return false;
        }
        albumName = newName;
        ContentValues value = new ContentValues();
        value.put(AllAlbumFeederContract.AllAlbumFeedEntry.COLUMN_ALBUM_NAME,albumName);

        String selection = AllAlbumFeederContract.AllAlbumFeedEntry._ID + " LIKE ?";
        String[] selectionArgs = {Integer.toString(albumId)};

        allAlbumDatabase.update(AllAlbumFeederContract.AllAlbumFeedEntry.TABLE_NAME,
                value,selection,selectionArgs);
        return true;
    }

    // Getter
    public int getAlbumType() {return this.type;}
    public String getAlbumCover() { return albumCover; }
    public int getAlbumId() {return albumId;}
    public String getAlbumName() {return albumName;}
    public ArrayList<ImageData> getAlbumImageList() {
        ArrayList<ImageData> imageList = new ArrayList<ImageData>(imageHashMap.values());
        Collections.sort(imageList, new Comparator<ImageData>() {
            @Override
            public int compare(ImageData o1, ImageData o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
        return imageList;
    }

    private int insertNewAlbum(String albumName, String albumCover, int albumType) {
        ContentValues values = new ContentValues();
        values.put(AllAlbumFeederContract.AllAlbumFeedEntry.COLUMN_ALBUM_NAME,albumName);
        values.put(AllAlbumFeederContract.AllAlbumFeedEntry.COLUMN_ALBUM_COVER,albumCover);
        values.put(AllAlbumFeederContract.AllAlbumFeedEntry.COLUMN_ALBUM_TYPE,albumType);

        return (int) allAlbumDatabase.insert(AllAlbumFeederContract.AllAlbumFeedEntry.TABLE_NAME,null,values);
    }

}
