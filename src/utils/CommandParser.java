package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fileio.input.CommandInput;
import fileio.input.LibraryInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.output.*;

import java.util.ArrayList;
import java.util.List;

public class CommandParser {
    private LibraryInput library;
    private ArrayList<User> users;
    private ArrayList<Playlist> playlists;
    private ObjectMapper mapper;
    private int lastTimestamp;

    public CommandParser(LibraryInput library, ArrayList<User> users, ObjectMapper mapper) {
        this.library = library;
        this.users = users;
        this.mapper = mapper;
        this.playlists = new ArrayList<>();
        this.lastTimestamp = 0;
    }

    public User getUser(String username) {
        for (User user : users) {
            if (user.getName().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public void update(int difference) {
        for (User user : users) {
            user.update(difference);
        }
    }

    public JsonNode parseCommand(CommandInput commandInput) {
        JsonNode output = null;
        String command = commandInput.getCommand();
        update(commandInput.getTimestamp() - lastTimestamp);
        if (command.equals("search")) {
            Searcher searcher = Searcher.getInstance();
            getUser(commandInput.getUsername()).stop();
            if (commandInput.getType().equals("song")) {
                SearchOutput searchOutput = new SearchOutput(commandInput);
                ArrayList<SongInput> list = searcher.searchSong(commandInput.getFilters(), library.getSongs());
                getUser(commandInput.getUsername()).setSearchedSongs(list);
                searchOutput.setMessage("Search returned " + list.size() + " results");
                ArrayList<String> results = new ArrayList<>();
                for (SongInput song : list) {
                    results.add(song.getName());
                }
                searchOutput.setResults(results);
                output = mapper.valueToTree(searchOutput);
            } else if (commandInput.getType().equals("podcast")) {
                SearchOutput searchOutput = new SearchOutput(commandInput);
                ArrayList<PodcastInput> list = searcher.searchPodcast(commandInput.getFilters(), library.getPodcasts());
                getUser(commandInput.getUsername()).setSearchedPodcasts(list);
                searchOutput.setMessage("Search returned " + list.size() + " results");
                ArrayList<String> results = new ArrayList<>();
                for (PodcastInput podcast : list) {
                    results.add(podcast.getName());
                }
                searchOutput.setResults(results);
                output = mapper.valueToTree(searchOutput);
            } else {
                SearchOutput searchOutput = new SearchOutput(commandInput);
                ArrayList<Playlist> list = searcher.searchPlaylist(commandInput.getFilters(), playlists);
                getUser(commandInput.getUsername()).setSearchedPlaylists(list);
                searchOutput.setMessage("Search returned " + list.size() + " results");
                ArrayList<String> results = new ArrayList<>();
                for (Playlist playlist : list) {
                    results.add(playlist.getName());
                }
                searchOutput.setResults(results);
                output = mapper.valueToTree(searchOutput);
            }
        } else if (command.equals("select")) {
            SelectOutput selectOutput = new SelectOutput(commandInput);
            selectOutput.setMessage(getUser(commandInput.getUsername()).select(commandInput.getItemNumber()));
            output = mapper.valueToTree(selectOutput);
        } else if (command.equals("load")) {
            LoadOutput loadOutput = new LoadOutput(commandInput);
            loadOutput.setMessage(getUser(commandInput.getUsername()).load());
            output = mapper.valueToTree(loadOutput);
        } else if (command.equals("playPause")) {
            PlayPauseOutput playPauseOutput = new PlayPauseOutput(commandInput);
            playPauseOutput.setMessage(getUser(commandInput.getUsername()).playPause());
            output = mapper.valueToTree(playPauseOutput);
        } else if (command.equals("status")) {
            Stats stats = getUser(commandInput.getUsername()).getPlayer().status();
            StatsOutput status = new StatsOutput(commandInput, stats);
            output = mapper.valueToTree(status);
        } else if (command.equals("createPlaylist")) {
            LoadOutput createPlaylistOutput = new LoadOutput(commandInput);
            Playlist playlist = getUser(commandInput.getUsername()).createPlaylist(commandInput.getPlaylistName());
            if (playlist == null) {
                createPlaylistOutput.setMessage("A playlist with the same name already exists.");
            } else {
                createPlaylistOutput.setMessage("Playlist created successfully.");
                playlists.add(playlist);
            }
            output = mapper.valueToTree(createPlaylistOutput);
        } else if (command.equals("addRemoveInPlaylist")) {
            LoadOutput addRemoveOutput = new LoadOutput(commandInput);
            addRemoveOutput.setMessage(getUser(commandInput.getUsername()).addRemoveInPlaylist(commandInput.getPlaylistId()));
            output = mapper.valueToTree(addRemoveOutput);
        } else if (command.equals("like")) {
            LoadOutput likeOutput = new LoadOutput(commandInput);
            likeOutput.setMessage(getUser(commandInput.getUsername()).like());
            output = mapper.valueToTree(likeOutput);
        } else if (command.equals("showPlaylists")) {
            SearchPlaylistOutput showPlaylistOutput = new SearchPlaylistOutput(commandInput);
            showPlaylistOutput.setResult(getUser(commandInput.getUsername()).showPlaylists());
            output = mapper.valueToTree(showPlaylistOutput);
        } else if (command.equals("showPreferredSongs")) {
            ShowLikedOutput likedOutput = new ShowLikedOutput(commandInput);
            likedOutput.setResult(getUser(commandInput.getUsername()).showPreferredSongs());
            output = mapper.valueToTree(likedOutput);
        } else if (command.equals("repeat")) {
            LoadOutput repeatOutput = new LoadOutput(commandInput);
            repeatOutput.setMessage(getUser(commandInput.getUsername()).repeat());
            output = mapper.valueToTree(repeatOutput);
        } else if (command.equals("shuffle")) {
            LoadOutput shuffleOutput = new LoadOutput(commandInput);
            shuffleOutput.setMessage(getUser(commandInput.getUsername()).shuffle(commandInput.getSeed()));
            output = mapper.valueToTree(shuffleOutput);
        }
        lastTimestamp = commandInput.getTimestamp();
        return output;
    }
}
