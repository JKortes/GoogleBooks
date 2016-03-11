package net.josecortes.com.googlebooksapp.model;

import java.io.Serializable;

/**
 * Data structure to encapsulate the Book entity
 */

public class Book implements Serializable {
    String title;
    String author;
    String imageThumbnailUrl;
    String imageUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setImageThumbnailUrl(String imageThumbnailUrl) {
        this.imageThumbnailUrl = imageThumbnailUrl;
    }

    public String getImageThumbnailUrl() {
        return imageThumbnailUrl;

    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
