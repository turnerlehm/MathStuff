package data_structures.simplegraphs;

public class WeightedDAG<E> extends DirectedAcyclicGraph<E>
{
    public void addEdge(int from, int to)
    {
        super.addEdge(from, to);
    }

    public void addEdge(Node from, Node to)
    {
        super.addEdge(from, to);
    }

    public void addEdge(int from, int to, int weight)
    {
        if(from == to)
            return;
        Node f, t;
        if((f = getNode(from)) == null)
            f = addNode(from);
        if((t = getNode(to)) == null)
            t = addNode(to);
        if(hasEdge(f,t))
            return;
        Edge e = new Edge(f,t,weight);
        adjacentTo(from).add(e);
        numEdges++;
        removeCycle(from,e);
    }

    public void addEdge(Node from, Node to, int weight)
    {
        if(from.equals(to))
            return;
        if(!hasNode(from))
            addNode(from);
        if(!hasNode(to))
            addNode(to);
        Edge e = new Edge(from,to,weight);
        adjacentTo(from).add(e);
        numEdges++;
        removeCycle(from, e);
    }
}
