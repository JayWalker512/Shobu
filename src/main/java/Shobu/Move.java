package Shobu;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class Move {
    private Vector2 origin;
    private Vector2 heading;
    private List<String> errors;

    public Vector2 getOrigin() {
        return new Vector2(origin);
    }

    public void setOrigin(Vector2 origin) {
        this.origin = new Vector2(origin);
    }

    public Vector2 getHeading() {
        return new Vector2(heading);
    }

    public void setHeading(Vector2 heading) {
        this.heading = new Vector2(heading);
    }

    public Move(Vector2 origin, Vector2 heading) {
        this.origin = origin;
        this.heading = heading;
        this.errors = new ArrayList<>();
    }

    // This method does not consider other stones in the way. Only spacing.
    public boolean isValid() {
        // TODO FIXME this method should validate just that the move is orthogonal/diagonal.
        // Other validation methods can determine if the move is "legal".

        //x's mark valid moves, o is the stone.
        //x_x_x
        //_xxx_
        //XXoXX
        //_XXX_
        //X_X_X

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
            if (heading.equals(invalid) == true) {
                return false;
            }
        }

        if (heading.x > 2 || heading.y > 2) {
            return false;
        }

        return true;
    }

    public void addError(String e) {
        this.errors.add(e);
    }

    public List<String> getErrors() {
        return new ArrayList<>(this.errors);
    }

    public static Move fromJsonReader(JsonReader jsonReader) {
        Vector2 origin = new Vector2(0,0);
        Vector2 heading = new Vector2(0,0);
        try {
            JsonToken nextToken = jsonReader.peek();
            if (nextToken != JsonToken.BEGIN_OBJECT) { return null; } // invalid!
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                nextToken = jsonReader.peek();
                if (nextToken == JsonToken.NAME) {
                    String name = jsonReader.nextName();
                    if (name.equals("origin")) {
                        origin = Vector2.fromJsonReader(jsonReader);
                        if (origin == null) { return null; }
                    } else if (name.equals("heading")) {
                        heading = Vector2.fromJsonReader(jsonReader);
                        if (heading == null) { return null; }
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            }
            jsonReader.endObject();
            return new Move(origin, heading);
        } catch (IOException e) {
            return null;
        }
    }
}
