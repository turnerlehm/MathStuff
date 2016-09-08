package data_structures;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;

public class BigLinkedList<E> implements Iterable<E> 
{
	Node head;
	Node tail;
	BigInteger size;
	
	public BigLinkedList()
	{
		head = new Node(null);
		tail = new Node(null);
		size = BigInteger.ZERO;
		head.next = tail;
		tail.prev = head;
	}
	
	public void add(E e)
	{
		Node N = new Node(e);
		if(size.equals(BigInteger.ZERO))
		{
			N.next = tail;
			N.prev = head;
			head.next = N;
			tail.prev = N;
			size = size.add(BigInteger.ONE);
		}
		else
		{
			N.next = tail;
			N.prev = tail.prev;
			tail.prev.next = N;
			tail.prev = N;
			size = size.add(BigInteger.ONE);
		}
	}
	
	//we want the index of the new node to be the specified insertion index
	//to do this we add the new node BEFORE the specified index instead of after
	public void add(int index, E e)
	{
		BigInteger idx = new BigInteger("" + index);
		if(idx.compareTo(BigInteger.ZERO) < 0 || idx.compareTo(size) >= 0)
			throw new IndexOutOfBoundsException("" + idx);
		else
		{
			//closer to the head of the list
			if(BigInteger.ZERO.subtract(idx).abs().compareTo(size.subtract(idx)) < 0)
			{
				Node cur = head.next;
				BigInteger i = BigInteger.ZERO;
				while(!i.equals(idx))
				{
					cur = cur.next;
					i = i.add(BigInteger.ONE);
				}
				Node N = new Node(e);
				N.next = cur;
				N.prev = cur.prev;
				cur.prev.next = N;
				cur.prev = N;
			}
			else//closer to the tail of the list
			{
				Node cur = tail.prev;
				BigInteger i = size.subtract(BigInteger.ONE);
				while(!i.equals(idx))
				{
					cur = cur.prev;
					i = i.subtract(BigInteger.ONE);
				}
				Node N = new Node(e);
				N.next = cur;
				N.prev = cur.prev;
				cur.prev.next = N;
				cur.prev = N;
			}
			size = size.add(BigInteger.ONE);
		}
	}
	
	public void add(BigInteger index, E e)
	{
		if(index.compareTo(BigInteger.ZERO) < 0 || index.compareTo(size) >= 0)
			throw new IndexOutOfBoundsException("" + index);
		else
		{
			if(BigInteger.ZERO.subtract(index).abs().compareTo(size.subtract(index)) < 0)
			{
				Node cur = head.next;
				BigInteger i = BigInteger.ZERO;
				while(!i.equals(index))
				{
					cur = cur.next;
					i = i.add(BigInteger.ONE);
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
				BigInteger i = size.subtract(BigInteger.ONE);
				while(!i.equals(index))
				{
					cur = cur.prev;
					i = i.subtract(BigInteger.ONE);
				}
				Node N = new Node(e);
				N.next = cur;
				N.prev = cur.prev;
				cur.prev.next = N;
				cur.prev = N;
			}
			size = size.add(BigInteger.ONE);
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
	
	public void addAll(BigInteger index, Collection<? extends E> c)
	{
		for(E item : c)
		{
			add(index, item);
			index = index.add(BigInteger.ONE);
		}
	}
	
	public void addFirst(E e)
	{
		Node N = new Node(e);
		if(size.equals(BigInteger.ZERO))//NOTE: This step is not needed, it is simply here for demonstration
		{
			N.next = tail;
			N.prev = head;
			head.next = N;
			tail.prev = N;
		}
		else
		{
			N.next = head.next;
			N.prev = head;
			head.next.prev = N;
			head.next = N;
		}
		size = size.add(BigInteger.ONE);
	}
	
	public void addLast(E e)
	{
		Node N = new Node(e);
		N.next = tail;
		N.prev = tail.prev;
		tail.prev.next = N;
		tail.prev = N;
		size = size.add(BigInteger.ONE);
	}
	
	public void clear()
	{
		head.next.prev = null;//simply remove references to the actual beginning/end of the list
		head.next = tail;
		tail.prev.next = null;
		tail.prev = head;
		size = BigInteger.ZERO;
	}
	
	public boolean contains(Object o)
	{
		if(o == null)
			return false;
		if(size.equals(BigInteger.ZERO))
			return false;
		else
		{
			Node cur = head.next;
			while(cur.next != tail)
			{
				if(o.equals(cur.data))
					return true;
				cur = cur.next;
			}
			return false;
		}
	}
	
	public E element()
	{
		return head.next.data;
	}
	
	public E get(int index)
	{
		BigInteger idx = new BigInteger("" + index);
		if(idx.compareTo(BigInteger.ZERO) < 0 || idx.compareTo(size) >= 0)
			throw new IndexOutOfBoundsException("" + idx);
		else
		{
			if(BigInteger.ZERO.subtract(idx).abs().compareTo(size.subtract(idx)) < 0)
			{
				Node cur = head.next;
				BigInteger i = BigInteger.ZERO;
				while(!i.equals(idx))
				{
					cur = cur.next;
					i = i.add(BigInteger.ONE);
				}
				return cur.data;
			}
			else
			{
				Node cur = tail.prev;
				BigInteger i = size.subtract(BigInteger.ONE);
				while(!i.equals(idx))
				{
					cur = cur.prev;
					i = i.subtract(BigInteger.ONE);
				}
				return cur.data;
			}
		}
	}
	
	@Override
	public Iterator<E> iterator() 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	private class BigListIterator<E> implements Iterator<E>
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
		
		public Node(E e)
		{
			this.data = e;
		}
	}

}
