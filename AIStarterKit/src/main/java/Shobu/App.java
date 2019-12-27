package Shobu;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.*;

public class App {

    /** This method reads the output from STDIN until it determines that it has received a complete
     * JSON object. It does this by matching curly braces { } and with no other method or validation of the JSON
     * structure. That's up to the caller! It then returns the String containing this object.
     * @return
     */
    public static String getNextJsonFromStdin() {
        StringBuilder sb = new StringBuilder();
        InputStream processOutput = System.in;
        int byteRead = 0;
        try {
            while (processOutput.available() == 0) {
                byteRead = 0; // just loop and wait until input comes.
            }
            boolean objectStarted = false;
            boolean objectFinished = false;
            Stack<Character> stack = new Stack<>();

            while (objectFinished == false) {
                byteRead = processOutput.read();
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
                        if (value.equals("gamestate")) {
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

    public static void main(String args[]) {
        System.out.println("AI Starter Kit!");

        // Read game state JSON
        String nextJson = App.getNextJsonFromStdin();
        if (isGameStatePayload(nextJson)) {
            JsonReader jsonReader = unwrapGameStateJsonObject(nextJson);
            GameState g = GameState.fromJsonReader(jsonReader);

            // Enumerate valid Turns
            //List<Turn> validTurns = enumerateValidTurns(g.getBoard(), g.getWhosTurnIsIt());

            // Choose a Turn to play
            //Turn chosenTurn = validTurns.get(random);

            // Send turn back to Engine as JSON
            //System.out.println(chosenTurn.toJson());

            // Repeat
        }


    }
}