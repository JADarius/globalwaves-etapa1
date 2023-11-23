package fileio.output;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GenericOutput {
    protected String command;
    protected String user;
    protected int timestamp;
}
