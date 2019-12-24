package Shobu;

import com.google.gson.stream.JsonReader;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.*;

public class Vector2Test {
    @Test
    public void testFromJsonReader() {
        String vectorJson = "{\"x\":1, \"y\":2} the rest of the garbage here will be ignored {\"bla\": 999}";
        Vector2 parsedVector = Vector2.fromJsonReader(new JsonReader(new StringReader(vectorJson)));
        assertTrue(null != parsedVector);
        assertEquals(1, parsedVector.x);
        assertEquals(2,parsedVector.y);

        vectorJson = "   {  \"y\":2,   \"x\":    1    }";
        parsedVector = Vector2.fromJsonReader(new JsonReader(new StringReader(vectorJson)));
        assertTrue(null != parsedVector);
        assertEquals(1, parsedVector.x);
        assertEquals(2,parsedVector.y);
    }
}
