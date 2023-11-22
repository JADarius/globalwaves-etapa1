package fileio.input;

import lombok.Getter;

@Getter
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

    public CommandInput() {
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setFilters(FilterInput filters) {
        this.filters = filters;
    }

    public void setItemNumber(int itemNumber) {
        this.itemNumber = itemNumber;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }
}
