package com.christopherbare.midterm;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;

public class App implements Serializable {
    String name, releaseDate, artistName, copyright, artworkUrl100;
    ArrayList<String> genres = new ArrayList<>();

    public App() {
        this.name = "";
        this.releaseDate = "";
        this.artistName = "";
        this.copyright = "";
        this.artworkUrl100 = null;
        this.genres.add("");
    }

    public App(String name, String releaseDate, String genre, String artistName, String copyright, String artworkUrl100) {
        this.name = name;
        this.releaseDate = releaseDate;
        this.artistName = artistName;
        this.copyright = copyright;
        this.artworkUrl100 = artworkUrl100;
        this.addGenre(genre);
    }

    public void addGenre(String genre) {
        genres.add(genre);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getGenres() {
        if (genres.get(0) == "") {
            genres.remove(0);
            return genres.toString();
        } else {
            return genres.toString();
        }

    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getArtworkUrl100() {
        return artworkUrl100;
    }

    public void setArtworkUrl100(String artworkUrl100) {
        this.artworkUrl100 = artworkUrl100;
    }
}
