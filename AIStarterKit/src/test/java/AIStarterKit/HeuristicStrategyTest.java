package AIStarterKit;

import Shobu.*;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertTrue;

public class HeuristicStrategyTest {
    @Test
    public void testHeuristicStrategyChoosesMoveThatWins() {
        StoneFactory sf = StoneFactory.getInstance();
        Board board = new Board(false);
        board.setStone(new Vector2(6, 0), sf.createStone(Stone.COLOR.WHITE));
        board.setStone(new Vector2(6, 1), sf.createStone(Stone.COLOR.BLACK));
        board.setStone(new Vector2(1, 6), sf.createStone(Stone.COLOR.BLACK));

        Game game = new Game(new GameRules(), board, Stone.COLOR.BLACK, 0);
        AIStrategy ai = new HeuristicStrategy();
        Optional<Turn> chosenTurn = ai.getTurnToPlay(game);
        assertTrue(chosenTurn.isPresent());
        System.out.println(chosenTurn.get().toString());
        assertTrue(chosenTurn.get().getPassive().getHeading().equals(new Vector2(0, -1)));
        assertTrue(chosenTurn.get().getAggressive().getHeading().equals(new Vector2(0, -1)));
    }
}
