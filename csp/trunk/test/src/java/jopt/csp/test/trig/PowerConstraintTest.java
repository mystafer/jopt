package jopt.csp.test.trig;

import java.math.BigDecimal;
import java.util.Random;

import jopt.csp.CspSolver;
import jopt.csp.spi.arcalgorithm.constraint.num.NumExpr;
import jopt.csp.spi.arcalgorithm.constraint.num.PowerConstraint;
import jopt.csp.util.DoubleUtil;
import jopt.csp.variable.CspDoubleVariable;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

public class PowerConstraintTest extends TestCase {
    private CspSolver solver;
    private CspVariableFactory varFactory;
    
    private CspDoubleVariable x;
    private CspDoubleVariable y;
    private CspDoubleVariable z;
    
    private NumExpr xexpr;
    private NumExpr yexpr;
    private NumExpr zexpr;
    
    public void setUp() {
        solver = CspSolver.createSolver();
        varFactory = solver.getVarFactory();
        
        x = varFactory.doubleVar("x", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        y = varFactory.doubleVar("y", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        z = varFactory.doubleVar("z", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        
        xexpr = (NumExpr) x;
        yexpr = (NumExpr) y;
        zexpr = (NumExpr) z;
    }
    
    public void tearDown() {
        solver = null;
        varFactory = null;
        
        x = null;
        y = null;
        z = null;
        
        xexpr = null;
        yexpr = null;
        zexpr = null;
    }
    
    public void testConstY() {
    	Random rand = new Random();
    	double y = 0;
    	double yrecip = 0;
    	double yconstRecipMin = 0;
    	double yconstRecipMax = 0;
    	double minZ = 0;
    	double maxZ = 0;
    	double p1 = 0;
    	double p2 = 0;
    	double p3 = 0;
    	double p4 = 0;
    	double minX = 0;
    	double maxX = 0;
        try {
            solver.addConstraint(new PowerConstraint(xexpr, new Double(0), zexpr));
            assertTrue("z bound", z.isBound());
            assertEquals("z", 1, z.getMin(), 0.0001);
            
            solver.clear();
            solver.addConstraint(new PowerConstraint(xexpr, new Double(1), zexpr));
            assertFalse("z bound", z.isBound());
            assertEquals("z min", Double.NEGATIVE_INFINITY, z.getMin(), 0.0001);
            assertEquals("z min", Double.POSITIVE_INFINITY, z.getMax(), 0.0001);
            
            solver.clear();
            solver.addVariable(x);
            x.setMin(-0.23714121773002395d);
            x.setMax(0.6989226568066242d);
            y = -0.08323008552974487d;
            solver.addConstraint(new PowerConstraint(xexpr, new Double(y), zexpr));
            assertEquals("z min", 1.030263176740829d, z.getMin(), 0.0001);
            assertEquals("z min", 1.03026317674083d, z.getMax(), 0.0001);
            
            solver.clear();
            solver.addVariable(x);
            x.setMin(-0.44585198524331804d);
            x.setMax(0.07127399055213246d);
            y = -0.9814671702968868d;
            solver.addConstraint(new PowerConstraint(xexpr, new Double(y), zexpr));
            assertTrue("z bound", z.isBound());
            assertEquals("z min", 13.360124086548572d, z.getMin(), 0.0001);
            assertEquals("z min", 13.360124086548572d, z.getMax(), 0.0001);
            
            solver.clear();
            solver.addVariable(x);
            x.setMin(-0.8407386908812085d);
            x.setMax(0.04312608731133616d);
            y = -0.6466828978647876d;
            solver.addConstraint(new PowerConstraint(xexpr, new Double(y), zexpr));
            assertTrue("z bound", z.isBound());
            assertEquals("z min", 7.636427901166096d, z.getMin(), 0.0001);
            assertEquals("z min", 7.636427901166096d, z.getMax(), 0.0001);
            
            solver.clear();
            solver.addVariable(x);
            x.setMin(-0.8214027379450982d);
            x.setMax(0.030525801614696424d);
            y = -0.9136425506196973d;
            solver.addConstraint(new PowerConstraint(xexpr, new Double(y), zexpr));
            solver.addVariable(x);
            assertTrue("z bound", z.isBound());
            assertEquals("z min", 24.236652518495063d, z.getMin(), 0.0001);
            assertEquals("z min", 24.236652518495063d, z.getMax(), 0.0001);
            
            solver.clear();
            solver.addVariable(x);
            solver.addVariable(x);
            x.setMin(9.366962252255195E-4d);
            x.setMax(0.9770615338662825d);
            y = 2.775116051722113d;
            solver.addConstraint(new PowerConstraint(xexpr, new Double(y), zexpr));
            assertEquals("z min", 3.943075890294413E-9d, z.getMin(), 0.0001);
            assertEquals("z min", 0.9376314084181139d, z.getMax(), 0.0001);

            solver.clear();
            solver.addVariable(x);
            x.setMin(3.308993794337269d);
            x.setMax(4.044243911319325d);
            y = 2.756713427102905d;
            solver.addConstraint(new PowerConstraint(xexpr, new Double(y), zexpr));
            assertEquals("z min", 27.0802588185378d, z.getMin(), 0.0001);
            assertEquals("z min", 47.084357024531826d, z.getMax(), 0.0001);
            
            // test with random x and y
            for (int i=1; i<=2; i++) {
                for (int j=1; j<=5; j++) {
                    int base = j * ((i==1) ? -1 : 1);
                    
                    for (int k=0; k<100; k++) {
                        solver.clear();
                        solver.addVariable(x);
                        
                        // initialize x
                        double n1 = base * rand.nextDouble();
                        double n2 = base * rand.nextDouble();
                        minX = Math.min(n1, n2);
                        x.setMin(minX);
                        
                        maxX = Double.NEGATIVE_INFINITY;
                        while (maxX < minX) {
                            n1 = minX + rand.nextDouble();
                            n2 = minX + rand.nextDouble();
                            maxX = Math.max(n1, n2);
                        }
                        x.setMax(maxX);
                        
                        // initialize y
                        y = base * rand.nextDouble();
                        
                        // calculate z
                        p1 = Math.pow(minX, y);
                        p2 = Math.pow(maxX, y);
                        minZ = Math.min(p1, p2);
                        maxZ = Math.max(p1, p2);
                        if (Double.isNaN(p1)) {
                            minZ = p2;
                            maxZ = p2;
                        }
                        else if (Double.isNaN(p2)) {
                            minZ = p1;
                            maxZ = p1;
                        }
                        
                        try {
                        	solver.addConstraint(new PowerConstraint(xexpr, new Double(y), zexpr));
                            
                            // check for min NaN
                            if (Double.isNaN(minZ))
                                assertTrue("z min", DoubleUtil.isEqual(maxZ, z.getMin(), DoubleUtil.DEFAULT_PRECISION));
                            else
                                assertTrue("z min", DoubleUtil.isEqual(minZ, z.getMin(), DoubleUtil.DEFAULT_PRECISION));
                            
                            // check for max NaN
                            if (Double.isNaN(maxZ))
                                assertTrue("z max", DoubleUtil.isEqual(minZ, z.getMax(), DoubleUtil.DEFAULT_PRECISION));
                            else
                                assertTrue("z max", DoubleUtil.isEqual(maxZ, z.getMax(), DoubleUtil.DEFAULT_PRECISION));
                        }
                        catch(PropagationFailureException propx) {
                            
                            // failure should occur if either result is NaN
                        	if (!Double.isNaN(minZ) && !Double.isNaN(maxZ)) {
                                propx.printStackTrace(System.out);
                                fail();
                            }
                        }
                    }
                }
            }

        	solver.clear();
        	solver.addVariable(z);
        	z.setMin(-0.4593403188092644d);
        	z.setMax(0.48400692894710406d);
        	y = -0.023744010429082918d;
            yrecip = -42.11588446638935;
            assertEquals("yrecip", 1 / y, yrecip, 0.0001);
        	solver.addConstraint(new PowerConstraint(xexpr, new Double(y), zexpr));
        	assertTrue("z bound", x.isBound());
        	assertEquals("x min", 1.8739348596189156E13d, x.getMin(), 0.0001);
        	assertEquals("x min", 1.8739348596189156E13d, x.getMax(), 0.0001);
        	
            solver.clear();
            solver.addVariable(z);
            z.setMin(-0.5332628448689378d);
            z.setMax(0.11905977799283429d);
            y = -0.013108034870272123d;
            yrecip = -76.28908603744354d;
            assertEquals("yrecip", 1 / y, yrecip, 0.0001);
            solver.addConstraint(new PowerConstraint(xexpr, new Double(y), zexpr));
            assertTrue("z bound", x.isBound());
            assertEquals("x min", 3.2287782244083376E70d, x.getMin(), 0.0001);
            assertEquals("x min", 3.2287782244083376E70d, x.getMax(), 0.0001);
            
            solver.clear();
            solver.addVariable(z);
            z.setMin(-0.5916786990797678d);
            z.setMax(0.3339893519530165d);
            y = -0.0015317644112420314d;
            yrecip = -652.841907450474d;
            assertEquals("yrecip", 1 / y, yrecip, 0.0001);
            solver.addConstraint(new PowerConstraint(xexpr, new Double(y), zexpr));
            assertFalse("z bound", x.isBound());
            assertEquals("x min", Double.NEGATIVE_INFINITY, x.getMin(), 0.0001);
            assertEquals("x min", Double.POSITIVE_INFINITY, x.getMax(), 0.0001);
            
            solver.clear();
            solver.addVariable(z);
            z.setMin(-0.9153488890259519d);
            z.setMax(0.07393389468041534d);
            y = -0.08389284805681807d;
            yrecip = -11.919967233949794d;
            assertEquals("yrecip", 1 / y, yrecip, 0.0001);
            solver.addConstraint(new PowerConstraint(xexpr, new Double(y), zexpr));
            assertTrue("x bound", x.isBound());
            assertEquals("x min", 3.043318521510235E13d, x.getMin(), 0.0001);
            assertEquals("x max", 3.043318521510235E13d, x.getMax(), 0.0001);
            
        	solver.clear();
        	solver.addVariable(z);
        	z.setMin(0.16212201152668815d);
        	z.setMax(0.8837283839850757d);
        	y = 0.020125170447540584524115890744607d;
        	yrecip = 49.68902015546437d;
        	assertEquals("yrecip", 1 / y, yrecip, 0.0001);
        	solver.addConstraint(new PowerConstraint(xexpr, new Double(y), zexpr));
        	assertFalse("x bound", x.isBound());
        	assertEquals("x min", 5.467923091303813E-40d, x.getMin(), 0.0001);
        	assertEquals("x max", 0.0021509681221760007d, x.getMax(), 0.0001);
        	
        	solver.clear();
        	solver.addVariable(z);
        	z.setMin(0.714088126945697d);
        	z.setMax(1.2001292843655484d);
        	y = 0.8111066810018106d;
        	yrecip = 1.232883446065176d;
        	assertEquals("yrecip", 1 / y, yrecip, 0.0001);
        	solver.addConstraint(new PowerConstraint(xexpr, new Double(y), zexpr));
        	assertFalse("x bound", x.isBound());
        	assertEquals("x min", 0.6602266189876176d, x.getMin(), 0.0001);
        	assertEquals("x max", 1.2522150826855336d, x.getMax(), 0.0001);
        	
        	solver.clear();
        	solver.addVariable(z);
        	z.setMin(0.03057563726687007d);
        	z.setMax(0.7320863688847639d);
        	y = 0.009141529720023689d;
        	yrecip = 109.39088212004508d;
        	assertEquals("yrecip", 1 / y, yrecip, 0.0001);
        	solver.addConstraint(new PowerConstraint(xexpr, new Double(y), zexpr));
        	//assertFalse("x bound", x.isBound());
        	assertEquals("x min", 2.060116492680186E-166d, x.getMin(), 0.0001);
        	assertEquals("x max", 1.5288073925797909E-15d, x.getMax(), 0.0001);
        	
        	solver.clear();
        	solver.addVariable(z);
        	z.setMin(0d);
        	z.setMax(2.502845031054754E-124d);
        	y = 0.0015876747529747215d;
        	yrecip = 629.8519253558488d;
        	assertEquals("yrecip", 1 / y, yrecip, 0.0001);
        	solver.addConstraint(new PowerConstraint(xexpr, new Double(y), zexpr));
        	//assertFalse("x bound", x.isBound());
        	assertEquals("x min", 2.060116492680186E-166d, x.getMin(), 0.0001);
        	assertEquals("x max", 1.5288073925797909E-15d, x.getMax(), 0.0001);
        	
            // test with random y and z
            for (int j=1; j<=5; j++) {
                for (int k=0; k<100; k++) {
                    solver.clear();
                    solver.addVariable(z);
                    
                    // initialize z
                    double n1 = j * rand.nextDouble();
                    double n2 = j * rand.nextDouble();
                    minZ = Math.min(n1, n2);
                    z.setMin(minZ);
                    
                    maxZ = Double.NEGATIVE_INFINITY;
                    while (maxZ < minZ) {
                        n1 = minZ + rand.nextDouble();
                        n2 = minZ + rand.nextDouble();
                        maxZ = Math.max(n1, n2);
                    }
                    z.setMax(maxZ);
                    
                    // initialize y
                    y = j * rand.nextDouble();
                    yconstRecipMin = 0;
                    yconstRecipMax = 0;
                    
                    if (y == 0) {
                        yconstRecipMin = Double.NEGATIVE_INFINITY;
                        yconstRecipMax = Double.POSITIVE_INFINITY;
                    }
                    else {
                        BigDecimal one = BigDecimal.valueOf(1);
                        BigDecimal yconstRecip = one.divide(new BigDecimal(y), 20, BigDecimal.ROUND_DOWN);
                        double yconstRecipUp = yconstRecip.setScale(15, BigDecimal.ROUND_UP).doubleValue();
                        double yconstRecipDn = yconstRecip.setScale(15, BigDecimal.ROUND_DOWN).doubleValue();
                        yconstRecipMin = Math.min(yconstRecipUp, yconstRecipDn);
                        yconstRecipMax = Math.max(yconstRecipUp, yconstRecipDn);
                    }
                    
                    // calculate power for each z, y combination
                    p1 = Math.pow(minZ, yconstRecipMin);
                    p2 = Math.pow(maxZ, yconstRecipMin);
                    p3 = Math.pow(minZ, yconstRecipMax);
                    p4 = Math.pow(maxZ, yconstRecipMax);

                    // calculate min x
                    minX = p1;
                    
                    if (Double.isNaN(minX)) minX = p2;
                    else if (!Double.isNaN(p2)) minX = Math.min(minX, p2);
                    
                    if (Double.isNaN(minX)) minX = p3;
                    else if (!Double.isNaN(p3)) minX = Math.min(minX, p3);
                    
                    if (Double.isNaN(minX)) minX = p4;
                    else if (!Double.isNaN(p4)) minX = Math.min(minX, p4);
                    
                    if (Double.isInfinite(minX) && (Double.isNaN(p1) || Double.isNaN(p2) || Double.isNaN(p3) || Double.isNaN(p4)))
                        minX = Double.NEGATIVE_INFINITY;
                    
                    // calculate max x
                    maxX = p1;
                    
                    if (Double.isNaN(maxX)) maxX = p2;
                    else if (!Double.isNaN(p2)) maxX = Math.max(maxX, p2);
                    
                    if (Double.isNaN(maxX)) maxX = p3;
                    else if (!Double.isNaN(p3)) maxX = Math.max(maxX, p3);
                    
                    if (Double.isNaN(maxX)) maxX = p4;
                    else if (!Double.isNaN(p4)) maxX = Math.max(maxX, p4);
                    
                    solver.addConstraint(new PowerConstraint(xexpr, new Double(y), zexpr));

                    // check for min NaN
                    if (Double.isNaN(minX))
                        assertTrue("x min", DoubleUtil.isEqual(maxX, x.getMin(), 0.00001));
                    else
                        assertTrue("x min", DoubleUtil.isEqual(minX, x.getMin(), 0.00001));
                    
                    // check for max NaN
                    if (Double.isNaN(maxX))
                        assertTrue("x max", DoubleUtil.isEqual(minX, x.getMax(), 0.00001));
                    else
                        assertTrue("x max", DoubleUtil.isEqual(maxX, x.getMax(), 0.00001));
                }
            }
        }
        catch(PropagationFailureException propx) {
            System.out.println("--------------");
            System.out.println("Propagation Failure in PowerConstraintTest.testConstY()");
            System.out.println("y: " + y);
            System.out.println("yrecip: " + yrecip);
            System.out.println("yconstRecipMin: " + yconstRecipMin);
            System.out.println("yconstRecipMaxy: " + yconstRecipMax);
            System.out.println("minZ: " + minZ);
            System.out.println("maxZ: " + maxZ);
            System.out.println("p1: " + p1);
            System.out.println("p2: " + p2);
            System.out.println("p3: " + p3);
            System.out.println("p4: " + p4);
            System.out.println("minX: " + minX);
            System.out.println("maxX: " + maxX);
            System.out.println(xexpr);
            System.out.println(zexpr);
            propx.printStackTrace(System.out);
            System.out.println("--------------");
            
            fail();
        }
    }

    public void testConstX() {
        try {
            solver.addConstraint(new PowerConstraint(new Double(0), yexpr, zexpr));
            assertFalse("z bound", z.isBound());
            assertEquals("z min", 0, z.getMin(), 0.0001);
            assertEquals("z min", 1, z.getMax(), 0.0001);
            assertEquals("y min", 0, y.getMin(), 0.0001);
            assertEquals("y max", Double.POSITIVE_INFINITY, y.getMax(), 0.0001);
            
            solver.clear();
            solver.addConstraint(new PowerConstraint(new Double(1), yexpr, zexpr));
            assertFalse("z bound", z.isBound());
            assertEquals("z min", Double.NEGATIVE_INFINITY, z.getMin(), 0.0001);
            assertEquals("z max", Double.POSITIVE_INFINITY, z.getMax(), 0.0001);
            assertEquals("y min", Double.NEGATIVE_INFINITY, y.getMin(), 0.0001);
            assertEquals("y max", Double.POSITIVE_INFINITY, y.getMax(), 0.0001);
            
            solver.clear();
            solver.addConstraint(new PowerConstraint(new Double(Double.NEGATIVE_INFINITY), yexpr, zexpr));
            assertFalse("z bound", z.isBound());
            assertEquals("z min", Double.NEGATIVE_INFINITY, z.getMin(), 0.0001);
            assertEquals("z max", Double.POSITIVE_INFINITY, z.getMax(), 0.0001);
            assertEquals("y min", Double.NEGATIVE_INFINITY, y.getMin(), 0.0001);
            assertEquals("y max", Double.POSITIVE_INFINITY, y.getMax(), 0.0001);
            
            solver.clear();
            solver.addConstraint(new PowerConstraint(new Double(Double.POSITIVE_INFINITY), yexpr, zexpr));
            assertFalse("z bound", z.isBound());
            assertEquals("z min", Double.NEGATIVE_INFINITY, z.getMin(), 0.0001);
            assertEquals("z max", Double.POSITIVE_INFINITY, z.getMax(), 0.0001);
            assertEquals("y min", Double.NEGATIVE_INFINITY, y.getMin(), 0.0001);
            assertEquals("y max", Double.POSITIVE_INFINITY, y.getMax(), 0.0001);

        	solver.clear();
            solver.addVariable(y);
            y.setMin(1.5667399569882579d);
            y.setMax(2.54748262227823d);
        	solver.addConstraint(new PowerConstraint(new Double(1.427588574330915d), yexpr, zexpr));
            assertEquals("y min", 1.5667399569882579d, y.getMin(), 0.0001);
            assertEquals("y max", 2.54748262227823d, y.getMax(), 0.0001);
            assertEquals("z min", 1.7467180050665156d, z.getMin(), 0.0001);
            assertEquals("z max", 2.4765586501094052d, z.getMax(), 0.0001);
            
            solver.clear();
            solver.addVariable(y);
            y.setMin(0.9551295678665934d);
            y.setMax(1.5695288061575599d);
            solver.addConstraint(new PowerConstraint(new Double(1.7415063835618438d), yexpr, zexpr));
            assertEquals("y min", 0.9551295678665934d, y.getMin(), 0.0001);
            assertEquals("y max", 1.5695288061575599d, y.getMax(), 0.0001);
            assertEquals("z min", 1.6986920673049823d, z.getMin(), 0.0001);
            assertEquals("z max", 2.388574758941177d, z.getMax(), 0.0001);
            
            // test with random x and y
            Random rand = new Random();
            for (int j=1; j<=5; j++) {
                for (int k=0; k<100; k++) {
                    solver.clear();
                    solver.addVariable(y);
                    
                    // initialize x
                    double x = j * rand.nextDouble();
                    
                    // initialize y
                    double n1 = j * rand.nextDouble();
                    double n2 = j * rand.nextDouble();
                    double minY = Math.min(n1, n2);
                    y.setMin(minY);
                    
                    double maxY = Double.NEGATIVE_INFINITY;
                    while (maxY < minY) {
                        n1 = minY + rand.nextDouble();
                        n2 = minY + rand.nextDouble();
                        maxY = Math.max(n1, n2);
                    }
                    y.setMax(maxY);
                    
                    // calculate z
                    double p1 = Math.pow(x, minY);
                    double p2 = Math.pow(x, maxY);
                    double minZ = Math.min(p1, p2);
                    double maxZ = Math.max(p1, p2);
                    if (Double.isNaN(p1)) {
                        minZ = p2;
                        maxZ = p2;
                    }
                    else if (Double.isNaN(p2)) {
                        minZ = p1;
                        maxZ = p1;
                    }
                    
                    solver.addConstraint(new PowerConstraint(new Double(x), yexpr, zexpr));
                    
                    // check for min NaN
                    if (Double.isNaN(minZ))
                        assertTrue("z min", DoubleUtil.isEqual(maxZ, z.getMin(), DoubleUtil.DEFAULT_PRECISION));
                    else
                        assertTrue("z min", DoubleUtil.isEqual(minZ, z.getMin(), DoubleUtil.DEFAULT_PRECISION));
                    
                    // check for max NaN
                    if (Double.isNaN(maxZ))
                        assertTrue("z max", DoubleUtil.isEqual(minZ, z.getMax(), DoubleUtil.DEFAULT_PRECISION));
                    else
                        assertTrue("z max", DoubleUtil.isEqual(maxZ, z.getMax(), DoubleUtil.DEFAULT_PRECISION));
                }
            }

            
            // test with random x and z
            for (int j=1; j<=5; j++) {
                for (int k=0; k<100; k++) {
                    solver.clear();
                    solver.addVariable(z);
                    
                    // initialize x
                    double x = j * rand.nextDouble();
                    
                    // initialize z
                    double n1 = j * rand.nextDouble();
                    double n2 = j * rand.nextDouble();
                    double minZ = Math.min(n1, n2);
                    z.setMin(minZ);
                    
                    double maxZ = Double.NEGATIVE_INFINITY;
                    while (maxZ < minZ) {
                        n1 = minZ + rand.nextDouble();
                        n2 = minZ + rand.nextDouble();
                        maxZ = Math.max(n1, n2);
                    }
                    z.setMax(maxZ);
                    
                    // calculate y
                    double logX = Math.log(x);
                    double p1 = Math.log(minZ) / logX;
                    double p2 = Math.log(maxZ) / logX;
                    double minY = Math.min(p1, p2);
                    double maxY = Math.max(p1, p2);
                    if (Double.isNaN(p1)) {
                        minY = p2;
                        maxY = p2;
                    }
                    else if (Double.isNaN(p2)) {
                        minY = p1;
                        maxY = p1;
                    }
                    
                    solver.addConstraint(new PowerConstraint(new Double(x), yexpr, zexpr));
                    
                    // check for min NaN
                    if (Double.isNaN(minY))
                        assertTrue("y min", DoubleUtil.isEqual(maxY, y.getMin(), DoubleUtil.DEFAULT_PRECISION));
                    else
                        assertTrue("y min", DoubleUtil.isEqual(minY, y.getMin(), DoubleUtil.DEFAULT_PRECISION));
                    
                    // check for max NaN
                    if (Double.isNaN(maxY))
                        assertTrue("y max", DoubleUtil.isEqual(minY, y.getMax(), DoubleUtil.DEFAULT_PRECISION));
                    else
                        assertTrue("y max", DoubleUtil.isEqual(maxY, y.getMax(), DoubleUtil.DEFAULT_PRECISION));
                }
            }
        }
        catch(PropagationFailureException propx) {
//            propx.printStackTrace(System.out);
            fail();
        }
    }

    public void testConstZ() {
    	Random rand = new Random();
    	double z = 0;
    	double minY = 0;
    	double maxY = 0;
    	double p1 = 0;
    	double p2 = 0;
    	double minX = 0;
    	double maxX = 0;
    	
        try {
            solver.addConstraint(new PowerConstraint(xexpr, yexpr, new Double(1)));
            assertFalse("x bound", x.isBound());
            assertEquals("x", Double.NEGATIVE_INFINITY, x.getMin(), 0.0001);
            assertEquals("x", Double.POSITIVE_INFINITY, x.getMax(), 0.0001);
            assertEquals("y min", Double.NEGATIVE_INFINITY, y.getMin(), 0.0001);
            assertEquals("y max", Double.POSITIVE_INFINITY, y.getMax(), 0.0001);
            
            solver.clear();
            solver.addConstraint(new PowerConstraint(xexpr, yexpr, new Double(Double.POSITIVE_INFINITY)));
            assertFalse("x bound", x.isBound());
            assertEquals("x", Double.NEGATIVE_INFINITY, x.getMin(), 0.0001);
            assertEquals("x", Double.POSITIVE_INFINITY, x.getMax(), 0.0001);
            assertEquals("y min", Double.NEGATIVE_INFINITY, y.getMin(), 0.0001);
            assertEquals("y max", Double.POSITIVE_INFINITY, y.getMax(), 0.0001);
            
            solver.clear();
            solver.addVariable(x);
            x.setMin(0.524172700965391d);
            x.setMax(1.4924766001611416d);
            solver.addConstraint(new PowerConstraint(xexpr, yexpr, new Double(0.9193962889454499)));
            assertFalse("x bound", x.isBound());
            assertEquals("x", 0.524172700965391d, x.getMin(), 0.0001);
            assertEquals("x", 1.4924766001611416d, x.getMax(), 0.0001);
            assertEquals("y min", -0.2098658603879418, y.getMin(), 0.0001);
            assertEquals("y max", 0.13010311160738622, y.getMax(), 0.0001);
            
            solver.clear();
            solver.addVariable(x);
            x.setMin(0.12472517263258875d);
            x.setMax(0.41466092945037103d);
            solver.addConstraint(new PowerConstraint(xexpr, yexpr, new Double(0.1032591117904218d)));
            assertFalse("x bound", x.isBound());
            assertEquals("x", 0.12472517263258875d, x.getMin(), 0.0001);
            assertEquals("x", 0.41466092945037103d, x.getMax(), 0.0001);
            assertEquals("y min", 1.0907318200686547d, y.getMin(), 0.0001);
            assertEquals("y max", 2.5792672283193707d, y.getMax(), 0.0001);
            
            // test with random x and z
            for (int j=1; j<=5; j++) {
                for (int k=0; k<100; k++) {
                    solver.clear();
                    solver.addVariable(x);
                    
                    // initialize z
                    z = j * rand.nextDouble();
                    
                    // initialize x
                    double n1 = j * rand.nextDouble();
                    double n2 = j * rand.nextDouble();
                    minX = Math.min(n1, n2);
                    x.setMin(minX);
                    
                    maxX = Double.NEGATIVE_INFINITY;
                    while (maxX < minX) {
                        n1 = minX + rand.nextDouble();
                        n2 = minX + rand.nextDouble();
                        maxX = Math.max(n1, n2);
                    }
                    x.setMax(maxX);
                    
                    // calculate y
                    double logZ = Math.log(z);
                    p1 = logZ / Math.log(minX);
                    p2 = logZ / Math.log(maxX);
                    minY = Math.min(p1, p2);
                    maxY = Math.max(p1, p2);
                    if (Double.isNaN(p1)) {
                        minY = p2;
                        maxY = p2;
                    }
                    else if (Double.isNaN(p2)) {
                        minY = p1;
                        maxY = p1;
                    }
                    
                    solver.addConstraint(new PowerConstraint(xexpr, yexpr, new Double(z)));
                    
                    // check for min NaN
                    if (Double.isNaN(minY))
                        assertTrue("y min", DoubleUtil.isEqual(maxY, y.getMin(), DoubleUtil.DEFAULT_PRECISION));
                    else
                        assertTrue("y min", DoubleUtil.isEqual(minY, y.getMin(), DoubleUtil.DEFAULT_PRECISION));
                    
                    // check for max NaN
                    if (Double.isNaN(maxY))
                        assertTrue("y max", DoubleUtil.isEqual(minY, y.getMax(), DoubleUtil.DEFAULT_PRECISION));
                    else
                        assertTrue("y max", DoubleUtil.isEqual(maxY, y.getMax(), DoubleUtil.DEFAULT_PRECISION));
                }
            }

            // test with random y and z
            for (int j=1; j<=5; j++) {
                for (int k=0; k<100; k++) {
                    solver.clear();
                    solver.addVariable(y);
                    
                    // initialize z
                    z = j * rand.nextDouble();
                    
                    // initialize y
                    double n1 = j * rand.nextDouble();
                    double n2 = j * rand.nextDouble();
                    minY = Math.min(n1, n2);
                    y.setMin(minY);
                    
                    maxY = Double.NEGATIVE_INFINITY;
                    while (maxY < minY) {
                        n1 = minY + rand.nextDouble();
                        n2 = minY + rand.nextDouble();
                        maxY = Math.max(n1, n2);
                    }
                    y.setMax(maxY);
                    
                    // calculate x
                    p1 = Math.pow(z, 1/minY);
                    p2 = Math.pow(z, 1/maxY);
                    minX = Math.min(p1, p2);
                    maxX = Math.max(p1, p2);
                    if (Double.isNaN(p1)) {
                        minX = p2;
                        maxX = p2;
                    }
                    else if (Double.isNaN(p2)) {
                        minX = p1;
                        maxX = p1;
                    }
                    
                    solver.addConstraint(new PowerConstraint(xexpr, yexpr, new Double(z)));
                    
                    // check for min NaN
                    if (Double.isNaN(minX))
                        assertTrue("x min", DoubleUtil.isEqual(maxX, x.getMin(), DoubleUtil.DEFAULT_PRECISION));
                    else
                        assertTrue("x min", DoubleUtil.isEqual(minX, x.getMin(), DoubleUtil.DEFAULT_PRECISION));
                    
                    // check for max NaN
                    if (Double.isNaN(maxX))
                        assertTrue("x max", DoubleUtil.isEqual(minX, x.getMax(), DoubleUtil.DEFAULT_PRECISION));
                    else
                        assertTrue("x max", DoubleUtil.isEqual(maxX, x.getMax(), DoubleUtil.DEFAULT_PRECISION));
                }
            }
        }
        catch(PropagationFailureException propx) {
            System.out.println("--------------");
            System.out.println("Propagation Failure in PowerConstraintTest.testConstY()");
            System.out.println("z: " + z);
            System.out.println("minY: " + minY);
            System.out.println("maxY: " + maxY);
            System.out.println("p1: " + p1);
            System.out.println("p2: " + p2);
            System.out.println("minX: " + minX);
            System.out.println("maxX: " + maxX);
            System.out.println(xexpr);
            System.out.println(yexpr);
            propx.printStackTrace(System.out);
            System.out.println("--------------");
            
            fail();
        }
    }


    public void testXY() {
        try {
            // test with random x and y
            Random rand = new Random();
            for (int j=1; j<=5; j++) {
                for (int k=0; k<100; k++) {
                    solver.clear();
                    solver.addVariable(x);
                    solver.addVariable(y);
                    
                    // initialize x
                    double n1 = j * rand.nextDouble();
                    double n2 = j * rand.nextDouble();
                    double minX = Math.min(n1, n2);
                    x.setMin(minX);
                    
                    double maxX = Double.NEGATIVE_INFINITY;
                    while (maxX < minX) {
                        n1 = minX + rand.nextDouble();
                        n2 = minX + rand.nextDouble();
                        maxX = Math.max(n1, n2);
                    }
                    x.setMax(maxX);
                    
                    // initialize y
                    n1 = j * rand.nextDouble();
                    n2 = j * rand.nextDouble();
                    double minY = Math.min(n1, n2);
                    y.setMin(minY);
                    
                    double maxY = Double.NEGATIVE_INFINITY;
                    while (maxY < minY) {
                        n1 = minY + rand.nextDouble();
                        n2 = minY + rand.nextDouble();
                        maxY = Math.max(n1, n2);
                    }
                    y.setMax(maxY);
                    
                    
                    // calculate z
                    double p1 = Math.pow(minX, minY);
                    double p2 = Math.pow(minX, maxY);
                    double p3 = Math.pow(maxX, minY);
                    double p4 = Math.pow(maxX, maxY);
                    double minZ = min(p1, p2, p3, p4);
                    double maxZ = max(p1, p2, p3, p4);
                    
                    solver.addConstraint(new PowerConstraint(xexpr, yexpr, zexpr));
                    
                    // check for min NaN
                    if (Double.isNaN(minZ))
                        assertTrue("z min", DoubleUtil.isEqual(maxZ, z.getMin(), DoubleUtil.DEFAULT_PRECISION));
                    else
                        assertTrue("z min", DoubleUtil.isEqual(minZ, z.getMin(), DoubleUtil.DEFAULT_PRECISION));
                    
                    // check for max NaN
                    if (Double.isNaN(maxZ))
                        assertTrue("z max", DoubleUtil.isEqual(minZ, z.getMax(), DoubleUtil.DEFAULT_PRECISION));
                    else
                        assertTrue("z max", DoubleUtil.isEqual(maxZ, z.getMax(), DoubleUtil.DEFAULT_PRECISION));
                }
            }
        }
        catch(PropagationFailureException propx) {
        	propx.printStackTrace(System.out);
        	fail();
        }
    }

    public void testXZ() {
        try {
        	solver.clear();
        	solver.addVariable(x);
            solver.addVariable(z);
        	x.setMin(0.26412360109149613d);
        	x.setMax(0.45497647086429294d);
            z.setMin(0.27041259621173186d);
            z.setMax(0.9451044709911331d);
        	solver.addConstraint(new PowerConstraint(xexpr, yexpr, zexpr));
        	assertEquals("y min", 0.04240831559524969d, y.getMin(), 0.0001);
        	assertEquals("y max", 1.66068628986948927d, y.getMax(), 0.0001);
            
            // test with random x and z
            Random rand = new Random();
            for (int j=1; j<=5; j++) {
                for (int k=0; k<100; k++) {
                    solver.clear();
                    solver.addVariable(x);
                    solver.addVariable(z);
                    
                    // initialize x
                    double n1 = j * rand.nextDouble();
                    double n2 = j * rand.nextDouble();
                    double minX = Math.min(n1, n2);
                    x.setMin(minX);
                    
                    double maxX = Double.NEGATIVE_INFINITY;
                    while (maxX < minX) {
                        n1 = minX + rand.nextDouble();
                        n2 = minX + rand.nextDouble();
                        maxX = Math.max(n1, n2);
                    }
                    x.setMax(maxX);
                    
                    // initialize z
                    n1 = j * rand.nextDouble();
                    n2 = j * rand.nextDouble();
                    double minZ = Math.min(n1, n2);
                    z.setMin(minZ);
                    
                    double maxZ = Double.NEGATIVE_INFINITY;
                    while (maxZ < minZ) {
                        n1 = minZ + rand.nextDouble();
                        n2 = minZ + rand.nextDouble();
                        maxZ = Math.max(n1, n2);
                    }
                    z.setMax(maxZ);
                    
                    
                    // calculate y
                    double logMinZ = Math.log(minZ);
                    double logMaxZ = Math.log(maxZ);
                    double logMinX = Math.log(minX);
                    double logMaxX = Math.log(maxX);
                    double p1 = logMinZ / logMinX;
                    double p2 = logMinZ / logMaxX;
                    double p3 = logMaxZ / logMinX;
                    double p4 = logMaxZ / logMaxX;
                    double minY = min(p1, p2, p3, p4);
                    double maxY = max(p1, p2, p3, p4);
                    
                    solver.addConstraint(new PowerConstraint(xexpr, yexpr, zexpr));
                    
                    // check for min NaN
                    if (Double.isNaN(minY))
                        assertTrue("y min", DoubleUtil.isEqual(maxY, y.getMin(), DoubleUtil.DEFAULT_PRECISION));
                    else
                        assertTrue("y min", DoubleUtil.isEqual(minY, y.getMin(), DoubleUtil.DEFAULT_PRECISION));
                    
                    // check for max NaN
                    if (Double.isNaN(maxY))
                        assertTrue("y max", DoubleUtil.isEqual(minY, y.getMax(), DoubleUtil.DEFAULT_PRECISION));
                    else
                        assertTrue("z max", DoubleUtil.isEqual(maxY, y.getMax(), DoubleUtil.DEFAULT_PRECISION));
                }
            }
        }
        catch(PropagationFailureException propx) {
            propx.printStackTrace(System.out);
            fail();
        }
    }

    public void testYZ() {
        try {
        	solver.clear();
        	solver.addVariable(y);
        	solver.addVariable(z);
        	y.setMin(0.270188403758157d);
        	y.setMax(1.0211695057944188d);
        	z.setMin(0.32197022782102236d);
        	z.setMax(0.6147389711519803d);
        	solver.addConstraint(new PowerConstraint(xexpr, yexpr, zexpr));
        	assertEquals("x min", 0.015078785426286425d, x.getMin(), 0.0001);
        	assertEquals("x max", 0.6209710072979173d, x.getMax(), 0.0001);
            
            solver.clear();
            solver.addVariable(y);
            solver.addVariable(z);
            y.setMin(0.10065938250118567d);
            y.setMax(0.6362256016118161d);
            z.setMin(0.16973525170438397d);
            z.setMax(0.7994846973470346d);
            solver.addConstraint(new PowerConstraint(xexpr, yexpr, zexpr));
            assertEquals("x min", 2.2293347242498136E-8d, x.getMin(), 0.0001);
//            assertEquals("x max", 0.7034609043574714d, x.getMax(), 0.0001);
            
            solver.clear();
            solver.addVariable(y);
            solver.addVariable(z);
            y.setMin(0.5161014780138984d);
            y.setMax(0.9351836140470431d);
            z.setMin(0.47299995521331206d);
            z.setMax(1.3387842466135673d);
            solver.addConstraint(new PowerConstraint(xexpr, yexpr, zexpr));
            assertEquals("x min", 0.23442814315187835d, x.getMin(), 0.0001);
            assertEquals("x max", 1.760008972544156d, x.getMax(), 0.0001);
            
            // test with random y and z
            Random rand = new Random();
            for (int j=1; j<=5; j++) {
                for (int k=0; k<100; k++) {
                    solver.clear();
                    solver.addVariable(y);
                    solver.addVariable(z);
                    
                    // initialize y
                    double n1 = j * rand.nextDouble();
                    double n2 = j * rand.nextDouble();
                    double minY = Math.min(n1, n2);
                    y.setMin(minY);
                    
                    double maxY = Double.NEGATIVE_INFINITY;
                    while (maxY < minY) {
                        n1 = minY + rand.nextDouble();
                        n2 = minY + rand.nextDouble();
                        maxY = Math.max(n1, n2);
                    }
                    y.setMax(maxY);
                    
                    // initialize z
                    n1 = j * rand.nextDouble();
                    n2 = j * rand.nextDouble();
                    double minZ = Math.min(n1, n2);
                    z.setMin(minZ);
                    
                    double maxZ = Double.NEGATIVE_INFINITY;
                    while (maxZ < minZ) {
                        n1 = minZ + rand.nextDouble();
                        n2 = minZ + rand.nextDouble();
                        maxZ = Math.max(n1, n2);
                    }
                    z.setMax(maxZ);
                    
                    
                    // calculate x
                    double p1 = Math.pow(minZ, 1/minY);
                    double p2 = Math.pow(minZ, 1/maxY);
                    double p3 = Math.pow(maxZ, 1/minY);
                    double p4 = Math.pow(maxZ, 1/maxY);
                    double minX = min(p1, p2, p3, p4);
                    double maxX = max(p1, p2, p3, p4);
                    
                    solver.addConstraint(new PowerConstraint(xexpr, yexpr, zexpr));
                    
                    // check for min NaN
                    if (Double.isNaN(minX))
                        assertTrue("x min", DoubleUtil.isEqual(maxX, x.getMin(), DoubleUtil.DEFAULT_PRECISION));
                    else
                        assertTrue("x min", DoubleUtil.isEqual(minX, x.getMin(), DoubleUtil.DEFAULT_PRECISION));
                    
                    // check for max NaN
                    if (Double.isNaN(maxX))
                        assertTrue("x max", DoubleUtil.isEqual(minX, x.getMax(), DoubleUtil.DEFAULT_PRECISION));
                    else
                        assertTrue("x max", DoubleUtil.isEqual(maxX, x.getMax(), DoubleUtil.DEFAULT_PRECISION));
                }
            }
        }
        catch(PropagationFailureException propx) {
            propx.printStackTrace(System.out);
            fail();
        }
    }

    /**
     * Helper function to return minimum value
     */
    private static double min(double p1, double p2, double p3, double p4) {
        double min = Double.POSITIVE_INFINITY;
        
        if (!Double.isNaN(p1))
            min = p1;
        
        if (!Double.isNaN(p2))
            min = Math.min(min, p2);
        
        if (!Double.isNaN(p3))
            min = Math.min(min, p3);
        
        if (!Double.isNaN(p4))
            min = Math.min(min, p4);
        
        return min;
    }    

    /**
     * Helper function to return maximum value
     */
    private static double max(double p1, double p2, double p3, double p4) {
        double max = Double.NEGATIVE_INFINITY;
        
        if (!Double.isNaN(p1))
            max = p1;
        
        if (!Double.isNaN(p2))
            max = Math.max(max, p2);
        
        if (!Double.isNaN(p3))
            max = Math.max(max, p3);
        
        if (!Double.isNaN(p4))
            max = Math.max(max, p4);
        
        return max;
    }    
}