package Visual;

import ForceDirected.FDGraphDrawing;
import LocalSearch.LocalHeuristicByNode;
import LocalSearch.LocalSearchHeuristic;
import jdg.graph.DirectedGraph;
import jdg.graph.GridPoint_2;
import jdg.graph.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Main class providing tools for computing an upward grid drawing of a directed graph
 * that minimizes the number of crossings
 * 
 * @author Luca Castelli Aleardi (2019)
 *
 */
public class UpwardDrawing {
    /** input graph to draw */
    public DirectedGraph g=null;
    /** height of the grid (input parameter) */
    public int height;
    /** width of the grid (input parameter) */
    public int width;
    
    /**
     * initialize the input of the program
     */
    public UpwardDrawing(DirectedGraph g, int width, int height) {
    	this.g=g;
    	this.width=width;
    	this.height=height;
    }
    
    /**
     * Return the number of edge crossings
     */
    public int computeCrossings()
    {
    	// COMPLETE THIS METHOD
        return g.numGraphIntersected();
    }

    /**
     * Check whether the graph embedding is a valid upward drawing <br>
     * -) the drawing must be upward <br>
     * -) the integer coordinates of nodes must lie in the prescribed bounds: the drawing area is <em>[0..width] x [0..height]</em> <br>
     * -) non adjacent crossing edges must intersect at their interiors
     */
    public boolean checkValidity()
    {
        // Here we check whether the coords are in the given range.
        for (Node node : g.vertices)
        {
            if(node.p.x<0 || node.p.y<0 || node.p.x>this.width || node.p.y>this.height)
                    return false;
        }

        // We check the property of upward,
        // and intersection at interior in the class DirectedGraph.
        return g.isValid();
    }

    /**
     * Compute a valid initial layout, satisfying the prescribed requirements according to the problem definition <br>
     * 
     * Remark: the vertex coordinates are stored in the class 'Node' (Point_2 'p' attribute)
     */
    public void computeValidInitialLayout()
    {
        g.nodeClassifier();
        g.numGraphIntersected();
        while(!this.checkValidity())
        {
            g.allocateRandom(this.width, this.height);
        }

    }

    /**
     * Improve the current layout by performing a local search heuristic: nodes are moved
     * to a new location in order to reduce the number of crossings. The layout must remain valid at the end.
     */
    public void localSearchHeuristic()
    {
        // Memorize the origin state.
        int initial_num_intersect = g.numGraphIntersected();
        GridPoint_2[] origin_position_list = new GridPoint_2[g.vertices.size()];
        for(Node node : g.vertices)
        {
            origin_position_list[node.index] = new GridPoint_2(node.p);
        }

        LocalSearchHeuristic local = new LocalSearchHeuristic(this.g, this.width);
        local.process(6, this.g.edges.size(), 1000);

        if(local.graph.numGraphIntersected() > initial_num_intersect)
        {
            for(Node node : g.vertices)
            {
                node.p.x = origin_position_list[node.index].x;
                node.p.y = origin_position_list[node.index].y;
            }
        }
    }


    /**
     * Improve the current layout by performing a local search heuristic: nodes are moved
     * to a new location in order to reduce the number of crossings. The layout must remain valid at the end.
     * In this method, we traverse all the layer where the target vertex is to find a minimal value.
     */
    public void localSearchHeuristicNode(int tol)
    {
        LocalHeuristicByNode local = new LocalHeuristicByNode(this.g, this.width);
        local.localHeuristic(tol);
    }

    /**
     * Improve the current layout by performing a local search heuristic: nodes are moved
     * to a new location in order to reduce the number of crossings. The layout must remain valid at the end.
     */
    public void forceDirectedHeuristic()
    {
        // Memorize the origin state.
        int initial_num_intersect = g.numGraphIntersected();
        GridPoint_2[] origin_position_list = new GridPoint_2[g.vertices.size()];
        for(Node node : g.vertices)
        {
            origin_position_list[node.index] = new GridPoint_2(node.p);
        }



        FDGraphDrawing fdgraph = new FDGraphDrawing(this.g, this.height, this.width,1, 10);

        double initial_step =  (double)this.width/this.g.max_level_size();
        initial_step /= 2;

        fdgraph.forceDirectedProcess(1000, initial_step, 0.0001);


        if(fdgraph.graph.numGraphIntersected() > initial_num_intersect)
        {
            for(Node node : g.vertices)
            {
                node.p.x = origin_position_list[node.index].x;
                node.p.y = origin_position_list[node.index].y;
            }

        }


    }

    /**
     * Main function that computes a valid upward drawing that minimizes edge crossings. <br>
     * 
     * You are free to use and combine any algorithm/heuristic to obtain a good upward drawing.
     */
    public void computeUpwardDrawing()
    {
    	System.out.print("Compute a valid drawing with few crossings: ");
    	long startTime=System.nanoTime(), endTime; // for evaluating time performances
    	
    	// COMPLETE THIS METHOD
        this.localSearchHeuristicNode(3);
        this.forceDirectedHeuristic();
        this.localSearchHeuristicNode(1);
        this.localSearchHeuristic();
        this.localSearchHeuristicNode(1);

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
