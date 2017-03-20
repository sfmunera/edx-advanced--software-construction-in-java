package expressivo;

import java.util.Map;

public class Number implements Expression {
    private final double value;
    // Rep invariant:
    //    value >= 0
    // Abstraction Function:
    //   represents the value of a numerical expression

    // Check that the rep invariant is true
    // *** Warning: this does nothing unless you turn on assertion checking
    // by passing -enableassertions to Java
    private void checkRep() {
        assert value >= 0;
    }
    
    public Number(double value) {
        this.value = value;
        checkRep();
    }
    
    @Override
    public Expression plus(Expression rhs) {
        return new Plus(this, rhs);
    }

    @Override
    public Expression times(Expression rhs) {
        return new Times(this, rhs);
    }

    @Override 
    public String toString() {
        return String.format("%.4f", this.value);
    }
    
    @Override
    public boolean equals(Object thatObject) {
        if (!(thatObject instanceof Number)) return false;
        Number thatNumber = (Number) thatObject;
        
        return this.value == thatNumber.value;
    }
    
    @Override
    public int hashCode() {
        return Double.hashCode(this.value);
    }

    @Override
    public Expression differentiate(String variable) {
        return new Number(0);
    }

    @Override
    public Expression simplify(final Map<String, Double> environment) {
        return this;
    }

    @Override
    public boolean hasValue() {
        return true;
    }

    @Override
    public double getValue() {
        return this.value;
    }
}
