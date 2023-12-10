package utils;

import fileio.input.CommandInput;

public interface GenericUser {
    public String getName();
    public String addAlbum(CommandInput commandInput);
    public String removeAlbum(CommandInput commandInput);
    public String addEvent(CommandInput commandInput);
    public String removeEvent(CommandInput commandInput);
    public String addMerch(CommandInput commandInput);
    public String addPocast(CommandInput commandInput);
    public String removePodcast(CommandInput commandInput);
    public String addAnnouncement(CommandInput commandInput);
    public String removeAnnouncement(CommandInput commandInput);
    public String switchConnectionStatus();
}
