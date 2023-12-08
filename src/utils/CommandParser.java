package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fileio.input.CommandInput;
import fileio.input.LibraryInput;
import fileio.output.MessageOutput;
import fileio.output.SearchOutput;
import fileio.output.SearchPlaylistOutput;
import fileio.output.ShowLikedOutput;
import fileio.output.StatsOutput;
import fileio.output.TopOutput;
import utils.library.Library;
import utils.library.Playlist;
import utils.library.Podcast;
import utils.library.Song;

import java.util.ArrayList;
import java.util.Comparator;

public final class CommandParser {
    private static final int MAX_COUNT = 5;
    private final Library library;
    private final ArrayList<Playlist> playlists;
    private final ArrayList<Song> songs;
    private final ObjectMapper mapper;
    private int lastTimestamp;
    private static User currentUser = null;

    public CommandParser(final LibraryInput library,
                         final ObjectMapper mapper,
                         final ArrayList<Song> songs) {
        this.library = new Library(library);
        this.mapper = mapper;
        this.songs = songs;
        this.playlists = new ArrayList<>();
        this.lastTimestamp = 0;
    }

    /**
     * Gets the user from the command
     * @param commandInput The command
     * @return The user from the command
     */
    private User getUser(final CommandInput commandInput) {
        for (User user : library.getUsers()) {
            if (user.getName().equals(commandInput.getUsername())) {
                return user;
            }
        }
        return null;
    }

    /**
     * Updates all the players with the time elapsed
     * @param difference The time elapsed
     */
    private void update(final int difference) {
        for (User user : library.getUsers()) {
            user.update(difference);
        }
    }

    /**
     * Parses the command and gives the corresponding output to be written in the JSON
     * @param commandInput The command
     * @return The output to be written in the JSON
     */
    public JsonNode parseCommand(final CommandInput commandInput) {
        String command = commandInput.getCommand();
        update(commandInput.getTimestamp() - lastTimestamp);
        lastTimestamp = commandInput.getTimestamp();
        currentUser = getUser(commandInput);
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
            default -> null;
        };
    }

    private JsonNode searchLibrary(final CommandInput commandInput) {
        Searcher searcher = new Searcher();
        currentUser.stop();
        if (commandInput.getType().equals("song")) {
            SearchOutput searchOutput = new SearchOutput(commandInput);
            ArrayList<Song> list = searcher.searchSong(commandInput.getFilters(),
                    library.getSongs());
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
            ArrayList<Podcast> list = searcher.searchPodcast(commandInput.getFilters(),
                    library.getPodcasts());
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
            ArrayList<Playlist> list = searcher.searchPlaylist(commandInput.getFilters(),
                    playlists, commandInput.getUsername());
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

    private JsonNode selectItem(final CommandInput commandInput) {
            MessageOutput selectOutput = new MessageOutput(commandInput);
            selectOutput.setMessage(currentUser.select(commandInput.getItemNumber()));
            return mapper.valueToTree(selectOutput);
    }

    private JsonNode loadItem(final CommandInput commandInput) {
        MessageOutput loadOutput = new MessageOutput(commandInput);
        loadOutput.setMessage(currentUser.load());
        return mapper.valueToTree(loadOutput);
    }

    private JsonNode playPauseItem(final CommandInput commandInput) {
        MessageOutput playPauseOutput = new MessageOutput(commandInput);
        playPauseOutput.setMessage(currentUser.playPause());
        return mapper.valueToTree(playPauseOutput);
    }

    private JsonNode getStatus(final CommandInput commandInput) {
        Stats stats = currentUser.getPlayer().status();
        StatsOutput status = new StatsOutput(commandInput, stats);
        return mapper.valueToTree(status);
    }

    private JsonNode createPlaylist(final CommandInput commandInput) {
        MessageOutput createOutput = new MessageOutput(commandInput);
        Playlist playlist = currentUser.createPlaylist(commandInput.getPlaylistName(),
                commandInput.getTimestamp());
        if (playlist == null) {
            createOutput.setMessage("A playlist with the same name already exists.");
        } else {
            createOutput.setMessage("Playlist created successfully.");
            playlists.add(playlist);
        }
        return mapper.valueToTree(createOutput);
    }

    private JsonNode addRemoveInPlaylist(final CommandInput commandInput) {
        MessageOutput addOutput = new MessageOutput(commandInput);
        addOutput.setMessage(currentUser.addRemoveInPlaylist(commandInput.getPlaylistId()));
        return mapper.valueToTree(addOutput);
    }

    private JsonNode like(final CommandInput commandInput) {
        MessageOutput likeOutput = new MessageOutput(commandInput);
        likeOutput.setMessage(currentUser.like(songs));
        return mapper.valueToTree(likeOutput);
    }

    private JsonNode showPlaylists(final CommandInput commandInput) {
        SearchPlaylistOutput showPlaylistOutput = new SearchPlaylistOutput(commandInput);
        showPlaylistOutput.setResult(currentUser.showPlaylists());
        return mapper.valueToTree(showPlaylistOutput);
    }

    private JsonNode showPreferredSongs(final CommandInput commandInput) {
        ShowLikedOutput likedOutput = new ShowLikedOutput(commandInput);
        likedOutput.setResult(currentUser.showPreferredSongs());
        return mapper.valueToTree(likedOutput);
    }

    private JsonNode repeatItem(final CommandInput commandInput) {
        MessageOutput repeatOutput = new MessageOutput(commandInput);
        repeatOutput.setMessage(currentUser.repeat());
        return mapper.valueToTree(repeatOutput);
    }

    private JsonNode shuffleItem(final CommandInput commandInput) {
        MessageOutput shuffleOutput = new MessageOutput(commandInput);
        shuffleOutput.setMessage(currentUser.shuffle(commandInput.getSeed()));
        return mapper.valueToTree(shuffleOutput);
    }

    private JsonNode prevItem(final CommandInput commandInput) {
        MessageOutput prevOutput = new MessageOutput(commandInput);
        prevOutput.setMessage(currentUser.prev());
        return mapper.valueToTree(prevOutput);
    }

    private JsonNode forward(final CommandInput commandInput) {
        MessageOutput forwardOutput = new MessageOutput(commandInput);
        forwardOutput.setMessage(currentUser.forward());
        return mapper.valueToTree(forwardOutput);
    }

    private JsonNode backward(final CommandInput commandInput) {
        MessageOutput backwardOutput = new MessageOutput(commandInput);
        backwardOutput.setMessage(currentUser.backward());
        return mapper.valueToTree(backwardOutput);
    }

    private JsonNode nextItem(final CommandInput commandInput) {
        MessageOutput nextOutput = new MessageOutput(commandInput);
        nextOutput.setMessage(currentUser.next());
        return mapper.valueToTree(nextOutput);
    }

    private JsonNode switchVisibility(final CommandInput commandInput) {
        MessageOutput visiOutput = new MessageOutput(commandInput);
        visiOutput.setMessage(currentUser.switchVisibility(commandInput.getPlaylistId()));
        return mapper.valueToTree(visiOutput);
    }

    private JsonNode followPlaylist(final CommandInput commandInput) {
        MessageOutput followOutput = new MessageOutput(commandInput);
        followOutput.setMessage(currentUser.follow());
        return mapper.valueToTree(followOutput);
    }

    private JsonNode getTop5Playlists(final CommandInput commandInput) {
        playlists.sort(new Comparator<Playlist>() {
            @Override
            public int compare(final Playlist o1, final Playlist o2) {
                if (o1.getFollowers() > o2.getFollowers()) {
                    return -1;
                } else if (o1.getFollowers() < o2.getFollowers()) {
                    return 1;
                } else {
                    if (o1.getCreationTime() < o2.getCreationTime()) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            }
        });
        int count = 0;
        ArrayList<String> topFive = new ArrayList<>();
        for (Playlist playlist : playlists) {
            topFive.add(playlist.getName());
            count++;
            if (count == MAX_COUNT) {
                break;
            }
        }
        TopOutput topFiveOutput = new TopOutput(commandInput);
        topFiveOutput.setResult(topFive);
        return mapper.valueToTree(topFiveOutput);
    }

    private JsonNode getTop5Songs(final CommandInput commandInput) {
        songs.sort(new Comparator<Song>() {
            @Override
            public int compare(final Song o1, final Song o2) {
                if (o1.getLikes() > o2.getLikes()) {
                    return -1;
                } else if (o1.getLikes() < o2.getLikes()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        int count = 0;
        ArrayList<String> topFive = new ArrayList<>();
        for (Song song : songs) {
            topFive.add(song.getName());
            count++;
            if (count == MAX_COUNT) {
                break;
            }
        }
        TopOutput topFiveOutput = new TopOutput(commandInput);
        topFiveOutput.setResult(topFive);
        return mapper.valueToTree(topFiveOutput);
    }
}
