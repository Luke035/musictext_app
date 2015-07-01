package com.application.misc;

/**
 * Created by lucagrazioli on 18/06/15.
 */
public class Track {
    private String name;
    private String artist;

    public Track(String name, String artist) {
        this.name = name;
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }
}
