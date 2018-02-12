package data_structures;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HashTable<K,V>
{
    private ArrayList<Entry<K,V>> table;
    private int buckets;
    private int size;
    private int threshold;

    public HashTable()
    {
        table = new ArrayList<>();
        buckets = 10;
        size = 0;
        for(int i = 0; i < buckets; i++)
            table.add(null);
        threshold = (int)(buckets * 0.75);
    }

    public int size()
    {
        return size;
    }

    public boolean isEmpty(){return size == 0;}

    private int index(K key)
    {
        int hash = 0;
        for(byte b : key.toString().getBytes())
            hash ^= (hash << 5) ^ (hash >> 2) ^ b;
        return (hash & Integer.MAX_VALUE) % buckets;
    }

    public V get(K key)
    {
        int idx = index(key);
        Entry<K,V> e = table.get(idx);
        while(e != null)
        {
            if(e.key.equals(key))
                return e.value;
            e = e.next;
        }
        return null;
    }

    public V put(K key, V value)
    {
        int idx = index(key);
        Entry<K,V> e = table.get(idx);
        while(e != null)
        {
            if(e.key.equals(key))
            {
                V old = e.value;
                e.value = value;
                return old;
            }
            e = e.next;
        }
        e = table.get(idx);
        Entry<K,V> E = new Entry<K,V>(key, value);
        E.next = e;
        table.set(idx, E);
        size++;
        if((double)size/buckets >= 0.75)
        {
            ArrayList<Entry<K,V>> temp = table;
            table = new ArrayList<>();
            buckets = 2 * buckets + 1;
            size = 0;
            for(int i = 0; i < buckets; i++)
                table.add(null);
            for(Entry<K,V> entry : temp)
            {
                while(entry != null)
                {
                    put(entry.key,entry.value); //not good
                    entry = entry.next;
                }
            }
        }
        return value;
    }

    public static void main(String[] args)
    {
        HashTable<String, Integer> table = new HashTable<>();
        String[] students = new String[50];
        for(int i = 0; i < students.length; i++)
            students[i] = "Student" + i;
        for(int i = 0; i < students.length; i++)
            table.put(students[i],i);
        System.out.println("Hello");
    }
}

class Entry<K,V>
{
    K key;
    V value;
    Entry<K,V> next;

    public Entry(K key, V value)
    {
        this.key = key;
        this.value = value;
    }
}
