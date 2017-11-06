package data_structures.simplegraphs;

/**
 * A simple undirected graph
 * Most code borrowed from here https://www.cs.duke.edu/courses/cps100e/spring10/class/14_Bacon/code/Graph.html
 * @param <E> the labeling for the graph, if it exists
 */
public class SimpleGraph<E> extends Graph<E>
{
    @Override
    public void addEdge(int from, int to)
    {
        Node f, t;
        if((f = getNode(from)) == null)
            f = addNode(from);
        if((t = getNode(to)) == null)
            t = addNode(to);
        if(hasEdge(f,t))
            return;
        addEdge(f,t);
    }

    @Override
    public void addEdge(Node from, Node to)
    {
        if(hasEdge(from,to))
            return;
        if(!hasNode(from))
            addNode(from);
        if(!hasNode(to))
            addNode(to);
        if(hasEdge(from,to))
            return;
        Edge e = new Edge(from,to);
        adjacentTo(from).add(e);
        numEdges++;
    }

    /*sanity check
    public static void main(String... args)
    {
        Graph<String> G = new SimpleGraph<String>();
        G.addNode("A",0);
        G.addNode("B",1);
        G.addNode("C",2);
        G.addNode("D",3);
        G.addEdge(0,1);
        G.addEdge(2,3);
        G.addEdge(1,3);
        G.addEdge(3,0);
        System.out.println(G);
    }*/
}
