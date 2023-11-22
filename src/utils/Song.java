package utils;

import fileio.input.SongInput;
import lombok.Getter;

@Getter
public class Song {
    private final String name;
    private int likes;

    public Song(SongInput song) {
        this.name = song.getName();
        this.likes = 0;
    }

    public void like() {
        likes++;
    }

    public void dislike() {
        likes--;
    }
}
