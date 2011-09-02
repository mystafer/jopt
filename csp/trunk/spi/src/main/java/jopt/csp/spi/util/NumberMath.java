package jopt.csp.spi.util;

import java.math.BigInteger;

import jopt.csp.util.DoubleUtil;
import jopt.csp.util.FloatUtil;
import jopt.csp.util.LongUtil;

public class NumberMath implements NumConstants {
    /**
     * Adds 2 numbers and returns result in format of target node type
     * Sets invalid flag when when overflow occurs for Integer and Long 
     */
    public static void add(Number a, Number b, int resultType, MutableNumber result) {
        switch(resultType) {
            case INTEGER:
                long l = a.longValue() + b.longValue();
                if (l > Integer.MAX_VALUE) result.setInvalid(true);
                else if (l < Integer.MIN_VALUE) result.setInvalid(true);
                else result.setIntValue((int) l);
                break;
                
            case LONG:
                BigInteger bi = BigInteger.valueOf(a.longValue()).add(BigInteger.valueOf(b.longValue()));
                if (bi.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0) result.setInvalid(true);
                else if (bi.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0) result.setInvalid(true);
                else result.setLongValue(bi.longValue());
                break;
                
            case FLOAT:
                result.setFloatValue(a.floatValue() + b.floatValue());
                break;
                
            default:
                result.setDoubleValue(a.doubleValue() + b.doubleValue());
                break;
        }
    }

    /**
     * Adds 2 numbers and returns result in format of target node type
     * Returns min and max values when when overflow occurs for Integer and Long 
     */
    public static void addNoInvalid(Number a, Number b, int resultType, MutableNumber result) {
        switch(resultType) {
            case INTEGER:
                long l = a.longValue() + b.longValue();
                if (l > Integer.MAX_VALUE) result.setIntValue(Integer.MAX_VALUE);
                else if (l < Integer.MIN_VALUE) result.setIntValue(Integer.MIN_VALUE);
                else result.setIntValue((int) l);
                break;
                
            case LONG:
                BigInteger bi = BigInteger.valueOf(a.longValue()).add(BigInteger.valueOf(b.longValue()));
                if (bi.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0) result.setLongValue(Long.MAX_VALUE);
                else if (bi.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0) result.setLongValue(Long.MIN_VALUE);
                else result.setLongValue(bi.longValue());
                break;
                
            case FLOAT:
                result.setFloatValue(a.floatValue() + b.floatValue());
                break;
                
            default:
                result.setDoubleValue(a.doubleValue() + b.doubleValue());
                break;
        }
    }
    
    /**
     * Subtracts a from b and returns result in format of target node type
     * Sets invalid flag when when overflow occurs for Integer and Long 
     */
    public static void subtract(Number a, Number b, int resultType, MutableNumber result) {
        switch(resultType) {
            case INTEGER:
                long l = a.longValue() - b.longValue();
                if (l < Integer.MIN_VALUE) result.setInvalid(true);
                else if (l > Integer.MAX_VALUE) result.setInvalid(true);
                else result.setIntValue((int) l);
                break;
                
            case LONG:
                BigInteger bi = BigInteger.valueOf(a.longValue()).subtract(BigInteger.valueOf(b.longValue()));
                if (bi.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0) result.setInvalid(true);
                else if (bi.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0) result.setInvalid(true);
                else result.setLongValue(bi.longValue());
                break;
                
            case FLOAT:
                result.setFloatValue(a.floatValue() - b.floatValue());
                break;
                
            default:
                result.setDoubleValue(a.doubleValue() - b.doubleValue());
                break;
        }
    }
    
    /**
     * Subtracts a from b and returns result in format of target node type
     * Returns min and max values when when overflow occurs for Integer and Long 
     */
    public static void subtractNoInvalid(Number a, Number b, int resultType, MutableNumber result) {
        switch(resultType) {
            case INTEGER:
                long l = a.longValue() - b.longValue();
                if (l < Integer.MIN_VALUE) result.setIntValue(Integer.MIN_VALUE);
                else if (l > Integer.MAX_VALUE) result.setIntValue(Integer.MAX_VALUE);
                else result.setIntValue((int) l);
                break;
                
            case LONG:
                BigInteger bi = BigInteger.valueOf(a.longValue()).subtract(BigInteger.valueOf(b.longValue()));
                if (bi.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0) result.setLongValue(Long.MIN_VALUE);
                else if (bi.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0) result.setLongValue(Long.MAX_VALUE);
                else result.setLongValue(bi.longValue());
                break;
                
            case FLOAT:
                result.setFloatValue(a.floatValue() - b.floatValue());
                break;
                
            default:
                result.setDoubleValue(a.doubleValue() - b.doubleValue());
                break;
        }
    }
    
    /**
     * Multiplies 2 numbers and returns result in format of target node type
     * Sets invalid flag when overflow occurs for Integer and Long 
     */
    public static void multiply(Number a, Number b, int resultType, MutableNumber result) {
        switch(resultType) {
            case INTEGER:
                long l = a.longValue() * b.longValue();
                if (l > Integer.MAX_VALUE) result.setInvalid(true);
                else if (l < Integer.MIN_VALUE) result.setInvalid(true);
                else result.setIntValue((int) l);
                break;
                
            case LONG:
                BigInteger bi = BigInteger.valueOf(a.longValue()).multiply(BigInteger.valueOf(b.longValue()));
                if (bi.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0) result.setInvalid(true);
                else if (bi.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0) result.setInvalid(true);
                else result.setLongValue(bi.longValue());
                break;
                
            case FLOAT:
                result.setFloatValue(a.floatValue() * b.floatValue());
                break;
                
            default:
                result.setDoubleValue(a.doubleValue() * b.doubleValue());
                break;
        }
    }

    /**
     * Multiplies 2 numbers and returns result in format of target node type
     * Returns min and max values when when overflow occurs for Integer and Long 
     */
    public static void multiplyNoInvalid(Number a, Number b, int resultType, MutableNumber result) {
        switch(resultType) {
            case INTEGER:
                long l = a.longValue() * b.longValue();
                if (l > Integer.MAX_VALUE) result.setIntValue(Integer.MAX_VALUE);
                else if (l < Integer.MIN_VALUE) result.setIntValue(Integer.MIN_VALUE);
                else result.setIntValue((int) l);
                break;
                
            case LONG:
                BigInteger bi = BigInteger.valueOf(a.longValue()).multiply(BigInteger.valueOf(b.longValue()));
                if (bi.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0) result.setLongValue(Long.MAX_VALUE);
                else if (bi.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0) result.setLongValue(Long.MIN_VALUE);
                else result.setLongValue(bi.longValue());
                break;
                
            case FLOAT:
                result.setFloatValue(a.floatValue() * b.floatValue());
                break;
                
            default:
                result.setDoubleValue(a.doubleValue() * b.doubleValue());
                break;
        }
    }

    /**
     * Divides a by b and returns result in format of target node type
     * Returns next higher whole number if result type is integer or long and result is
     * fractional or outside int / long ranges.
     */
    public static void divideCeil(Number a, Number b, int resultType, MutableNumber result) {
        switch(resultType) {
            case INTEGER:
                result.setIntValue(DoubleUtil.intCeil(a.doubleValue() / b.doubleValue()));
                break;
                
            case LONG:
                result.setLongValue(DoubleUtil.longCeil(a.doubleValue() / b.doubleValue()));
                break;
                
            case FLOAT:
                result.setFloatValue(a.floatValue() / b.floatValue());
                break;
                
            default:
                result.setDoubleValue(a.doubleValue() / b.doubleValue());
                break;
        }
    }
    
    /**
     * Divides a by b and returns result in format of target node type
     * Returns next lower whole number if result type is integer or long and result is
     * fractional or outside int / long ranges.
     */
    public static void divideFloor(Number a, Number b, int resultType, MutableNumber result) {
        switch(resultType) {
            case INTEGER:
                result.setIntValue(DoubleUtil.intFloor(a.doubleValue() / b.doubleValue()));
                break;
                
            case LONG:
                result.setLongValue(DoubleUtil.longFloor(a.doubleValue() / b.doubleValue()));
                break;
                
            case FLOAT:
                result.setFloatValue(a.floatValue() / b.floatValue());
                break;
                
            default:
                result.setDoubleValue(a.doubleValue() / b.doubleValue());
                break;
        }
    }
    
    /**
     * Divides a by b and returns result in format of target node type
     * Sets invalid flag if result type is integer or long and result is
     * fractional or outside int / long ranges.
     */
    public static void divide(Number a, Number b, int resultType, MutableNumber result) {
        switch(resultType) {
            case FLOAT:
                result.setFloatValue(a.floatValue() / b.floatValue());
                break;
                
            default:
                double d = a.doubleValue() / b.doubleValue();
            
                // convert to integer types if necessary
                switch(resultType) {
                    case INTEGER:
                        if (DoubleUtil.isIntEquivalent(d)) result.setIntValue((int) d);
                        else result.setInvalid(true);
                        break;
                        
                    case LONG:
                        if (DoubleUtil.isLongEquivalent(d)) result.setLongValue((long) d);
                        else result.setInvalid(true);
                        break;
                        
                    default:
                        result.setDoubleValue(d);
                        break;
                }
                break;
        }
    }
    
    /**
     * Divides a by b and returns result in format of target node type
     * Sets min and max values for Integer and Long when overflow occurs
     */
    public static void divideNoInvalid(Number a, Number b, int resultType, MutableNumber result) {
        switch(resultType) {
            case INTEGER:
                result.setIntValue((int) (a.doubleValue() / b.doubleValue()));
                break;
                
            case LONG:
                result.setLongValue((long) (a.doubleValue() / b.doubleValue()));
                break;
                
            case FLOAT:
                result.setFloatValue(a.floatValue() / b.floatValue());
                break;
                
            default:
                result.setDoubleValue(a.doubleValue() / b.doubleValue());
                break;
        }
    }
    
    /**
     * Returns number squared in format of target node type
     * Sets invalid flag for Integer and Long when overflow occurs
     */
    public static void square(Number a, int resultType, MutableNumber result) {
        switch(resultType) {
            case INTEGER:
                long l = a.longValue() * a.longValue();
                if (l > Integer.MAX_VALUE) result.setInvalid(true);
                else result.setIntValue((int) l);
                break;
                
            case LONG:
                BigInteger bi = BigInteger.valueOf(a.longValue()).pow(2);
                if (bi.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0) result.setInvalid(true);
                else result.setLongValue(bi.longValue());
                break;
                
            case FLOAT:
                result.setFloatValue(a.floatValue() * a.floatValue());
                break;
                
            default:
                result.setDoubleValue(a.doubleValue() * a.doubleValue());
                break;
        }
    }

    /**
     * Returns number squared in format of target node type
     * Returns min and max values for Integer and Long when overflow occurs
     */
    public static void squareNoInvalid(Number a, int resultType, MutableNumber result) {
        switch(resultType) {
            case INTEGER:
                long l = a.longValue() * a.longValue();
                if (l > Integer.MAX_VALUE) result.setIntValue(Integer.MAX_VALUE);
                else result.setIntValue((int) l);
                break;
                
            case LONG:
                BigInteger bi = BigInteger.valueOf(a.longValue()).pow(2);
                if (bi.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0) result.setLongValue(Long.MAX_VALUE);
                else result.setLongValue(bi.longValue());
                break;
                
            case FLOAT:
                result.setFloatValue(a.floatValue() * a.floatValue());
                break;
                
            default:
                result.setDoubleValue(a.doubleValue() * a.doubleValue());
                break;
        }
    }


    /**
     * Calculates square root of a and returns result in format of target node type
     * Sets invalid flag if result type is integer or long and result is
     * fractional or outside int / long ranges.
     */
    public static void sqrt(Number a, int resultType, MutableNumber result) {
        double d = Math.sqrt(a.doubleValue());
        
        switch(resultType) {
            case INTEGER:
                if (DoubleUtil.isIntEquivalent(d))
                    result.setIntValue((int) d);
                else
                    result.setInvalid(true);
                break;
                
            case LONG:
                if (DoubleUtil.isLongEquivalent(d))
                    result.setLongValue((long) d);
                else
                    result.setInvalid(true);
                break;
                
            case FLOAT:
                result.setFloatValue((float) d);
                break;
                
            default:
                result.setDoubleValue(d);
                break;
        }
    }
    
    /**
     * Calculates square root of a and returns result in format of target node type
     * Returns next higher whole number if result type is integer or long and result is
     * fractional or outside int / long ranges.
     */
    public static void sqrtCeil(Number a, int resultType, MutableNumber result) {
        double d = Math.sqrt(a.doubleValue());
        
        switch(resultType) {
            case INTEGER:
                result.setIntValue(DoubleUtil.intCeil(d));
                break;
                
            case LONG:
                result.setLongValue(DoubleUtil.longCeil(d));
                break;
                
            case FLOAT:
                result.setFloatValue((float) d);
                break;
                
            default:
                result.setDoubleValue(d);
                break;
        }
    }
    
    /**
     * Calculates square root of a and returns result in format of target node type
     * Returns next lower whole number if result type is integer or long and result is
     * fractional or outside int / long ranges.
     */
    public static void sqrtFloor(Number a, int resultType, MutableNumber result) {
        double d = Math.sqrt(a.doubleValue());
        
        switch(resultType) {
            case INTEGER:
                result.setIntValue(DoubleUtil.intFloor(d));
                break;
                
            case LONG:
                result.setLongValue(DoubleUtil.longFloor(d));
                break;
                
            case FLOAT:
                result.setFloatValue((float) d);
                break;
                
            default:
                result.setDoubleValue(d);
                break;
        }
    }
    
    /**
     * Calculates square root of a and returns result in format of target node type
     */
    public static void sqrtNoInvalid(Number a, int resultType, MutableNumber result) {
        double d = Math.sqrt(a.doubleValue());
        
        switch(resultType) {
            case INTEGER:
                result.setIntValue((int) d);
                break;
                
            case LONG:
                result.setLongValue((long) d);
                break;
                
            case FLOAT:
                result.setFloatValue((float) d);
                break;
                
            default:
                result.setDoubleValue(d);
                break;
        }
    }
    
    /**
     * Returns maximum of 2 numbers
     */
    public static void max(Number a, Number b, MutableNumber result) {
        switch(Math.max(numberType(a), numberType(b))) {
            case INTEGER:
                int ai = a.intValue();
                int bi = b.intValue();
                if (ai > bi) 
                    result.setIntValue(ai);
                else
                    result.setIntValue(bi);
                break;
                
            case LONG:
                long al = a.longValue();
                long bl = b.longValue();
                if (al > bl)
                    result.setLongValue(al);
                else
                    result.setLongValue(bl);
                break;
                
            case FLOAT:
                float af = a.floatValue();
                float bf = b.floatValue();
                if (af > bf)
                    result.setFloatValue(af);
                else
                    result.setFloatValue(bf);
                break;
                
            default:
                double ad = a.doubleValue();
                double bd = b.doubleValue();
                if (ad > bd)
                    result.setDoubleValue(ad);
                else
                    result.setDoubleValue(bd);
                break;
        }
    }
    
    /**
     * Returns minimum of 2 numbers
     */
    public static void min(Number a, Number b, MutableNumber result) {
        switch(Math.max(numberType(a), numberType(b))) {
            case INTEGER:
                int ai = a.intValue();
                int bi = b.intValue();
                if (ai < bi) 
                    result.setIntValue(ai);
                else
                    result.setIntValue(bi);
                break;
                
            case LONG:
                long al = a.longValue();
                long bl = b.longValue();
                if (al < bl)
                    result.setLongValue(al);
                else
                    result.setLongValue(bl);
                break;
                
            case FLOAT:
                float af = a.floatValue();
                float bf = b.floatValue();
                if (af < bf)
                    result.setFloatValue(af);
                else
                    result.setFloatValue(bf);
                break;
                
            default:
                double ad = a.doubleValue();
                double bd = b.doubleValue();
                if (ad < bd)
                    result.setDoubleValue(ad);
                else
                    result.setDoubleValue(bd);
                break;
        }
    }
    
    /**
     * Helper function to return maximum value
     */
    public static void max(Number p1, Number p2, Number p3, Number p4, MutableNumber result) {
        max(p1, p2, result);
        max(result, p3, result);
        max(result, p4, result);
    }
    
    /**
     * Helper function to return maximum value
     */
    public static void max(Number p1, Number p2, Number p3, MutableNumber result) {
        max(p1, p2, result);
        max(result, p3, result);
    }
        
    /**
     * Helper function to return minimum value
     */
    public static void min(Number p1, Number p2, Number p3, Number p4, MutableNumber result) {
        min(p1, p2, result);
        min(result, p3, result);
        min(result, p4, result);
    }
    
    /**
     * Helper function to return minimum value
     */
    public static void min(Number p1, Number p2, Number p3, MutableNumber result) {
        min(p1, p2, result);
        min(result, p3, result);
    }    
    
    /**
     * Returns absolute value of a number
     */
    public static void abs(Number n, MutableNumber result) {
        switch(numberType(n)) {
            case INTEGER:
                result.setIntValue(Math.abs(n.intValue()));
                break;
                
            case LONG:
                result.setLongValue(Math.abs(n.longValue()));
                break;
                
            case FLOAT:
                result.setFloatValue(Math.abs(n.floatValue()));
                break;
                
            default:
                result.setDoubleValue(Math.abs(n.doubleValue()));
                break;
        }
    }
    
    /**
     * Returns negative value of a number
     */
    public static void neg(Number n, MutableNumber result) {
        switch(numberType(n)) {
            case INTEGER:
                result.setIntValue(-n.intValue());
                break;
                
            case LONG:
                result.setLongValue(-n.longValue());
                break;
                
            case FLOAT:
                result.setFloatValue(-n.floatValue());
                break;
                
            default:
                result.setDoubleValue(-n.doubleValue());
                break;
        }
    }
    
    /**
     * Returns the node type for a given number
     */
    public static int numberType(Number n) {
        if (n instanceof Integer) return INTEGER;
        if (n instanceof Long) return LONG;
        if (n instanceof Float) return FLOAT;
        if (n instanceof Double) return DOUBLE;
        if (n instanceof MutableNumber) return ((MutableNumber) n).getType();
        return DOUBLE;
    }

    /**
     * Returns true if node type is a Float or Double
     */
    public static boolean isRealType(int numberType) {
        return numberType == FLOAT || numberType == DOUBLE;
    }

    /**
     * Returns true if a number is equal to zero
     */
    public static boolean isZero(Number n) {
        switch(numberType(n)) {
            case INTEGER:
                return n.intValue() == 0;
                
            case LONG:
                return n.longValue() == 0L;
                
            case FLOAT:
                return n.floatValue() == 0f;
                
            default:
                return n.doubleValue() == 0d;
        }
    }

    /**
     * Returns true if a number is less than zero
     */
    public static boolean isNegative(Number n) {
        switch(numberType(n)) {
            case INTEGER:
                return n.intValue() < 0;
                
            case LONG:
                return n.longValue() < 0L;
                
            case FLOAT:
                return n.floatValue() < 0f;
                
            default:
                return n.doubleValue() < 0d;
        }
    }

    /**
     * Returns true if a number is greater than zero
     */
    public static boolean isPositive(Number n) {
        switch(numberType(n)) {
            case INTEGER:
                return n.intValue() > 0;
                
            case LONG:
                return n.longValue() > 0L;
                
            case FLOAT:
                return n.floatValue() > 0f;
                
            default:
                return n.doubleValue() > 0d;
        }
    }

    /**
     * Returns zero for the specified number type
     */
    public static Number zero(int numberType) {
        switch(numberType) {
            case INTEGER:
                return NumConstants.INTEGER_ZERO;
                
            case LONG:
                return NumConstants.LONG_ZERO;
                
            case FLOAT:
                return NumConstants.FLOAT_ZERO;
                
            default:
                return NumConstants.DOUBLE_ZERO;
        }
    }

    /**
     * Returns maximum possible value for the specified number type
     */
    public static Number maxValue(int numberType) {
        switch(numberType) {
            case INTEGER:
                return NumConstants.INTEGER_MAX;
                
            case LONG:
                return NumConstants.LONG_MAX;
                
            case FLOAT:
                return NumConstants.FLOAT_POS_INF;
                
            default:
                return NumConstants.DOUBLE_POS_INF;
        }
    }

    /**
     * Returns minimum possible value for the specified number type
     */
    public static Number minValue(int numberType) {
        switch(numberType) {
            case INTEGER:
                return NumConstants.INTEGER_MIN;
                
            case LONG:
                return NumConstants.LONG_MIN;
                
            case FLOAT:
                return NumConstants.FLOAT_NEG_INF;
                
            default:
                return NumConstants.DOUBLE_NEG_INF;
        }
    }

    /**
     * Returns minimum possible value for the specified number type
     */
    public static int compare(Number n1, Number n2, double precision, int numberType) {
        switch(numberType) {
            case INTEGER:
                if (n1.intValue() == n2.intValue()) return 0;
                if (n1.intValue() < n2.intValue()) return -1;
                return 1;
                
            case LONG:
                if (n1.longValue() == n2.longValue()) return 0;
                if (n1.longValue() < n2.longValue()) return -1;
                return 1;
                
            case FLOAT:
                return FloatUtil.compare(n1.floatValue(), n2.floatValue(), (float) precision);
                
            default:
                return DoubleUtil.compare(n1.doubleValue(), n2.doubleValue(), precision);
        }
    }

    /**
     * Compares Zero to number given.  
     *
     * @see java.lang.Comparable
     */
    public static int compareToZero(Number n, double precision) {
        if (n instanceof Integer) {
            Integer i = (Integer) n;
            if (i.intValue() > 0) return 1;
            if (i.intValue() < 0) return -1;
            return 0;
        }
        if (n instanceof Long) {
            Long l = (Long) n;
            if (l.intValue() > 0) return 1;
            if (l.intValue() < 0) return -1;
            return 0;
        }
        if (n instanceof Float) {
            return FloatUtil.compare(n.floatValue(), 0f, (float) precision);
        }
        else {
            return DoubleUtil.compare(n.doubleValue(), 0d, precision);
        }
    }

    /**
     * Returns integer value for a number rounding using ceiling
     * method if necessary
     */
    public static int intCeil(Number n) {
        switch(numberType(n)) {
            case INTEGER:
                return n.intValue();
                
            case LONG:
                return LongUtil.intValue(n.longValue());
                
            case FLOAT:
                return FloatUtil.intCeil(n.floatValue());
                
            default:
                return DoubleUtil.intCeil(n.doubleValue());
        }
    }

    /**
     * Returns integer value for a number rounding using floor
     * method if necessary
     */
    public static int intFloor(Number n) {
        switch(numberType(n)) {
            case INTEGER:
                return n.intValue();
                
            case LONG:
                return LongUtil.intValue(n.longValue());
                
            case FLOAT:
                return FloatUtil.intFloor(n.floatValue());
                
            default:
                return DoubleUtil.intFloor(n.doubleValue());
        }
    }


    /**
     * Returns true if value is equivalent to an integer value
     */
    public static boolean isIntEquivalent(Number n) {
        switch(numberType(n)) {
            case INTEGER:
                return true;
                
            case LONG:
                return LongUtil.isIntEquivalent(n.longValue());
                
            case FLOAT:
                return FloatUtil.isIntEquivalent(n.floatValue());
                
            default:
                return DoubleUtil.isIntEquivalent(n.doubleValue());
        }
   }


    /**
     * Returns long value for a number rounding using ceiling
     * method if necessary
     */
    public static long longCeil(Number n) {
        switch(numberType(n)) {
            case INTEGER:
                return n.intValue();
                
            case LONG:
                return n.longValue();
                
            case FLOAT:
                return FloatUtil.longCeil(n.floatValue());
                
            default:
                return DoubleUtil.longCeil(n.doubleValue());
        }
    }

    /**
     * Returns long value for a number rounding using floor
     * method if necessary
     */
    public static long longFloor(Number n) {
        switch(numberType(n)) {
            case INTEGER:
                return n.intValue();
                
            case LONG:
                return n.longValue();
                
            case FLOAT:
                return FloatUtil.longFloor(n.floatValue());
                
            default:
                return DoubleUtil.longFloor(n.doubleValue());
        }
    }

    /**
     * Returns true if value is equivalent to a long value
     */
    public static boolean isLongEquivalent(Number n) {
        switch(numberType(n)) {
            case INTEGER:
                return true;
                
            case LONG:
                return true;
                
            case FLOAT:
                return FloatUtil.isLongEquivalent(n.floatValue());
                
            default:
                return DoubleUtil.isLongEquivalent(n.doubleValue());
        }
   }

    /**
     * Returns next value in sequence for a given number's type
     */
    public static void next(Number n, double precision, MutableNumber result) {
        switch(numberType(n)) {
            case INTEGER:
                int ival = n.intValue();
                if (ival >= Integer.MAX_VALUE)
                    result.setIntValue(Integer.MAX_VALUE);
                else
                    result.setIntValue(ival+1);
                break;
                
            case LONG:
                long lval = n.longValue();
                if (lval >= Long.MAX_VALUE)
                    result.setLongValue(Long.MAX_VALUE);
                else
                    result.setLongValue(lval+1);
                break;
                
            case FLOAT:
                result.setFloatValue(FloatUtil.next((n.floatValue() + (float) precision)));
                break;
                
            default:
                result.setDoubleValue(DoubleUtil.next(n.doubleValue() + precision));
                break;
        }
   }


    /**
     * Returns previous value in sequence for a given number's type
     */
    public static void previous(Number n, double precision, MutableNumber result) {
        switch(numberType(n)) {
            case INTEGER:
                int ival = n.intValue();
                if (ival <= Integer.MIN_VALUE)
                    result.setIntValue(Integer.MIN_VALUE);
                else
                    result.setIntValue(ival-1);
                break;
                
            case LONG:
                long lval = n.longValue();
                if (lval <= Long.MIN_VALUE)
                    result.setLongValue(Long.MIN_VALUE);
                else
                    result.setLongValue(lval-1);
                break;
                
            case FLOAT:
                result.setFloatValue(FloatUtil.previous(n.floatValue() - (float) precision));
                break;
                
            default:
                result.setDoubleValue(DoubleUtil.previous(n.doubleValue() - precision));
                break;
        }
    }
    
    /**
     * Ensures that number is immutable and returns java.lang.Number equivalent for any MutableNumber
     */
    public static Number toConst(Number n) {
    	if (n instanceof MutableNumber) return ((MutableNumber) n).toConst();
        return n;
    }
}
