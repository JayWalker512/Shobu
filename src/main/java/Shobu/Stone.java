package Shobu;

public class Stone {

    enum COLOR {
        BLACK,
        WHITE
    };

    //private Vector2 location;
    private COLOR color;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public COLOR getColor() {
        return color;
    }

    public void setColor(COLOR color) {
        this.color = color;
    }

    /*
    public Vector2 getLocation() {
        return new Vector2(location.x, location.y);
    }

    public void setLocation(Vector2 location) {
        this.location = new Vector2(location.x, location.y);
    }
    */

    Stone(int id, COLOR color) {
        this.id = id;
        this.color = color;
    }

    // Copy constructor
    Stone(Stone s) {
        this(s.getId(), s.getColor());
    }
}
