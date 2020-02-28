package LocalSearch;

import jdg.graph.DirectedGraph;
import jdg.graph.Edge;
import jdg.graph.GridSegment_2;
import jdg.graph.Node;

import java.util.Random;

public class LocalSearchHeuristic
{
    public DirectedGraph graph;
    private int width;
    public double coeff_T = 2;

    /*
    * The Constructor
    * */
    public LocalSearchHeuristic(DirectedGraph graph, int width)
    {
        this.graph = graph;
        this.width = width;
    }


    /**
    * Function : processEdge
    *   Used to move the end points of one edge,
    *   to reduce the intersections.
     * @param edge, the target edge that we work on.
     * @param tol, the minimal value of T. If T < tol, we terminate the process.
     * @param max_iter, the maximal time of iteration.
    **/
    public void processEdge(Edge edge, double tol, int max_iter)
    {
        Random rand = new Random();

        // Find the two end points.
        Node node_p = edge.node_p;
        Node node_q = edge.node_q;

        int step;
        if(this.width <= 1000) step = 1;
        else step = (int)(this.width/1000);

        MarkovX X_p = new MarkovX(graph, node_p, edge, 0, width, coeff_T, step);
        MarkovX X_q = new MarkovX(graph, node_q, edge, 0, width, coeff_T, step);

        int V_init_p = X_p.V();
        int V_init_q = X_q.V();
        // We make the two end-points convergent.
        int num_iter = 0;
        while(X_p.getT_n()>tol || X_q.getT_n()>tol)
        {
            // In each loop, we randomly choose one endpoint to move.
            if(rand.nextBoolean())
                X_p.nextFast();
            else
                X_q.nextFast();

            ++num_iter;
            if(num_iter > max_iter)
                break;

        }

        //System.out.println("We have looped " + num_iter + " times.");
        //int V_end_p = X_p.V();
        //int V_end_q = X_q.V();
        //System.out.println("the V init are " + V_init_p + " and " + V_init_q +".");
        //System.out.println("the V ends are " + V_end_p + " and " + V_end_q +".");
        //System.out.println("T_end : " + X_p.getT_n() + ", " + X_q.getT_n());

    }


    /**
    * Function : process
    *   Used to reduce the number of intersections by local heuristic.
     * @param tol , the tol param used in function processEdge.
     * @param num_edge_process, the number of outer iterations that we will take.
     * @param max_iter , the param used in function processEdge.
    * */
    public void process(double tol, int num_edge_process, int max_iter)
    {

        for(int i = 0; i < num_edge_process; ++ i)
        {
            Edge edge_max = this.graph.maxIntersectionEdge();
            if(edge_max.num_intersection <= 1)
                break;

            //System.out.println("The edge moved : " + edge_max);

            processEdge(edge_max, tol, max_iter);

            int num_intersect = this.graph.numGraphIntersected();

            //System.out.println("Final position : " + edge_max);
            //System.out.println("Num intersection : " + num_intersect);
            //System.out.println();

        }
    }


}
