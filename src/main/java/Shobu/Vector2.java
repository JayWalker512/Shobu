package Shobu;

public class Vector2 {
    public int x;
    public int y;

    Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Vector2 a) {
        if (a.x == this.x && a.y == this.y) {
            return true;
        }
        return false;
    }

    public Vector2 add(Vector2 a) {
        return new Vector2(this.x + a.x, this.y + a.y);
    }
}
