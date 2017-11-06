package data_structures.simplegraphs;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public abstract class Graph<E>
{
    private HashMap<Node, HashSet<Edge>> adjacency;
    private HashMap<Integer, Node> nodes;
    private final HashSet<Edge> EMPTY = new HashSet<Edge>();
    protected int numNodes;
    protected int numEdges;

    public Graph()
    {
        adjacency = new HashMap<Node, HashSet<Edge>>();
        nodes = new HashMap<Integer,Node>();
    }

    //add an unlabeled node
    public Node addNode(int key)
    {
        Node n = nodes.get(key);
        if(n == null)
        {
            n = new Node(null, numNodes, key);
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
            n = new Node(label, numNodes, key);
            nodes.put(key, n);
            adjacency.put(n, new HashSet<Edge>());
            numNodes++;
        }
        return n;
    }

    public void addNode(Node n)
    {
        if(n == null)
            return;
        else if(hasNode(n))
            return;
        else
        {
            nodes.put(n.key, n);
            adjacency.put(n, new HashSet<Edge>());
            numNodes++;
        }
    }

    public Node getNode(int node)
    {
        return nodes.get(node);
    }

    public boolean hasNode(int node)
    {
        return nodes.containsKey(node);
    }

    public void removeNode(int node)
    {
        nodes.remove(node);
    }

    public void removeNode(Node n)
    {
        nodes.values().remove(n);
    }

    public boolean hasNode(Node n)
    {
        return nodes.values().contains(n);
    }

    public boolean hasEdge(Node from, Node to)
    {
        if(from == null || to == null)
            return false;
        for(Edge e : adjacency.get(from))
            if(e.to.equals(to))
                return true;
        return false;
    }

    public abstract void addEdge(int from, int to);

    public abstract void addEdge(Node from, Node to);

    public HashSet<Edge> adjacentTo(int node)
    {
        return !hasNode(node) ? EMPTY : adjacency.get(getNode(node));
    }

    public HashSet<Edge> adjacentTo(Node n)
    {
        return !adjacency.containsKey(n) ? EMPTY : adjacency.get(n);
    }

    public Collection<Node> getNodes()
    {
        return nodes.values();
    }

    public String toString()
    {
        String s = "";
        for(Node n : getNodes())
        {
            s += n + ": ";
            for(Edge e : adjacentTo(n))
                s += e + " ";
            s += "\n";
        }
        return s;
    }

    protected class Node
    {
        E data;
        int num;
        int key;
        public Node(E data, int n, int k)
        {
            this.data = data;
            this.num = n;
            this.key = k;
        }

        public String toString()
        {
            if(data != null)
                return "[" + key + "," + data + "]"; //if the node is labeled/typed, append label/type to key
            else
                return "" + key;
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

        public String toString()
        {
            if(weight != 0)
                return "(" + to + "," + weight + ")";
            else
                return to.toString();
        }
    }
}
