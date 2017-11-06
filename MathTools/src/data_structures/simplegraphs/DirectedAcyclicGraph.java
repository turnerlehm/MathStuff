package data_structures.simplegraphs;

public class DirectedAcyclicGraph<E> extends Graph<E> //because checking cyclicity is more complicated
{

    /**
     * 1. Check if from == to (no self-loops)
     * 2. Check if adjacency list contains either the start node or duplicates
     * @param from
     * @param to
     */
    @Override
    public void addEdge(int from, int to)
    {

    }

    @Override
    public void addEdge(Node from, Node to)
    {

    }
}
