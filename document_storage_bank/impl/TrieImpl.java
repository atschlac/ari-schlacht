package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.Trie;
import edu.yu.cs.com1320.project.stage5.impl.DocumentImpl;

import java.net.URI;
import java.util.*;

public class TrieImpl <Value> implements Trie<Value>
{
    //Node Class
    public static class Node<Value>
    {
        protected Set<Value> val = new HashSet<>();
        protected Node<?>[] links = new Node[TrieImpl.alphabetSize];
    }
    //Instance Variables
    private static final int alphabetSize = 256; // extended ASCII
    private Node root; // root of trie

    //Constructors
    public TrieImpl()
    {
        this.root = new Node<>();
    }
    @Override
    public void put(String key, Value val)
    {
        this.validate(key);
        //Do nothing if the value is null
        if(val != null)
        {
            this.root = put(this.root, key, val, 0);
        }
    }

        private void validate(String str)
        {
            if(str == null)
                throw new IllegalArgumentException();
        }

        private Node put(Node x, String key, Value val, int d)
        {
            //create a new node
            if (x == null)
            {
                x = new Node();
            }
            //we've reached the last node in the key,
            //set the value for the key and return the node
            if (d == key.length())
            {
                //x. val is a set<Value>
                x.val.add(val);
                return x;
            }
            //proceed to the next node in the chain of nodes that
            //forms the desired key
            char c = key.charAt(d);
            x.links[c] = this.put(x.links[c], key, val, d + 1);
            return x;
        }

    @Override
    public List<Value> getAllSorted(String key, Comparator<Value> comparator)
    {
        this.validate(key);
        Node x = this.get(this.root, key, 0);
        if (x == null)
        {
            return new LinkedList<>();
        }
        //Add x.val to a List
        List<Value> tempLL = new LinkedList<>(x.val);
        tempLL.sort(comparator);
        return tempLL;
    }

        private Node get(Node x, String key, int d)
        {
            //link was null - return null, indicating a miss
            if (x == null)
            {
                return null;
            }
            //we've reached the last node in the key,
            //return the node
            if (d == key.length())
            {
                return x;
            }
            //proceed to the next node in the chain of nodes that
            //forms the desired key
            char c = key.charAt(d);
            return this.get(x.links[c], key, d + 1);
        }

    @Override
    public List<Value> getAllWithPrefixSorted(String prefix, Comparator<Value> comparator)
    {
        this.validate(prefix);
        Set<Value> setOfValues = new HashSet<>();
        collectAllValues(get(root, prefix, 0), prefix, setOfValues);

        List<Value> listOfValues = new LinkedList<>(setOfValues);
        listOfValues.sort(comparator);

        return listOfValues;
    }
            private void collectAllValues(Node x, String pre, Set<Value> setOfValues)
            {
             if (x == null)
                 return;

             if (x.val != null)
             {
                 setOfValues.addAll(x.val);
             }

             for (char c = 0; c < alphabetSize; c++)
             {
                 collectAllValues(x.links[c], pre + c, setOfValues);
             }
        }

    @Override
    public Set<Value> deleteAllWithPrefix(String prefix)
    {
        this.validate(prefix);
        Set<Value> oldValues = new HashSet<>();
        Node currentNode = get(root, prefix, 0);
        //Add all the values within this subtree to oldValues
        collectAllValues(currentNode, prefix, oldValues);
        //Delete the subtree of currentNode
        currentNode.links = new Node[TrieImpl.alphabetSize];
        //Delete the current Node and add it's values to the oldValues set
        this.deleteAll(this.root, prefix, 0, oldValues);

        return oldValues;
    }

    @Override
    public Set<Value> deleteAll(String key)
    {
        this.validate(key);
        Set<Value> deletedValues = new HashSet<>();
        this.root = deleteAll(this.root, key, 0, deletedValues);
        return deletedValues;
    }
        private Node deleteAll(Node x, String key, int d, Set<Value> deletedValues)
        {
            if (x == null)
            {
                return null;
            }
            //we're at the node to del - set the val to null
            if (d == key.length())
            {
                deletedValues.addAll(x.val);
                x.val.clear();
            }
            //continue down the trie to the target node
            else
            {
                char c = key.charAt(d);
                x.links[c] = this.deleteAll(x.links[c], key, d + 1, deletedValues);
            }
            //this node has a val – do nothing, return the node
            if (x.val != null)
            {
                return x;
            }
            //remove subtrie rooted at x if it is completely empty
            for (int c = 0; c <TrieImpl.alphabetSize; c++)
            {
                if (x.links[c] != null)
                {
                    return x; //not empty
                }
            }
            //empty - set this link to null in the parent
            return null;
        }

    @Override
    public Value delete(String key, Value val)
    {
        this.validate(key);
        boolean exists = false;

        if(this.get(this.root, key, 0) != null)
             exists = this.get(this.root, key, 0).val.contains(val);

        this.delete(this.root, key, 0, val);

        if(exists == false)
            return null;
        return val;
    }
        private Node delete(Node x, String key, int d, Value val)
        {
            if (x == null)
            {
                return null;
            }
            if (d == key.length())
            {
                x.val.remove(val);
            }
            //continue down the trie to the target node
            else
            {
                char c = key.charAt(d);
                x.links[c] = this.delete(x.links[c], key, d + 1, val);
            }
            //this node has a val – do nothing, return the node
            if (x.val.isEmpty() == false)
            {
                return x;
            }
            //remove subtrie rooted at x if it is completely empty
            for (int c = 0; c <TrieImpl.alphabetSize; c++)
            {
                if (x.links[c] != null)
                {
                    return x; //not empty
                }
            }
            //empty - set this link to null in the parent
            return null;
        }
}