
package com.nvqquy98.lib.doc.office.fc.fs.filesystem;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.nvqquy98.lib.doc.office.fc.fs.storage.LittleEndian;
import com.nvqquy98.lib.doc.office.fc.fs.storage.RawDataBlock;

/**
 * This abstract base class is the ancestor of all classes
 * implementing POIFS Property behavior.
 *
 */

public class Property
{
    public static final int  PROPERTY_TYPE_OFFSET = 0x42;
    // the property types
    public static final byte DIRECTORY_TYPE       = 1;
    public static final byte DOCUMENT_TYPE        = 2;
    public static final byte ROOT_TYPE            = 5;
    
    private static final  int NAME_SIZE_OFFSET  = 0x40;
    //
    private static final int PREVIOUS_PROPERTY_OFFSET = 0x44;
    //
    private static final int NEXT_PROPERTY_OFFSET = 0x48;
    //
    private static final int CHILD_PROPERTY_OFFSET = 0x4c;
    
    //private static final  int _max_name_length = (_name_size_offset / LittleEndianConsts.SHORT_SIZE) - 1;
    protected static final  int _NO_INDEX = -1;
    // documents must be at least this size to be stored in big blocks
    private static int  _big_block_minimum_bytes  = CFBConstants.BIG_BLOCK_MINIMUM_DOCUMENT_SIZE;

    private static final  int START_BLOCK_OFFSET = 0x74;
    private static final  int SIZE_OFFSET = 0x78;

    // node colors
    static final protected byte _NODE_BLACK = 1;
    static final protected byte _NODE_RED = 0;
    
    /**
     * Constructor from byte data
     *
     * @param index index number
     * @param array byte data
     * @param offset offset into byte data
     */
    public Property(int index, byte[] array, int offset)
    {
        //_raw_data = new byte[CFBConstants.PROPERTY_SIZE];
        //System.arraycopy(array, offset, _raw_data, 0, CFBConstants.PROPERTY_SIZE);
        //
        _name_size = LittleEndian.getShort(array, NAME_SIZE_OFFSET + offset);
        //
        _previous_property = LittleEndian.getShort(array, PREVIOUS_PROPERTY_OFFSET + offset);
        //
        _next_property = LittleEndian.getShort(array, NEXT_PROPERTY_OFFSET + offset);
        //
        _chlid_property = LittleEndian.getShort(array, CHILD_PROPERTY_OFFSET + offset);
        //
        _start_block = LittleEndian.getInt(array, START_BLOCK_OFFSET + offset);
        //
        _size = LittleEndian.getInt(array, SIZE_OFFSET + offset);
        //
        _property_type = array[PROPERTY_TYPE_OFFSET + offset];
        int name_length = (_name_size / LittleEndian.SHORT_SIZE) - 1;

        if (name_length < 1)
        {
            if (_property_type == ROOT_TYPE)
            {
                _name = "Root Entry";    
            }
            else
            {
                _name = "aaa";
            }
        }
        else
        {
            char[] char_array = new char[name_length];
            int name_offset = 0;

            for (int j = 0; j < name_length; j++)
            {
                char_array[j] = (char)LittleEndian.getShort(array, name_offset + offset);
                name_offset += LittleEndian.SHORT_SIZE;
            }
            _name = new String(char_array, 0, name_length);
        }
    }

    /**
     * 
     * @return
     */
    public byte[] getDocumentRawData()
    {
        return this.documentRawData;
    }
    
    /**
     * 
     * @return
     */
    public void setDocumentRawData(byte[] rawData)
    { 
        documentRawData = rawData;
    }

    /**
     * @return the start block
     */
    public int getStartBlock()
    {
        return _start_block;
    }

    /**
     * find out the document size
     *
     * @return size in bytes
     */
    public int getSize()
    {
        return _size;
    }
    
    /**
     * 
     * @return
     */
    public int getPreviousPropertyIndex()
    {
        return _previous_property;
    }
    
    /**
     * 
     */
    public int getNextPropertyIndex()
    {
        return _next_property;
    }
    
    /**
     * 
     */
    public int getChildPropertyIndex()
    {
        return _chlid_property;
    }
    

    /**
     * Based on the currently defined size, should this property use
     * small blocks?
     *
     * @return true if the size is less than _big_block_minimum_bytes
     */
    public boolean shouldUseSmallBlocks()
    {
        return  getSize() <  _big_block_minimum_bytes;
    }
    /**
     * Get the name of this property
     *
     * @return property name as String
     */
    public String getName()
    {
        return _name;
    }
    
    /**
     * 
     * @param propertyName
     * @return
     */
    public long getPropertyRawDataSize()
    {
        if (blocks != null)
        {
            return blocks[0].getData().length * blocks.length;
        }
        return documentRawData.length;
    }

    /**
     * @return Returns the blocks.
     */
    public RawDataBlock[] getBlocks()
    {
        return blocks;
    }

    /**
     * @param blocks The blocks to set.
     */
    public void setBlocks(RawDataBlock[] blocks)
    {
        this.blocks = blocks;
        blockSize = blocks[0].getData().length;
    }
    
    /**
     * 
     */
    public boolean isDocument()
    {
        return _property_type ==  DOCUMENT_TYPE;
    }
    
    /**
     * 
     */
    public boolean isDirectory()
    {
        return _property_type == DIRECTORY_TYPE;
    }
    
    /**
     * 
     */
    public boolean isRoot()
    {
        return _property_type == ROOT_TYPE;
    }
    
    /**
     *  get an unsigned short value from a byte array
     *
     *@param  data    the byte array
     *@param  offset  a starting offset into the byte array
     *@return         the unsigned short (16-bit) value in an integer
     */
    public int getUShort(int offset) 
    {
        int b0 = getByteForOffset(offset);
        int b1 = getByteForOffset(offset + 1);
        
        return (b1 << 8) + (b0 << 0);
    }
    
    public long getUInt(int offset) 
    {
        long retNum = getInt(offset);
        return retNum & 0x00FFFFFFFFl;
    }
    
    /**
     *  get an int value from a byte array
     *
     *@param  data    the byte array
     *@param  offset  a starting offset into the byte array
     *@return         the int (32-bit) value
     */
    public int getInt(int offset) 
    {
        int b0 = getByteForOffset(offset);
        int b1 = getByteForOffset(offset + 1);
        int b2 = getByteForOffset(offset + 2);
        int b3 = getByteForOffset(offset + 3);
        
        /*int i=offset;
        int b0 = data[i++] & 0xFF;
        int b1 = data[i++] & 0xFF;
        int b2 = data[i++] & 0xFF;
        int b3 = data[i++] & 0xFF;*/
        return (b3 << 24) + (b2 << 16) + (b1 << 8) + (b0 << 0);
    }
    
    /**
     *  get a long value from a byte array
     *
     *@param  data    the byte array
     *@param  offset  a starting offset into the byte array
     *@return         the long (64-bit) value
     */
    public long getLong(int offset)
    {
        long result = 0;
        for (int j = offset + 8 - 1; j >= offset; j--) 
        {
            result <<= 8;
            result |= 0xff & getByteForOffset(j);
        }
        return result;
    }
    
    
    /**
     * 
     * @param out
     * @param offset
     * @param len
     * @throws IOException
     */
    public void writeByte(OutputStream out, int offset, int len) throws IOException
    {
    	//write BLOCKNUMBER blocks one time 
    	final int BLOCKNUMBER = 16;
    	
    	int length = Math.min(len, blockSize * BLOCKNUMBER);
    	byte[] data =  new byte[length];
        int index = getBlockIndexForOffset(offset);
        
        //the first data in the index-th block
        int off = offset - blockSize * index;
        int writeLen = Math.min(len, blockSize - off);       
        System.arraycopy(blocks[index].getData(), off, data, 0, writeLen);
        int blockCnt = 1;
        while (writeLen <= len && index < blocks.length)
        {
	   		 if(blockCnt < BLOCKNUMBER)
	   		 {
	   			index++;
	   			blockCnt++;
                if (writeLen + blockSize > len)
                {
                	//the rest data
                	if(len > writeLen && index < blocks.length)
                	{
                		System.arraycopy(blocks[index].getData(), 0, data, writeLen, len - writeLen);
                	}                	
                	out.write(data, 0, len);
                    break;
                }
                System.arraycopy(blocks[index].getData(), 0, data, writeLen, blockSize);
                writeLen += blockSize;   
	   		 }
	   		 else
	   		 {
	   			 //has composite BLOCKNUMBER blocks data, then write it to out
	   			 out.write(data, 0, writeLen);
	   			 
	   			 //update state
	   			 len -= writeLen;
	   			 blockCnt = 0;
	   			 writeLen = 0;
	   		 }            
        }
       
        data = null;
    }
    
    
    /**
     * 
     */
    private int getBlockIndexForOffset(int offset)
    {
        return offset / blockSize;
    }
    
    /**
     * 
     */
    private int getByteForOffset(int offset)
    {
        int index = offset / blockSize;
        int off = offset - blockSize * index;
        return blocks[index].getData()[off] & 0xFF;
    }
    
    public byte[] getRecordData(int usrOffset)
    {
        //len
        int rlen = (int)getUInt(usrOffset + 4) + 8;

        // Sanity check the length
        if(rlen < 0) { rlen = 0; }
        
        if(documentRawData == null || documentRawData.length < rlen)
        {
            documentRawData = new byte[Math.max(rlen, blockSize)];
        }
        
        //save record data to documentRawData        
        int startIndex = usrOffset / blockSize;
        int endIndex = (usrOffset + rlen)/ blockSize;
        
        if(endIndex > startIndex)
        {
            //multi blocks
            int off = usrOffset % blockSize;
            //first block
            System.arraycopy(blocks[startIndex].getData(), off, documentRawData, 0, blockSize - off);
            
            off = blockSize - off;
            //middle whole block
            if(startIndex + 1 < endIndex)
            {
                for(int i = startIndex + 1; i < endIndex; i++)
                {
                    System.arraycopy(blocks[i].getData(), 0, documentRawData, off, blockSize);
                    off = off + blockSize;
                }
            }
            
            //last block
            if (endIndex < blocks.length)
            {
                System.arraycopy(blocks[endIndex].getData(), 0, documentRawData, off, (usrOffset + rlen) % blockSize);
            }
        }
        else
        {
            //single block
            int off = usrOffset % blockSize;
            System.arraycopy(blocks[startIndex].getData(), off, documentRawData, 0, rlen);
        }
        return documentRawData;
    }
    
    /**
     * 
     * @param property
     */
    public void addChildProperty(Property property)
    {
        properties.put(property.getName(), property);        
    }
    
    /**
     * 
     */
    public Property getChlidProperty(String name)
    {
        return properties.get(name);
    }
    /**
     * 
     */
    public void dispose()
    {
        //_raw_data = null;
        documentRawData = null;
        _name = null;
        blocks = null;
        if (properties != null)
        {
            Set<String> set = properties.keySet();
            for (String key : set)
            {
                properties.get(key).dispose();
            }
            properties.clear();
            properties = null;
        }
    }
    
    private String _name;
    private short _name_size;
    private byte _property_type;
    private int _start_block;
    private int _size;
    private int _chlid_property;
    private int _next_property;
    private int _previous_property;
    //private byte[] _raw_data;
    private byte[] documentRawData;
    //
    private RawDataBlock[] blocks;
    //
    private int blockSize;
    //
    protected Map<String, Property> properties = new HashMap<String, Property>();
}
