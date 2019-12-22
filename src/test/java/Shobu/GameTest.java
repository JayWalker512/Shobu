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
        game.swapWhosTurnItIs();
        assertEquals(game.getWhosTurnItIs(), Stone.COLOR.BLACK);
    }

    @Test
    public void testTurnIncrement() {
        Game game = new Game(new GameRules(), new Board(true));
        assertEquals(0, game.getTurnNumber());
        game.takeTurn(new Turn(
                new Move(
                        new Vector2(4,7), new Vector2(0,-1)
                ),
                new Move(
                        new Vector2(0,3), new Vector2(0,-1)
                )));
        assertEquals(1, game.getTurnNumber());
    }

    @Test
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
        System.out.println(game.getBoard().toString());
        Turn whitesTurn = new Turn(
                new Move(
                        new Vector2(1, 0), new Vector2(-1, 1)
                ),
                new Move(
                        new Vector2(5, 0), new Vector2(-1, 1)
                ));
        assertTrue(game.takeTurn(whitesTurn));
        System.out.println(game.getBoard().toString());

        // Now check that the black stone we pushed off is no longer on the board
        assertEquals(15, Utilities.countStonesOfColorOnBoard(game.getBoard(), Stone.COLOR.BLACK));
        assertEquals(16, Utilities.countStonesOfColorOnBoard(game.getBoard(), Stone.COLOR.WHITE));

        // Now attempt to make an illegal move, ensure that it is not validated and the next game state is correct.
        String gameState = game.toString();
        Turn illegalBlackTurn = new Turn(
                new Move(
                        new Vector2(2, 7), new Vector2(-2, 0)
                ),
                new Move(
                        new Vector2(6, 3), new Vector2(-2, 0)
                ));
        assertFalse(game.takeTurn(illegalBlackTurn));
        assertEquals(gameState, game.toString());
        assertEquals(game.getWhosTurnItIs(), Stone.COLOR.BLACK);
    }

    @Test
    public void testWinCondition() {

    }

}
