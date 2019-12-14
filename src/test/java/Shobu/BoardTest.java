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
}
