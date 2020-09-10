package com.example.upvote;
import com.google.firebase.firestore.PropertyName;

public class Upvote_model{

    @PropertyName("image")
    private String image;
    @PropertyName("title")
    private String title;
    @PropertyName("description")
    private String description;
    @PropertyName("upvote_count")
    private int upvote_count;

    private Upvote_model() {
    }

    public Upvote_model(String image, String title, int upvote_count, String description) {
        this.image = image;
        this.title = title;
        this.upvote_count = upvote_count;
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUpvoteCount() {
        return upvote_count;
    }

    public void setUpvoteCount(int upvote_count) {
        this.upvote_count = upvote_count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

