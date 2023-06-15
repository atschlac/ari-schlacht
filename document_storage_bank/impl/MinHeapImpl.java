package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.MinHeap;

import java.util.NoSuchElementException;

public class MinHeapImpl<E extends Comparable<E>> extends MinHeap<E>
{
    public MinHeapImpl()
    {
        this.elements = (E[]) new Comparable[10];
    }

    @Override
    public void reHeapify(E element)
    {
        try
        {
            int elementIndex = this.getArrayIndex(element);
            this.upHeap(elementIndex);
            this.downHeap(elementIndex);
        }
        catch (NoSuchElementException e)
        {
           return;
        }

    }

        @Override
        protected int getArrayIndex(E element)
        {
            for(int i = 1; i < this.count + 1; i++)
            {
                if(this.elements[i].equals(element))
                {
                    return i;
                }
            }
            throw new NoSuchElementException();
            //Doesn't exist
            //return 0;
        }

        @Override
        protected void doubleArraySize()
        {
            E[] newElementsArray = (E[]) new Comparable[this.elements.length * 2];

            for(int i = 0; i < this.elements.length; i++)
            {
                newElementsArray[i] = this.elements[i];
            }

            this.elements = newElementsArray;
        }
}
