/* Copyright (c) 2015-2017 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for the Expression abstract data type.
 */
public class ExpressionTest {
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testStringExpressionNumber() {
        Expression e = Expression.number(1.0);
        assertTrue(e.equals(Expression.parse(e.toString())));
    }
    
    @Test
    public void testStringExpressionVariable() {
        Expression e = Expression.variable("var");
        assertTrue(e.equals(Expression.parse(e.toString())));
    }
    
    @Test
    public void testStringExpressionPlus() {
        Expression e = Expression.variable("var").plus(Expression.number(1.0));
        assertTrue(e.equals(Expression.parse(e.toString())));
    }
    
    @Test
    public void testStringExpressionTimes() {
        Expression e = Expression.variable("var").times(Expression.number(1.0));
        assertTrue(e.equals(Expression.parse(e.toString())));
    }
    
    @Test
    public void testStringExpressionCombined() {
        Expression x = Expression.variable("x");
        Expression y = Expression.variable("y");
        Expression z = Expression.variable("z");
        Expression n = Expression.number(10.0);
        
        Expression e1 = x.plus(y).times(z).times(n);
        assertTrue(e1.equals(Expression.parse(e1.toString())));
        
        Expression e2 = x.times(y).plus(z).plus(n);
        assertTrue(e2.equals(Expression.parse(e2.toString())));
    }
    
    @Test
    public void testEqualsExpressionNumber() {
        Expression e1 = Expression.number(1.0);
        Expression e2 = Expression.number(1.0);
        assertTrue(e1.equals(e2));
        assertTrue(e2.equals(e1));
        assertEquals(e1.hashCode(), e2.hashCode());
    }
    
    @Test
    public void testEqualsExpressionNumber2() {
        Expression e1 = Expression.number(1.0);
        Expression e2 = Expression.number(2.0);
        assertFalse(e1.equals(e2));
        assertFalse(e2.equals(e1));
        assertNotEquals(e1.hashCode(), e2.hashCode());
    }
    
    @Test
    public void testEqualsExpressionVariable() {
        Expression e1 = Expression.variable("x");
        Expression e2 = Expression.variable("x");
        assertTrue(e1.equals(e2));
        assertTrue(e2.equals(e1));
        assertEquals(e1.hashCode(), e2.hashCode());
    }
    
    @Test
    public void testEqualsExpressionVariable2() {
        Expression e1 = Expression.variable("x");
        Expression e2 = Expression.variable("y");
        assertFalse(e1.equals(e2));
        assertFalse(e2.equals(e1));
        assertNotEquals(e1.hashCode(), e2.hashCode());
    }
    
    @Test
    public void testEqualsExpressionPlus() {
        Expression e1 = Expression.variable("var").plus(Expression.number(1.0));
        Expression e2 = Expression.number(1.0).plus(Expression.variable("var"));
        assertFalse(e1.equals(e2));
    }
    
    @Test
    public void testEqualsExpressionTimes() {
        Expression e1 = Expression.variable("var").times(Expression.number(1.0));
        Expression e2 = Expression.number(1.0).times(Expression.variable("var"));
        assertFalse(e1.equals(e2));
        assertNotEquals(e1.hashCode(), e2.hashCode());
    }

    @Test
    public void testParseExpressionParens() {
        Expression e1 = Expression.parse("(1+2)+(x+y)");
        Expression e2 = Expression.parse("(((1)+2)+((x)+(y)))");
        
        assertTrue(e1.equals(e2));
    }
    
    @Test
    public void testParseExpressionAssociative() {
        Expression e1 = Expression.parse("3*x + 2.4");
        Expression e2 = Expression.parse("3*(x + 2.4)");
        
        assertFalse(e1.equals(e2));
    }
    
    @Test
    public void testParseExpressionAssociative2() {
        Expression e1 = Expression.parse("3 + 4 + 5");
        Expression e2 = Expression.parse("(3 + 4) + 5");
        Expression e3 = Expression.parse("3 + (4 + 5)");
        
        assertTrue(e1.equals(e2));
        assertFalse(e1.equals(e3));
    }
    
    @Test
    public void testParseExpressionNotCommutative() {
        Expression e1 = Expression.parse("x + y");
        Expression e2 = Expression.parse("y + x");
        
        assertFalse(e1.equals(e2));
    }
    
    @Test
    public void testParseExpressionNotCommutative2() {
        Expression e1 = Expression.parse("x * y");
        Expression e2 = Expression.parse("y * x");
        
        assertFalse(e1.equals(e2));
    }
    
    @Test
    public void testParseExpressionParens2() {
        Expression e1 = Expression.parse("x + y");
        Expression e2 = Expression.parse("(x + y)");
        Expression e3 = Expression.parse("(x+y)");
        Expression e4 = Expression.parse("(x)+(y)");
        
        assertTrue(e1.equals(e2));
        assertTrue(e2.equals(e3));
        assertTrue(e3.equals(e4));
    }
    
    @Test
    public void testDifferentiate1() {
        Expression e1 = Expression.parse("x + y");
        Expression de1 = Expression.parse("1 + 0");
        
        assertTrue(de1.equals(e1.differentiate("x")));
    }
    
    @Test
    public void testDifferentiate2() {
        Expression e1 = Expression.parse("x + y");
        Expression de1 = Expression.parse("0 + 1");
        
        assertTrue(de1.equals(e1.differentiate("y")));
    }
    
    @Test
    public void testDifferentiate3() {
        Expression e1 = Expression.parse("x");
        Expression de1 = Expression.parse("1");
        
        assertTrue(de1.equals(e1.differentiate("x")));
    }
    
    @Test
    public void testDifferentiate4() {
        Expression e1 = Expression.parse("x");
        Expression de1 = Expression.parse("0");
        
        assertTrue(de1.equals(e1.differentiate("y")));
    }
    
    @Test
    public void testDifferentiate5() {
        Expression e1 = Expression.parse("x*x*x + x*y + z + 1");
        Expression de1 = Expression.parse("x*x*1 + x*(x*1+x*1) + (x*0 + y*1) + 0 + 0");
        
        assertTrue(de1.equals(e1.differentiate("x")));
    }
    
    @Test
    public void testSimplify1() {
        Map<String, Double> environment = new HashMap<>();
        environment.put("x", 10.0);
        Expression e1 = Expression.parse("x*x*x + x*y + z + 1");
        Expression se1 = Expression.parse("1000 + 10*y + z + 1");
        
        assertTrue(se1.equals(e1.simplify(environment)));
    }
    
    @Test
    public void testSimplify2() {
        Map<String, Double> environment = new HashMap<>();
        environment.put("x", 10.0);
        environment.put("y", 2.0);
        Expression e1 = Expression.parse("x*x*x + x*y + z + 1");
        Expression se1 = Expression.parse("1020 + z + 1");
        
        assertTrue(se1.equals(e1.simplify(environment)));
    }
    
    @Test
    public void testSimplify3() {
        Map<String, Double> environment = new HashMap<>();
        environment.put("x", 10.0);
        environment.put("y", 2.0);
        environment.put("z", 7.0);
        Expression e1 = Expression.parse("x*x*x + x*y + z + 1");
        Expression se1 = Expression.parse("1028");
        
        assertTrue(se1.equals(e1.simplify(environment)));
    }
}
