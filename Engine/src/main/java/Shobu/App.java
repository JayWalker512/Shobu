/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package Shobu;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.*;

public class App {

    private static class GameAndTurn {
        public Game game;
        public Turn turn;

        public boolean isValid() {
            if (null != game && null != turn) {
                return true;
            }
            return false;
        }

        public static GameAndTurn fromJsonReader(JsonReader jsonReader) {
            GameAndTurn gameAndTurn = new GameAndTurn();
            try {
                JsonToken nextToken = jsonReader.peek();
                if(!JsonToken.BEGIN_OBJECT.equals(nextToken)) { return null; }
                jsonReader.beginObject();

                while(jsonReader.hasNext()) {
                    nextToken = jsonReader.peek();

                    if (nextToken == JsonToken.NAME) {
                        String name  =  jsonReader.nextName();

                        if (name.equals("game_state")) {
                            gameAndTurn.game = Game.fromJsonReader(jsonReader, new GameRules());
                        } else if (name.equals("turn")) {
                            gameAndTurn.turn = Turn.fromJsonReader(jsonReader);
                        } else {
                            return null; // unrecognized part of a turn
                        }
                    }
                }
                jsonReader.endObject();
                return gameAndTurn;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new GameAndTurn();
        }
    }

    private static void jsonPassThrough() {
        InputStream inputStream = new BufferedInputStream(System.in);
        String nextJson = Utilities.getNextJsonFromInputStream(inputStream);
        GameAndTurn gameAndTurn = GameAndTurn.fromJsonReader(new JsonReader(new StringReader(nextJson)));
        if (false == gameAndTurn.isValid()) {
            System.out.println("Invalid game and turn input!");
            return;
        }
        //make Game from game state
        //DONE above

        //validate turn against game state
        Turn validatedTurn = gameAndTurn.game.getRules().validateTurn(gameAndTurn.game, gameAndTurn.game.getBoard(), gameAndTurn.turn);

        //if valid, transition board and output JSON
        if (validatedTurn.getErrors().size() == 0) {
            gameAndTurn.game.takeTurn(validatedTurn);
            System.out.println(gameAndTurn.game.toJson());
        } else {
            //else, return Turn JSON with errors
            System.out.println(validatedTurn.toJson());
        }
    }

    public static void main(String[] args) {
        // Validate command line arguments
        Options programOptions = new Options(args);
        if (programOptions.isValid() == false) {
            System.out.println("Invalid arguments provided");
            return;
        }

        if (programOptions.jsonPassThrough()) {
            jsonPassThrough();
            return;
        }

        //System.out.println("Arguments: " + Arrays.asList(args));

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

        // List of JSON representations of game states/turns
        List<String> gameStatesJSON = new ArrayList<>();
        List<String> turnsJSON = new ArrayList<>();
        String winnerName = "";

        try {
            while (shobuGame.getRules().getWinner(shobuGame, shobuGame.getBoard()) == null && shobuGame.getTurnNumber() < 1000) {
                // TODO output the new game state JSON here
                gameStatesJSON.add(shobuGame.toJson());

                //System.out.println(shobuGame.getWhosTurnItIs().toString() + " player's turn (" + shobuGame.getTurnNumber() + ")");

                // Send gamestate JSON to AI whos turn it is
                aiController.sendStringToSubprocess(colorToProcessMap.get(shobuGame.getWhosTurnItIs()), Utilities.wrapJsonWithContainer("game_state", shobuGame.toJson()));

                // Get their turn response (TODO this needs a timeout to die if there's a problem)
                String turnJson = aiController.getNextJsonFromSubprocess(colorToProcessMap.get(shobuGame.getWhosTurnItIs()));
                if (turnJson == null) {
                    //System.out.println("Subprocess died. Ending game.");
                    aiController.killSubprocesses();
                    return;
                }

                // Parse JSON turn response
                if (Utilities.isTurnPayload(turnJson)) {
                    JsonReader turnJsonReader = Utilities.unwrapTurnJsonObject(turnJson);
                    Turn aiTurn = Turn.fromJsonReader(turnJsonReader);
                    Turn validatedTurn = shobuGame.getRules().validateTurn(shobuGame, shobuGame.getBoard(), aiTurn);
                    if (validatedTurn.getErrors().size() != 0) {
                        for (String e : validatedTurn.getErrors()) {
                            //System.out.println(e);
                        }
                    } else { // Turn was valid, advance the game state
                        // TODO output JSON turn here
                        turnsJSON.add(validatedTurn.toJson());

                        //System.out.println(shobuGame.getWhosTurnItIs().toString() + " plays " + validatedTurn.toString());
                        shobuGame.takeTurn(validatedTurn);
                        //System.out.println(shobuGame.getBoard().toString());
                    }
                }
            }
            if (shobuGame.getRules().getWinner(shobuGame, shobuGame.getBoard()) == null) {
                //System.out.println("Winner is: NONE");
            } else {
                // TODO output JSON winner here
                winnerName = shobuGame.getRules().getWinner(shobuGame, shobuGame.getBoard()).toString();
                //System.out.println("Winner is: " + winnerName);
            }
        } catch (Exception e) {
            // Kill subprocesses
            aiController.killSubprocesses();
            System.err.println("The Shobu game engine encountered an error: " + e.toString() + "\n" + "Error message: " + e.getMessage() + "\n");
            throw e; // just so we can see the stack trace.
        }
        aiController.killSubprocesses();

        outputGameDataAsJSON(gameStatesJSON, turnsJSON, winnerName);
    }

    private static void outputGameDataAsJSON(List<String> gameStates, List<String> turns, String winnerName) {
        System.out.print("{\"game_states\": [");
        ListIterator<String> stateIter = gameStates.listIterator();
        while (stateIter.hasNext()) {
            if (stateIter.nextIndex() != gameStates.size() - 1) {
                System.out.print(stateIter.next() + ",");
            } else {
                System.out.print(stateIter.next() + "],");
            }
        }

        System.out.print("\"turns\": [");
        ListIterator<String> turnIter = turns.listIterator();
        while (turnIter.hasNext()) {
            if (turnIter.nextIndex() != turns.size() - 1) {
                System.out.print(turnIter.next() + ",");
            } else {
                System.out.print(turnIter.next() + "],");
            }
        }

        System.out.print("\"winner\": \"" + winnerName + "\"}");
    }
}
