package expressivo;

import java.util.Map;

public class Times implements Expression {
    
    private final Expression left;
    private final Expression right;
    // Rep invariant:
    //    left != null and right != null
    // Abstraction Function:
    //   represent the left and right operands of a product
    
    // Check that the rep invariant is true
    // *** Warning: this does nothing unless you turn on assertion checking
    // by passing -enableassertions to Java
    private void checkRep() {
        assert left != null && right != null;
    }

    public Times(Expression left, Expression right) {
        this.left = left;
        this.right = right;
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
        return this.left.toString() + "*" + this.right.toString();
    }
    
    @Override
    public boolean equals(Object thatObject) {
        if (!(thatObject instanceof Times)) return false;
        Times thatTimes = (Times) thatObject;
        
        return this.left.equals(thatTimes.left) && this.right.equals(thatTimes.right);
    }
    
    @Override
    public int hashCode() {
        int hashCode = 1;
        hashCode = 31 * hashCode + this.left.hashCode();
        hashCode = 31 * hashCode + this.right.hashCode();
        
        return hashCode;
    }

    @Override
    public Expression differentiate(String variable) {
        Expression leftDerivative = this.left.differentiate(variable);
        Expression rightDerivative = this.right.differentiate(variable);
        
        return new Plus(new Times(this.left, rightDerivative), new Times(this.right, leftDerivative));
    }

    @Override
    public Expression simplify(Map<String, Double> environment) {
        Expression leftSimplified = this.left.simplify(environment);
        Expression rightSimplified = this.right.simplify(environment);
        
        if (leftSimplified.hasValue() && rightSimplified.hasValue()) {
            return new Number(leftSimplified.getValue() * rightSimplified.getValue());
        } else {
            return new Times(leftSimplified, rightSimplified);
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
