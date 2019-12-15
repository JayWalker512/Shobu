package Shobu;

import org.junit.Test;

import static org.junit.Assert.*;

public class MoveTest {

    @Test
    public void testValidMoves() {
        Move validMove = new Move(new Vector2(0,0), new Vector2(2,2));
        assertTrue(validMove.isValid());

        Move invalidMove = new Move(new Vector2(0,0), new Vector2(1, 2));
        assertTrue(!invalidMove.isValid());
    }

}
