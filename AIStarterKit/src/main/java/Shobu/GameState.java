package Shobu;

import com.google.gson.stream.JsonReader;

public class GameState {
    private Board board;
    private Stone.COLOR whosTurnIsIt;

    public Board getBoard() {
        return new Board(board);
    }

    public Stone.COLOR getWhosTurnIsIt() {
        return whosTurnIsIt;
    }

    // Private constructor as GameState can only be built from JSON input.
    private GameState(Board board, Stone.COLOR whosTurnIsIt) {
        this.board = new Board(board);
        this.whosTurnIsIt = whosTurnIsIt;
    }

    public static GameState fromJsonReader(JsonReader jsonReader) {
        // TODO WRITE ME
        return new GameState(new Board(false), Stone.COLOR.BLACK);
    }
}
