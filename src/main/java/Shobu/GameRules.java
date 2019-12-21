package Shobu;

import java.util.List;

public class GameRules {

    GameRules() {

    }

    GameRules(GameRules gr) {

    }

    public static Board transitionBoard(Board board, Turn turn) {
        Board newBoard = new Board(board);

        // Logic for moving a stone from the aggressive move. Assumes the move has been validated.
        Vector2 step = new Vector2(Utilities.clamp(turn.getAggressive().getHeading().x, -1, 1), Utilities.clamp(turn.getAggressive().getHeading().y, -1 ,1));
        // calculate first stones new position
        // if on top, move other stone same direction
        // if not done, repeat
        // put "our stone" down in it's final position
        Vector2 intermediatePos = new Vector2(turn.getAggressive().getOrigin());
        boolean done = false;
        while (!done) {
            intermediatePos = intermediatePos.add(step);
            if (newBoard.getStone(intermediatePos) != null) {
                newBoard.moveStone(intermediatePos, intermediatePos.add(step)); // This handles removing stones from the board if they cross boundaries
            }
            if (intermediatePos.equals(turn.getAggressive().getOrigin().add(turn.getAggressive().getHeading()))) {
                done = true;
            }
        }

        return newBoard;
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

        // Can't push through 2+ stones
        List<Stone> intersectedStones = Utilities.getStonesIntersected(board, turn.getPassive());
        if (intersectedStones.size() != 0) {
            validatedTurn.addError("Attempts to push " + Integer.toString(intersectedStones.size()) + " stone(s) with a passive move.");
        }
        intersectedStones = Utilities.getStonesIntersected(board, turn.getAggressive());
        if (intersectedStones.size() >= 2) {
            validatedTurn.addError("Attempts to push more than 1 stone with aggressive move.");
        }

        // Can't push stones of own color
        for (Stone s : intersectedStones) {
            if (s.getColor() == game.getWhosTurnItIs()) {
                validatedTurn.addError("Attempts to push stone of own color.");
            }
        }

        // TODO FIXME add error strings from Move.validateMove(move) later on once you write it.
        return validatedTurn;
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
