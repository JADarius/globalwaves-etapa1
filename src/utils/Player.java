package utils;

import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;

import java.util.ArrayList;
import java.util.Random;

public class Player {
    private SongInput song = null;
    private PodcastInput podcast = null;
    private Playlist playlist = null;
    private EpisodeInput episode = null;
    private int remainedTime;
    private boolean paused;
    private int repeat;
    private boolean finshed;
    private int type; // 0 - song, 1 - podcast, 2 - playlist
    private ArrayList<Integer> shuffleOrder;
    private Random rand = null;
    private boolean shuffle;
    private int currentItem;


    public Player() {
        this.paused = false;
        this.repeat = 0;
        this.finshed = false;
        this.shuffle = false;
        this.currentItem = 1;
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
        this.episode = podcast.getEpisodes().get(0);
        this.remainedTime = episode.getDuration();
        this.type = 1;
    }

    public Player(PodcastSave save) {
        this();
        this.podcast = save.getPodcast();
        this.currentItem = save.getCurrentItem();
        this.episode = podcast.getEpisodes().get(currentItem - 1);
        this.remainedTime = save.getRemainedTime();
        this.type = 1;
    }

    public Player(Playlist playlist) {
        this();
        this.playlist = playlist;
        this.song = playlist.getSongs().get(0);
        this.remainedTime = song.getDuration();
        this.type = 2;
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
        if (!paused && !finshed) {
            while (remainedTime < difference) {
                difference -= remainedTime;
                next();
                if (finshed) {
                    break;
                }
            }
            if (!finshed && difference > 0) {
                remainedTime -= difference;
                if (remainedTime == 0) {
                    finshed = true;
                    paused = true;
                }
            }
        }
    }

    public Stats status() {
        if (finshed) {
            return new Stats("", remainedTime, repeat, shuffle, paused, type);
        } else {
            if (type == 0 || type == 2) {
                return new Stats(song.getName(), remainedTime, repeat, shuffle, paused, type);
            } else {
                return new Stats(episode.getName(), remainedTime, repeat, shuffle, paused, type);
            }
        }
    }

    public void shuffle(int seed) {
        this.rand = new Random(seed);
        this.shuffleOrder = new ArrayList<>();
        for (int index = 0; index < playlist.getSongs().size(); index++) {
            int changeIndex = rand.nextInt(playlist.getSongs().size());
            shuffleOrder.add(changeIndex);
        }
        this.shuffle = true;
    }

    public void next() {
        switch (type) {
            case 0:
                if (repeat == 1) {
                    remainedTime = song.getDuration();
                    repeat = 0;
                } else if (repeat == 2) {
                    remainedTime = song.getDuration();
                } else {
                    remainedTime = 0;
                    finshed = true;
                    paused = true;
                    song = null;
                }
                break;
            case 1:
                if (currentItem == podcast.getEpisodes().size()) {
                    if (repeat == 1) {
                        remainedTime = episode.getDuration();
                        repeat = 0;
                    } else if (repeat == 2) {
                        remainedTime = episode.getDuration();
                    } else {
                        remainedTime = 0;
                        finshed = true;
                        paused = true;
                        episode = null;
                        podcast = null;
                    }
                } else {
                    episode = podcast.getEpisodes().get(currentItem++);
                    remainedTime = episode.getDuration();
                }
                break;
            case 2:
                if (currentItem == playlist.getSongs().size()) {
                    if (repeat == 1) {
                        if (shuffle) {
                            song = playlist.getSongs().get(shuffleOrder.get(0));
                        } else {
                            song = playlist.getSongs().get(0);
                        }
                        remainedTime = song.getDuration();
                    } else if (repeat == 2) {
                        remainedTime = song.getDuration();
                    } else {
                        remainedTime = 0;
                        finshed = true;
                        paused = true;
                        playlist = null;
                        song = null;
                    }
                } else {
                    song = playlist.getSongs().get(currentItem++);
                    remainedTime = song.getDuration();
                }
                break;
        }
    }

    public void stop() {
        episode = null;
        podcast = null;
        song = null;
        playlist = null;
        paused = true;
        remainedTime = 0;
        repeat = 0;
        finshed = true;
    }

    public String getRepeatState() {
        if (type == 2) {
            switch (repeat) {
                case 0:
                    return "no repeat.";
                case 1:
                    return "repeat all.";
                case 2:
                    return "repeat current song.";
            }
        } else {
            switch (repeat) {
                case 0:
                    return "no repeat.";
                case 1:
                    return "repeat once.";
                case 2:
                    return "repeat infinite.";
            }
        }
        return "";
    }

    public boolean isFinshed() {
        return finshed;
    }

    public int getType() {
        return type;
    }

    public SongInput getSong() {
        return song;
    }

    public PodcastInput getPodcast() {
        return podcast;
    }

    public int getRemainedTime() {
        return remainedTime;
    }

    public int getCurrentItem() {
        return currentItem;
    }
}
