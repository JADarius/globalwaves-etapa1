package utils;

import fileio.input.SongInput;
import lombok.Getter;

@Getter
public final class Song {
    private final String name;
    private int likes;

    public Song(final SongInput song) {
        this.name = song.getName();
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
