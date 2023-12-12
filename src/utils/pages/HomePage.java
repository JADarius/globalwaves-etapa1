package utils.pages;

import utils.accounts.User;
import utils.library.Playlist;
import utils.library.Song;

import java.util.ArrayList;
import java.util.List;

public class HomePage implements PageObserver {
    private List<String> songs;
    private List<String> playlists;
    private final User owner;
    private static final int MAX_COUNT = 5;

    public HomePage(final User owner) {
        this.owner = owner;
        update();
    }

    public String getOutput() {
        return "Liked songs:\\n\\t"
                + songs
                + "\\n\\nFollowed playlists:\\n\\t"
                + playlists;
    }

    private void setTopSongs() {
        songs = new ArrayList<>();
        List<Song> likedSongs = new ArrayList<>(owner.getLikedSongs());
        likedSongs.sort((o1, o2) -> {
            if (o2.getLikes() != o1.getLikes()) {
                return o1.getLikes() - o2.getLikes();
            }
            return o1.getName().compareTo(o2.getName());
        });
        int count = 0;
        for (Song song : likedSongs) {
            songs.add(song.getName());
            count += 1;
            if (count == MAX_COUNT) {
                break;
            }
        }
    }

    private void setTopPlaylists() {
        playlists = new ArrayList<>();
        List<Playlist> followedPlaylists = new ArrayList<>(owner.getFollowedPlaylists());
        followedPlaylists.sort((o1, o2) -> {
            if (o2.getTotalLikes() != o1.getTotalLikes()) {
                return o1.getTotalLikes() - o2.getTotalLikes();
            }
            return o1.getName().compareTo(o2.getName());
        });
        int count = 0;
        for (Playlist playlist : followedPlaylists) {
            playlists.add(playlist.getName());
            count +=1;
            if (count == MAX_COUNT) {
                break;
            }
        }
    }

    @Override
    public void update() {
        setTopSongs();
        setTopPlaylists();
    }
}
