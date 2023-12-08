package utils;

import lombok.Getter;
import utils.library.Podcast;

@Getter
public class PodcastSave {
    private final Podcast podcast;
    private final int remainedTime;
    private final int currentItem;

    public PodcastSave(final Podcast podcast, final int remainedTime, final int currentItem) {
        this.podcast = podcast;
        this.remainedTime = remainedTime;
        this.currentItem = currentItem;
    }

}
