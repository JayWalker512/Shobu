package AIStarterKit;

import Shobu.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.*;
import java.util.*;

import static java.util.Random.*;

public class App {

    /** This method reads the output from STDIN until it determines that it has received a complete
     * JSON object. It does this by matching curly braces { } and with no other method or validation of the JSON
     * structure. That's up to the caller! It then returns the String containing this object.
     * @return
     */
    public static String getNextJsonFromInputStream(InputStream in) {
        StringBuilder sb = new StringBuilder();
        InputStream processOutput = in;
        int byteRead = 0;
        try {
            boolean objectStarted = false;
            boolean objectFinished = false;
            Stack<Character> stack = new Stack<>();

            while (objectFinished == false) {
                byteRead = processOutput.read();
                char asciiCharRead = (char)byteRead;
                AIController.JSON_DELIMITERS delimReceived = AIController.matchCurlyBraces(stack, asciiCharRead);
                if (delimReceived == AIController.JSON_DELIMITERS.STARTED_OBJECT) {
                    objectStarted = true;
                }
                if (delimReceived == AIController.JSON_DELIMITERS.FINISHED_OBJECT) {
                    objectFinished = true;
                }
                if (objectStarted) {
                    sb.append(asciiCharRead);
                }
            }
        } catch (Exception e) {
            return "";
        }
        return sb.toString();
    }

    /**
     * This method checks if a JSON string provided is A. a JSON object and B. has a property "type" equal
     * to "turn". That is all, it does not validate if a payload is present or do any other kind of checks.
     * @param jsonInput
     * @return true if the json object has "type":"turn", false otherwise.
     */
    public static boolean isGameStatePayload(String jsonInput) {
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
                        if (value.equals("gamestate")) {
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

    public static JsonReader unwrapGameStateJsonObject(String jsonInput) {
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

    public static List<Vector2> getPossibleHeadings() {
        List<Vector2> headings = new ArrayList<>();
        headings.add(new Vector2(-2, -2)); // up left
        headings.add(new Vector2(0, -2)); // up
        headings.add(new Vector2(2, -2)); // up right
        headings.add(new Vector2(-2, 0)); // left
        headings.add(new Vector2(2, 0)); // right
        headings.add(new Vector2(-2, 2)); // down left
        headings.add(new Vector2(0, 2)); // down
        headings.add(new Vector2(2, 2)); // down right

        headings.add(new Vector2(-1, -1)); // up left
        headings.add(new Vector2(0, -1)); // up
        headings.add(new Vector2(1, -1)); // up right
        headings.add(new Vector2(-1, 0)); // left
        headings.add(new Vector2(1, 0)); // right
        headings.add(new Vector2(-1, 1)); // down left
        headings.add(new Vector2(0, 1)); // down
        headings.add(new Vector2(1, 1)); // down right
        return Collections.unmodifiableList(headings);
    }

    public static List<Stone> getStonesOfColorOnOtherColorBoards(Board board, Stone.COLOR myColor, Vector2 origin) {
        List<Stone> stonesOnOtherColorBoard = new ArrayList<>();
        int originalQuadrant = board.getQuadrant(origin);
        for (Stone s : board.getStones()) {
            if (s == null) { continue; }
            Vector2 stoneLocation = board.getStoneLocation(s.getId());
            int stoneQuadrant = board.getQuadrant(stoneLocation);
            if (originalQuadrant == 0 || originalQuadrant == 2) {
                if (stoneQuadrant == 1 || stoneQuadrant == 3) {
                    stonesOnOtherColorBoard.add(s);
                }
            } else if (originalQuadrant == 1 || originalQuadrant == 3) {
                if (stoneQuadrant == 0 || stoneQuadrant == 2) {
                    stonesOnOtherColorBoard.add(s);
                }
            }
        }
        return stonesOnOtherColorBoard;
    }

    public static List<Turn> enumerateLegalTurns(Game game) {
        Board board = game.getBoard();
        Stone.COLOR whosTurnItIs = game.getWhosTurnItIs();
        // Enumerate possible headings (from function)
        List<Vector2> possibleHeadings = getPossibleHeadings();

        // For each own stone in home board (passive):
        List<Stone> ownStonesOnHomeBoard = board.getStonesOfColorOnHomeBoard(whosTurnItIs);
        List<Move> possiblePassiveMoves = new ArrayList<>();
        for (Stone s : ownStonesOnHomeBoard) {
            // Make a Move() for each possible heading
            for (Vector2 heading : possibleHeadings) {
                possiblePassiveMoves.add(new Move(board.getStoneLocation(s.getId()), heading));
            }
        }

        // For possible (passive) move on home board stones:
        List<Turn> possibleTurns = new ArrayList<>();
        for (Move m : possiblePassiveMoves) {
            // For each own stone on other color board:
            List<Stone> validAggressiveStones = getStonesOfColorOnOtherColorBoards(board, whosTurnItIs, m.getOrigin());
            for (Stone s : validAggressiveStones) {
                // Make a Turn() pairing possible passive move with matching active move origin+heading
                possibleTurns.add(new Turn(m, new Move(board.getStoneLocation(s.getId()), m.getHeading())));
            }
        }

        // For each potentially possible Turn that's been created:
        List<Turn> legalTurns = new ArrayList<>();
        for (Turn t : possibleTurns) {
            // Validate turns and add them to a list of legal turns
            if (game.getRules().validateTurn(game, board, t).getErrors().size() != 0) {
                legalTurns.add(t);
            }
        }

        // Return list of legal turns
        return Collections.unmodifiableList(legalTurns);
    }

    public static void main(String args[]) {
        System.out.println("AI Starter Kit!");

        InputStream in = System.in;
        if (args.length == 0) {
            in = System.in;
        } else {
            try {
                in = new FileInputStream(args[0]);
            } catch (FileNotFoundException e) {
                System.out.println("Couldn't open file: " + args[0]);
                return;
            }
        }

        while (true) {
            // Read game state JSON
            String nextJson = App.getNextJsonFromInputStream(in);
            if (isGameStatePayload(nextJson)) {
                JsonReader jsonReader = unwrapGameStateJsonObject(nextJson);
                GameState g = GameState.fromJsonReader(jsonReader);

                // GameState object doesn't keep track of rules, so we provide that here.
                Game shobuGame = new Game(new GameRules(), g.getBoard(), g.getWhosTurnIsIt(), g.getTurnNumber());

                // Enumerate legal Turns
                List<Turn> legalTurns = enumerateLegalTurns(shobuGame);

                // Choose a Turn to play
                Turn chosenTurn = legalTurns.get(new Random().nextInt(legalTurns.size()));

                // Send turn back to Engine as JSON
                System.out.println(chosenTurn.toJson());
            }
        }
    }
}