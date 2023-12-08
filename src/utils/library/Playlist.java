package utils.library;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public final class Playlist {
    private String owner;
    private String name;
    private ArrayList<Song> songs;
    private String visibility;
    private int followers;
    private int creationTime;

    public Playlist(final String owner, final String name, final int creationTime) {
        this.owner = owner;
        this.name = name;
        this.songs = new ArrayList<>();
        this.visibility = "public";
        this.followers = 0;
        this.creationTime = creationTime;
    }

    /**
     * Switches the visibility mode of the playlist
     */
    public void switchVisibility() {
        if (visibility.equals("public")) {
            visibility = "private";
        } else {
            visibility = "public";
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
}
