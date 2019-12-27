package Shobu.AIStarterKit;

import com.google.gson.stream.JsonReader;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.*;

public class TurnTest {
    @Test
    public void testFromJsonReader() {
        String turnJson = "{\"passive\": {\"origin\": {\"x\":1, \"y\":2}, \"heading\": {\"x\":1, \"y\":2} }, \"aggressive\": {\"origin\": {\"x\":1, \"y\":2}, \"heading\": {\"x\":1, \"y\":2} } }";
        Turn turnFromJson = Turn.fromJsonReader(new JsonReader(new StringReader(turnJson)));
        assertTrue(turnFromJson != null);
        assertEquals(1, turnFromJson.getPassive().getOrigin().x);
        assertEquals(2, turnFromJson.getPassive().getOrigin().y);
        assertEquals(1, turnFromJson.getPassive().getHeading().x);
        assertEquals(2, turnFromJson.getPassive().getHeading().y);
        assertEquals(1, turnFromJson.getAggressive().getOrigin().x);
        assertEquals(2, turnFromJson.getAggressive().getOrigin().y);
        assertEquals(1, turnFromJson.getAggressive().getHeading().x);
        assertEquals(2, turnFromJson.getAggressive().getHeading().y);
    }
}
