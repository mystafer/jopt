package jopt.csp.spi.util;

/**
 * Enumeration of common values used by constraints as well as
 * some common mathematical constants.
 */
public interface NumConstants {
    public static final int EQ  = 0;
    public static final int LT  = 1;
    public static final int LEQ = 2;
    public static final int GT  = 3;
    public static final int GEQ = 4;
    public static final int NEQ = 5;
    public static final int RANGE = 6;
    
    public final static double HALF_PI = 0.5d * Math.PI;
    public final static double PI = Math.PI;
    public final static double ONE_AND_HALF_PI = 1.5d * Math.PI;
    public final static double TWO_PI = 2.0d * Math.PI;
    
    public final static int INTEGER    = 0;
    public final static int LONG       = 1;
    public final static int FLOAT      = 2;
    public final static int DOUBLE     = 3;
    
    public final static int ADD				=0;
    public final static int SUBTRACT 		=1;
    public final static int SUBTRACT_FROM 	=2;
    public final static int MULTIPLY		=3;
    public final static int DIVIDE			=4;
    public final static int DIVIDE_CEIL		=5;
    public final static int DIVIDE_FLOOR	=6;    
    public final static int DIVIDE_BY		=7;
    public final static int DIVIDE_BY_CEIL	=8;
    public final static int DIVIDE_BY_FLOOR =9;

    public static final Integer INTEGER_ZERO   = new Integer(0);
    public static final Long LONG_ZERO         = new Long(0);
    public static final Float FLOAT_ZERO       = new Float(0);
    public static final Double DOUBLE_ZERO     = new Double(0);

    public static final Integer INTEGER_MAX    = new Integer(Integer.MAX_VALUE);
    public static final Long LONG_MAX          = new Long(Long.MAX_VALUE);
    public static final Float FLOAT_POS_INF    = new Float(Float.POSITIVE_INFINITY);
    public static final Double DOUBLE_POS_INF  = new Double(Double.POSITIVE_INFINITY);

    public static final Integer INTEGER_MIN    = new Integer(Integer.MIN_VALUE);
    public static final Long LONG_MIN          = new Long(Long.MIN_VALUE);
    public static final Float FLOAT_NEG_INF    = new Float(Float.NEGATIVE_INFINITY);
    public static final Double DOUBLE_NEG_INF  = new Double(Double.NEGATIVE_INFINITY);
}

