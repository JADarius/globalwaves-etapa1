package utils;

import fileio.input.PodcastInput;
import lombok.Getter;

@Getter
public class PodcastSave {
    private final PodcastInput podcast;
    private final int remainedTime;
    private final int currentItem;

    public PodcastSave(PodcastInput podcast, int remainedTime, int currentItem) {
        this.podcast = podcast;
        this.remainedTime = remainedTime;
        this.currentItem = currentItem;
    }

}
