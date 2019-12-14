package Shobu;

public class Stone {

    enum COLOR {
        BLACK,
        WHITE
    };

    private Vector2 location;

    public COLOR getColor() {
        return color;
    }

    public void setColor(COLOR color) {
        this.color = color;
    }

    private COLOR color;

    public Vector2 getLocation() {
        return new Vector2(location.x, location.y);
    }

    public void setLocation(Vector2 location) {
        this.location = new Vector2(location.x, location.y);
    }

    Stone(Vector2 location) {
        this.location = location;
    }



}
