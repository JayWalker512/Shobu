package AIStarterKit;

import Shobu.App;
import Shobu.GameState;
import Shobu.Stone;
import com.google.gson.stream.JsonReader;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.*;

public class GameStateTest {

    @Test
    public void testGameStateJson() {
        String gameStateJson = "{\n" +
                "  \"type\": \"gamestate\",\n" +
                "  \"payload\": {\n" +
                "    \"board\": \"oooooooo................xxxxxxxxoooooooo................xxxxxxxx\",\n" +
                "    \"turn\": \"BLACK\",\n" +
                "    \"turnNumber\": 0\n" +
                "  }\n" +
                "}";

        GameState gs = GameState.fromJsonReader(App.unwrapGameStateJsonObject(gameStateJson));
        assertNotNull(gs);
        assertEquals(Stone.COLOR.BLACK, gs.getWhosTurnIsIt());
        assertEquals(0, gs.getTurnNumber());

    }
}
