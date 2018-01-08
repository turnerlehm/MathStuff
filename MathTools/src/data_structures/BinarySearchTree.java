package data_structures;

import java.util.NoSuchElementException;

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
        return root;
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
    }
}
