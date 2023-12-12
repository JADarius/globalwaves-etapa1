package utils.accounts;

import fileio.input.CommandInput;
import fileio.input.SongInput;
import lombok.Getter;
import utils.Admin;
import utils.Enums;
import utils.library.Album;
import utils.library.Song;
import utils.library.goodies.Event;
import utils.library.goodies.Merch;
import utils.pages.ArtistPage;
import utils.pages.PageObserver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Artist implements GenericUser {
    @Getter
    private final String name;
    private final int age;
    private final String city;
    @Getter
    private final List<Album> albums;
    @Getter
    private final List<Event> events;
    @Getter
    private final List<Merch> merch;
    @Getter
    private final PageObserver page;

    public Artist(final CommandInput commandInput) {
        this.name = commandInput.getName();
        this.age = commandInput.getAge();
        this.city = commandInput.getCity();
        this.albums = new ArrayList<>();
        this.events = new ArrayList<>();
        this.merch = new ArrayList<>();
        this.page = new ArtistPage(this);
    }

    private boolean checkForDuplicates(CommandInput commandInput) {
        Set<String> songSet = new HashSet<>();
        for (SongInput songInput : commandInput.getSongs()) {
            if (songSet.contains(songInput.getName())) {
                return true;
            }
            songSet.add(songInput.getName());
        }
        return false;
    }

    private Album searchForAlbum(String name) {
        for (Album album : albums) {
            if (album.getName().equals(name)) {
                return album;
            }
        }
        return null;
    }

    private Event searchForEvent(String name) {
        for (Event event : events) {
            if (event.getName().equals(name)) {
                return event;
            }
        }
        return null;
    }

    private boolean searchForMerch(String name) {
        for (Merch merch_listing : merch) {
            if (merch_listing.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDate(String date) {
        if (!date.matches("(\\d\\d)-(\\d\\d)-(\\d\\d\\d\\d)")) {
            return false;
        }
        String[] list = date.split("-");
        int day = Integer.parseInt(list[0]);
        int month = Integer.parseInt(list[1]);
        int year = Integer.parseInt(list[2]);
        return !((month == 2 && day > 28) || (day > 31) || (month > 12) || (year < 1900) || (year > 2023));
    }

    @Override
    public String addAlbum(CommandInput commandInput) {
        if (searchForAlbum(commandInput.getName()) != null) {
            return name + " has another album with the same name";
        }
        if (checkForDuplicates(commandInput)) {
            return name + " has the same song at least twice in this album.";
        }
        List<Song> songs = new ArrayList<>();
        for (SongInput songInput : commandInput.getSongs()) {
            songs.add(new Song(songInput, Enums.CreationType.ARTIST));
        }
        Album newAlbum = new Album(commandInput.getName(), songs, name, commandInput.getReleaseYear(), commandInput.getDescription());
        Admin.addAlbum(newAlbum);
        albums.add(newAlbum);
        page.update();
        return name + " has added new album successfully.";
    }

    @Override
    public String removeAlbum(CommandInput commandInput) {
        Album query = searchForAlbum(commandInput.getName());
        if (query == null) {
            return name + " doesn't have an album with the given name.";
        }
        if (Admin.checkIfUsingAlbum(query)) {
            return name + " can't delete this album.";
        }
        Admin.removeAlbum(query);
        albums.remove(query);
        page.update();
        return name + " deleted the album successfully.";
    }

    @Override
    public String addEvent(CommandInput commandInput) {
        if (searchForEvent(commandInput.getName()) != null) {
            return name + " has another event with the same name.";
        }
        if (checkDate(commandInput.getDate())) {
            return "Event for " + name + " does not have a valid date.";
        }
        Event newEvent = new Event(commandInput.getName(), commandInput.getDate(), commandInput.getDescription());
        events.add(newEvent);
        page.update();
        return name + " has added new event successfully.";
    }

    @Override
    public String removeEvent(CommandInput commandInput) {
        if (searchForEvent(commandInput.getName()) == null) {
            return name + " doesn't have an event with the given name.";
        }
        events.remove(searchForEvent(commandInput.getName()));
        page.update();
        return name + " deleted the event successfully.";
    }

    @Override
    public String addMerch(CommandInput commandInput) {
        if (searchForMerch(commandInput.getName())) {
            return name + " has merchandise with the same name.";
        }
        if (commandInput.getPrice() < 0) {
            return "Price for merchandise can not be negative.";
        }
        merch.add(new Merch(commandInput.getName(), commandInput.getPrice(), commandInput.getDescription()));
        page.update();
        return name + " has added new merchandise successfully.";
    }

    @Override
    public String addPocast(CommandInput commandInput) {
        return name + " is not a host.";
    }

    @Override
    public String removePodcast(CommandInput commandInput) {
        return name + " is not a host.";
    }

    @Override
    public String addAnnouncement(CommandInput commandInput) {
        return name + " is not a host.";
    }

    @Override
    public String removeAnnouncement(CommandInput commandInput) {
        return name + " is not a host.";
    }

    @Override
    public String switchConnectionStatus() {
        return name + " is not a normal user.";
    }

}
