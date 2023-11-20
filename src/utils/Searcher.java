package utils;

import fileio.input.FilterInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;

import java.util.ArrayList;

public class Searcher {
    private static Searcher instance = null;

    private Searcher() {}

    public static Searcher getInstance() {
        if (instance == null) {
            instance = new Searcher();
        }
        return instance;
    }

    public ArrayList<SongInput> searchSong(FilterInput filters, ArrayList<SongInput> songs) {
        int song_count = 0;
        ArrayList<SongInput> list = new ArrayList<SongInput>();
        for (SongInput song : songs) {
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
            if (filters.getLyrics() != null && !song.getLyrics().contains(filters.getLyrics())) {
                continue;
            }
            if (filters.getGenre() != null && !song.getGenre().toUpperCase().equals(filters.getGenre().toUpperCase())) {
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
                }
            }
            if (filters.getArtist() != null && !song.getArtist().equals(filters.getArtist())) {
                continue;
            }
            list.add(song);
            song_count += 1;
            if (song_count == 5) {
                break;
            }
        }
        return list;
    }

    public ArrayList<PodcastInput> searchPodcast(FilterInput filters, ArrayList<PodcastInput> podcasts) {
        int podcast_count = 0;
        ArrayList<PodcastInput> list = new ArrayList<PodcastInput>();
        for (PodcastInput podcast : podcasts) {
            if (filters.getName() != null && !podcast.getName().contains(filters.getName())) {
                continue;
            }
            if (filters.getOwner() != null && !podcast.getOwner().equals(filters.getOwner())) {
                continue;
            }
            list.add(podcast);
            podcast_count += 1;
            if (podcast_count == 5) {
                break;
            }
        }
        return list;
    }
}
