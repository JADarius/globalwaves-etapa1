package utils;

import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

@Getter
public final class Player {
    private static final int SONG_PLAYER = 0;
    private static final int PODCAST_PLAYER = 1;
    private static final int PLAYLIST_PLAYER = 2;
    private static final int TIME_SKIP = 90;
    private static final int REPEAT_TYPES = 3;
    private SongInput song = null;
    private PodcastInput podcast = null;
    private Playlist playlist = null;
    private EpisodeInput episode = null;
    private int remainedTime;
    private boolean paused;
    private int repeat;
    private boolean finished;
    private int type; // 0 - song, 1 - podcast, 2 - playlist
    private ArrayList<Integer> shuffleOrder;
    private boolean shuffle;
    private int currentItem;


    private Player() {
        this.paused = false;
        this.repeat = 0;
        this.finished = false;
        this.shuffle = false;
        this.currentItem = 0;
    }

    public Player(final SongInput song) {
        this();
        this.song = song;
        this.remainedTime = song.getDuration();
        this.type = SONG_PLAYER;
    }

    public Player(final PodcastInput podcast) {
        this();
        this.podcast = podcast;
        this.episode = podcast.getEpisodes().get(0);
        this.remainedTime = episode.getDuration();
        this.type = PODCAST_PLAYER;
    }

    public Player(final PodcastSave save) {
        this();
        this.podcast = save.getPodcast();
        this.currentItem = save.getCurrentItem();
        this.episode = podcast.getEpisodes().get(currentItem);
        this.remainedTime = save.getRemainedTime();
        this.type = PODCAST_PLAYER;
    }

    public Player(final Playlist playlist) {
        this();
        this.playlist = playlist;
        this.song = playlist.getSongs().get(0);
        this.remainedTime = song.getDuration();
        this.type = PLAYLIST_PLAYER;
    }

    /**
     * Switches between the play and pause modes
     * @return Returns a string representing the result.
     */
    public String playPause() {
        if (finished) {
            return "Please load a source before attempting to pause or resume playback.";
        }
        paused = !paused;
        if (paused) {
            return "Playback paused successfully.";
        } else {
            return "Playback resumed successfully.";
        }
    }

    /**
     * Switches between the repeat modes
     */
    public void repeat() {
        repeat = (repeat + 1) % REPEAT_TYPES;
    }

    /**
     * Updates the player with how much time has elapsed from the last command
     * @param difference the amount of time elapsed
     */
    public void update(int difference) {
        if (!paused && !finished) {
            while (remainedTime < difference) {
                difference -= remainedTime;
                next();
                if (finished) {
                    break;
                }
            }
            if (!finished && difference > 0) {
                remainedTime -= difference;
                if (remainedTime == 0) {
                    next();
                }
            }
        }
    }

    /**
     * Gets the status of the player
     * @return The status of the player
     */
    public Stats status() {
        if (finished) {
            return new Stats("", remainedTime, repeat, shuffle, paused, type);
        } else {
            if (type == SONG_PLAYER || type == PLAYLIST_PLAYER) {
                return new Stats(song.getName(), remainedTime, repeat, shuffle, paused, type);
            } else {
                return new Stats(episode.getName(), remainedTime, repeat, shuffle, paused, type);
            }
        }
    }

    /**
     * Changes between the normal and shuffle mode of the playlist
     * @param seed The seed which is used in the randomization
     */
    public void shuffle(final int seed) {
        if (!shuffle) {
            Random rand = new Random(seed);
            shuffleOrder = new ArrayList<>();
            for (int index = 0; index < playlist.getSongs().size(); index++) {
                shuffleOrder.add(index);
            }
            Collections.shuffle(shuffleOrder, rand);
            shuffle = true;
            for (int index = 0; index < playlist.getSongs().size(); index++) {
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

    /**
     * Makes the player change to the next song/episode if possible
     */
    public void next() {
        switch (type) {
            case SONG_PLAYER:
                if (repeat == 1) {
                    remainedTime = song.getDuration();
                    repeat = 0;
                } else if (repeat == 2) {
                    remainedTime = song.getDuration();
                } else {
                    stop();
                }
                break;
            case PODCAST_PLAYER:
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
            case PLAYLIST_PLAYER:
                if (currentItem == playlist.getSongs().size() - 1
                        || (shuffle && currentItem == shuffleOrder.size() - 1)) {
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
            default:
                break;
        }
    }

    /**
     * Makes the player change to the previous song/episode
     * @return The name of the song/episode that is playing after the operation
     */
    public String prev() {
        paused = false;
        if (type == PODCAST_PLAYER) {
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

    /**
     * Stops the player and removes any source from it
     */
    public void stop() {
        episode = null;
        podcast = null;
        song = null;
        playlist = null;
        paused = true;
        remainedTime = 0;
        repeat = 0;
        finished = true;
        shuffle = false;
    }

    /**
     * Gets the repeat state
     * @return a string representing the repeat state
     */
    public String getRepeatState() {
        if (type == PLAYLIST_PLAYER) {
            switch (repeat) {
                case 0:
                    return "no repeat.";
                case 1:
                    return "repeat all.";
                case 2:
                    return "repeat current song.";
                default:
                    return "";
            }
        } else {
            switch (repeat) {
                case 0:
                    return "no repeat.";
                case 1:
                    return "repeat once.";
                case 2:
                    return "repeat infinite.";
                default:
                    return "";
            }
        }
    }

    /**
     * Skips the player 90 seconds
     */
    public void forward() {
        if (remainedTime <= TIME_SKIP) {
            next();
            remainedTime = episode.getDuration();
        } else {
            remainedTime -= TIME_SKIP;
        }
    }

    /**
     * Rewinds the player 90 seconds or to the start of the song/episode if 90 seconds didn't pass
     */
    public void backward() {
        if (remainedTime + TIME_SKIP >= episode.getDuration()) {
            remainedTime = episode.getDuration();
        } else {
            remainedTime = remainedTime + TIME_SKIP;
        }
    }
}
