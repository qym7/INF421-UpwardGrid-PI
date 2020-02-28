package jdg.graph;

/**
 * Class Edge,
 * Parent class: GridSegment_2
 * Used to store the information of a directed edge in the graph.
 * */
public class Edge extends GridSegment_2
{
    public Node node_p;
    public Node node_q;
    public int num_intersection = 0;

    /**
     * Constructor
     * @param node_p, the start point of this edge.
     * @param node_q, the end point of this edge.
     * */
    public Edge(Node node_p, Node node_q)
    {
        super(node_p.p, node_q.p);
        this.node_p = node_p;
        this.node_q = node_q;
    }

    public String toString()
    {
        return this.node_p.toString() + " and " + this.node_q.toString() + ", num of intersection : " + num_intersection;
    }

    /**
     * Check whether the given vertex is on this edge or not.
     * @param node, the target vertex.
     * @return if node is on this edge, return true. If not, return false.
     * */
    public boolean hasOn(Node node)
    {
        // If node is one of the endpoints of this edge.
        if(this.node_p.index == node.index || this.node_q.index == node.index)
            return false;

        else
            return super.hasOn(node.p);
    }

}
