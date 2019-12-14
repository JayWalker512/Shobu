package Shobu;

public class Turn {

    private Move passive;
    private Move aggressive;

    public void setPassive(Move passive) {
        this.passive = passive;
    }

    public void setAggressive(Move aggressive) {
        this.aggressive = aggressive;
    }

    public Move getPassive() {
        return passive;
    }

    public Move getAggressive() {
        return aggressive;
    }

}
