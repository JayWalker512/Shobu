package Shobu;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
    private Stone.COLOR whosTurnIsIt;
    private int turnNumber;

    private GameRules rules; // Remind me why I need this?
    private Board board;

    public GameRules getRules() {
        return new GameRules(rules);
    }

    public void setRules(GameRules rules) {
        this.rules = new GameRules(rules);
    }

    public Board getBoard() {
        return new Board(board);
    }

    public void setBoard(Board board) {
        this.board = new Board(board);
    }

    public Game(GameRules rules, Board board) {
        this.whosTurnIsIt = Stone.COLOR.BLACK;
        this.turnNumber = 0;
        this.rules = new GameRules(rules);
        this.board = new Board(board);
    }

    public Game(GameRules rules, Board board, Stone.COLOR whosTurnIsIt, int turnNumber) {
        this.whosTurnIsIt = whosTurnIsIt;
        this.turnNumber = turnNumber;
        this.rules = new GameRules(rules);
        this.board = new Board(board);
    }

    public Game(Game game) {
        this.whosTurnIsIt = game.getWhosTurnItIs();
        this.turnNumber = game.getTurnNumber();
        this.rules = new GameRules(game.getRules());
        this.board = new Board(game.getBoard());
    }

    public Stone.COLOR getWhosTurnItIs() {
        return whosTurnIsIt;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public boolean takeTurn(Turn turn) {
        Turn validatedTurn = rules.validateTurn(this, this.getBoard(), turn);
        if (validatedTurn.getErrors().size() != 0) {
            for (String e : validatedTurn.getErrors()) {
                System.out.println(e);
            }
            return false;
        }

        this.setBoard(rules.transitionBoard(this.getBoard(), turn));

        swapWhosTurnItIs();
        this.turnNumber++;

        return true;
    }

    void swapWhosTurnItIs() {
        if (this.whosTurnIsIt == Stone.COLOR.BLACK) {
            this.whosTurnIsIt = Stone.COLOR.WHITE;
            return;
        };

        this.whosTurnIsIt = Stone.COLOR.BLACK;
    }

    public List<Turn> enumerateLegalTurns() {
        Board board = this.getBoard();
        Stone.COLOR whosTurnItIs = this.getWhosTurnItIs();
        // Enumerate possible headings (from function)
        List<Vector2> possibleHeadings = Utilities.getPossibleHeadings();

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
            List<Stone> validAggressiveStones = Utilities.getStonesOfColorOnOtherColorBoards(board, whosTurnItIs, m.getOrigin());
            for (Stone s : validAggressiveStones) {
                // Make a Turn() pairing possible passive move with matching active move origin+heading
                possibleTurns.add(new Turn(m, new Move(board.getStoneLocation(s.getId()), m.getHeading())));
            }
        }

        // For each potentially possible Turn that's been created:
        List<Turn> legalTurns = new ArrayList<>();
        for (Turn t : possibleTurns) {
            // Validate turns and add them to a list of legal turns
            if (this.getRules().validateTurn(this, board, t).getErrors().size() == 0) {
                legalTurns.add(t);
            }
        }

        // Return list of legal turns
        return Collections.unmodifiableList(legalTurns);

    }

    public String toJson() {
        return "{ \"board\": \"" + this.board.toSerializedString() + "\", \"turn\": \"" + this.getWhosTurnItIs().toString() + "\", \"turnNumber\": " + Integer.toString(this.getTurnNumber()) + "}";
    }

    public static Game fromJsonReader(JsonReader jsonReader, GameRules rules) {
        String boardSerialized = null;
        Stone.COLOR turn = null;
        int turnNumber = 0;
        try {
            JsonToken nextToken = jsonReader.peek();
            if (nextToken != JsonToken.BEGIN_OBJECT) { return null; } // invalid!
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                nextToken = jsonReader.peek();
                if (nextToken == JsonToken.NAME) {
                    String name = jsonReader.nextName();
                    if (name.equals("board")) {
                        boardSerialized = jsonReader.nextString();
                        if (boardSerialized == null || boardSerialized.length() != 64) { return null; }
                    } else if (name.equals("turn")) {
                        String colorName = jsonReader.nextString();
                        if (colorName.equals(Stone.COLOR.BLACK.toString())) {
                            turn = Stone.COLOR.BLACK;
                        } else if (colorName.equals((Stone.COLOR.WHITE.toString()))) {
                            turn = Stone.COLOR.WHITE;
                        } else {
                            return null;
                        }
                    } else if (name.equals("turnNumber")){
                        turnNumber = jsonReader.nextInt();
                        if (turnNumber < 0) { return null; }
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            }
            jsonReader.endObject();

            if (boardSerialized == null || turn == null || turnNumber < 0) { return null; }
            return new Game(rules, Board.fromSerializedString(boardSerialized), turn, turnNumber);
        } catch (IOException e) {
            return null;
        }
    }
}
