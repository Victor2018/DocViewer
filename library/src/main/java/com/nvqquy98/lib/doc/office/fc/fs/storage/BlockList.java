
package com.nvqquy98.lib.doc.office.fc.fs.storage;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.nvqquy98.lib.doc.office.fc.fs.filesystem.BlockSize;

/**
 * A simple implementation of BlockList
 */
public class BlockList
{

    /**
     * Constructor RawDataBlockList
     *
     * @param stream the InputStream from which the data will be read
     * @param bigBlockSize The big block size, either 512 bytes or 4096 bytes
     *
     * @exception IOException on I/O errors, and if an incomplete
     *            block is read
     */
    public BlockList(InputStream stream, BlockSize bigBlockSize) throws IOException
    {
        List<RawDataBlock> blocks = new ArrayList<RawDataBlock>();        
        int size = bigBlockSize.getBigBlockSize(); 
        while (true)
        {
            byte[] b = new byte[size];
            int count = stream.read(b);
            if (count <= 0)
            {
                break;
            }
            RawDataBlock block = new RawDataBlock(b);
            blocks.add(block);
            if (count != size)
            {
                break;
            }
        }
        _blocks = blocks.toArray(new RawDataBlock[blocks.size()]);
    }
    
    /**
     * 
     * @param _blocks
     */
    public BlockList(RawDataBlock[] _blocks)
    {
        this._blocks = _blocks;
    }

    /**
     * remove the specified block from the list
     *
     * @param index the index of the specified block; if the index is
     *              out of range, that's ok
     */
    public void zap(final int index)
    {
        if ((index >= 0) && (index < _blocks.length))
        {
            _blocks[index] = null;
        }
    }

    /**
     * Unit testing method. Gets, without sanity checks or
     *  removing.
     */
    protected RawDataBlock get(final int index)
    {
        return _blocks[index];
    }

    /**
     * remove and return the specified block from the list
     *
     * @param index the index of the specified block
     *
     * @return the specified block
     *
     * @exception IOException if the index is out of range or has
     *            already been removed
     */
    public RawDataBlock remove(int index) throws IOException
    {
        if (index < 0 || index >= _blocks.length)
        {
            return null;
        }
        RawDataBlock result = _blocks[index];
        _blocks[index] = null;
        return result;
    }

    /**
     * get the blocks making up a particular stream in the list. The
     * blocks are removed from the list.
     *
     * @param startBlock the index of the first block in the stream
     *
     * @return the stream as an array of correctly ordered blocks
     *
     * @exception IOException if blocks are missing
     */
    public RawDataBlock[] fetchBlocks(int startBlock, int headerPropertiesStartBlock)
        throws IOException
    {
        if (_bat == null)
        {
            throw new IOException("Improperly initialized list: no block allocation table provided");
        }
        return _bat.fetchBlocks(startBlock, headerPropertiesStartBlock, this);
    }

    /**
     * set the associated BlockAllocationTable
     *
     * @param bat the associated BlockAllocationTable
     */
    public void setBAT(BlockAllocationTableReader bat) throws IOException
    {
        _bat = bat;
    }

    /**
     * Returns the count of the number of blocks
     */
    public int blockCount()
    {
        return _blocks.length;
    }
    
    //
    private RawDataBlock[] _blocks;
    //
    private BlockAllocationTableReader _bat;
}
