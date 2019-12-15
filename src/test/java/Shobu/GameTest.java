package Shobu;

import org.junit.Test;
import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void testGameInitializes() {
        Game game = new Game(new GameRules(), new Board());
        assertEquals(game.getWhosTurnItIs(), Stone.COLOR.BLACK);
    }

    @Test
    public void testTurnSwap() {
        Game game = new Game(new GameRules(), new Board());
        assertEquals(game.getWhosTurnItIs(), Stone.COLOR.BLACK);
        game.swapWhosTurnItIs();
        assertEquals(game.getWhosTurnItIs(), Stone.COLOR.WHITE);
    }

    @Test
    public void testTurnIncrement() {
        Game game = new Game(new GameRules(), new Board());
        assertEquals(0, game.getTurnNumber());
        game.takeTurn(new Turn(
                new Move(
                        new Vector2(0,0), new Vector2(0,0)
                ),
                new Move(
                        new Vector2(0,0), new Vector2(0,0)
                )));
        assertEquals(1, game.getTurnNumber());
    }

    private int countStonesOfColorOnBoard(Board b, Stone.COLOR c) {
        int sum = 0;
        for (int x = 0; x < b.getDimensions().x; x++) {
            for (int y = 0; y < b.getDimensions().y; y++) {
                Stone s = b.getStone(new Vector2(x,y));
                if (s != null && s.getColor() == c) {
                    sum += 1;
                }
            }
        }
        return sum;
    }

    @Test
    public void testSequenceOfTurns() {
        Game game = new Game(new GameRules(), new Board());
        Turn blacksTurn = new Turn(
                new Move(
                        new Vector2(0, 7), new Vector2(0, -2)
                ),
                new Move(
                        new Vector2(4, 3), new Vector2(0, -2)
                ));
        game.takeTurn(blacksTurn);
        Turn whitesTurn = new Turn(
                new Move(
                        new Vector2(1, 0), new Vector2(-1, -1)
                ),
                new Move(
                        new Vector2(5, 0), new Vector2(-1, -1)
                ));
        game.takeTurn(whitesTurn);

        // Now check that the black stone we pushed off is no longer on the board
        assertEquals(15, countStonesOfColorOnBoard(game.getBoard(), Stone.COLOR.BLACK));
        assertEquals(16, countStonesOfColorOnBoard(game.getBoard(), Stone.COLOR.WHITE));
    }

}
