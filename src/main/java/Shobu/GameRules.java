package Shobu;

import java.util.List;

public class GameRules {

    GameRules() {

    }

    GameRules(GameRules gr) {

    }

    public Board transitionBoard(Board board, Turn turn) {
        // The pushStone methods validate moves just to see if they're possible, not if they're legal.
        // You should validate a Turn for "rule" legality prior to calling transitionBoard.
        // This method will just do what you tell it to so long as it's possible to do it.

        Board newBoard = new Board(board);
        newBoard.pushStones(turn.getPassive());
        newBoard.pushStones(turn.getAggressive());
        return newBoard;
    }

    public Turn validateTurn(Game game, Board board, Turn turn) {

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

        // Ensure that passive and aggressive moves have the same heading
        Vector2 passiveHeading = turn.getPassive().getHeading();
        Vector2 aggressiveHeading = turn.getAggressive().getHeading();
        if (false == passiveHeading.equals(aggressiveHeading)) {
            validatedTurn.addError("Passive move and aggressive move do not have equal headings: " + passiveHeading.toString() + " " + aggressiveHeading.toString());
        }

        // Ensure the passive and aggressive moves are attempting to move a stone of the color who's turn it is
        Stone.COLOR passiveStoneColor = board.getStone(passiveOrigin).getColor();
        Stone.COLOR aggressiveStoneColor = board.getStone(aggressiveOrigin).getColor();
        if (passiveStoneColor != game.getWhosTurnItIs() || aggressiveStoneColor != game.getWhosTurnItIs()) {
            validatedTurn.addError("Attempts to move stone of opposing players color when it is not their turn.");
        }

        // Ensure that the passive move is on the home board of the player who's turn it is
        if (game.getWhosTurnItIs() == Stone.COLOR.WHITE &&
                board.getQuadrant(turn.getPassive().getOrigin()) != 0 &&
                board.getQuadrant(turn.getPassive().getOrigin()) != 1) {
            validatedTurn.addError("Passive move is not on the home board of the player who's turn it is: " + game.getWhosTurnItIs().toString());
        }
        if (game.getWhosTurnItIs() == Stone.COLOR.BLACK &&
                board.getQuadrant(turn.getPassive().getOrigin()) != 2 &&
                board.getQuadrant(turn.getPassive().getOrigin()) != 3) {
            validatedTurn.addError("Passive move is not on the home board of the player who's turn it is: " + game.getWhosTurnItIs().toString());
        }

        // Ensure that the passive and aggressive moves are on different horizontal boards.
        int passiveQuadrant = board.getQuadrant(turn.getPassive().getOrigin());
        int aggressiveQuadrant = board.getQuadrant(turn.getAggressive().getOrigin());
        if ( (passiveQuadrant == 0 || passiveQuadrant == 2) && (aggressiveQuadrant != 1 && aggressiveQuadrant != 3)) {
            validatedTurn.addError("Passive and aggressive moves must be on different color boards.");
        }
        if ( (passiveQuadrant == 1 || passiveQuadrant == 3) && (aggressiveQuadrant != 0 && aggressiveQuadrant != 2)) {
            validatedTurn.addError("Passive and aggressive moves must be on different color boards.");
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
    public Stone.COLOR getWinner(Game game, Board board) {
        return null;
    }

}
