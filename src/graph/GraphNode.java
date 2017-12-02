/*
 * Keith Fosmire
 *Graph Assignment 3
 * Due May 16 2014
*This preogram searches through the Humanism page on wikipedia 
*retrieves all links and randomly chooses one lik to follow, recursively repeatng
*until 1010 pages are collected with their links.
 */
package graph;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author BlackHawk31
 */
public class GraphNode {

    private final String vertex;
    private String title;
    private final ArrayList<String> edges;

    GraphNode(String url) {
        vertex = url;
        edges = new ArrayList();
        title = null;
    }

    public ArrayList<String> getEdges() {

        return edges;
    }

    public String getVertex() {
        return vertex;
    }

    public void addEdge(String edge) {
        if (!edges.contains(edge)) {
            edges.add(edge);
        } else {
        }
    }

    public int getNumOfEdges() {

        return edges.size();
    }

    public void setTitle(String in) {
        title = in;
    }

    public String getTitle() {
        return title;
    }

    public String randomLink() {

        Random rand = new Random();
        int n = rand.nextInt(edges.size());
        return edges.get(n);
    }
}
