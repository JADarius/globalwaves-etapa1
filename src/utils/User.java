package utils;

import fileio.input.UserInput;
import fileio.output.PlaylistOutput;
import lombok.Getter;
import utils.library.Playlist;
import utils.library.Podcast;
import utils.library.Song;

import java.util.ArrayList;

public final class User {
    @Getter
    private final String name;
    private int age;
    private String city;
    private final ArrayList<Song> likedSongs;
    private final ArrayList<Playlist> followedPlaylists;
    @Getter
    private ArrayList<Podcast> searchedPodcasts;
    private final ArrayList<PodcastSave> savedPodcasts;
    @Getter
    private ArrayList<Playlist> searchedPlaylists;
    @Getter
    private ArrayList<Song> searchedSongs;
    private final ArrayList<Playlist> playlists;
    private boolean searchedForSongs;
    private boolean searchedForPodcasts;
    private boolean searchedForPlaylists;
    private Song selectedSong;
    private Podcast selectedPodcast;
    private Playlist selectedPlaylist;
    @Getter
    private Player player = null;

    public User(final UserInput user) {
        this.name = user.getUsername();
        this.city = user.getCity();
        this.age = user.getAge();
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

    /**
     * In case of a faulty "select" command, this is used to unload previous selections
     */
    private void selectFail() {
        selectedPlaylist = null;
        selectedPodcast = null;
        selectedSong = null;
    }

    /**
     * Selects a song/podcast/playlist
     * @param index The index to be use in the search list
     * @return A string giving out a success/failure message
     */
    public String select(int index) {
        if (searchedForSongs) {
            index--;
            if (index >= searchedSongs.size()) {
                selectFail();
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
                selectFail();
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
                selectFail();
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

    /**
     * Loads the selection into the player
     * @return A string giving out a success/failure message
     */
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

    /**
     * Function that updates the player with the time elapsed
     * @param difference The elapsed time from the last command
     */
    public void update(final int difference) {
        if (player != null) {
            player.update(difference);
        }
    }

    /**
     * Wrapper for the player playPause function
     * @return A string giving out a success/failure message
     */
    public String playPause() {
        if (player == null) {
            return "Please load a source before attempting to pause or resume playback.";
        }
        return player.playPause();
    }

    /**
     * Searches in the list of personal playlist for a particular one
     * @param playlistName The name of the playlist
     * @return The playlist
     */
    public Playlist searchForPlaylist(final String playlistName) {
        for (Playlist playlist : playlists) {
            if (playlist.getName().equals(playlistName)) {
                return playlist;
            }
        }
        return null;
    }

    /**
     * Creates a playlist if one with the same name doesn't exist
     * @param playlistName The name of the new playlist
     * @param creationTime The timestamp at which the playlist was created
     * @return The playlist or a null pointer if one with the same name already existed
     */
    public Playlist createPlaylist(final String playlistName, final int creationTime) {
        if (searchForPlaylist(playlistName) != null) {
            // we found a playlist
            return null;
        }
        Playlist playlist = new Playlist(this.name, playlistName, creationTime);
        playlists.add(playlist);
        return playlist;
    }

    /**
     * Adds the currently playing song into a playlist
     * @param pid The pid of the playlist where the song should be added to
     * @return A string giving out a success/failure message
     */
    public String addRemoveInPlaylist(int pid) {
        pid--;
        if (pid >= playlists.size()) {
            return "The specified playlist does not exist.";
        }
        if (player == null || player.isFinished()) {
            return "Please load a source before adding to or removing from the playlist.";
        }
        if (player.getType() == Enums.PlayerType.PODCAST) {
            return "The loaded source is not a song.";
        }
        return playlists.get(pid).addRemoveInPlaylist(player.getSong());
    }

    /**
     * Likes the currently playing song
     * @param songs A list of all the songs from the library to register the like
     * @return A string giving out a success/failure message
     */
    public String like(final ArrayList<Song> songs) {
        if (player == null || player.getSong() == null) {
            return "Please load a source before liking or unliking.";
        }
        if (player.getType() == Enums.PlayerType.PODCAST) {
            return "Loaded source is not a song.";
        }
        if (likedSongs.contains(player.getSong())) {
            likedSongs.remove(player.getSong());
            for (Song song : songs) {
                if (song.getName().equals(player.getSong().getName())) {
                    song.dislike();
                    return "Unlike registered successfully.";
                }
            }
        } else {
            likedSongs.add(player.getSong());
            for (Song song : songs) {
                if (song.getName().equals(player.getSong().getName())) {
                    song.like();
                    return "Like registered successfully.";
                }
            }
        }
        return "";
    }

    /**
     * Shows all the playlists of the user
     * @return A list of all the playlists of the user
     */
    public ArrayList<PlaylistOutput> showPlaylists() {
        ArrayList<PlaylistOutput> output = new ArrayList<>();
        for (Playlist playlist : playlists) {
            output.add(new PlaylistOutput(playlist));
        }
        return output;
    }

    /**
     * Shows a list of all the liked songs
     * @return A list of all the liked songs
     */
    public ArrayList<String> showPreferredSongs() {
        ArrayList<String> output = new ArrayList<>();
        for (Song song : likedSongs) {
            output.add(song.getName());
        }
        return output;
    }

    /**
     * A wrapper for the player's "stop" function that saves the progress of a podcast
     */
    public void stop() {
        if (player != null) {
            if (player.getType() == Enums.PlayerType.PODCAST) {
                savedPodcasts.add(new PodcastSave(player.getPodcast(),
                        player.getRemainedTime(), player.getCurrentItem()));
            }
            player.stop();
        }
    }

    /**
     * A wrapper for the player's "repeat" function
     * @return A string giving out a success/failure message
     */
    public String repeat() {
        if (player == null || player.isFinished()) {
            return "Please load a source before setting the repeat status.";
        }
        player.repeat();
        return "Repeat mode changed to " + player.getRepeatState();
    }

    /**
     * A wrapper for the player's "shuffle" command
     * @param seed The seed for the randomization
     * @return A string giving out a success/failure message
     */
    public String shuffle(final int seed) {
        if (player == null || player.isFinished()) {
            return "Please load a source before using the shuffle function.";
        }
        if (player.getType() != Enums.PlayerType.PLAYLIST) {
            return "The loaded source is not a playlist.";
        }
        player.shuffle(seed);
        if (player.isShuffle()) {
            return "Shuffle function activated successfully.";
        }
        return "Shuffle function deactivated successfully.";
    }

    /**
     * A wrapper for the player's "prev" function
     * @return A string giving out a success/failure message
     */
    public String prev() {
        if (player == null || player.isFinished()) {
            return "Please load a source before returning to the previous track.";
        }
        return "Returned to previous track successfully. The current track is " + player.prev();
    }

    /**
     * A wrapper for the player's "next" command
     * @return A string giving out a success/failure message
     */
    public String next() {
        if (player == null || player.isFinished()) {
            return "Please load a source before skipping to the next track.";
        }
        player.next();
        if (player.isFinished()) {
            return "Please load a source before skipping to the next track.";
        }
        if (player.getType() == Enums.PlayerType.PODCAST) {
            return "Skipped to next track successfully. The current track is "
                    + player.getEpisode().getName() + ".";
        } else {
            return "Skipped to next track successfully. The current track is "
                    + player.getSong().getName() + ".";
        }
    }

    /**
     * A wrapper for the player's "forward" function
     * @return A string giving out a success/failure message
     */
    public String forward() {
        if (player == null || player.isFinished()) {
            return "Please load a source before attempting to forward.";
        }
        if (player.getType() != Enums.PlayerType.PODCAST) {
            return "The loaded source is not a podcast.";
        }
        player.forward();
        return "Skipped forward successfully.";
    }

    /**
     * A wrapper for the player's "backward" function
     * @return A string giving out a success/failure message
     */
    public String backward() {
        if (player == null || player.isFinished()) {
            return "Please select a source before rewinding.";
        }
        if (player.getType() != Enums.PlayerType.PODCAST) {
            return "The loaded source is not a podcast.";
        }
        player.backward();
        return "Rewound successfully.";
    }

    /**
     * A wrapper for the playlist's "switchVisibility" function
     * @param pid The id of the playlist
     * @return A string giving out a success/failure message
     */
    public String switchVisibility(int pid) {
        pid--;
        if (pid >= playlists.size()) {
            return "The specified playlist ID is too high.";
        }
        playlists.get(pid).switchVisibility();
        return "Visibility status updated successfully to "
                + playlists.get(pid).getVisibility() + ".";
    }

    /**
     * Follows the selected playlist
     * @return A string giving out a success/failure message
     */
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
            selectedPlaylist = null;
            return "Playlist unfollowed successfully.";
        } else {
            followedPlaylists.add(selectedPlaylist);
            selectedPlaylist.addFollower();
            selectedPlaylist = null;
            return "Playlist followed successfully.";
        }
    }

    /**
     * Setter for searchedSongs
     * @param searchedSongs The searched songs
     */
    public void setSearchedSongs(final ArrayList<Song> searchedSongs) {
        this.searchedSongs = searchedSongs;
        this.searchedForSongs = true;
        this.searchedForPlaylists = false;
        this.searchedForPodcasts = false;
    }

    /**
     * Setter for searchedPodcasts
     * @param searchedPodcasts The searched podcasts
     */
    public void setSearchedPodcasts(final ArrayList<Podcast> searchedPodcasts) {
        this.searchedPodcasts = searchedPodcasts;
        this.searchedForPodcasts = true;
        this.searchedForPlaylists = false;
        this.searchedForSongs = false;
    }

    /**
     * Setter for searchedPlaylist
     * @param searchedPlaylists The searched playlists
     */
    public void setSearchedPlaylists(final ArrayList<Playlist> searchedPlaylists) {
        this.searchedPlaylists = searchedPlaylists;
        this.searchedForPlaylists = true;
        this.searchedForSongs = false;
        this.searchedForPodcasts = false;
    }
}
