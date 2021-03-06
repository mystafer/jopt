/*
 * Copyright 2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jopt.js.spi.util;

import java.util.EmptyStackException;

import org.apache.commons.collections.primitives.ArrayIntList;

/**
 * A primitive int based Stack.
 *
 * @author Apache Directory Project
 * @since Commons Primitives 1.1
 * @version $Revision: 1.3 $ $Date: 2006/06/23 21:06:06 $
 */
public class IntStack
{
    /** the underlying dynamic primitive backing store */
    private ArrayIntList list = new ArrayIntList() ;
    
    
    public IntStack()
    {
    }
    
    
    public IntStack( int[] numbas )
    {
        for ( int ii = 0; ii < numbas.length; ii++ )
        {    
            list.add( numbas[ii] ) ;
        }
    }
    

    /**
     * Tests if this stack is empty.
     * 
     * @return true if and only if this stack contains no ints; false otherwise
     */
    public boolean empty()
    {
        return list.isEmpty() ;
    }

    
    /**
     * Looks at the int at the top of this stack without removing it from 
     * the stack.
     * 
     * @return int at the top of this stack (last int in ArrayIntList)
     * @throws EmptyStackException if this stack is empty
     */
    public int peek()
    {
        if ( list.isEmpty() )
        {
            throw new EmptyStackException() ;
        }
        
        return list.get( list.size() - 1 ) ;
    }

    
    /**
     * Return the n'th int down the stack, where 0 is the top element and
     * [size()-1] is the bottom element.
     *
     * @param n the element index
     * @return the element at the index
     * @throws EmptyStackException if the stack is empty
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    public int peek( int n )
    {
        if ( list.isEmpty() )
        {
            throw new EmptyStackException() ;
        }

        return list.get( list.size() - n - 1 ) ;
    }


    /**
     * Removes the int at the top of this stack and returns that object as the 
     * value of this function.
     * 
     * @return int at the top of this stack (last int in ArrayIntList)
     * @throws EmptyStackException if this stack is empty
     */
    public int pop()
    {
        if ( list.isEmpty() )
        {
            throw new EmptyStackException() ;
        }
        
        return list.removeElementAt( list.size() - 1 ) ;
    }

    
    /**
     * Pushes an int item onto the top of this stack.
     * 
     * @param item the int item to push onto this stack
     * @return the item argument for call chaining
     */
    public int push( int item )
    {
        list.add( item ) ;
        return item ;
    }
    

    /**
     * Returns the 1-based position where an int is on this stack. If the int 
     * occurs as an item in this stack, this method returns the distance from 
     * the top of the stack of the occurrence nearest the top of the stack; the 
     * topmost item on the stack is considered to be at distance 1. 
     * 
     * @param item the int to search for from the top down
     * @return the 1-based position from the top of the stack where the int is 
     *  located; the return value -1 indicates that the int is not on the stack
     */
    public int search( int item )
    {
        for ( int ii = list.size() - 1; ii >= 0; ii-- )
        {
            if ( list.get( ii ) == item )
            {
                return list.size() - ii ;
            }
        }
        
        
        return -1 ;
    }
    
    
    /**
     * Gets items from the stack where the index is zero based and the top of
     * the stack is at an index of size()-1 with the bottom of the stack at an
     * index of 0.
     * 
     * @param index the index into the stack treated as a list
     * @return the int value at the index
     */
    public int get( int index )
    {
        return list.get( index ) ;
    }
    
    
    /**
     * Gets the size of this stack.
     * 
     * @return the size of this stack
     */
    public int size()
    {
        return list.size() ;
    }
    

    /**
     * Empties the contents of the stack.
     */
    public void clear()
    {
        list.clear() ;
    }
}

