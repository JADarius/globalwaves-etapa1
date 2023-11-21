package utils;

import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.output.SelectOutput;

import java.util.ArrayList;

public class User {
    private String name;
    private ArrayList<SongInput> searchedSongs;
    private ArrayList<PodcastInput> searchedPodcasts;
    private boolean searchedForSongs;
    private boolean searchedForPodcasts;
    private SongInput selectedSong;
    private PodcastInput selectedPodcast;
    private Player player = null;

    public User(String name) {
        this.name = name;
        searchedSongs = null;
        searchedPodcasts = null;
        searchedForSongs = false;
        searchedForSongs = false;
        selectedSong = null;
        selectedPodcast = null;
    }

    public ArrayList<SongInput> getSearchedSongs() {
        return searchedSongs;
    }

    public void setSearchedSongs(ArrayList<SongInput> searchedSongs) {
        this.searchedSongs = searchedSongs;
        this.searchedForSongs = true;
        this.searchedForPodcasts = false;
    }

    public ArrayList<PodcastInput> getSearchedPodcasts() {
        return searchedPodcasts;
    }

    public void setSearchedPodcasts(ArrayList<PodcastInput> searchedPodcasts) {
        this.searchedPodcasts = searchedPodcasts;
        this.searchedForPodcasts = true;
        this.searchedForSongs = false;
    }

    public String getName() {
        return name;
    }

    public String select(int index) {
        if (searchedForSongs) {
            index--;
            if (index >= searchedSongs.size()) {
                return "The selected ID is too high.";
            } else {
                selectedSong = searchedSongs.get(index);
                selectedPodcast = null;
                return "Successfully selected " + searchedSongs.get(index).getName() + ".";
            }
        }
        if (searchedForPodcasts) {
            index--;
            if (index >= searchedPodcasts.size()) {
                return "The selected ID is too high.";
            } else {
                selectedPodcast = searchedPodcasts.get(index);
                selectedSong = null;
                return "Successfully selected " + searchedPodcasts.get(index).getName() + ".";
            }
        }
        return "Please conduct a search before making a selection.";
    }

    public String load() {
        if (selectedSong != null) {
            player = new Player(selectedSong);
            return "Playback loaded successfully.";
        } else if (selectedPodcast != null) {
            player = new Player(selectedPodcast);
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

    public Player getPlayer() {
        return player;
    }
}
