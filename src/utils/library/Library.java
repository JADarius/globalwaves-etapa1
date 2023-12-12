package utils.library;

import fileio.input.LibraryInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;
import lombok.Getter;
import utils.Enums;
import utils.accounts.Artist;
import utils.accounts.Host;
import utils.accounts.User;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Library {
    private List<Song> songs;
    private List<Album> albums;
    private List<Podcast> podcasts;
    private List<Playlist> playlists;
    private List<User> users;
    private List<Artist> artists;
    private List<Host> hosts;

    public Library(LibraryInput libraryInput) {
        this.songs = new ArrayList<>();
        for (SongInput song : libraryInput.getSongs()) {
            this.songs.add(new Song(song, Enums.CreationType.ADMIN));
        }

        this.podcasts = new ArrayList<>();
        for (PodcastInput podcast : libraryInput.getPodcasts()) {
            this.podcasts.add(new Podcast(podcast));
        }

        this.users = new ArrayList<>();
        for (UserInput user : libraryInput.getUsers()) {
            this.users.add(new User(user));
        }

        this.albums = new ArrayList<>();

        this.playlists = new ArrayList<>();

        this.artists = new ArrayList<>();

        this.hosts = new ArrayList<>();
    }

    public void addPlaylist(Playlist playlist) {
        this.playlists.add(playlist);
    }

    public void addPodcast(Podcast podcast) {
        this.podcasts.add(podcast);
    }

    public void addSong(Song song) {
        this.songs.add(song);
    }

    public void addAlbum(Album album) {
        this.albums.add(album);
        for (Song song : album.getSongs()) {
            addSong(song);
        }
    }

    public void removeSong(Song song) {
        this.songs.remove(song);
    }

    public void removeAlbum(Album album) {
        for (Song song : album.getSongs()) {
            removeSong(song);
            for (Playlist playlist : playlists) {
                playlist.getSongs().remove(song);
            }
            for (User user : users) {
                user.getLikedSongs().remove(song);
                user.notifyObservers();
            }
        }
    }

    public void removePodcast(Podcast podcast) {
        this.podcasts.remove(podcast);
    }
}
