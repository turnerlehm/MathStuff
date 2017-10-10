package data_structures.simplegraphs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

public abstract class Graph<E>
{
    private int V;
    private int E;
    private HashMap<Node, HashSet<Edge>> adjacency;
    private HashMap<Integer, Node> nodes;
    private final HashSet<Node> EMPTY = new HashSet<Node>();
    private int numNodes;
    private int numEdges;

    //add an unlabeled node
    public Node addNode(int key)
    {
        Node n = nodes.get(key);
        if(n == null)
        {
            n = new Node(null, numNodes);
            nodes.put(key, n);
            adjacency.put(n, new HashSet<Edge>());
            numNodes++;
        }
        return n;
    }

    //add a labeled node
    public Node addNode(E label, int key)
    {
        Node n = nodes.get(key);
        if(n == null)
        {
            n = new Node(label, numNodes);
            nodes.put(key, n);
            adjacency.put(n, new HashSet<Edge>());
            numNodes++;
        }
        return n;
    }

    protected class Node
    {
        E data;
        int num;
        public Node(E data, int n)
        {
            this.data = data;
            this.num = n;
        }
    }

    protected class Edge
    {
        Node from;
        Node to;
        int weight;

        //simple, unweighted edge
        public Edge(Node from, Node to)
        {
            assert from != null : "from node must not be null";
            this.from = from;
            this.to = to;
        }

        //weighted edge
        public Edge(Node from, Node to, int weight)
        {
            assert (from != null || to != null) : "from and to must not be null";
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }
}
