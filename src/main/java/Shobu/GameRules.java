package Shobu;

public class GameRules {

    GameRules() {

    }

    GameRules(GameRules gr) {

    }

    public static Board transitionBoard(Game game, Board board, Turn turn) {
        return null;
    }

    public static Turn validateTurn(Game game, Board board, Turn turn) {

        Turn validatedTurn = new Turn(turn.getPassive(), turn.getAggressive());

        // Ensure the passive and aggressive origins are in bounds of the board
        Vector2 passiveOrigin = turn.getPassive().getOrigin();
        if (passiveOrigin.x > board.getDimensions().x || passiveOrigin.x < 0) {
            validatedTurn.addError("Passive move origin is out of board boundaries: " + passiveOrigin.toString());
        }
        Vector2 aggressiveOrigin = turn.getAggressive().getOrigin();
        if (aggressiveOrigin.x > board.getDimensions().x || aggressiveOrigin.x < 0) {
            validatedTurn.addError("Aggressive move origin is out of board boundaries: " + aggressiveOrigin.toString());
        }

        // Ensure the passive and aggressive moves are attempting to move a stone of the color who's turn it is
        Stone.COLOR passiveStoneColor = board.getStone(passiveOrigin).getColor();
        Stone.COLOR aggressiveStoneColor = board.getStone(aggressiveOrigin).getColor();
        if (passiveStoneColor != game.getWhosTurnItIs() || aggressiveStoneColor != game.getWhosTurnItIs()) {
            validatedTurn.addError("Attempts to move stone of opposing players color when it is not their turn.");
        }

        // TODO FIXME add error strings from Move.validateMove(move) later on once you write it.



        return null;
    }

    /**
     * Returns the color of the winner or null if the win condition has not been reached.
     *
     * This assumes that the game was in a valid state. If there's no stones on the board at all, the winner
     * is meaningless.
     * @param game
     * @param board
     * @return
     */
    public static Stone.COLOR getWinner(Game game, Board board) {
        return null;
    }

}
