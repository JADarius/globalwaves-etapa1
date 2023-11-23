package fileio.input;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
public final class PodcastInput {
    private String name;
    private String owner;
    private ArrayList<EpisodeInput> episodes;
}
