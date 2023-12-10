package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fileio.input.CommandInput;
import fileio.input.LibraryInput;
import fileio.output.*;
import utils.library.Library;
import utils.library.Playlist;
import utils.library.Podcast;
import utils.library.Song;

import java.util.ArrayList;
import java.util.Comparator;

public final class CommandParser {
    private static final ObjectMapper mapper = new ObjectMapper();

    private CommandParser() {
    }

    /**
     * Parses the command and gives the corresponding output to be written in the JSON
     * @param commandInput The command
     * @return The output to be written in the JSON
     */
    public static JsonNode parseCommand(final CommandInput commandInput) {
        String command = commandInput.getCommand();
        Admin.update(commandInput.getTimestamp());
        return switch (command) {
            case "search" -> searchLibrary(commandInput);
            case "select" -> selectItem(commandInput);
            case "load" -> loadItem(commandInput);
            case "playPause" -> playPauseItem(commandInput);
            case "status" -> getStatus(commandInput);
            case "createPlaylist" -> createPlaylist(commandInput);
            case "addRemoveInPlaylist" -> addRemoveInPlaylist(commandInput);
            case "like" -> like(commandInput);
            case "showPlaylists" -> showPlaylists(commandInput);
            case "showPreferredSongs" -> showPreferredSongs(commandInput);
            case "repeat" -> repeatItem(commandInput);
            case "shuffle" -> shuffleItem(commandInput);
            case "prev" -> prevItem(commandInput);
            case "forward" -> forward(commandInput);
            case "backward" -> backward(commandInput);
            case "next" -> nextItem(commandInput);
            case "switchVisibility" -> switchVisibility(commandInput);
            case "follow" -> followPlaylist(commandInput);
            case "getTop5Playlists" -> getTop5Playlists(commandInput);
            case "getTop5Songs" -> getTop5Songs(commandInput);
            case "switchConnectionStatus" -> switchConnectionStatus(commandInput);
//            case "getOnlineUsers" -> getOnlineUsers(commandInput);
            default -> null;
        };
    }

    private static JsonNode searchLibrary(final CommandInput commandInput) {
        User currentUser = Admin.getUser(commandInput.getUsername());
        currentUser.stop();
        if (currentUser != null && !currentUser.isOnline()) {
            SearchOutput output = new SearchOutput(commandInput);
            output.setMessage(currentUser.getName() + " is offline.");
            return mapper.valueToTree(output);
        }
        if (commandInput.getType().equals("song")) {
            SearchOutput searchOutput = new SearchOutput(commandInput);
            ArrayList<Song> list = Searcher.searchSong(commandInput.getFilters(),
                    Admin.getSongs());
            currentUser.setSearchedSongs(list);
            searchOutput.setMessage("Search returned " + list.size() + " results");
            ArrayList<String> results = new ArrayList<>();
            for (Song song : list) {
                results.add(song.getName());
            }
            searchOutput.setResults(results);
            return mapper.valueToTree(searchOutput);
        } else if (commandInput.getType().equals("podcast")) {
            SearchOutput searchOutput = new SearchOutput(commandInput);
            ArrayList<Podcast> list = Searcher.searchPodcast(commandInput.getFilters(),
                    Admin.getPodcasts());
            currentUser.setSearchedPodcasts(list);
            searchOutput.setMessage("Search returned " + list.size() + " results");
            ArrayList<String> results = new ArrayList<>();
            for (Podcast podcast : list) {
                results.add(podcast.getName());
            }
            searchOutput.setResults(results);
            return mapper.valueToTree(searchOutput);
        } else {
            SearchOutput searchOutput = new SearchOutput(commandInput);
            ArrayList<Playlist> list = Searcher.searchPlaylist(commandInput.getFilters(),
                    Admin.getPlaylists(), commandInput.getUsername());
            currentUser.setSearchedPlaylists(list);
            searchOutput.setMessage("Search returned " + list.size() + " results");
            ArrayList<String> results = new ArrayList<>();
            for (Playlist playlist : list) {
                results.add(playlist.getName());
            }
            searchOutput.setResults(results);
            return mapper.valueToTree(searchOutput);
        }
    }

    private static JsonNode selectItem(final CommandInput commandInput) {
        User currentUser = Admin.getUser(commandInput.getUsername());
        MessageOutput selectOutput = new MessageOutput(commandInput);
        selectOutput.setMessage(currentUser.select(commandInput.getItemNumber()));
        return mapper.valueToTree(selectOutput);
    }

    private static JsonNode loadItem(final CommandInput commandInput) {
        User currentUser = Admin.getUser(commandInput.getUsername());
        MessageOutput loadOutput = new MessageOutput(commandInput);
        loadOutput.setMessage(currentUser.load());
        return mapper.valueToTree(loadOutput);
    }

    private static JsonNode playPauseItem(final CommandInput commandInput) {
        User currentUser = Admin.getUser(commandInput.getUsername());
        MessageOutput playPauseOutput = new MessageOutput(commandInput);
        playPauseOutput.setMessage(currentUser.playPause());
        return mapper.valueToTree(playPauseOutput);
    }

    private static JsonNode getStatus(final CommandInput commandInput) {
        User currentUser = Admin.getUser(commandInput.getUsername());
        Stats stats = currentUser.getPlayer().status();
        StatsOutput status = new StatsOutput(commandInput, stats);
        return mapper.valueToTree(status);
    }

    private static JsonNode createPlaylist(final CommandInput commandInput) {
        User currentUser = Admin.getUser(commandInput.getUsername());
        MessageOutput createOutput = new MessageOutput(commandInput);
        Playlist playlist = currentUser.createPlaylist(commandInput.getPlaylistName(),
                commandInput.getTimestamp());
        if (playlist == null) {
            createOutput.setMessage("A playlist with the same name already exists.");
        } else {
            createOutput.setMessage("Playlist created successfully.");
            Admin.addPlaylist(playlist);
        }
        return mapper.valueToTree(createOutput);
    }

    private static JsonNode addRemoveInPlaylist(final CommandInput commandInput) {
        User currentUser = Admin.getUser(commandInput.getUsername());
        MessageOutput addOutput = new MessageOutput(commandInput);
        addOutput.setMessage(currentUser.addRemoveInPlaylist(commandInput.getPlaylistId()));
        return mapper.valueToTree(addOutput);
    }

    private static JsonNode like(final CommandInput commandInput) {
        User currentUser = Admin.getUser(commandInput.getUsername());
        MessageOutput likeOutput = new MessageOutput(commandInput);
        likeOutput.setMessage(currentUser.like(Admin.getSongs()));
        return mapper.valueToTree(likeOutput);
    }

    private static JsonNode showPlaylists(final CommandInput commandInput) {
        User currentUser = Admin.getUser(commandInput.getUsername());
        SearchPlaylistOutput showPlaylistOutput = new SearchPlaylistOutput(commandInput);
        showPlaylistOutput.setResult(currentUser.showPlaylists());
        return mapper.valueToTree(showPlaylistOutput);
    }

    private static JsonNode showPreferredSongs(final CommandInput commandInput) {
        User currentUser = Admin.getUser(commandInput.getUsername());
        ShowLikedOutput likedOutput = new ShowLikedOutput(commandInput);
        likedOutput.setResult(currentUser.showPreferredSongs());
        return mapper.valueToTree(likedOutput);
    }

    private static JsonNode repeatItem(final CommandInput commandInput) {
        User currentUser = Admin.getUser(commandInput.getUsername());
        MessageOutput repeatOutput = new MessageOutput(commandInput);
        repeatOutput.setMessage(currentUser.repeat());
        return mapper.valueToTree(repeatOutput);
    }

    private static JsonNode shuffleItem(final CommandInput commandInput) {
        User currentUser = Admin.getUser(commandInput.getUsername());
        MessageOutput shuffleOutput = new MessageOutput(commandInput);
        shuffleOutput.setMessage(currentUser.shuffle(commandInput.getSeed()));
        return mapper.valueToTree(shuffleOutput);
    }

    private static JsonNode prevItem(final CommandInput commandInput) {
        User currentUser = Admin.getUser(commandInput.getUsername());
        MessageOutput prevOutput = new MessageOutput(commandInput);
        prevOutput.setMessage(currentUser.prev());
        return mapper.valueToTree(prevOutput);
    }

    private static JsonNode forward(final CommandInput commandInput) {
        User currentUser = Admin.getUser(commandInput.getUsername());
        MessageOutput forwardOutput = new MessageOutput(commandInput);
        forwardOutput.setMessage(currentUser.forward());
        return mapper.valueToTree(forwardOutput);
    }

    private static JsonNode backward(final CommandInput commandInput) {
        User currentUser = Admin.getUser(commandInput.getUsername());
        MessageOutput backwardOutput = new MessageOutput(commandInput);
        backwardOutput.setMessage(currentUser.backward());
        return mapper.valueToTree(backwardOutput);
    }

    private static JsonNode nextItem(final CommandInput commandInput) {
        User currentUser = Admin.getUser(commandInput.getUsername());
        MessageOutput nextOutput = new MessageOutput(commandInput);
        nextOutput.setMessage(currentUser.next());
        return mapper.valueToTree(nextOutput);
    }

    private static JsonNode switchVisibility(final CommandInput commandInput) {
        User currentUser = Admin.getUser(commandInput.getUsername());
        MessageOutput visiOutput = new MessageOutput(commandInput);
        visiOutput.setMessage(currentUser.switchVisibility(commandInput.getPlaylistId()));
        return mapper.valueToTree(visiOutput);
    }

    private static JsonNode followPlaylist(final CommandInput commandInput) {
        User currentUser = Admin.getUser(commandInput.getUsername());
        MessageOutput followOutput = new MessageOutput(commandInput);
        followOutput.setMessage(currentUser.follow());
        return mapper.valueToTree(followOutput);
    }

    private static JsonNode getTop5Playlists(final CommandInput commandInput) {
        TopOutput topFiveOutput = new TopOutput(commandInput);
        topFiveOutput.setResult(Admin.getTop5Playlists());
        return mapper.valueToTree(topFiveOutput);
    }

    private static JsonNode getTop5Songs(final CommandInput commandInput) {
        TopOutput topFiveOutput = new TopOutput(commandInput);
        topFiveOutput.setResult(Admin.getTop5Songs());
        return mapper.valueToTree(topFiveOutput);
    }

    private static JsonNode switchConnectionStatus(final CommandInput commandInput) {
        GenericUser currentUser = Admin.getUserFromAll(commandInput.getName());
        MessageOutput output = new MessageOutput(commandInput);
        if (currentUser == null) {
            output.setMessage("The username " + commandInput.getUsername() + " doesn't exist.");
            return mapper.valueToTree(output);
        }
        output.setMessage(currentUser.switchConnectionStatus());
        return mapper.valueToTree(output);
    }

//    private static JsonNode getOnlineUsers(final CommandInput commandInput) {
//        AnonymousOutput output = new AnonymousOutput(commandInput);
//        ArrayList<String> onlineList = output.getResult();
//        for (User user: library.getUsers()) {
//            if (user.isOnline()) {
//                onlineList.add(user.getName());
//            }
//        }
//        return mapper.valueToTree(output);
//    }
}
