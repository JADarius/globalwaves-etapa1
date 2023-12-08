package utils.library;

import fileio.input.SongInput;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public final class Song {
    private final String name;
    private final Integer duration;
    private final String album;
    private final ArrayList<String> tags;
    private final String lyrics;
    private final String genre;
    private final Integer releaseYear;
    private final String artist;
    private int likes;

    public Song(final SongInput song) {
        this.name = song.getName();
        this.duration = song.getDuration();
        this.album = song.getAlbum();
        this.tags = song.getTags();
        this.lyrics = song.getLyrics();
        this.genre = song.getGenre();
        this.releaseYear = song.getReleaseYear();
        this.artist = song.getArtist();
        this.likes = 0;
    }

    /**
     * Increases the likes count
     */
    public void like() {
        likes++;
    }

    /**
     * Decreases the likes count
     */
    public void dislike() {
        likes--;
    }
}
