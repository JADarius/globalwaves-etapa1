package fileio.output;

import fileio.input.SongInput;
import utils.Playlist;

import java.util.ArrayList;

public class PlaylistOutput {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<String> songs) {
        this.songs = songs;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }
}
