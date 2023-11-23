package fileio.input;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public final class EpisodeInput {
    private String name;
    private Integer duration;
    private String description;
}
