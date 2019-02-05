package tech.ducletran.travelgallery.ImageData;

import java.util.Date;

public class ImageData {

    private String path;
    private String timeStamp;
    private boolean isPeople;
    private boolean isFood;
    private boolean isFavorite;


    public ImageData(String path,String timeStamp) {
        this.path = path;
        this.timeStamp = timeStamp;
        isFavorite = false;
        isFood = false;
        isPeople = false;
    }

    public Date getDate() {
        Long dateLong = Long.parseLong(timeStamp);
        return new Date(dateLong);
    }

    public String getPath() {
        return path;
    }

    public void setFood() {
        isFood = !isFood;
    }
    public void setFavorite() {
        isFavorite = !isFavorite;
    }
    public void setPeople() {
        isPeople = !isPeople;
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
