package utils.library.goodies;

import lombok.Getter;

@Getter
public final class Merch {
    private final String name;
    private final int price;
    private final String description;

    public Merch(final String name, final int price, final String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }
}
