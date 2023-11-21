package utils;

import fileio.input.SongInput;

import java.util.ArrayList;
import java.util.Random;

public class Playlist {
    private String user;
    private String name;
    private ArrayList<SongInput> songs;
    private String visibility;
    private int followers;
    private ArrayList<Integer> shuffleOrder;
    private Random rand = null;
    private boolean shuffle;
    private int currentSong;

    public Playlist(String user, String name) {
        this.user = user;
        this.name = name;
        this.songs = new ArrayList<SongInput>();
        this.visibility = "public";
        this.followers = 0;
        this.shuffle = false;
        this.currentSong = 0;
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

    public void shuffle(int seed) {
        this.rand = new Random(seed);
        this.shuffleOrder = new ArrayList<>();
        for (int index = 0; index < songs.size(); index++) {
            int changeIndex = rand.nextInt(songs.size());
            shuffleOrder.add(changeIndex);
        }
        this.shuffle = true;
    }

    public SongInput next() {
        if (shuffle) {
            return songs.get(shuffleOrder.get(++this.currentSong));
        } else {
            return songs.get(++this.currentSong);
        }
    }

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

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }
}
