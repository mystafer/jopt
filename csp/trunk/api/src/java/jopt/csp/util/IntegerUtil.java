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
 * Integer type utility functions 
 */
public class IntegerUtil {
    /**
     * Returns minimum value of a numeric expression as an int type
     */
    public static int getMin(CspNumExpr expr) {
        if (expr instanceof CspIntExpr)
            return ((CspIntExpr) expr).getMin();
        
        if (expr instanceof CspLongExpr)
            return LongUtil.intValue(((CspLongExpr) expr).getMin());
        
        if (expr instanceof CspFloatExpr)
            return FloatUtil.intCeil(((CspFloatExpr) expr).getMin());
        
        return DoubleUtil.intCeil(((CspDoubleExpr) expr).getMin());
    }
    
    /**
     * Returns maximum value of a numeric expression as an int type
     */
    public static int getMax(CspNumExpr expr) {
        if (expr instanceof CspIntExpr)
            return ((CspIntExpr) expr).getMax();
        
        if (expr instanceof CspLongExpr)
            return LongUtil.intValue(((CspLongExpr) expr).getMax());
        
        if (expr instanceof CspFloatExpr)
            return FloatUtil.intFloor(((CspFloatExpr) expr).getMax());
        
        return DoubleUtil.intFloor(((CspDoubleExpr) expr).getMax());
    }

    /**
     * Returns minimum value of a numeric expression as a double type
     */
    public static int getMin(VariableSolution sol) {
        if (sol instanceof IntSolution)
            return ((IntSolution) sol).getMin();
        
        if (sol instanceof LongSolution)
            return LongUtil.intValue(((LongSolution) sol).getMin());
        
        if (sol instanceof FloatSolution)
            return FloatUtil.intCeil(((FloatSolution) sol).getMin());
        
        return DoubleUtil.intCeil(((DoubleSolution) sol).getMin());
    }
    
    /**
     * Returns maximum value of a numeric expression as a double type
     */
    public static int getMax(VariableSolution sol) {
        if (sol instanceof IntSolution)
            return ((IntSolution) sol).getMax();
        
        if (sol instanceof LongSolution)
            return LongUtil.intValue(((LongSolution) sol).getMax());
        
        if (sol instanceof FloatSolution)
            return FloatUtil.intFloor(((FloatSolution) sol).getMax());
        
        return DoubleUtil.intFloor(((DoubleSolution) sol).getMax());
    }
}