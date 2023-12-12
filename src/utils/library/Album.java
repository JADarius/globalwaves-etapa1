package utils.library;

import lombok.Getter;

import java.util.List;

@Getter
public final class Album implements SongCollection {
    private final String name;
    private final List<Song> songs;
    private final String owner;
    private final int releaseYear;
    private final String description;

    public Album(final String name, final List<Song> songs, final String owner, final int releaseYear, final String description) {
        this.name = name;
        this.songs = songs;
        this.owner = owner;
        this.releaseYear = releaseYear;
        this.description = description;
    }

    public int getTotalLikes() {
        int like_count = 0;
        for (Song song : songs) {
            like_count += song.getLikes();
        }
        return like_count;
    }
}
