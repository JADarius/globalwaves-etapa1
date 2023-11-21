package utils;

import fileio.input.SongInput;

import java.util.ArrayList;
import java.util.Random;

public class Playlist {
    private String owner;
    private String name;
    private ArrayList<SongInput> songs;
    private String visibility;
    private int followers;

    public Playlist(String owner, String name) {
        this.owner = owner;
        this.name = name;
        this.songs = new ArrayList<SongInput>();
        this.visibility = "public";
        this.followers = 0;
    }

    public void switchVisibility() {
        if (this.visibility.equals("public")) {
            this.visibility = "private";
        } else {
            this.visibility = "public";
        }
    }

    public String addRemoveInPlaylist(SongInput song) {
        if (this.songs.contains(song)) {
            this.songs.remove(song);
            return "Successfully removed from playlist.";
        } else {
            this.songs.add(song);
            return "Successfully added to playlist.";
        }
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<SongInput> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<SongInput> songs) {
        this.songs = songs;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public String getVisibility() {
        return visibility;
    }
}
