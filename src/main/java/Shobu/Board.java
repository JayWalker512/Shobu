package Shobu;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Board {
    private List<Stone> stones;
    private Vector2 dimensions;

    public Board(Board b) {
        this.dimensions = b.getDimensions();
        this.stones = b.getStones();
    }

    public Board(boolean initializeDefaultStones) {
        this.stones = new ArrayList<Stone>();
        this.dimensions = new Vector2(8,8); // call the other constructor
        for (int i = 0; i < 8 * 8; i++) {
            this.stones.add(null);
        }

        if (initializeDefaultStones) {
            // Put the stones on the board in the default initial configuration.
            StoneFactory sf = StoneFactory.getInstance();
            for (int x = 0; x < this.dimensions.x; x++) {
                this.setStone(new Vector2(x, 0), sf.createStone(Stone.COLOR.WHITE));
                this.setStone(new Vector2(x, 4), sf.createStone(Stone.COLOR.WHITE));
                this.setStone(new Vector2(x, 3), sf.createStone(Stone.COLOR.BLACK));
                this.setStone(new Vector2(x, 7), sf.createStone(Stone.COLOR.BLACK));
            }
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

    public Vector2 getStoneLocation(int id) {
        int index = this.getStoneListIndex(id);
        int x = index % this.dimensions.x;
        int y = Math.floorDiv(index, this.dimensions.x);
        return new Vector2(x, y);
    }

    private int getStoneListIndex(int id) {
        for (int i = 0; i < this.stones.size(); i++) {
            Stone s = this.stones.get(i);
            if (s != null && s.getId() == id) {
                // Didn't use .indexOf because it throws exception with null elements in the list.
                return i;
            }
        }
        return -1; // couldn't find it
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

    public void pushStones(Move m) {
        // If it's not orthogonal or diagonal, this algorithm won't push the stones correctly so we bail.
        if (m.isValid() == false) {
            return;
        }

        // Get the stones intersected by a move
        List<Stone> intersectedStones = Utilities.getStonesIntersected(this, m);
        for (Stone s : intersectedStones) {
            // Push the stones relative to any we intersected. This is how a long line can get pushed.
            Move intersectedMove = new Move(this.getStoneLocation(s.getId()), m.getHeading());
            this.pushStones(intersectedMove);
        }

        // Since intersections have been dealt with and moved previously, now we can move self.
        this.moveStone(m.getOrigin(), m.getOrigin().add(m.getHeading()));
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

        this.setStone(from, null); // remove the stone from it's original location
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
