package jopt.csp.spi.util;


/**
 * Utility for finding the a numeric value that is supported by a given primitive type.
 * Functions are used to find a value that is immediately before or after a given value. 
 */
public class NumSequence {
//    /**
//     * Compares two values
//     * 
//     * @param val1          First value to compare       
//     * @param val2          Second value to compare
//     */
//    public static int compare(Number val1, Number val2) {
//        int type = Math.max(NumberMath.numberType(val1), NumberMath.numberType(val2));
//        
//        switch(type) {
//        	case INTEGER:
//                int i1 = val1.intValue();
//                int i2 = val2.intValue();
//                if (i1==i2) return 0;
//                else if (i1 < i2) return -1;
//                return 1;
//                
//            case LONG:
//                long l1 = val1.longValue();
//                long l2 = val2.longValue();
//                if (l1==l2) return 0;
//                else if (l1 < l2) return -1;
//                return 1;
//                
//            case FLOAT:
//                float f1 = val1.floatValue();
//                float f2 = val2.floatValue();
//                if (f1==f2) return 0;
//                else if (f1 < f2) return -1;
//                return 1;
//                
//            default:
//                double d1 = val1.doubleValue();
//                double d2 = val2.doubleValue();
//                if (d1==d2) return 0;
//                else if (d1 < d2) return -1;
//                return 1;
//        }
//    }
//    
//    /**
//     * Compares two values using a given precision to determine if they are equal
//     * 
//     * @param val1          First value to compare       
//     * @param val2          Second value to compare
//     * @param precision     Precision values must fall within to be equal
//     */
//    public static int compare(Number val1, Number val2, double precision, int numberType) {
//        switch(numberType) {
//            case INTEGER:
//            case LONG:
//                return compare(val1, val2);
//                
//            default:
//                if (precision < 0)
//                    return compare(val1, val2);
//                else
//                    return compare(val1.doubleValue(), val2.doubleValue(), precision);
//        }
//    }
//    
//    /**
//     * Returns true if two values are within precision of one another
//     * 
//     * @param val1          First value to compare       
//     * @param val2          Second value to compare
//     * @param precision     Precision values must fall within to return true.
//     */
//    public static boolean isEqual(Number val1, Number val2, double precision, int numberType) {
//        switch(numberType) {
//            case INTEGER:
//                return val1.intValue() == val2.intValue();
//                
//            case LONG:
//                return val1.longValue() == val2.longValue();
//                
//            default:
//                return isEqual(val1.doubleValue(), val2.doubleValue(), precision);
//        }
//    }
//    
//    /**
//     * Compares two values using a given precision to determine if they are equal
//     * 
//     * @param val1          First value to compare       
//     * @param val2          Second value to compare
//     * @param precision     Precision values must fall within to be equal
//     */
//    public static int compare(double val1, double val2, double precision) {
//        if (isEqual(val1, val2, precision))
//            return 0;
//        else if (val1 < val2)
//            return -1;
//        else
//            return 1;
//    }
//    
//    /**
//     * Returns true if two values are within precision of one another
//     * 
//     * @param val1          First value to compare       
//     * @param val2          Second value to compare
//     * @param precision     Precision values must fall within to return true.
//     */
//    public static boolean isEqual(double val1, double val2, double precision) {
//        if (val1 == val2) return true;
//        
//        // ensure precision is not less than minimum precision
//        precision = Math.max(precision, DEFAULT_PRECISION);
//        
//        // swap infinity values with largest / smallest values
//        if (val1 == Double.NEGATIVE_INFINITY)
//            val1 = -Double.MAX_VALUE;
//        else if (val1 == Double.POSITIVE_INFINITY)
//            val1 = Double.MAX_VALUE;
//        
//        if (val2 == Double.NEGATIVE_INFINITY)
//            val2 = -Double.MAX_VALUE;
//        else if (val2 == Double.POSITIVE_INFINITY)
//            val2 = Double.MAX_VALUE;
//        
//        // determine minimum and maximum values
//        double min = Math.min(val1, val2);
//        double max = Math.max(val1, val2);
//        double divisor = Math.max(1, Math.abs(min));
//        
//        double diff = max - min;
//        double p = diff / divisor;
//        return p <= precision;
//    }
}