package Visual;

import jdg.graph.DirectedGraph;
import jdg.graph.GridSegment_2;
import jdg.graph.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Main class providing tools for computing an upward grid drawing of a directed graph
 * that minimizes the number of crossings
 * 
 * @author Luca Castelli Aleardi (2019)
 *
 */
public class UpwardDrawingCor {
    /** input graph to draw */
    public DirectedGraph g=null;
    /** height of the grid (input parameter) */
    public int height;
    /** width of the grid (input parameter) */
    public int width;

    /**
     * initialize the input of the program
     */
    public UpwardDrawingCor(DirectedGraph g, int width, int height) {
    	this.g=g;
    	this.width=width;
    	this.height=height;
    }
    
    /**
     * Return the number of edge crossings
     */
    public int computeCrossings() {
    	// COMPLETE THIS METHOD
    	System.out.println("");
    	
    	return -1;
    }

    /**
     * Check whether the graph embedding is a valid upward drawing <br>
     * -) the drawing must be upward <br>
     * -) the integer coordinates of nodes must lie in the prescribed bounds: the drawing area is <em>[0..width] x [0..height]</em> <br>
     * -) non adjacent crossing edges must intersect at their interiors
     */
    public boolean checkValidity()
    {

    	throw new Error("To be completed");
    }

    /**
     * Compute a valid initial layout, satisfying the prescribed requirements according to the problem definition <br>
     * 
     * Remark: the vertex coordinates are stored in the class 'Node' (Point_2 'p' attribute)
     */
    public void computeValidInitialLayout() {
    	throw new Error("To be completed");
    }

    /**
     * Improve the current layout by performing a local search heuristic: nodes are moved
     * to a new location in order to reduce the number of crossings. The layout must remain valid at the end.
     */
    public void localSearchHeuristic() {
    	// YOU ARE FREE to choose to implement this function
    }

    /**
     * Improve the current layout by performing a local search heuristic: nodes are moved
     * to a new location in order to reduce the number of crossings. The layout must remain valid at the end.
     */
    public void forceDirectedHeuristic() {
    	// YOU ARE FREE to choose to implement this function
    }

    /**
     * Main function that computes a valid upward drawing that minimizes edge crossings. <br>
     * 
     * You are free to use and combine any algorithm/heuristic to obtain a good upward drawing.
     */
    public void computeUpwardDrawing() {
    	System.out.print("Compute a valid drawing with few crossings: ");
    	long startTime=System.nanoTime(), endTime; // for evaluating time performances
    	
    	// COMPLETE THIS METHOD
    	System.out.println("TO BE COMPLETED");
    	
    	endTime=System.nanoTime();
        double duration=(double)(endTime-startTime)/1000000000.;
    	System.out.println("Elapsed time: "+duration+" seconds");
    }

    /**
     * Check whether the current graph is provided with an embedding <br>
     * -) if all nodes are set to (0, 0): the graph has no embedding by definition <br>
     * -) otherwise, the graph has an embedding
     */
    public static boolean hasInitialLayout(DirectedGraph graph) {
    	for(Node v: graph.vertices) {
    		if(v.getPoint().getX()!=0 || v.getPoint().getY()!=0)
    			return true;
    	}
    	return false;
    }

}
