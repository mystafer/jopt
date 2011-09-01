package jopt.csp.test.arcalgorithm.util;

import java.util.ArrayList;
import java.util.List;

import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.util.ArcQueue;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

public class ArcQueueTest extends TestCase {
    private Arc arc0a;
    private Arc arc0b;
    private Arc arc0c;
    private Arc arc1a;
    private Arc arc1b;
    private Arc arc2a;
    private Arc arc2b;
    private Arc arc3a;
    private Arc arc4a;
    private Arc arc5a;
    private Arc arc6a;
    private Arc arc7a;
    private Arc arc8a;

    protected void setUp() throws Exception {
        arc0a = new DummyArc(0);
        arc0b = new DummyArc(0);
        arc0c = new DummyArc(0);
        arc1a = new DummyArc(1);
        arc1b = new DummyArc(1);
        arc2a = new DummyArc(2);
        arc2b = new DummyArc(2);
        arc3a = new DummyArc(3);
        arc4a = new DummyArc(4);
        arc5a = new DummyArc(5);
        arc6a = new DummyArc(6);
        arc7a = new DummyArc(7);
        arc8a = new DummyArc(8);
    }

    protected void tearDown() throws Exception {
        arc0a = null;
        arc0b = null;
        arc0c = null;
        arc1a = null;
        arc1b = null;
        arc2a = null;
        arc2b = null;
        arc3a = null;
        arc4a = null;
        arc5a = null;
        arc6a = null;
        arc7a = null;
        arc8a = null;
    }
    
    public void testOrdering() {
        ArcQueue q = new ArcQueue();
        q.add(arc2a);
        q.add(arc1b);
        q.add(arc7a);
        q.add(arc0c);
        q.add(arc1a);
        q.add(arc2b);
        q.add(arc6a);
        q.add(arc8a);
        q.add(arc0a);
        assertTrue(q.hasNext());
        assertEquals(q.next(),arc0c);
        assertEquals(q.next(),arc0a);
        assertEquals(q.next(),arc1b);
        assertEquals(q.next(),arc1a);
        assertEquals(q.next(),arc2a);
        assertEquals(q.next(),arc2b);
        assertEquals(q.next(),arc6a);
        assertEquals(q.next(),arc7a);
        assertTrue(q.hasNext());
        assertEquals(q.next(),arc8a);
        assertFalse(q.hasNext());
    }
    
    public void testRemovalOfDuplicates() {
        ArcQueue q = new ArcQueue();
        q.add(arc2a);
        q.add(arc1b);
        q.add(arc7a);
        q.add(arc1a);
        q.add(arc2b);
        q.add(arc2a);
        q.add(arc7a);
        q.add(arc5a);
        assertEquals(q.next(),arc1b);
        assertEquals(q.next(),arc1a);
        assertEquals(q.next(),arc2a);
        assertEquals(q.next(),arc2b);
        assertEquals(q.next(),arc5a);
        assertEquals(q.next(),arc7a);
        q.add(arc2a);
        q.add(arc3a);
        q.add(arc7a);
        assertEquals(q.next(),arc2a);
        q.add(arc0a);
        q.add(arc1b);
        q.add(arc7a);
        assertEquals(q.next(),arc0a);
        assertEquals(q.next(),arc1b);
        assertEquals(q.size(),2);
        assertEquals(q.next(),arc3a);
        assertEquals(q.next(),arc7a);
        assertEquals(q.size(),0);
        assertFalse(q.hasNext());
    }
    
    public void testMinRequiredComplexity() {
        ArcQueue q = new ArcQueue();
        q.setRequiredMinComplexity(1);
        assertEquals(q.getRequiredMinComplexity(),1);
        assertEquals(q.getMinComplexity(),-1);
        q.add(arc2a);
        q.add(arc7a);
        assertEquals(q.getMinComplexity(),2);
        q.add(arc1b);
        q.add(arc0a);
        assertEquals(q.size(),3);
        q.add(arc1b);
        q.add(arc7a);
        q.add(arc1b);
        assertTrue(q.hasNext());
        assertEquals(q.next(),arc1b);
        assertEquals(q.next(),arc2a);
        assertEquals(q.next(),arc7a);
        assertFalse(q.hasNext());
    }
    
    public void testRemove() {
        ArcQueue q = new ArcQueue();
        q.add(arc2a);
        q.add(arc1b);
        q.add(arc7a);
        q.add(arc0a);
        q.add(arc1b);
        assertTrue(q.remove(arc2a));
        q.add(arc4a);
        q.add(arc1b);
        assertTrue(q.hasNext());
        assertEquals(q.next(),arc0a);
        assertEquals(q.next(),arc1b);
        assertEquals(q.next(),arc4a);
        assertEquals(q.next(),arc7a);
        assertFalse(q.hasNext());
    }
    
    public void testAddAll() {
        List<Arc> l = new ArrayList<Arc>();
        l.add(arc2a);
        l.add(arc1b);
        l.add(arc4a);
        l.add(arc2b);
        l.add(arc0a);
        l.add(arc0b);
        ArcQueue q = new ArcQueue();
        q.addAll(l);
        assertEquals(q.next(),arc0a);
        assertEquals(q.next(),arc0b);
        assertEquals(q.next(),arc1b);
        assertEquals(q.next(),arc2a);
        assertEquals(q.next(),arc2b);
        assertEquals(q.next(),arc4a);
    }
    
    public void testClear() {
        ArcQueue q = new ArcQueue();
        q.add(arc2a);
        q.add(arc1b);
        q.add(arc7a);
        q.add(arc0a);
        q.add(arc1b);
        assertTrue(q.remove(arc2a));
        q.add(arc4a);
        q.clear();
        assertEquals(q.size(),0);
        assertFalse(q.hasNext());
        assertFalse(q.remove(arc2a));
        q.add(arc2a);
        q.add(arc1b);
        q.add(arc7a);
        q.add(arc0a);
        q.add(arc1b);
        assertTrue(q.remove(arc2a));
        q.add(arc4a);
        q.add(arc1b);
        assertTrue(q.hasNext());
        assertEquals(q.next(),arc0a);
        assertEquals(q.next(),arc1b);
        assertEquals(q.next(),arc4a);
        assertEquals(q.next(),arc7a);
        assertFalse(q.hasNext());
    }
    
    /**
     * Trivial arc for testing the complexity-handling of the
     * arc queue.
     */
    private class DummyArc implements Arc {
        private int complexity;
        
        public DummyArc(int complexity) {
            this.complexity = complexity;
        }

        public int getArcType() {
            return 0;
        }

        public int getComplexity() {
            return this.complexity;
        }

        public void propagate() throws PropagationFailureException {
        }

        public void propagate(Node src) throws PropagationFailureException {
        }

        public void setAlgorithmStrength(int strength) {
        }

        public void setUseDomainDeltas(boolean useDeltas) {
        }
    }

}
