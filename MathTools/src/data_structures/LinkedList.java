package data_structures;

import java.util.Collection;
import java.util.Iterator;

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
	
	@Override
	public Iterator<E> iterator() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	private class LinkedIterator<E> implements Iterator<E>
	{

		@Override
		public boolean hasNext() 
		{
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public E next() 
		{
			// TODO Auto-generated method stub
			return null;
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
}
