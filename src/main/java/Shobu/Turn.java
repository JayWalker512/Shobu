package Shobu;

import java.util.ArrayList;
import java.util.List;

public class Turn {

    private Move passive;
    private Move aggressive;
    private List<String> errors;

    Turn(Move passive, Move aggressive) {
        this.errors = new ArrayList<>();
        this.passive = passive;
        this.aggressive = aggressive;
    }

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

    public void addError(String e) {
        this.errors.add(e);
    }

    public List<String> getErrors() {
        return new ArrayList<>(this.errors);
    }

}
