package expressivo;

import java.util.Map;

public class Variable implements Expression {
    private final String name;
 // Rep invariant:
    //    name.length() > 0 and name contains only 
    //    lowercase or uppercase letters
    // Abstraction Function:
    //   represents a variable in a mathematical expression
    
    // Check that the rep invariant is true
    // *** Warning: this does nothing unless you turn on assertion checking
    // by passing -enableassertions to Java
    private void checkRep() {
        assert name.length() > 0;
        for (char x : name.toCharArray()) {
            assert Character.isLetter(x);
        }
    }
    
    public Variable(String name) {
        this.name = name;
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
        return this.name;
    }
    
    @Override
    public boolean equals(Object thatObject) {
        if (!(thatObject instanceof Variable)) return false;
        Variable thatVariable = (Variable) thatObject;
        
        return this.name.equals(thatVariable.name);
    }
    
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public Expression differentiate(String variable) {
        if (this.name.equals(variable)) {
            return new Number(1);
        } else {
            return new Number(0);
        }
    }

    @Override
    public Expression simplify(final Map<String, Double> environment) {
        if (environment.containsKey(this.name)) {
            return new Number(environment.get(this.name));
        } else {
            return this;
        }
    }

    @Override
    public boolean hasValue() {
        return false;
    }

    @Override
    public double getValue() {
        throw new UnsupportedOperationException();
    }
}
