package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.Stack;

public class StackImpl<T> implements Stack<T>
{
    private class Node <data>
    {
        //Instance Variables
        private data data;
        private Node<data> next;

        //Constructor
        private Node (data data)
        {
            this.data = data;
            this.next = null;
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

            Node<data> otherNode = (Node<data>)o;
            if(this.data == otherNode.data)
            {
                //System.out.println("Documents/ data are the same");
                return true;
            }

            //System.out.println("Documents/ data are not the same");
            return false;
        }
    }
    //INSTANCE VARIABLES
    Node<T> head;
    int size = 0;

    //CONSTRUCTOR
    public StackImpl()
    {
        this.head = null;
    }


    @Override
    public void push(T element)
    {
        //add the key and value to a new Node
        Node<T> tempNode = new Node<>(element);

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
        //Increment the size of this stack
        this.size++;
    }

    @Override
    public T pop()
    {
        //If this stack is empty, return null
        if(this.head == null)
        {
            return null;
        }

        T tempData = this.head.data;
        this.head = this.head.next;
        this.size--;
        return tempData;
    }


    @Override
    public T peek()
    {
        return head != null ? head.data: null;
    }


    @Override
    public int size() {
        return this.size;
    }
    //Stack Instance Variables
}