package Shobu;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;

public class GameState {
    private Board board;
    private Stone.COLOR whosTurnIsIt;
    private int turnNumber;

    public Board getBoard() {
        return new Board(board);
    }

    public Stone.COLOR getWhosTurnIsIt() {
        return whosTurnIsIt;
    }

    public int getTurnNumber() { return turnNumber; }

    // Private constructor as GameState can only be built from JSON input.
    private GameState(Board board, Stone.COLOR whosTurnIsIt, int turnNumber) {
        this.board = new Board(board);
        this.whosTurnIsIt = whosTurnIsIt;
        this.turnNumber = turnNumber;
    }

    public static GameState fromJsonReader(JsonReader jsonReader) {
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
            return new GameState(Board.fromSerializedString(boardSerialized), turn, turnNumber);
        } catch (IOException e) {
            return null;
        }
        //return new GameState(new Board(false), Stone.COLOR.BLACK);
    }
}
