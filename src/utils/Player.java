package utils;

import fileio.input.PodcastInput;
import fileio.input.SongInput;

public class Player {
    private SongInput song = null;
    private PodcastInput podcast = null;
    private int remainedTime;
    private boolean paused;
    private int repeat;
    private boolean finshed;
    private int type; // 0 - song, 1 - podcast, 2 - playlist

    public Player() {
        this.paused = false;
        this.repeat = 0;
        this.finshed = false;
    }

    public Player(SongInput song) {
        this();
        this.song = song;
        this.remainedTime = song.getDuration();
        this.type = 0;
    }

    public Player(PodcastInput podcast) {
        this();
        this.podcast = podcast;
        this.remainedTime = podcast.getEpisodes().get(0).getDuration();
        this.type = 1;
    }

    public String playPause() {
        if (finshed) {
            return "Please load a source before attempting to pause or resume playback.";
        }
        paused = !paused;
        if (paused) {
            return "Playback paused successfully.";
        } else {
            return "Playback resumed successfully.";
        }
    }

    public void repeat() {
        repeat = (repeat + 1) % 3;
    }

    public void update(int difference) {
        if (!paused) {
            remainedTime -= difference;
            if (remainedTime <= 0) {
                if (repeat != 0) {
                    remainedTime = song.getDuration();
                } else {
                    remainedTime = 0;
                    paused = true;
                    finshed = true;
                }
            }
        }
    }

    public Stats status() {
        if (finshed) {
            return new Stats("", remainedTime, repeat, false, paused);
        } else {
            return new Stats(song.getName(), remainedTime, repeat, false, paused);
        }
    }
}
