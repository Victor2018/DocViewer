package com.nvqquy98.lib.doc.office.fc.hwpf.usermodel;

import java.util.ArrayList;

import com.nvqquy98.lib.doc.office.fc.ddf.DefaultEscherRecordFactory;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherContainerRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherRecordFactory;
import com.nvqquy98.lib.doc.office.fc.hwpf.model.PictureDescriptor;
import com.nvqquy98.lib.doc.office.fc.util.LittleEndian;

public class InlineWordArt extends PictureDescriptor
{
	private ArrayList<EscherRecord> escherRecords;
	
	public InlineWordArt(byte[] _dataStream, int dataBlockStartOffset)
    {
        super(_dataStream, dataBlockStartOffset);
        
		int dataBlockSize = LittleEndian.getInt(_dataStream, dataBlockStartOffset);
		final int dataBlockEndOffset = dataBlockSize + dataBlockStartOffset;
		  
		// Skip over the PICT block
	    int PICTFBlockSize = LittleEndian.getShort(_dataStream, dataBlockStartOffset
	            + 4); // Should be 68 bytes
	    // Now the PICTF1
	    int PICTF1BlockOffset = dataBlockStartOffset + PICTFBlockSize + 4;
	    short MM_TYPE = LittleEndian.getShort(_dataStream, dataBlockStartOffset
	            + 4 + 2);
	    if (MM_TYPE == 0x66)
	    {
	        // Skip the stPicName
	        int cchPicName = LittleEndian.getUnsignedByte(_dataStream, PICTF1BlockOffset);
	        PICTF1BlockOffset += 1 + cchPicName;
	    }
	    int PICTF1BlockSize = LittleEndian.getShort(_dataStream, PICTF1BlockOffset);

	    int unknownHeaderOffset = (PICTF1BlockSize + PICTF1BlockOffset) < dataBlockEndOffset
	            ? (PICTF1BlockSize + PICTF1BlockOffset) : PICTF1BlockOffset;
	        
	    escherRecords = 
	    		fillEscherRecords(_dataStream, PICTF1BlockOffset - 4, unknownHeaderOffset - PICTF1BlockOffset + 4);	    
    }
	
	public HWPFShape getInlineWordArt()
	{
		if(escherRecords != null && escherRecords.size() > 0 && escherRecords.get(0) instanceof EscherContainerRecord)
	    {
	    	return HWPFShapeFactory.createShape((EscherContainerRecord)(escherRecords.get(0)), null);
	    }
		return null;
	}
	
	/**
     * @return Horizontal scaling factor supplied by user expressed in .001%
     *         units
     */
    public int getHorizontalScalingFactor()
    {
        return mx;
    }
    
    /**
     * @return Vertical scaling factor supplied by user expressed in .001% units
     */
    public int getVerticalScalingFactor()
    {
        return my;
    }
    
    /**
     * Gets the initial width of the picture, in twips, prior to cropping or
     * scaling.
     * 
     * @return the initial width of the picture in twips
     */
    public int getDxaGoal()
    {
        return dxaGoal;
    }

    /**
     * Gets the initial height of the picture, in twips, prior to cropping or
     * scaling.
     * 
     * @return the initial width of the picture in twips
     */
    public int getDyaGoal()
    {
        return dyaGoal;
    }
    
	private ArrayList<EscherRecord> fillEscherRecords(byte[] data, int offset, int size)
	{
		ArrayList<EscherRecord> escherRecords = new ArrayList<EscherRecord>();
		EscherRecordFactory recordFactory = new DefaultEscherRecordFactory();
		try
		{
			int pos = offset;
			while (pos < offset + size)
			{
				EscherRecord r = recordFactory.createRecord(data, pos);
				escherRecords.add(r);
				int bytesRead = r.fillFields(data, pos, recordFactory);
				pos += bytesRead;
			}
			return escherRecords;
		}
		catch(Exception e)
		{
			return null;
		}
	}
}
