package Shobu.AIStarterKit;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for misc methods that don't necessarily fit nicely into another class. Can be refactored later.
 */
public class Utilities {

    static int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }

    public static List<Stone> getStonesIntersected(Board board, Move move) {
        if (move.isValid() == false) {
            return null; // This algorithm doesn't make sense for invalid moves.
        }

        int xAdjust = Utilities.clamp(move.getHeading().x, -1, 1);
        int yAdjust = Utilities.clamp(move.getHeading().y, -1, 1);
        List<Stone> stones = new ArrayList<>();
        Vector2 currentScanLocation = new Vector2(move.getOrigin()).add(new Vector2(xAdjust, yAdjust));

        // This is a pretty fragile algorithm begging for an infinite loop...
        while (true) {
            if (board.getQuadrant(currentScanLocation) != board.getQuadrant(move.getOrigin())) { return stones; }
            Stone intersected = board.getStone(currentScanLocation);
            if (intersected != null) {
                stones.add(intersected);
            }
            if (currentScanLocation.equals(move.getOrigin().add(move.getHeading()))) { break; }
            currentScanLocation = new Vector2(currentScanLocation).add(new Vector2(xAdjust, yAdjust));
        }
        return stones;
    }

    public static int countStonesOfColorOnBoard(Board b, Stone.COLOR c) {
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
}
