package fileio.input;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public final class FilterInput {
    private String name;
    private String album;
    private ArrayList<String> tags;
    private String lyrics;
    private String genre;
    private String releaseYear;
    private String artist;
    private String owner;

    public FilterInput() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
