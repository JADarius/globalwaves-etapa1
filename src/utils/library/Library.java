package utils.library;

import fileio.input.LibraryInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;
import lombok.Getter;
import utils.User;

import java.util.ArrayList;

@Getter
public class Library {
    private final ArrayList<Song> songs;
    private final ArrayList<Podcast> podcasts;
    private final ArrayList<User> users;

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
    }
}
