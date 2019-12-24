package Shobu;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class Turn {

    private Move passive;
    private Move aggressive;
    private List<String> errors;

    Turn(Move passive, Move aggressive) {
        this.errors = new ArrayList<>();
        this.passive = passive;
        this.aggressive = aggressive;
    }

    public Move getPassive() {
        return passive;
    }

    public Move getAggressive() {
        return aggressive;
    }

    public void addError(String e) {
        this.errors.add(e);
    }

    public List<String> getErrors() {
        return new ArrayList<>(this.errors);
    }

    public static Turn fromJsonReader(JsonReader jsonReader) {
        Move passiveMove = new Move(new Vector2(0,0), new Vector2(0,0));
        Move aggressiveMove = new Move(new Vector2(0,0), new Vector2(0,0));
        try {
            JsonToken nextToken = jsonReader.peek();
            if(!JsonToken.BEGIN_OBJECT.equals(nextToken)) { return null; }
            jsonReader.beginObject();
            while(jsonReader.hasNext()) {
                nextToken = jsonReader.peek();
                if(JsonToken.NAME.equals(nextToken)){
                    nextToken = jsonReader.peek();
                    if (nextToken == JsonToken.NAME) {
                        String name  =  jsonReader.nextName();
                        if (name.equals("passive")) {
                            passiveMove = Move.fromJsonReader(jsonReader);
                            if (passiveMove == null) { return null; }
                        } else if (name.equals("aggressive")) {
                            aggressiveMove = Move.fromJsonReader(jsonReader);
                            if (aggressiveMove == null) { return null; }
                        } else {
                            return null; // unrecognized part of a turn
                        }
                    }
                } else {
                    return null;
                }
            }
            jsonReader.endObject();
            return new Turn(passiveMove, aggressiveMove);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
