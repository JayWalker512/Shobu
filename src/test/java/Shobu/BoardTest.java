package Shobu;

import org.junit.Test;
import static org.junit.Assert.*;

public class BoardTest {

    @Test
    public void testDefaultBoardConfiguration() {
        Board b = new Board();
        String defaultGameBoard = "oooo|oooo\n" + "    |    \n" + "    |    \n" + "xxxx|xxxx\n" + "----|----\n" + "oooo|oooo\n" + "    |    \n" + "    |    \n" + "xxxx|xxxx\n";
        assertEquals(defaultGameBoard, b.toString());
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
    public void testMovingAcrossBoundaries() {
        Board b = new Board(); // This has the default game board configuration, so we move an existing stone.
        b.moveStone(new Vector2(3,3), new Vector2(4, 3));
        assertEquals(15, countStonesOfColorOnBoard(b, Stone.COLOR.BLACK));
        assertEquals(16, countStonesOfColorOnBoard(b, Stone.COLOR.WHITE));
    }
}
