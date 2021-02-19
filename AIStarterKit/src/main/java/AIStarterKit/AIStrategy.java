package AIStarterKit;

import Shobu.Game;
import Shobu.Turn;

import java.util.Optional;

public interface AIStrategy {
    Optional<Turn> getTurnToPlay(Game shobuGame);
}

