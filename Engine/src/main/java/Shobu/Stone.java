package Shobu;

public class Stone {

    public enum COLOR {
        BLACK,
        WHITE
    };

    private COLOR color;
    private int id;

    Stone(int id, COLOR color) {
        this.id = id;
        this.color = color;
    }

    // Copy constructor
    Stone(Stone s) {
        this(s.getId(), s.getColor());
    }

    public int getId() {
        return id;
    }

    public COLOR getColor() {
        return color;
    }


    public String toString() {
        return "(" + Integer.toString(this.id) + ", " + (this.color == COLOR.WHITE ? "WHITE" : "BLACK") + ")";
    }
}
