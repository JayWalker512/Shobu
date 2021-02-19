package AIStarterKit;

import Shobu.Board;
import Shobu.Vector2;

import java.awt.*;

/**
 * This class provides a series of methods to calculate various scalar "features" that would be useful as input to
 * an ML algorithm, or heuristic based strategy.
 */
public class Features {

    // TODO add number of ways stone at (x,y) can be pushed off
    public static int dangerCount(Board board, Vector2 position, Color opponentColor) {
        return 0;
    }

    // TODO add number of ways a stone at (x,y) can push an opposing stone off
    public static int attackCount(Board board, Vector2 position, Color opponentColor) {
        return 0;
    }
}
