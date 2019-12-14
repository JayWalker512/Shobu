package Shobu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class Move {
    private Vector2 from;
    private Vector2 to;

    public Move(Vector2 from, Vector2 to) {
        this.from = from;
        this.to = to;
    }

    // This method does not consider other stones in the way. Only spacing.
    public boolean isValid() {
        //x's mark valid moves, o is the stone.
        //x_x_x
        //_xxx_
        //XXoXX
        //_XXX_
        //X_X_X

        // must match, (x,y),(x,y)
        if (from.x != to.x || from.y != to.y) {
            return false;
        }

        List<Vector2> invalidMoves = new ArrayList<>();
        invalidMoves.add(new Vector2(-1,-2));
        invalidMoves.add(new Vector2(1,-2));

        invalidMoves.add(new Vector2(-2,-1));
        invalidMoves.add(new Vector2(2,-1));

        invalidMoves.add(new Vector2(0,0));

        invalidMoves.add(new Vector2(-2,1));
        invalidMoves.add(new Vector2(2,1));

        invalidMoves.add(new Vector2(-1,2));
        invalidMoves.add(new Vector2(1,2));

        for (Vector2 invalid : invalidMoves) {
            if (from.equals(invalid) == true) {
                return false;
            }
        }

        if (from.x > 2 || to.x > 2 || from.y > 2 || to.y > 2) {
            return false;
        }

        return true;
    }
}
