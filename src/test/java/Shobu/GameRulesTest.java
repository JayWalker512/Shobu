package Shobu;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class GameRulesTest {

    @Test
    public void testMoveIntersection() {

        // set up a mock board
        Board b = new Board();
        b.setStone(new Vector2(0,0), new Stone(0, Stone.COLOR.WHITE));
        b.setStone(new Vector2(1,1), new Stone(1, Stone.COLOR.WHITE));
        b.setStone(new Vector2(2,2), new Stone(2, Stone.COLOR.BLACK));

        // movement size 1
        Move m = new Move(new Vector2(2,2), new Vector2(-1, -1));
        List<Stone> intersected = GameRules.getStonesIntersected(b, m);
        assertEquals(1, intersected.size());

        // movement size 2
        m = new Move(new Vector2(2,2), new Vector2(-2, -2));
        intersected = GameRules.getStonesIntersected(b, m);
        assertEquals(2, intersected.size());

        for (Stone s : intersected) {
            assertEquals(Stone.COLOR.WHITE, s.getColor());
        }
    }

}
