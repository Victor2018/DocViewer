
package com.nvqquy98.lib.doc.office.thirdpart.emf.font;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Concrete implementation of the TrueType Input for one Table, read from a TTF
 * File.
 * 
 * Reads one table from the file.
 * 
 * @author Simon Fischer
 * @version $Id: TTFFileInput.java 8584 2006-08-10 23:06:37Z duns $
 */
public class TTFFileInput extends TTFInput
{

    private RandomAccessFile ttf;

    private long offset, length, checksum;

    public TTFFileInput(RandomAccessFile file, long offset, long length, long checksum)
        throws IOException
    {
        this.ttf = file;
        this.offset = offset;
        this.length = length;
        this.checksum = checksum;
    }

    // --------------- IO ---------------

    public void seek(long offset) throws IOException
    {
        ttf.seek(this.offset + offset);
        // System.out.println("seek "+(this.offset+offset));
    }

    long getPointer() throws IOException
    {
        return ttf.getFilePointer() - offset;
    }

    // ---------- Simple Data Types --------------

    public int readByte() throws IOException
    {
        return ttf.readUnsignedByte();
    }

    public int readRawByte() throws IOException
    {
        return ttf.readByte() & 255;
    }

    public short readShort() throws IOException
    {
        return ttf.readShort();
    }

    public int readUShort() throws IOException
    {
        return ttf.readUnsignedShort();
    }

    public int readLong() throws IOException
    {
        return ttf.readInt();
    }

    public long readULong() throws IOException
    {
        byte[] temp = new byte[4];
        ttf.readFully(temp);
        long l = 0;
        long weight = 1;
        for (int i = 0; i < temp.length; i++)
        {
            // l |= (temp[3-i]&255) << (8*i);
            l += (temp[3 - i] & 255) * weight;
            weight *= 256;
        }
        return l;
    }

    public byte readChar() throws IOException
    {
        return ttf.readByte();
    }

    // ---------------- Arrays -------------------

    public void readFully(byte[] b) throws IOException
    {
        ttf.readFully(b);
    }

    public String toString()
    {
        return offset + "-" + (offset + length - 1) + " - " + checksum;
    }
}
