package utils.accounts;

import fileio.input.CommandInput;
import fileio.input.UserInput;
import fileio.output.PlaylistOutput;
import lombok.Getter;
import utils.Enums;
import utils.Player;
import utils.PodcastSave;
import utils.library.Album;
import utils.library.Playlist;
import utils.library.Podcast;
import utils.library.Song;
import utils.pages.HomePage;
import utils.pages.LikeContentPage;
import utils.pages.PageObserver;

import java.util.ArrayList;
import java.util.List;

public class User implements GenericUser {
    @Getter
    private String name;
    private int age;
    private String city;
    @Getter
    private final List<Song> likedSongs;
    @Getter
    private final List<Playlist> followedPlaylists;
    @Getter
    private List<Podcast> searchedPodcasts;
    private final List<PodcastSave> savedPodcasts;
    @Getter
    private List<Playlist> searchedPlaylists;
    @Getter
    private List<Song> searchedSongs;
    private final List<Playlist> playlists;
    private final List<PageObserver> searchedPages;
    private Song selectedSong;
    private Podcast selectedPodcast;
    private Playlist selectedPlaylist;
    @Getter
    private Player player = null;
    private Enums.ConnectionStatus connectionStatus;
    private final List<PageObserver> observers;
    private PageObserver currentPage;
    private Enums.MediaType searchType;
    private Enums.MediaType selectType;

    public User() {
        playlists = new ArrayList<>();
        likedSongs = new ArrayList<>();
        savedPodcasts = new ArrayList<>();
        followedPlaylists = new ArrayList<>();
        searchedPages = new ArrayList<>();
        connectionStatus = Enums.ConnectionStatus.ONLINE;
        currentPage = new HomePage(this);
        observers = new ArrayList<>();
        observers.add(currentPage);
        observers.add(new LikeContentPage(this));
        searchType = Enums.MediaType.NONE;
        selectType = Enums.MediaType.NONE;
    }

    public User(final UserInput user) {
        this();
        name = user.getUsername();
        city = user.getCity();
        age = user.getAge();
    }

    public User(final CommandInput commandInput) {
        this();
        name = commandInput.getUsername();
        city = commandInput.getCity();
        age = commandInput.getAge();
    }

    /**
     * In case of a faulty "select" command, this is used to unload previous selections
     */
    private void selectFail() {
        selectType = Enums.MediaType.NONE;
        searchType = Enums.MediaType.NONE;
    }

    /**
     * Selects a song/podcast/playlist
     * @param index The index to be use in the search list
     * @return A string giving out a success/failure message
     */
    public String select(int index) {
        index--;
        String output = "Please conduct a search before making a selection.";
        switch (searchType) {
            case SONG -> {
                if (index >= searchedSongs.size()) {
                    selectFail();
                    return "The selected ID is too high.";
                } else {
                    selectedSong = searchedSongs.get(index);
                    output =  "Successfully selected " + selectedSong.getName() + ".";
                    searchType = Enums.MediaType.NONE;
                    selectType = Enums.MediaType.SONG;
                }
            }
            case PODCAST -> {
                if (index >= searchedPodcasts.size()) {
                    selectFail();
                    return "The selected ID is too high.";
                } else {
                    selectedPodcast = searchedPodcasts.get(index);
                    output =  "Successfully selected " + selectedPodcast.getName() + ".";
                    searchType = Enums.MediaType.NONE;
                    selectType = Enums.MediaType.PODCAST;
                }
            }
            case PLAYLIST -> {
                if (index >= searchedPlaylists.size()) {
                    selectFail();
                    return "The selected ID is too high.";
                } else {
                    selectedPlaylist = searchedPlaylists.get(index);
                    output =  "Successfully selected " + selectedPlaylist.getName() + ".";
                    searchType = Enums.MediaType.NONE;
                    selectType = Enums.MediaType.PLAYLIST;
                }
            }
        }
        return output;
    }

    /**
     * Loads the selection into the player
     * @return A string giving out a success/failure message
     */
    public String load() {
        if (selectType == Enums.MediaType.SONG) {
            player = new Player(selectedSong);
            selectType = Enums.MediaType.NONE;
            return "Playback loaded successfully.";
        } else if (selectType == Enums.MediaType.PODCAST) {
            for (PodcastSave save : savedPodcasts) {
                if (save.getPodcast().getName().equals(selectedPodcast.getName())) {
                    player = new Player(save);
                    selectType = Enums.MediaType.NONE;
                    return "Playback loaded successfully.";
                }
            }
            player = new Player(selectedPodcast);
            selectType = Enums.MediaType.NONE;
            return "Playback loaded successfully.";
        } else if (selectType == Enums.MediaType.PLAYLIST) {
            player = new Player(selectedPlaylist);
            selectType = Enums.MediaType.NONE;
            return "Playback loaded successfully.";
        }
        return "Please select a source before attempting to load.";
    }

    /**
     * Function that updates the player with the time elapsed
     * @param difference The elapsed time from the last command
     */
    public void update(final int difference) {
        if (player != null && connectionStatus == Enums.ConnectionStatus.ONLINE) {
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

    public void notifyObservers() {
        for (PageObserver observer : observers) {
            observer.update();
        }
    }

    /**
     * Likes the currently playing song
     * @param songs A list of all the songs from the library to register the like
     * @return A string giving out a success/failure message
     */
    public String like(final List<Song> songs) {
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
                    notifyObservers();
                    return "Unlike registered successfully.";
                }
            }
        } else {
            likedSongs.add(player.getSong());
            for (Song song : songs) {
                if (song.getName().equals(player.getSong().getName())) {
                    song.like();
                    notifyObservers();
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
    public List<PlaylistOutput> showPlaylists() {
        List<PlaylistOutput> output = new ArrayList<>();
        for (Playlist playlist : playlists) {
            output.add(new PlaylistOutput(playlist));
        }
        return output;
    }

    /**
     * Shows a list of all the liked songs
     * @return A list of all the liked songs
     */
    public List<String> showPreferredSongs() {
        List<String> output = new ArrayList<>();
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
        if (selectType != Enums.MediaType.PLAYLIST) {
            if (selectType == Enums.MediaType.NONE) {
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
            selectType = Enums.MediaType.NONE;
            return "Playlist unfollowed successfully.";
        } else {
            followedPlaylists.add(selectedPlaylist);
            selectedPlaylist.addFollower();
            selectType = Enums.MediaType.NONE;
            return "Playlist followed successfully.";
        }
    }

    /**
     * Setter for searchedSongs
     * @param searchedSongs The searched songs
     */
    public void setSearchedSongs(final List<Song> searchedSongs) {
        this.searchedSongs = searchedSongs;
        this.searchType = Enums.MediaType.SONG;
    }

    /**
     * Setter for searchedPodcasts
     * @param searchedPodcasts The searched podcasts
     */
    public void setSearchedPodcasts(final List<Podcast> searchedPodcasts) {
        this.searchedPodcasts = searchedPodcasts;
        this.searchType = Enums.MediaType.PODCAST;
    }

    /**
     * Setter for searchedPlaylist
     * @param searchedPlaylists The searched playlists
     */
    public void setSearchedPlaylists(final List<Playlist> searchedPlaylists) {
        this.searchedPlaylists = searchedPlaylists;
        this.searchType = Enums.MediaType.PLAYLIST;
    }

    public boolean isOnline() {
        return switch (connectionStatus) {
            case ONLINE -> true;
            case OFFLINE -> false;
        };
    }

    public boolean checkForAlbum(final Album album) {
        if (player == null) {
            return false;
        }
        switch (player.getType()) {
            case PODCAST -> {
                return false;
            }
            case SONG -> {
                return album.getSongs().contains(player.getSong());
            }
            case PLAYLIST -> {
                for (Song song : player.getPlaylist().getSongs()) {
                    if (album.getSongs().contains(song)) {
                        return true;
                    }
                }
            }
        };
        return false;
    }

    public boolean checkForPodcast(final Podcast podcast) {
        if (player == null) {
            return false;
        }
        if (player.getType() == Enums.PlayerType.PODCAST) {
            return player.getPodcast().equals(podcast);
        }
        return false;
    }

    public void changePage(final PageObserver page) {
        currentPage = page;
    }

    public String printCurrentPage() {
        return currentPage.getOutput();
    }

    @Override
    public String addAlbum(CommandInput commandInput) {
        return name + " is not an artist.";
    }

    @Override
    public String removeAlbum(CommandInput commandInput) {
        return name + " is not an artist.";
    }

    @Override
    public String addEvent(CommandInput commandInput) {
        return name + " is not an artist.";
    }

    @Override
    public String removeEvent(CommandInput commandInput) {
        return name + " is not an artist.";
    }

    @Override
    public String addMerch(CommandInput commandInput) {
        return name + " is not an artist.";
    }

    @Override
    public String addPocast(CommandInput commandInput) {
        return name + " is not a host.";
    }

    @Override
    public String removePodcast(CommandInput commandInput) {
        return name + " is not a host.";
    }

    @Override
    public String addAnnouncement(CommandInput commandInput) {
        return name + " is not a host.";
    }

    @Override
    public String removeAnnouncement(CommandInput commandInput) {
        return name + " is not a host.";
    }

    @Override
    public String switchConnectionStatus() {
        connectionStatus = switch (connectionStatus) {
            case ONLINE -> Enums.ConnectionStatus.OFFLINE;
            case OFFLINE -> Enums.ConnectionStatus.ONLINE;
        };
        return name + " has changed status successfully.";
    }
}
