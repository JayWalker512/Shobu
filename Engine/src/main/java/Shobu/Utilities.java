package Shobu;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Class for misc methods that don't necessarily fit nicely into another class. Can be refactored later.
 */
public class Utilities {

    static int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }

    public static List<Stone> getStonesIntersected(Board board, Move move) {
        if (move.isValid() == false) {
            return null; // This algorithm doesn't make sense for invalid moves.
        }

        int xAdjust = Utilities.clamp(move.getHeading().x, -1, 1);
        int yAdjust = Utilities.clamp(move.getHeading().y, -1, 1);
        List<Stone> stones = new ArrayList<>();
        Vector2 currentScanLocation = new Vector2(move.getOrigin()).add(new Vector2(xAdjust, yAdjust));

        // This is a pretty fragile algorithm begging for an infinite loop...
        while (true) {
            if (board.getQuadrant(currentScanLocation) != board.getQuadrant(move.getOrigin())) { return stones; }
            Stone intersected = board.getStone(currentScanLocation);
            if (intersected != null) {
                stones.add(intersected);
            }
            if (currentScanLocation.equals(move.getOrigin().add(move.getHeading()))) { break; }
            currentScanLocation = new Vector2(currentScanLocation).add(new Vector2(xAdjust, yAdjust));
        }
        return stones;
    }

    public static int countStonesOfColorOnBoard(Board b, Stone.COLOR c) {
        int sum = 0;
        for (int x = 0; x < b.getDimensions().x; x++) {
            for (int y = 0; y < b.getDimensions().y; y++) {
                Stone s = b.getStone(new Vector2(x,y));
                if (s != null && s.getColor() == c) {
                    sum += 1;
                }
            }
        }
        return sum;
    }

    /** This method reads the output from STDIN until it determines that it has received a complete
     * JSON object. It does this by matching curly braces { } and with no other method or validation of the JSON
     * structure. That's up to the caller! It then returns the String containing this object.
     * @return
     */
    public static String getNextJsonFromInputStream(InputStream in) {
        StringBuilder sb = new StringBuilder();
        InputStream processOutput = in;
        int byteRead = 0;
        try {
            boolean objectStarted = false;
            boolean objectFinished = false;
            Stack<Character> stack = new Stack<>();

            while (objectFinished == false) {
                byteRead = processOutput.read();
                if (byteRead == -1) {
                    // End of input, subprocess probably died.
                    return null; // no input from subprocess
                }
                char asciiCharRead = (char)byteRead;
                AIController.JSON_DELIMITERS delimReceived = AIController.matchCurlyBraces(stack, asciiCharRead);
                if (delimReceived == AIController.JSON_DELIMITERS.STARTED_OBJECT) {
                    objectStarted = true;
                }
                if (delimReceived == AIController.JSON_DELIMITERS.FINISHED_OBJECT) {
                    objectFinished = true;
                }
                if (objectStarted) {
                    sb.append(asciiCharRead);
                }
            }
        } catch (Exception e) {
            return "";
        }
        return sb.toString();
    }

    /**
     * This method checks if a JSON string provided is A. a JSON object and B. has a property "type" equal
     * to "turn". That is all, it does not validate if a payload is present or do any other kind of checks.
     * @param jsonInput
     * @return true if the json object has "type":"turn", false otherwise.
     */
    public static boolean isGameStatePayload(String jsonInput) {
        JsonReader jsonReader = new JsonReader(new StringReader(jsonInput));
        try {
            JsonToken nextToken = jsonReader.peek();
            if(!JsonToken.BEGIN_OBJECT.equals(nextToken)) { return false; }
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                nextToken = jsonReader.peek();
                if (JsonToken.NAME.equals(nextToken)) {
                    String name = jsonReader.nextName();
                    if (name.equals("type")) {
                        String value = jsonReader.nextString();
                        if (value.equals("game_state")) {
                            return true;
                        }
                    }
                }
                jsonReader.skipValue();
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    public static JsonReader unwrapGameStateJsonObject(String jsonInput) {
        JsonReader jsonReader = new JsonReader(new StringReader(jsonInput));
        try {
            JsonToken nextToken = jsonReader.peek();
            if(!JsonToken.BEGIN_OBJECT.equals(nextToken)) { return null; }
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                nextToken = jsonReader.peek();
                if (JsonToken.NAME.equals(nextToken)) {
                    String name = jsonReader.nextName();
                    if (name.equals("payload")) {
                        return jsonReader;
                    }
                }
                jsonReader.skipValue();
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    /**
     * This method checks if a JSON string provided is A. a JSON object and B. has a property "type" equal
     * to "turn". That is all, it does not validate if a payload is present or do any other kind of checks.
     * @param jsonInput
     * @return true if the json object has "type":"turn", false otherwise.
     */
    public static boolean isTurnPayload(String jsonInput) {
        JsonReader jsonReader = new JsonReader(new StringReader(jsonInput));
        try {
            JsonToken nextToken = jsonReader.peek();
            if(!JsonToken.BEGIN_OBJECT.equals(nextToken)) { return false; }
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                nextToken = jsonReader.peek();
                if (JsonToken.NAME.equals(nextToken)) {
                    String name = jsonReader.nextName();
                    if (name.equals("type")) {
                        String value = jsonReader.nextString();
                        if (value.equals("turn")) {
                            return true;
                        }
                    }
                }
                jsonReader.skipValue();
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    public static String wrapJsonWithContainer(String type, String jsonPayload) {
        return "{ \"type\": \"" + type + "\", \"payload\": " + jsonPayload + "}";
    }

    public static JsonReader unwrapTurnJsonObject(String jsonInput) {
        JsonReader jsonReader = new JsonReader(new StringReader(jsonInput));
        try {
            JsonToken nextToken = jsonReader.peek();
            if(!JsonToken.BEGIN_OBJECT.equals(nextToken)) { return null; }
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                nextToken = jsonReader.peek();
                if (JsonToken.NAME.equals(nextToken)) {
                    String name = jsonReader.nextName();
                    if (name.equals("payload")) {
                        return jsonReader;
                    }
                }
                jsonReader.skipValue();
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }
}
