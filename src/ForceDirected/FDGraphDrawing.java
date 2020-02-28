package ForceDirected;


import jdg.graph.DirectedGraph;
import jdg.graph.GridVector_2;
import jdg.graph.Node;

import java.util.ArrayList;

/**
 * The class that used to implement the Force directed drawing algorithm.
 * */
public class FDGraphDrawing
{
    public DirectedGraph graph;
    private int height = 0;
    private int width = 0;
    private double C = 1;
    private double K = 1;
    private double coeff_step = 0.9;
    private int energy = 0;
    private double step = 0;
    private int process = 0;
    //private int energy_change = 0;
    //private ArrayList<GridVector_2> force_list = null;

    public FDGraphDrawing()
    {
        graph = new DirectedGraph();
    }

    public FDGraphDrawing(DirectedGraph graph, int height, int width, double C, double K)
    {
        //this.graph = new DirectedGraph(graph);
        this.graph = graph;
        this.height = height;
        this.width = width;
        this.C = C;
        this.K = K;

        this.step = (double)this.width/(this.graph.numLevel()*2);
    }

    /**
    * Function used to calculate the attractive force from node x to node y.
    * @param x, the vertex that provides the force.
    * @param y, the vertex that receives the force.
    * @return GridVector_2, the attractive force from x on y.
    * */
    private GridVector_2 forceA(Node x, Node y)
    {
        GridVector_2 diff = new GridVector_2(x.p,y.p);
        double param = diff.norm()/K;
        return diff.multiplyByScalar(param);
    }


    /**
     * Function used to calculate the repulsive force from node x to node y.
     * @param x, the vertex that provides the force.
     * @param y, the vertex that receives the force.
     * @return GridVector_2, the repulsive force from x on y.
     * */
    private GridVector_2 forceR(Node x, Node y)
    {
        if (x.p.equalsTo(y.p))
            return new GridVector_2(0,0);

        GridVector_2 diff = new GridVector_2(x.p,y.p);
        double param = -C*K*K/Math.pow(diff.norm(), 2);

        return diff.multiplyByScalar(param);
    }


    private GridVector_2 forceN(Node x)
    {
        GridVector_2 force = new GridVector_2(0,0);
        for (Node pred : x.predecessors)
        {
            for (Node ngh : pred.successors)
            {
                if (x.p.equalsTo(ngh.p)) continue;
                force.add(forceR(ngh,x));
            }
        }

        for (Node succ : x.successors)
        {
            for (Node ngh : succ.predecessors)
            {
                if (x.p.equalsTo(ngh.p)) continue;
                force.add(forceR(ngh,x));
            }
        }

        return force.multiplyByScalar(1);
    }


    /**
     * Function used to calculate the sum force acting on the node give.
     * @param  node, the target in which we calculate the force.
     * @return GridVector_2, the total force on the target.
     * */
    private GridVector_2 forceTotal(Node node)
    {
        GridVector_2 force = new GridVector_2(0,0);
        for (Node x : node.successors)
        {
            force.add(this.forceA(node, x));
        }
        for (Node x: node.predecessors)
        {
            force.add(this.forceA(node, x));
        }

        for (Node x : graph.vertices)
        {
            force.add(this.forceR(node, x));
        }

        //force.add(forceN(node));

        return force;
    }


    /**
    * Function used to calculate the new step length,
    * based on the value $this.process, and $energe_new.
    * @param energy_new, type int, the new energy.
    * */
    private void updateStepLength(int energy_new)
    {
        if(energy_new < this.energy)
        {
            this.process += 1;
            if(this.process >= 5)
            {
                this.process = 0;
                this.step = this.step / this.coeff_step;
            }
        }
        else
        {
            this.process = 0;
            this.step *= this.coeff_step;
        }
    }



    /**
    * Function used to calculate the forces, the energy, and to execute the moves.
    * */
    public void step()
    {
        // Calculate the forces, and the energy, and move the nodes.
        int energy_new = 0;
        //this.force_list = new ArrayList<>();
        for(Node node:graph.vertices)
        {
            GridVector_2 force = forceTotal(node);
            energy_new += Math.pow(force.norm(), 2);
            //this.force_list.add(force);

            /*
            node.p = node.p.sum(force.multiplyByScalar(this.step/force.norm()));
            if(node.p.x > this.width) node.p.x = this.width;
            else if (node.p.x < 0) node.p.x = 0;
            if(node.p.y > this.height) node.p.y = this.height;
            else if (node.p.y < 0) node.p.y = 0;
             */

            // Only move horizontally.
            if(force.x > 0)
            {
                node.p.x += (int)this.step;
                //if(node.p.x >= this.width) node.p.x = this.width;
            }
            else if (force.x < 0)
            {
                node.p.x -= (int)this.step;
                //if(node.p.x <= 0) node.p.x = 0;
            }
        }

        // Calculate the step length;
        this.updateStepLength(energy_new);
        // Update the energy.
        this.energy = energy_new;

    }


    /**
     * Function used to process the force directed process
     * @param max_step_num, the maximal time of iterating.
     * @param initial_step_length, the initial length for the process.
     * @param tol,  if the step_length is below tol, we terminate the process.
     * */
    public void forceDirectedProcess(int max_step_num, double initial_step_length, double tol) {
        this.step = initial_step_length;
        for (int i = 0; i < max_step_num; ++i) {

            if (this.step < tol)
                break;

            this.step();
        }

        validalizeGraph();

        int right_most = this.width;
        int left_most = 0;
        for (Node node : this.graph.vertices)
        {
            if(node.p.x < right_most) right_most = node.p.x;
            if(node.p.x > left_most) left_most = node.p.x;
        }

        double coeff = (double)this.width/(left_most-right_most);
        for (Node node : this.graph.vertices)
        {
            node.p.x = (int)((node.p.x - right_most)*coeff);
        }

    }


    /*
    * Function used to make the newly calculated graph valid.
    * */
    public void validalizeGraph()
    {
        ArrayList<Node> invalid_list = this.graph.getInvalidVertex();

        while (invalid_list.size()!=0)
        {
            for (Node node: invalid_list)
            {
                GridVector_2 force = forceTotal(node);
                if (force.x >= 0) node.p.x++;
                else node.p.x--;
            }
            invalid_list = this.graph.getInvalidVertex();
        }

    }
}
