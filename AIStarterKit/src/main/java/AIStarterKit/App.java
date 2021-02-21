package AIStarterKit;

import Shobu.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.*;
import java.util.*;

public class App {

    private static void log(BufferedWriter logWriter, String s) {
        try {
            if (logWriter != null) {
                logWriter.write(s + "\n");
                logWriter.flush();
            }
        } catch (Exception e) {
            // do nothing
        }
    }

    public static JsonReader getGameStatesArrayFromJsonReader(JsonReader jsonReader) {
        try {
            JsonToken nextToken = jsonReader.peek();
            if (nextToken != JsonToken.BEGIN_OBJECT) { return null; } // invalid!
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                nextToken = jsonReader.peek();
                if (nextToken == JsonToken.NAME) {
                    String name = jsonReader.nextName();
                    if (name.equals("game_states")) {
                        jsonReader.beginArray();
                        return jsonReader;
                    }
                } else {
                    return null;
                }
            }
            jsonReader.endObject();
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    public static Game getNextGameFromArrayInJsonReader(JsonReader jsonReader) {
        try {
            JsonToken nextToken = jsonReader.peek();
            if (nextToken != JsonToken.BEGIN_OBJECT) { return null; } // invalid!
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                return Game.fromJsonReader(jsonReader, new GameRules());
            }
            jsonReader.endObject();
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    public static void main(String args[]) {

        InputStream in = new BufferedInputStream(System.in);

        BufferedWriter logWriter = null;
        /* if (args.length == 1) {
            try {
                logWriter = new BufferedWriter(new FileWriter(args[0], false));
            } catch (Exception e) {
                return;
            }
        } */

        // TODO Option set to read game states & output features for each state
        boolean option = false;
        if (option) {
            JsonReader jsonReader = new JsonReader(new InputStreamReader(in));
            jsonReader = getGameStatesArrayFromJsonReader(jsonReader);
            Game nextGame = null;
            for (nextGame = getNextGameFromArrayInJsonReader(jsonReader); nextGame != null; nextGame = getNextGameFromArrayInJsonReader(jsonReader)) {
                nextGame.getBoard().toString();
                // TODO calculate features
            }
            // TODO Output features to file
        }

        boolean canPlay = true;
        AIStrategy strategy = new HeuristicStrategy();
        // TODO need proper argument parsing to handle log file, strategy selection, etc.
        if (args.length == 1 && args[0].equals("random")) {
            strategy = new RandomStrategy();
        }
        while (canPlay) {
            // Read game state JSON
            String nextJson = Utilities.getNextJsonFromInputStream(in);
            if (nextJson == null) { // input stream ended
                canPlay = false;
                return;
            }
            if (Utilities.isGameStatePayload(nextJson)) {
                JsonReader jsonReader = Utilities.unwrapGameStateJsonObject(nextJson);
                log(logWriter,"finished unwrapping GameState");

                // GameState object doesn't keep track of rules, so we provide that here.
                Game shobuGame = Game.fromJsonReader(jsonReader, new GameRules());
                log(logWriter,"finished creating Game object from JSON");

                // Choose a Turn to play
                Optional<Turn> chosenTurn = strategy.getTurnToPlay(shobuGame);
                if (chosenTurn.isPresent()) {
                    // Send turn back to Engine as JSON
                    /* TODO add a "log" field to each turn? Would make debugging AI choices a little easier.
                     * But also slow things down a lot & decrease parallelism of this process writing by itself while the
                     * engine is working. But keeping everything in one file would be nice.
                     */
                    System.out.println("{\"type\": \"turn\", \"payload\": " + chosenTurn.get().toJson() + "}");
                    log(logWriter, "Sent to engine: " + "{\"type\": \"turn\", \"payload\": " + chosenTurn.get().toJson() + "}");
                } else {
                    log(logWriter, "Have no legal turns: must abdicate.");
                    canPlay = false;
                    return;
                }
            }
        }
        if (logWriter != null) {
            try {
                logWriter.close();
            } catch (Exception e) {
                // do nothing
            }
        }
    }
}