package jopt.csp.spi.arcalgorithm.variable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import jopt.csp.spi.arcalgorithm.constraint.num.GenericNumExpr;
import jopt.csp.spi.arcalgorithm.constraint.num.NumExpr;
import jopt.csp.spi.arcalgorithm.constraint.num.global.GlobalCardinalityConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.global.GlobalCardinalityCountConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.global.MemberOfArray;
import jopt.csp.spi.arcalgorithm.constraint.num.global.NotMemberOfArray;
import jopt.csp.spi.arcalgorithm.constraint.num.global.NumAllDiffConstraint;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.IndexIterator;
import jopt.csp.spi.util.NameUtil;
import jopt.csp.spi.util.NumOperation;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspDoubleCast;
import jopt.csp.variable.CspDoubleExpr;
import jopt.csp.variable.CspFloatCast;
import jopt.csp.variable.CspFloatExpr;
import jopt.csp.variable.CspGenericDoubleExpr;
import jopt.csp.variable.CspGenericFloatExpr;
import jopt.csp.variable.CspGenericIndex;
import jopt.csp.variable.CspGenericIndexRestriction;
import jopt.csp.variable.CspGenericIntExpr;
import jopt.csp.variable.CspGenericLongExpr;
import jopt.csp.variable.CspIntExpr;
import jopt.csp.variable.CspLongCast;
import jopt.csp.variable.CspLongExpr;
import jopt.csp.variable.CspMath;
import jopt.csp.variable.CspNumExpr;

/**
 * Utility class for creating advanced mathematical expressions. 
 */
public class VarMath implements CspMath {
    private final static String sigma = Character.toString((char) 0x3A3); 
    private final static String delta = Character.toString((char) 0x394); 

    /**
     * Constructor
     */
    public VarMath() {
    }

    public CspIntExpr add(int a, CspIntExpr bexpr) {
        if (bexpr instanceof GenericIntExpr)
	    return new GenericIntExpr(null, a, NumOperation.ADD, (GenericIntExpr)bexpr);
	else
	    return new IntExpr(null, a, NumOperation.ADD, (IntExpr)bexpr);
    }
    public CspLongExpr add(int a, CspLongExpr bexpr) {
	if (bexpr instanceof GenericLongExpr)
	    return new GenericLongExpr(null, a, NumOperation.ADD, (GenericLongExpr)bexpr);
	else
	    return new LongExpr(null, a, NumOperation.ADD, (LongCast)bexpr);
    }
    public CspFloatExpr add(int a, CspFloatExpr bexpr) {
	if (bexpr instanceof GenericFloatExpr)
	    return new GenericFloatExpr(null, a, NumOperation.ADD, (GenericFloatExpr)bexpr);
	else
	    return new FloatExpr(null, a, NumOperation.ADD, (FloatCast)bexpr);
    }
    public CspDoubleExpr add(int a, CspDoubleExpr bexpr) {
	if (bexpr instanceof GenericDoubleExpr)
	    return new GenericDoubleExpr(null, a, NumOperation.ADD, (GenericDoubleExpr)bexpr);
	else
	    return new DoubleExpr(null, a, NumOperation.ADD, (DoubleCast)bexpr);
    }

    public CspIntExpr subtract(int a, CspIntExpr bexpr) {
	if (bexpr instanceof GenericIntExpr)
	    return new GenericIntExpr(null, a, NumOperation.SUBTRACT, (GenericIntExpr)bexpr);
	else
	    return new IntExpr(null, a, NumOperation.SUBTRACT, (IntExpr)bexpr);
    }
    public CspLongExpr subtract(int a, CspLongExpr bexpr) {
	if (bexpr instanceof GenericLongExpr)
	    return new GenericLongExpr(null, a, NumOperation.SUBTRACT, (GenericLongExpr)bexpr);
	else
	    return new LongExpr(null, a, NumOperation.SUBTRACT, (LongCast)bexpr);
    }
    public CspFloatExpr subtract(int a, CspFloatExpr bexpr) {
	if (bexpr instanceof GenericFloatExpr)
	    return new GenericFloatExpr(null, a, NumOperation.SUBTRACT, (GenericFloatExpr)bexpr);
	else
	    return new FloatExpr(null, a, NumOperation.SUBTRACT, (FloatCast)bexpr);
    }
    public CspDoubleExpr subtract(int a, CspDoubleExpr bexpr) {
	if (bexpr instanceof GenericDoubleExpr)
	    return new GenericDoubleExpr(null, a, NumOperation.SUBTRACT, (GenericDoubleExpr)bexpr);
	else
	    return new DoubleExpr(null, a, NumOperation.SUBTRACT, (DoubleCast)bexpr);
    }

    public CspIntExpr multiply(int a, CspIntExpr bexpr) {
	if (bexpr instanceof GenericIntExpr)
	    return new GenericIntExpr(null, a, NumOperation.MULTIPLY, (GenericIntExpr)bexpr);
	else
	    return new IntExpr(null, a, NumOperation.MULTIPLY, (IntExpr)bexpr);
    }
    public CspLongExpr multiply(int a, CspLongExpr bexpr) {
	if (bexpr instanceof GenericLongExpr)
	    return new GenericLongExpr(null, a, NumOperation.MULTIPLY, (GenericLongExpr)bexpr);
	else
	    return new LongExpr(null, a, NumOperation.MULTIPLY, (LongCast)bexpr);
    }
    public CspFloatExpr multiply(int a, CspFloatExpr bexpr) {
	if (bexpr instanceof GenericFloatExpr)
	    return new GenericFloatExpr(null, a, NumOperation.MULTIPLY, (GenericFloatExpr)bexpr);
	else
	    return new FloatExpr(null, a, NumOperation.MULTIPLY, (FloatCast)bexpr);
    }
    public CspDoubleExpr multiply(int a, CspDoubleExpr bexpr) {
	if (bexpr instanceof GenericDoubleExpr)
	    return new GenericDoubleExpr(null, a, NumOperation.MULTIPLY, (GenericDoubleExpr)bexpr);
	else
	    return new DoubleExpr(null, a, NumOperation.MULTIPLY, (DoubleCast)bexpr);
    }

    public CspIntExpr divide(int a, CspIntExpr bexpr) {
	if (bexpr instanceof GenericIntExpr)
	    return new GenericIntExpr(null, a, NumOperation.DIVIDE, (GenericIntExpr)bexpr);
	else
	    return new IntExpr(null, a, NumOperation.DIVIDE, (IntExpr)bexpr);
    }
    public CspLongExpr divide(int a, CspLongExpr bexpr) {
	if (bexpr instanceof GenericLongExpr)
	    return new GenericLongExpr(null, a, NumOperation.DIVIDE, (GenericLongExpr)bexpr);
	else
	    return new LongExpr(null, a, NumOperation.DIVIDE, (LongCast)bexpr);
    }
    public CspFloatExpr divide(int a, CspFloatExpr bexpr) {
	if (bexpr instanceof GenericFloatExpr)
	    return new GenericFloatExpr(null, a, NumOperation.DIVIDE, (GenericFloatExpr)bexpr);
	else
	    return new FloatExpr(null, a, NumOperation.DIVIDE, (FloatCast)bexpr);
    }
    public CspDoubleExpr divide(int a, CspDoubleExpr bexpr) {
	if (bexpr instanceof GenericDoubleExpr)
	    return new GenericDoubleExpr(null, a, NumOperation.DIVIDE, (GenericDoubleExpr)bexpr);
	else
	    return new DoubleExpr(null, a, NumOperation.DIVIDE, (DoubleCast)bexpr);
    }

    public CspIntExpr abs(CspIntExpr expr) {
	if (expr instanceof GenericIntExpr)
	    return new GenericIntExpr(null, (GenericIntExpr)expr, NumOperation.ABS);
	else
	    return new IntExpr(null, (IntExpr)expr, NumOperation.ABS);
    }
    public CspLongExpr abs(CspLongExpr expr) {
	if (expr instanceof GenericLongExpr)
	    return new GenericLongExpr(null, (GenericLongExpr)expr, NumOperation.ABS);
	else
	    return new LongExpr(null, (LongCast)expr, NumOperation.ABS);
    }
    public CspFloatExpr abs(CspFloatExpr expr) {
	if (expr instanceof GenericFloatExpr)
	    return new GenericFloatExpr(null, (GenericFloatExpr)expr, NumOperation.ABS);
	else
	    return new FloatExpr(null, (FloatCast)expr, NumOperation.ABS);
    }
    public CspDoubleExpr abs(CspDoubleExpr expr) {
	if (expr instanceof GenericDoubleExpr)
	    return new GenericDoubleExpr(null, (GenericDoubleExpr)expr, NumOperation.ABS);
	else
	    return new DoubleExpr(null, (DoubleCast)expr, NumOperation.ABS);
    }

    public CspIntExpr square(CspIntExpr expr) {
	return expr.multiply(expr);
    }
    public CspLongExpr square(CspLongExpr expr) {
	return expr.multiply(expr);
    }
    public CspFloatExpr square(CspFloatExpr expr) {
	return expr.multiply(expr);
    }
    public CspDoubleExpr square(CspDoubleExpr expr) {
	return expr.multiply(expr);
    }

    public CspDoubleExpr power(CspDoubleExpr x, double p) {
	return new DoubleExpr(null, (DoubleCast)x, NumOperation.POWER, p);
    }
    public CspDoubleExpr power(CspDoubleExpr x, CspDoubleExpr p) {
	return new DoubleExpr(null, (DoubleCast)x, NumOperation.POWER, (DoubleCast)p);
    }
    public CspDoubleExpr power(double x, CspDoubleExpr p) {
	return new DoubleExpr(null, x, NumOperation.POWER, (DoubleCast)p);
    }

    public CspDoubleExpr exp(CspDoubleExpr x) {
	return new DoubleExpr(null, (DoubleCast)x, NumOperation.EXP);
    }

    public CspDoubleExpr log(CspDoubleExpr x) {
	return new DoubleExpr(null, (DoubleCast)x, NumOperation.NAT_LOG);
    }

    public CspDoubleExpr cos(CspDoubleExpr x) {
	return new DoubleExpr(null, (DoubleCast)x, NumOperation.COS);
    }
    public CspDoubleExpr acos(CspDoubleExpr x) {
	return new DoubleExpr(null, (DoubleCast)x, NumOperation.ACOS);
    }
    public CspDoubleExpr sin(CspDoubleExpr x) {
	return new DoubleExpr(null, (DoubleCast)x, NumOperation.SIN);
    }
    public CspDoubleExpr asin(CspDoubleExpr x) {
	return new DoubleExpr(null, (DoubleCast)x, NumOperation.ASIN);
    }
    public CspDoubleExpr tan(CspDoubleExpr x) {
	return new DoubleExpr(null, (DoubleCast)x, NumOperation.TAN);
    }
    public CspDoubleExpr atan(CspDoubleExpr x) {
	return new DoubleExpr(null, (DoubleCast)x, NumOperation.ATAN);
    }

    /**
     * Returns a constraint that constraints an array of variables to be different
     */
     public CspConstraint allDifferent(CspIntExpr exprs[]) {
	return new NumAllDiffConstraint(toSpiExpressions(exprs));
    }

    /**
     * Returns a constraint that constraints an array of variables to be different
     */
     public CspConstraint allDifferent(CspLongExpr exprs[]) {
	return new NumAllDiffConstraint(toSpiExpressions(exprs));
    }

    /**
     * Constraints all elements of a generic variable to be a different value
     */
     public CspConstraint allDifferent(CspGenericIntExpr expr) {
	return new NumAllDiffConstraint((GenericNumExpr) expr);
    }

    /**
     * Constraints all elements of a generic variable to be a different value
     */
     public CspConstraint allDifferent(CspGenericLongExpr expr) {
	 return new NumAllDiffConstraint((GenericNumExpr) expr);
     }

     /**
      * Returns a constraint that constraints an array of variables to be different
      */
     public CspConstraint globalCardinality(CspIntExpr exprs[], Number[] vals, int[]lb, int[]ub) {
	 return new GlobalCardinalityConstraint(toSpiExpressions(exprs),vals,lb,ub);
     }

     /**
      * Returns a constraint that constraints an array of variables to be different
      */
     public CspConstraint globalCardinality(CspLongExpr exprs[],Number[] vals, int[]lb,int[]ub) {
	 return new GlobalCardinalityConstraint(toSpiExpressions(exprs),vals,lb,ub);
     }

     /**
      * Constraints all elements of a generic variable to be a different value
      */
     public CspConstraint globalCardinality(CspGenericIntExpr expr,Number[] vals, int[]lb,int[]ub) {
	 return new GlobalCardinalityConstraint((GenericNumExpr) expr,vals,lb,ub);
     }

     /**
      * Constraints all elements of a generic variable to be a different value
      */
     public CspConstraint globalCardinality(CspGenericLongExpr expr,Number[] vals, int[]lb,int[]ub) {
	 return new GlobalCardinalityConstraint((GenericNumExpr) expr,vals,lb,ub);
     }

     /**
      * Returns a constraint that constraints an array of variables to be different
      */
     public CspConstraint globalCardCount(CspIntExpr exprs[], Number[] vals, CspIntExpr[] count) {
	 return new GlobalCardinalityCountConstraint(toSpiExpressions(exprs),vals,toSpiIntExpressions(count));
     }

     /**
      * Returns a constraint that constraints an array of variables to be different
      */
     public CspConstraint globalCardCount(CspLongExpr exprs[], Number[] vals, CspIntExpr[] count) {
	 return new GlobalCardinalityCountConstraint(toSpiExpressions(exprs),vals,toSpiIntExpressions(count));
     }

     /**
      * Constraints all elements of a generic variable to be a different value
      */
     public CspConstraint globalCardCount(CspGenericIntExpr expr, Number[] vals, CspIntExpr[] count) {
	 return new GlobalCardinalityCountConstraint((GenericNumExpr) expr,vals,toSpiIntExpressions(count));
     }

     /**
      * Constraints all elements of a generic variable to be a different value
      */
     public CspConstraint globalCardCount(CspGenericLongExpr expr, Number[] vals, CspIntExpr[] count) {
	 return new GlobalCardinalityCountConstraint((GenericNumExpr) expr,vals,toSpiIntExpressions(count));
     }

     // javadoc inherited
     public CspConstraint memberOfArray(CspIntExpr sources[], CspIntExpr expr) {
	 return new MemberOfArray(toSpiExpressions(sources), (NumExpr) expr);
     }

     // javadoc inherited
     public CspConstraint memberOfArray(CspLongExpr sources[], CspLongExpr expr) {
	 return new MemberOfArray(toSpiExpressions(sources), (NumExpr) expr);
     }

     // javadoc inherited
     public CspConstraint notMemberOfArray(CspIntExpr sources[], CspIntExpr expr) {
	 return new NotMemberOfArray(toSpiExpressions(sources), (NumExpr) expr);
     }

     // javadoc inherited
     public CspConstraint notMemberOfArray(CspLongExpr sources[], CspLongExpr expr) {
	 return new NotMemberOfArray(toSpiExpressions(sources), (NumExpr) expr);
     }

     /**
      * Creates a variable equal to the summation of values in the generic
      * variable.  All indices in given range must exist in the variable X.
      * If additional indices exist in X not used within the range, the returned
      * expression will be a generic expression that is indexed on the leftover
      * indices.
      * 
      * @param x                         Generic variable summation is over
      * @param indexRange                Indices within generic variable X that summation is over
      * @param sourceIdxRestriction      Optional restriction of values placed on source index ranges
      * @return Expression that is equal to summation of X
      */
     public CspIntExpr summation(CspGenericIntExpr x, CspGenericIndex indexRange[], 
	     CspGenericIndexRestriction sourceIdxRestriction) 
     {
	 CspIntExpr intExpression = null;

	 // create unique set of source indices
	 Set<CspGenericIndex> s = new HashSet<CspGenericIndex>(Arrays.asList(x.getIndices()));

	 // create list of range indices
	 List<CspGenericIndex> rangeList = Arrays.asList(indexRange);

	 // ensure that all indices in range are also in x
	 if (!s.containsAll(rangeList))
	     throw new IllegalStateException("all indices within range must exist in generic source variable");

	 // determine leftover indices after range indices are removed
	 s.removeAll(rangeList);

	 // create normal variable if source indices are in range
	 GenericIndex sumIndices[] = VarMath.toGenericArray(indexRange);
	 String sumIdxString = indicesToString(sumIndices, false);
	 String baseName = "_" + x.getName() + NameUtil.nextNum();
	 if (s.size()==0) {
	     String sumName = "_(" + sigma + baseName + sumIdxString + ")";
	     intExpression = new IntExpr(sumName, x, sumIndices, sourceIdxRestriction);
	 }
	 else {
	     GenericIntExpr xexpr = (GenericIntExpr) x;

	     // build array of indices not included in summations
	     GenericIndex otherIndices[] = (GenericIndex[])s.toArray(new GenericIndex[s.size()]);

	     // iterate over combinations of indices not part of summation
	     // and build a summation expression for each
	     IndexIterator otherIter = new IndexIterator(new LinkedList<CspGenericIndex>(s));
	     IndexIterator sumIter = new IndexIterator(rangeList);
	     LinkedList<IntExpr> forAllExprs = new LinkedList<IntExpr>();
	     while (otherIter.hasNext()) {
		 otherIter.next();

		 // retrieve variables that are part of this summations
		 LinkedList<CspIntExpr> vars = new LinkedList<CspIntExpr>();
		 sumIter.reset();
		 while (sumIter.hasNext()) {
		     sumIter.next();
		     vars.add(xexpr.getExpressionForIndex());
		 }

		 // generate names for new variables
		 StringBuffer genericName = new StringBuffer("_(");
		 genericName.append(delta);
		 genericName.append(baseName);
		 genericName.append(indicesToString(otherIndices, true));
		 genericName.append(")");

		 StringBuffer sumName = new StringBuffer("_(");
		 sumName.append(sigma);
		 sumName.append(genericName);
		 sumName.append(sumIdxString);
		 sumName.append(")");

		 // create expression that represents summation
		 IntExpr exprs[] = (IntExpr[]) vars.toArray(new IntExpr[vars.size()]);
		 GenericIntExpr gexpr = new GenericIntExpr(genericName.toString(), sumIndices, exprs,true);

		 forAllExprs.add(new IntExpr(sumName.toString(), gexpr, sumIndices, sourceIdxRestriction));
	     }

	     // create generic expression that wraps all the summations
	     IntExpr exprs[] = (IntExpr[]) forAllExprs.toArray(new IntExpr[forAllExprs.size()]);
	     String sumName = "_(" + sigma + x.getName() + sumIdxString + ")";
	     intExpression = new GenericIntExpr(sumName, otherIndices, exprs, true);
	 }

	 return intExpression;
     }

     /**
      * Creates a variable equal to the summation of values in the generic
      * variable.  All indices in given range must exist in the variable X.
      * If additional indices exist in X not used within the range, the returned
      * expression will be a generic expression that is indexed on the leftover
      * indices.
      * 
      * @param x                         Generic variable summation is over
      * @param indexRange                Indices within generic variable X that summation is over
      * @param sourceIdxRestriction      Optional restriction of values placed on source index ranges
      * @return Expression that is equal to summation of X
      */
     public CspLongExpr summation(CspGenericLongExpr x, CspGenericIndex indexRange[], 
	     CspGenericIndexRestriction sourceIdxRestriction) 
     {
	 CspLongExpr longExpression = null;
	 // create unique set of source indices
	 Set<CspGenericIndex> s = new HashSet<CspGenericIndex>(Arrays.asList(x.getIndices()));

	 // create list of range indices
	 List<CspGenericIndex> rangeList = Arrays.asList(indexRange);

	 // ensure that all indices in range are also in x
	 if (!s.containsAll(rangeList))
	     throw new IllegalStateException("all indices within range must exist in generic source variable");

	 // determine leftover indices after range indices are removed
	 s.removeAll(rangeList);

	 // create normal variable if source indices are in range
	 GenericIndex sumIndices[] = VarMath.toGenericArray(indexRange);
	 String sumIdxString = indicesToString(sumIndices, false);
	 String baseName = "_" + x.getName() + NameUtil.nextNum();
	 if (s.size()==0) {
	     String sumName = "_(" + sigma + baseName + sumIdxString + ")";
	     longExpression = new LongExpr(sumName, x, sumIndices, sourceIdxRestriction);
	 }
	 else {
	     GenericLongExpr xexpr = (GenericLongExpr) x;

	     // build array of indices not included in summations
	     GenericIndex otherIndices[] = (GenericIndex[])s.toArray(new GenericIndex[s.size()]);

	     // iterate over combinations of indices not part of summation
	     // and build a summation expression for each
	     IndexIterator otherIter = new IndexIterator(new LinkedList<CspGenericIndex>(s));
	     IndexIterator sumIter = new IndexIterator(rangeList);
	     LinkedList<LongExpr> forAllExprs = new LinkedList<LongExpr>();
	     while (otherIter.hasNext()) {
		 otherIter.next();

		 // retrieve variables that are part of this summations
		 LinkedList<CspLongCast> vars = new LinkedList<CspLongCast>();
		 sumIter.reset();
		 while (sumIter.hasNext()) {
		     sumIter.next();
		     vars.add(xexpr.getExpressionForIndex());
		 }

		 // generate names for new variables
		 StringBuffer genericName = new StringBuffer("_(");
		 genericName.append(delta);
		 genericName.append(baseName);
		 genericName.append(indicesToString(otherIndices, true));
		 genericName.append(")");

		 StringBuffer sumName = new StringBuffer("_(");
		 sumName.append(sigma);
		 sumName.append(genericName);
		 sumName.append(sumIdxString);
		 sumName.append(")");

		 // create expression that represents summation
		 LongExpr exprs[] = (LongExpr[]) vars.toArray(new LongExpr[vars.size()]);
		 GenericLongExpr gexpr = new GenericLongExpr(genericName.toString(), sumIndices, exprs,true);

		 forAllExprs.add(new LongExpr(sumName.toString(), gexpr, sumIndices, sourceIdxRestriction));
	     }

	     // create generic expression that wraps all the summations
	     LongExpr exprs[] = (LongExpr[]) forAllExprs.toArray(new LongExpr[forAllExprs.size()]);
	     String sumName = "_(" + sigma + x.getName() + sumIdxString + ")";
	     longExpression = new GenericLongExpr(sumName, otherIndices, exprs, true);
	 }
	 return longExpression;
     }

     /**
      * Creates a variable equal to the summation of values in the generic
      * variable.  All indices in given range must exist in the variable X.
      * If additional indices exist in X not used within the range, the returned
      * expression will be a generic expression that is indexed on the leftover
      * indices.
      * 
      * @param x                         Generic variable summation is over
      * @param indexRange                Indices within generic variable X that summation is over
      * @param sourceIdxRestriction      Optional restriction of values placed on source index ranges
      * @return Expression that is equal to summation of X
      */
     public CspFloatExpr summation(CspGenericFloatExpr x, CspGenericIndex indexRange[], 
	     CspGenericIndexRestriction sourceIdxRestriction) 
     {
	 CspFloatExpr floatExpression = null;
	 // create unique set of source indices
	 Set<CspGenericIndex> s = new HashSet<CspGenericIndex>(Arrays.asList(x.getIndices()));

	 // create list of range indices
	 List<CspGenericIndex> rangeList = Arrays.asList(indexRange);

	 // ensure that all indices in range are also in x
	 if (!s.containsAll(rangeList))
	     throw new IllegalStateException("all indices within range must exist in generic source variable");

	 // determine leftover indices after range indices are removed
	 s.removeAll(rangeList);

	 // create normal variable if source indices are in range
	 GenericIndex sumIndices[] = VarMath.toGenericArray(indexRange);
	 String sumIdxString = indicesToString(sumIndices, false);
	 String baseName = "_" + x.getName() + NameUtil.nextNum();
	 if (s.size()==0) {
	     String sumName = "_(" + sigma + baseName + sumIdxString + ")";
	     floatExpression = new FloatExpr(sumName, x, sumIndices, sourceIdxRestriction);
	 }
	 else {
	     GenericFloatExpr xexpr = (GenericFloatExpr) x;

	     // build array of indices not included in summations
	     GenericIndex otherIndices[] = (GenericIndex[])s.toArray(new GenericIndex[s.size()]);

	     // iterate over combinations of indices not part of summation
	     // and build a summation expression for each
	     IndexIterator otherIter = new IndexIterator(new LinkedList<CspGenericIndex>(s));
	     IndexIterator sumIter = new IndexIterator(rangeList);
	     LinkedList<FloatExpr> forAllExprs = new LinkedList<FloatExpr>();
	     while (otherIter.hasNext()) {
		 otherIter.next();

		 // retrieve variables that are part of this summations
		 LinkedList<CspFloatCast> vars = new LinkedList<CspFloatCast>();
		 sumIter.reset();
		 while (sumIter.hasNext()) {
		     sumIter.next();
		     vars.add(xexpr.getExpressionForIndex());
		 }

		 // generate names for new variables
		 StringBuffer genericName = new StringBuffer("_(");
		 genericName.append(delta);
		 genericName.append(baseName);
		 genericName.append(indicesToString(otherIndices, true));
		 genericName.append(")");

		 StringBuffer sumName = new StringBuffer("_(");
		 sumName.append(sigma);
		 sumName.append(genericName);
		 sumName.append(sumIdxString);
		 sumName.append(")");

		 // create expression that represents summation
		 FloatExpr exprs[] = (FloatExpr[]) vars.toArray(new FloatExpr[vars.size()]);
		 GenericFloatExpr gexpr = new GenericFloatExpr(genericName.toString(), sumIndices, exprs,true);

		 forAllExprs.add(new FloatExpr(sumName.toString(), gexpr, sumIndices, sourceIdxRestriction));
	     }

	     // create generic expression that wraps all the summations
	     FloatExpr exprs[] = (FloatExpr[]) forAllExprs.toArray(new FloatExpr[forAllExprs.size()]);
	     String sumName = "_(" + sigma + x.getName() + sumIdxString + ")";
	     floatExpression = new GenericFloatExpr(sumName, otherIndices, exprs, true);
	 }
	 return floatExpression;
     }

     /**
      * Creates a variable equal to the summation of values in the generic
      * variable.  All indices in given range must exist in the variable X.
      * If additional indices exist in X not used within the range, the returned
      * expression will be a generic expression that is indexed on the leftover
      * indices.
      * 
      * @param x                         Generic variable summation is over
      * @param indexRange                Indices within generic variable X that summation is over
      * @param sourceIdxRestriction      Optional restriction of values placed on source index ranges
      * @return Expression that is equal to summation of X
      */
     public CspDoubleExpr summation(CspGenericDoubleExpr x, CspGenericIndex indexRange[], 
	     CspGenericIndexRestriction sourceIdxRestriction) 
     {
	 CspDoubleExpr doubleExpression = null;
	 // create unique set of source indices
	 Set<CspGenericIndex> s = new HashSet<CspGenericIndex>(Arrays.asList(x.getIndices()));

	 // create list of range indices
	 List<CspGenericIndex> rangeList = Arrays.asList(indexRange);

	 // ensure that all indices in range are also in x
	 if (!s.containsAll(rangeList))
	     throw new IllegalStateException("all indices within range must exist in generic source variable");

	 // determine leftover indices after range indices are removed
	 s.removeAll(rangeList);

	 // create normal variable if source indices are in range
	 GenericIndex sumIndices[] = VarMath.toGenericArray(indexRange);
	 String sumIdxString = indicesToString(sumIndices, false);
	 String baseName = "_" + x.getName() + NameUtil.nextNum();
	 if (s.size()==0) {
	     String sumName = "_(" + sigma + baseName + sumIdxString + ")";
	     doubleExpression = new DoubleExpr(sumName, x, sumIndices, sourceIdxRestriction);
	 }
	 else {
	     GenericDoubleExpr xexpr = (GenericDoubleExpr) x;

	     // build array of indices not included in summations
	     GenericIndex otherIndices[] = (GenericIndex[])s.toArray(new GenericIndex[s.size()]);

	     // iterate over combinations of indices not part of summation
	     // and build a summation expression for each
	     IndexIterator otherIter = new IndexIterator(new LinkedList<CspGenericIndex>(s));
	     IndexIterator sumIter = new IndexIterator(rangeList);
	     LinkedList<DoubleExpr> forAllExprs = new LinkedList<DoubleExpr>();
	     while (otherIter.hasNext()) {
		 otherIter.next();

		 // retrieve variables that are part of this summations
		 LinkedList<CspDoubleCast> vars = new LinkedList<CspDoubleCast>();
		 sumIter.reset();
		 while (sumIter.hasNext()) {
		     sumIter.next();
		     vars.add(xexpr.getExpressionForIndex());
		 }

		 // generate names for new variables
		 StringBuffer genericName = new StringBuffer("_(");
		 genericName.append(delta);
		 genericName.append(baseName);
		 genericName.append(indicesToString(otherIndices, true));
		 genericName.append(")");

		 StringBuffer sumName = new StringBuffer("_(");
		 sumName.append(sigma);
		 sumName.append(genericName);
		 sumName.append(sumIdxString);
		 sumName.append(")");

		 // create expression that represents summation
		 DoubleExpr exprs[] = (DoubleExpr[]) vars.toArray(new DoubleExpr[vars.size()]);
		 GenericDoubleExpr gexpr = new GenericDoubleExpr(genericName.toString(), sumIndices, exprs,true);

		 forAllExprs.add(new DoubleExpr(sumName.toString(), gexpr, sumIndices, sourceIdxRestriction));
	     }


	     // create generic expression that wraps all the summations
	     DoubleExpr exprs[] = (DoubleExpr[]) forAllExprs.toArray(new DoubleExpr[forAllExprs.size()]);
	     String sumName = "_(" + sigma + x.getName() + sumIdxString + ")";
	     doubleExpression = new GenericDoubleExpr(sumName, otherIndices, exprs,true);
	 }
	 return doubleExpression;
     }

     /**
      * Converts an array of indices to a string for generating names
      */
     public static String indicesToString(GenericIndex[] indices, boolean includeVal) {
	 StringBuffer buf = new StringBuffer();

	 buf.append("[");
	 for (int i=0; i<indices.length; i++) {
	     if (i>0) buf.append(", ");

	     GenericIndex idx = indices[i];
	     buf.append(idx.getName());

	     if (includeVal) {
		 buf.append(":");
		 buf.append(idx.currentVal());
	     }
	 }
	 buf.append("]");

	 return buf.toString();
     }

     /**
      * Converts an array of api expressions to spi expressions
      */
     public static NumExpr[] toSpiExpressions(CspNumExpr exprs[]) {
	 NumExpr nexprs[] = new NumExpr[exprs.length];
	 for (int i=0; i<exprs.length; i++)
	     nexprs[i] = (NumExpr) exprs[i];
	 return nexprs;
     }

     /**
      * Converts an array of api expressions to spi expressions
      */
     public static IntExpr[] toSpiIntExpressions(CspIntExpr exprs[]) {
	 IntExpr nexprs[] = new IntExpr[exprs.length];
	 for (int i=0; i<exprs.length; i++)
	     nexprs[i] = (IntExpr) exprs[i];
	 return nexprs;
     }

     /**
      * Converts an array of CSP indices to internal index objects
      */
     public static GenericIndex[] toGenericArray(CspGenericIndex indices[]) {
	 GenericIndex idxArray[] = new GenericIndex[indices.length];
	 for (int i=0; i<indices.length; i++)
	     idxArray[i] = (GenericIndex) indices[i];
	 return idxArray;
     }

     /**
      * Returns the total number of values in a generic variable given a set
      * of indices
      */
     public static int totalGenericSize(GenericIndex indices[]) {
	 int totalExprs = indices[indices.length-1].size();
	 for (int i=indices.length-2; i>=0; i--) {
	     // total nodes is product of all sizes
	     totalExprs *= indices[i].size();
	 }
	 return totalExprs;
     }
}
