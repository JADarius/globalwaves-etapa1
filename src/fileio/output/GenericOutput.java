package fileio.output;

import lombok.Getter;

@Getter
public class GenericOutput {
    protected String command;
    protected String user;
    protected int timestamp;

    public GenericOutput() {}

    public void setCommand(String command) {
        this.command = command;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}
