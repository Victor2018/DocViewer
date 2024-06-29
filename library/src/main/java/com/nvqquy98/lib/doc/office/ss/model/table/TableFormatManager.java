package com.nvqquy98.lib.doc.office.ss.model.table;

import java.util.HashMap;
import java.util.Map;

import com.nvqquy98.lib.doc.office.ss.model.style.CellStyle;

public class TableFormatManager 
{
	public TableFormatManager(int count)
	{
		formats = new HashMap<Integer, CellStyle>(count);
	}
	
	public int addFormat(CellStyle format)
	{
		int size = formats.size();
		formats.put(size, format);
		return size;
	}
	
	public CellStyle getFormat(int index)
	{
		if(index >= 0 && index < formats.size())
		{
			return formats.get(index);
		}
		
		return null;
	}
	
	public void dispose()
	{
		formats.clear();
		formats = null;
	}
	
	private Map<Integer, CellStyle> formats;
}
