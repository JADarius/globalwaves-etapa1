package utils;

import fileio.input.EpisodeInput;
import lombok.Getter;
import utils.library.Podcast;
import utils.library.Song;
import utils.library.SongCollection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Getter
public final class Player {
    private static final int TIME_SKIP = 90;
    private static final int REPEAT_TYPES = 3;
    private Song song = null;
    private Podcast podcast = null;
    private SongCollection playlist = null;
    private EpisodeInput episode = null;
    private int remainedTime;
    private boolean paused;
    private int repeat;
    private boolean finished;
    private Enums.PlayerType type;
    private List<Integer> shuffleOrder;
    private boolean shuffle;
    private int currentItem;


    private Player() {
        this.paused = false;
        this.repeat = 0;
        this.finished = false;
        this.shuffle = false;
        this.currentItem = 0;
    }

    public Player(final Song song) {
        this();
        this.song = song;
        this.remainedTime = song.getDuration();
        this.type = Enums.PlayerType.SONG;
    }

    public Player(final Podcast podcast) {
        this();
        this.podcast = podcast;
        this.episode = podcast.getEpisodes().get(0);
        this.remainedTime = episode.getDuration();
        this.type = Enums.PlayerType.PODCAST;
    }

    public Player(final PodcastSave save) {
        this();
        this.podcast = save.getPodcast();
        this.currentItem = save.getCurrentItem();
        this.episode = podcast.getEpisodes().get(currentItem);
        this.remainedTime = save.getRemainedTime();
        this.type = Enums.PlayerType.PODCAST;
    }

    public Player(final SongCollection playlist) {
        this();
        this.playlist = playlist;
        this.song = playlist.getSongs().get(0);
        this.remainedTime = song.getDuration();
        this.type = Enums.PlayerType.PLAYLIST;
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
            if (type == Enums.PlayerType.SONG || type == Enums.PlayerType.PLAYLIST) {
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
            case SONG:
                if (repeat == 1) {
                    remainedTime = song.getDuration();
                    repeat = 0;
                } else if (repeat == 2) {
                    remainedTime = song.getDuration();
                } else {
                    stop();
                }
                break;
            case PODCAST:
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
            case PLAYLIST:
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
        if (type == Enums.PlayerType.PODCAST) {
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
        if (type == Enums.PlayerType.PLAYLIST) {
            return switch (repeat) {
                case 0 -> "no repeat.";
                case 1 -> "repeat all.";
                case 2 -> "repeat current song.";
                default -> "";
            };
        } else {
            return switch (repeat) {
                case 0 -> "no repeat.";
                case 1 -> "repeat once.";
                case 2 -> "repeat infinite.";
                default -> "";
            };
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
