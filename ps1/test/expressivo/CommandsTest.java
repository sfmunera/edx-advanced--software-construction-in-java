/* Copyright (c) 2015-2017 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for the static methods of Commands.
 */
public class CommandsTest {

    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testDifferentiate1() {
        String s = Commands.differentiate("x + y", "x");
        assertEquals(s, "(1.0000 + 0.0000)");
    }
    
    @Test
    public void testDifferentiate2() {
        String s = Commands.differentiate("x + y", "y");
        assertEquals(s, "(0.0000 + 1.0000)");
    }
    
    @Test
    public void testDifferentiate3() {
        String s = Commands.differentiate("x", "x");
        assertEquals(s, "1.0000");
    }
    
    @Test
    public void testDifferentiate4() {
        String s = Commands.differentiate("x", "y");
        assertEquals(s, "0.0000");
    }
    
    @Test
    public void testDifferentiate5() {
        String s = Commands.differentiate("x*x*x", "x");
        assertEquals(s, "(x*x*1.0000 + x*(x*1.0000 + x*1.0000))");
    }
    
    @Test
    public void testSimplify1() {
        Map<String, Double> environment = new HashMap<>();
        environment.put("x", 10.0);
        String s = Commands.simplify("x*x*x + x*y + z + 1", environment);
        
        assertEquals(s, "(((1000.0000 + 10.0000*y) + z) + 1.0000)");
    }
    
    @Test
    public void testSimplify2() {
        Map<String, Double> environment = new HashMap<>();
        environment.put("x", 10.0);
        environment.put("y", 2.0);
        String s = Commands.simplify("x*x*x + x*y + z + 1", environment);
        
        assertEquals(s, "((1020.0000 + z) + 1.0000)");
    }
    
    @Test
    public void testSimplify3() {
        Map<String, Double> environment = new HashMap<>();
        environment.put("x", 10.0);
        environment.put("y", 2.0);
        environment.put("z", 7.0);
        String s = Commands.simplify("x*x*x + x*y + z + 1", environment);
        
        assertEquals(s, "1028.0000");
    }
}
