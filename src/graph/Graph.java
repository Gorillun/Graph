/*
 * Keith Fosmire
 *Graph Assignment 3
 * Due May 16 2014
*This preogram searches through the Humanism page on wikipedia 
*retrieves all links and randomly chooses one lik to follow, recursively repeatng
*until 1010 pages are collected with their links.
 */
package graph;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author BlackHawk31
 */
public class Graph {

    private static final String url = "http://en.wikipedia.org/wiki/CD134";
    private int numOfNodes;
    private final ArrayList<String> names;
    private final ArrayList<String> titles;
    private final ArrayList<GraphNode> nodes;
    private final ArrayList<String> vertices;
    private Deque<String> q;
    private final Hashtable<String, String> titleUrl;
    private final Hashtable<String, Integer> hashTitles;
    private final Hashtable<String, Integer> hashVertices;
    private final Hashtable<String, ArrayList<String>> edges;

    Graph() {
        numOfNodes = 0;
        names = new ArrayList();
        q = new LinkedList();
        titles = new ArrayList();
        nodes = new ArrayList();
        hashTitles = new Hashtable();
        titleUrl = new Hashtable();
        hashVertices = new Hashtable();
        vertices = new ArrayList();
        edges = new Hashtable();
        populateNames();
        initHashTables();
        readVertices();
        //MST();
    }

    private void populateNames() {

        try {
            FileInputStream fstream = new FileInputStream("names.txt");
            DataInputStream input = new DataInputStream(fstream);
            try {

                try (BufferedReader read = new BufferedReader(new InputStreamReader(input))) {
                    String url;
                    while ((url = read.readLine()) != null) {
                        names.add(numOfNodes, url);

                        ++numOfNodes;
                    }
                }
            } catch (IOException e) {
                e.getLocalizedMessage();
                System.out.println("error reading file");

            }
        } catch (FileNotFoundException e) {
            e.getLocalizedMessage();
            System.out.println("Error opening file");
        }
        populateTitles();
    }

    private void populateTitles() {

        try {
            FileInputStream fstream = new FileInputStream("titles.txt");
            DataInputStream input = new DataInputStream(fstream);
            try {

                try (BufferedReader read = new BufferedReader(new InputStreamReader(input))) {
                    String url;
                    int n = 0;
                    while ((url = read.readLine()) != null) {
                        titles.add(n, url);
                        ++n;
                    }
                }
            } catch (IOException e) {
                e.getLocalizedMessage();
                System.out.println("error reading file");

            }
        } catch (FileNotFoundException e) {
            e.getLocalizedMessage();
            System.out.println("Error opening file");
        }
        writeNodes();
    }

    public void writeNodes() {
        int i = 0;
        while (i < numOfNodes) {

            try {

                Scanner read = new Scanner(new FileReader(i + "links.txt"));
                String url;
                int j = 0;
                GraphNode node = new GraphNode(names.get(i));
                nodes.add(i, node);
                while (read.hasNext()) {
                    url = read.next();
                    node.addEdge(url);
                    ++j;
                }
                node.setTitle(titles.get(i));
            } catch (FileNotFoundException e) {
                e.getLocalizedMessage();
                System.out.println("error reading file");

            }
            ++i;
        }

    }

    public ArrayList<String> readNodeNames() {
        ArrayList<String> out = new ArrayList();
        Iterator<String> it = titles.iterator();
        while (it.hasNext()) {
            String s = it.next();
            out.add(s);
        }

        return out;
    }

    private void initHashTables() {

        Iterator<GraphNode> it = nodes.iterator();
        int i = 0;
        while (it.hasNext()) {
            GraphNode temp = it.next();
            String title = temp.getTitle();
            String v = temp.getVertex();
            hashTitles.put(title, i);
            hashVertices.put(v, i);
            vertices.add(i, v);
            titleUrl.put(temp.getTitle(), temp.getVertex());
            ++i;
        }

    }

    private void readVertices() {
        try {
            FileInputStream fstream = new FileInputStream("titles.txt");
            DataInputStream input = new DataInputStream(fstream);
            try {

                try (BufferedReader read = new BufferedReader(new InputStreamReader(input))) {
                    String url;
                    while ((url = read.readLine()) != null) {
                        vertices.add(url);
                    }
                    HashSet h = new HashSet();
                    h.addAll(vertices);
                    vertices.clear();
                    vertices.addAll(h);
                }
            } catch (IOException e) {
                e.getLocalizedMessage();
                System.out.println("error reading file");

            }
        } catch (FileNotFoundException e) {
            e.getLocalizedMessage();
            System.out.println("Error opening file");
        }
    }

    public void MST() {
        try {
            File links = new File("2ndspanningTree.txt");
            BufferedWriter write = new BufferedWriter(new FileWriter(links));
            GraphNode current;
            ArrayList<String> next;
            Iterator<String> it = vertices.iterator();
            ArrayList<String> visited = new ArrayList();

            q.add(url);
            while (q.peek() != null) {
                String qd = q.poll();
                if (visited.contains(qd)) {
                } else if (hashVertices.get(qd) == null) {
                    visited.add(qd);
                } else {
                    current = nodes.get(hashVertices.get(qd));
                    next = current.getEdges();
                    Iterator<String> t = next.iterator();
                    visited.add(qd);
                    write.write(qd + "--->\n");
                    while (t.hasNext()) {
                        String s = t.next();

                        q.add(s);
                    }
                }

            }

        } catch (IOException e) {
            e.getLocalizedMessage();
            System.out.println("Error writing file");
        }
    }

    public String findPath(String start, String end) {

        GraphNode node = nodes.get(hashTitles.get(start));
        String e = titleUrl.get(end);
        String s = node.getVertex();
        q.clear();
        ArrayList<String> visited = new ArrayList();
        Stack<String> path = new Stack();
        q.add(s);
        while (q.peek() != null) {
            BFS(q.poll(), e, visited, path);
        }
        return path.toString();
    }

    private void BFS(String start, String end, ArrayList<String> visited, Stack<String> path) {
        if (visited.contains(start)) {

        } else if (start.equalsIgnoreCase(end)) {
            path.add(start + "\n");
            q.clear();

        } else if (hashVertices.get(start) == null) {
            visited.add(start);
        } else {
            GraphNode current = nodes.get(hashVertices.get(start));
            path.add(start + "\n");
            visited.add(start);
            ArrayList<String> next = current.getEdges();
            Iterator<String> it = next.iterator();
            while (it.hasNext()) {
                q.add(it.next());
            }

        }

    }

}
