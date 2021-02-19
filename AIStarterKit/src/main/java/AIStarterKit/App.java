package AIStarterKit;

import Shobu.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.*;
import java.util.*;

public class App {

    // Initialized in the main() method.
    static List<Vector2> allPossibleHeadings = null;

    public static List<Vector2> getPossibleHeadings() {
        if (allPossibleHeadings == null) {
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
        return allPossibleHeadings;
    }

    public static List<Stone> getStonesOfColorOnOtherColorBoards(Board board, Stone.COLOR myColor, Vector2 origin) {
        List<Stone> stonesOnOtherColorBoard = new ArrayList<>();
        int originalQuadrant = board.getQuadrant(origin);
        for (Stone s : board.getStones()) {
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

            // TODO does this method even need to be in here?
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
            if (game.getRules().validateTurn(game, board, t).getErrors().size() == 0) {
                legalTurns.add(t);
            }
        }

        // Return list of legal turns
        return Collections.unmodifiableList(legalTurns);
    }

    public static long calculateHeuristicBoardValueForColor(Board board, Stone.COLOR color) {
        long whiteStones = board.getStones().stream().filter((s) -> {
            return (s.getColor() == Stone.COLOR.WHITE);
        }).count();
        long blackStones = board.getStones().stream().filter((s) -> {
            return (s.getColor() == Stone.COLOR.BLACK);
        }).count();
        long valueForBlack = blackStones - whiteStones;
        return (color == Stone.COLOR.BLACK ? valueForBlack : -valueForBlack);
    }

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

        // Initialize constants
        allPossibleHeadings = getPossibleHeadings();

        InputStream in = new BufferedInputStream(System.in);

        BufferedWriter logWriter = null;
        if (args.length == 1) {
            try {
                logWriter = new BufferedWriter(new FileWriter(args[0], false));
            } catch (Exception e) {
                return;
            }
        }

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

                // Enumerate legal Turns
                List<Turn> legalTurns = enumerateLegalTurns(shobuGame);
                log(logWriter,"finished enumerating legal turns.");

                // Choose a Turn to play
                Turn chosenTurn;
                long maxValue = -Long.MAX_VALUE;
                if (legalTurns.size() > 0) {
                    chosenTurn = legalTurns.get(new Random().nextInt(legalTurns.size())); // to ensure it's set
                    for (Turn turn : legalTurns) {
                        Game tempGame = new Game(shobuGame);
                        tempGame.takeTurn(turn);
                        long value = calculateHeuristicBoardValueForColor(tempGame.getBoard(), shobuGame.getWhosTurnItIs());
                        if (value >= maxValue) { chosenTurn = turn; }
                    };
                    log(logWriter,"finished choosing a turn to play from legal turns.");

                    // Send turn back to Engine as JSON
                    System.out.println("{\"type\": \"turn\", \"payload\": " + chosenTurn.toJson() + "}");
                    log(logWriter, "Sent to engine: " + "{\"type\": \"turn\", \"payload\": " + chosenTurn.toJson() + "}");
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