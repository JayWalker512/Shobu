package Shobu;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;

public class Vector2 {
    public final int x;
    public final int y;

    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 v) {
        this.x = v.x;
        this.y = v.y;
    }

    public boolean equals(Vector2 a) {
        if (a.x == this.x && a.y == this.y) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "(" + Integer.toString(x) + "," + Integer.toString(y) + ")";
    }

    public Vector2 add(Vector2 a) {
        return new Vector2(this.x + a.x, this.y + a.y);
    }

    public static double euclideanDistance(Vector2 a, Vector2 b) {
        double ac = Math.abs(b.y - a.y);
        double cb = Math.abs(b.x - a.x);
        return Math.hypot(ac, cb);
    }

    public static int manhattanDistance(Vector2 a, Vector2 b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    public static Vector2 fromJsonReader(JsonReader jsonReader) {
        int x = 0;
        int y = 0;
        try {
            JsonToken nextToken = jsonReader.peek();
            if (nextToken != JsonToken.BEGIN_OBJECT) { return null; } // invalid!
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                nextToken = jsonReader.peek();
                if (nextToken == JsonToken.NAME) {
                    String name = jsonReader.nextName();
                    if (name.equals("x")) {
                        nextToken = jsonReader.peek();
                        if (nextToken != JsonToken.NUMBER) { return null; }
                        x = jsonReader.nextInt();
                    } else if (name.equals("y")) {
                        nextToken = jsonReader.peek();
                        if (nextToken != JsonToken.NUMBER) { return null; }
                        y = jsonReader.nextInt();
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            }
            jsonReader.endObject();
            return new Vector2(x,y);
        } catch (IOException e) {
            return null;
        }
    }

    public String toJson() {
        return "{\"x\": " + Integer.toString(x) + ", \"y\": " + Integer.toString(y) + "}";
    }
}
