package fileio.output;

import fileio.input.CommandInput;

import java.util.ArrayList;

public class SearchPlaylistOutput extends GenericOutput {
    private ArrayList<PlaylistOutput> result;

    public SearchPlaylistOutput(CommandInput query) {
        this.command = query.getCommand();
        this.user = query.getUsername();
        this.timestamp = query.getTimestamp();
        this.result = new ArrayList<>();
    }

    public ArrayList<PlaylistOutput> getResult() {
        return result;
    }

    public void setResult(ArrayList<PlaylistOutput> result) {
        this.result = result;
    }
}
