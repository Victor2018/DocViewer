
package com.nvqquy98.lib.doc.office.fc.fs.storage;

public class LittleEndian
{
    public static final int BYTE_SIZE   = 1;
    public static final int SHORT_SIZE  = 2;
    public static final int INT_SIZE    = 4;
    public static final int LONG_SIZE   = 8;
    public static final int DOUBLE_SIZE = 8;
    
    /**
     *  get a short value from a byte array
     *
     *@param  data    the byte array
     *@param  offset  a starting offset into the byte array
     *@return         the short (16-bit) value
     */
    public static short getShort(byte[] data, int offset)
    {
        int b0 = data[offset] & 0xFF;
        int b1 = data[offset + 1] & 0xFF;
        return (short)((b1 << 8) + (b0 << 0));
    }

    /**
     *  get an unsigned short value from a byte array
     *
     *@param  data    the byte array
     *@param  offset  a starting offset into the byte array
     *@return         the unsigned short (16-bit) value in an integer
     */
    public static int getUShort(byte[] data, int offset)
    {
        int b0 = data[offset] & 0xFF;
        int b1 = data[offset + 1] & 0xFF;
        return (b1 << 8) + (b0 << 0);
    }

    /**
     *  get a short value from the beginning of a byte array
     *
     *@param  data  the byte array
     *@return       the short (16-bit) value
     */
    public static short getShort(byte[] data)
    {
        return getShort(data, 0);
    }

    /**
     *  get an unsigned short value from the beginning of a byte array
     *
     *@param  data  the byte array
     *@return       the unsigned short (16-bit) value in an int
     */
    public static int getUShort(byte[] data)
    {
        return getUShort(data, 0);
    }

    /**
     *  get an int value from a byte array
     *
     *@param  data    the byte array
     *@param  offset  a starting offset into the byte array
     *@return         the int (32-bit) value
     */
    public static int getInt(byte[] data, int offset)
    {
        int i = offset;
        int b0 = data[i++] & 0xFF;
        int b1 = data[i++] & 0xFF;
        int b2 = data[i++] & 0xFF;
        int b3 = data[i++] & 0xFF;
        return (b3 << 24) + (b2 << 16) + (b1 << 8) + (b0 << 0);
    }

    /**
     *  get an int value from the beginning of a byte array
     *
     *@param  data  the byte array
     *@return the int (32-bit) value
     */
    public static int getInt(byte[] data)
    {
        return getInt(data, 0);
    }

    /**
     *  get an unsigned int value from a byte array
     *
     *@param  data    the byte array
     *@param  offset  a starting offset into the byte array
     *@return         the unsigned int (32-bit) value in a long
     */
    public static long getUInt(byte[] data, int offset)
    {
        long retNum = getInt(data, offset);
        return retNum & 0x00FFFFFFFFl;
    }

    /**
     *  get an unsigned int value from a byte array
     *
     *@param  data    the byte array
     *@return         the unsigned int (32-bit) value in a long
     */
    public static long getUInt(byte[] data)
    {
        return getUInt(data, 0);
    }

    /**
     *  get a long value from a byte array
     *
     *@param  data    the byte array
     *@param  offset  a starting offset into the byte array
     *@return         the long (64-bit) value
     */
    public static long getLong(byte[] data, int offset)
    {
        long result = 0;

        for (int j = offset + LONG_SIZE - 1; j >= offset; j--)
        {
            result <<= 8;
            result |= 0xff & data[j];
        }
        return result;
    }

}
