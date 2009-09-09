/*
 * Created on Jan 18, 2005
 */
package jopt.csp.spi.util;

import java.math.BigDecimal;

import jopt.csp.util.DoubleUtil;

/**
 * @author ncoleman
 */
public class TrigMath {
    private static MutableNumber work1 = new MutableNumber();
    private static MutableNumber work2 = new MutableNumber();
    private static MutableNumber work3 = new MutableNumber();
    
    /**
     * Returns a value between 0 and 2*pi within half pi increments if a value is close
     */
    private static double halfPiPrecision(double v, double precision) {
        if (DoubleUtil.isEqual(0, v, precision))
            return 0;
        else if (DoubleUtil.isEqual(NumberMath.HALF_PI, v, precision))
            return NumberMath.HALF_PI;
        else if (DoubleUtil.isEqual(NumberMath.PI, v, precision))
            return NumberMath.PI;
        else if (DoubleUtil.isEqual(NumberMath.ONE_AND_HALF_PI, v, precision))
            return NumberMath.ONE_AND_HALF_PI;
        else if (DoubleUtil.isEqual(NumberMath.TWO_PI, v, precision))
            return NumberMath.TWO_PI;
        
        return v;
    }
    
    /**
     * Calculates modulus of a value in relation to a circle
     */
    private static double circleModulus(double v, double precision) {
        v = v % NumberMath.TWO_PI;
        return halfPiPrecision(v, precision);
    }
    
    /**
     * Calculates minimum and maximum value of Z where Z = sin(X)
     */
    public static void sinMinMax(TrigExpr z, TrigExpr x, double precision, MutableNumber resultMin, MutableNumber resultMax) {
        // Calculate min and max values
        resultMin.setInvalid(true);
        resultMax.setInvalid(true);

        // If min and max exceed 2*pi (360 degrees), min and max are -1 and 1
        double xmin = x.getNumMin().doubleValue();
        double xmax = x.getNumMax().doubleValue();
        if (DoubleUtil.compare(xmax - xmin, NumberMath.TWO_PI, precision) >= 0) {
            resultMin.setDoubleValue(-1d);
            resultMax.setDoubleValue(1d);
        }

        // Min and max are within 2*pi (360 degrees) of each other
        else {
            double xminModulus = circleModulus(xmin, precision);
            double xmaxModulus = circleModulus(xmax, precision);
            if (xmaxModulus==0 && DoubleUtil.compare(xmax, xmin, precision) > 0) xmaxModulus = NumberMath.TWO_PI;
            double exp1 = Math.sin(xminModulus);
            double exp2 = Math.sin(xmaxModulus);

            // determine if value straddles zero
            double minRotations = (xmin - xminModulus) / NumberMath.TWO_PI;
            double maxRotations = (xmax - xmaxModulus) / NumberMath.TWO_PI;
            boolean straddleZero = DoubleUtil.compare(minRotations, maxRotations, precision) < 0;
            
            // Determine min
            if (DoubleUtil.compare(xminModulus, NumberMath.ONE_AND_HALF_PI, precision) <= 0 && DoubleUtil.compare(NumberMath.ONE_AND_HALF_PI, xmaxModulus, precision) <= 0){
                resultMin.setDoubleValue(-1d);
            }
            else {
                // check for rounding error within precision
                double zmin = z.getNumMin().doubleValue();
                double d = Math.min(exp1, exp2);
                if (DoubleUtil.isEqual(zmin, d, precision))
                    resultMin.setDoubleValue(zmin);
                else
                    resultMin.setDoubleValue(d);
            }

            // straddles zero
            if (straddleZero && resultMin.doubleValue() > 0) {
                resultMin.setDoubleValue(0d);
            }
            
            // Determine max
            if (DoubleUtil.compare(xminModulus, NumberMath.HALF_PI, precision) <=0 && (straddleZero || DoubleUtil.compare(NumberMath.HALF_PI, xmaxModulus, precision) <= 0)){
                resultMax.setDoubleValue(1d);
            }
            else {
                // check for rounding error within precision
                double zmax = z.getNumMax().doubleValue();
                double d = Math.max(exp1, exp2);
                if (DoubleUtil.isEqual(zmax, d, precision))
                    resultMax.setDoubleValue(zmax);
                else
                    resultMax.setDoubleValue(d);
            }
        }
    }

    /**
     * Calculates minimum and maximum value of Z where Z = cos(X)
     */
    public static void cosMinMax(TrigExpr z, TrigExpr x, double precision, MutableNumber resultMin, MutableNumber resultMax) {
        // Calculate min and max values
        resultMin.setInvalid(true);
        resultMax.setInvalid(true);

        // If min and max exceed 2*pi (360 degrees), min and max are -1 and 1
        double xmin = x.getNumMin().doubleValue();
        double xmax = x.getNumMax().doubleValue();
        if (DoubleUtil.compare(xmax - xmin, NumberMath.TWO_PI, precision) >= 0) {
            resultMin.setDoubleValue(-1d);
            resultMax.setDoubleValue(1d);
        }

        // Min and max are within 2*pi (360 degrees) of each other
        else {
            double xminModulus = circleModulus(xmin, precision);
            double xmaxModulus = circleModulus(xmax, precision);
            if (DoubleUtil.isEqual(xmaxModulus, 0, precision) && DoubleUtil.compare(xmax, xmin, precision) > 0) xmaxModulus = NumberMath.TWO_PI;
            double exp1 = Math.cos(xminModulus);
            double exp2 = Math.cos(xmaxModulus);

            // Determine min
            if (DoubleUtil.compare(xminModulus, NumberMath.PI, precision) <= 0 && DoubleUtil.compare(NumberMath.PI, xmaxModulus, precision) <= 0){
                resultMin.setDoubleValue(-1d);
            }
            else {
                // check for rounding error within precision
                double zmin = z.getNumMin().doubleValue();
                double d = Math.min(exp1, exp2);
                if (DoubleUtil.isEqual(zmin, d, precision))
                    resultMin.setDoubleValue(zmin);
                else
                    resultMin.setDoubleValue(d);
            }

            // determine if value straddles zero
            double minRotations = (xmin - xminModulus) / NumberMath.TWO_PI;
            double maxRotations = (xmax - xmaxModulus) / NumberMath.TWO_PI;
            boolean straddleZero = (DoubleUtil.compare(minRotations, maxRotations, precision) < 0) || 
            					   (DoubleUtil.compare(xmin, 0, precision) < 0 && DoubleUtil.compare(xmax, 0, precision) > 0);
            
            // Determine max
            if (straddleZero) {
                resultMax.setDoubleValue(1d);
            }
            else {
                // check for rounding error within precision
                double zmax = z.getNumMax().doubleValue();
                double d = Math.max(exp1, exp2);
                if (DoubleUtil.isEqual(zmax, d, precision))
                    resultMax.setDoubleValue(zmax);
                else
                    resultMax.setDoubleValue(d);
            }
        }
    }

    /**
     * Calculates minimum and maximum value of Z where Z = sin(x)
     */
    public static void sinMinMax(TrigExpr z, double x, double precision, MutableNumber resultMin, MutableNumber resultMax) {
        // Calculate min and max values
        resultMin.setInvalid(true);
        resultMax.setInvalid(true);
        x = circleModulus(x, precision);
        double trigVal = Math.sin(x);

        // Determine min
        if (DoubleUtil.isEqual(x, NumberMath.ONE_AND_HALF_PI, precision)) {
            resultMin.setDoubleValue(-1d);
        }
        else {
            // check for rounding error within precision
            double zmin = z.getNumMin().doubleValue();
            if (DoubleUtil.isEqual(zmin, trigVal, precision))
                resultMin.setDoubleValue(zmin);
            else
                resultMin.setDoubleValue(trigVal);
        }

        // Determine max
        if (DoubleUtil.isEqual(x, NumberMath.HALF_PI, precision)) {
            resultMax.setDoubleValue(1d);
        }
        else {
            // check for rounding error within precision
            double zmax = z.getNumMax().doubleValue();
            if (DoubleUtil.isEqual(zmax, trigVal, precision))
                resultMax.setDoubleValue(zmax);
            else
                resultMax.setDoubleValue(trigVal);
        }
    }

    /**
     * Calculates minimum and maximum value of Z where Z = cos(x)
     */
    public static void cosMinMax(TrigExpr z, double x, double precision, MutableNumber resultMin, MutableNumber resultMax) {
        // Calculate min and max values
        resultMin.setInvalid(true);
        resultMax.setInvalid(true);
        x = circleModulus(x, precision);
        double trigVal = Math.cos(x);

        // Determine min
        if (DoubleUtil.isEqual(x, NumberMath.PI, precision)){
            resultMin.setDoubleValue(-1d);
        }
        else {
            // check for rounding error within precision
            double zmin = z.getNumMin().doubleValue();
            if (DoubleUtil.isEqual(zmin, trigVal, precision))
                resultMin.setDoubleValue(zmin);
            else
                resultMin.setDoubleValue(trigVal);
        }

        // Determine max
        if (DoubleUtil.isEqual(x, 0, precision)) {
            resultMax.setDoubleValue(1d);
        }
        else {
            // check for rounding error within precision
            double zmax = z.getNumMax().doubleValue();
            if (DoubleUtil.isEqual(zmax, trigVal, precision))
                resultMax.setDoubleValue(zmax);
            else
                resultMax.setDoubleValue(trigVal);
        }
    }

    /**
     * Calculates minimum and maximum value of Z where Z = asin(X)
     */
    public static void asinMinMax(TrigExpr z, TrigExpr x, double precision, MutableNumber resultMin, MutableNumber resultMax) {
        resultMin.setInvalid(true);
        resultMax.setInvalid(true);
        double xmin = x.getNumMin().doubleValue();
        double xmax = x.getNumMax().doubleValue();
        
        if (DoubleUtil.compare(xmin, 1, precision) > 1)
            return;
        else if (xmin > 1)
        	xmin = 1;
        else if (xmin < -1) 
            xmin = -1;
        
        if (DoubleUtil.compare(xmax, -1, precision) < 0)
            return;
        if (xmax < -1)
            xmax = -1;
        else if (xmax > 1) 
            xmax = 1;
        
        // check for entire range included
        if (DoubleUtil.compare(xmin, 0, precision) <= 0 && DoubleUtil.compare(0, xmax, precision) <= 0) {
            resultMin.setDoubleValue(z.getNumMin().doubleValue());
            resultMax.setDoubleValue(z.getNumMax().doubleValue());
            return;
        }
        
        // calculate asin values values
        asinMinMax(z, x.getNumMin().doubleValue(), precision, work1, work2);
        double min1 = work1.doubleValue();
        double max1 = work2.doubleValue();
        asinMinMax(z, x.getNumMax().doubleValue(), precision, work1, work2);
        double min2 = work1.doubleValue();
        double max2 = work2.doubleValue();
        
        // determine min value
        if (min1<min2)
            resultMin.setDoubleValue(min1);
        else
            resultMin.setDoubleValue(min2);
        
        // determine max value
        if (max1>max2)
            resultMax.setDoubleValue(max1);
        else
            resultMax.setDoubleValue(max2);
    }

    /**
     * Calculates minimum and maximum value of Z where Z = acos(X)
     */
    public static void acosMinMax(TrigExpr z, TrigExpr x, double precision, MutableNumber resultMin, MutableNumber resultMax) {
        resultMin.setInvalid(true);
        resultMax.setInvalid(true);
        double xmin = x.getNumMin().doubleValue();
        double xmax = x.getNumMax().doubleValue();
        
        if (DoubleUtil.compare(xmin, 1, precision) > 1)
            return;
        else if (xmin > 1)
        	xmin = 1;
        else if (xmin < -1) 
            xmin = -1;
        
        if (DoubleUtil.compare(xmax, -1, precision) < 0)
            return;
        else if (xmax < -1)
            xmax = -1;
        else if (xmax > 1) 
            xmax = 1;
        
        // check for entire range included
        if (DoubleUtil.isEqual(xmin, -1, precision) && DoubleUtil.isEqual(xmax, 1, precision)) {
            resultMin.setDoubleValue(z.getNumMin().doubleValue());
            resultMax.setDoubleValue(z.getNumMax().doubleValue());
            return;
        }
        
        // calculate acos values values
        acosMinMax(z, x.getNumMin().doubleValue(), precision, work1, work2);
        double min1 = work1.doubleValue();
        double max1 = work2.doubleValue();
        acosMinMax(z, x.getNumMax().doubleValue(), precision, work1, work2);
        double min2 = work1.doubleValue();
        double max2 = work2.doubleValue();
        
        // determine min value
        if (min1<min2)
            resultMin.setDoubleValue(min1);
        else
            resultMin.setDoubleValue(min2);
        
        // determine max value
        if (max1>max2)
            resultMax.setDoubleValue(max1);
        else
            resultMax.setDoubleValue(max2);
    }

    /**
     * Calculates minimum and maximum value of Z where Z = asin(x)
     */
    public static void asinMinMax(TrigExpr z, double x, double precision, MutableNumber resultMin, MutableNumber resultMax) {
        resultMin.setInvalid(true);
        resultMax.setInvalid(true);
        
        if (DoubleUtil.compare(x, -1, precision) < 0 || DoubleUtil.compare(x, 1, precision) > 0)
            return;
        else if (x < -1)
        	x = -1;
        else if (x > 1)
        	x = 1;
        
        // asin(0) = full rotation
        if (DoubleUtil.isEqual(0, x, precision)) {
            resultMin.setDoubleValue(z.getNumMin().doubleValue());
            resultMax.setDoubleValue(z.getNumMax().doubleValue());
            return;
        }
        
        // compute smaller angle base
        double smallAngleBase = Math.asin(x);
        int cmp = DoubleUtil.compare(smallAngleBase, 0, precision);
        if (cmp == 0) 
        	smallAngleBase = 0;
        else if (cmp < 0) 
        	smallAngleBase = halfPiPrecision(smallAngleBase + NumberMath.TWO_PI, precision);
        else
        	smallAngleBase = halfPiPrecision(smallAngleBase, precision);
        
        // compute larger angle base
        double largeAngleBase = 0;
        if (smallAngleBase < NumberMath.PI) {
            largeAngleBase = NumberMath.PI - smallAngleBase;
        }
        else {
            largeAngleBase = smallAngleBase;
            smallAngleBase = NumberMath.TWO_PI - (smallAngleBase - NumberMath.PI);
        }
        
        asinAcosMinMax(z, smallAngleBase, largeAngleBase, precision, resultMin, resultMax);
    }
    
    
    /**
     * Calculates minimum and maximum value of Z where Z = acos(x)
     */
    public static void acosMinMax(TrigExpr z, double x, double precision, MutableNumber resultMin, MutableNumber resultMax) {
        resultMin.setInvalid(true);
        resultMax.setInvalid(true);
        
        if (DoubleUtil.compare(x, -1, precision) < 0 || DoubleUtil.compare(x, 1, precision) > 0)
            return;
        else if (x < -1)
        	x = -1;
        else if (x > 1)
        	x = 1;
        
        // compute smaller angle base
        double smallAngleBase = Math.acos(x);
        int cmp = DoubleUtil.compare(smallAngleBase, 0, precision);
        if (cmp == 0) 
        	smallAngleBase = 0;
        else if (cmp < 0) 
        	smallAngleBase = halfPiPrecision(smallAngleBase + NumberMath.TWO_PI, precision);
        else
        	smallAngleBase = halfPiPrecision(smallAngleBase, precision);
        
        // compute larger angle base
        double largeAngleBase = NumberMath.TWO_PI - smallAngleBase;
        
        asinAcosMinMax(z, smallAngleBase, largeAngleBase, precision, resultMin, resultMax);
    }
    
    /**
     * Utility function to handle locating min and max asin / acos value since all values % 2*pi
     * have to be considered 
     */
    private static void asinAcosMinMax(TrigExpr z, double smallAngleBase, double largeAngleBase, double precision, MutableNumber resultMin, MutableNumber resultMax) {
        resultMin.setInvalid(true);
        resultMax.setInvalid(true);
        double zmin = z.getNumMin().doubleValue();
        double zmax = z.getNumMax().doubleValue();
        
        // Initialize variables used to calc min and max
        double startOffset = (Math.floor(zmin / NumberMath.TWO_PI)-1) * NumberMath.TWO_PI;
        double endOffset = Math.ceil(zmax / NumberMath.TWO_PI) * NumberMath.TWO_PI;

        // initialize small and large angle below starting offset
        double smallAngle = startOffset + ((startOffset>=0) ? smallAngleBase : (NumberMath.TWO_PI-largeAngleBase));
        double largeAngle = startOffset + ((startOffset>=0) ? largeAngleBase : (NumberMath.TWO_PI-smallAngleBase));
        
        // Determine minimum value
        while (DoubleUtil.compare(smallAngle, endOffset, precision) <= 0) {
            // check if smaller angle is in domain 
            smallAngle = DoubleUtil.returnMinMaxIfClose(zmin, zmax, smallAngle, precision);
            work3.setDoubleValue(smallAngle);
            if (z.isInDomain(work3)) {
                resultMin.setDoubleValue(smallAngle);
                break;
            }
            
            // if smaller angle is not in domain, check larger angle
            largeAngle = DoubleUtil.returnMinMaxIfClose(zmin, zmax, largeAngle, precision);
            work3.setDoubleValue(largeAngle);
            if (z.isInDomain(work3)) {
                resultMin.setDoubleValue(largeAngle);
                break;
            }

            // move up 2pi and check again
            double prevSmall = smallAngle;
            double prevLarge = largeAngle;
            smallAngle += NumberMath.TWO_PI;
            largeAngle += NumberMath.TWO_PI;
            
            // check if values flipped from negative to positive
            if (prevSmall < 0 && smallAngle >0) {
                smallAngle = smallAngleBase;
            }
            if (prevLarge < 0 && largeAngle >0) {
                largeAngle = largeAngleBase;
            }
        }

        // initialize small and large angle above starting offset
        smallAngle = endOffset + ((endOffset>=0) ? smallAngleBase : (NumberMath.TWO_PI-largeAngleBase));
        largeAngle = endOffset + ((endOffset>=0) ? largeAngleBase : (NumberMath.TWO_PI-smallAngleBase));
        
        // Determine maximum value
        while (DoubleUtil.compare(largeAngle, startOffset, precision) >= 0) {
            // check if larger angle is in domain 
            largeAngle = DoubleUtil.returnMinMaxIfClose(zmin, zmax, largeAngle, precision);
            work3.setDoubleValue(largeAngle);
            if (z.isInDomain(work3)) {
                resultMax.setDoubleValue(largeAngle);
                break;
            }
            
            // if larger angle is not in domain, check smaller angle
            smallAngle = DoubleUtil.returnMinMaxIfClose(zmin, zmax, smallAngle, precision);
            work3.setDoubleValue(smallAngle);
            if (z.isInDomain(work3)) {
                resultMax.setDoubleValue(smallAngle);
                break;
            }

            // move down 2pi and check again
            double prevLarge = largeAngle;
            smallAngle -= NumberMath.TWO_PI;
            largeAngle -= NumberMath.TWO_PI;
            
            // check if values flipped from positive to negative
            if (prevLarge > 0 && smallAngle < 0) {
                smallAngle = -largeAngleBase;
            }
            if (prevLarge > 0 && largeAngle < 0) {
                largeAngle = -smallAngleBase;
            }
        }
    }
    
    /**
     * Calculates minimum and maximum value of Z where Z = tan(X)
     */
    public static void tanMinMax(TrigExpr z, TrigExpr x, double precision, MutableNumber resultMin, MutableNumber resultMax) {
        resultMin.setInvalid(true);
        resultMax.setInvalid(true);
        
        // If min and max exceed pi (180 degrees), min and max are infinite
        double xmin = x.getNumMin().doubleValue();
        double xmax = x.getNumMax().doubleValue();
        double diff = xmax - xmin;
        if (DoubleUtil.compare(diff, NumberMath.PI, precision) >= 0) {
            resultMin.setDoubleValue(Double.NEGATIVE_INFINITY);
            resultMax.setDoubleValue(Double.POSITIVE_INFINITY);
        }

        // Min and max are within pi (180 degrees) of each other
        else {
            xmin = xmin % NumberMath.TWO_PI;
            xmax = xmax % NumberMath.TWO_PI;
            if (DoubleUtil.isEqual(xmax, 0, precision) && x.getNumMax().doubleValue() > x.getNumMin().doubleValue())
                xmax = NumberMath.TWO_PI;

            // If 90 degrees or 270 degrees, neg and pos infinity for min and max
            if ( (DoubleUtil.compare(xmin, NumberMath.HALF_PI, precision) <= 0 && DoubleUtil.compare(NumberMath.HALF_PI, xmax, precision) <= 0) ||
                 (DoubleUtil.compare(xmin, NumberMath.ONE_AND_HALF_PI, precision) <= 0 && DoubleUtil.compare(NumberMath.ONE_AND_HALF_PI, xmax, precision) <= 0) )
            {
                resultMin.setDoubleValue(Double.NEGATIVE_INFINITY);
                resultMax.setDoubleValue(Double.POSITIVE_INFINITY);
            }

            else {
                double zmin = z.getNumMin().doubleValue();
                double zmax = z.getNumMax().doubleValue();
                resultMin.setDoubleValue(DoubleUtil.returnMinMaxIfClose(zmin, zmax, Math.tan(xmin), precision));
                resultMax.setDoubleValue(DoubleUtil.returnMinMaxIfClose(zmin, zmax, Math.tan(xmax), precision));
            }
        }
    }
    
    /**
     * Calculates minimum and maximum value of Z where Z = tan(x)
     */
    public static void tanMinMax(TrigExpr z, double x, double precision, MutableNumber resultMin, MutableNumber resultMax) {
        resultMin.setInvalid(true);
        resultMax.setInvalid(true);
        
        // Calculate min and max values
        x = circleModulus(x, precision);

        // check for infinity
        if (DoubleUtil.isEqual(x, NumConstants.HALF_PI, precision) || DoubleUtil.isEqual(x, NumConstants.ONE_AND_HALF_PI, precision)) {
            resultMin.setDoubleValue(Double.NEGATIVE_INFINITY);
            resultMax.setDoubleValue(Double.POSITIVE_INFINITY);
        }
        else {
            double tanX = DoubleUtil.returnMinMaxIfClose(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Math.tan(x), precision);
    
            // Determine if value is infinite
            if (Double.isInfinite(tanX)) {
                resultMin.setDoubleValue(Double.NEGATIVE_INFINITY);
                resultMax.setDoubleValue(Double.POSITIVE_INFINITY);
            }
            else {
                double zmin = z.getNumMin().doubleValue();
                double zmax = z.getNumMax().doubleValue();
                double v = DoubleUtil.returnMinMaxIfClose(zmin, zmax, tanX, precision);
                resultMin.setDoubleValue(v);
                resultMax.setDoubleValue(v);
            }
        }
    }

    /**
     * Calculates minimum and maximum value of Z where Z = atan(X)
     */
    public static void atanMinMax(TrigExpr z, TrigExpr x, double precision, MutableNumber resultMin, MutableNumber resultMax) {
        resultMin.setInvalid(true);
        resultMax.setInvalid(true);
        
        double xmin = x.getNumMin().doubleValue();
        double xmax = x.getNumMax().doubleValue();
        
        // calculate atan values values
        atanMinMax(z, xmin, precision, work1, work2);
        double min1 = work1.doubleValue();
        double max1 = work2.doubleValue();
        atanMinMax(z, xmax, precision, work1, work2);
        double min2 = work1.doubleValue();
        double max2 = work2.doubleValue();
        
        // determine min value
        if (min1<min2)
            resultMin.setDoubleValue(min1);
        else
            resultMin.setDoubleValue(min2);
        
        // determine max value
        if (max1>max2)
            resultMax.setDoubleValue(max1);
        else
            resultMax.setDoubleValue(max2);
    }
    
    /**
     * Calculates minimum and maximum value of Z where Z = atan(x)
     */
    public static void atanMinMax(TrigExpr z, double x, double precision, MutableNumber resultMin, MutableNumber resultMax) {
        resultMin.setInvalid(true);
        resultMax.setInvalid(true);
        
        double zmin = z.getNumMin().doubleValue();
        double zmax = z.getNumMax().doubleValue();
        
        // map to infinity if value is large enough
        x = DoubleUtil.returnMinMaxIfClose(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, x, precision);
        
        if (Double.isInfinite(x)) {
            resultMin.setDoubleValue(z.getNumMin().doubleValue());
            resultMax.setDoubleValue(z.getNumMax().doubleValue());
            return;
        }
        
        // compute smaller angle base
        double angleBase = Math.atan(x);
        if (angleBase < 0) angleBase += NumConstants.PI;
        
        // Initialize variables used to calc min and max
        double startOffset = (Math.floor(zmin / NumberMath.TWO_PI)-1) * NumberMath.TWO_PI;
        double endOffset = Math.ceil(zmax / NumberMath.TWO_PI) * NumberMath.TWO_PI;

        // initialize small and large angle below starting offset
        double angle = startOffset + ((DoubleUtil.compare(startOffset, 0, precision) >= 0) ? angleBase : (NumberMath.TWO_PI-angleBase));
        
        // Determine minimum value
        while (DoubleUtil.compare(angle, endOffset, precision) <= 0) {
            // check if smaller angle is in domain 
            angle = DoubleUtil.returnMinMaxIfClose(zmin, zmax, angle, precision);
            work3.setDoubleValue(angle);
            if (z.isInDomain(work3)) {
                resultMin.setDoubleValue(angle);
                break;
            }

            // move up pi and check again
            double prevAngle = angle;
            angle += NumberMath.PI;
            
            // check if values flipped from negative to positive
            if (prevAngle < 0 && angle >0) {
                angle = angleBase;
            }
        }
        
        // initialize small and large angle above starting offset
        angle = endOffset + ((DoubleUtil.compare(endOffset, 0, precision) >= 0) ? angleBase : (NumberMath.TWO_PI-angleBase));
        
        // Determine maximum value
        while (DoubleUtil.compare(angle, startOffset, precision) >= 0) {
            // check if larger angle is in domain 
            angle = DoubleUtil.returnMinMaxIfClose(zmin, zmax, angle, precision);
            work3.setDoubleValue(angle);
            if (z.isInDomain(work3)) {
                resultMax.setDoubleValue(angle);
                break;
            }
            
            // move down pi and check again
            double prevAngle = angle;
            angle -= NumberMath.PI;
            
            // check if values flipped from positive to negative
            if (prevAngle > 0 && angle < 0) {
                angle = -angleBase;
            }
        }
    }

    /**
     * Calculates minimum and maximum value of Z where Z = X^Y given minimum and maximum values of y
     */
    private static void powerMinMax(TrigExpr z, Number xminNum, Number xmaxNum, Number yminNum, Number ymaxNum, boolean isRecip, double precision, MutableNumber resultMin, MutableNumber resultMax) {
        double xmin = xminNum.doubleValue();
        double xmax = xmaxNum.doubleValue();
        double ymin = yminNum.doubleValue();
        double ymax = ymaxNum.doubleValue();
        double zmin = z.getNumMin().doubleValue();
        double zmax = z.getNumMax().doubleValue();
        
        // handle infinite y
        resultMin.setInvalid(true);
        resultMax.setInvalid(true);
        if (Double.isInfinite(ymin) && Double.isInfinite(ymax)) {
            resultMin.setDoubleValue(Double.NEGATIVE_INFINITY);
            resultMax.setDoubleValue(Double.POSITIVE_INFINITY);
        }
        
        else {
            // calculate all 4 combinations
            double p1 = Math.pow(xmin, ymin);
            double p2 = Math.pow(xmax, ymin);
            double p3 = Math.pow(xmin, ymax);
            double p4 = Math.pow(xmax, ymax);
            
            // Determine min
            if (Double.isInfinite(xmin)) {
                if (!isRecip && DoubleUtil.isEqual(ymin, 0, precision) && DoubleUtil.isEqual(ymax, 0, precision))
                    resultMin.setDoubleValue(1);
                else
                    resultMin.setDoubleValue(zmin);
            }
            else {
                double m = p1;
                
                if (Double.isNaN(m)) m = p2;
                else if (!Double.isNaN(p2)) m = Math.min(m, p2);
                
                if (Double.isNaN(m)) m = p3;
                else if (!Double.isNaN(p3)) m = Math.min(m, p3);
                
                if (Double.isNaN(m)) m = p4;
                else if (!Double.isNaN(p4)) m = Math.min(m, p4);
                
                if (Double.isInfinite(m) && zmin == Double.NEGATIVE_INFINITY)
                    m = Double.NEGATIVE_INFINITY;
                else if (DoubleUtil.isEqual(m, 1, precision) && DoubleUtil.compare(xmin, 1, precision) <= 0 && DoubleUtil.compare(xmax, 1, precision) >= 0)
                    m = Double.NEGATIVE_INFINITY;
                else if (isRecip && DoubleUtil.isEqual(m, 1, precision) && DoubleUtil.compare(ymin, 0, precision) <= 0 && DoubleUtil.compare(ymax, 0, precision) >= 0)
                    m = Double.NEGATIVE_INFINITY;
                
            	m = DoubleUtil.returnMinMaxIfClose(zmin, zmax, m, precision);
                resultMin.setDoubleValue(m);
            }
    
            // Determine max
            if (Double.isInfinite(xmax)) {
                if (!isRecip && DoubleUtil.isEqual(ymin, 0, precision) && DoubleUtil.isEqual(ymax, 0, precision))
                    resultMax.setDoubleValue(1);
                else 
                    resultMax.setDoubleValue(zmax);
            }
            else {
                double m = Math.max(resultMin.doubleValue(), p1);
                
                if (Double.isNaN(m)) m = p2;
                else if (!Double.isNaN(p2)) m = Math.max(m, p2);
                
                if (Double.isNaN(m)) m = p3;
                else if (!Double.isNaN(p3)) m = Math.max(m, p3);
                
                if (Double.isNaN(m)) m = p4;
                else if (!Double.isNaN(p4)) m = Math.max(m, p4);
                
                if (DoubleUtil.isEqual(m, 1, precision) && DoubleUtil.compare(xmin, 1, precision) <= 0 && DoubleUtil.compare(xmax, 1, precision) >= 0)
                    m = Double.POSITIVE_INFINITY;
                else if (isRecip && DoubleUtil.isEqual(m, 1, precision) && DoubleUtil.compare(ymin, 0, precision) <= 0 && DoubleUtil.compare(ymax, 0, precision) >= 0)
                    m = Double.POSITIVE_INFINITY;
                
                resultMax.setDoubleValue(DoubleUtil.returnMinMaxIfClose(zmin, zmax, m, precision));
            }
        }
    }
    
    /**
     * Calculates minimum and maximum value of Z where Z = X^Y
     */
    public static void powerMinMax(TrigExpr z, TrigExpr x, TrigExpr y, boolean yreciprocal, double precision, MutableNumber resultMin, MutableNumber resultMax) {
        // Determine min and max values of y
        Number xmin = x.getNumMin();
        Number xmax = x.getNumMax();
        Number ymin = null;
        Number ymax = null;

        // Determine if reciprocal of y should be used
        if (yreciprocal) {
            BigDecimal one = BigDecimal.valueOf(1);
            BigDecimal yminRecip = one.divide(new BigDecimal(y.getNumMin().doubleValue()), 20, BigDecimal.ROUND_DOWN);
            BigDecimal yminRecipUp = yminRecip.setScale(15, BigDecimal.ROUND_UP);
            BigDecimal yminRecipDn = yminRecip.setScale(15, BigDecimal.ROUND_DOWN);
            BigDecimal ymaxrecip = one.divide(new BigDecimal(y.getNumMax().doubleValue()), 20, BigDecimal.ROUND_DOWN);
            BigDecimal ymaxrecipUp = ymaxrecip.setScale(15, BigDecimal.ROUND_UP);
            BigDecimal ymaxrecipDn = ymaxrecip.setScale(15, BigDecimal.ROUND_DOWN);

            ymin = yminRecipDn.min(ymaxrecipDn);
            ymax = yminRecipUp.max(ymaxrecipUp);
        }
        else {
            ymin = y.getNumMin();
            ymax = y.getNumMax();
        }
        
        powerMinMax(z, xmin, xmax, ymin, ymax, yreciprocal, precision, resultMin, resultMax);    
    }

    /**
     * Calculates minimum and maximum value of Z where Z = x^Y
     */
    public static void powerMinMax(TrigExpr z, Number xconst, TrigExpr y, boolean yreciprocal, double precision, MutableNumber resultMin, MutableNumber resultMax) {
        // Determine min and max values of y
        Number xmin = xconst;
        Number xmax = xconst;
        Number ymin = null;
        Number ymax = null;
        
        // Determine if reciprocal of y should be used
        if (yreciprocal) {
            BigDecimal one = BigDecimal.valueOf(1);
            
            BigDecimal yminRecipUp = null;
            BigDecimal yminRecipDn = null;
            if (Double.isInfinite(y.getNumMin().doubleValue())) {
            	yminRecipUp = new BigDecimal(0);
                yminRecipDn = yminRecipUp;
            }
            else {
                BigDecimal yminRecip = one.divide(new BigDecimal(y.getNumMin().doubleValue()), 20, BigDecimal.ROUND_DOWN);
                yminRecipUp = yminRecip.setScale(15, BigDecimal.ROUND_UP);
                yminRecipDn = yminRecip.setScale(15, BigDecimal.ROUND_DOWN);
            }
            
            BigDecimal ymaxrecipUp = null;
            BigDecimal ymaxrecipDn = null;
            if (Double.isInfinite(y.getNumMax().doubleValue())) {
                ymaxrecipUp = new BigDecimal(0);
                ymaxrecipDn = yminRecipUp;
            }
            else {
                BigDecimal ymaxrecip = one.divide(new BigDecimal(y.getNumMax().doubleValue()), 20, BigDecimal.ROUND_DOWN);
                ymaxrecipUp = ymaxrecip.setScale(15, BigDecimal.ROUND_UP);
                ymaxrecipDn = ymaxrecip.setScale(15, BigDecimal.ROUND_DOWN);
            }
            
            ymin = yminRecipDn.min(ymaxrecipDn);
            ymax = yminRecipUp.max(ymaxrecipUp);
        }
        else {
            ymin = y.getNumMin();
            ymax = y.getNumMax();
        }
        
        powerMinMax(z, xmin, xmax, ymin, ymax, yreciprocal, precision, resultMin, resultMax);    
    }

    /**
     * Calculates minimum and maximum value of Z where Z = X^y
     */
    public static void powerMinMax(TrigExpr z, TrigExpr x, Number y, double precision, MutableNumber resultMin, MutableNumber resultMax) {
        powerMinMax(z, x.getNumMin(), x.getNumMax(), y, y, false, precision, resultMin, resultMax);
    }

    /**
     * Calculates minimum and maximum value of Z where Z = e^X
     */
    public static void expMinMax(TrigExpr z, TrigExpr x, double precision, MutableNumber resultMin, MutableNumber resultMax) {
        double zmin = z.getNumMin().doubleValue();
        double zmax = z.getNumMax().doubleValue();
        double exp1 = Math.exp(x.getNumMin().doubleValue());
        double exp2 = Math.exp(x.getNumMax().doubleValue());
        
        // Determine min
        resultMin.setDoubleValue(DoubleUtil.returnMinMaxIfClose(zmin, zmax, Math.min(exp1, exp2), precision));
        
        // Determine max
        resultMax.setDoubleValue(DoubleUtil.returnMinMaxIfClose(zmin, zmax, Math.max(exp1, exp2), precision));
    }
    
    /**
     * Calculates minimum and maximum value of Z where Z = natural log of X
     */
    public static void natLogMinMax(TrigExpr z, TrigExpr x, double precision, MutableNumber resultMin, MutableNumber resultMax) {
        double zmin = z.getNumMin().doubleValue();
        double zmax = z.getNumMax().doubleValue();
        double xmin = x.getNumMin().doubleValue();
        double xmax = x.getNumMax().doubleValue();
        double log1 = Math.log(xmin);
        double log2 = Math.log(xmax);
        
        if (log2 > Double.NEGATIVE_INFINITY && Double.isNaN(log1) && Double.isInfinite(xmin))
            log1 = Double.NEGATIVE_INFINITY;
        
        // Determine min
        resultMin.setDoubleValue(DoubleUtil.returnMinMaxIfClose(zmin, zmax, Math.min(log1, log2), precision));
        
        // Determine max
        resultMax.setDoubleValue(DoubleUtil.returnMinMaxIfClose(zmin, zmax, Math.max(log1, log2), precision));
    }

    /**
     * Calculates minimum and maximum value of Z where Z = logX(y)
     */
    public static void logMinMax(Number xmin, Number xmax, Number ymin, Number ymax, double precision, MutableNumber resultMin, MutableNumber resultMax) {
        double minX = xmin.doubleValue();
        double maxX = xmax.doubleValue();
        double minY = ymin.doubleValue();
        double maxY = ymax.doubleValue();
        
        // handle infinity values
        if (Double.isInfinite(minX) || Double.isInfinite(maxX)) {
            resultMin.setDoubleValue(Double.NEGATIVE_INFINITY);
            resultMax.setDoubleValue(Double.POSITIVE_INFINITY);
            return;
        }
        if ((minX!=0 || maxX!=0) && (Double.isInfinite(minY) || Double.isInfinite(maxY))) {
            resultMin.setDoubleValue(Double.NEGATIVE_INFINITY);
            resultMax.setDoubleValue(Double.POSITIVE_INFINITY);
            return;
        }
        
        double p1 = Math.log(minY) / Math.log(minX);
        double p2 = Math.log(minY) / Math.log(maxX);
        double p3 = Math.log(maxY) / Math.log(minX);
        double p4 = Math.log(maxY) / Math.log(maxX);

        // Determine min
        double d = p1;
        if (!Double.isNaN(p2)) d = Double.isNaN(d) ? p2 : Math.min(d, p2);
        if (!Double.isNaN(p3)) d = Double.isNaN(d) ? p3 : Math.min(d, p3);
        if (!Double.isNaN(p4)) d = Double.isNaN(d) ? p4 : Math.min(d, p4);
        if (ymin.doubleValue() <= 0 && ymax.doubleValue() > 0) d = 0;
        else if (ymin.doubleValue() == 1) d = Double.NEGATIVE_INFINITY;
        else if (ymin.doubleValue() == Double.NEGATIVE_INFINITY) d = Double.NEGATIVE_INFINITY;
        resultMin.setDoubleValue(d);

        // Determine max
        d = p1;
        if (!Double.isNaN(p2)) d = Double.isNaN(d) ? p2 : Math.max(d, p2);
        if (!Double.isNaN(p3)) d = Double.isNaN(d) ? p3 : Math.max(d, p3);
        if (!Double.isNaN(p4)) d = Double.isNaN(d) ? p4 : Math.max(d, p4);
        if (ymax.doubleValue() == Double.POSITIVE_INFINITY) d = Double.POSITIVE_INFINITY;
        else if (ymax.doubleValue() == 1) d = Double.POSITIVE_INFINITY;
        resultMax.setDoubleValue(d);
    }
}
