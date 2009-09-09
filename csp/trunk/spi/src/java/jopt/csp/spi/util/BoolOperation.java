package jopt.csp.spi.util;

/**
 * Enumeration of mathematical operations
 */
public interface BoolOperation {
    public static final int EQ         = 0;
    public static final int AND        = 1;
    public static final int OR         = 2;
    public static final int XOR        = 3;
    public static final int NOT        = 4;
    public static final int IMPLIES    = 5;
}