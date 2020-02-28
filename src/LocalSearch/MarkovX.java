package LocalSearch;

import jdg.graph.DirectedGraph;
import jdg.graph.Edge;
import jdg.graph.GridSegment_2;
import jdg.graph.Node;

import java.util.Arrays;
import java.util.Random;

public class MarkovX
{
    private DirectedGraph graph = null;
    private Node node = null;
    private boolean[] isAvailable = null;
    private int numAvailable = 0;
    private Edge edge = null;

    private int min=0;
    private int max;
    private int step=1;

    public int n = 0;
    private double T_n=10000;//The initial value is not important.
    private double coeff_T=1;

    //private int V_n;
    //private boolean is_V_n_fresh = false;

    private Random rand;


    /////////////////////////////////////
    /////////////////////////////////////

    /**
     * Constructor
     * @param graph, the graph in which we work.
     * @param node , the target vertex.
     * @param edge, the target edge. We have node one of the endpoints of edge.
     * @param min , the minimal value that node.x can attain.
     * @param max , the maximal value that node.x can attain.
     * @param coeff_T , a constant, used to calculate the temperature.
     * @param step , a constant, used to define the set of available x values.
     * */
    public MarkovX(DirectedGraph graph, Node node, Edge edge, int min, int max, double coeff_T, int step)
    {
        this.graph = graph;
        this.edge = edge;
        this.node = node;

        this.max = max;
        this.min = min;
        this.step = step;

        this.coeff_T = coeff_T;

        // Initialize the isAvailable list.
        int length = max-min + 1;
        this.isAvailable = new boolean[length];
        Arrays.fill(this.isAvailable, true);

        int level = node.level;
        for(Node level_n_node : graph.level_list.get(level))
        {
            this.isAvailable[level_n_node.p.x] = false;
        }

        this.numAvailable = length - graph.level_list.get(level).size();
        this.numAvailable = (int)(this.numAvailable/this.step);

        this.rand = new Random();
    }


    public double getT_n() {return this.T_n;}

   /* public double Q(int x, int y)
    {
        if(x == min)
        {
            if(y==(x+step) || y==min)
                return 0.5;
            else
                return 0;
        }

        if(x == max)
        {
            if(y==(x-step) || y==max)
                return 0.5;
            else
                return 0;
        }

        if(x < min+step)
        {
            if(y == min || y == x+step)
                return 0.5;
            else
                return 0;
        }

        if(x > max-step)
        {
            if(y == max || y == x-step)
                return 0.5;
            else
                return 0;
        }


        if(y==(x-step) || y==(x+step))
            return 0.5;
        else
            return 0;
    }
*/

   /**
    * V is the target function that we want to minimize.
    */
    public int V(int y)
    {
        int tmp = this.node.p.x;
        this.node.p.x = y;
        //int v = this.graph.numGraphIntersected();
        int v = 0;
        //v = this.graph.getNodeIntersection(node);
        v += this.graph.getNodeIntersection(this.node);
        //v += this.graph.getNodeIntersection(this.edge.node_q);
        //v -= this.graph.getEdgeIntersection(this.edge);

        this.node.p.x = tmp;
        return v;
    }

    /**
     * Default V
     * */
    public int V()
    {
        return V(this.node.p.x);
    }


    /**
     * R is the rejection value function.
     **/
    public double R(int x, int y)
    {
        if(!(min<=y && y<=max))
            return 0;

        double r = 1;
        r *= Math.exp((V()-V(y))/this.T_n);

        return r/(1+r);
    }


    /**
     * We have the state X_n, and use this function
     * to calculate X_n+1.
     * In this function, X can move only to the left
     * or right $step many grid.
     * */
    public int next()
    {
        this.n++;
        if(this.n <= 2)
            this.T_n = this.coeff_T*V();
        else
            this.T_n = this.coeff_T*V() / (Math.log(this.n));

        int x = node.p.x;
        int y = node.p.x;
        if(node.p.x==this.max)
            y-=step;
        else if(node.p.x==this.min)
            y+=step;
        else
        {
            if(this.rand.nextBoolean())
            {
                y-=step;
                if(y<min) y=min;
            }
            else
            {
                y+=step;
                if(y>max) y=max;
            }
        }


        double u = this.rand.nextDouble();
        if(u < this.R(x,y))
        {
            this.node.p.x = y;
            //this.is_V_n_fresh = false;
        }

        return this.node.p.x;
    }

    /*
    * A faster markov process, in which $x can go to any $y, but not just move by $step.
    * */
    public int nextFast()
    {
        //Update $n and $this.T_n.
        this.n++;
        if (this.n <= 2)
            this.T_n = this.coeff_T * V();
        else
            this.T_n = this.coeff_T * V() / (Math.log(this.n));

        int x = this.node.p.x;

        //Pick the random value.
        int r;

        while(true)
        {
            r = this.rand.nextInt(this.numAvailable)*this.step;
            if (this.isAvailable[r])
                break;
        }
        int y = r + this.min;


        double u = this.rand.nextDouble();
        // if u < R(x,y), we let X_n+1 = Y_n+1.
        // else, we let X_n+1 = X_n.
        if (u < this.R(x, y))
        {
            this.node.p.x = y;
            // Update the isAvailable list.
            this.isAvailable[x] = true;
            this.isAvailable[y] = false;
        }

        return this.node.p.x;
    }
}
