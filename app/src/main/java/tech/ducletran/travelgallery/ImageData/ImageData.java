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
    private boolean isPeople;
    private boolean isFood;
    private boolean isFavorite;
    private int imageId;

    public ImageData(Context context,String path, String timeStamp, String thumbnail, String latitude, String longtitude, String size) {
        this.path = path;
        this.timeStamp = timeStamp;
        this.thumbnail = thumbnail;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.size = size;
        isFavorite = false;
        isFood = false;
        isPeople = false;
        imageId = insertNewImage(context,path,timeStamp,thumbnail,latitude,longtitude,size);
    }

    public ImageData(String path,String timeStamp, String thumbnail, String latitude, String longtitude, String size, int imageId) {
        this.path = path;
        this.timeStamp = timeStamp;
        this.thumbnail = thumbnail;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.size = size;
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
                        ,"Title","Snippet",this);
    }

    public Date getDate() {
        Long dateLong = Long.parseLong(timeStamp);
        return new Date(dateLong);
    }

    public String getDateFormatted() {
        DateFormat formatter = new SimpleDateFormat("hh:mm:ss | dd MMMM yyyy");
        return formatter.format(getDate());
    }

    public String getPath() {
        return path;
    }

    public void setFood() { isFood = !isFood; }
    public void setFavorite() { isFavorite = !isFavorite; }
    public void setPeople() {
        isPeople = !isPeople;
    }
    public String getLatitude() { return latitude; }
    public String getLongtitude() { return longtitude; }
    public String getThumbnail() { return thumbnail; }
    public boolean isPeople() { return isPeople; }
    public boolean isFood() { return isFood; }
    public boolean isFavorite() { return isFavorite; }
    public boolean getIsPeople() {
        return isPeople;
    }
    public boolean getIsFood() {
        return isFood;
    }
    public boolean getIsFavorite() {
        return isFavorite;
    }

    public String getSize() {
        return String.valueOf(new DecimalFormat("#.##")
                .format(Double.parseDouble(size) / 1048576)) + " MB" ;
    }

    private int insertNewImage(Context context, String path, String timeStamp, String thumbnail, String latitude,
                               String longtitude, String size) {
        SQLiteDatabase db = new AllImageReaderDbHelper(context).getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_PATH,path);
        values.put(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_TIMESTAMP,timeStamp);
        values.put(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_THUMBNAIL,thumbnail);
        values.put(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_LATITUDE,latitude);
        values.put(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_LONGTITUDE,longtitude);
        values.put(AllImageFeederContract.FeedEntry.COLUMN_IMAGE_SIZE,size);

        return (int) db.insert(AllImageFeederContract.FeedEntry.TABLE_NAME,null,values);
    }
}
