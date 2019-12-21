package Shobu;

import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class GameRulesTest {

    @Test
    @Ignore
    public void testTransitionBoard() {

        // Test scenario 1
        Board b = new Board(false);
        StoneFactory sf = StoneFactory.getInstance();

        // Test x pushes o from board.
        // o...|....
        // .x..|....
        // ....|....
        // ....|....
        // ---------
        // ....|....
        // ....|....
        // ....|....
        // ....|....

        b.setStone(new Vector2(0,0), sf.createStone(Stone.COLOR.WHITE));
        b.setStone(new Vector2(1,1), sf.createStone(Stone.COLOR.BLACK));

        Turn t = new Turn(
                new Move(new Vector2(1,1), new Vector2(0,0)), //passive
                new Move(new Vector2(1,1), new Vector2(-1,-1)) //aggressive
        );

        // Move stone(s) and ensure the black one is the only one that remains in place of the white.
        b = GameRules.transitionBoard(b, t);
        assertEquals(Stone.COLOR.BLACK, b.getStone(new Vector2(0,0)));
        assertEquals(1, b.getStones().size());
    }

}
