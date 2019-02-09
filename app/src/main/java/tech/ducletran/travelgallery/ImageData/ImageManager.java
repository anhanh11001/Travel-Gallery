package tech.ducletran.travelgallery.ImageData;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore;

import java.util.*;

public class ImageManager {
    public static String[] projection = { MediaStore.MediaColumns.DATA, // File path
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // Album name
            MediaStore.Images.Media.DATE_TAKEN,          // Date taken
            MediaStore.Images.Thumbnails.DATA,          // Thumbnail link
            MediaStore.Images.Media.LATITUDE,           // Latitude
            MediaStore.Images.Media.LONGITUDE,          // Longtitude
            MediaStore.Images.Media.SIZE                // Size
    };

    private static Map<Integer,ImageData> imageDataHashMap = new HashMap<Integer,ImageData>();
    private static ArrayList<ImageData> imageDataList = new ArrayList<ImageData>(imageDataHashMap.values());

    public static ArrayList<ImageData> getImageDataList() {
        return imageDataList;
    }

    public static void addImage(ImageData data) {
        imageDataHashMap.put(data.getImageId(),data);
        imageDataList = new ArrayList<ImageData>(imageDataHashMap.values());
    }

    public static void removeImage(ImageData data,Context context) {
        imageDataHashMap.remove(data.getImageId());
        imageDataList = new ArrayList<ImageData>(imageDataHashMap.values());
        AlbumManager.removeImage(data);

        String selection = AllImageFeederContract.FeedEntry._ID + " LIKE ?";
        String[] selectionArgs = {Integer.toString(data.getImageId())};

        new AllImageReaderDbHelper(context).getReadableDatabase().delete(
                AllImageFeederContract.FeedEntry.TABLE_NAME,selection,selectionArgs
        );
    }

    public static void loadImage(Context context) {
        SQLiteDatabase db = new AllImageReaderDbHelper(context).getReadableDatabase();
        String[] projetion = {
                BaseColumns._ID,
                AllImageFeederContract.FeedEntry.COLUMN_IMAGE_PATH,
                AllImageFeederContract.FeedEntry.COLUMN_IMAGE_THUMBNAIL,
                AllImageFeederContract.FeedEntry.COLUMN_IMAGE_TIMESTAMP,
                AllImageFeederContract.FeedEntry.COLUMN_IMAGE_LATITUDE,
                AllImageFeederContract.FeedEntry.COLUMN_IMAGE_LONGTITUDE,
                AllImageFeederContract.FeedEntry.COLUMN_IMAGE_SIZE
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
            ImageManager.addImage(new ImageData(path,time,thumbnail,latittude,longtitude,size,imageId));

        }
        cursor.close();
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
    public static int generateImageId() {
        if (imageDataHashMap.size() ==0) {
            return 0;
        } else {
            return Collections.max(imageDataHashMap.keySet()) + 1;
        }
    }

}
