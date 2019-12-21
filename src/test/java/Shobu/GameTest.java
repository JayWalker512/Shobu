package Shobu;

import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void testGameInitializes() {
        Game game = new Game(new GameRules(), new Board(true));
        assertEquals(game.getWhosTurnItIs(), Stone.COLOR.BLACK);
    }

    @Test
    public void testTurnSwap() {
        Game game = new Game(new GameRules(), new Board(true));
        assertEquals(game.getWhosTurnItIs(), Stone.COLOR.BLACK);
        game.swapWhosTurnItIs();
        assertEquals(game.getWhosTurnItIs(), Stone.COLOR.WHITE);
    }

    @Test
    public void testTurnIncrement() {
        Game game = new Game(new GameRules(), new Board(true));
        assertEquals(0, game.getTurnNumber());
        game.takeTurn(new Turn(
                new Move(
                        new Vector2(0,7), new Vector2(0,-1)
                ),
                new Move(
                        new Vector2(0,3), new Vector2(0,-1)
                )));
        assertEquals(1, game.getTurnNumber());
    }

    @Test
    @Ignore //not ready to be used yet, needs TransitionBoard to work.
    public void testSequenceOfTurns() {
        Game game = new Game(new GameRules(), new Board(true));
        Turn blacksTurn = new Turn(
                new Move(
                        new Vector2(0, 7), new Vector2(0, -2)
                ),
                new Move(
                        new Vector2(4, 3), new Vector2(0, -2)
                ));
        assertTrue(game.takeTurn(blacksTurn));
        Turn whitesTurn = new Turn(
                new Move(
                        new Vector2(1, 0), new Vector2(-1, -1)
                ),
                new Move(
                        new Vector2(5, 0), new Vector2(-1, -1)
                ));
        assertTrue(game.takeTurn(whitesTurn));

        // Now check that the black stone we pushed off is no longer on the board
        assertEquals(15, Utilities.countStonesOfColorOnBoard(game.getBoard(), Stone.COLOR.BLACK));
        assertEquals(16, Utilities.countStonesOfColorOnBoard(game.getBoard(), Stone.COLOR.WHITE));
    }

}
