package Visual;
import jdg.graph.DirectedGraph;
import jdg.io.GraphLoader;
import jdg.io.GraphReader_Json;
import jdg.io.GraphWriter_Json;
import processing.core.PApplet;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Test
{
    public static void main(String[] args)
    {

        String inputFile = "./data/graph_05.json"; // input file, encoded as JSON format

        DirectedGraph g=GraphLoader.loadGraph(inputFile); // input graph
        int[] drawingBounds=GraphReader_Json.readDrawingAreaBounds(inputFile); // reading width and height of the drawing area

        // set the input parameters for drawing the graph layout
        GraphRenderer.sizeX=800; // setting canvas width (number of pixels)
        GraphRenderer.sizeY=800; // setting canvas height (pixels)
        GraphRenderer.inputGraph=g; // set the input graph (for rendering)
        GraphRenderer.drawingWidth=drawingBounds[0]; // setting the width of the drawing area
        GraphRenderer.drawingHeight=drawingBounds[1];  // setting the width of the drawing area

        if(UpwardDrawing.hasInitialLayout(g)) // check whether the nodes are provided with an initial embedding
            System.out.println("The input graph has an initial embedding");
        else
            System.out.println("The input graph is not provided with an initial embedding");

        ///////////////////////////////////////////////////////
        //#####################################################
        ///////////////////////////////////////////////////////
        // initialize the upward drawing
        UpwardDrawing ud=new UpwardDrawing(g, GraphRenderer.drawingWidth, GraphRenderer.drawingHeight);
        System.out.println(ud.checkValidity());

        ud.computeValidInitialLayout(); // first phase: compute a valid drawing (if necessary)
        System.out.println(ud.checkValidity());
        int crossing_init = ud.computeCrossings();

        ////////////////////////////////////////////////////////////
        // MODIFY HERE TO CHANGE THE METHODS
        //ud.computeUpwardDrawing(); // second phase: minimize the number of crossings
        //ud.forceDirectedHeuristic();
        ud.localSearchHeuristicNode(1);
        //ud.localSearchHeuristicNode(1);
        ////////////////////////////////////////////////////////////

        boolean isValid=ud.checkValidity(); // check whether the result is a valid drawing
        System.out.println(isValid);
        int crossings_end = ud.computeCrossings();
        System.out.println(" Init : " + crossing_init);
        System.out.println(" End :  " + crossings_end);

        // uncomment the line below to show a 2D layout of the graph
        PApplet.main(new String[] { "Visual.GraphRenderer" }); // launch the Processing viewer

    }

}
