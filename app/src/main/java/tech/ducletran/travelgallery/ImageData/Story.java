package tech.ducletran.travelgallery.ImageData;

public class Story {

    private String title;
    private String description;
    private String cover;
    private int id;

    public Story (String title, String description, String cover) {
        this.title = title;
        this.description = description;
        this.cover = cover;
        this.id = StoriesManager.generateId();
        StoriesManager.registerAlbum(this,id);
    }

    public Story(String title, String description) {
        this(title,description,null);
    }

    // Getters
    public String getTitle() {return title;}
    public String getCover() {return cover;}
    public String getDescription() {return description;}
}
