package fileio.output;

import fileio.input.SongInput;
import lombok.Getter;
import utils.Playlist;

import java.util.ArrayList;

@Getter
public final class PlaylistOutput {
    private String name;
    private ArrayList<String> songs;
    private String visibility;
    private int followers;

    public PlaylistOutput(Playlist playlist) {
        this.name = playlist.getName();
        this.visibility = playlist.getVisibility();
        this.followers = playlist.getFollowers();
        this.songs = new ArrayList<>();
        for (SongInput song : playlist.getSongs()) {
            this.songs.add(song.getName());
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSongs(ArrayList<String> songs) {
        this.songs = songs;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }
}
