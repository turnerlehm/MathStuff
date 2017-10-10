package data_structures;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class LinkedList<E> implements Iterable<E>
{
	private int size;
	private Node head;
	private Node tail;
	public LinkedList() 
	{
		size = 0;
		head = new Node(null);
		tail = new Node(null);
		head.next = tail;
		tail.prev = head;
	}
	
	public void add(E e)
	{
		Node N = new Node(e);
		if(size == 0)
		{
			N.next = this.tail;
			N.prev = this.head;
			tail.prev = N;
			head.next = N;
			size++;
		}
		else
		{
			N.prev = tail.prev;
			N.next = tail;
			tail.prev.next = N;
			tail.prev = N;
			size++;
		}
	}
	
	public void add(int index, E e)
	{
		if(index < 0 || index >= size)
			throw new IndexOutOfBoundsException("" + index);
		else
		{
			if(Math.abs(0 - index) < (size - index))
			{
				Node cur = head.next;
				int i = 0;
				while(i != index)
				{
					cur = cur.next;
					i++;
				}
				Node N = new Node(e);
				N.next = cur;
				N.prev = cur.prev;
				cur.prev.next = N;
				cur.prev = N;
			}
			else
			{
				Node cur = tail.prev;
				int i = size - 1;
				while(i != index)
				{
					cur = cur.prev;
					i--;
				}
				Node N = new Node(e);
				N.next = cur;
				N.prev = cur.prev;
				cur.prev.next = N;
				cur.prev = N;
			}
			size++;
		}
	}
	
	public void addAll(Collection<? extends E> c)
	{
		for(E item : c)
			add(item);
	}
	
	public void addAll(int index, Collection<? extends E> c)
	{
		for(E item : c)
		{
			add(index, item);
			index++;
		}
	}

	public void addFirst(E e)
	{
		Node N = new Node(e);
		N.next = head.next;
		N.prev = head;
		head.next.prev = N;
		head.next = N;
		size++;
	}

	public void addLast(E e)
	{
		Node N = new Node(e);
		N.next = tail;
		N.prev = tail.prev;
		tail.prev.next = N;
		tail.prev = N;
		size++;
	}

	public void clear()
	{
        head.next.prev = null;
        head.next = tail;
        tail.prev.next = null;
        tail.prev = head;
        size = 0;
	}

	public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    public boolean contains(Object o)
    {
        if(o == null)
            return false;
        else if(size == 0)
            return false;
        else
        {
            boolean found = false;
            Node cur = head.next;
            while(cur != tail && !found)
            {
                found = o.equals(cur.data);
                cur = cur.next;
            }
            return found;
        }
    }

    public E element()
    {
        return head.next.data;
    }

    public E get(int index)
    {
        if(index < 0 || index >= size)
            throw new IndexOutOfBoundsException("" + index);
        else
        {
            if(Math.abs(0 - index) < size - index)//closer to head
            {
                Node cur = head.next;
                int i = 0;
                while(i++ != index)
                    cur = cur.next;
                return cur.data;
            }
            else
            {
                Node cur = tail.prev;
                int i = size - 1;
                while(i-- != index)
                    cur = cur.next;
                return cur.data;
            }
        }
    }

    public E getFirst()
    {
        return element();
    }

    public E getLast()
    {
        return tail.prev.data;
    }

    public int indexOf(Object o)
    {
        if(!contains(o))
            return -1;
        else
        {
            Node cur = head.next;
            int index = 0;
            while(!o.equals(cur.data))
            {
                index++;
                cur = cur.next;
            }
            return index;
        }
    }

    public int lastIndexOf(Object o)
    {
        if(!contains(o))
            return -1;
        else
        {
            Node cur = tail.prev;
            int index = size - 1;
            while(!o.equals(cur.data))
            {
                index--;
                cur = cur.prev;
            }
            return index;
        }
    }

    public boolean offer(E e)
    {
        addLast(e);
        return true;
    }

    public boolean offerFirst(E e)
    {
        addFirst(e);
        return true;
    }

    public boolean offerLast(E e)
    {
        return offer(e);
    }

    public E peek()
    {
        return getFirst();
    }

    public E peekFirst()
    {
        return peek();
    }

    public E peekLast()
    {
        return getLast();
    }

    public E poll()//retrieve and remove first element of the list
    {
        if(size == 0)
            return null;
        else if(size == 1)
        {
            E data = head.next.data;
            head.next = tail;
            tail.prev = head;
            size--;
            return data;
        }
        else
        {
            E data = head.next.data;
            head.next = head.next.next;
            head.next.prev = head;
            size--;
            return data;
        }
    }

    public E pollFirst()
    {
        return poll();
    }

    public E pollLast()
    {
        if(size == 0)
            return null;
        else if(size == 1)
        {
            E data = tail.prev.data;
            tail.prev = head;
            head.next = tail;
            size--;
            return data;
        }
        else
        {
            E data = tail.prev.data;
            tail.prev = tail.prev.prev;
            tail.prev.next = tail;
            size--;
            return data;
        }
    }

    public E pop()
    {
        return poll();
    }

    public void push(E e)
    {
        addFirst(e);
    }

    public E remove()
    {
        return poll();
    }

    public E remove(int index)
    {
        if(index < 0 || index >= size)
            throw new IndexOutOfBoundsException("" + index);
        else if(Math.abs(0 - index) < size - index)
        {
            Node cur = head.next;
            int i = 0;
            while(i++ != index)
                cur = cur.next;
            //E data = cur.data;
            cur.prev.next = cur.next;
            cur.next.prev = cur.prev;
            //cur = null;
            size--;
            //return data;
            return cur.data; //will this cause any sort of mem leak, or will gc see that it's no longer referenced in this scope?
        }
        else
        {
            Node cur = tail.prev;
            int i = size - 1;
            while(i-- != index)
                cur = cur.prev;
            //E data = cur.data;
            cur.prev.next = cur.next;
            cur.next.prev = cur.prev;
            size--;
            //return data;
            return cur.data;
        }
    }

    public boolean remove(Object o)
    {
        boolean found = contains(o);
        if(found)
            remove(indexOf(o));
        return found;
    }

    public E removeFirst()
    {
        return pop();
    }

    public boolean removeFirstOccurence(Object o)
    {
        return remove(o);
    }

    public E removeLast()
    {
        return pollLast();
    }

    public boolean removeLastOccurence(Object o)
    {
        boolean found = contains(o);
        if(found)
            remove(lastIndexOf(o));
        return found;
    }

    public E set(int index, E e)
    {
        if(index < 0 || index >= size)
            throw new IndexOutOfBoundsException("" + index);
        else if(Math.abs(0 - index) < size - index)
        {
            Node cur = head.next;
            int i = 0;
            while(i++ != index)
                cur = cur.next;
            E data = cur.data;
            cur.data = e;
            return data;
        }
        else
        {
            Node cur = tail.prev;
            int i = size - 1;
            while(i-- != index)
                cur = cur.prev;
            E data = cur.data;
            cur.data = e;
            return data;
        }
    }

    public int size()
    {
        return size;
    }

    public Object[] toArray()
    {
        Object[] ret = new Object[size];
        Node cur = head.next;
        int i = 0;
        while(cur != tail)
        {
            ret[i] = cur.data;
            i++;
            cur = cur.next;
        }
        return ret;
    }

    public <T> T[] toArray(T[] a)
    {
        //not currently implemented or necessary
        return a;
    }

    @Override
    public String toString()
    {
        String ret = "[";
        Node cur = head.next;
        while(cur != tail)
        {
            if(cur.next == tail)
                ret += cur.data.toString();
            else
                ret += cur.data.toString() + ",";
            cur = cur.next;
        }
        return ret += "]";
    }

	@Override
	public Iterator<E> iterator() 
	{
		return new LinkedIterator(head.next, tail);
	}

	private class LinkedIterator implements Iterator<E>
	{
        private Node cur;
        private Node tail;

        public LinkedIterator(Node cur, Node tail)
        {
            if(cur == null || tail == null)
                throw new IllegalArgumentException("Current and tail nodes must not be null");
            else
            {
                this.cur = cur;
                this.tail = tail;
            }
        }
		@Override
		public boolean hasNext() 
		{
		    return cur != tail;
		}

		@Override
		public E next() 
		{
			E data = cur.data;
			cur = cur.next;
			return data;
		}
		
	}
	
	private class Node
	{
		Node next;
		Node prev;
		E data;
		
		public Node(E data)
		{
			this.data = data;
		}
	}


	public static void main(String... args)
    {
        LinkedList<String> myList = new LinkedList<String>();
        myList.add("1");
        myList.add("2");
        System.out.println(myList.toString());
        myList.clear();
        System.out.println(myList.toString());
        myList.add("1");
        myList.add("2");
        System.out.println("Adding \"3\" at index = 0 (the start of the list)");
        myList.add(0, "3");
        System.out.println(myList.toString());
        System.out.println("mylist.contains(\"4\"): " + myList.contains("4"));
        System.out.println("Adding \"4\" to list.");
        myList.add("4");
        System.out.println("mylist.contains(\"4\"): " + myList.contains("4"));
        System.out.println("Removing \"3\" from the list using index");
        myList.remove(0);
        System.out.println(myList.toString());
        System.out.println("Add back \"3\" and remove by key");
        myList.add("3");
        System.out.println(myList.toString());
        myList.remove("3");
        System.out.println(myList.toString());
        System.out.println("Let's put \"3\" right where it belongs (between 2 and 4)");
        myList.add(2, "3");
        System.out.println(myList.toString());
        System.out.println("Let's trying removing \"5\" from our list");
        System.out.println("myList.remove(\"5\") should return false: " + myList.remove("5"));
        String[] test = {"1","5","6","7","8","9","cat", "dog"};
        System.out.println("Let's try tacking on an array of strings");
        System.out.println("Array we're adding: " + Arrays.deepToString(test));
        myList.addAll(Arrays.asList(test));
        System.out.println("Our list after adding the array: " + myList.toString());
        String[] test2 = {"elephant", "mouse"};
        System.out.println("Let's add another array, this time in the middle.");
        System.out.println("Array we're adding: " + Arrays.deepToString(test2));
        myList.addAll(4,Arrays.asList(test2));
        System.out.println("Our list after adding the array starting from index = 4: " + myList.toString());
        System.out.println("Let's get rid of that second \"1\"");
        myList.removeLastOccurence("1");
        System.out.println(myList.toString());
        System.out.println("And now let's test out that fancy iterator");
        for(String s : myList)
            System.out.println(s);
    }
}
