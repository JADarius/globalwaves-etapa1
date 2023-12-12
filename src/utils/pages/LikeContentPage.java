package utils.pages;

import utils.accounts.User;
import utils.library.Playlist;
import utils.library.Song;

import java.util.ArrayList;
import java.util.List;

public class LikeContentPage implements PageObserver {
    private final User owner;
    private List<String> songs;
    private List<String> playlists;

    public LikeContentPage(User owner) {
        this.owner = owner;
        update();
    }

    public String getOutput() {
        return "Liked Songs:\\n\\t"
                + songs
                + "\\n\\nFollowed Playlists:\\n\\t"
                + playlists;
    }

    private void setSongs() {
        songs = new ArrayList<>();
        for (Song song : owner.getLikedSongs()) {
            songs.add(song.getName() + " - " + song.getArtist());
        }
    }

    private void setPlaylists() {
        playlists = new ArrayList<>();
        for (Playlist playlist : owner.getFollowedPlaylists()) {
            playlists.add(playlist.getName() + " - " + playlist.getOwner());
        }
    }

    @Override
    public void update() {
        setSongs();
        setPlaylists();
    }
}
