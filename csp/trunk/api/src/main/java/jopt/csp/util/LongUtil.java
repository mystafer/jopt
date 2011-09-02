package jopt.csp.util;

import jopt.csp.solution.DoubleSolution;
import jopt.csp.solution.FloatSolution;
import jopt.csp.solution.IntSolution;
import jopt.csp.solution.LongSolution;
import jopt.csp.solution.VariableSolution;
import jopt.csp.variable.CspDoubleExpr;
import jopt.csp.variable.CspFloatExpr;
import jopt.csp.variable.CspIntExpr;
import jopt.csp.variable.CspLongExpr;
import jopt.csp.variable.CspNumExpr;


/**
 * Long type utility functions 
 */
public class LongUtil {
    /**
     * Returns true if value is equivalent to an integer value
     */
    public static boolean isIntEquivalent(long val) {
        return Integer.MIN_VALUE <= val && val <= Integer.MAX_VALUE;
    }

    /**
     * Returns equivalent integer value for a given long value.  Will
     * return integer min / max value in case of overflow
     */
    public static int intValue(long val) {
        if (val >= Integer.MAX_VALUE) return Integer.MAX_VALUE;
        if (val <= Integer.MIN_VALUE) return Integer.MIN_VALUE;
        return (int) val;
    }

    /**
     * Returns minimum value of a numeric expression as a long type
     */
    public static long getMin(CspNumExpr expr) {
        if (expr instanceof CspIntExpr)
            return ((CspIntExpr) expr).getMin();
        
        if (expr instanceof CspLongExpr)
            return ((CspLongExpr) expr).getMin();
        
        if (expr instanceof CspFloatExpr)
            return FloatUtil.longCeil(((CspFloatExpr) expr).getMin());
        
        return DoubleUtil.longCeil(((CspDoubleExpr) expr).getMin());
    }
    
    /**
     * Returns maximum value of a numeric expression as a long type
     */
    public static long getMax(CspNumExpr expr) {
        if (expr instanceof CspIntExpr)
            return ((CspIntExpr) expr).getMax();
        
        if (expr instanceof CspLongExpr)
            return ((CspLongExpr) expr).getMax();
        
        if (expr instanceof CspFloatExpr)
            return FloatUtil.longFloor(((CspFloatExpr) expr).getMax());
        
        return DoubleUtil.longFloor(((CspDoubleExpr) expr).getMax());
    }

    /**
     * Returns minimum value of a numeric expression as a double type
     */
    public static long getMin(VariableSolution sol) {
        if (sol instanceof IntSolution)
            return ((IntSolution) sol).getMin();
        
        if (sol instanceof LongSolution)
            return ((LongSolution) sol).getMin();
        
        if (sol instanceof FloatSolution)
            return FloatUtil.longCeil(((FloatSolution) sol).getMin());
        
        return DoubleUtil.longCeil(((DoubleSolution) sol).getMin());
    }
    
    /**
     * Returns maximum value of a numeric expression as a double type
     */
    public static long getMax(VariableSolution sol) {
        if (sol instanceof IntSolution)
            return ((IntSolution) sol).getMax();
        
        if (sol instanceof LongSolution)
            return ((LongSolution) sol).getMax();
        
        if (sol instanceof FloatSolution)
            return FloatUtil.longFloor(((FloatSolution) sol).getMax());
        
        return DoubleUtil.longFloor(((DoubleSolution) sol).getMax());
    }
}