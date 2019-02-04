package tech.ducletran.travelgallery;

import java.util.Date;

public class ImageData {

    private String path;
    private String timeStamp;
    private String albumName;
    private int id;


    public ImageData(String path,String timeStamp,String albumName) {
        this.path = path;
        this.timeStamp = timeStamp;
        this.albumName = albumName;
    }

    public Date getDate() {
        Long dateLong = Long.parseLong(timeStamp);
        return new Date(dateLong);
    }

    public String getPath() {
        return path;
    }

}
