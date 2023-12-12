package fileio.output;

import lombok.Getter;
import lombok.Setter;
import utils.Enums;
import utils.library.Playlist;
import utils.library.Song;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public final class PlaylistOutput {
    private String name;
    private List<String> songs;
    private String visibility;
    private int followers;

    public PlaylistOutput(final Playlist playlist) {
        this.name = playlist.getName();
        this.visibility = playlist.getVisibility();
        this.followers = playlist.getFollowers();
        this.songs = new ArrayList<>();
        for (Song song : playlist.getSongs()) {
            this.songs.add(song.getName());
        }
    }
}
