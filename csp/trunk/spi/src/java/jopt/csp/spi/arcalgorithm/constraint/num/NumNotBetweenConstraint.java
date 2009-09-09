package jopt.csp.spi.arcalgorithm.constraint.num;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.global.NumBetweenConstraint;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.global.GenericNumNotBetweenArc;
import jopt.csp.spi.arcalgorithm.graph.arc.hyper.TernaryNumNotBetweenArc;
import jopt.csp.spi.arcalgorithm.graph.node.GenericNumNode;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.arcalgorithm.variable.GenericNumConstant;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.variable.CspGenericNumConstant;

/**
 * Z is not a subset of A, and does not exist between min and max
 */
public class NumNotBetweenConstraint extends NumRangeConstraint {
    
    
    
    public NumNotBetweenConstraint(NumExpr sourceMin, boolean minExclusive, NumExpr sourceMax, boolean maxExclusive, NumExpr z) {
        super(sourceMin, minExclusive, sourceMax, maxExclusive, z, null);
    }
    
    public NumNotBetweenConstraint(Number sourceMin, boolean minExclusive, Number sourceMax, boolean maxExclusive,NumExpr z) {
        super(sourceMin, minExclusive, sourceMax, maxExclusive, z, null);
    }
    
    public NumNotBetweenConstraint(CspGenericNumConstant sourceMin, boolean minExclusive, CspGenericNumConstant sourceMax, boolean maxExclusive,NumExpr z) {
        super(sourceMin, minExclusive, sourceMax, maxExclusive, z, null);
    }
    
    protected AbstractConstraint createConstraint(NumExpr sourceMin, boolean minExclusive, Number numMinConst, GenericNumConstant gcmin, NumExpr sourceMax, boolean maxExclusive,Number numMaxConst, GenericNumConstant gcmax, NumExpr z) {
        if ((sourceMin!=null)&&(sourceMax!=null)) {
            return new NumNotBetweenConstraint(sourceMin, minExclusive, sourceMax, maxExclusive, z);
        }
        else if ((gcmin!=null)&&(gcmax!=null)) {
            return new NumNotBetweenConstraint(gcmin, minExclusive, gcmax, maxExclusive, z);
        }
        else {
            return new NumNotBetweenConstraint(numMinConst, minExclusive, numMaxConst, maxExclusive, z);
        }
    }
    
    /**
     * Creates generic numeric arcs
     */
    protected Arc[] createGenericArcs() {
        GenericNumNode sourceMin = (sourceExprMin!=null) ? (GenericNumNode) sourceExprMin.getNode() : null;
        GenericNumNode sourceMax = (sourceExprMax!=null) ? (GenericNumNode) sourceExprMax.getNode() : null;
        GenericNumNode z = (zexpr!=null) ? (GenericNumNode) zexpr.getNode() : null;
        
        if (sourceMin!=null) {
            Arc a1 = new GenericNumNotBetweenArc(sourceMin, minExclusive, sourceMax, maxExclusive, z, nodeType);
            return new Arc[] { a1 };
        }
        else if(sourceConstMin!=null){
            Arc a1 = new GenericNumNotBetweenArc(sourceConstMin, minExclusive, sourceConstMax, maxExclusive, z, nodeType);
            return new Arc[] { a1 };
        }
        else{
            Arc a1 = new GenericNumNotBetweenArc(sourceGenConstMin, minExclusive, sourceGenConstMax, maxExclusive, z, nodeType);
            return new Arc[] { a1 };
        }
    }
    
    /**
     * Creates standard numeric arcs
     */
    protected Arc[] createStandardArcs() {
        NumNode sourceMin = (sourceExprMin!=null) ? (NumNode) sourceExprMin.getNode() : null;
        NumNode sourceMax = (sourceExprMax!=null) ? (NumNode) sourceExprMax.getNode() : null;
        NumNode z = (zexpr!=null) ? (NumNode) zexpr.getNode() : null;
        
        if ((sourceMin==null)&&(sourceMax==null)) {
            Arc a1 = new TernaryNumNotBetweenArc(sourceConstMin, minExclusive, sourceConstMax, maxExclusive, z, nodeType);
            return new Arc[] { a1 };
        }
        else if ((sourceMin!=null)&&(sourceMax==null)) {
            Arc a1 = new TernaryNumNotBetweenArc(sourceMin, minExclusive, sourceConstMax, maxExclusive, z, nodeType);
            return new Arc[] { a1 };
        }
        else if ((sourceMin==null)&&(sourceMax!=null)) {
            Arc a1 = new TernaryNumNotBetweenArc(sourceConstMin, minExclusive, sourceMax, maxExclusive, z, nodeType);
            return new Arc[] { a1 };
        }
        else {
            Arc a1 = new TernaryNumNotBetweenArc(sourceMin, minExclusive, sourceMax, maxExclusive, z, nodeType);
            return new Arc[] { a1 };
        }
    }
    
    // javadoc inherited
    public boolean violated() {
        double precision = getPrecision();
    //TODO: Is this needed anymore?
//            //If there is an expression within an expression, there is no way to know if it is violated for sure
//            //unless it is bound.
//            if((sourceExpr!=null)&&(!currentZ.isBound())) {
//                return false;
//            }
        Number runtimeMin=currentMin;
        Number runtimeMax=currentMax;
        if (runtimeMin==null) {
            if (currentGenSourceExprMin!=null) {
                runtimeMin = currentGenSourceExprMin.getNumMax();
            }
            else if (currentSourceExprMin!=null) {
                runtimeMin = currentSourceExprMin.getNumMax();
            }
        }
        if (runtimeMax==null) {
            if (currentGenSourceExprMax!=null) {
                runtimeMax = currentGenSourceExprMax.getNumMin();
            }
            else if (currentSourceExprMax!=null) {
                runtimeMax = currentSourceExprMax.getNumMin();
            }
        }
        
        int cmp = NumberMath.compare(runtimeMin, currentZ.getNumMin(), precision, nodeType);
        boolean zGreater = (minExclusive) ? cmp <= 0 : cmp < 0;
        
        cmp = NumberMath.compare(runtimeMax, currentZ.getNumMax(), precision, nodeType);
        boolean zLesser = (maxExclusive ? cmp >= 0 : cmp > 0);
        
        return (zGreater&&zLesser); 
    }
    
    protected AbstractConstraint createOpposite() {
        if ((sourceExprMin!=null)&&(sourceExprMax!=null)) {
            return new NumBetweenConstraint(sourceExprMin, minExclusive, sourceExprMax, maxExclusive, zexpr);
        }
        else if (sourceConstMin!=null) {
            return new NumBetweenConstraint(sourceConstMin, minExclusive, sourceConstMax, maxExclusive, zexpr);
        }
        else {
            return new NumBetweenConstraint(sourceGenConstMin, minExclusive, sourceGenConstMax, maxExclusive, zexpr);
        }
    }
}