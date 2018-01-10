package data_structures;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Queue;

public class BinarySearchTree<K,V>
{
    private Node root;
    private int size;
    private class Node
    {
        K key;
        V data;
        Node parent;
        Node left, right;
        int size;
        public Node(K key, V data, int size)
        {
            this.key = key;
            this.data = data;
            this.size = size;
        }
    }

    public BinarySearchTree(){}

    public void insert(K key, V val)
    {
        if(key == null)
            throw new IllegalArgumentException("null key");
        if(val == null)
        {
            remove(key);
            return;
        }
        root = insert(root, key, val);
    }

    private Node insert(Node root, K key, V val)
    {
        if(root == null)
        {
            root = new Node(key, val, 1);
            return root;
        }
        int cmp = key.toString().compareTo(root.key.toString());
        if(cmp < 0)
        {
            root.left = insert(root.left, key, val);
            root.left.parent = root;
        }
        else if(cmp > 0)
        {
            root.right = insert(root.right, key, val);
            root.right.parent = root;
        }
        else
            root.data = val;
        root.size = 1 + size(root.left) + size(root.right);
        assert check();
        return root;
    }

    public V get(K key)
    {
        return get(root, key);
    }

    private V get(Node root, K key)
    {
        Node cur = root;
        while(cur != null)
        {
            int cmp = key.toString().compareTo(cur.key.toString());
            if(cmp == 0)
                return cur.data;
            else if(cmp < 0)
                cur = cur.left;
            else
                cur = cur.right;
        }
        return null;
    }

    public int size()
    {
        return size(root);
    }

    private int size(Node root)
    {
        if(root == null)
            return 0;
        return root.size;
    }

    public boolean isEmpty()
    {
        return size() == 0;
    }

    public K min()
    {
        if(isEmpty())
            throw  new NoSuchElementException("empty tree");
        return min(root).key;
    }

    public K max()
    {
        if(isEmpty())
            throw new NoSuchElementException("empty tree");
        return max(root).key;
    }

    public Node max(Node root)
    {
        if(root.right == null)
            return root;
        return max(root.right);
    }

    private Node min(Node root)
    {
        Node cur = root;
        while(cur.left != null)
            cur = cur.left;
        return cur;
    }

    private void replaceParent(Node root, Node n)
    {
        if(root.parent != null)
        {
            if(root.equals(root.parent.left))
                root.parent.left = n;
            else
                root.parent.right = n;
        }
        if(n != null)
            n.parent = root.parent;
    }

    public boolean contains(K key)
    {
        if(key == null)
            throw new IllegalArgumentException("null key");
        if(isEmpty())
            throw new NoSuchElementException("empty tree");
        return contains(root, key) != null;
    }

    private Node contains(Node root, K key)
    {
        Node cur = root;
        while(cur != null)
        {
            int cmp = key.toString().compareTo(cur.key.toString());
            if(cmp == 0)
                return cur;
            else if(cmp < 0)
                cur = cur.left;
            else
                cur = cur.right;
        }
        return cur;
    }

    public void remove(K key)
    {
        if(key == null)
            throw new IllegalArgumentException("null key");
        if(isEmpty())
            throw new NoSuchElementException("empty tree");
        if(!contains(key))
            return;
        remove(root, key);
        assert check();
    }

    private void remove(Node root, K key)
    {
        int cmp = key.toString().compareTo(root.key.toString());
        if(cmp < 0)
            remove(root.left, key);
        else if(cmp > 0)
            remove(root.right, key);
        else
        {
            if(root.left != null && root.right != null)
            {
                Node succ = min(root.right);
                root.key = succ.key;
                remove(succ, succ.key);
            }
            else if(root.left != null)
                replaceParent(root, root.left);
            else if(root.right != null)
                replaceParent(root, root.right);
            else
                replaceParent(root, null);
        }
        root.size = size(root.left) + size(root.right) + 1;
    }

    public int height()
    {
        return height(root);
    }

    private int height(Node root)
    {
        if(root == null)
            return -1;
        return 1 + Math.max(height(root.left),height(root.right));
    }

    public K select(int k)
    {
        if(k < 0 || k >= size())
            throw new IndexOutOfBoundsException("" + k);
        Node x = select(root, k);
        return x.key;
    }

    private Node select(Node root, int k)
    {
        if(root == null)
            return null;
        int t = size(root.left);
        if(t > k)
            return select(root.left, k);
        else if(t < k)
            return select(root.right, k - t - 1);
        else
            return root;
    }

    public int rank(K key)
    {
        if(key == null)
            throw new IllegalArgumentException("null key");
        return rank(root, key);
    }

    private int rank(Node root, K key)
    {
        if(root == null)
            return 0;
        int cmp = key.toString().compareTo(root.key.toString());
        if(cmp < 0)
            return rank(root.left, key);
        else if(cmp > 0)
            return 1 + size(root.left) + rank(root.right, key);
        else
            return size(root.left);
    }

    public Iterable<K> keys()
    {
        if(isEmpty())
            return new LinkedList<K>();
        return keys(min(), max());
    }

    private Iterable<K> keys(K min, K max)
    {
        if(min == null)
            throw new IllegalArgumentException("null min key");
        if(max == null)
            throw new IllegalArgumentException("null max key");
        LinkedList<K> queue = new LinkedList<K>();
        keys(root, queue, min, max);
        return queue;
    }

    private void keys(Node root, LinkedList<K> queue, K min, K max)
    {
        if(root == null)
            return;
        int cmpl = min.toString().compareTo(root.key.toString());
        int cmph = max.toString().compareTo(root.key.toString());
        if(cmpl < 0)
            keys(root.left, queue, min, max);
        if(cmpl <= 0 && cmph >= 0)
            queue.add(root.key);
        if(cmph > 0)
            keys(root.right, queue, min, max);
    }

    private boolean check()
    {
        if(!isBST())
            System.out.println("Not in sym. order");
        if(!sizeConsistent())
            System.out.println("Subtree counts not consistent");
        if(!rankConsistent())
            System.out.println("Ranks not consistent");
        return isBST() && sizeConsistent() && rankConsistent();
    }

    private boolean isBST()
    {
        return isBST(root, null, null);
    }

    private boolean isBST(Node root, K min, K max)
    {
        if(root == null)
            return true;
        if(min != null && root.key.toString().compareTo(min.toString()) <= 0)
            return false;
        if(max != null && root.key.toString().compareTo(max.toString()) >= 0)
            return false;
        return isBST(root.left, min, root.key) && isBST(root.right, root.key, max);
    }

    private boolean sizeConsistent()
    {
        return sizeConsistent(root);
    }

    private boolean sizeConsistent(Node root)
    {
        if(root == null)
            return true;
        if(root.size != size(root.left) + size(root.right) + 1)
            return false;
        return sizeConsistent(root.left) && sizeConsistent(root.right);
    }

    private boolean rankConsistent()
    {
        for(int i = 0; i < size(); i++)
            if(i != rank(select(i)))
                return false;
        for(K key : keys())
            if(key.toString().compareTo(select(rank(key)).toString()) != 0)
                return false;
        return true;
    }

    private void traverseStore(Node root, ArrayList<Node> nodes)
    {
        if(root == null)
            return;
        traverseStore(root.left, nodes);
        nodes.add(root);
        traverseStore(root.right, nodes);
    }

    private Node buildTree(ArrayList<Node> nodes, int start, int end)
    {
        if(start > end)
            return null;
        int mid = (start + end) / 2;
        Node m = nodes.get(mid);
        m.left = buildTree(nodes, start, mid -1);
        m.right = buildTree(nodes, mid + 1, end);
        if(m.left != null)
            m.left.parent = m;
        if(m.right != null)
            m.right.parent = m;
        m.size = 1 + size(m.left) + size(m.right);
        return m;
    }

    public void balance()
    {
        ArrayList<Node> nodes = new ArrayList<Node>();
        traverseStore(root, nodes);
        int n = nodes.size();
        root = buildTree(nodes, 0, n - 1);
        root.parent = null;
    }

    public static void main(String[] args)
    {
        BinarySearchTree<Integer, String> bst = new BinarySearchTree<Integer, String>();
        bst.insert(2, "A");
        bst.insert(1, "B");
        bst.insert(3, "C");
        System.out.println(bst.size());
        bst.remove(3);
        System.out.println(bst.size());
        bst.insert(3, "C");
        System.out.println(bst.size());
        System.out.println(bst.get(3));
        bst.insert(4, "D");
        bst.insert(5, "E");
        bst.balance();
        System.out.println(bst.size());
    }
}
