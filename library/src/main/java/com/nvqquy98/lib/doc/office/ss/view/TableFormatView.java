package com.nvqquy98.lib.doc.office.ss.view;

import com.nvqquy98.lib.doc.office.common.PaintKit;
import com.nvqquy98.lib.doc.office.ss.model.CellRangeAddress;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Workbook;
import com.nvqquy98.lib.doc.office.ss.model.style.BorderStyle;
import com.nvqquy98.lib.doc.office.ss.model.style.CellStyle;
import com.nvqquy98.lib.doc.office.ss.model.table.SSTable;
import com.nvqquy98.lib.doc.office.ss.model.table.TableFormatManager;
import com.nvqquy98.lib.doc.office.ss.util.ModelUtil;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class TableFormatView 
{
	public TableFormatView( SheetView sheetView)
    {
        this.sheetView = sheetView;        
    } 
	
	public void draw(Canvas canvas)
	{
		 Paint paint = PaintKit.instance().getPaint();
		//save paint property      
	    int oldColor = paint.getColor(); 
	    canvas.save();
	    
		TableFormatManager formatMgr = sheetView.getCurrentSheet().getWorkbook().getTableFormatManager();
		 SSTable[] tables = sheetView.getCurrentSheet().getTables();
		 if(tables != null && formatMgr != null)
		 {
			 for(SSTable table : tables)
			 {
				 //header row
				 if(table.isHeaderRowShown() && (table.getHeaderRowDxfId() >= 0 || table.getHeaderRowBorderDxfId() >= 0))
				 {
					 drawHeaderRowFormat(canvas, formatMgr, table, paint);
				 }
				 
				 //total row
				 if(table.isTotalRowShown() && (table.getTotalsRowDxfId() >= 0 || table.getTotalsRowBorderDxfId() >= 0))
				 {
					 drawTotalRowFormat(canvas, formatMgr, table, paint);
				 }
				 
				 //table border
				 if(table.getTableBorderDxfId() >= 0)
				 {
					 drawTableBorders(canvas, formatMgr, table, paint);
				 }
			 }
		 }
		 
		 paint.setColor(oldColor);
	     canvas.restore(); 
	}
	
	private void drawHeaderRowFormat(Canvas canvas, TableFormatManager formatMgr, SSTable table, Paint paint)
	{
		Workbook book = sheetView.getCurrentSheet().getWorkbook();
		
		CellRangeAddress ref = table.getTableReference();
		
		CellStyle headerRowDxf = formatMgr.getFormat(table.getHeaderRowDxfId());
		CellStyle headerRowBorderDxf = formatMgr.getFormat(table.getHeaderRowBorderDxfId());
		RectF rect = ModelUtil.instance().getCellAnchor(sheetView, ref.getFirstRow(), ref.getFirstColumn(), ref.getLastColumn());
		
		//border
		if(headerRowDxf != null)
		{
			drawFormatBorders(canvas, paint, book, headerRowDxf, rect);
		}
		
		if(headerRowBorderDxf != null)
		{
			drawFormatBorders(canvas, paint, book, headerRowBorderDxf, rect);
		}		
	}

	private void drawTotalRowFormat(Canvas canvas, TableFormatManager formatMgr, SSTable table, Paint paint)
	{
		Workbook book = sheetView.getCurrentSheet().getWorkbook();
		
		CellRangeAddress ref = table.getTableReference();
		
		CellStyle totalsRowDxf = formatMgr.getFormat(table.getTotalsRowDxfId());
		CellStyle totalsRowBorderDxf = formatMgr.getFormat(table.getTotalsRowBorderDxfId());
		RectF rect = ModelUtil.instance().getCellAnchor(sheetView, ref.getLastRow(), ref.getFirstColumn(), ref.getLastColumn());
		
		//border
		if(totalsRowDxf != null)
		{
			drawFormatBorders(canvas, paint, book, totalsRowDxf, rect);
		}
		
		if(totalsRowBorderDxf != null)
		{
			drawFormatBorders(canvas, paint, book, totalsRowBorderDxf, rect);
		}		
	}
	
	private void drawTableBorders(Canvas canvas, TableFormatManager formatMgr, SSTable table, Paint paint)
	{
		RectF rect = ModelUtil.instance().getCellRangeAddressAnchor(sheetView, table.getTableReference());
		
		drawFormatBorders(canvas, paint, sheetView.getCurrentSheet().getWorkbook(), 
				formatMgr.getFormat(table.getTableBorderDxfId()), rect);
	}
	
	private void drawFormatBorders(Canvas canvas, Paint paint, Workbook book,
			CellStyle headerRowDxf, RectF rect)
	{
		// draw left border
		if(rect.left > sheetView.getRowHeaderWidth() && headerRowDxf.getBorderLeft() != BorderStyle.BORDER_NONE)
		{
			paint.setColor(book.getColor(headerRowDxf.getBorderLeftColorIdx()));
		    canvas.drawRect(rect.left, rect.top, rect.left + 1, rect.bottom, paint);
		}
		
		// draw top border
		if(rect.top > sheetView.getColumnHeaderHeight() && headerRowDxf.getBorderTop() != BorderStyle.BORDER_NONE)
		{
			paint.setColor(book.getColor(headerRowDxf.getBorderTopColorIdx()));
		    canvas.drawRect(rect.left, rect.top, rect.right, rect.top + 1, paint);
		}
		
		// draw right border
		if(rect.right > sheetView.getRowHeaderWidth() && headerRowDxf.getBorderRight() != BorderStyle.BORDER_NONE)
		{
			paint.setColor(book.getColor(headerRowDxf.getBorderRightColorIdx()));
			canvas.drawRect(rect.right, rect.top, rect.right + 1, rect.bottom, paint);
		}
		
		// draw bottom border
		if(rect.bottom > sheetView.getColumnHeaderHeight() && headerRowDxf.getBorderBottom() != BorderStyle.BORDER_NONE)
		{
			paint.setColor(book.getColor(headerRowDxf.getBorderBottomColorIdx()));
		    canvas.drawRect(rect.left, rect.bottom, rect.right, rect.bottom + 1, paint);
		}
	}
	
	// 当前显示Sheet
    private SheetView sheetView;
}
