package Shobu;

import com.google.gson.stream.JsonReader;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.*;

public class MoveTest {

    @Test
    public void testValidMoves() {
        Move validMove = new Move(new Vector2(0,0), new Vector2(2,2));
        assertTrue(validMove.isValid());

        Move invalidMove = new Move(new Vector2(0,0), new Vector2(1, 2));
        assertTrue(!invalidMove.isValid());
    }

    @Test
    public void testFromJsonReader() {
        String moveJson = "{\"origin\": {\"x\":1, \"y\":2}, \"heading\": {\"x\":1, \"y\":2} }";
        Move moveFromJson = Move.fromJsonReader(new JsonReader(new StringReader(moveJson)));
        assertNotNull(moveFromJson);
    }

}
