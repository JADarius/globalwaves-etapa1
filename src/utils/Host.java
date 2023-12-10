package utils;

import fileio.input.CommandInput;
import lombok.Getter;
import utils.library.Podcast;

import java.util.ArrayList;

public final class Host implements GenericUser {
    @Getter
    private String name;
    private int age;
    private String city;
    private ArrayList<Podcast> podcasts;
    private Enums.UserType userType;

    public Host(CommandInput commandInput) {
        this.name = commandInput.getName();
        this.age = commandInput.getAge();
        this.city = commandInput.getCity();
        this.userType = Enums.UserType.HOST;
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
        return null;
    }

    @Override
    public String removePodcast(CommandInput commandInput) {
        return null;
    }

    @Override
    public String addAnnouncement(CommandInput commandInput) {
        return null;
    }

    @Override
    public String removeAnnouncement(CommandInput commandInput) {
        return null;
    }

    @Override
    public String switchConnectionStatus() {
        return name + " is not a normal user.";
    }
}
