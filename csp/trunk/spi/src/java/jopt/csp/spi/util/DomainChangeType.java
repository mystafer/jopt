package jopt.csp.spi.util;

/**
 * Class enumerating the three types of domain changes.  These are used
 * for indicating the types of node change events and domain change events.
 * Also used to to indicate the type of source dependency for arcs.
 * 
 * These three values are arranged in increasing order of specificity.
 * In this way, each larger value indicates a situation that is a subset
 * of the previous.  A change in range (bounds) necessarily implies a
 * change in domain.  So also a variable that has been bound to a single
 * value has had a range (bounds) change and the domain has changed as well.
 * In some structures like the node queue, the highest applicable value is
 * stored because it necessarily implies those lower than it.
 * 
 * @author pvanwylen
 */
public interface DomainChangeType {
    /**
     * A change in domain resulting from internal values being removed from the
     * domain.  The maximum and minimum do not, however, change.
     */
    public final static int DOMAIN  = 0;
    /**
     * A change in domain occurring when the maximum and/or minimum value of
     * the domain changes (along with perhaps other internal values).  The 
     * maximum and minimum remain different, however, and the domain is not
     * bound to a single value.
     */
    public final static int RANGE   = 1;
    /**
     * A change in domain occurring when the domain is bound to a single value.
     * The maximum and minimum value of the domain are the same.
     */
    public final static int VALUE   = 2;
}
