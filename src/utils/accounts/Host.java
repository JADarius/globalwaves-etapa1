package utils.accounts;

import fileio.input.CommandInput;
import fileio.input.EpisodeInput;
import lombok.Getter;
import utils.Admin;
import utils.Enums;
import utils.library.Podcast;
import utils.library.goodies.Announcement;
import utils.pages.HostPage;
import utils.pages.PageObserver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Host implements GenericUser {
    @Getter
    private final String name;
    private final int age;
    private final String city;
    @Getter
    private final List<Podcast> podcasts;
    @Getter
    private final List<Announcement> announcements;
    @Getter
    private final PageObserver page;

    public Host(CommandInput commandInput) {
        this.name = commandInput.getName();
        this.age = commandInput.getAge();
        this.city = commandInput.getCity();
        this.podcasts = new ArrayList<>();
        this.announcements = new ArrayList<>();
        this.page = new HostPage(this);
    }

    private Podcast searchForPodcast(String name) {
        for (Podcast podcast : podcasts) {
            if (podcast.getName().equals(name)) {
                return podcast;
            }
        }
        return null;
    }

    private boolean checkForDuplicates(CommandInput commandInput) {
        Set<String> podcastSet = new HashSet<>();
        for (EpisodeInput episodeInput : commandInput.getEpisodes()) {
            if (podcastSet.contains(episodeInput.getName())) {
                return true;
            }
            podcastSet.add(episodeInput.getName());
        }
        return false;
    }

    private Announcement searchForAnnouncement(String name) {
        for (Announcement announcement : announcements) {
            if (announcement.getName().equals(name)) {
                return announcement;
            }
        }
        return null;
    }

    @Override
    public String addAlbum(CommandInput commandInput) {
        return name + " is not an artist.";
    }

    @Override
    public String removeAlbum(CommandInput commandInput) {
        return name + " is not an artist.";
    }

    @Override
    public String addEvent(CommandInput commandInput) {
        return name + " is not an artist.";
    }

    @Override
    public String removeEvent(CommandInput commandInput) {
        return name + " is not an artist.";
    }

    @Override
    public String addMerch(CommandInput commandInput) {
        return name + " is not an artist.";
    }

    @Override
    public String addPocast(CommandInput commandInput) {
        String name = commandInput.getName();
        if (searchForPodcast(name) != null) {
            return this.name + " has another podcast with the same name.";
        }
        if (checkForDuplicates(commandInput)) {
            return name + " has the same episode in this podcast.";
        }
        Podcast newPodcast = new Podcast(name, this.name, commandInput.getEpisodes());
        podcasts.add(newPodcast);
        Admin.addPodcast(newPodcast);
        page.update();
        return this.name + " has added new podcast successfully.";
    }

    @Override
    public String removePodcast(CommandInput commandInput) {
        Podcast query = searchForPodcast(commandInput.getName());
        if (query == null) {
            return name + " doesn't have a podcast with the given name.";
        }
        if (Admin.checkIfUsingPodcast(query)) {
            return name + " can't delete this podcast.";
        }
        Admin.removePodcast(query);
        podcasts.remove(query);
        page.update();
        return name + " deleted the podcast successfully.";
    }

    @Override
    public String addAnnouncement(CommandInput commandInput) {
        if (searchForAnnouncement(commandInput.getName()) != null) {
            return name + " has already added an announcement with this name.";
        }
        Announcement newAnnouncement = new Announcement(commandInput.getName(), commandInput.getDescription());
        announcements.add(newAnnouncement);
        page.update();
        return name + " has successfully added new announcement.";
    }

    @Override
    public String removeAnnouncement(CommandInput commandInput) {
        Announcement query = searchForAnnouncement(commandInput.getName());
        if (query == null) {
            return name + " has no announcement with the given name.";
        }
        announcements.remove(query);
        page.update();
        return name + " has successfully deleted the announcement.";
    }

    @Override
    public String switchConnectionStatus() {
        return name + " is not a normal user.";
    }
}
