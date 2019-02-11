package tech.ducletran.travelgallery.ImageData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoriesManager {

    private static Map<Integer,Story> storyHashMap = new HashMap<>();
    private static List<Story> storyList = new ArrayList<>();

    public static void registerAlbum(Story story,int id) {
        storyList.add(story);
        storyHashMap.put(id,story);
    }

    public static List<Story> getStoryList() {return storyList;}

    public static int generateId() {return 0;}
}
