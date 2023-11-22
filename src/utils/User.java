package utils;

import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.output.PlaylistOutput;
import lombok.Getter;

import java.util.ArrayList;

public class User {
    @Getter
    private final String name;
    private final ArrayList<SongInput> likedSongs;
    private final ArrayList<Playlist> followedPlaylists;
    @Getter
    private ArrayList<PodcastInput> searchedPodcasts;
    private final ArrayList<PodcastSave> savedPodcasts;
    @Getter
    private ArrayList<Playlist> searchedPlaylists;
    @Getter
    private ArrayList<SongInput> searchedSongs;
    private final ArrayList<Playlist> playlists;
    private boolean searchedForSongs;
    private boolean searchedForPodcasts;
    private boolean searchedForPlaylists;
    private SongInput selectedSong;
    private PodcastInput selectedPodcast;
    private Playlist selectedPlaylist;
    @Getter
    private Player player = null;

    public User(String name) {
        this.name = name;
        searchedSongs = null;
        searchedPodcasts = null;
        searchedPlaylists = null;
        searchedForSongs = false;
        searchedForPlaylists = false;
        selectedSong = null;
        selectedPodcast = null;
        playlists = new ArrayList<>();
        likedSongs = new ArrayList<>();
        savedPodcasts = new ArrayList<>();
        followedPlaylists = new ArrayList<>();
    }

    public String select(int index) {
        if (searchedForSongs) {
            index--;
            if (index >= searchedSongs.size()) {
                return "The selected ID is too high.";
            } else {
                selectedSong = searchedSongs.get(index);
                selectedPodcast = null;
                selectedPlaylist = null;
                searchedSongs = null;
                searchedForSongs = false;
                return "Successfully selected " + selectedSong.getName() + ".";
            }
        }
        if (searchedForPodcasts) {
            index--;
            if (index >= searchedPodcasts.size()) {
                return "The selected ID is too high.";
            } else {
                selectedPodcast = searchedPodcasts.get(index);
                selectedSong = null;
                selectedPlaylist = null;
                searchedPodcasts = null;
                searchedForPodcasts = false;
                return "Successfully selected " + selectedPodcast.getName() + ".";
            }
        }
        if (searchedForPlaylists) {
            index--;
            if (index >= searchedPlaylists.size()) {
                return "The selected ID is too high.";
            } else {
                selectedPlaylist = searchedPlaylists.get(index);
                selectedPodcast = null;
                selectedSong = null;
                searchedPlaylists = null;
                searchedForPlaylists = false;
                return "Successfully selected " + selectedPlaylist.getName() + ".";
            }
        }
        return "Please conduct a search before making a selection.";
    }

    public String load() {
        if (selectedSong != null) {
            player = new Player(selectedSong);
            selectedSong = null;
            return "Playback loaded successfully.";
        } else if (selectedPodcast != null) {
            for (PodcastSave save : savedPodcasts) {
                if (save.getPodcast().getName().equals(selectedPodcast.getName())) {
                    player = new Player(save);
                    selectedPodcast = null;
                    return "Playback loaded successfully.";
                }
            }
            player = new Player(selectedPodcast);
            selectedPodcast = null;
            return "Playback loaded successfully.";
        } else if (selectedPlaylist != null) {
            player = new Player(selectedPlaylist);
            selectedPlaylist = null;
            return "Playback loaded successfully.";
        }
        return "Please select a source before attempting to load.";
    }

    public void update(int difference) {
        if (player != null) {
            player.update(difference);
        }
    }

    public String playPause() {
        if (player == null) {
            return "Please load a source before attempting to pause or resume playback.";
        }
        return player.playPause();
    }

    public Playlist searchForPlaylist(String name) {
        if (playlists == null) {
            return null;
        }
        for (Playlist playlist : playlists) {
            if (playlist.getName().equals(name)) {
                return playlist;
            }
        }
        return null;
    }

    public Playlist createPlaylist(String name) {
        if (searchForPlaylist(name) != null) {
            // we found a playlist
            return null;
        }
        Playlist playlist = new Playlist(this.name, name);
        playlists.add(playlist);
        return playlist;
    }

    public String addRemoveInPlaylist(int pid) {
        pid--;
        if (pid >= playlists.size()) {
            return "The specified playlist does not exist.";
        }
        if (player == null || player.isFinshed()) {
            return "Please load a source before adding to or removing from the playlist.";
        }
        if (player.getType() == 1) {
            return "The loaded source is not a song.";
        }
        return playlists.get(pid).addRemoveInPlaylist(player.getSong());
    }

    public String like() {
        if (player == null || player.getSong() == null) {
            return "Please load a source before liking or unliking.";
        }
        if (player.getType() == 1) {
            return "Loaded source is not a song.";
        }
        if (likedSongs.contains(player.getSong())) {
            likedSongs.remove(player.getSong());
            return "Unlike registered successfully.";
        } else {
            likedSongs.add(player.getSong());
            return "Like registered successfully.";
        }
    }

    public ArrayList<PlaylistOutput> showPlaylists() {
        ArrayList<PlaylistOutput> output = new ArrayList<>();
        for (Playlist playlist : playlists) {
            output.add(new PlaylistOutput(playlist));
        }
        return output;
    }

    public ArrayList<String> showPreferredSongs() {
        ArrayList<String> output = new ArrayList<>();
        for (SongInput song : likedSongs) {
            output.add(song.getName());
        }
        return output;
    }

    public void stop() {
        if (player != null) {
            if (player.getType() == 1) {
                savedPodcasts.add(new PodcastSave(player.getPodcast(), player.getRemainedTime(), player.getCurrentItem()));
            }
            player.stop();
        }
    }

    public String repeat() {
        if (player == null || player.isFinshed()) {
            return "Please load a source before setting the repeat status.";
        }
        player.repeat();
        return "Repeat mode changed to " + player.getRepeatState();
    }

    public String shuffle(int seed) {
        if (player == null || player.isFinshed()) {
            return "Please load a source before using the shuffle function.";
        }
        if (player.getType() != 2) {
            return "The loaded source is not a playlist.";
        }
        player.shuffle(seed);
        if (player.isShuffle()) {
            return "Shuffle function activated successfully.";
        }
        return "Shuffle function deactivated successfully.";
    }

    public String prev() {
        if (player == null || player.isFinshed()) {
            return "Please load a source before returning to the previous track.";
        }
        return "Returned to previous track successfully. The current track is " + player.prev();
    }

    public String next() {
        if (player == null || player.isFinshed()) {
            return "Please load a source before skipping to the next track.";
        }
        player.next();
        if (player.isFinshed()) {
            return "Please load a source before skipping to the next track.";
        }
        if (player.getType() == 1) {
            return "Skipped to next track successfully. The current track is " + player.getEpisode().getName() + ".";
        } else {
            return "Skipped to next track successfully. The current track is " + player.getSong().getName() + ".";
        }
    }

    public String forward() {
        if (player == null || player.isFinshed()) {
            return "Please load a source before attempting to forward.";
        }
        if (player.getType() != 1) {
            return "The loaded source is not a podcast.";
        }
        player.forward();
        return "Skipped forward successfully.";
    }

    public String backward() {
        if (player == null || player.isFinshed()) {
            return "Please select a source before rewinding.";
        }
        if (player.getType() != 1) {
            return "The loaded source is not a podcast.";
        }
        player.backward();
        return "Rewound successfully.";
    }

    public String switchVisibility(int pid) {
        pid--;
        if (pid >= playlists.size()) {
            return "The specified playlist ID is too high.";
        }
        playlists.get(pid).switchVisibility();
        return "Visibility status updated successfully to " + playlists.get(pid).getVisibility() + ".";
    }

    public String follow() {
        if (selectedPlaylist == null) {
            if (selectedPodcast == null && selectedSong == null) {
                return "Please select a source before following or unfollowing.";
            } else {
                return "The selected source is not a playlist.";
            }
        }
        if (selectedPlaylist.getOwner().equals(name)) {
            return "You cannot follow or unfollow your own playlist.";
        }
        if (followedPlaylists.contains(selectedPlaylist)) {
            followedPlaylists.remove(selectedPlaylist);
            selectedPlaylist.removeFollower();
            return "Playlist unfollowed successfully.";
        } else {
            followedPlaylists.add(selectedPlaylist);
            selectedPlaylist.addFollower();
            return "Playlist followed successfully.";
        }
    }

    public void setSearchedSongs(ArrayList<SongInput> searchedSongs) {
        this.searchedSongs = searchedSongs;
        this.searchedForSongs = true;
        this.searchedForPlaylists = false;
        this.searchedForPodcasts = false;
    }

    public void setSearchedPodcasts(ArrayList<PodcastInput> searchedPodcasts) {
        this.searchedPodcasts = searchedPodcasts;
        this.searchedForPodcasts = true;
        this.searchedForPlaylists = false;
        this.searchedForSongs = false;
    }

    public void setSearchedPlaylists(ArrayList<Playlist> searchedPlaylists) {
        this.searchedPlaylists = searchedPlaylists;
        this.searchedForPlaylists = true;
        this.searchedForSongs = false;
        this.searchedForPodcasts = false;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
