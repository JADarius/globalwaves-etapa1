package utils;

import fileio.input.CommandInput;
import lombok.Getter;
import utils.library.Album;

import java.util.ArrayList;

public final class Artist implements GenericUser {
    @Getter
    private String name;
    private int age;
    private String city;
    private Enums.UserType userType;
    private ArrayList<Album> albums;

    public Artist(CommandInput commandInput) {
        this.name = commandInput.getName();
        this.age = commandInput.getAge();
        this.city = commandInput.getCity();
        this.userType = Enums.UserType.ARTIST;
    }


    @Override
    public String addAlbum(CommandInput commandInput) {
        return null;
    }

    @Override
    public String removeAlbum(CommandInput commandInput) {
        return null;
    }

    @Override
    public String addEvent(CommandInput commandInput) {
        return null;
    }

    @Override
    public String removeEvent(CommandInput commandInput) {
        return null;
    }

    @Override
    public String addMerch(CommandInput commandInput) {
        return null;
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
