package tech.ducletran.travelgallery.ImageData;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
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

    public static void removeImage(ImageData data) {
        imageDataHashMap.remove(data.getImageId());
        imageDataList = new ArrayList<ImageData>(imageDataHashMap.values());
        AlbumManager.removeImage(data);
    }

    public static void loadImage(Context context) {
        Uri uriExternal = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;


        Cursor cursor = context.getContentResolver().query(uriExternal,projection,
                null, null,null);

        while (cursor.moveToNext() ) {
            String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
            String timestamp = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN));
            String albumName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
            String thumbnail = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA));
            String latitude = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.LATITUDE));
            String longtitude = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.LONGITUDE));
            String size = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE));
            if (albumName.equals("Facebook") || albumName.equals("Camera")) {
                ImageManager.addImage(new ImageData(path,timestamp,thumbnail,latitude,longtitude,size));
            }

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
