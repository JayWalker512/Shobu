package AIStarterKit;

import Shobu.Game;
import Shobu.Turn;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class RandomStrategy implements AIStrategy {

    public Optional<Turn> getTurnToPlay(Game shobuGame) {
        // Enumerate legal Turns
        List<Turn> legalTurns = shobuGame.enumerateLegalTurns(); // enumerateLegalTurns(shobuGame);
        if (legalTurns.size() > 0) {
            Turn chosenTurn = legalTurns.get(new Random().nextInt(legalTurns.size())); // to ensure it's set
            return Optional.of(chosenTurn);
        }
        return Optional.empty();
    }
}
