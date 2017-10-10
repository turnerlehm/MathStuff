package data_structures.graphs;


import java.util.HashMap;

import java.util.TreeSet;

public abstract class Graph<T>
{
    private HashMap<String, Node> V;
    private TreeSet<Edge> E;
    private int nodes;
    private int edges;

    //add an unlabelled node
    public Node addNode(String node)
    {
        Node n = V.get(node);
        if(n == null)
        {
            n = new Node(node, null, nodes);
            V.put(node, n);
            nodes++;
        }
        return n;
    }
    //add a labelled node
    public Node addNode(String node, T label)
    {
        Node n = V.get(node);
        if(n == null)
        {
            n = new Node(node, label, nodes);
            V.put(node, n);
            nodes++;
        }
        return n;
    }


    public Iterable<Edge> adjacent(Node n)
    {
        if(valid(n))
            return n.adjList;
        return null;
    }

    public int degree(String node)
    {
        if(valid(node))
            return V.get(node).adjList.size();
        return -1;
    }

    public Iterable<Edge> edges()
    {
        return E;
    }

    public int numEdges()
    {
        return E.size();
    }

    public Iterable<Node> nodes()
    {
        return V.values();
    }

    public int numNodes()
    {
        return V.size();
    }

    //BUG?: if using key, an attacker could pass in a forged key and retrieve valid data
    private boolean valid(String node)
    {
        if (node == null)
            return false;
        return V.containsKey(node);
    }

    private boolean valid(Node node)
    {
        if(node == null)
            return false;
        return V.containsKey(node.key);
    }

    private boolean valid(int n)
    {
        return (n > 0 && n < nodes);
    }

    protected class Node
    {
        TreeSet<Edge> adjList;
        String key;
        T label;
        int num;
        public Node(String key, T label, int num)
        {
            this.key = key;
            this.label = label;
            this.num = num;
            this.adjList = new TreeSet<Edge>();
        }
    }
    protected class Edge
    {
        Node from;
        Node to;
        int weight;
        double capacity; //for networks
        double flow; //for networks
        int int_flow; //for networks
        //simple graphs
        public Edge(Node from, Node to)
        {
            if(from == null)
                throw new IllegalArgumentException("from node must not be null");
            this.from = from;
            this.to = to;
        }
        //graphs with weighted edges
        public Edge(Node from, Node to, int weight)
        {
            if(from == null || to == null)
                throw new IllegalArgumentException("from and to must not be null");
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
        //networks
        public Edge(Node from, Node to, double capacity)
        {
            if(from == null || to == null)
                throw new IllegalArgumentException("from and to must not be null");
            if(capacity < 0)
                throw new IllegalArgumentException("capacity must be positive");
            this.from = from;
            this.to = to;
            this.capacity = capacity;
        }
    }
}
