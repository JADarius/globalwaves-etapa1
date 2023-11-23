package fileio.input;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
