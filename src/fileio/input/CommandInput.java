package fileio.input;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
public final class CommandInput {
    private String command;
    private String username;
    private int timestamp;
    private String type;
    private FilterInput filters;
    private int itemNumber;
    private int seed;
    private int playlistId;
    private String playlistName;
    private int age;
    private String city;
    private ArrayList<SongInput> songs;
    private String name;
    private int releaseYear;
    private String description;
    private String date;
    private int price;
    private ArrayList<EpisodeInput> episodes;
    private String nextPage;
}
