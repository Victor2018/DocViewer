package com.nvqquy98.lib.doc.office.fc.hwpf.model;

import com.nvqquy98.lib.doc.office.fc.util.LittleEndian;

/**
 * PLC structure where the data elements are Tbkd structures (6 bytes each).
 * The PLC structure is an array of character positions followed by an array of data elements. 
 * The data elements for any PLC MUST be the same size of zero or more bytes.
 * The number of CPs MUST be one more than the number of data elements. 
 * The CPs MUST appear in ascending order. There are different types of PLC structures, 
 * as specified in section 2.8. Each type specifies whether duplicate CPs are allowed for that type.
 * If the total size of a PLC (cbPlc) and the size of a single data element (cbData) are known, 
 * the number of data elements in that PLC (n) is given by the following expression: 
 * cbPlc - 4 = n * (cbData + 4)
 * The preceding expression MUST yield a whole number for n.
 * @author jqin
 *
 */
public class PlcfTxbxBkd 
{
	public PlcfTxbxBkd(byte[] data, int offset, int length)
	{
		int tbkdSize = Tbkd.getSize();
		
		int tbkdCnt = (length - 4) / (tbkdSize + 4);
		int CPCnt = (length - tbkdCnt * tbkdSize) / 4;
		
		CPs = new int[CPCnt];		
		tbkds = new Tbkd[tbkdCnt];
		
		for(int i = 0; i < CPCnt; i++)
		{
			CPs[i] = LittleEndian.getUShort( data, offset);
			offset += 4;
		}
		
		for(int i = 0; i < tbkdCnt; i++)
		{			
			tbkds[i] = new Tbkd(data, offset);
			offset += tbkdSize;
		}
	}
	
	public int[] getCharPositions()
	{
		return CPs;
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public int getCharPosition(int index)
	{
		if(CPs != null && CPs.length > index)
		{
			return CPs[index];
		}
		
		return -1;
	}
	
	public Tbkd[] getTbkds()
	{
		return tbkds;
	}
	
	/**
	 * An array of CPs(Character Position). CPs are positions in the textboxes document.
	 * Each CP specifies the beginning of a range of text to appear in a textbox specified in the corresponding Tbkd structure. 
	 * The range of text ends immediately prior to the next CP. The last CP does not begin a new text range; 
	 * it only terminates the previous one.	A character position, which is also known as a CP, is an unsigned 32-bit integer 
	 * that serves as the zero-based index of a character in the document text
	 * A PlcfTxbxBkd MUST NOT contain duplicate CPs.
	 */
	private int[] CPs;
	/**
	 * An array of 6-byte Tbkd structures that associate the text ranges with FTXBXS objects from PlcftxbxTxt.
	 */
	private Tbkd[] tbkds;
}
