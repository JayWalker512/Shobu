package Shobu;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private List<Stone> stones;

    public Board(List<Stone> stones) {
        this.stones = new ArrayList<Stone>(stones);
    }
}
