package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fileio.input.CommandInput;
import fileio.input.LibraryInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.output.MessageOutput;
import fileio.output.SearchOutput;
import fileio.output.SearchPlaylistOutput;
import fileio.output.ShowLikedOutput;
import fileio.output.StatsOutput;
import fileio.output.TopOutput;

import java.util.ArrayList;
import java.util.Comparator;

public final class CommandParser {
    private static final int MAX_COUNT = 5;
    private final LibraryInput library;
    private final ArrayList<User> users;
    private final ArrayList<Playlist> playlists;
    private final ArrayList<Song> songs;
    private final ObjectMapper mapper;
    private int lastTimestamp;

    public CommandParser(final LibraryInput library,
                         final ArrayList<User> users,
                         final ObjectMapper mapper,
                         final ArrayList<Song> songs) {
        this.library = library;
        this.users = users;
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
        for (User user : users) {
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
        for (User user : users) {
            user.update(difference);
        }
    }

    /**
     * Parses the command and gives the corresponding output to be written in the JSON
     * @param commandInput The command
     * @return The output to be written in the JSON
     */
    public JsonNode parseCommand(final CommandInput commandInput) {
        JsonNode output = null;
        String command = commandInput.getCommand();
        update(commandInput.getTimestamp() - lastTimestamp);
        User currentUser = getUser(commandInput);
        switch (command) {
            case "search" -> {
                Searcher searcher = new Searcher();
                getUser(commandInput).stop();
                if (commandInput.getType().equals("song")) {
                    SearchOutput searchOutput = new SearchOutput(commandInput);
                    ArrayList<SongInput> list = searcher.searchSong(commandInput.getFilters(),
                            library.getSongs());
                    currentUser.setSearchedSongs(list);
                    searchOutput.setMessage("Search returned " + list.size() + " results");
                    ArrayList<String> results = new ArrayList<>();
                    for (SongInput song : list) {
                        results.add(song.getName());
                    }
                    searchOutput.setResults(results);
                    output = mapper.valueToTree(searchOutput);
                } else if (commandInput.getType().equals("podcast")) {
                    SearchOutput searchOutput = new SearchOutput(commandInput);
                    ArrayList<PodcastInput> list = searcher.searchPodcast(commandInput.getFilters(),
                            library.getPodcasts());
                    currentUser.setSearchedPodcasts(list);
                    searchOutput.setMessage("Search returned " + list.size() + " results");
                    ArrayList<String> results = new ArrayList<>();
                    for (PodcastInput podcast : list) {
                        results.add(podcast.getName());
                    }
                    searchOutput.setResults(results);
                    output = mapper.valueToTree(searchOutput);
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
                    output = mapper.valueToTree(searchOutput);
                }
            }
            case "select" -> {
                MessageOutput selectOutput = new MessageOutput(commandInput);
                selectOutput.setMessage(currentUser.select(commandInput.getItemNumber()));
                output = mapper.valueToTree(selectOutput);
            }
            case "load" -> {
                MessageOutput loadOutput = new MessageOutput(commandInput);
                loadOutput.setMessage(currentUser.load());
                output = mapper.valueToTree(loadOutput);
            }
            case "playPause" -> {
                MessageOutput playPauseOutput = new MessageOutput(commandInput);
                playPauseOutput.setMessage(currentUser.playPause());
                output = mapper.valueToTree(playPauseOutput);
            }
            case "status" -> {
                Stats stats = currentUser.getPlayer().status();
                StatsOutput status = new StatsOutput(commandInput, stats);
                output = mapper.valueToTree(status);
            }
            case "createPlaylist" -> {
                MessageOutput createOutput = new MessageOutput(commandInput);
                Playlist playlist = currentUser.createPlaylist(commandInput.getPlaylistName(),
                        commandInput.getTimestamp());
                if (playlist == null) {
                    createOutput.setMessage("A playlist with the same name already exists.");
                } else {
                    createOutput.setMessage("Playlist created successfully.");
                    playlists.add(playlist);
                }
                output = mapper.valueToTree(createOutput);
            }
            case "addRemoveInPlaylist" -> {
                MessageOutput addOutput = new MessageOutput(commandInput);
                addOutput.setMessage(currentUser.addRemoveInPlaylist(commandInput.getPlaylistId()));
                output = mapper.valueToTree(addOutput);
            }
            case "like" -> {
                MessageOutput likeOutput = new MessageOutput(commandInput);
                likeOutput.setMessage(currentUser.like(songs));
                output = mapper.valueToTree(likeOutput);
            }
            case "showPlaylists" -> {
                SearchPlaylistOutput showPlaylistOutput = new SearchPlaylistOutput(commandInput);
                showPlaylistOutput.setResult(currentUser.showPlaylists());
                output = mapper.valueToTree(showPlaylistOutput);
            }
            case "showPreferredSongs" -> {
                ShowLikedOutput likedOutput = new ShowLikedOutput(commandInput);
                likedOutput.setResult(currentUser.showPreferredSongs());
                output = mapper.valueToTree(likedOutput);
            }
            case "repeat" -> {
                MessageOutput repeatOutput = new MessageOutput(commandInput);
                repeatOutput.setMessage(currentUser.repeat());
                output = mapper.valueToTree(repeatOutput);
            }
            case "shuffle" -> {
                MessageOutput shuffleOutput = new MessageOutput(commandInput);
                shuffleOutput.setMessage(currentUser.shuffle(commandInput.getSeed()));
                output = mapper.valueToTree(shuffleOutput);
            }
            case "prev" -> {
                MessageOutput prevOutput = new MessageOutput(commandInput);
                prevOutput.setMessage(currentUser.prev());
                output = mapper.valueToTree(prevOutput);
            }
            case "forward" -> {
                MessageOutput forwardOutput = new MessageOutput(commandInput);
                forwardOutput.setMessage(currentUser.forward());
                output = mapper.valueToTree(forwardOutput);
            }
            case "backward" -> {
                MessageOutput backwardOutput = new MessageOutput(commandInput);
                backwardOutput.setMessage(currentUser.backward());
                output = mapper.valueToTree(backwardOutput);
            }
            case "next" -> {
                MessageOutput nextOutput = new MessageOutput(commandInput);
                nextOutput.setMessage(currentUser.next());
                output = mapper.valueToTree(nextOutput);
            }
            case "switchVisibility" -> {
                MessageOutput visiOutput = new MessageOutput(commandInput);
                visiOutput.setMessage(currentUser.switchVisibility(commandInput.getPlaylistId()));
                output = mapper.valueToTree(visiOutput);
            }
            case "follow" -> {
                MessageOutput followOutput = new MessageOutput(commandInput);
                followOutput.setMessage(currentUser.follow());
                output = mapper.valueToTree(followOutput);
            }
            case "getTop5Playlists" -> {
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
                output = mapper.valueToTree(topFiveOutput);
            }
            case "getTop5Songs" -> {
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
                output = mapper.valueToTree(topFiveOutput);
            }
            default -> {
                break;
            }
        }
        lastTimestamp = commandInput.getTimestamp();
        return output;
    }
}
