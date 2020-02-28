package LocalSearch;

import jdg.graph.DirectedGraph;
import jdg.graph.Node;

import java.util.ArrayList;
import java.util.Arrays;

public class LocalHeuristicByNode
{
    public DirectedGraph graph;
    private int width;


    /*
    * Constructor
    * */
    public LocalHeuristicByNode(DirectedGraph graph, int width)
    {
        this.graph = graph;
        this.width = width;
    }


    /**
     * Function used to move the  target node, to have the minimal number of crossings.
     * For each target vertex, there are at most 1000 available places.
     * @param node , the target vertex
     * */
    public void processNode(Node node)
    {
        // Find all available places for node.
        int level = node.level;
        int step = 1;
        if(this.width > 1000)
            step = (int)(this.width/1000);

        boolean[] isAvailable = new boolean[this.width+1];
        Arrays.fill(isAvailable, true);
        // If one grid is occupied, it is not available.
        for(Node level_n_node : this.graph.level_list.get(level))
        {
            isAvailable[level_n_node.p.x] = false;
        }


        // traverse the available places.
        int tmp = 0;
        int num_intersect = this.graph.getNodeIntersection(node);
        for(int i = 0; i < this.width; i+=step)
        {
            // If the position is not available, we skip it.
            if(!isAvailable[i])
                continue;
            // If not, we calculate the new number of intersection.
            tmp = node.p.x;
            node.p.x = i;
            int new_num_intersect = this.graph.getNodeIntersection(node);

            // If the new position causes less intersections.
            if(new_num_intersect < num_intersect)
            {
                num_intersect = new_num_intersect;
            }
            else
            {
                node.p.x = tmp;
            }
        }
    }



    /**
     * Function used to process the local heuristic traversal method.
     * @param tol , the tolerance. When we have $num_crossings_changed < tol, we terminate the process.
     * */
    public void localHeuristic(int tol)
    {
        // Pre-calculate the number of intersections.
        int num_intersect_graph = this.graph.numGraphIntersected();
        int new_num_intersect = num_intersect_graph;
        do
        {
            num_intersect_graph = new_num_intersect;

            Node node = this.graph.maxIntersectionNode();
            this.processNode(node);
            new_num_intersect = this.graph.numGraphIntersected();

            //System.out.println(num_intersect_graph - new_num_intersect);
        }
        while (num_intersect_graph-new_num_intersect > tol); // To avoid an endless loop.

    }
}
