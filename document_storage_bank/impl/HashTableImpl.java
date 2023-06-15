package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.HashTable;

public class HashTableImpl <Key, Value> implements HashTable<Key, Value>
{
    //Class LinkedListMap with class Node inside it
        private class LinkedListMap<K, V>
        {
        //Node Class
        private class Node <key, value>
        {
            //Instance Variables
            private key key;
            private value value;
            private Node<key, value> next;

            //Constructor
            private Node (key key, value value)
            {
                this.key = key;
                this.value = value;
                this.next = null;
            }

            private Node(key key)
            {
                this(key, null);
            }


            private boolean isEqual(Object o)
            {
                if(o == null)
                {
                    //System.out.println("o is null");
                    return false;
                }

                if(this.getClass() != o.getClass())
                {
                    //System.out.println("o is a different class");
                    return false;
                }

                Node<key, value> otherNode = (Node<key, value>)o;
                if(this.key == otherNode.key)
                {
                    //System.out.println("Documents/ data are the same");
                    return true;
                }

                //System.out.println("Documents/ data are not the same");
                return false;
            }
        }



        //Instance variables for class LinkedListMap
        private Node <K, V> head;

        //Constructor
        private LinkedListMap()
        {
            this.head = null;
        }

        private void add(K key, V value) //Adds a Key and Value to a Node and that Node to the Beginning of this LL
        {
            //add the key and value to a new Node
            Node<K, V> tempNode = new Node<>(key, value);

            //Determine if this is the first key and value in this LL or not
            if(this.head == null)
            {
                //Set the head to this new Node
                this.head = tempNode;
            }
            else
            {
                tempNode.next = this.head;
                this.head = tempNode;
            }
        }

        private boolean delete(K key)
        {
            //Determine the Node you want to find and then delete
            Node<K, V> tempNode = new Node<>(key);

            //Begin a traversal of the Nodes that are linked to this.head
            Node<K, V> current = this.head;
            if(current == null)
                return false;

            //If the first Node/ current in the LL equals the tempNode
            if(current.isEqual(tempNode))
            {
                this.head = current.next;
                return true;
            }
            //Look through the loop to determine if tempNode exists excluding the first Node
            while(current.next != null && !current.next.isEqual(tempNode))
            {
                current = current.next;
            }

            //This means we did not reach the end of the loop, thus current.next much == tempNode
            if(current.next != null)
            {
                //Delete the tempNode which is currently current.next and return true bc it was a success
                current.next = current.next.next;
                return true;
            }

            //There was no Node in the LL that contained the doc to delete
            return false;

        }

        private V find(K key)
        {
            //Return null if there are no elements in this LL
            if(this.head == null)
            {
                return null;
            }

            //Declare the tempNode we are looking to return and the current to the head of the LL
            Node<K, V> tempNode = new Node<>(key);
            Node<K, V> current = this.head;

            //If tempNode matches the head then is the first Node in the LL, so return the match bc we found it
            if(current.isEqual(tempNode))
            {
                return (V) current.value;
            }

            while(current.next != null && !current.next.isEqual(tempNode))
            {
                current = current.next;
            }
            //Found the match for tempNode
            if(current.next != null)
            {
                return (V)current.next.value;
            }
            //Reached the end of the list without finding the match for tempNode
            return null;
        }

        //If there is an existent key, replace the old value with the new value parameter and return oldValue, if there is no existent key, return null
        private V replace(K key, V value)
        {
            //Determine the Node you want to find and then delete
            Node<K, V> tempNode = new Node<>(key);

            //Begin a traversal of the Nodes that are linked to this.head
            Node<K, V> current = this.head;
            //Determine if this LL is empty
            if(head == null)
            {
                return null;
            }

            //If the first Node/ current in the LL equals the tempNode
            if(current.isEqual(tempNode))
            {
                V oldValue = this.head.value;
                this.head.value = value;
                return oldValue;
            }
            //Look through the loop to determine if tempNode exists excluding the first Node
            while(current.next != null && !current.next.isEqual(tempNode))
            {
                current = current.next;
            }

            //This means we did not reach the end of the loop, thus current.next much == tempNode
            if(current.next != null)
            {
                V oldValue = current.next.value;
                //Delete the tempNode which is currently current.next and return true bc it was a success
                current.next.value = value;
                return oldValue;
            }

            //There was no Node in the LL that contained the doc to delete
            return null;
        }

        private K getLastKey()
        {
            //Return the last key that was added
            return this.head.key;
        }

        private V getLastValue()
        {
            //Return the last key that was added
            return this.head.value;
        }

        private void deleteLast()
        {
            this.head = this.head.next;
        }

        private boolean isEmpty()
        {
            return head == null;
        }
    }

    //Instance variables
    private LinkedListMap<?, ?>[] table; //Needs to be a wild card type to ensure that the array(which usually checks at compile) will wait to compile time to check the type
    private int keysInArray;

    //Default Constructor
    public HashTableImpl()
    {
        this.table = new LinkedListMap[5];
        //Instantiate new LL in every slot of this.table
        for (int i = 0; i < this.table.length; i++) {
            this.table[i] = new LinkedListMap<>();
        }
    }

    //Methods

    private int hashFunction(Key key, int multiplier)
    {
        return (key.hashCode() & 0x7fffffff) % (this.table.length * multiplier);
    }

    private int hashFunction(Key key)
    {
        return this.hashFunction(key, 1);
    }

    @Override
    public Value get(Key k)
    {
        //Determine which LL in this.table is holding where this key is
        int indexInArray = this.hashFunction(k);
        LinkedListMap<Key, Value> tempLL = (LinkedListMap<Key, Value>) this.table[indexInArray]; //We know that all items in this "wild card array" are these LL with these generics

        //Search the tempLL for an Entry with a matching Key
        Value foundValue = tempLL.find(k);

        if(foundValue != null)
        {
            return foundValue;
        }
        return null;

    }
    @Override
    public Value put(Key k, Value v)
    {
        //Hash the key into an int for this.table
        int indexInArray = this.hashFunction(k);
        //Get the LL in this.table that the key belongs in based off the indexInArray
        LinkedListMap<Key, Value> tempLL = (LinkedListMap<Key, Value>) this.table[indexInArray];

        //If v is null, delete that Node from the LL
        if(v == null)
        {
            Value oldValue = tempLL.find(k);
            tempLL.delete(k);
            this.keysInArray--;
            return oldValue;
        }

        //Find if there is already an existent key or not, and replace the oldValue with v if there is
        Value oldValue = tempLL.replace(k, v);
        if (oldValue == null) {
            //If the key doesn't exist yet, add a new key and value pair to the LL
            //Determine if the array needs to be doubled
            if(this.table.length == (this.keysInArray / 4))
            {
                this.doubleArray();
            }

            tempLL.add(k, v);
            this.keysInArray++;
            return null;
        }
        //If the key is existent, then it's oldValue was replaced on line 56, now return the oldValue of that key
        return oldValue;
    }
    
    @Override
    public boolean containsKey(Key key)
    {
        //Determine which LL in this.table is holding where this key is
        int indexInArray = this.hashFunction(key);
        LinkedListMap<Key, Value> tempLL = (LinkedListMap<Key, Value>) this.table[indexInArray];
        return tempLL.find(key) != null;
    }

    private void doubleArray()
    {
        //Make a new LLMap that's double the array size as the last one(this.table)
        LinkedListMap<?, ?>[] newLLArray = new LinkedListMap[this.table.length * 2];
        for(int i = 0; i < newLLArray.length; i++)
        {
            newLLArray[i] = new LinkedListMap<>();
        }
        //Go through the old this.table, array index by index and get every value and
        for(int i = 0; i < this.table.length; i++)
        {
            while(this.table[i].isEmpty() == false)
            {
                //Make a tempNode for the current Node being taken out of this.table
                Key tempKey = (Key) this.table[i].getLastKey();
                Value tempValue = (Value) this.table[i].getLastValue();
                this.table[i].deleteLast();
                //ReHash the current key
                int newIndex = this.hashFunction(tempKey, 2);

                //Get the LL in the newLLArray where this key and value have been reHashed to
                LinkedListMap<Key, Value> tempLL = (LinkedListMap<Key, Value>) newLLArray[newIndex];

                //Add the key and value into their new slot in the tempLL which is pointing to an LL in the newLLArray
                tempLL.add(tempKey, tempValue);
            }
        }
        //Set this.table = to the new LLMap
        this.table = newLLArray;
    }

    //METHOD USED ONLY FOR TESTING this.doubleArray

    private int sizeOfTable()
    {
        return this.table.length;
    }

}