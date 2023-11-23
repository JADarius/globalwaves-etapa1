package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fileio.input.CommandInput;
import fileio.input.LibraryInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.output.*;

import java.util.ArrayList;
import java.util.Comparator;

public class CommandParser {
    private final LibraryInput library;
    private final ArrayList<User> users;
    private final ArrayList<Playlist> playlists;
    private final ArrayList<Song> songs;
    private final ObjectMapper mapper;
    private int lastTimestamp;

    public CommandParser(LibraryInput library, ArrayList<User> users, ObjectMapper mapper, ArrayList<Song> songs) {
        this.library = library;
        this.users = users;
        this.mapper = mapper;
        this.songs = songs;
        this.playlists = new ArrayList<>();
        this.lastTimestamp = 0;
    }

    public User getUser(CommandInput commandInput) {
        for (User user : users) {
            if (user.getName().equals(commandInput.getUsername())) {
                return user;
            }
        }
        return null;
    }

    public void update(int difference) {
        for (User user : users) {
//            if (user.getName().equals("emily30")) {
//                System.out.println("debug");
//            }
            user.update(difference);
        }
    }

    public JsonNode parseCommand(CommandInput commandInput) {
        JsonNode output = null;
        String command = commandInput.getCommand();
        update(commandInput.getTimestamp() - lastTimestamp);
        User currentUser = getUser(commandInput);
        // Debug
//        if (currentUser.getName().equals("grace25") && currentUser.getPlayer() != null && currentUser.getPlayer().getType() == 2) {
//            System.out.println(commandInput.getTimestamp());
//            System.out.println("Playing: " + currentUser.getPlayer().getSong().getName());
//            System.out.println("CurrentItem: " + currentUser.getPlayer().getCurrentItem());
//            System.out.println("Shuffle: " + currentUser.getPlayer().isShuffle());
//            for (SongInput song : currentUser.getPlayer().getPlaylist().getSongs()) {
//                System.out.println(song.getName());
//            }
//        }
        if (command.equals("search")) {
            Searcher searcher = Searcher.getInstance();
            getUser(commandInput).stop();
            if (commandInput.getType().equals("song")) {
                SearchOutput searchOutput = new SearchOutput(commandInput);
                ArrayList<SongInput> list = searcher.searchSong(commandInput.getFilters(), library.getSongs());
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
                ArrayList<PodcastInput> list = searcher.searchPodcast(commandInput.getFilters(), library.getPodcasts());
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
                ArrayList<Playlist> list = searcher.searchPlaylist(commandInput.getFilters(), playlists, commandInput.getUsername());
                currentUser.setSearchedPlaylists(list);
                searchOutput.setMessage("Search returned " + list.size() + " results");
                ArrayList<String> results = new ArrayList<>();
                for (Playlist playlist : list) {
                    results.add(playlist.getName());
                }
                searchOutput.setResults(results);
                output = mapper.valueToTree(searchOutput);
            }
        } else if (command.equals("select")) {
            MessageOutput selectOutput = new MessageOutput(commandInput);
            selectOutput.setMessage(currentUser.select(commandInput.getItemNumber()));
            output = mapper.valueToTree(selectOutput);
        } else if (command.equals("load")) {
            MessageOutput loadOutput = new MessageOutput(commandInput);
            loadOutput.setMessage(currentUser.load());
            output = mapper.valueToTree(loadOutput);
        } else if (command.equals("playPause")) {
            MessageOutput playPauseOutput = new MessageOutput(commandInput);
            playPauseOutput.setMessage(currentUser.playPause());
            output = mapper.valueToTree(playPauseOutput);
        } else if (command.equals("status")) {
            Stats stats = currentUser.getPlayer().status();
            StatsOutput status = new StatsOutput(commandInput, stats);
            output = mapper.valueToTree(status);
        } else if (command.equals("createPlaylist")) {
            MessageOutput createPlaylistOutput = new MessageOutput(commandInput);
            Playlist playlist = currentUser.createPlaylist(commandInput.getPlaylistName(), commandInput.getTimestamp());
            if (playlist == null) {
                createPlaylistOutput.setMessage("A playlist with the same name already exists.");
            } else {
                createPlaylistOutput.setMessage("Playlist created successfully.");
                playlists.add(playlist);
            }
            output = mapper.valueToTree(createPlaylistOutput);
        } else if (command.equals("addRemoveInPlaylist")) {
            MessageOutput addRemoveOutput = new MessageOutput(commandInput);
            addRemoveOutput.setMessage(currentUser.addRemoveInPlaylist(commandInput.getPlaylistId()));
            output = mapper.valueToTree(addRemoveOutput);
        } else if (command.equals("like")) {
            MessageOutput likeOutput = new MessageOutput(commandInput);
            likeOutput.setMessage(currentUser.like(songs));
            output = mapper.valueToTree(likeOutput);
        } else if (command.equals("showPlaylists")) {
            SearchPlaylistOutput showPlaylistOutput = new SearchPlaylistOutput(commandInput);
            showPlaylistOutput.setResult(currentUser.showPlaylists());
            output = mapper.valueToTree(showPlaylistOutput);
        } else if (command.equals("showPreferredSongs")) {
            ShowLikedOutput likedOutput = new ShowLikedOutput(commandInput);
            likedOutput.setResult(currentUser.showPreferredSongs());
            output = mapper.valueToTree(likedOutput);
        } else if (command.equals("repeat")) {
            MessageOutput repeatOutput = new MessageOutput(commandInput);
            repeatOutput.setMessage(currentUser.repeat());
            output = mapper.valueToTree(repeatOutput);
        } else if (command.equals("shuffle")) {
            MessageOutput shuffleOutput = new MessageOutput(commandInput);
            shuffleOutput.setMessage(currentUser.shuffle(commandInput.getSeed()));
            output = mapper.valueToTree(shuffleOutput);
        } else if (command.equals("prev")) {
            MessageOutput prevOutput = new MessageOutput(commandInput);
            prevOutput.setMessage(currentUser.prev());
            output = mapper.valueToTree(prevOutput);
        } else if (command.equals("forward")) {
            MessageOutput forwardOutput = new MessageOutput(commandInput);
            forwardOutput.setMessage(currentUser.forward());
            output = mapper.valueToTree(forwardOutput);
        } else if (command.equals("backward")) {
            MessageOutput backwardOutput = new MessageOutput(commandInput);
            backwardOutput.setMessage(currentUser.backward());
            output = mapper.valueToTree(backwardOutput);
        } else if (command.equals("next")) {
            MessageOutput nextOutput = new MessageOutput(commandInput);
            nextOutput.setMessage(currentUser.next());
            output = mapper.valueToTree(nextOutput);
        } else if (command.equals("switchVisibility")) {
            MessageOutput visibilityOutput = new MessageOutput(commandInput);
            visibilityOutput.setMessage(currentUser.switchVisibility(commandInput.getPlaylistId()));
            output = mapper.valueToTree(visibilityOutput);
        } else if (command.equals("follow")) {
            MessageOutput followOutput = new MessageOutput(commandInput);
            followOutput.setMessage(currentUser.follow());
            output = mapper.valueToTree(followOutput);
        } else if (command.equals("getTop5Playlists")) {
            playlists.sort(new Comparator<Playlist>() {
                @Override
                public int compare(Playlist o1, Playlist o2) {
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
                if (count == 5) {
                    break;
                }
            }
            TopOutput topFiveOutput = new TopOutput(commandInput);
            topFiveOutput.setResult(topFive);
            output = mapper.valueToTree(topFiveOutput);
        } else if (command.equals("getTop5Songs")) {
            songs.sort(new Comparator<Song>() {
                @Override
                public int compare(Song o1, Song o2) {
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
                if (count == 5) {
                    break;
                }
            }
            TopOutput topFiveOutput = new TopOutput(commandInput);
            topFiveOutput.setResult(topFive);
            output = mapper.valueToTree(topFiveOutput);
        }
        lastTimestamp = commandInput.getTimestamp();
        return output;
    }
}
