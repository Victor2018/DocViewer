package com.nvqquy98.lib.doc.office.fc.hwpf.model;

import com.nvqquy98.lib.doc.office.fc.util.LittleEndian;

/**
 * The Tbkd structure is used by the PlcftxbxBkd and PlcfTxbxHdrBkd structures to associate ranges of 
 * text from the Textboxes Document and the Header Textboxes Document with FTXBXS objects. 
 * Ranges of text from the Textboxes Document are associated with FTXBXS objects from PlcftxbxTxt; 
 * ranges of text from the Header Textboxes Document are associated with FTXBXS objects 
 * from PlcfHdrtxbxTxt.
 * @author jqin
 *
 */
public class Tbkd 
{

	public Tbkd(byte[] data, int offset)
	{
		itxbxs = LittleEndian.getShort( data, offset);
		dcpDepend = LittleEndian.getShort( data, offset + 2);
		
		int t = LittleEndian.getShort( data, offset + 4);
		fMarkDelete = ((t & 0x20) != 0);
		fUnk = ((t & 0x10) != 0);
		fTextOverflow = ((t & 0x8) != 0);
	}
	
	public static int getSize()
	{
		return 6;
	}
	
	public int getTxbxIndex()
	{
		return itxbxs;
	}
	
	/**
	 * A signed integer that specifies the index of an FTXBXS object 
	 * within the PlcftxbxTxt structure or the PlcfHdrtxbxTxt structure. 
	 * The text range of this Tbkd object MUST be the same as the text range of the FTXBXS object, 
	 * or else it MUST be a subset of that range. When the FTXBXS object specifies 
	 * a chain of linked textboxes, the text range of each component textbox MUST be represented by 
	 * its own Tbkd object and a discrete text range.
	 * 
	 * In all but the last Tbkd object, itxbxs MUST be a valid FTXBXS index. The final Tbkd is 
	 * not associated with any FTXBXS object. The itxbxs value for the final Tbkd MUST be ignored
	 */
	private short itxbxs;
	
	/**
	 * Specifies version-specific information about the quantity of text that was processed. 
	 * This makes it possible to identify the end of the corresponding text range. 
	 * This value SHOULD<255> be zero and SHOULD<256> be ignored.
	 */
	private short dcpDepend;
	
	/**
	 * This value MUST be zero and MUST be ignored.
	 */
	private boolean fMarkDelete;
	/**
	 * Specifies version-specific information that flags the text range 
	 * which corresponds to this Tbkd as not being used by a textbox. 
	 * This value SHOULD<257> be zero and SHOULD<258> be ignored.
	 */
	private boolean fUnk;
	/**
	 * Specifies version-specific information about whether the text that is associated
	 *  with a textbox exceeds the amount that fits into the associated shape. 
	 *  This value SHOULD<259> be zero and SHOULD<260> be ignored.
	 */
	private boolean fTextOverflow;
}
