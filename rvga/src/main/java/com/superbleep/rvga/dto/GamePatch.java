package com.superbleep.rvga.dto;

public class GamePatch {
    private String title;
    private String developer;
    private String publisher;
    private Long platformId;
    private String genre;

    public GamePatch(String title, String developer, String publisher, Long platformId, String genre) {
        this.title = title;
        this.developer = developer;
        this.publisher = publisher;
        this.platformId = platformId;
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Long getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Long platformId) {
        this.platformId = platformId;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
