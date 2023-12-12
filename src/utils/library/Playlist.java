package utils.library;

import lombok.Getter;
import lombok.Setter;
import utils.Enums;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public final class Playlist implements SongCollection {
    private String owner;
    private String name;
    private List<Song> songs;
    private Enums.Visibility visibility;
    private int followers;
    private int creationTime;

    public Playlist(final String owner, final String name, final int creationTime) {
        this.owner = owner;
        this.name = name;
        this.songs = new ArrayList<>();
        this.visibility = Enums.Visibility.PUBLIC;
        this.followers = 0;
        this.creationTime = creationTime;
    }

    /**
     * Switches the visibility mode of the playlist
     */
    public void switchVisibility() {
        if (visibility == Enums.Visibility.PUBLIC) {
            visibility = Enums.Visibility.PRIVATE;
        } else {
            visibility = Enums.Visibility.PUBLIC;
        }
    }

    /**
     * Adds a song to the playlist or removes it if already there
     * @param song The song to be added/removed
     * @return A string that specifies what operation took place
     */
    public String addRemoveInPlaylist(final Song song) {
        if (songs.contains(song)) {
            songs.remove(song);
            return "Successfully removed from playlist.";
        } else {
            songs.add(song);
            return "Successfully added to playlist.";
        }
    }

    /**
     * Increases the follower count
     */
    public void addFollower() {
        followers++;
    }

    /**
     * Decreases the follower count
     */
    public void removeFollower() {
        followers--;
    }

    public String getVisibility() {
        return switch (visibility) {
            case PUBLIC -> "public";
            case PRIVATE -> "private";
        };
    }

    public int getTotalLikes() {
        int like_count = 0;
        for (Song song : songs) {
            like_count += song.getLikes();
        }
        return like_count;
    }
}
