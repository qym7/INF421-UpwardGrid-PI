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
public class ComputeUpwardMainClone {

    public static void main(String[] args) {
        System.out.println("Tools for the \"Graph Drawing Contest 2020: Live Challenge\"");

        //String inputFile=args[0]; // input file, encoded as JSON format
        for (int i = 2; i < 13; i++) {
            String temp;
            if (i < 10) temp = "0" + i;
            else temp = "" + i;
            String inputFile = "data/graph_" + temp + ".json";
            DirectedGraph g = GraphLoader.loadGraph(inputFile); // input graph
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

            System.out.println("Force directed : ");
            ud.g = GraphLoader.loadGraph(inputFile);
            ud.computeValidInitialLayout(); // first phase: compute a valid drawing (if necessary)
            int crossing_init_force = ud.computeCrossings();
            long time_force_start = System.currentTimeMillis();
            ud.forceDirectedHeuristic();
            long time_force_end = System.currentTimeMillis();
            int crossings_end_force = ud.computeCrossings();

		/*
		System.out.println("Force directed:");
		System.out.println(" Init : " + crossing_init_force);
		System.out.println(" End :  " + crossings_end_force);
		System.out.println(" Time used : " + (time_force_end-time_force_start) + "ms.");
		System.out.println();

		 */

            System.out.println("Local transverse: ");
            ud.g = GraphLoader.loadGraph(inputFile);
            ud.computeValidInitialLayout(); // first phase: compute a valid drawing (if necessary)
            int crossing_init_transverse = ud.computeCrossings();
            long time_transverse_start = System.currentTimeMillis();
            ud.localSearchHeuristicNode(3);
            //ud.localSearchHeuristic();
            long time_transverse_end = System.currentTimeMillis();
            int crossing_end_transverse = ud.computeCrossings();
            System.out.println(ud.checkValidity());


            System.out.println("Local Markov: ");
            ud.g = GraphLoader.loadGraph(inputFile);
            ud.computeValidInitialLayout(); // first phase: compute a valid drawing (if necessary)
            int crossing_init_markov = ud.computeCrossings();
            long time_markov_start = System.currentTimeMillis();
            //ud.localSearchHeuristicNode(3);
            ud.localSearchHeuristic();
            long time_markov_end = System.currentTimeMillis();
            int crossing_end_markov = ud.computeCrossings();
            System.out.println(ud.checkValidity());
            System.out.println();


            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            System.out.println("-----------------------------------");
            System.out.println(dtf.format(now));
            System.out.println("Graph : " + inputFile);
            System.out.println("Number of level : " + ud.g.level_list.size() + ".");
            System.out.println("Size of grid : " + ud.width + "*" + ud.height + ".");
            System.out.println("Results : ");
            System.out.println();
            System.out.println("Force directed :");
            System.out.println("Init : " + crossing_init_force);
            System.out.println("End :  " + crossings_end_force);
            System.out.println("Time used : " + (time_force_end - time_force_start) + "ms.");
            System.out.println();
            System.out.println("Local Transverse :");
            System.out.println("Init : " + crossing_init_transverse);
            System.out.println("End :  " + crossing_end_transverse);
            System.out.println("Time used : " + (time_transverse_end - time_transverse_start) + "ms.");
            System.out.println();
            System.out.println("Local Markov :");
            System.out.println("Init : " + crossing_init_markov);
            System.out.println("End :  " + crossing_end_markov);
            System.out.println("Time used : " + (time_markov_end - time_markov_start) + "ms.");


            // save results to file
            try {
                FileOutputStream file_os = new FileOutputStream("./result/test_result_2.data", true);
                PrintStream file_out = new PrintStream(file_os);

                file_out.println("-----------------------------------");
                file_out.println(dtf.format(now));
                file_out.println("Graph : " + inputFile);
                file_out.println("Number of level : " + ud.g.level_list.size() + ".");
                file_out.println("Size of grid : " + ud.width + "*" + ud.height + ".");
                file_out.println("Results : ");
                file_out.println();
                file_out.println("Force directed :");
                file_out.println("Init : " + crossing_init_force);
                file_out.println("End :  " + crossings_end_force);
                file_out.println("Time used : " + (time_force_end - time_force_start) + "ms.");
                file_out.println();
                file_out.println("Local Transverse :");
                file_out.println("Init : " + crossing_init_transverse);
                file_out.println("End :  " + crossing_end_transverse);
                file_out.println("Time used : " + (time_transverse_end - time_transverse_start) + "ms.");
                file_out.println();
                file_out.println("Local Markov :");
                file_out.println("Init : " + crossing_init_markov);
                file_out.println("End :  " + crossing_end_markov);
                file_out.println("Time used : " + (time_markov_end - time_markov_start) + "ms.");
                file_out.println();
            } catch (FileNotFoundException e) {
                System.err.println(e);
            }


            //System.out.println(ud.g.getInvalidVertex());

            // write the upward drawing computed by your program to a Json file
            GraphWriter_Json.write(g, GraphRenderer.drawingWidth, GraphRenderer.drawingHeight, "output.json");

            // uncomment the line below to show a 2D layout of the graph
            PApplet.main(new String[]{"Visual.GraphRenderer"}); // launch the Processing viewer

        }
    }
}