package utils;

import fileio.input.SongInput;

import java.util.ArrayList;

public class Playlist {
    private String user;
    private String name;
    private ArrayList<SongInput> songs;
    private String visibility;
    private int followers;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public Playlist(String user, String name) {
        this.user = user;
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

    public void AddRemoveInPlaylist(SongInput song) {
        if (this.songs.contains(song)) {
            this.songs.remove(song);
        } else {
            this.songs.add(song);
        }
    }
}
