
package com.nvqquy98.lib.doc.office.fc.fs.storage;

import java.io.IOException;
import java.io.InputStream;

import com.nvqquy98.lib.doc.office.fc.fs.filesystem.BlockSize;
import com.nvqquy98.lib.doc.office.fc.fs.filesystem.CFBConstants;
import com.nvqquy98.lib.doc.office.fc.util.HexDump;

/**
 * The block containing the archive header
 */
public final class HeaderBlock
{

    public static final long _signature               = 0xE11AB1A1E011CFD0L;
    public static final int  _bat_array_offset        = 0x4c;
    public static final int  _max_bats_in_header      = (CFBConstants.SMALLER_BIG_BLOCK_SIZE - _bat_array_offset) / 4;
    
    // useful offsets
    public static final int  _signature_offset        = 0;
    public static final int  _bat_count_offset        = 0x2C;
    public static final int  _property_start_offset   = 0x30;
    public static final int  _sbat_start_offset       = 0x3C;
    public static final int  _sbat_block_count_offset = 0x40;
    public static final int  _xbat_start_offset       = 0x44;
    public static final int  _xbat_count_offset       = 0x48;
    
    /**
     * What big block size the file uses. Most files
     *  use 512 bytes, but a few use 4096
     */
    private BlockSize bigBlockSize;

    /** 
     * Number of big block allocation table blocks (int).
     * (Number of FAT Sectors in Microsoft parlance).
     */
    private int _bat_count;

    /** 
     * Start of the property set block (int index of the property set
     * chain's first big block).
     */
    private int _property_start;

    /** 
     * start of the small block allocation table (int index of small
     * block allocation table's first big block)
     */
    private int _sbat_start;
    /**
     * Number of small block allocation table blocks (int)
     * (Number of MiniFAT Sectors in Microsoft parlance)
     */
    private int _sbat_count;

    /** 
     * Big block index for extension to the big block allocation table
     */
    private int _xbat_start;
    /**
     * Number of big block allocation table blocks (int)
     * (Number of DIFAT Sectors in Microsoft parlance)
     */
    private int _xbat_count;

    /**
     * The data. Only ever 512 bytes, because 4096 byte
     *  files use zeros for the extra header space.
     */
    private byte[] _data;

    /**
     * create a new HeaderBlockReader from an InputStream
     *
     * @param stream the source InputStream
     *
     * @exception IOException on errors or bad data
     */
    public HeaderBlock(InputStream stream) throws IOException
    {
        // Grab the first 512 bytes
        _data = new byte[512];
        stream.read(_data);
        // verify signature
        long signature = LittleEndian.getLong(_data, _signature_offset);

        if (signature != _signature)
        {
            // Is it one of the usual suspects?
            /*byte[] OOXML_FILE_HEADER = POIFSConstants.OOXML_FILE_HEADER;
            if (_data[0] == OOXML_FILE_HEADER[0] && _data[1] == OOXML_FILE_HEADER[1]
                && _data[2] == OOXML_FILE_HEADER[2] && _data[3] == OOXML_FILE_HEADER[3])
            {
                throw new OfficeXmlFileException(
                    "The supplied data appears to be in the Office 2007+ XML. You are calling the part of POI that deals with OLE2 Office Documents. You need to call a different part of POI to process this data (eg XSSF instead of HSSF)");
            }
            if ((signature & 0xFF8FFFFFFFFFFFFFL) == 0x0010000200040009L)
            {
                // BIFF2 raw stream starts with BOF (sid=0x0009, size=0x0004, data=0x00t0)
                throw new IllegalArgumentException(
                    "The supplied data appears to be in BIFF2 format.  "
                        + "POI only supports BIFF8 format");
            }*/

            // Give a generic error
            throw new IOException("Invalid header signature; read " + longToHex(signature)
                + ", expected " + longToHex(_signature));
        }
        // Figure out our block size
        if (_data[30] == 12)
        {
            this.bigBlockSize = CFBConstants.LARGER_BIG_BLOCK_SIZE_DETAILS;
        }
        else if (_data[30] == 9)
        {
            this.bigBlockSize = CFBConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS;
        }
        else
        {
            throw new IOException("Unsupported blocksize  (2^" + _data[30]
                + "). Expected 2^9 or 2^12.");
        }

        // Setup the fields to read and write the counts and starts
        _bat_count = LittleEndian.getInt(_data, _bat_count_offset);
        _property_start = LittleEndian.getInt(_data, _property_start_offset);
        _sbat_start = LittleEndian.getInt(_data, _sbat_start_offset);
        _sbat_count = LittleEndian.getInt(_data, _sbat_block_count_offset);
        _xbat_start = LittleEndian.getInt(_data, _xbat_start_offset);
        _xbat_count = LittleEndian.getInt(_data, _xbat_count_offset);
    }

    /**
     * 
     * @param value
     * @return
     */
    private String longToHex(long value)
    {
        return new String(HexDump.longToHex(value));
    }
    /**
     * get start of Property Table
     *
     * @return the index of the first block of the Property Table
     */
    public int getPropertyStart()
    {
        return _property_start;
    }

    /**
     * Set start of Property Table
     *
     * @param startBlock the index of the first block of the Property Table
     */
    public void setPropertyStart(final int startBlock)
    {
        _property_start = startBlock;
    }

    /**
     * @return start of small block (MiniFAT) allocation table
     */
    public int getSBATStart()
    {
        return _sbat_start;
    }

    public int getSBATCount()
    {
        return _sbat_count;
    }

    /**
     * Set start of small block allocation table
     *
     * @param startBlock the index of the first big block of the small
     *                   block allocation table
     */
    public void setSBATStart(final int startBlock)
    {
        _sbat_start = startBlock;
    }

    /**
     * Set count of SBAT blocks
     *
     * @param count the number of SBAT blocks
     */
    public void setSBATBlockCount(final int count)
    {
        _sbat_count = count;
    }

    /**
     * @return number of BAT blocks
     */
    public int getBATCount()
    {
        return _bat_count;
    }

    /**
     * Sets the number of BAT blocks that are used.
     * This is the number used in both the BAT and XBAT. 
     */
    public void setBATCount(final int count)
    {
        _bat_count = count;
    }

    /**
     * Returns the offsets to the first (up to) 109
     *  BAT sectors.
     * Any additional BAT sectors are held in the XBAT (DIFAT)
     *  sectors in a chain.
     * @return BAT offset array
     */
    public int[] getBATArray()
    {
        // Read them in
        int[] result = new int[Math.min(_bat_count, _max_bats_in_header)];
        int offset = _bat_array_offset;
        for (int j = 0; j < result.length; j++)
        {
            result[j] = LittleEndian.getInt(_data, offset);
            offset += LittleEndian.INT_SIZE;
        }
        return result;
    }

    /**
     * @return XBAT (DIFAT) count
     */
    public int getXBATCount()
    {
        return _xbat_count;
    }

    /**
     * @return XBAT (DIFAT) index
     */
    public int getXBATIndex()
    {
        return _xbat_start;
    }

    /**
     * @return The Big Block size, normally 512 bytes, sometimes 4096 bytes
     */
    public BlockSize getBigBlockSize()
    {
        return bigBlockSize;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        _data = null;
        bigBlockSize = null;
    }
}
