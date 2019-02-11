package tech.ducletran.travelgallery.ImageData;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.google.android.gms.maps.model.LatLng;

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
        isFavorite = false;
        isFood = false;
        isPeople = false;
        imageId = insertNewImage(path,timeStamp,thumbnail,latitude,longtitude,size,"","");
    }

    public ImageData(Context context,String path,String timeStamp, String thumbnail, String latitude, String longtitude,
                     String size, String title, String description, int imageId) {
        database = new AllImageReaderDbHelper(context).getWritableDatabase();

        this.path = path;
        this.timeStamp = timeStamp;
        this.thumbnail = thumbnail;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.size = size;
        this.title = title;
        this.description = description;
        isFavorite = false;
        isFood = false;
        isPeople = false;
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
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

    public void setFood() { isFood = !isFood; }
    public void setFavorite() { isFavorite = !isFavorite; }
    public void setPeople() {
        isPeople = !isPeople;
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
                               String longtitude, String size,String title,String description) {
        ContentValues values = new ContentValues();

        values.put(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_PATH,path);
        values.put(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_TIMESTAMP,timeStamp);
        values.put(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_THUMBNAIL,thumbnail);
        values.put(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_LATITUDE,latitude);
        values.put(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_LONGTITUDE,longtitude);
        values.put(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_SIZE,size);
        values.put(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_TITLE,title);
        values.put(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_DESCRIPTION,description);

        return (int) database.insert(AllImageFeederContract.FeedEntry.TABLE_NAME,null,values);
    }
}
