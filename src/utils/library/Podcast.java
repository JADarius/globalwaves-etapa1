package utils.library;

import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Podcast {
    private final String name;
    private final String owner;
    private final List<EpisodeInput> episodes;

    public Podcast(PodcastInput podcast) {
        this.name = podcast.getName();
        this.owner = podcast.getOwner();
        this.episodes = podcast.getEpisodes();
    }

    public Podcast(final String name, final String owner, final List<EpisodeInput> episodes) {
        this.name = name;
        this.owner = owner;
        this.episodes = episodes;
    }
}
