package Shobu.AIStarterKit;

public final class StoneFactory {
    private int i = 0;
    private static StoneFactory instance = null;

    private StoneFactory() {}

    public static StoneFactory getInstance() {
        if (instance == null) {
            instance = new StoneFactory();
        }
        return instance;
    }

    public Stone createStone(Stone.COLOR color) {
        Stone s = new Stone(this.i, color);
        this.i++;
        return s;
    }
}
