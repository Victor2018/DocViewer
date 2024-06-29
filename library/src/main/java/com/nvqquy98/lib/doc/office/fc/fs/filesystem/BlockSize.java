
package com.nvqquy98.lib.doc.office.fc.fs.filesystem;

import com.nvqquy98.lib.doc.office.fc.fs.storage.LittleEndian;


/**
 * <p>A class describing attributes of the Big Block Size</p>
 */
public class BlockSize
{
    private int bigBlockSize;
    private short headerValue;

    protected BlockSize(int bigBlockSize, short headerValue)
    {
        this.bigBlockSize = bigBlockSize;
        this.headerValue = headerValue;
    }

    public int getBigBlockSize()
    {
        return bigBlockSize;
    }

    /**
     * Returns the value that gets written into the 
     *  header.
     * Is the power of two that corresponds to the
     *  size of the block, eg 512 => 9
     */
    public short getHeaderValue()
    {
        return headerValue;
    }

    public int getPropertiesPerBlock()
    {
        return bigBlockSize / CFBConstants.PROPERTY_SIZE;
    }

    public int getBATEntriesPerBlock()
    {
        return bigBlockSize / LittleEndian.INT_SIZE;
    }

    public int getXBATEntriesPerBlock()
    {
        return getBATEntriesPerBlock() - 1;
    }

    public int getNextXBATChainOffset()
    {
        return getXBATEntriesPerBlock() * LittleEndian.INT_SIZE;
    }
}
