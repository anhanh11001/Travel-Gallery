package tech.ducletran.travelgallery.ImageData;

import java.util.*;

public class ImageHolder {

    private static List<ImageData> imageDataList = new ArrayList<ImageData>();
    private static Album[] baseAlbum = {new Album("Favorite"),new Album("Food"),new Album("People")};

    public static Album[] getBaseAlbum() {return baseAlbum;}
    public static List<ImageData> getImageDataList() {
        return imageDataList;
    }

    public static void addImage(ImageData data) {
        imageDataList.add(data);
    }

    public static void removeImage(int id) {
        imageDataList.remove(id);
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

}
