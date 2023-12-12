package utils.library;

import fileio.input.SongInput;
import lombok.Getter;
import utils.Enums;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class Song {
    private final String name;
    private final Integer duration;
    private final String album;
    private final List<String> tags;
    private final String lyrics;
    private final String genre;
    private final Integer releaseYear;
    private final String artist;
    private int likes;
    private final Enums.CreationType creationType;

    public Song(final SongInput song, final Enums.CreationType creationType) {
        this.name = song.getName();
        this.duration = song.getDuration();
        this.album = song.getAlbum();
        this.tags = song.getTags();
        this.lyrics = song.getLyrics();
        this.genre = song.getGenre();
        this.releaseYear = song.getReleaseYear();
        this.artist = song.getArtist();
        this.likes = 0;
        this.creationType = creationType;
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
