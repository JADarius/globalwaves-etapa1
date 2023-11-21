package utils;

import fileio.input.PodcastInput;

public class PodcastSave {
    private PodcastInput podcast;
    private int remainedTime;
    private int currentItem;

    public PodcastSave(PodcastInput podcast, int remainedTime, int currentItem) {
        this.podcast = podcast;
        this.remainedTime = remainedTime;
        this.currentItem = currentItem;
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
