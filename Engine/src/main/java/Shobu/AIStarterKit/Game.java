package Shobu.AIStarterKit;

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

    Game(GameRules rules, Board board) {
        this.whosTurnIsIt = Stone.COLOR.BLACK;
        this.turnNumber = 0;
        this.rules = new GameRules(rules);
        this.board = new Board(board);
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

    public String toJson() {
        return "{\"type\":\"gamestate\", \"payload\": { \"board\": \"" + this.board.toSerializedString() + "\", \"turn\": \"" + this.getWhosTurnItIs().toString() + "\", \"turnNumber\": " + Integer.toString(this.getTurnNumber()) + "\"}}";
    }
}