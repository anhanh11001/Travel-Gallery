package tech.ducletran.travelgallery.ImageData;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AlbumManager {
    public static int ALBUM_FAVORITE_CODE = 0;
    public static int ALBUM_FOOD_CODE = 1;
    public static int ALBUM_PEOPLE_CODE = 2;

    // initialize everythin
    private static Map<Integer,Album> albumHashMap = new HashMap<Integer, Album>() {};
    private static ArrayList<Album> specialAlbum = new ArrayList<Album>();
    private static ArrayList<Album> locationAlbum = new ArrayList<Album>();
    private static ArrayList<Album> othersAlbum = new ArrayList<>();
    private static Album favorite = new Album("Favorite",Album.ALBUM_TYPE_SPECIAL);
    private static Album food = new Album("Food",Album.ALBUM_TYPE_SPECIAL);
    private static Album people = new Album("People",Album.ALBUM_TYPE_SPECIAL);


    // Album method
    public static Album getAlbum(int albumId) {return albumHashMap.get(albumId);}
    public static ArrayList<Album> getSpecialAlbum() {return specialAlbum;}
    public static ArrayList<Album> getLocationAlbum() {return locationAlbum;}
    public static ArrayList<Album> getOthersAlbum() {return othersAlbum;}

    // Register album to album manager
    public static void registerAlbum(Album album) {
        albumHashMap.put(album.getAlbumId(),album);
    }
    public static void registerSpecialAlbum(Album album) {specialAlbum.add(album);}
    public static void registerLocationAlbum(Album album) {locationAlbum.add(album);}
    public static void registerOthersAlbum(Album album) {othersAlbum.add(album);}

    public static int generateId() {
        if (albumHashMap.size() == 0) {
            return 0;
        } else {
            return Collections.max(albumHashMap.keySet()) + 1;
        }
    }

    public static void removeImage(ImageData image) {
        for (Album album: albumHashMap.values()) {
            album.removeFromAlbum(image);
        }
    }

}
