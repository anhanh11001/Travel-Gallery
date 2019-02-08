package tech.ducletran.travelgallery.ImageData;

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

    public ImageData(String path, String timeStamp, String thumbnail, String latitude, String longtitude, String size) {
        this.path = path;
        this.timeStamp = timeStamp;
        this.thumbnail = thumbnail;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.size = size;
        isFavorite = false;
        isFood = false;
        isPeople = false;
        imageId = ImageManager.generateImageId();
    }

    public int getImageId() {
        return imageId;
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


    public String getSize() {
        String sizeFormatted =
                String.valueOf(new DecimalFormat("#.##")
                        .format(Double.parseDouble(size) / 1048576)) + " MB" ;
        return sizeFormatted;
    }

    public boolean getIsPeople() {
        return isPeople;
    }

    public boolean getIsFood() {
        return isFood;
    }

    public boolean getIsFavorite() {
        return isFavorite;
    }
}
