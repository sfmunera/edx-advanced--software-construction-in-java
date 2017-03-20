package expressivo;

import lib6005.parser.*;
import java.io.*;
import java.util.Map;

/**
 * An immutable data type representing a polynomial expression of:
 *   + and *
 *   nonnegative integers and floating-point numbers
 *   variables (case-sensitive nonempty strings of letters)
 * 
 * <p>PS1 instructions: this is a required ADT interface.
 * You MUST NOT change its name or package or the names or type signatures of existing methods.
 * You may, however, add additional methods, or strengthen the specs of existing methods.
 * Declare concrete variants of Expression in their own Java source files.
 */
public interface Expression {
    // Rep:
    // Expression = Number(value:double)
    //            + Variable(name:String)
    //            + Plus(left:Expression, right:Expression)
    //            + Times(left:Expression, right:Expression)
    
    enum Grammar {ROOT, SUM, PRODUCT, PRIMITIVE, NUMBER, VARIABLE, WHITESPACE};
    
    /**
     * Create a number expression.
     * @param value numerical value of the number expression.
     * @return new instance of a number expression
     */
    public static Expression number(double value)
    {
        return new Number(value);
    }
    
    /**
     * Create a variable expression.
     * @param name variable name.
     * @return new instance of a variable expression
     */
    public static Expression variable(String name)
    {
        return new Variable(name);
    }
    
    /**
     * Create a new expression representing the sum of two expressions.
     * The result has this expression as the left operand.
     * @param rhs right operand of the sum operation.
     * @return new instance of a sum expression
     */
    public Expression plus(Expression rhs);
    
    /**
     * Create a new expression representing the product of two expressions.
     * The result has this expression as the left operand.
     * @param rhs right operand of the product operation.
     * @return new instance of a product expression
     */
    public Expression times(Expression rhs);
    
    /**
     * Parse an expression.
     * @param input expression to parse, as defined in the PS1 handout.
     * @return expression AST for the input
     * @throws IllegalArgumentException if the expression is invalid
     */
    public static Expression parse(String input) {
        try {
            Parser<Grammar> parser =
                    GrammarCompiler.compile(new File("src/expressivo/Expression.g"), Grammar.ROOT);
            ParseTree<Grammar> tree = parser.parse(input);
            
            return buildAST(tree);
        } catch (UnableToParseException utpe) {
            System.out.println(utpe.getClass().getName() + ": " + utpe.getMessage());
        } catch (IOException ioe) {
            System.out.println(ioe.getClass().getName() + ": " + ioe.getMessage());
        }
        throw new IllegalArgumentException("expression is invalid");
    }
    
    /**
     * @return a parsable representation of this expression, such that
     * for all e:Expression, e.equals(Expression.parse(e.toString())).
     */
    @Override 
    public String toString();

    /**
     * @param thatObject any object
     * @return true if and only if this and thatObject are structurally-equal
     * Expressions, as defined in the PS1 handout.
     */
    @Override
    public boolean equals(Object thatObject);
    
    /**
     * @return hash code value consistent with the equals() definition of structural
     * equality, such that for all e1,e2:Expression,
     *     e1.equals(e2) implies e1.hashCode() == e2.hashCode()
     */
    @Override
    public int hashCode();
    
    /**
    * Function converts a ParseTree to an Expression. 
    * @param p
    *  ParseTree<Grammar> that is assumed to have been constructed by the grammar in Expression.g
    * @return expression AST of the input
    */
    public static Expression buildAST(ParseTree<Grammar> p){

        switch (p.getName()){
        /*
         * Since p is a ParseTree parameterized by the type Grammar, p.getName() 
         * returns an instance of the Grammar enum. This allows the compiler to check
         * that we have covered all the cases.
         */
        case NUMBER:
            /*
             * A number will be a terminal containing a number.
             */
            return new Number(Double.parseDouble(p.getContents()));
        case VARIABLE:
            /*
             * A variable will be a terminal containing a name.
             */
            return new Variable(p.getContents());
        case PRIMITIVE:
            /*
             * A primitive will have either a number, a variable or a sum as child (in addition to 
             * some whitespace)
             */             

            if (p.childrenByName(Grammar.NUMBER).isEmpty() && p.childrenByName(Grammar.VARIABLE).isEmpty()) {
                return buildAST(p.childrenByName(Grammar.SUM).get(0));
            } else if (p.childrenByName(Grammar.VARIABLE).isEmpty()) {
                return buildAST(p.childrenByName(Grammar.NUMBER).get(0));
            } else {
                return buildAST(p.childrenByName(Grammar.VARIABLE).get(0));
            }

        case SUM:
            /*
             * A sum will have one or more children that need to be summed together.
             * Note that we only care about the children that are product. There may also be 
             * some whitespace children which we want to ignore.
             */
            boolean first = true;
            Expression result = null;
            for (ParseTree<Grammar> child : p.childrenByName(Grammar.PRODUCT)){                
                if (first){
                    result = buildAST(child);
                    first = false;
                } else {
                    result = new Plus(result, buildAST(child));
                }
            }
            if (first) {
                throw new RuntimeException("sum must have a non whitespace child:" + p);
            }
            return result;
        case PRODUCT:
            /*
             * A product will have one or more children that need to be multiplied together.
             * Note that we only care about the children that are primitive. There may also be 
             * some whitespace children which we want to ignore.
             */
            first = true;
            result = null;
            for (ParseTree<Grammar> child : p.childrenByName(Grammar.PRIMITIVE)){                
                if (first){
                    result = buildAST(child);
                    first = false;
                } else {
                    result = new Times(result, buildAST(child));
                }
            }
            if (first) {
                throw new RuntimeException("product must have a non whitespace child:" + p);
            }
            return result;
        case ROOT:
            /*
             * The root has a single sum child, in addition to having potentially some whitespace.
             */
            return buildAST(p.childrenByName(Grammar.SUM).get(0));
        case WHITESPACE:
            /*
             * Since we are always avoiding calling buildAST with whitespace, 
             * the code should never make it here. 
             */
            throw new RuntimeException("You should never reach here:" + p);
        }   
        /*
         * The compiler should be smart enough to tell that this code is unreachable, but it isn't.
         */
        throw new RuntimeException("You should never reach here:" + p);
    }
    
    /**
     * Return the derivative of this expression with respect to variable.
     * @param variable variable with respect to which this expression is differentiated.
     * @return expression differentiated expression.
     */
    public Expression differentiate(String variable);
    
    /**
     * Return the expression with variables replaced according to environment
     * and numerical operations simplified.
     * @param environment mapping from variables to numerical values.
     * @return expression simplified expression.
     */
    public Expression simplify(final Map<String, Double> environment);
    
    /**
     * @return true if this expression has a value (only true for Number),
     * false otherwise.
     */
    public boolean hasValue();
    
    /**
     * @return numerical value if this expression is Number.
     */
    public double getValue();
    
    /* Copyright (c) 2015-2017 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires permission of course staff.
     */
}
