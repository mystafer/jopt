/*
 * NameUtil.java
 * 
 * Created on Jun 21, 2005
 */
package jopt.csp.spi.util;

/**
 * Utility for generating unique names
 */
public class NameUtil {
	private static long nextName = 0;

    
    /**
     * Returns next name unique name from this utility
     */
    public static String nextName() {
        StringBuffer buf = new StringBuffer("_");
        buf.append(nextNum());
        return buf.toString();
    }
    
    /**
     * Returns next name number to use when generating unique names
     */
    public static long nextNum() {
        long n = nextName++;
        nextName %= Long.MAX_VALUE;
        return n;
    }
}
