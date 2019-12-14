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
    public void testSwapTurn() {
        Game game = new Game(new GameRules(), new Board());
        assertEquals(game.getWhosTurnItIs(), Stone.COLOR.BLACK);
        game.swapWhosTurnItIs();
        assertEquals(game.getWhosTurnItIs(), Stone.COLOR.WHITE);
    }


}
