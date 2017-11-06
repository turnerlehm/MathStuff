package data_structures.simplegraphs;

import java.util.Arrays;
import java.util.Stack;
import java.util.HashSet;

public abstract class DirectedAcyclicGraph<E> extends Graph<E> //because checking cyclicity is more complicated
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
        if(from == to)
            return;
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
        removeCycle(from,e);
    }

    @Override
    public void addEdge(Node from, Node to)
    {
        if(from.equals(to))
        return;
        if(!hasNode(from))
            addNode(from);
        if(!hasNode(to))
            addNode(to);
        Edge e = new Edge(from,to);
        adjacentTo(from).add(e);
        numEdges++;
        removeCycle(from, e);
    }

    //if on adding an edge we create a cycle, remove that edge.
    //could use Tarjan's SCC algorithm and check if |SCC| != 0
    //issue if each node is disconnected since |SCC| = |V|
    //but we can check for this as well
    //Or do DFT and check for back-edges
    protected void removeCycle(int from, Edge e)
    {
        Stack<HashSet<Node>> components = tarjan(this);
        if(components.size() != 0 && components.size() != numNodes)
            adjacentTo(from).remove(e);
        numEdges--;
    }

    protected void removeCycle(Node from, Edge e)
    {
        Stack<HashSet<Node>> components = tarjan(this);
        if(components.size() != 0 && components.size() != numNodes)
            adjacentTo(from).remove(e);
        numEdges--;
    }

    private Stack<HashSet<Node>> tarjan(Graph<E> G)
    {
        Stack<HashSet<Node>> comps = new Stack<HashSet<Node>>();
        Stack<Node> S = new Stack<Node>();
        int idx = 0;
        int[] index = new int[numNodes];
        Arrays.fill(index, -1);
        int[] low = new int[numNodes];
        boolean[] stacked = new boolean[numNodes];
        for(Node n : getNodes())
            if(index[n.num] == -1)
                strongConnect(n,idx,index,low,stacked,comps,S);
        return comps;
    }

    private void strongConnect(Node n, int idx, int[] index, int[] low, boolean[] stacked, Stack<HashSet<Node>> comps, Stack<Node> S)
    {
        index[n.num] = idx;
        low[n.num] = idx;
        idx++;
        S.push(n);
        stacked[n.num] = true;
        for(Edge e : adjacentTo(n))
        {
            if(index[e.to.num] == -1)
            {
                strongConnect(e.to, idx, index, low, stacked, comps, S);
                low[n.num] = Math.min(low[n.num],low[e.to.num]);
            }
            else if(stacked[e.to.num])
                low[n.num] = Math.min(low[n.num], index[e.to.num]);
        }
        if(low[n.num] == index[n.num])
        {
            Node w;
            HashSet<Node> C = new HashSet<Node>();
            do
            {
                w = S.pop();
                stacked[w.num] = false;
                C.add(w);
            }while(!w.equals(n));
            comps.push(C);
        }
    }
}
