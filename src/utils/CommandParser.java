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
    private ObjectMapper mapper;
    private int lastTimestamp;

    public CommandParser(LibraryInput library, ArrayList<User> users, ObjectMapper mapper) {
        this.library = library;
        this.users = users;
        this.mapper = mapper;
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
            SearchOutput searchOutput = new SearchOutput(commandInput);
            if (commandInput.getType().equals("song")) {
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
                ArrayList<PodcastInput> list = searcher.searchPodcast(commandInput.getFilters(), library.getPodcasts());
                getUser(commandInput.getUsername()).setSearchedPodcasts(list);
                searchOutput.setMessage("Search returned " + list.size() + " results");
                ArrayList<String> results = new ArrayList<>();
                for (PodcastInput podcast : list) {
                    results.add(podcast.getName());
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
        }
        lastTimestamp = commandInput.getTimestamp();
        return output;
    }
}
