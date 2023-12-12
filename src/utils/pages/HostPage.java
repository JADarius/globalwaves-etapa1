package utils.pages;

import fileio.input.EpisodeInput;
import utils.accounts.Host;
import utils.library.Podcast;
import utils.library.goodies.Announcement;

import java.util.ArrayList;
import java.util.List;

public class HostPage implements PageObserver {
    private final Host owner;
    private List<String> podcasts;
    private List<String> announcements;

    public HostPage(final Host owner) {
        this.owner = owner;
        update();
    }

    public String getOutput() {
        return "Podcasts:\\n\\t"
                + podcasts
                + "\\n\\nAnnouncements:\\n\\t"
                + announcements;
    }

    public void setPodcasts() {
        podcasts = new ArrayList<>();
        for (Podcast podcast : owner.getPodcasts()) {
            List<String> episodes = new ArrayList<>();
            for (EpisodeInput episodeInput : podcast.getEpisodes()) {
                episodes.add(episodeInput.getName() + " - " + episodeInput.getDescription());
            }
            podcasts.add(podcast.getName() + ":\\n\\t" + episodes);
        }
    }

    public void setAnnouncements() {
        announcements = new ArrayList<>();
        for (Announcement announcement : owner.getAnnouncements()) {
            announcements.add(announcement.getName() + "\\n\\t" + announcement.getDescription() + "\\n");
        }
    }

    @Override
    public void update() {
        setPodcasts();
        setAnnouncements();
    }
}
