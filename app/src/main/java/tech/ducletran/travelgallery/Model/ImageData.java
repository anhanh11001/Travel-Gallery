package tech.ducletran.travelgallery.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.google.android.gms.maps.model.LatLng;
import tech.ducletran.travelgallery.Database.AllImageFeederContract;
import tech.ducletran.travelgallery.Database.AllImageReaderDbHelper;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageData {

    private String path;
    private String timeStamp;
    private String latitude;
    private String longtitude;
    private String thumbnail;
    private String size;
    private String title;
    private String description;
    private boolean isPeople;
    private boolean isFood;
    private boolean isFavorite;
    private int imageId;
    private boolean isLocationCounted;

    private SQLiteDatabase database;

    public ImageData(Context context,String path, String timeStamp, String thumbnail, String latitude, String longtitude, String size) {
        database = new AllImageReaderDbHelper(context).getWritableDatabase();

        this.path = path;
        this.timeStamp = timeStamp;
        this.thumbnail = thumbnail;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.size = size;
        this.title = "";
        this.description = "";
        this.isFavorite = false;
        this.isFood = false;
        this.isPeople = false;
        this.isLocationCounted = false;
        this.imageId = insertNewImage(path,timeStamp,thumbnail,latitude,longtitude,size,
                "","",false,false,false,false);
    }

    public ImageData(Context context,String path,String timeStamp, String thumbnail, String latitude, String longtitude,
                     String size, String title, String description, int imageId, boolean isFavorite, boolean isFood,
                     boolean isPeople, boolean isLocationCounted) {
        database = new AllImageReaderDbHelper(context).getWritableDatabase();

        this.isLocationCounted = isLocationCounted;
        this.path = path;
        this.timeStamp = timeStamp;
        this.thumbnail = thumbnail;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.size = size;
        this.title = title;
        this.description = description;
        this.isFavorite = isFavorite;
        this.isFood = isFood;
        this.isPeople = isPeople;
        this.imageId = imageId;

        if (isFood) {AlbumManager.getAlbum(Album.DEFAULT_FOOD_ID).addToAlbum(this); }
        if (isFavorite) {AlbumManager.getAlbum(Album.DEFAULT_FAVORITE_ID).addToAlbum(this);}
        if (isPeople) {AlbumManager.getAlbum(Album.DEFAULT_PEOPLE_ID).addToAlbum(this);}
    }



    public ImageMarker getImageMarker() {
        if (longtitude == null || latitude == null) {
            return null;
        }
        return new ImageMarker(new LatLng(Double.valueOf(latitude),Double.valueOf(longtitude))
                        ,this);
    }

    public Date getDate() {
        Long dateLong = Long.parseLong(timeStamp);
        return new Date(dateLong);
    }
    public String getDateFormatted() {
        DateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
        return formatter.format(getDate());
    }
    public boolean getIsLocationCounted() {return isLocationCounted;}
    public int getImageId() {
        return imageId;
    }
    public String getTitle() {return this.title;}
    public String getDescription() {return this.description;}
    public String getLatitude() { return latitude; }
    public String getLongtitude() { return longtitude; }
    public String getThumbnail() { return thumbnail; }
    public boolean getIsPeople() {
        return isPeople;
    }
    public boolean getIsFood() {
        return isFood;
    }
    public boolean getIsFavorite() {
        return isFavorite;
    }
    public String getPath() {
        return path;
    }

    // Setters
    public void setIsLocationCounted() {
        isLocationCounted = !isLocationCounted;
        ContentValues value = new ContentValues();
        value.put(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_IS_LOCATION_COUNTED,(isLocationCounted) ? 1:0);

        String selection = AllImageFeederContract.FeedEntry._ID + " LIKE ?";
        String[] selectionArgs = {Integer.toString(imageId)};

        database.update(AllImageFeederContract.FeedEntry.TABLE_NAME,
                value,selection,selectionArgs);
    }
    public void setFood() {
        isFood = !isFood;
        ContentValues value = new ContentValues();
        value.put(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_IS_FOOD,(isFood) ? 1:0);

        String selection = AllImageFeederContract.FeedEntry._ID + " LIKE ?";
        String[] selectionArgs = {Integer.toString(imageId)};

        database.update(AllImageFeederContract.FeedEntry.TABLE_NAME,
                value,selection,selectionArgs);
    }
    public void setFavorite() {
        isFavorite = !isFavorite;
        ContentValues value = new ContentValues();
        value.put(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_IS_FAVORITE,(isFavorite) ? 1:0);

        String selection = AllImageFeederContract.FeedEntry._ID + " LIKE ?";
        String[] selectionArgs = {Integer.toString(imageId)};

        database.update(AllImageFeederContract.FeedEntry.TABLE_NAME,
                value,selection,selectionArgs);
    }
    public void setPeople() {
        isPeople = !isPeople;
        ContentValues value = new ContentValues();
        value.put(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_IS_PEOPLE,(isPeople) ? 1:0);

        String selection = AllImageFeederContract.FeedEntry._ID + " LIKE ?";
        String[] selectionArgs = {Integer.toString(imageId)};

        database.update(AllImageFeederContract.FeedEntry.TABLE_NAME,
                value,selection,selectionArgs);
    }
    public void setNewTitle(String newTitle) {
        this.title = newTitle;
        // Update inside database;
        ContentValues value = new ContentValues();
        value.put(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_TITLE,newTitle);

        String selection = AllImageFeederContract.FeedEntry._ID + " LIKE ?";
        String[] selectionArgs = {Integer.toString(imageId)};

        database.update(AllImageFeederContract.FeedEntry.TABLE_NAME,
                value,selection,selectionArgs);
    }
    public void setNewDescription(String newDescription) {
        this.description = newDescription;
        // Update inside database;
        ContentValues value = new ContentValues();
        value.put(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_DESCRIPTION,newDescription);

        String selection = AllImageFeederContract.FeedEntry._ID + " LIKE ?";
        String[] selectionArgs = {Integer.toString(imageId)};

        database.update(AllImageFeederContract.FeedEntry.TABLE_NAME,
                value,selection,selectionArgs);
    }
    public void setNewDate(Date date) {
        // Do something here + update inside database
        this.timeStamp = Long.toString(date.getTime());
        ContentValues values = new ContentValues();
        values.put(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_TIMESTAMP,timeStamp);

        String selection = AllImageFeederContract.FeedEntry._ID + " LIKE ?";
        String[] selectionArgs = {Integer.toString(imageId)};

        database.update(AllImageFeederContract.FeedEntry.TABLE_NAME,
                values,selection,selectionArgs);
    }

    public void setNewLocation(String newLatitude, String newLongtitude) {
        this.latitude = newLatitude;
        this.longtitude = newLongtitude;
        // Update inside database
        ContentValues values = new ContentValues();
        values.put(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_LATITUDE,newLatitude);
        values.put(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_LONGTITUDE,newLongtitude);

        String selection = AllImageFeederContract.FeedEntry._ID + " LIKE ?";
        String[] selectionArgs = {Integer.toString(imageId)};

        database.update(AllImageFeederContract.FeedEntry.TABLE_NAME,
                values,selection,selectionArgs);
    }


    public String getSize() {
        return String.valueOf(new DecimalFormat("#.##")
                .format(Double.parseDouble(size) / 1048576)) + " MB" ;
    }

    private int insertNewImage(String path, String timeStamp, String thumbnail, String latitude,
                               String longtitude, String size,String title,String description,
                               boolean isFavorite, boolean isPeople, boolean isFood, boolean isLocationCounted) {
        ContentValues values = new ContentValues();

        values.put(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_PATH,path);
        values.put(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_TIMESTAMP,timeStamp);
        values.put(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_THUMBNAIL,thumbnail);
        values.put(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_LATITUDE,latitude);
        values.put(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_LONGTITUDE,longtitude);
        values.put(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_SIZE,size);
        values.put(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_TITLE,title);
        values.put(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_DESCRIPTION,description);
        values.put(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_IS_FAVORITE, (isFavorite) ? 1:0);
        values.put(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_IS_PEOPLE, (isPeople) ? 1:0);
        values.put(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_IS_FOOD, (isFood) ? 1:0);
        values.put(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_IS_LOCATION_COUNTED, (isLocationCounted) ? 1:0);

        return (int) database.insert(AllImageFeederContract.FeedEntry.TABLE_NAME,null,values);
    }
}
