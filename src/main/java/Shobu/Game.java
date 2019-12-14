package Shobu;

public class Game {
    private Stone.COLOR whosTurnIsIt;

    Game() {
        whosTurnIsIt = Stone.COLOR.BLACK;
    }

    public void tick() {
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
