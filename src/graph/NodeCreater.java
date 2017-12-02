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
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TitleTag;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.NodeList;

/**
 *
 * @author BlackHawk31
 */
public class NodeCreater {

    private ArrayList<GraphNode> nodes = new ArrayList();
    private ArrayList<String> names = new ArrayList();
    private Map<String, ArrayList> graph = new HashMap();
    private int numOfNodes;

    public NodeCreater() {
        this.numOfNodes = 0;
    }

    public ArrayList<String> readNodeNames() {

        Iterator<GraphNode> it = nodes.iterator();
        ArrayList<String> out = new ArrayList();
        while (it.hasNext()) {
            GraphNode temp = it.next();
            out.add(temp.getTitle());
        }
        return out;
    }

    public void createNodes(String url) {
        while (numOfNodes < 1010) {
            try {
                try {
                    File links = new File(numOfNodes + "links.txt");
                    ArrayList<String> allUrls = new ArrayList();
                    Parser parser = new Parser(url);
                    NodeList list = parser.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));
                    BufferedWriter write = new BufferedWriter(new FileWriter(links));
                    names.add(numOfNodes, url);
                    int i;
                    for (i = 0; i < list.size(); ++i) {
                        LinkTag l = (LinkTag) list.elementAt(i);
                        String s = l.getLink();
                        if (s.contains("Talk:") || s.contains("User:") || s.length() < 20 || s.contains("#") || s.contains(".svg") || s.contains("File:") || s.contains("%") || s.contains(".jpg") || s.contains("Category:") || s.contains("Wikipedia:") || s.contains("Help:") || s.contains("Portal:") || s.contains("Main_Page") || s.contains(".php") || s.contains("Link_rot") || s.contains("cite_ref") || s.contains("cite_note") || !s.contains("en.wikipedia") || s.contains("Special:") || s.contains("Template")) {
                        } else {
                            write.write(s + '\n');
                            allUrls.add(s);
                        }
                    }

                    GraphNode temp = new GraphNode(url);
                    nodes.add(numOfNodes, temp);
                    ++numOfNodes;

                    Iterator itt = allUrls.iterator();
                    while (itt.hasNext()) {
                        String s = itt.next().toString();
                        temp.addEdge(s);
                    }
                    String ln = temp.randomLink();
                    while (names.contains(ln)) {
                        ln = temp.randomLink();
                    }
                    write.close();
                    createNodes(ln);
                } catch (ParserException e) {
                    e.getLocalizedMessage();
                    System.out.println("Parser Failed");
                }
            } catch (IOException e) {
                e.getLocalizedMessage();
                System.out.println("Error writing file");
            }
        }
        writeNames();
    }

    public void createNodes(int f, String url) {
        try {
            try {
                File links = new File(f + "links.txt");
                Parser parser = new Parser(url);
                NodeList list = parser.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));
                BufferedWriter write = new BufferedWriter(new FileWriter(links));
                int i;
                for (i = 0; i < list.size(); ++i) {
                    LinkTag l = (LinkTag) list.elementAt(i);
                    String s = l.getLink();
                    if (s.length() < 20 || s.contains("#") || s.contains(".jpg") || s.contains(".svg") || s.contains("File:") || s.contains("%") || s.contains("Category:") || s.contains("Wikipedia:") || s.contains("Help:") || s.contains("Portal:") || s.contains("Main_Page") || s.contains(".php") || s.contains("Link_rot") || s.contains("cite_ref") || s.contains("cite_note") || !s.contains("en.wikipedia") || s.contains("Special:") || s.contains("Template")) {
                    } else {
                        write.write(s + '\n');
                    }
                }

                GraphNode node = new GraphNode(url);
                nodes.add(f, node);
                names.add(f, url);
                Scanner scan = new Scanner(links);
                while (scan.hasNext()) {
                    String s = scan.next();
                    node.addEdge(s);
                }
                write.close();
            } catch (ParserException e) {
                e.getLocalizedMessage();
                System.out.println("Parser Failed CF");
                System.out.println(url);
                --numOfNodes;
            }
        } catch (IOException e) {
            e.getLocalizedMessage();
            System.out.println("Error writing file");
        }
    }

    public void readVertices() {

        try {

            FileInputStream fstream = new FileInputStream("names.txt");
            DataInputStream input = new DataInputStream(fstream);
            try {
                ArrayList<String> vertices = new ArrayList();
                try (BufferedReader read = new BufferedReader(new InputStreamReader(input))) {
                    String url;
                    System.out.println("reading");
                    int j = 0;
                    while ((url = read.readLine()) != null) {
                        vertices.add(j, url);
                        ++j;
                    }

                    getTitles(vertices);
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

    public void getTitles(ArrayList<String> in) {

        try {
            try {
                File links = new File("titles.txt");
                BufferedWriter write = new BufferedWriter(new FileWriter(links));
                System.out.println("writing");
                int i = 0;
                while (i < in.size()) {

                    String url = in.get(i);
                    Parser parser = new Parser(url);
                    NodeList list = parser.parse(new TagNameFilter("TITLE"));
                    if (list.elementAt(0) != null) {
                        String t = list.elementAt(0).toString();
                        write.write(t + "\n");
                    } else {

                        write.write(i + "\n");
                    }
                    ++i;
                }
                write.close();
            } catch (ParserException e) {
                e.getLocalizedMessage();
                System.out.println("Parser Failed");
            }
        } catch (IOException e) {
            e.getLocalizedMessage();
            System.out.println("Error writing file");
        }

    }

    public void writeNames() {
        try {
            File name = new File("names.txt");
            BufferedWriter write = new BufferedWriter(new FileWriter(name));
            Iterator<String> it = names.iterator();
            int i = 0;
            while (it.hasNext()) {
                write.write(it.next() + "\n");
                ++i;
            }
            write.close();

        } catch (IOException e) {
            e.getLocalizedMessage();
            System.out.println("Error writing file");
        }
    }
}
