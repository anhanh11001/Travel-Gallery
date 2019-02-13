package tech.ducletran.travelgallery.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import tech.ducletran.travelgallery.Database.*;

import java.util.*;

public class ImageManager {
    public static String[] projection = { MediaStore.MediaColumns.DATA, // File path
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // Album name
            MediaStore.Images.Media.DATE_TAKEN,          // Date taken
            MediaStore.Images.Thumbnails.DATA,          // Thumbnail link
            MediaStore.Images.Media.LATITUDE,           // Latitude
            MediaStore.Images.Media.LONGITUDE,          // Longtitude
            MediaStore.Images.Media.SIZE,                // Size
    };

    private static Map<Integer,ImageData> imageDataHashMap = new HashMap<Integer,ImageData>();
    private static ArrayList<ImageData> imageDataList = new ArrayList<>();

    public static ArrayList<ImageData> getImageDataList() {
        return imageDataList;
    }
    public static ImageData getImageById(int id) {
        return imageDataHashMap.get(id);
    }

    public static void addImage(ImageData data) {
        imageDataHashMap.put(data.getImageId(),data);
        imageDataList.add(data);
    }

    public static void resetImageDataList() {
        imageDataList.clear();
        imageDataList.addAll(imageDataHashMap.values());
    }

    public static void removeImage(ImageData data,Context context) {
        imageDataHashMap.remove(data.getImageId());
        resetImageDataList();
        AlbumManager.removeImage(data);

        String selection = AllImageFeederContract.FeedEntry._ID + " LIKE ?";
        String[] selectionArgs = {Integer.toString(data.getImageId())};

        new AllImageReaderDbHelper(context).getWritableDatabase().delete(
                AllImageFeederContract.FeedEntry.TABLE_NAME,selection,selectionArgs
        );
    }

    public static void loadImage(Context context) {

        AlbumManager.registerAlbum(new Album(context,Album.DEFAULT_FAVORITE_ID,"Favorite",null,Album.ALBUM_TYPE_SPECIAL));
        AlbumManager.registerAlbum(new Album(context,Album.DEFAULT_FOOD_ID,"Food",null,Album.ALBUM_TYPE_SPECIAL));
        AlbumManager.registerAlbum(new Album(context,Album.DEFAULT_PEOPLE_ID,"People",null,Album.ALBUM_TYPE_SPECIAL));

        SQLiteDatabase db = new AllImageReaderDbHelper(context).getReadableDatabase();
        String[] projetion = {
                BaseColumns._ID,
                AllImageFeederContract.FeedEntry.COLUMN_IMAGE_PATH,
                AllImageFeederContract.FeedEntry.COLUMN_IMAGE_THUMBNAIL,
                AllImageFeederContract.FeedEntry.COLUMN_IMAGE_TIMESTAMP,
                AllImageFeederContract.FeedEntry.COLUMN_IMAGE_LATITUDE,
                AllImageFeederContract.FeedEntry.COLUMN_IMAGE_LONGTITUDE,
                AllImageFeederContract.FeedEntry.COLUMN_IMAGE_SIZE,
                AllImageFeederContract.FeedEntry.COLUMN_IMAGE_TITLE,
                AllImageFeederContract.FeedEntry.COLUMN_IMAGE_DESCRIPTION,
                AllImageFeederContract.FeedEntry.COLUMN_IMAGE_IS_PEOPLE,
                AllImageFeederContract.FeedEntry.COLUMN_IMAGE_IS_FOOD,
                AllImageFeederContract.FeedEntry.COLUMN_IMAGE_IS_FAVORITE,
                AllImageFeederContract.FeedEntry.COLUMN_IMAGE_IS_LOCATION_COUNTED
        };

        Cursor cursor = db.query(AllImageFeederContract.FeedEntry.TABLE_NAME,projetion,null,null,null,null,null);
        while (cursor.moveToNext()) {
            int imageId = (int) cursor.getLong(cursor.getColumnIndexOrThrow(AllImageFeederContract.FeedEntry._ID));
            String path = cursor.getString(cursor.getColumnIndexOrThrow(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_PATH));
            String thumbnail = cursor.getString(cursor.getColumnIndexOrThrow(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_THUMBNAIL));
            String time = cursor.getString(cursor.getColumnIndexOrThrow(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_TIMESTAMP));
            String latittude = cursor.getString(cursor.getColumnIndexOrThrow(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_LATITUDE));
            String longtitude = cursor.getString(cursor.getColumnIndexOrThrow(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_LONGTITUDE));
            String size = cursor.getString(cursor.getColumnIndexOrThrow(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_SIZE));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_TITLE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_DESCRIPTION));
            boolean isFood =
                    (cursor.getInt(cursor.getColumnIndexOrThrow(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_IS_FOOD)) == 1);
            boolean isPeople =
                    (cursor.getInt(cursor.getColumnIndexOrThrow(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_IS_PEOPLE)) == 1);
            boolean isFavorite =
                    (cursor.getInt(cursor.getColumnIndexOrThrow(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_IS_FAVORITE)) == 1);
            boolean isLocationCounted =
                    (cursor.getInt(cursor.getColumnIndexOrThrow(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_IS_LOCATION_COUNTED)) == 1);

            ImageManager.addImage(
                    new ImageData(context,path,time,thumbnail,latittude,longtitude,size,title,description,imageId,
                            isFavorite,isFood,isPeople, isLocationCounted));

        }
        cursor.close();
        loadAlbum(context);
        loadStory(context);
    }

    public static void sortByDate() {
        Collections.sort(imageDataList, new Comparator<ImageData>() {
            @Override
            public int compare(ImageData o1, ImageData o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
    }

    public static void shuffle() {
        Collections.shuffle(imageDataList);
    }

    private static void loadAlbum(Context context) {
        SQLiteDatabase albumDatabase = new AllAlbumReaderDbHelper(context).getReadableDatabase();
        String[] projetion = {
                BaseColumns._ID,
                AllAlbumFeederContract.AllAlbumFeedEntry.COLUMN_ALBUM_NAME,
                AllAlbumFeederContract.AllAlbumFeedEntry.COLUMN_ALBUM_COVER,
                AllAlbumFeederContract.AllAlbumFeedEntry.COLUMN_ALBUM_TYPE,
        };

        Cursor cursor = albumDatabase.query(AllAlbumFeederContract.AllAlbumFeedEntry.TABLE_NAME,projetion,null,null,null,null,null);
        while (cursor.moveToNext()) {
            int albumId = (int) cursor.getLong(cursor.getColumnIndexOrThrow(AllAlbumFeederContract.AllAlbumFeedEntry._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(AllAlbumFeederContract.AllAlbumFeedEntry.COLUMN_ALBUM_NAME));
            String cover = cursor.getString(cursor.getColumnIndexOrThrow(AllAlbumFeederContract.AllAlbumFeedEntry.COLUMN_ALBUM_COVER));
            int type = cursor.getInt(cursor.getColumnIndexOrThrow(AllAlbumFeederContract.AllAlbumFeedEntry.COLUMN_ALBUM_TYPE));

            Album album = new Album(context,albumId,name,cover,type);
            AlbumManager.registerAlbum(album);
            SQLiteDatabase singleAlbumDatabase = new SingleAlbumReaderDbHelper(context,albumId).getReadableDatabase();
            String[] singleAlbumProjection = {
                   SingleAlbumReaderDbHelper.ID,
            };
            Cursor singleAlbumCursor = singleAlbumDatabase.query(SingleAlbumReaderDbHelper.getTableName(albumId),singleAlbumProjection
                    ,null,null,null,null,null);
            while (singleAlbumCursor.moveToNext()) {
                int imageId = singleAlbumCursor.getInt(singleAlbumCursor.getColumnIndexOrThrow(SingleAlbumReaderDbHelper.ID));
                album.addToAlbum(ImageManager.getImageById(imageId));
            }
        }
    }

    private static void loadStory(Context context) {
        SQLiteDatabase storyDatabase = new AllStoriesReaderDbHelper(context).getReadableDatabase();
        String[] projection = {
            AllStoriesFeederContract.AllStoryFeedEntry._ID,
            AllStoriesFeederContract.AllStoryFeedEntry.COLUMN_STORY_NAME,
            AllStoriesFeederContract.AllStoryFeedEntry.COLUMN_STORY_COVER,
            AllStoriesFeederContract.AllStoryFeedEntry.COLUMN_STORY_DESCRIPTION
        };

        Cursor cursor = storyDatabase.query(AllStoriesFeederContract.AllStoryFeedEntry.TABLE_NAME,projection,null,null,null,null,null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(AllStoriesFeederContract.AllStoryFeedEntry._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(AllStoriesFeederContract.AllStoryFeedEntry.COLUMN_STORY_NAME));
            String cover = cursor.getString(cursor.getColumnIndexOrThrow(AllStoriesFeederContract.AllStoryFeedEntry.COLUMN_STORY_COVER));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(AllStoriesFeederContract.AllStoryFeedEntry.COLUMN_STORY_DESCRIPTION));
            new Story(context,id,title,description,cover);
        }
    }

}
