package utils;

import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Player {
    @Getter
    private SongInput song = null;
    @Getter
    private PodcastInput podcast = null;
    @Getter
    private Playlist playlist = null;
    @Getter
    private EpisodeInput episode = null;
    @Getter
    private int remainedTime;
    private boolean paused;
    private int repeat;
    @Getter
    private boolean finshed;
    @Getter
    private int type; // 0 - song, 1 - podcast, 2 - playlist
    private ArrayList<Integer> shuffleOrder;
    @Getter
    private boolean shuffle;
    @Getter
    private int currentItem;


    private Player() {
        this.paused = false;
        this.repeat = 0;
        this.finshed = false;
        this.shuffle = false;
        this.currentItem = 0;
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
        this.episode = podcast.getEpisodes().get(currentItem);
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
                    next();
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
        if (!shuffle) {
            Random rand = new Random(seed);
            shuffleOrder = new ArrayList<>();
            for (int index = 0; index < playlist.getSongs().size(); index++) {
                shuffleOrder.add(index);
            }
            Collections.shuffle(shuffleOrder, rand);
            shuffle = true;
            for(int index = 0; index < playlist.getSongs().size(); index++) {
                if (shuffleOrder.get(index) == currentItem) {
                    currentItem = index;
                    break;
                }
            }
        } else {
            shuffle = false;
            for (int index = 0; index < playlist.getSongs().size(); index++) {
                if (playlist.getSongs().get(index).getName().equals(song.getName())) {
                    currentItem = index;
                }
            }
        }
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
                    stop();
                }
                break;
            case 1:
                if (currentItem == podcast.getEpisodes().size() - 1) {
                    if (repeat == 1) {
                        remainedTime = episode.getDuration();
                        repeat = 0;
                    } else if (repeat == 2) {
                        remainedTime = episode.getDuration();
                    } else {
                        stop();
                    }
                } else {
                    episode = podcast.getEpisodes().get(++currentItem);
                    remainedTime = episode.getDuration();
                    paused = false;
                }
                break;
            case 2:
                if (currentItem == playlist.getSongs().size() - 1 || (shuffle && currentItem == shuffleOrder.size() - 1)) {
                    if (repeat == 1) {
                        if (shuffle) {
                            song = playlist.getSongs().get(shuffleOrder.get(0));
                        } else {
                            song = playlist.getSongs().get(0);
                        }
                        remainedTime = song.getDuration();
                        currentItem = 0;
                    } else if (repeat == 2) {
                        remainedTime = song.getDuration();
                    } else {
                        stop();
                    }
                } else {
                    if (repeat != 2) {
                        if (shuffle) {
                            song = playlist.getSongs().get(shuffleOrder.get(++currentItem));
                        } else {
                            song = playlist.getSongs().get(++currentItem);
                        }
                    }
                    remainedTime = song.getDuration();
                    paused = false;
                }
                break;
        }
    }

    public String prev() {
        paused = false;
        if (type == 1) {
            if (remainedTime < episode.getDuration()) {
                remainedTime = episode.getDuration();
            } else {
                if (currentItem != 0) {
                    episode = podcast.getEpisodes().get(--currentItem);
                    remainedTime = episode.getDuration();
                }
            }
            return episode.getName() + ".";
        } else {
            if (remainedTime < song.getDuration()) {
                remainedTime = song.getDuration();
            } else {
                if (currentItem != 0) {
                    if (shuffle) {
                        song = playlist.getSongs().get(shuffleOrder.get(--currentItem));
                    } else {
                        song = playlist.getSongs().get(--currentItem);
                    }
                    remainedTime = song.getDuration();
                }
            }
        }
        return song.getName() + ".";
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
        shuffle = false;
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

    public void forward() {
        if (remainedTime <= 90) {
            next();
            remainedTime = episode.getDuration();
        } else {
            remainedTime -= 90;
        }
    }

    public void backward() {
        if (remainedTime + 90 >= episode.getDuration()) {
            remainedTime = episode.getDuration();
        } else {
            remainedTime = remainedTime + 90;
        }
    }
}
