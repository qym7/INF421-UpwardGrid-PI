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

/**
 * Main program that takes as input a JSON and compute an upward grid drawing
 * that minimizes the number of crossings.
 *
 * @author Luca Castelli Aleardi (INF421, 2019)
 *
 */
public class out {

    public static void main(String[] args) {
        System.out.println("Tools for the \"Graph Drawing Contest 2020: Live Challenge\"");

        System.out.println("Tools for the \"Graph Drawing Contest 2020: Live Challenge\"");
        if(args.length<1) {
            System.out.println("Error: one argument required: input file in JSON format");
            System.exit(0);
        }

        String inputFile=args[0]; // input file, encoded as JSON format
        DirectedGraph g=GraphLoader.loadGraph(inputFile); // input graph

//        for (int i = 2; i < 13; i++) {
//            String temp;
//            if (i < 10) temp = "0" + i;
//            else temp = "" + i;
//            String inputFile = "data/graph_" + temp + ".json";
//            DirectedGraph g = GraphLoader.loadGraph(inputFile); // input graph
            int[] drawingBounds = GraphReader_Json.readDrawingAreaBounds(inputFile); // reading width and height of the drawing area

            // set the input parameters for drawing the graph layout
            GraphRenderer.sizeX = 800; // setting canvas width (number of pixels)
            GraphRenderer.sizeY = 800; // setting canvas height (pixels)
            GraphRenderer.inputGraph = g; // set the input graph (for rendering)
            GraphRenderer.drawingWidth = drawingBounds[0]; // setting the width of the drawing area
            GraphRenderer.drawingHeight = drawingBounds[1];  // setting the width of the drawing area

            if (UpwardDrawing.hasInitialLayout(g)) // check whether the nodes are provided with an initial embedding
                System.out.println("The input graph has an initial embedding");
            else
                System.out.println("The input graph is not provided with an initial embedding");


            // initialize the upward drawing
            UpwardDrawing ud = new UpwardDrawing(g, GraphRenderer.drawingWidth, GraphRenderer.drawingHeight);
            System.out.println(ud.checkValidity());

            ud.computeValidInitialLayout(); // first phase: compute a valid drawing (if necessary)
            System.out.println(ud.checkValidity());
            //ud.computeUpwardDrawing(); // second phase: minimize the number of crossings
            //boolean isValid=ud.checkValidity(); // check whether the result is a valid drawing

            ud.g = GraphLoader.loadGraph(inputFile);
            ud.computeValidInitialLayout(); // first phase: compute a valid drawing (if necessary)


            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            System.out.println("-----------------------------------");
            System.out.println(dtf.format(now));
            System.out.println("Graph : " + inputFile);
            System.out.println("Width : " + ud.height);
            System.out.println("Height: " + ud.width);
            System.out.println("Dimension : " + ud.height*ud.width);
            System.out.println("Edges: " + ud.g.edges.size());
            System.out.println("Nodes: " + ud.g.vertices.size());
            System.out.println();
            // save results to file
            try {
                FileOutputStream file_os = new FileOutputStream("./result/dimension.data", true);
                PrintStream file_out = new PrintStream(file_os);

                file_out.println("-----------------------------------");
                file_out.println(dtf.format(now));
                file_out.println("Graph : " + inputFile);
                file_out.println("Width : " + ud.height);
                file_out.println("Height: " + ud.width);
                file_out.println("Dimension : " + ud.height*ud.width);
                file_out.println("Edges: " + ud.g.edges.size());
                file_out.println("Nodes: " + ud.g.vertices.size());
                file_out.println();

                file_out.println("-----------------------------------");
                file_out.println(dtf.format(now));

                file_out.println();
            } catch (FileNotFoundException e) {
                System.err.println(e);
            }
            //System.out.println(ud.g.getInvalidVertex());
        // ud.computeValidInitialLayout();
        ud.forceDirectedHeuristic();
        //ud.localSearchHeuristic();
        // write the upward drawing computed by your program to a Json file
        DirectedGraph f = new DirectedGraph();
        f = ud.g;
        GraphWriter_Json.write(f, GraphRenderer.drawingWidth, GraphRenderer.drawingHeight, "output.json");

        // uncomment the line below to show a 2D layout of the graph
        PApplet.main(new String[]{"Visual.GraphRenderer"}); // launch the Processing viewer

        }
        //String inputFile=args[0]; // input file, encoded as JSON format
    }
//}