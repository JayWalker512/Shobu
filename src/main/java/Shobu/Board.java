package Shobu;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Board {
    private List<Stone> stones;
    private Vector2 dimensions;

    private class StoneFactory {
        private int i = 0;
        public Stone createStone(Stone.COLOR color) {
            Stone s = new Stone(this.i, color);
            this.i++;
            return s;
        }
    }

    public Board() {
        this.stones = new ArrayList<Stone>();
        this.dimensions = new Vector2(8,8); // call the other constructor
        for (int i = 0; i < 8 * 8; i++) {
            this.stones.add(null);
        }

        // Put the stones on the board in the default initial configuration.
        StoneFactory sf = new StoneFactory();
        for (int x = 0; x < this.dimensions.x; x++) {
            this.setStone(new Vector2(x, 0), sf.createStone(Stone.COLOR.WHITE));
            this.setStone(new Vector2(x, 4), sf.createStone(Stone.COLOR.WHITE));
            this.setStone(new Vector2(x, 3), sf.createStone(Stone.COLOR.BLACK));
            this.setStone(new Vector2(x, 7), sf.createStone(Stone.COLOR.BLACK));
        }
    }

    public boolean setStone(Vector2 location, Stone stone) {
        if (location.x >= this.dimensions.x || location.x < 0) {
            return false;
        }
        if (location.y >= this.dimensions.y || location.y < 0) {
            return false;
        }
        this.stones.set(location.x + (location.y * this.dimensions.x), stone);
        return true;
    }

    public Stone getStone(int id) {
        // Could use a hash table to make this faster. Optimize if necessary.
        for (Stone stone : this.stones) {
            if (null != stone && stone.getId() == id) {
                return new Stone(stone);
            }
        }
        return null;
    }

    public Stone getStone(Vector2 location) {
        return this.stones.get(location.x + (location.y * this.dimensions.x));
    }

    public String toString() {
        StringBuilder representation = new StringBuilder();

        for (int y = 0; y < this.dimensions.y; y++) {
            for (int x = 0; x < this.dimensions.x; x++) {
                Stone s = this.getStone(new Vector2(x, y));
                if (s == null) {
                    representation.append(' ');
                } else {
                    if (s.getColor() == Stone.COLOR.BLACK) {
                        representation.append('x');
                    }

                    if (s.getColor() == Stone.COLOR.WHITE) {
                        representation.append('o');
                    }
                }

                if (x == 3) {
                    representation.append('|');
                }
            }
            representation.append('\n');

            if (y == 3) {
                representation.append("----|----\n");
            }
        }

        return representation.toString();
    }
}
