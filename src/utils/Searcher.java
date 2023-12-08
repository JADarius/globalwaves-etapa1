package utils;

import fileio.input.FilterInput;
import utils.library.Playlist;
import utils.library.Podcast;
import utils.library.Song;

import java.util.ArrayList;

public final class Searcher {
    private static final int MAX_FINDS = 5;
    public Searcher() {

    }

    /**
     * Searches for a song using the filters
     * @param filters The filters used to search
     * @param songs The list of songs in which the search will take place
     * @return A list of the first 5 songs that match all the filters
     */
    public ArrayList<Song> searchSong(final FilterInput filters,
                                      final ArrayList<Song> songs) {
        int songCount = 0;
        ArrayList<Song> list = new ArrayList<Song>();
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
    public ArrayList<Podcast> searchPodcast(final FilterInput filters,
                                            final ArrayList<Podcast> podcasts) {
        int podcastCount = 0;
        ArrayList<Podcast> list = new ArrayList<Podcast>();
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
    public ArrayList<Playlist> searchPlaylist(final FilterInput filters,
                                              final ArrayList<Playlist> playlists,
                                              final String username) {
        int playlistCount = 0;
        ArrayList<Playlist> list = new ArrayList<>();
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
}
