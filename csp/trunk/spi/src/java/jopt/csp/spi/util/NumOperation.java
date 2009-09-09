package jopt.csp.spi.util;

/**
 * Enumeration of mathematical operations
 */
public final class NumOperation {
    private static String[] operationNames = new String[19];
    private static String[] operationSymbols = new String[19];

    public static final int NONE       = 0;
    public static final int ADD        = 1;
    public static final int SUBTRACT   = 2;
    public static final int MULTIPLY   = 3;
    public static final int DIVIDE     = 4;
    public static final int SQUARE     = 5;
    public static final int SQRT       = 6;  
    public static final int ABS        = 7;
    public static final int POWER      = 8;
    public static final int LOGARITHM  = 9;
    public static final int NAT_LOG    = 10;
    public static final int EXP        = 11;
    public static final int SIN        = 12;
    public static final int COS        = 13;
    public static final int TAN        = 14;
    public static final int ASIN       = 15;
    public static final int ACOS       = 16;
    public static final int ATAN       = 17;
    public static final int SUMMATION  = 18;

    static {
        operationNames[NONE]          = "none";
        operationSymbols[NONE]        = "none";
        operationNames[ADD]           = "add";
        operationSymbols[ADD]         = "+";
        operationNames[SUBTRACT]      = "subtract";
        operationSymbols[SUBTRACT]    = "-";
        operationNames[MULTIPLY]      = "multiply";
        operationSymbols[MULTIPLY]    = "*";
        operationNames[DIVIDE]        = "divide";
        operationSymbols[DIVIDE]      = "/";
        operationNames[SQUARE]        = "square";
        operationNames[SQRT]          = "sqrt";
        operationNames[ABS]           = "abs";
        operationNames[POWER]         = "power";
        operationNames[LOGARITHM]     = "log";
        operationNames[NAT_LOG]       = "nat_log";
        operationNames[EXP]           = "exp";
        operationNames[SIN]           = "sin";
        operationNames[COS]           = "cos";
        operationNames[TAN]           = "tan";
        operationNames[ASIN]          = "asin";
        operationNames[ACOS]          = "acos";
        operationNames[ATAN]          = "atan";
        operationNames[SUMMATION]     = "summation";
    }

    public static String operationName(int operation) {
        if (operation < operationNames.length && operationNames[operation] != null) {
            return operationNames[operation];
        }
        return "?";
    }

    public static String operationSymbol(int operation) {
        if (operation < operationSymbols.length && operationSymbols[operation] != null) {
            return operationSymbols[operation];
        }
        return "?";
    }

}