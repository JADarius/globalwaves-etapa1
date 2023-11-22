package fileio.output;

import fileio.input.CommandInput;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public final class ShowLikedOutput extends GenericOutput {
    ArrayList<String> result;

    public ShowLikedOutput(CommandInput query) {
        this.command = query.getCommand();
        this.user = query.getUsername();
        this.timestamp = query.getTimestamp();
        this.result = new ArrayList<>();
    }

    public void setResult(ArrayList<String> result) {
        this.result = result;
    }
}
