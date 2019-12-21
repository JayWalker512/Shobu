package Shobu;

public class Vector2 {
    public final int x;
    public final int y;

    Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Vector2(Vector2 v) {
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

}
