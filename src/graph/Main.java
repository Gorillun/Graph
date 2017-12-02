/*
 * Keith Fosmire
 *Graph Assignment 3
 * Due May 16 2014
*This preogram searches through the Humanism page on wikipedia 
*retrieves all links and randomly chooses one lik to follow, recursively repeatng
*until 1010 pages are collected with their links.
 */
package graph;

/**
 *
 * @author BlackHawk31
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    private static final String url = "http://en.wikipedia.org/wiki/Humanism";

    public static void main(String[] args) {

        Display display = new Display();
        display.setVisible(true);
        //NodeCreater graph = new NodeCreater();
        //graph.readVertices();
    }
}
