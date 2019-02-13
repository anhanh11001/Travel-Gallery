package tech.ducletran.travelgallery.Model;

import android.content.Context;

public class Story {

    private String title;
    private String description;
    private String cover;
    private int id;

    public Story (Context context, String title, String description, String cover) {
        this.title = title;
        this.description = description;
        this.cover = cover;
        this.id = StoriesManager.generateId(context,this);
        StoriesManager.registerStory(this,id);
    }

    public Story (int id, String title, String description, String cover) {
        this.title = title;
        this.id = id;
        this.description = description;
        this.cover = cover;
        StoriesManager.registerStory(this,id);
    }


    // Getters
    public String getTitle() {return title;}
    public String getCover() {return cover;}
    public String getDescription() {return description;}
    public int getStoryId() {return id;}
}
