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

    public Board(Board b) {
        this.dimensions = b.getDimensions();
        this.stones = b.getStones();
    }

    public Board() {
        // TODO FIXME maybe I want the option of initializing a board with no stones for tests?

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

    public Vector2 getDimensions() {
        return new Vector2(dimensions);
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
        // bounds check
        int index = location.x + (location.y * this.dimensions.x);
        if (index >= this.getDimensions().x * this.getDimensions().y || index < 0) {
            return null;
        }

        if (this.stones.get(index) != null) {
            return new Stone(this.stones.get(index));
        }
        return null;
    }

    public List<Stone> getStones() {
        //System.out.println("called getStones()");
        List<Stone> stonesList = new ArrayList<>();
        for (Stone s : this.stones) {
            if (s != null) {
                //System.out.println(s.toString());
                stonesList.add(new Stone(s));
            } else {
                //System.out.println("Added a null element.");
                stonesList.add(s); // null
            }

        }
        return stonesList;
    }

    // TODO FIXME should this return a new board rather than modifying in place?
    public void moveStone(Vector2 from, Vector2 to) {
        Stone stoneToMove = this.getStone(from);
        if (stoneToMove == null) { return; }

        // check if the stone is being moved across board boundaries. If so, delete it.
        if (from.x > 3 && to.x <= 3 || from.x <= 3 && to.x > 3) {
            this.setStone(from, null);
            return;
        }
        if (from.y > 3 && to.y <= 3 || from.y <= 3 && to.y > 3) {
            this.setStone(from, null);
            return;
        }

        this.setStone(to, stoneToMove);
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
