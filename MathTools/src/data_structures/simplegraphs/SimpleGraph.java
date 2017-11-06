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
        Edge e = new Edge(f,t);
        adjacentTo(f).add(e);
        adjacentTo(t).add(e);
        numEdges++;
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
        adjacentTo(to).add(e);
        numEdges++;
    }
}
