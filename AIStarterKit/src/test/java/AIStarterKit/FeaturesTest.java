package AIStarterKit;

import Shobu.Board;
import Shobu.Vector2;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class FeaturesTest {
    @Test
    public void testInitialBoardFeatures() {
        Board initialBoard = new Board(true);

        assertEquals(0, Features.attackCount(initialBoard, new Vector2(0,0), Color.BLACK));
        assertEquals(0, Features.dangerCount(initialBoard, new Vector2(0, 0), Color.BLACK));

        assertEquals(2, Features.attackCount(initialBoard, new Vector2(0,2), Color.BLACK));
        assertEquals(1, Features.dangerCount(initialBoard, new Vector2(0, 2), Color.BLACK));
    }
}
