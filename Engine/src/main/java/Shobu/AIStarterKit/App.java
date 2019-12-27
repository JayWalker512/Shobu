/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package Shobu.AIStarterKit;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

public class App {

    private static String getStdin() {
        Scanner in = new Scanner(System.in);
        String s = in.next();
        return s;
    }

    /**
     * This method checks if a JSON string provided is A. a JSON object and B. has a property "type" equal
     * to "turn". That is all, it does not validate if a payload is present or do any other kind of checks.
     * @param jsonInput
     * @return true if the json object has "type":"turn", false otherwise.
     */
    public static boolean isTurnPayload(String jsonInput) {
        JsonReader jsonReader = new JsonReader(new StringReader(jsonInput));
        try {
            JsonToken nextToken = jsonReader.peek();
            if(!JsonToken.BEGIN_OBJECT.equals(nextToken)) { return false; }
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                nextToken = jsonReader.peek();
                if (JsonToken.NAME.equals(nextToken)) {
                    String name = jsonReader.nextName();
                    if (name.equals("type")) {
                        String value = jsonReader.nextString();
                        if (value.equals("turn")) {
                            return true;
                        }
                    }
                }
                jsonReader.skipValue();
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    public static JsonReader unwrapTurnJsonObject(String jsonInput) {
        JsonReader jsonReader = new JsonReader(new StringReader(jsonInput));
        try {
            JsonToken nextToken = jsonReader.peek();
            if(!JsonToken.BEGIN_OBJECT.equals(nextToken)) { return null; }
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                nextToken = jsonReader.peek();
                if (JsonToken.NAME.equals(nextToken)) {
                    String name = jsonReader.nextName();
                    if (name.equals("payload")) {
                        return jsonReader;
                    }
                }
                jsonReader.skipValue();
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println("Arguments: " + Arrays.asList(args));
        ListIterator<String> argListIter = Arrays.asList(args).listIterator();

        // Validate command line arguments
        Options programOptions = new Options(args);
        if (programOptions.isValid() == false) {
            System.out.println("Invalid arguments provided");
            return;
        }

        // Determine method to execute AI subprocesses (java, python3, etc)
        // Start subprocesses and open IO pipes to them
        AIController aiController = new AIController(programOptions.getProgramNames(), programOptions.getExtensions());
        if (aiController.getErrors().size() != 0) {
            System.out.println("Failed to initialize AI controller.");
            return;
        }

        // Initialize Shobu game
        Game shobuGame = new Game(new GameRules(), new Board(true));

        // Map player colors to process number
        Map<Stone.COLOR, Integer> colorToProcessMap = new HashMap<>();
        colorToProcessMap.put(Stone.COLOR.BLACK, 0);
        colorToProcessMap.put(Stone.COLOR.WHITE, 1);

        while (shobuGame.getRules().getWinner(shobuGame, shobuGame.getBoard()) == null) {
            System.out.println(shobuGame.getWhosTurnItIs().toString() + " player's turn.");

            // Send gamestate JSON to AI whos turn it is
            aiController.sendStringToSubprocess(colorToProcessMap.get(shobuGame.getWhosTurnItIs()), shobuGame.toJson());

            // Get their turn response (TODO this needs a timeout to die if there's a problem)
            String turnJson = aiController.getNextJsonFromSubprocess(colorToProcessMap.get(shobuGame.getWhosTurnItIs()));

            // Parse JSON turn response
            if (isTurnPayload(turnJson)) {
                JsonReader turnJsonReader = unwrapTurnJsonObject(turnJson);
                Turn aiTurn = Turn.fromJsonReader(turnJsonReader);
                Turn validatedTurn = shobuGame.getRules().validateTurn(shobuGame, shobuGame.getBoard(), aiTurn);
                if (validatedTurn.getErrors().size() != 0) {
                    for (String e : validatedTurn.getErrors()) {
                        System.out.println(e);
                    }
                } else { // Turn was valid, advance the game state
                    shobuGame.takeTurn(validatedTurn);
                    System.out.println(shobuGame.getBoard().toString());
                }
            }
        }
        System.out.println("Winner is: " + shobuGame.getRules().getWinner(shobuGame, shobuGame.getBoard()).toString());
    }
}