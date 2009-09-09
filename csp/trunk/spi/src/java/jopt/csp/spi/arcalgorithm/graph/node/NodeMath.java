package jopt.csp.spi.arcalgorithm.graph.node;

import jopt.csp.spi.util.NumConstants;


public class NodeMath implements NumConstants {
    /**
     * Returns the node type for a given node
     */
    public static int nodeType(NumNode node) {
        if (node instanceof IntNode) return INTEGER;
        if (node instanceof LongNode) return LONG;
        if (node instanceof FloatNode) return FLOAT;
        return DOUBLE;
    }

    /**
     * Returns the node type for a given node
     */
    public static int nodeType(GenericNumNode node) {
        if (node instanceof GenericIntNode) return INTEGER;
        if (node instanceof GenericLongNode) return LONG;
        if (node instanceof GenericFloatNode) return FLOAT;
        return DOUBLE;
    }
}