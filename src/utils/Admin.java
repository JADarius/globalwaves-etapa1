package utils;

import com.fasterxml.jackson.databind.JsonNode;
import fileio.input.CommandInput;
import fileio.input.LibraryInput;
import fileio.output.TopOutput;
import utils.library.Library;
import utils.library.Playlist;
import utils.library.Podcast;
import utils.library.Song;

import java.util.ArrayList;
import java.util.Comparator;

public final class Admin {
    private static final int MAX_COUNT = 5;
    public static Library library = null;
    private static int lastTimestamp = 0;
//    private static User currentUser = null;

    public static void setLibrary(LibraryInput libraryInput) {
        library = new Library(libraryInput);
    }

//    public static void setCurrentUser(User user) {
//        currentUser = user;
//    }

    /**
     * Gets the user from the command
     * @param userName The username from the command
     * @return The user from the command
     */
    public static User getUser(final String userName) {
        for (User user : library.getUsers()) {
            if (user.getName().equals(userName)) {
                return user;
            }
        }
        return null;
    }

    public static GenericUser getUserFromAll(final String userName) {
        ArrayList<GenericUser> allUsers = new ArrayList<>();
        allUsers.addAll(library.getUsers());
        allUsers.addAll(library.getArtists());
        allUsers.addAll(library.getHosts());
        for (GenericUser user : allUsers) {
            if (user.getName().equals(userName)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Updates all the players with the time elapsed and updates the last timestamp
     * @param newTimeStamp The new timestamp
     */
    public static void update(final int newTimeStamp) {
        int elapsed = newTimeStamp - lastTimestamp;
        lastTimestamp = newTimeStamp;
        for (User user : library.getUsers()) {
            user.update(elapsed);
        }
    }

    public static ArrayList<Song> getSongs() {
        return new ArrayList<>(library.getSongs());
    }

    public static ArrayList<Playlist> getPlaylists() {
        return new ArrayList<>(library.getPlaylists());
    }

    public static ArrayList<Podcast> getPodcasts() {
        return new ArrayList<>(library.getPodcasts());
    }

    public static void addPlaylist(Playlist playlist) {
        library.addPlaylist(playlist);
    }

    public static ArrayList<String> getTop5Playlists() {
        ArrayList<Playlist> playlists = getSortedPlaylists();
        int count = 0;
        ArrayList<String> topFive = new ArrayList<>();
        for (Playlist playlist : playlists) {
            topFive.add(playlist.getName());
            count++;
            if (count == MAX_COUNT) {
                break;
            }
        }
        return topFive;
    }

    private static ArrayList<Playlist> getSortedPlaylists() {
        ArrayList<Playlist> playlists = new ArrayList<>(library.getPlaylists());
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
        return playlists;
    }

    public static ArrayList<String> getTop5Songs() {
        ArrayList<Song> songs = getSortedSongs();
        int count = 0;
        ArrayList<String> topFive = new ArrayList<>();
        for (Song song : songs) {
            topFive.add(song.getName());
            count++;
            if (count == MAX_COUNT) {
                break;
            }
        }
        return topFive;
    }

    private static ArrayList<Song> getSortedSongs() {
        ArrayList<Song> songs = new ArrayList<>(library.getSongs());
        songs.sort(new Comparator<Song>() {
            @Override
            public int compare(final Song o1, final Song o2) {
                return Integer.compare(o2.getLikes(), o1.getLikes());
            }
        });
        return songs;
    }
}
