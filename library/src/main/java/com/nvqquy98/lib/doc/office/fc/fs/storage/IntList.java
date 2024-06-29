
package com.nvqquy98.lib.doc.office.fc.fs.storage;

import com.nvqquy98.lib.doc.office.fc.fs.filesystem.CFBConstants;

/**
 * A List of int's; as full an implementation of the java.util.List
 * interface as possible, with an eye toward minimal creation of
 * objects
 *
 * the mimicry of List is as follows:
 * <ul>
 * <li> if possible, operations designated 'optional' in the List
 *      interface are attempted
 * <li> wherever the List interface refers to an Object, substitute
 *      int
 * <li> wherever the List interface refers to a Collection or List,
 *      substitute IntList
 * </ul>
 *
 * the mimicry is not perfect, however:
 * <ul>
 * <li> operations involving Iterators or ListIterators are not
 *      supported
 * <li> remove(Object) becomes removeValue to distinguish it from
 *      remove(int index)
 * <li> subList is not supported
 * </ul>
 */
public class IntList
{
    private int[] _array;
    private int _limit;
    private int fillval = 0;
    private static final int _default_size = 128;

    /**
     * create an IntList of default size
     */
    public IntList()
    {
        this(_default_size, 0);
    }
    
    /**
     * create an IntList with a predefined initial size
     *
     * @param initialCapacity the size for the internal array
     */

    public IntList(final int initialCapacity, int fillvalue)
    {
        _array = new int[initialCapacity];
        if (fillval != 0)
        {
            fillval = fillvalue;
            fillArray(fillval, _array, 0);
        }
        _limit = 0;
    }

    /**
     * 
     * @param val
     * @param array
     * @param index
     */
    private void fillArray(int val, int[] array, int index)
    {
        for (int k = index; k < array.length; k++)
        {
            array[k] = val;
        }
    }


    /**
     * Appends the specified element to the end of this list
     *
     * @param value element to be appended to this list.
     *
     * @return true (as per the general contract of the Collection.add
     *         method).
     */

    public boolean add(int value)
    {
        if (_limit == _array.length)
        {
            growArray(_limit * 2);
        }
        _array[_limit++] = value;
        return true;
    }
    

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index of element to return.
     *
     * @return the element at the specified position in this list.
     *
     * @exception IndexOutOfBoundsException if the index is out of
     *            range (index < 0 || index >= size()).
     */

    public int get(int index)
    {
        if (index >= _limit)
        {
            return CFBConstants.END_OF_CHAIN;
            //throw new IndexOutOfBoundsException(index + " not accessible in a list of length "
            //    + _limit);
        }
        return _array[index];
    }


    /**
     * Returns the number of elements in this list. If this list
     * contains more than Integer.MAX_VALUE elements, returns
     * Integer.MAX_VALUE.
     *
     * @return the number of elements in this IntList
     */

    public int size()
    {
        return _limit;
    }

    /**
     * 
     */
    private void growArray(int new_size)
    {
        int size = (new_size == _array.length) ? new_size + 1 : new_size;
        int[] new_array = new int[size];

        if (fillval != 0)
        {
            fillArray(fillval, new_array, _array.length);
        }

        System.arraycopy(_array, 0, new_array, 0, _limit);
        _array = new_array;
    }
}

