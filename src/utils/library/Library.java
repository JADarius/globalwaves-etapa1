package utils.library;

import fileio.input.LibraryInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;
import lombok.Getter;
import utils.Artist;
import utils.GenericUser;
import utils.Host;
import utils.User;

import java.util.ArrayList;

@Getter
public class Library {
    private ArrayList<Song> songs;
    private ArrayList<Podcast> podcasts;
    private ArrayList<Playlist> playlists;
    private ArrayList<User> users;
    private ArrayList<Artist> artists;
    private ArrayList<Host> hosts;

    public Library(LibraryInput libraryInput) {
        this.songs = new ArrayList<>();
        for (SongInput song : libraryInput.getSongs()) {
            this.songs.add(new Song(song));
        }

        this.podcasts = new ArrayList<>();
        for (PodcastInput podcast : libraryInput.getPodcasts()) {
            this.podcasts.add(new Podcast(podcast));
        }

        this.users = new ArrayList<>();
        for (UserInput user : libraryInput.getUsers()) {
            this.users.add(new User(user));
        }

        this.playlists = new ArrayList<>();

        this.artists = new ArrayList<>();

        this.hosts = new ArrayList<>();
    }

    public void addPlaylist(Playlist playlist) {
        this.playlists.add(playlist);
    }
}
