package utils;

import fileio.input.FilterInput;
import utils.accounts.Artist;
import utils.accounts.GenericUser;
import utils.library.Album;
import utils.library.Playlist;
import utils.library.Podcast;
import utils.library.Song;

import java.util.ArrayList;
import java.util.List;

public final class Searcher {
    private static final int MAX_FINDS = 5;
    private Searcher() {
    }

    /**
     * Searches for a song using the filters
     * @param filters The filters used to search
     * @param songs The list of songs in which the search will take place
     * @return A list of the first 5 songs that match all the filters
     */
    public static List<Song> searchSong(final FilterInput filters,
                                      final List<Song> songs) {
        int songCount = 0;
        List<Song> list = new ArrayList<Song>();
        for (Song song : songs) {
            if (filters.getName() != null && !song.getName().startsWith(filters.getName())) {
                continue;
            }
            if (filters.getAlbum() != null && !song.getAlbum().equals(filters.getAlbum())) {
                continue;
            }
            if (filters.getTags() != null) {
                boolean checksout = true;
                for (String tag : filters.getTags()) {
                    if (!song.getTags().contains(tag)) {
                        checksout = false;
                        break;
                    }
                }
                if (!checksout) {
                    continue;
                }
            }
            if (filters.getLyrics() != null
                    && !song.getLyrics().toLowerCase().contains(filters.getLyrics().toLowerCase())) {
                continue;
            }
            if (filters.getGenre() != null
                    && !song.getGenre().equalsIgnoreCase(filters.getGenre())) {
                continue;
            }
            if (filters.getReleaseYear() != null) {
                String condition = filters.getReleaseYear();
                int year = Integer.parseInt(condition.substring(1));
                switch (condition.charAt(0)) {
                    case '<':
                        if (song.getReleaseYear() > year) {
                            continue;
                        }
                        break;
                    case '>':
                        if (song.getReleaseYear() < year) {
                            continue;
                        }
                        break;
                    default:
                        break;
                }
            }
            if (filters.getArtist() != null
                    && !song.getArtist().equals(filters.getArtist())) {
                continue;
            }
            list.add(song);
            songCount += 1;
            if (songCount == MAX_FINDS) {
                break;
            }
        }
        return list;
    }

    /**
     * Searches for a podcast using the filters
     * @param filters The filters used to search
     * @param podcasts The list of podcasts in which the search will take place
     * @return A list of the first 5 podcasts that match all the filters
     */
    public static List<Podcast> searchPodcast(final FilterInput filters,
                                            final List<Podcast> podcasts) {
        int podcastCount = 0;
        List<Podcast> list = new ArrayList<Podcast>();
        for (Podcast podcast : podcasts) {
            if (filters.getName() != null && !podcast.getName().contains(filters.getName())) {
                continue;
            }
            if (filters.getOwner() != null && !podcast.getOwner().equals(filters.getOwner())) {
                continue;
            }
            list.add(podcast);
            podcastCount += 1;
            if (podcastCount == MAX_FINDS) {
                break;
            }
        }
        return list;
    }

    /**
     * Searches for a playlist using the filters
     * @param filters The filters used to search
     * @param playlists The list of playlists in which the search will take place
     * @param username Username used to check if the playlist can be accessed by the user
     * @return A list of the first 5 playlists that match all the filters
     */
    public static List<Playlist> searchPlaylist(final FilterInput filters,
                                              final List<Playlist> playlists,
                                              final String username) {
        int playlistCount = 0;
        List<Playlist> list = new ArrayList<>();
        for (Playlist playlist : playlists) {
            if (playlist.getVisibility().equals("private")
                    && !playlist.getOwner().equals(username)) {
                continue;
            }
            if (filters.getName() != null && !playlist.getName().contains(filters.getName())) {
                continue;
            }
            if (filters.getOwner() != null && !playlist.getOwner().equals(filters.getOwner())) {
                continue;
            }
            list.add(playlist);
            playlistCount += 1;
            if (playlistCount == MAX_FINDS) {
                break;
            }
        }
        return list;
    }

    public static List<Album> searchAlbum(final FilterInput filters,
                                          final List<Album> albums) {
        int albumCount = 0;
        List<Album> list = new ArrayList<>();
        for (Album album : albums) {
            if (filters.getName() != null && !album.getName().startsWith(filters.getName())) {
                continue;
            }
            if (filters.getOwner() != null && !album.getOwner().startsWith(filters.getOwner())) {
                continue;
            }
            if (filters.getDescription() != null && !album.getDescription().startsWith(filters.getDescription())) {
                continue;
            }
            list.add(album);
            albumCount += 1;
            if (albumCount == MAX_FINDS) {
                break;
            }
        }
        return list;
    }

    public static List<GenericUser> searchArtist(final FilterInput filters,
                                                 final List<GenericUser> users) {
        int userCount = 0;
        List<GenericUser> list = new ArrayList<>();
        for (GenericUser user : users) {
            if (filters.getName() != null && !user.getName().startsWith(filters.getName())) {
                continue;
            }
            list.add(user);
            userCount += 1;
            if (userCount == MAX_FINDS) {
                break;
            }
        }
        return list;
    }
}
