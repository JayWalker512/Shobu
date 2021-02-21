package AIStarterKit;

import Shobu.Board;
import Shobu.Game;
import Shobu.Stone;
import Shobu.Turn;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class HeuristicStrategy implements AIStrategy {
    @Override
    public Optional<Turn> getTurnToPlay(Game shobuGame) {
        // Enumerate legal Turns
        List<Turn> legalTurns = shobuGame.enumerateLegalTurns();

        // Choose a Turn to play
        if (legalTurns.size() > 0) {
            /* TODO maybe shuffle the list of legalTurns so the AI doesn't necessarily pick the same move on identical boards.
             * Makes for harder testing since it's non-deterministic in that case (random choice of equal-value turns).
             */
            legalTurns = legalTurns.stream().collect(Collectors.toList()); // make a copy so we can modify the list
            Collections.shuffle(legalTurns, new Random());
            long maxValue = -Long.MAX_VALUE;
            Turn chosenTurn = legalTurns.get(new Random().nextInt(legalTurns.size())); // to ensure it's set
            for (Turn turn : legalTurns) {
                Game tempGame = new Game(shobuGame);
                tempGame.takeTurn(turn);
                long value = calculateHeuristicBoardValueForColor(tempGame.getBoard(), shobuGame.getWhosTurnItIs());
                if (value >= maxValue) {
                    maxValue = value;
                    chosenTurn = turn;
                }
            };

            return Optional.of(chosenTurn);
        }
        return Optional.empty();
    }

    private long calculateHeuristicBoardValueForColor(Board board, Stone.COLOR color) {
        long whiteStones = board.getStones().stream().filter((s) -> (s.getColor() == Stone.COLOR.WHITE)).count();
        long blackStones = board.getStones().stream().filter((s) -> (s.getColor() == Stone.COLOR.BLACK)).count();

        long whiteStonesPerQuadrant[] = { 0, 0, 0, 0 };
        long blackStonesPerQuadrant[] = { 0, 0, 0, 0 };
        for (Stone stone : board.getStones()) {
            if (stone.getColor() == Stone.COLOR.BLACK) {
                blackStonesPerQuadrant[board.getQuadrant(board.getStoneLocation(stone.getId()))] += 1;
            } else {
                whiteStonesPerQuadrant[board.getQuadrant(board.getStoneLocation(stone.getId()))] += 1;
            }
        }
        long blackQuadrantBonus = 0;
        for (long count : whiteStonesPerQuadrant) {
            blackQuadrantBonus += (4 - count) * 2;
        }
        long whiteQuadrantBonus = 0;
        for (long count : blackStonesPerQuadrant) {
            whiteQuadrantBonus += (4 - count) * 2;
        }

        long valueForBlack = blackStones - whiteStones + blackQuadrantBonus - whiteQuadrantBonus;
        return (color == Stone.COLOR.BLACK ? valueForBlack : -valueForBlack);
    }
}
