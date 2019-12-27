package Shobu;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class UtilitiesTest {

    @Test
    public void testMoveIntersection() {

        // set up a mock board
        Board b = new Board(false);
        b.setStone(new Vector2(0,0), new Stone(0, Stone.COLOR.WHITE));
        b.setStone(new Vector2(1,1), new Stone(1, Stone.COLOR.WHITE));
        b.setStone(new Vector2(2,2), new Stone(2, Stone.COLOR.BLACK));

        // movement size 1
        Move m = new Move(new Vector2(2,2), new Vector2(-1, -1));
        List<Stone> intersected = Utilities.getStonesIntersected(b, m);
        assertEquals(1, intersected.size());
        assertFalse(2 == intersected.get(0).getId());

        // movement size 2
        m = new Move(new Vector2(2,2), new Vector2(-2, -2));
        intersected = Utilities.getStonesIntersected(b, m);
        assertEquals(2, intersected.size());

        for (Stone s : intersected) {
            assertEquals(Stone.COLOR.WHITE, s.getColor());
            assertFalse(2 == s.getId());
        }

        // Verify that we don't consider intersections across quadrant boundaries.
        b = new Board(false);
        b.setStone(new Vector2(3,3), new Stone(3, Stone.COLOR.WHITE));
        b.setStone(new Vector2(4,4), new Stone(4, Stone.COLOR.WHITE));
        m = new Move(new Vector2(3,3), new Vector2(1,1));
        intersected = Utilities.getStonesIntersected(b, m);
        assertEquals(0, intersected.size());
    }
}
