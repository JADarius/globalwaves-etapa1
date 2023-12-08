package utils.library;

import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class Podcast {
    private final String name;
    private final String owner;
    private final ArrayList<EpisodeInput> episodes;

    public Podcast(PodcastInput podcast) {
        this.name = podcast.getName();
        this.owner = podcast.getOwner();
        this.episodes = podcast.getEpisodes();
    }
}
