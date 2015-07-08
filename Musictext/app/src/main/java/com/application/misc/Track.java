package com.application.misc;

/**
 * Created by lucagrazioli on 18/06/15.
 */
public class Track {
    private String name;
    private String artist;
    private double rank;

    public Track(String name, String artist, double rank) {
        this.name = name;
        this.artist = artist;
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public double getRank() {
        return rank;
    }
}
