
package com.nvqquy98.lib.doc.office.fc.fs.storage;


/**
 * A big block created from an InputStream, holding the raw data
 * 
 */

public class RawDataBlock
{
    /**
     * 
     * @param data
     */
    public RawDataBlock(byte[] data)
    {
        this._data = data;
    }

    /**
     * Get the data from the block
     *
     * @return the block's data as a byte array
     */
    public byte[] getData() 
    {
        return _data;
    }
    
    //
    private byte[] _data;
} 

