package Shobu;

public class Game {
    private Stone.COLOR whosTurnIsIt;
    private int turnNumber;

    private GameRules rules;
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

    public void takeTurn(Turn turn) {
        // TODO transition the board here
        swapWhosTurnItIs();
        this.turnNumber++;
    }

    void swapWhosTurnItIs() {
        if (this.whosTurnIsIt == Stone.COLOR.BLACK) {
            this.whosTurnIsIt = Stone.COLOR.WHITE;
            return;
        };

        this.whosTurnIsIt = Stone.COLOR.BLACK;
    }
}
