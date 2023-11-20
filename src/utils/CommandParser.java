package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fileio.input.CommandInput;
import fileio.input.LibraryInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.output.SearchOutput;
import fileio.output.SelectOutput;

import java.util.ArrayList;
import java.util.List;

public class CommandParser {
    private static CommandParser instance = null;
    private LibraryInput library;
    private ArrayList<User> users;

    private CommandParser(LibraryInput library, ArrayList<User> users) {
        this.library = library;
        this.users = users;
    }

    public static CommandParser getInstance(LibraryInput library, ArrayList<User> users) {
        if (instance == null) {
            instance = new CommandParser(library, users);
        }
        return instance;
    }

    public JsonNode parseCommand(CommandInput commandInput) {
        JsonNode output = null;
        String command = commandInput.getCommand();
        if (command.equals("search")) {
            Searcher searcher = Searcher.getInstance();
            SearchOutput searchOutput = new SearchOutput(commandInput);
            if (commandInput.getType().equals("song")) {
                ArrayList<SongInput> list = searcher.searchSong(commandInput.getFilters(), library.getSongs());
                for (User user : users) {
                    if (user.getName().equals(commandInput.getUsername())) {
                        user.setSearchedSongs(list);
                        break;
                    }
                }
                searchOutput.setMessage("Search returned " + list.size() + " results");
                ArrayList<String> results = new ArrayList<>();
                for (SongInput song : list) {
                    results.add(song.getName());
                }
                searchOutput.setResults(results);
                ObjectMapper mapper = new ObjectMapper();
                output = mapper.valueToTree(searchOutput);
            } else if (commandInput.getType().equals("podcast")) {
                ArrayList<PodcastInput> list = searcher.searchPodcast(commandInput.getFilters(), library.getPodcasts());
                for (User user : users) {
                    if (user.getName().equals(commandInput.getUsername())) {
                        user.setSearchedPodcasts(list);
                        break;
                    }
                }
                searchOutput.setMessage("Search returned " + list.size() + " results");
                ArrayList<String> results = new ArrayList<>();
                for (PodcastInput podcast : list) {
                    results.add(podcast.getName());
                }
                searchOutput.setResults(results);
                ObjectMapper mapper = new ObjectMapper();
                output = mapper.valueToTree(searchOutput);
            }
        } else if (command.equals("select")) {
            for (User user : users) {
                if (user.getName().equals(commandInput.getUsername())) {
                    SelectOutput selectOutput = new SelectOutput(commandInput);
                    selectOutput.setMessage(user.select(commandInput.getItemNumber()));
                    ObjectMapper mapper = new ObjectMapper();
                    output = mapper.valueToTree(selectOutput);
                }
            }
        }
        return output;
    }
}
