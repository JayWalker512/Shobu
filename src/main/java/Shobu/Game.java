package Shobu;

public class Game {
    private Stone.COLOR whosTurnIsIt;
    private int turnNumber;

    Game(GameRules rules, Board board) {
        this.whosTurnIsIt = Stone.COLOR.BLACK;
        this.turnNumber = 0;

    }

    public Stone.COLOR getWhosTurnItIs() {
        return whosTurnIsIt;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void tick() {
        // TODO transition the board here
        swapWhosTurnItIs();
    }

    void swapWhosTurnItIs() {
        if (this.whosTurnIsIt == Stone.COLOR.BLACK) {
            this.whosTurnIsIt = Stone.COLOR.WHITE;
            return;
        };

        this.whosTurnIsIt = Stone.COLOR.BLACK;
    }
}
