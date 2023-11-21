package fileio.output;

import fileio.input.CommandInput;

import java.util.ArrayList;

public class ShowLikedOutput extends GenericOutput {
    ArrayList<String> result;

    public ShowLikedOutput(CommandInput query) {
        this.command = query.getCommand();
        this.user = query.getUsername();
        this.timestamp = query.getTimestamp();
        this.result = new ArrayList<>();
    }

    public ArrayList<String> getResult() {
        return result;
    }

    public void setResult(ArrayList<String> result) {
        this.result = result;
    }
}
