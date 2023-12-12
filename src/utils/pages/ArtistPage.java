package utils.pages;

import utils.accounts.Artist;
import utils.library.Album;
import utils.library.goodies.Event;
import utils.library.goodies.Merch;

import java.util.ArrayList;
import java.util.List;

public class ArtistPage implements PageObserver {
    private final Artist owner;
    private List<String> albums;
    private List<String> merch_list;
    private List<String> events;

    public ArtistPage(final Artist owner) {
        this.owner = owner;
        update();
    }

    public String getOutput() {
        return "Albums:\\n\\t"
                + albums
                + "\\n\\nMerch:\\n\\t"
                + merch_list
                + "\\n\\nEvent:\\n\\t"
                + events;
    }

    private void setAlbums() {
        albums = new ArrayList<>();
        for (Album album : owner.getAlbums()) {
            albums.add(album.getName());
        }
    }

    private void setEvents() {
        events = new ArrayList<>();
        for (Event event : owner.getEvents()) {
            events.add(event.getName() + " - " + event.getDate() + ":\\n\\t" + event.getDescription());
        }
    }

    private void setMerch() {
        merch_list = new ArrayList<>();
        for (Merch merch : owner.getMerch()) {
            merch_list.add(merch.getName() + " - " + merch.getPrice() + ":\\n\\t" + merch.getDescription());
        }
    }

    @Override
    public void update() {
        setAlbums();
        setEvents();
        setMerch();
    }
}
