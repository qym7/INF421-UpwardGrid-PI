package jdg.graph;

import java.util.*;

/**
 * Pointer based implementation of an Adjacency List Representation of a directed graph
 * 
 * @author Luca Castelli Aleardi (2019)
 *
 */
public class DirectedGraph {

	public ArrayList<Node> vertices; // list of vertices
	public HashMap<String, Node> labelMap; // map between vertices and their labels
	public ArrayList<Edge> edges=null;
	public ArrayList<List<Node>> level_list = null;


	public DirectedGraph() {
		this.vertices = new ArrayList<Node>();
		this.labelMap = null; // no labels defined
		this.edges = null;
	}


	public DirectedGraph(int mapCapacity) {
		this.vertices = new ArrayList<Node>();
		this.labelMap = new HashMap<String, Node>(mapCapacity); // labels are defined
		this.edges = null;
	}


	public DirectedGraph(DirectedGraph graph)
	{
		this.vertices = new ArrayList<>(graph.vertices);
		//this.labelMap = new HashMap<>(graph.labelMap);
		this.edges = new ArrayList<>(graph.edges);

		this.level_list = new ArrayList<>(graph.level_list);
	}

	public void addNode(Node v) {
		String label = v.label;
		if (label == null) {
			this.vertices.add(v);
			return;
		}

		if (this.labelMap.containsKey(label) == false) {
			this.labelMap.put(label, v);
			this.vertices.add(v);
		}
	}

	public Node getNode(String label) {
		if (this.labelMap != null && this.labelMap.containsKey(label) == true) {
			return this.labelMap.get(label);
		}
		return null;
	}

	public Node getNode(int index) {
		if (index >= 0 && index < this.vertices.size()) {
			return this.vertices.get(index);
		}
		return null;
	}


	public Node getNode(GridPoint_2 point)
	{
		for(Node node : this.vertices)
		{
			if(node.p.equalsTo(point))
				return node;
		}

		return null;
	}

	public void removeNode(Node v) {
		throw new Error("To be updated/implemented");
/*		if(this.vertices.contains(v)==false)
			return;
		for(Node u: this.getNeighbors(v)) { // remove all edges between v and its neighbors
			u.removeNeighbor(v);
		}
		this.vertices.remove(v); // remove the vertex from the graph*/
	}

	/**
	 * Add the (a, b) directed from 'a' to 'b'
	 */
	public void addDirectedEdge(Node a, Node b) {
		if (a == null || b == null)
			return;
		a.addSuccessor(b); // 'b' is a successor of 'b'
		b.addPredecessor(a);
	}

	/** Remove both edges edges (a, b) and (b, a)*/
    /*public void removeEdge(Node a, Node b){
    	if(a==null || b==null)
    		return;
    	a.removeNeighbor(b);
    	b.removeNeighbor(a);
    }*/

	/**
	 * Check whether one of the two edges (a, b) and (b, a) does exist
	 */
	public boolean adjacent(Node a, Node b) {
		if (a == null || b == null)
			throw new Error("Graph error: vertices not defined");
		if (a.isPredecessorsOf(b) == true)
			return true;
		if (a.isSuccessorOf(b) == true)
			return true;
		return false;
	}

	public int degree(Node v) {
		return v.degree();
	}

	public int numLevel(){ return  this.level_list.size(); }

	public int max_level_size()
	{
		int max_size = 0;
		for (List<Node> level : this.level_list)
			if(max_size < level.size())
				max_size = level.size();

		return max_size;
	}

	public Collection<Node> getSuccessors(Node v) {
		return v.successorsList();
	}

	public Collection<Node> getPredecessors(Node v) {
		return v.predecessorsList();
	}

	/**
	 * Return the number of nodes (it includes isolated nodes)
	 */
	public int sizeVertices() {
		return this.vertices.size();
	}

	/**
	 * Return the number of directed arcs
	 * <p>
	 * Remark: arcs are not counted twice
	 */
	public int sizeEdges() {
		int result1 = 0, result2 = 0;
		for (Node v : this.vertices) {
			result1 = result1 + getSuccessors(v).size();
			result2 = result2 + getPredecessors(v).size();
		}
		if (result1 != result2)
			throw new Error("Error: wrong number of edges");

		return result1;
	}

	/**
	 * Return the number of non isolated nodes
	 *
	 * @return the number of non isolated nodes
	 */
	public int countNonIsolatedVertices() {
		int N = 0; // count non isolated nodes
		for (Node u : this.vertices) {
			if (u.degree() >= 0)
				N++;
		}
		return N;
	}

	/**
	 * Return an array storing all vertex indices, according to the order of vertices
	 */
	public int[] getIndices() {
		int[] result = new int[this.vertices.size()];

		int count = 0;
		for (Node u : this.vertices) {
			if (u != null) {
				result[count] = u.index;
				count++;
			}
		}
		return result;
	}

	/**
	 * Return an array storing all vertex locations, according to the order of vertices
	 */
	public GridPoint_2[] getPositions() {
		GridPoint_2[] result = new GridPoint_2[this.vertices.size()];

		int count = 0;
		for (Node u : this.vertices) {
			if (u != null && u.getPoint() != null) {
				result[count] = u.getPoint();
				count++;
			}
		}
		return result;
	}



	/**
	 * List all edges
	 **/
	private void listEdges()
	{
		this.edges = new ArrayList<>();
		for (Node node : this.vertices)
		{
			for (Node succ : node.successors)
				this.edges.add(new Edge(node, succ));
		}
	}

	/**
	 * Compute the minimum vertex index of the graph (a non negative number)
	 * <p>
	 * Remark: vertices are allowed to have indices between 0..infty
	 * This is required when graphs are dynamic: vertices can be removed
	 */
	public int minVertexIndex() {
		int result = Integer.MAX_VALUE;
		for (Node v : this.vertices) {
			if (v != null)
				result = Math.min(result, v.index); // compute max degree
		}
		return result;
	}

	/**
	 * Compute the maximum vertex index of the graph (a non negative number)
	 * <p>
	 * Remark: vertices are allowed to have indices between 0..infty
	 * This is required when graphs are dynamic: vertices can be removed
	 */
	public int maxVertexIndex() {
		int result = 0;
		for (Node v : this.vertices) {
			if (v != null)
				result = Math.max(result, v.index); // compute max degree
		}
		return result;
	}

	/**
	 * Return a string containing informations and parameters of the graph
	 */
	public String info() {
		String result = sizeVertices() + " vertices, " + sizeEdges() + " edges\n";

		int isolatedVertices = 0;
		int maxDegree = 0;
		for (Node v : this.vertices) {
			if (v == null || v.degree() == 0)
				isolatedVertices++; // count isolated vertices
			//if(v!=null && v.p!=null && v.p.distanceFrom(new Point_3()).doubleValue()>0.) // check geometric coordinates
			//	geoCoordinates=true;
			if (v != null)
				maxDegree = Math.max(maxDegree, v.degree()); // compute max degree
		}
		result = result + "isolated vertices: " + isolatedVertices + "\n";
		result = result + "max vertex degree: " + maxDegree + "\n";

		result = result + "min and max vertex index: " + minVertexIndex();
		result = result + "..." + maxVertexIndex() + "\n";

		return result;
	}

	public void printCoordinates() {
		for (Node v : this.vertices) {
			if (v != null)
				System.out.println(v.index + " " + v.p);
		}
	}


	/**
	 * Check whether the layout is valid or not.
	 * */
	public boolean isValid()
	{
		// Check the coords:
		for (Node node : this.vertices)
		{
			// Check y-coord increasing.
			if (node.successors != null)
			{
				for (Node succ : node.successors)
				{
					if (succ.p.y <= node.p.y)
					{
						return false;
					}
				}
			}

			if (this.edges == null) this.listEdges();

			//Check if there exists an edge who passes a vertex.
			for (Edge edge : this.edges)
			{
				if (edge.hasOn(node))
				{
					System.out.println("The vertex " + node + " is in one edge.");
					return false;
				}
			}

		}

		return true;
//		return !isGraphOverlapped();
	}



	/**
	 * Determine whether two edges are intersected or not.
	 **/
	public boolean isIntersected(Edge edge1, Edge edge2)
	{

		if(edge1.node_p.p.y >= edge2.node_q.p.y || edge1.node_q.p.y <= edge2.node_p.p.y)
			return false;

		GridVector_2 vect1 = edge1.toVector();
		GridVector_2 vect2 = edge2.toVector();

		// If they are parallel.
		if(vect1.slope() == vect2.slope())
			return false;

		// If they have common end-point.
		if(edge1.source()==edge2.source() || edge1.target()==edge2.target() || edge1.source() ==edge2.target() || edge1.target() ==edge2.source())
			return false;

		GridVector_2 vect_u1 = new GridVector_2(edge1.source().x, edge1.source().y);
		GridVector_2 u2_1 = new GridVector_2(edge1.source(), edge2.source());
		GridVector_2 v2_1 = new GridVector_2(edge1.source(), edge2.target());
		double slope_1 = vect1.slope();
		if((u2_1.y*slope_1 - u2_1.x) * (v2_1.y*slope_1 - v2_1.x) > 0)
			return false;

		GridVector_2 vect_u2 = new GridVector_2(edge2.source().x, edge2.source().y);
		GridVector_2 u1_2 = new GridVector_2(edge2.source(), edge1.source());
		GridVector_2 v1_2 = new GridVector_2(edge2.source(), edge1.target());

		double slope_2 = vect2.slope();
		if((u1_2.y*slope_2 - u1_2.x) * (v1_2.y*slope_2 - v1_2.x) > 0)
			return false;

		return true;
	}


	/*******************
	 * Calculate the number of intersections in a graph.
	 *******************/
	public int numGraphIntersected()
	{
		int intersection = 0;

		//if the edges are not listed, we list them out.
		if(this.edges == null)
		{
			listEdges();
		}

		for(Edge edge : this.edges)
		{
			edge.num_intersection = 0;
			edge.node_q.num_intersection = 0;
			edge.node_p.num_intersection = 0;
		}
		//Clone the edges. So that we do not modify the original data.
		Edge[] edges_clone = this.edges.toArray(new Edge[0]);

		int length = this.edges.size();
		for (int i = 0; i < length; i++)
		{
			for (int j = i+1; j < length; j++)
			{
				if (isIntersected(edges_clone[i],edges_clone[j]))
				{
					edges_clone[i].num_intersection++;
					edges_clone[j].num_intersection++;
					edges_clone[i].node_p.num_intersection++;
					edges_clone[i].node_q.num_intersection++;
					edges_clone[j].node_p.num_intersection++;
					edges_clone[j].node_q.num_intersection++;
					intersection++;
					//System.out.println("Edges intersected are " + i + ", " + j + ".");
				}
			}
		}

		return intersection;
	}


	/*******************
	 * Classify the nodes by level and relocate the nodes to draw a valid graph.
	 *******************/
	public void nodeClassifier()
	{
		if(this.level_list != null)
			return;
		// boolean[] deja_vu = new boolean[this.nodes.length];
		int num_deja_vu = 0;

		//The list of lists, whose element is a list of nodes in level_n.
		this.level_list = new ArrayList<>();
		// for (int i = 0; i < this.nodes.length; i++) deja_vu[i] = false;
		List<Node> level_n_list = new ArrayList<>();

		for(Node node : this.vertices)
		{
			if (node.inDegree() == 0)
			{
				level_n_list.add(node);
				node.level = 0;
				// deja_vu[i] = true;
				++num_deja_vu;
			}
		}
		this.level_list.add(level_n_list);

		/*
		 * DEBUG*/
		//System.out.println(level_n_list.size());

		//distributeLevelRandom(level_n_list);
		/********************
		 * Not jet operated the 0m level
		 *******************/
		int n_level = 1;
		while (num_deja_vu < this.vertices.size())
		{
			List<Node> level_n_tmp = new ArrayList<>();
			// i is one of the vertexes in $V_n$.
			for(Node node : level_n_list)
			{
				// succ is one of the vertexes in Succ(node).
				for(Node succ : node.successors)
				{
					// If level(succ) >=0, this means that succ is already visited.
					if(succ.level >= 0)
					{
						continue;
					}

					boolean is_in_level_n = true; // Used to mark whether succ is in $V_n+1$ or not.
					// k is one of the vertexes in Pred(succ).
					for (Node k : succ.predecessors)
					{
						// If level(k) < 0, k is not yet seen. If level(k) >= n_level, k is not in $U_n$.
						if(k.level < 0 || k.level >= n_level)
						{
							is_in_level_n = false;
							break;
						}
					}

					if(is_in_level_n)
					{
						level_n_tmp.add(succ);
						succ.level = n_level;
						// deja_vu[j] = true;
						++num_deja_vu;
					}
				}
			}

			level_n_list = new ArrayList<>(level_n_tmp);
			this.level_list.add(level_n_list);
			/*
			 * DEBUG*/
			//System.out.println(level_n_list.size());

			//distributeLevelRandom(level_n_list);
			n_level++;

		}
	}


	private void distributeInLevelRandom(List<Node> level_n_list, int width)
	{
		if(level_n_list.size() == 0)
			return;

		Random rand = new Random();
		int length = level_n_list.size();
		int segment_width = (width+1)/length;

		int i = 0;
		for (Node node : level_n_list)
		{
			node.p.x = rand.nextInt(segment_width) + segment_width * i;
			i++;
		}
	}

	/******************
	 * Relocate the nodes so that the graph is valid
	 *****************/
	public void allocateRandom(int width, int height)
	{
		// if we have not given level.
        if(this.level_list == null)
        	nodeClassifier();

        int num_level = this.level_list.size();// the number of level
		int segment_height = height;
		if (num_level > 1) segment_height = height/(num_level-1);

        // For each level, we give allocate the nodes randomly.
        for(List<Node> level_n_list : this.level_list)
		{
			distributeInLevelRandom(level_n_list, width);

			for(Node node : level_n_list)
			    node.p.y = node.level*segment_height;

		}

	}


	/*******************************
	 * Function : getInvalideVertex
	 * 		Used to find all vertices which cause the
	 * 		graph layout invalid.
	 ******************************/
	public ArrayList<Node> getInvalidVertex()
	{
		ArrayList<Node> invalid_list = new ArrayList<>();
		for (Node node: this.vertices)
		{
			for (Edge edge: this.edges)
			{
				if (edge.hasOn(node))
				{
					invalid_list.add(node);
					break;
				}
			}
		}

		return invalid_list;
	}


	/******************************
	 * Function : maxIntersectionEdge
	 * 		Used to find the edge who causes the most intersections.
	 *****************************/
	public Edge maxIntersectionEdge()
	{
	    if(this.edges == null)
	    {
			this.listEdges();
			this.numGraphIntersected();
		}

	    Edge edge_max = this.edges.get(0);
	    for(Edge edge : this.edges)
		{
			if(edge_max.num_intersection < edge.num_intersection)
				edge_max = edge;
		}

	    return edge_max;
	}


	public int getEdgeIntersection(Edge edge_tar)
	{
		int num_intersect = 0;

		for(Edge edge : this.edges)
		{
		    if(isIntersected(edge_tar, edge))
		        num_intersect++;
		}

		return num_intersect;
	}


	public int getNodeIntersection(Node node)
	{
		int num_intersect = 0;

		for(Node pred : node.predecessors)
		{
			num_intersect += this.getEdgeIntersection(new Edge(pred, node));
		}

		for (Node succ : node.successors)
		{
			num_intersect += this.getEdgeIntersection(new Edge(node, succ));
		}

		return num_intersect;
	}


	public Node maxIntersectionNode()
	{
		if(this.edges == null)
		{
			this.listEdges();
			this.numGraphIntersected();
		}

		Node node_max = this.vertices.get(0);
		for(Node node : this.vertices)
		{
			if(node_max.num_intersection < node.num_intersection)
				node_max = node;
		}

		return node_max;
	}
}
