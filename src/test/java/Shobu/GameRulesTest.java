package Shobu;

import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class GameRulesTest {

    @Test
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
        b = new GameRules().transitionBoard(b, t);
        assertEquals(Stone.COLOR.BLACK, b.getStone(new Vector2(0,0)).getColor());
        assertEquals(1, Utilities.countStonesOfColorOnBoard(b, Stone.COLOR.BLACK));
    }

    @Test
    public void testTurnValidation() {
        Game g = new Game(new GameRules(), new Board(true));

        // Validate that passive and aggressive moves cannot have different headings
        Turn t = new Turn(
                new Move(new Vector2(0, 7), new Vector2(0, 1)), // bottom left board
                new Move(new Vector2(4, 7), new Vector2(1,1)) // bottom right board
        );
        assertTrue(0 != g.getRules().validateTurn(g, g.getBoard(), t).getErrors().size());

        // Validate that passive move must be played on the current players "home board"
        // Illegal turn
        t = new Turn(
                new Move(new Vector2(0, 3), new Vector2(0, -1)), // top left board
                new Move(new Vector2(4, 7), new Vector2(0,-1)) // bottom right board
        );
        assertTrue(0 != g.getRules().validateTurn(g, g.getBoard(), t).getErrors().size());

        // Legal turn
        t = new Turn(
                new Move(new Vector2(0, 7), new Vector2(0, -1)), // bottom left board
                new Move(new Vector2(4, 3), new Vector2(0,-1)) // top right board
        );
        assertEquals(0, g.getRules().validateTurn(g, g.getBoard(), t).getErrors().size());

        // Switch to white's turn. The previous turn is no longer legal.
        g.swapWhosTurnItIs();
        assertTrue(0 != g.getRules().validateTurn(g, g.getBoard(), t).getErrors().size());

        t = new Turn(
                new Move(new Vector2(0, 0), new Vector2(0, -1)), // top left board
                new Move(new Vector2(4, 0), new Vector2(0,-1)) // top right board
        );
        assertEquals(0, g.getRules().validateTurn(g, g.getBoard(), t).getErrors().size());

        // Validate that passive and aggressive moves must be played on different colored boards (different horizontal board)
        g = new Game(new GameRules(), new Board(true));
        t = new Turn(
                new Move(new Vector2(0, 7), new Vector2(0, 1)), // bottom left board
                new Move(new Vector2(0, 3), new Vector2(0,1)) // top left board
        );
        assertTrue( 0 != g.getRules().validateTurn(g, g.getBoard(), t).getErrors().size());
    }

}
