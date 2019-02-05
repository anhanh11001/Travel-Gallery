package tech.ducletran.travelgallery.ImageData;

import java.util.ArrayList;
import java.util.List;

public class Album {
    private String albumName;
    private List<ImageData> imageList;

    public Album(String name) {
        this.albumName = name;
        imageList = new ArrayList<ImageData>();
    }

    public void addToAlbum(ImageData image) {
        imageList.add(image);
    }

    public void removeFromAlbum(int index) {
        imageList.remove(index);
    }

}
