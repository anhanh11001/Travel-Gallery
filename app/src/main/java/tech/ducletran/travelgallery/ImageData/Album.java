package tech.ducletran.travelgallery.ImageData;

import java.util.*;

public class Album {
    public static int ALBUM_TYPE_SPECIAL = 0;
    public static int ALBUM_TYPE_LOCATION = 1;
    public static int ALBUM_TYPE_OTHER = 2;

    private String albumName;
    private HashMap<Integer,ImageData> imageHashMap;
    private int albumId;
    private int type;

    public Album(String name, int type) {
        this.albumName = name;
        this.imageHashMap = new HashMap<Integer,ImageData>();
        this.albumId = AlbumManager.generateId();
        this.type = type;
        AlbumManager.registerAlbum(this);
    }

    public int getAlbumId() {return albumId;}
    public String getAlbumName() {return albumName;}

    public ArrayList<ImageData> getAlbumImageList() {
        ArrayList<ImageData> imageList = new ArrayList<ImageData>(imageHashMap.values());
        Collections.sort(imageList, new Comparator<ImageData>() {
            @Override
            public int compare(ImageData o1, ImageData o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
        return imageList;
    }

    public void addToAlbum(ImageData image) {
        imageHashMap.put(image.getImageId(),image);
    }

    public void removeFromAlbum(ImageData image) {
        imageHashMap.remove(image.getImageId());
    }

    // To rename, a new name must be different and has the number of character from 1-20
    public boolean rename(String newName) {
        if (newName.length() == 0 || newName.length() > 20) {
            return false;
        }
        albumName = newName;
        return true;
    }



}
