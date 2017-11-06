package data_structures.simplegraphs;

/**
 * Simple unweighted directed graph.
 *
 * Abstract to allow for more modularity.
 * @param <E>
 */

public abstract class DirectedGraph<E> extends Graph<E>
{

    /**
     * Only from's set will have the edge after creation
     */
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
        adjacentTo(from).add(e);
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
        numEdges++;
    }
}
