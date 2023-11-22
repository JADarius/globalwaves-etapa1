package fileio.output;

import fileio.input.CommandInput;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public final class SearchPlaylistOutput extends GenericOutput {
    private ArrayList<PlaylistOutput> result;

    public SearchPlaylistOutput(CommandInput query) {
        this.command = query.getCommand();
        this.user = query.getUsername();
        this.timestamp = query.getTimestamp();
        this.result = new ArrayList<>();
    }

    public void setResult(ArrayList<PlaylistOutput> result) {
        this.result = result;
    }
}
