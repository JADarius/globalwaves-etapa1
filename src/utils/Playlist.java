package utils;

import fileio.input.SongInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Random;

@Getter
public class Playlist {
    private String owner;
    private String name;
    private ArrayList<SongInput> songs;
    private String visibility;
    private int followers;
    private int creationTime;

    public Playlist(String owner, String name, int creationTime) {
        this.owner = owner;
        this.name = name;
        this.songs = new ArrayList<>();
        this.visibility = "public";
        this.followers = 0;
        this.creationTime = creationTime;
    }

    public void switchVisibility() {
        if (visibility.equals("public")) {
            visibility = "private";
        } else {
            visibility = "public";
        }
    }

    public String addRemoveInPlaylist(SongInput song) {
        if (songs.contains(song)) {
            songs.remove(song);
            return "Successfully removed from playlist.";
        } else {
            songs.add(song);
            return "Successfully added to playlist.";
        }
    }

    public void addFollower() {
        followers++;
    }

    public void removeFollower() {
        followers--;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSongs(ArrayList<SongInput> songs) {
        this.songs = songs;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

}
