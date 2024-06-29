package com.nvqquy98.lib.doc.office.wp.view;

import com.nvqquy98.lib.doc.office.common.shape.WPAutoShape;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.simpletext.view.PageAttr;

public class PositionLayoutKit 
{
	 private static PositionLayoutKit kit = new PositionLayoutKit();
	 private PositionLayoutKit()
	 {
		 
	 }

	 public static PositionLayoutKit instance()
	 {
		 return kit;
	 }
	       
	 
	    
	    public void processShapePosition(LeafView leafView, WPAutoShape wpShape, PageAttr pageAttr)
	    {
	    	processHorizontalPosition(leafView, wpShape, pageAttr);
	    	
	    	processVerticalPosition(leafView, wpShape, pageAttr);
	    }
	    
	    private void processHorizontalPosition(LeafView leafView, WPAutoShape wpShape, PageAttr pageAttr)
	    {
	    	byte posType = wpShape.getHorPositionType();
	    	byte horRelative = wpShape.getHorizontalRelativeTo();
	    	
	    	if(posType == WPAutoShape.POSITIONTYPE_RELATIVE)
	    	{
	    		//relative postion
	    		float ratio = wpShape.getHorRelativeValue() / 1000f;
	    		
	    		if(horRelative == WPAutoShape.RELATIVE_PAGE)
	    		{
	    			leafView.setX(Math.round(pageAttr.pageWidth * ratio));
	    		}
	    		else if(horRelative == WPAutoShape.RELATIVE_MARGIN)
	    		{
	    			leafView.setX(pageAttr.leftMargin + Math.round((pageAttr.pageWidth - pageAttr.leftMargin - pageAttr.rightMargin) * ratio));
	    		}
	    		else if(horRelative ==  WPAutoShape.RELATIVE_LEFT)
	            {
	    			leafView.setX(Math.round(pageAttr.leftMargin * ratio));
	            }
	            else if(horRelative == WPAutoShape.RELATIVE_RIGHT)
	            {
	            	leafView.setX(pageAttr.pageWidth - pageAttr.rightMargin + Math.round(pageAttr.rightMargin * ratio));
	            }
	            else if(horRelative == WPAutoShape.RELATIVE_OUTER)
	            {
	            	if(leafView.getParentView() != null 
		        			&& leafView.getParentView().getParentView() != null 
		        			&& leafView.getParentView().getParentView().getParentView() != null)
		    		{
		        		PageView pageView = (PageView)leafView.getParentView().getParentView().getParentView();
		        		if(pageView.getPageNumber() % 2 == 1)
		        		{
		        			//Odd page
		        			leafView.setX(pageAttr.pageWidth - pageAttr.rightMargin + Math.round(pageAttr.rightMargin * ratio));
		        		}
		        		else
		        		{
		        			//Even page
		        			leafView.setX(Math.round(pageAttr.leftMargin * ratio));
		        		} 
		    		}
	            }
	            else if(horRelative == WPAutoShape.RELATIVE_INNER)
	            {
	            	PageView pageView = (PageView)leafView.getParentView().getParentView().getParentView();
	            	if(pageView.getPageNumber() % 2 == 1)
	        		{
	        			//Odd page
        				leafView.setX(Math.round(pageAttr.leftMargin * ratio));
	        		}
	        		else
	        		{
	        			//Even page
	        			leafView.setX(pageAttr.pageWidth - pageAttr.rightMargin + Math.round(pageAttr.rightMargin * ratio));			        			
	        		} 
	            }
	    	}
	    	else
	    	{
	    		byte horPosition = wpShape.getHorizontalAlignment();
	    		if(horPosition == WPAutoShape.ALIGNMENT_ABSOLUTE)
	    		{
	    			processHorizontalPosition_Absolute(leafView, wpShape, pageAttr);
	    		}
	    		else if (horPosition == WPAutoShape.ALIGNMENT_LEFT)
	            {
	            	//left alignment
	            	processHorizontalPosition_Left(leafView, wpShape, pageAttr);            
	            }
	            else if(horPosition == WPAutoShape.ALIGNMENT_CENTER)
	            {
	            	//center alignment
	            	processHorizontalPosition_Center(leafView, wpShape, pageAttr);
	            }
	            else if(horPosition == WPAutoShape.ALIGNMENT_RIGHT)
	            {
	            	//right alignment
	            	processHorizontalPosition_Right(leafView, wpShape, pageAttr);
	            }
	            else if(horPosition == WPAutoShape.ALIGNMENT_INSIDE)
	            {
	            	processHorizontalPosition_Inside(leafView, wpShape, pageAttr);
	            }
	            else if(horPosition == WPAutoShape.ALIGNMENT_OUTSIDE)
	            {
	            	processHorizontalPosition_Outside(leafView, wpShape, pageAttr);
	            }
	    	}    	
	    }
	    
	    private void processHorizontalPosition_Absolute(LeafView leafView, WPAutoShape wpShape, PageAttr pageAttr)
	    {
	    	Rectangle r = wpShape.getBounds();
	    	
	    	byte horRelative = wpShape.getHorizontalRelativeTo();
	    	if (horRelative == WPAutoShape.RELATIVE_MARGIN
	    			|| horRelative == WPAutoShape.RELATIVE_PARAGRAPH
	        		|| horRelative == WPAutoShape.RELATIVE_COLUMN 
	        		|| horRelative == WPAutoShape.RELATIVE_CHARACTER)
	        {
	            leafView.setX(pageAttr.leftMargin + r.x);
	        }
	        else if(horRelative == WPAutoShape.RELATIVE_PAGE 
	        		|| horRelative ==  WPAutoShape.RELATIVE_LEFT)
	        {
	        	leafView.setX(r.x);
	        }
	        else if(horRelative == WPAutoShape.RELATIVE_RIGHT)
	        {
	        	leafView.setX(pageAttr.pageWidth - pageAttr.rightMargin + r.x);
	        }
	        else if(horRelative == WPAutoShape.RELATIVE_OUTER)
	        {
	        	if(leafView.getParentView() != null 
	        			&& leafView.getParentView().getParentView() != null 
	        			&& leafView.getParentView().getParentView().getParentView() != null)
	    		{
	        		PageView pageView = (PageView)leafView.getParentView().getParentView().getParentView();
	        		if(pageView.getPageNumber() % 2 == 1)
	        		{
	        			//Odd page
	        			leafView.setX(pageAttr.pageWidth - pageAttr.rightMargin + r.x);
	        		}
	        		else
	        		{
	        			//Even page
	        			leafView.setX(r.x);
	        		}    			
	    		}
	        }
	        else if(horRelative == WPAutoShape.RELATIVE_INNER)
	        {
	        	if(leafView.getParentView() != null 
	        			&& leafView.getParentView().getParentView() != null 
	        			&& leafView.getParentView().getParentView().getParentView() != null)
	    		{
	        		PageView pageView = (PageView)leafView.getParentView().getParentView().getParentView();
	        		if(pageView.getPageNumber() % 2 == 1)
	        		{
	        			//Odd page
	        			leafView.setX(r.x);        			
	        		}
	        		else
	        		{
	        			//Even page
	        			leafView.setX(pageAttr.pageWidth - pageAttr.rightMargin + r.x);
	        		}    			
	    		}
	        }
	    }
	    
	    private void processHorizontalPosition_Left(LeafView leafView, WPAutoShape wpShape, PageAttr pageAttr)
	    {
	    	Rectangle r = wpShape.getBounds();
	    	
	    	byte horRelative = wpShape.getHorizontalRelativeTo();
	    	if (horRelative == WPAutoShape.RELATIVE_MARGIN
	    			|| horRelative == WPAutoShape.RELATIVE_PARAGRAPH
	        		|| horRelative == WPAutoShape.RELATIVE_COLUMN 
	        		|| horRelative == WPAutoShape.RELATIVE_CHARACTER)
	        {
	            leafView.setX(pageAttr.leftMargin);
	        }
	        else if(horRelative == WPAutoShape.RELATIVE_PAGE 
	        		|| horRelative ==  WPAutoShape.RELATIVE_LEFT)
	        {
	        	leafView.setX(0);
	        }
	        else if(horRelative == WPAutoShape.RELATIVE_RIGHT)
	        {
	        	leafView.setX(pageAttr.pageWidth - pageAttr.rightMargin);
	        }
	        else if(horRelative == WPAutoShape.RELATIVE_OUTER)
	        {
	        	if(leafView.getParentView() != null 
	        			&& leafView.getParentView().getParentView() != null 
	        			&& leafView.getParentView().getParentView().getParentView() != null)
	    		{
	        		PageView pageView = (PageView)leafView.getParentView().getParentView().getParentView();
	        		if(pageView.getPageNumber() % 2 == 1)
	        		{
	        			//Odd page
	        			leafView.setX(pageAttr.pageWidth - pageAttr.rightMargin);
	        		}
	        		else
	        		{
	        			//Even page
	        			leafView.setX(0);
	        		}    			
	    		}
	        }
	        else if(horRelative == WPAutoShape.RELATIVE_INNER)
	        {
	        	if(leafView.getParentView() != null 
	        			&& leafView.getParentView().getParentView() != null 
	        			&& leafView.getParentView().getParentView().getParentView() != null)
	    		{
	        		PageView pageView = (PageView)leafView.getParentView().getParentView().getParentView();
	        		if(pageView.getPageNumber() % 2 == 1)
	        		{
	        			//Odd page
	        			leafView.setX(0);        			
	        		}
	        		else
	        		{
	        			//Even page
	        			leafView.setX(pageAttr.pageWidth - pageAttr.rightMargin);
	        		}    			
	    		}
	        }
	    }
	    
	    private void processHorizontalPosition_Center(LeafView leafView, WPAutoShape wpShape, PageAttr pageAttr)
	    {
	    	Rectangle r = wpShape.getBounds();
	    	int halfShapeWidth = r.width / 2;
	    	
	    	byte horRelative = wpShape.getHorizontalRelativeTo();
	    	if(horRelative == WPAutoShape.RELATIVE_PAGE)
	    	{
	    		leafView.setX(pageAttr.pageWidth / 2 - halfShapeWidth);
	    	}
	    	else if(horRelative == WPAutoShape.RELATIVE_MARGIN 
	    			|| horRelative == WPAutoShape.RELATIVE_COLUMN)
	    	{
	    		leafView.setX(pageAttr.leftMargin + (pageAttr.pageWidth - pageAttr.leftMargin - pageAttr.rightMargin) / 2 - halfShapeWidth);
	    	}
	    	else if(horRelative == WPAutoShape.RELATIVE_CHARACTER)
	    	{
	    		leafView.setX(pageAttr.leftMargin - halfShapeWidth);
	    	}
	    	else if(horRelative == WPAutoShape.RELATIVE_LEFT)
	    	{
	    		leafView.setX(pageAttr.leftMargin / 2 - halfShapeWidth);
	    	}
	    	else if(horRelative == WPAutoShape.RELATIVE_RIGHT)
	    	{
	    		leafView.setX(pageAttr.pageWidth - pageAttr.rightMargin / 2 - halfShapeWidth);
	    	}
	    	else if(horRelative == WPAutoShape.RELATIVE_OUTER)
	        {
	        	if(leafView.getParentView() != null 
	        			&& leafView.getParentView().getParentView() != null 
	        			&& leafView.getParentView().getParentView().getParentView() != null)
	    		{
	        		PageView pageView = (PageView)leafView.getParentView().getParentView().getParentView();
	        		if(pageView.getPageNumber() % 2 == 1)
	        		{
	        			//Odd page
	        			leafView.setX(pageAttr.pageWidth - pageAttr.rightMargin / 2 - halfShapeWidth);
	        		}
	        		else
	        		{
	        			//Even page
	        			leafView.setX(pageAttr.leftMargin / 2 - halfShapeWidth);
	        		}    			
	    		}
	        }
	        else if(horRelative == WPAutoShape.RELATIVE_INNER)
	        {
	        	if(leafView.getParentView() != null 
	        			&& leafView.getParentView().getParentView() != null 
	        			&& leafView.getParentView().getParentView().getParentView() != null)
	    		{
	        		PageView pageView = (PageView)leafView.getParentView().getParentView().getParentView();
	        		if(pageView.getPageNumber() % 2 == 1)
	        		{
	        			//Odd page
	        			leafView.setX(pageAttr.leftMargin / 2 - halfShapeWidth);      			
	        		}
	        		else
	        		{
	        			//Even page
	        			leafView.setX(pageAttr.pageWidth - pageAttr.rightMargin / 2 - halfShapeWidth);
	        		}    			
	    		}
	        }
	    }
	    
	    private void processHorizontalPosition_Right(LeafView leafView, WPAutoShape wpShape, PageAttr pageAttr)
	    {
	    	Rectangle r = wpShape.getBounds();    	
	    	byte horRelative = wpShape.getHorizontalRelativeTo();
	    	if(horRelative == WPAutoShape.RELATIVE_PAGE 
	    			|| horRelative == WPAutoShape.RELATIVE_RIGHT)
	    	{
	    		leafView.setX(pageAttr.pageWidth - r.width);
	    	}
	    	else if(horRelative == WPAutoShape.RELATIVE_MARGIN || horRelative == WPAutoShape.RELATIVE_COLUMN)
	    	{
	    		leafView.setX(pageAttr.pageWidth - pageAttr.rightMargin - r.width);
	    	}
	    	else if(horRelative == WPAutoShape.RELATIVE_CHARACTER 
	    			|| horRelative == WPAutoShape.RELATIVE_LEFT)
	    	{
	    		leafView.setX(pageAttr.leftMargin - r.width);
	    	}
	    	else if(horRelative == WPAutoShape.RELATIVE_OUTER)
	        {
	        	if(leafView.getParentView() != null 
	        			&& leafView.getParentView().getParentView() != null
	        			&& leafView.getParentView().getParentView().getParentView() != null)
	    		{
	        		PageView pageView = (PageView)leafView.getParentView().getParentView().getParentView();
	        		if(pageView.getPageNumber() % 2 == 1)
	        		{
	        			//Odd page
	        			leafView.setX(pageAttr.pageWidth - r.width);
	        		}
	        		else
	        		{
	        			//Even page
	        			leafView.setX(pageAttr.leftMargin - r.width);
	        		}    			
	    		}
	        }
	        else if(horRelative == WPAutoShape.RELATIVE_INNER)
	        {
	        	if(leafView.getParentView() != null 
	        			&& leafView.getParentView().getParentView() != null
	        			&& leafView.getParentView().getParentView().getParentView() != null)
	    		{
	        		PageView pageView = (PageView)leafView.getParentView().getParentView().getParentView();
	        		if(pageView.getPageNumber() % 2 == 1)
	        		{
	        			//Odd page
	        			leafView.setX(pageAttr.leftMargin - r.width);      			
	        		}
	        		else
	        		{
	        			//Even page
	        			leafView.setX(pageAttr.pageWidth - r.width);
	        		}    			
	    		}
	        }
	    }
	    
	    private void processHorizontalPosition_Inside(LeafView leafView, WPAutoShape wpShape, PageAttr pageAttr)
	    {
	    	if(leafView.getParentView() != null 
        			&& leafView.getParentView().getParentView() != null
        			&& leafView.getParentView().getParentView().getParentView() != null)
    		{
        		PageView pageView = (PageView)leafView.getParentView().getParentView().getParentView();
        		
        		Rectangle r = wpShape.getBounds();    	
    	    	byte horRelative = wpShape.getHorizontalRelativeTo();
    	    	
        		if(pageView.getPageNumber() % 2 == 1)
        		{
        			//Odd page
        			if(horRelative == WPAutoShape.RELATIVE_PAGE)
        	    	{
        	    		leafView.setX(0);
        	    	}
        	    	else if(horRelative == WPAutoShape.RELATIVE_MARGIN )
        	    	{
        	    		leafView.setX(pageAttr.leftMargin);
        	    	}  			
        		}
        		else
        		{
        			//Even page
        			if(horRelative == WPAutoShape.RELATIVE_PAGE)
        	    	{
        	    		leafView.setX(pageAttr.pageWidth - r.width);
        	    	}
        	    	else if(horRelative == WPAutoShape.RELATIVE_MARGIN )
        	    	{
        	    		leafView.setX(pageAttr.pageWidth - pageAttr.rightMargin - r.width);
        	    	}
        		}    			
    		}	    	
	    }
	    
	    private void processHorizontalPosition_Outside(LeafView leafView, WPAutoShape wpShape, PageAttr pageAttr)
	    {
	    	if(leafView.getParentView() != null 
        			&& leafView.getParentView().getParentView() != null
        			&& leafView.getParentView().getParentView().getParentView() != null)
    		{
        		PageView pageView = (PageView)leafView.getParentView().getParentView().getParentView();
        		
        		Rectangle r = wpShape.getBounds();    	
    	    	byte horRelative = wpShape.getHorizontalRelativeTo();
    	    	
        		if(pageView.getPageNumber() % 2 == 1)
        		{
        			//Odd page
        			if(horRelative == WPAutoShape.RELATIVE_PAGE)
        	    	{
        	    		leafView.setX(pageAttr.pageWidth - r.width);
        	    	}
        	    	else if(horRelative == WPAutoShape.RELATIVE_MARGIN )
        	    	{
        	    		leafView.setX(pageAttr.pageWidth - pageAttr.rightMargin - r.width);
        	    	}        					
        		}
        		else
        		{
        			//Even page
        			if(horRelative == WPAutoShape.RELATIVE_PAGE)
        	    	{
        	    		leafView.setX(0);
        	    	}
        	    	else if(horRelative == WPAutoShape.RELATIVE_MARGIN )
        	    	{
        	    		leafView.setX(pageAttr.leftMargin);
        	    	}  	
        		}    			
    		}	    	
	    }
	    
	    private void processVerticalPosition(LeafView leafView, WPAutoShape wpShape, PageAttr pageAttr)
	    {
	    	byte posType = wpShape.getVerPositionType();
	    	byte verRelative = wpShape.getVerticalRelativeTo();
	    	
	    	if(posType == WPAutoShape.POSITIONTYPE_RELATIVE)
	    	{
	    		//relative postion
	    		float ratio = wpShape.getVerRelativeValue() / 1000f;
	    		
	    		if(verRelative == WPAutoShape.RELATIVE_PAGE)
	    		{
	    			leafView.setY(Math.round(pageAttr.pageHeight * ratio));
	    		}
	    		else if(verRelative == WPAutoShape.RELATIVE_MARGIN)
	    		{
	    			leafView.setY(pageAttr.topMargin + Math.round((pageAttr.pageHeight - pageAttr.topMargin - pageAttr.bottomMargin) * ratio));
	    		}
	    		else if(verRelative ==  WPAutoShape.RELATIVE_TOP)
	            {
	    			leafView.setY(Math.round(pageAttr.topMargin * ratio));
	            }
	            else if(verRelative == WPAutoShape.RELATIVE_BOTTOM)
	            {
	            	leafView.setY(pageAttr.pageHeight - pageAttr.bottomMargin + Math.round(pageAttr.bottomMargin * ratio));
	            }
	            else if(verRelative == WPAutoShape.RELATIVE_OUTER 
	            		|| verRelative == WPAutoShape.RELATIVE_INNER)
	            {
	            	if(leafView.getParentView() != null 
		        			&& leafView.getParentView().getParentView() != null
		        			&& leafView.getParentView().getParentView().getParentView() != null)
		    		{
		        		PageView pageView = (PageView)leafView.getParentView().getParentView().getParentView();
		        		if(pageView.getPageNumber() % 2 == 1)
		        		{
		        			//Odd page
		        			leafView.setY(Math.round(pageAttr.topMargin * ratio));
		        		}
		        		else
		        		{
		        			//Even page
		        			leafView.setY(pageAttr.pageHeight - pageAttr.bottomMargin + Math.round(pageAttr.bottomMargin * ratio));
		        		}    			
		    		}
	            }
	    	}
	    	else
	    	{
	    		byte verPosition = wpShape.getVerticalAlignment();
	    		if(verPosition == WPAutoShape.ALIGNMENT_ABSOLUTE)
	    		{
	    			processVerticalPosition_Absolute(leafView, wpShape, pageAttr);
	    		}
	    		else if(verPosition == WPAutoShape.ALIGNMENT_TOP)
	    		{
	    			processVerticalPosition_Top(leafView, wpShape, pageAttr);
	    		}
	    		else if(verPosition == WPAutoShape.ALIGNMENT_CENTER)
	    		{
	    			processVerticalPosition_Center(leafView, wpShape, pageAttr);
	    		}
	    		else if(verPosition == WPAutoShape.ALIGNMENT_BOTTOM)
	    		{
	    			processVerticalPosition_Bottom(leafView, wpShape, pageAttr);
	    		}
	    		else if(verPosition == WPAutoShape.ALIGNMENT_INSIDE)
	    		{
	    			processVerticalPosition_Inside(leafView, wpShape, pageAttr);
	    		}
	    		else if(verPosition == WPAutoShape.ALIGNMENT_OUTSIDE)
	    		{
	    			processVerticalPosition_Outside(leafView, wpShape, pageAttr);
	    		}
	    	}
	    }
	    
	    private void processVerticalPosition_Absolute(LeafView leafView, WPAutoShape wpShape, PageAttr pageAttr)
	    {
	    	Rectangle r = wpShape.getBounds();
	    	byte verRelativeTo = wpShape.getVerticalRelativeTo();
	    	
	    	if(verRelativeTo == WPAutoShape.RELATIVE_PAGE 
	    			|| verRelativeTo == WPAutoShape.RELATIVE_TOP)
	    	{
	    		leafView.setY(r.y);
	    	}
	    	else if(verRelativeTo == WPAutoShape.RELATIVE_INNER
	    			|| verRelativeTo == WPAutoShape.RELATIVE_OUTER)
	    	{
	    		if(leafView.getParentView() != null 
	    				&& leafView.getParentView().getParentView() != null 
	    				&& leafView.getParentView().getParentView().getParentView() != null)
	    		{
	        		PageView pageView = (PageView)leafView.getParentView().getParentView().getParentView();
	        		if(pageView.getPageNumber() % 2 == 1)
	        		{
	        			//Odd page
	        			leafView.setY(r.y);      			
	        		}
	        		else
	        		{
	        			//Even page
	        			leafView.setY(pageAttr.pageHeight - pageAttr.bottomMargin + r.y);
	        		}    			
	    		}
	    	}
	    	else if(verRelativeTo == WPAutoShape.RELATIVE_MARGIN)
	    	{
	    		leafView.setY(pageAttr.topMargin + r.y);
	    	}
	    	else if(verRelativeTo == WPAutoShape.RELATIVE_PARAGRAPH
	    			|| verRelativeTo == WPAutoShape.RELATIVE_LINE)
	    	{
	    		if(leafView.getParentView() != null 
	    				&& leafView.getParentView().getParentView() instanceof ParagraphView)
	    		{
	    			ParagraphView paraView = (ParagraphView)leafView.getParentView().getParentView();
	    			leafView.setY(paraView.getY() + r.y);
	    		}
	    	}
	    	else if(verRelativeTo == WPAutoShape.RELATIVE_BOTTOM)
	    	{
	    		leafView.setY(pageAttr.pageHeight - pageAttr.bottomMargin + r.y);
	    	}
	    }
	    
	    private void processVerticalPosition_Top(LeafView leafView, WPAutoShape wpShape, PageAttr pageAttr)
	    {
	    	Rectangle r = wpShape.getBounds();
	    	byte verRelativeTo = wpShape.getVerticalRelativeTo();
	    	
	    	if(verRelativeTo == WPAutoShape.RELATIVE_PAGE 
	    			|| verRelativeTo == WPAutoShape.RELATIVE_TOP)
	    	{
	    		leafView.setY(0);
	    	}
	    	else if(verRelativeTo == WPAutoShape.RELATIVE_INNER
	    			|| verRelativeTo == WPAutoShape.RELATIVE_OUTER)
	    	{
	    		if(leafView.getParentView() != null 
	    				&& leafView.getParentView().getParentView() != null 
	    				&& leafView.getParentView().getParentView().getParentView() != null)
	    		{
	        		PageView pageView = (PageView)leafView.getParentView().getParentView().getParentView();
	        		if(pageView.getPageNumber() % 2 == 1)
	        		{
	        			//Odd page
	        			leafView.setY(0);      			
	        		}
	        		else
	        		{
	        			//Even page
	        			leafView.setY(pageAttr.pageHeight - pageAttr.bottomMargin);
	        		}    			
	    		}
	    	}
	    	else if(verRelativeTo == WPAutoShape.RELATIVE_MARGIN)
	    	{
	    		leafView.setY(pageAttr.topMargin);
	    	}
	    	else if(verRelativeTo == WPAutoShape.RELATIVE_PARAGRAPH
	    			|| verRelativeTo == WPAutoShape.RELATIVE_LINE)
	    	{
	    		if(leafView.getParentView() != null 
	    				&& leafView.getParentView().getParentView() instanceof ParagraphView)
	    		{
	    			ParagraphView paraView = (ParagraphView)leafView.getParentView().getParentView();
	    			leafView.setY(paraView.getY());
	    		}
	    	}
	    	else if(verRelativeTo == WPAutoShape.RELATIVE_BOTTOM)
	    	{
	    		leafView.setY(pageAttr.pageHeight - pageAttr.bottomMargin);
	    	}
	    }
	    
	    private void processVerticalPosition_Center(LeafView leafView, WPAutoShape wpShape, PageAttr pageAttr)
	    {
	    	Rectangle r = wpShape.getBounds();
	    	byte verRelativeTo = wpShape.getVerticalRelativeTo();
	    	int halfShapeHeight = r.height / 2;
	    	if(verRelativeTo == WPAutoShape.RELATIVE_PAGE)
	    	{
	    		leafView.setY(pageAttr.pageHeight / 2 - halfShapeHeight);
	    	}
	    	else if(verRelativeTo == WPAutoShape.RELATIVE_MARGIN)
	    	{
	    		leafView.setY(pageAttr.topMargin + (pageAttr.pageHeight - pageAttr.topMargin - pageAttr.bottomMargin) / 2 - halfShapeHeight);
	    	}
	    	else if(verRelativeTo == WPAutoShape.RELATIVE_TOP)
	    	{
	    		leafView.setY(pageAttr.topMargin / 2 - halfShapeHeight);
	    	}
	    	else if(verRelativeTo == WPAutoShape.RELATIVE_INNER
	    			|| verRelativeTo == WPAutoShape.RELATIVE_OUTER)
	    	{
	    		if(leafView.getParentView() != null 
	    				&& leafView.getParentView().getParentView() != null 
	    				&& leafView.getParentView().getParentView().getParentView() != null)
	    		{
	        		PageView pageView = (PageView)leafView.getParentView().getParentView().getParentView();
	        		if(pageView.getPageNumber() % 2 == 1)
	        		{
	        			//Odd page
	        			leafView.setY(pageAttr.topMargin / 2 - halfShapeHeight);     			
	        		}
	        		else
	        		{
	        			//Even page
	        			leafView.setY(pageAttr.pageHeight - pageAttr.bottomMargin / 2 - halfShapeHeight);
	        		}    			
	    		}
	    	}
	    	else if(verRelativeTo == WPAutoShape.RELATIVE_BOTTOM)
	    	{
	    		leafView.setY(pageAttr.pageHeight - pageAttr.bottomMargin / 2 - halfShapeHeight);
	    	}
	    	else if(verRelativeTo == WPAutoShape.RELATIVE_PARAGRAPH
	    			|| verRelativeTo == WPAutoShape.RELATIVE_LINE)
	    	{
	    		if(leafView.getParentView() != null 
	    				&& leafView.getParentView().getParentView() instanceof ParagraphView)
	    		{
	    			ParagraphView paraView = (ParagraphView)leafView.getParentView().getParentView();
	    			leafView.setY(paraView.getY() - halfShapeHeight);
	    		}
	    	}
	    }
	    
	    private void processVerticalPosition_Bottom(LeafView leafView, WPAutoShape wpShape, PageAttr pageAttr)
	    {
	    	Rectangle r = wpShape.getBounds();
	    	byte verRelativeTo = wpShape.getVerticalRelativeTo();
	    	
	    	if(verRelativeTo == WPAutoShape.RELATIVE_PAGE || verRelativeTo == WPAutoShape.RELATIVE_BOTTOM)
	    	{
	    		leafView.setY(pageAttr.pageHeight - r.height);
	    	}
	    	else if(verRelativeTo == WPAutoShape.RELATIVE_MARGIN)
	    	{
	    		leafView.setY(pageAttr.pageHeight - pageAttr.bottomMargin - r.height);
	    	}
	    	else if(verRelativeTo == WPAutoShape.RELATIVE_PARAGRAPH
	    			|| verRelativeTo == WPAutoShape.RELATIVE_LINE)
	    	{
	    		if(leafView.getParentView() != null 
	    				&& leafView.getParentView().getParentView() instanceof ParagraphView)
	    		{
	    			ParagraphView paraView = (ParagraphView)leafView.getParentView().getParentView();
	    			leafView.setY(paraView.getY() + paraView.getHeight() - r.height);
	    		}
	    	}
	    	else if(verRelativeTo == WPAutoShape.RELATIVE_TOP)
	    	{
	    		leafView.setY(pageAttr.topMargin - r.height);
	    	}
	    	else if(verRelativeTo == WPAutoShape.RELATIVE_INNER
	    			|| verRelativeTo == WPAutoShape.RELATIVE_OUTER)
	    	{
	    		if(leafView.getParentView() != null 
	    				&& leafView.getParentView().getParentView() != null 
	    				&& leafView.getParentView().getParentView().getParentView() != null)
	    		{
	        		PageView pageView = (PageView)leafView.getParentView().getParentView().getParentView();
	        		if(pageView.getPageNumber() % 2 == 1)
	        		{
	        			//Odd page
	        			leafView.setY(pageAttr.topMargin - r.height);    			
	        		}
	        		else
	        		{
	        			//Even page
	        			leafView.setY(pageAttr.pageHeight - r.height);
	        		}    			
	    		}
	    	}
	    }

	    private void processVerticalPosition_Inside(LeafView leafView, WPAutoShape wpShape, PageAttr pageAttr)
	    {
	    	Rectangle r = wpShape.getBounds();
	    	byte verRelativeTo = wpShape.getVerticalRelativeTo();
	    	if(leafView.getParentView() != null 
	    			&& leafView.getParentView().getParentView() != null 
	    			&& leafView.getParentView().getParentView().getParentView() != null)
			{
	    		PageView pageView = (PageView)leafView.getParentView().getParentView().getParentView();
	    		if(pageView.getPageNumber() % 2 == 1)
	    		{
	    			//Odd page
	    			if(verRelativeTo == WPAutoShape.RELATIVE_PAGE)
	    			{
	    				leafView.setY(pageAttr.headerMargin / 2);
	    			}
	    			else if(verRelativeTo == WPAutoShape.RELATIVE_MARGIN)
	    			{
	    				leafView.setY(pageAttr.topMargin);
	    			}
	    			else if(verRelativeTo == WPAutoShape.RELATIVE_PARAGRAPH
	    	    			|| verRelativeTo == WPAutoShape.RELATIVE_LINE)
	    			{
	    				ParagraphView paraView = (ParagraphView)leafView.getParentView().getParentView();
	    				leafView.setY(paraView.getY());
	    			}
	    			else if(verRelativeTo == WPAutoShape.RELATIVE_TOP)
	    			{
	    				leafView.setY(0);
	    			}
	    			else if(verRelativeTo == WPAutoShape.RELATIVE_BOTTOM)
	    			{
	    				leafView.setY(pageAttr.pageHeight - pageAttr.bottomMargin);
	    			}
	    			else if(verRelativeTo == WPAutoShape.RELATIVE_INNER 
	    					|| verRelativeTo == WPAutoShape.RELATIVE_OUTER)
	    			{
	    				leafView.setY(0);
	    			}
	    		}
	    		else
	    		{
	    			//Even page
	    			if(verRelativeTo == WPAutoShape.RELATIVE_PAGE)
	    			{
	    				leafView.setY(pageAttr.pageHeight - pageAttr.footerMargin);
	    			}
	    			else if(verRelativeTo == WPAutoShape.RELATIVE_MARGIN)
	    			{
	    				leafView.setY(pageAttr.pageHeight - pageAttr.bottomMargin - r.height);
	    			}
	    			else if(verRelativeTo == WPAutoShape.RELATIVE_PARAGRAPH
	    	    			|| verRelativeTo == WPAutoShape.RELATIVE_LINE)
	    			{
	    				ParagraphView paraView = (ParagraphView)leafView.getParentView().getParentView();
	    				leafView.setY(paraView.getY() + paraView.getHeight() - r.height);
	    			}
	    			else if(verRelativeTo == WPAutoShape.RELATIVE_TOP)
	    			{
	    				leafView.setY(pageAttr.topMargin - r.height);
	    			}
	    			else if(verRelativeTo == WPAutoShape.RELATIVE_BOTTOM)
	    			{
	    				leafView.setY(pageAttr.pageHeight - r.height);
	    			}
	    			else if(verRelativeTo == WPAutoShape.RELATIVE_INNER 
	    					|| verRelativeTo == WPAutoShape.RELATIVE_OUTER)
	    			{
	    				leafView.setY(pageAttr.pageHeight - r.height);
	    			}
	    		}    			
			}
	    }
	    
	    private void processVerticalPosition_Outside(LeafView leafView, WPAutoShape wpShape, PageAttr pageAttr)
	    {
	    	Rectangle r = wpShape.getBounds();
	    	byte verRelativeTo = wpShape.getVerticalRelativeTo();
	    	if(leafView.getParentView() != null 
	    			&& leafView.getParentView().getParentView() != null 
	    			&& leafView.getParentView().getParentView().getParentView() != null)
			{
	    		PageView pageView = (PageView)leafView.getParentView().getParentView().getParentView();
	    		if(pageView.getPageNumber() % 2 == 1)
	    		{
	    			//Odd page
	    			if(verRelativeTo == WPAutoShape.RELATIVE_PAGE)
	    			{
	    				leafView.setY(pageAttr.pageHeight - pageAttr.footerMargin);
	    			}
	    			else if(verRelativeTo == WPAutoShape.RELATIVE_MARGIN)
	    			{
	    				leafView.setY(pageAttr.pageHeight - pageAttr.bottomMargin - r.height);
	    			}
	    			else if(verRelativeTo == WPAutoShape.RELATIVE_PARAGRAPH
	    	    			|| verRelativeTo == WPAutoShape.RELATIVE_LINE)
	    			{
	    				ParagraphView paraView = (ParagraphView)leafView.getParentView().getParentView();
	    				leafView.setY(paraView.getY() + paraView.getHeight() - r.height);
	    			}
	    			else if(verRelativeTo == WPAutoShape.RELATIVE_TOP)
	    			{
	    				leafView.setY(pageAttr.topMargin - r.height);
	    			}
	    			else if(verRelativeTo == WPAutoShape.RELATIVE_BOTTOM)
	    			{
	    				leafView.setY(pageAttr.pageHeight - r.height);
	    			}
	    			else if(verRelativeTo == WPAutoShape.RELATIVE_INNER 
	    					|| verRelativeTo == WPAutoShape.RELATIVE_OUTER)
	    			{
	    				leafView.setY(pageAttr.topMargin - r.height);
	    			}
	    		}
	    		else
	    		{
	    			//Even page
	    			if(verRelativeTo == WPAutoShape.RELATIVE_PAGE)
	    			{
	    				leafView.setY(pageAttr.headerMargin / 2);
	    			}
	    			else if(verRelativeTo == WPAutoShape.RELATIVE_MARGIN)
	    			{
	    				leafView.setY(pageAttr.topMargin);
	    			}
	    			else if(verRelativeTo == WPAutoShape.RELATIVE_PARAGRAPH
	    	    			|| verRelativeTo == WPAutoShape.RELATIVE_LINE)
	    			{
	    				ParagraphView paraView = (ParagraphView)leafView.getParentView().getParentView();
	    				leafView.setY(paraView.getY());
	    			}
	    			else if(verRelativeTo == WPAutoShape.RELATIVE_TOP)
	    			{
	    				leafView.setY(0);
	    			}
	    			else if(verRelativeTo == WPAutoShape.RELATIVE_BOTTOM)
	    			{
	    				leafView.setY(pageAttr.pageHeight - pageAttr.bottomMargin);
	    			}
	    			else if(verRelativeTo == WPAutoShape.RELATIVE_INNER 
	    					|| verRelativeTo == WPAutoShape.RELATIVE_OUTER)
	    			{
	    				leafView.setY(pageAttr.pageHeight - pageAttr.bottomMargin);
	    			}
	    		}    			
			}
	    }   
}
