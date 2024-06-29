package com.nvqquy98.lib.doc.office.fc.doc;

import java.util.ArrayList;
import java.util.List;

import com.nvqquy98.lib.doc.office.common.autoshape.pathbuilder.ArrowPathAndTail;
import com.nvqquy98.lib.doc.office.common.autoshape.pathbuilder.LineArrowPathBuilder;
import com.nvqquy98.lib.doc.office.common.shape.WPAutoShape;

import android.graphics.Path;
import android.graphics.PointF;

/**
 * http://www.w3.org/TR/NOTE-VML
 * 
 * command 	Name 			parameters 	Description 
 *	m 		moveto 				2 		Start a new sub-path at the given (x,y) coordinate 
 *	l 		lineto 				2* 		Draw a line from the current point to the given (x,y) coordinate which becomes the new current point. 
 *										A number of coordinate pairs may be specified to form a polyline. 
 *	c 		curveto 			6* 		Draw a cubic bezier curve from the current point to the coordinate given by the final two parameters, 
 *										the control points given by the first four parameters. The current point becomes the end point of the b锟絲ier. 
 *	x 		close 				0 		Close the current sub-path by drawing a straight line from the current point to the original moveto point. 
 *	e 		end 				0 		End the current set of sub-paths. A given set of sub-paths (as delimited by end) is filled using eofill. 
 *										Subsequent sets of sub-paths are filled independently and superimposed on existing ones. 
 *	t 		rmoveto 			2* 		Start a new sub-path at the coordinate (cpx+x, cpy+y). 
 *	r 		rlineto 			2* 		Draw a line from the current point to the given relative coordinate (cpx+x, cpy+y). 
 *	v 		rcurveto 			6* 		Cubic bezier curve using the given coordinate relative to the current point. 
 *	nf 		nofill 				0 		The current set of sub-paths (delimited by end - e) will not be filled. 
 *	ns 		nostroke 			0 		The current set of sub-paths (delimited by end - e) will not be filled. 
 *	ae 		angleellipseto 		6* 		center (x,y) size(w,h) start-angle, end-angle. Draw a segment of an ellipse as describes using these parameters. 
 *										A straight line is drawn from the current point to the start point of the segment. 
 *	al 		angleellipse 		6* 		Same as angleellipseto except that there is an implied moveto the starting point of the segment. 
 *	at 		arcto 				8* 		left, top, right, bottom start(x,y) end(x,y). The first four values define the bounding box of an ellipse. 
 *										The last four define two radial vectors. A segment of the ellipse is drawn which starts at the angle defined 
 *										by the start radius vector and ends at the angle defined by the end vector. A straight line is drawn from the 
 *										current point to the start of the arc. The arc is always drawn in a counterclockwise direction. 
 *	ar 		arc 				8* 		left, top, right, bottom start(x,y) end(x,y). Same as arcto however a new sub-path is started by an implied moveto 
 *										the start point of the arc. 
 *	wa 		clockwisearcto 		8* 		left, top, right, bottom start(x,y) end(x,y). Same as arcto but the arc is drawn in a clockwise direction. 
 *	wr 		clockwisearc 		8* 		left, top, right, bottom start(x,y) end(x,y). Same as arc but the arc is drawn in a clockwise direction 
 *	qx 		ellipticalqaudrantx 2* 		end(x,y). A quarter ellipse is drawn from the current point to the given end point. The elliptical segment 
 *										is initially tangential to a line parallel to the x-axis. (i.e. the segment starts out horizontal)
 * 
 *	qy 		ellipticalquadranty 2* 		end(x,y).Same as ellipticalquadrantx except that the elliptical segment is initially tangential 
 *										to a line parallel to the y-axis. (i.e. the segment starts out vertical)
 * 
 *	qb 		quadraticbezier 	2+2* 	(controlpoint(x,y))*, end(x,y) Defines one or more quadratic bezier curves by means of control points 
 *										and an end point.  Intermediate (on-curve) points are obtained by interpolation between successive control points 
 *										as in the OpenType font specification.  The sub-path need not be started in which case the sub-path will be closed.
 *										In this case the last point of the sub-path defines the start point of the quadratic bezier.

 * @author jqin
 *
 */

public class VMLPathParser 
{
	public final static byte Command_Invalid = -1;
	
	public final static byte Command_MoveTo = 0;
	
	public final static byte Command_LineTo = Command_MoveTo + 1;
	
	public final static byte Command_CurveTo = Command_LineTo + 1;
	
	public final static byte Command_Close = Command_CurveTo + 1;
	
	public final static byte Command_End = Command_Close + 1;
	
	public final static byte Command_RMoveTo = Command_End + 1;
	
	public final static byte Command_RLineTo = Command_RMoveTo + 1;
	
	public final static byte Command_RCurveTo = Command_RLineTo + 1;
	
	public final static byte Command_NoFill = Command_RCurveTo + 1;
	
	public final static byte Command_NoStroke = Command_NoFill + 1;
	
	public final static byte Command_AngleEllipseTo = Command_NoStroke + 1;
	
	public final static byte Command_AngleEllipse = Command_AngleEllipseTo + 1;
	
	public final static byte Command_ArcTo = Command_AngleEllipse + 1;
	
	public final static byte Command_Arc = Command_ArcTo +1;
	
	public final static byte Command_ClockwiseArcTo = Command_Arc + 1;
	
	public final static byte Command_ClockwiseArc = Command_ClockwiseArcTo + 1;
	
	private final static byte Command_EllipticalQaudrantX = Command_ClockwiseArc + 1;
	
	private final static byte Command_EllipticalQaudrantY = Command_EllipticalQaudrantX + 1;
	
	private final static byte Command_QuadraticBezier = Command_EllipticalQaudrantY + 1;
	
	private static VMLPathParser instance = new VMLPathParser();
	
	private static byte NodeType_Invalidate = -1;
	private static byte NodeType_Start = 0;
	private static byte NodeType_Middle = 1;
	private static byte NodeType_End = 2;
	
	private byte currentNodeType = NodeType_Invalidate;
	private byte preNodeType = NodeType_Invalidate;
	
	private PointF preNode = new PointF();
	private PointF ctrNode1 = new PointF();
	private PointF ctrNode2 = new PointF();
	private PointF nextNode = new PointF();
	
	Path startArrowPath = null;
	Path endArrowPath = null;
	
	private VMLPathParser()
	{		
	}
	
	public static VMLPathParser instance()
	{
		return instance;
	}
	
	public PathWithArrow createPath(WPAutoShape autoshape, String pathContext, int lineWidth)
	{
		try
		{
			index = 0;
			startArrowPath = null;
			endArrowPath = null;
			
			List<Path> pathList = new ArrayList<Path>();
			Path path= null;
			boolean newPath = true;
			byte command = nextCommand(pathContext);
			byte nextCommand = command;
			currentNodeType = NodeType_Start;
			preNodeType = NodeType_Invalidate;
					
			while(command != Command_Invalid)
			{
				if(command == Command_End)
				{
					newPath = true;
					nextCommand = nextCommand(pathContext);
					if(nextCommand == Command_Invalid)
					{
						currentNodeType = NodeType_End;
					}
				}
				else
				{
					if(newPath)
					{
						newPath = false;
						path = new Path();
						pathList.add(path);						
					}
					
					Integer[] paras = nextParameters(pathContext);
					
					nextCommand = nextCommand(pathContext);
					if(nextCommand == Command_Invalid || nextCommand == Command_End)
					{
						currentNodeType = NodeType_End;
					}
					
					processPath(autoshape, lineWidth, path, command, paras);
					
					preNodeType = currentNodeType;
					currentNodeType = NodeType_Middle;
				}
				
				command = nextCommand;
			}
			
			PathWithArrow pathWithArrow = new PathWithArrow(pathList.toArray(new Path[pathList.size()]), startArrowPath, endArrowPath);
			startArrowPath = null;
			endArrowPath = null;
			return pathWithArrow;
		}
		catch(Exception e)
		{
			return  null;
		}		
	}
	
	/**
	 * 
	 * @param pathContext
	 * @return
	 */
	private byte nextCommand(String pathContext)
	{
		builder.delete(0, builder.length());
		
		while(index < pathContext.length() && Character.isLetter(pathContext.charAt(index)))
		{
			builder.append(pathContext.charAt(index++));
		}
		
		String comm = builder.toString();
		if(comm.contains("h"))
		{
			//eg.ha,hb,hc,hd,he,hf,hg,hh,hi
			comm = comm.substring(2);
		}		
		
		if("m".equalsIgnoreCase(comm))
		{
			return Command_MoveTo;
		}
		else if("l".equalsIgnoreCase(comm))
		{
			return Command_LineTo;
		}
		else if("c".equalsIgnoreCase(comm))
		{
			return Command_CurveTo;
		}
		else if("x".equalsIgnoreCase(comm))
		{
			return Command_Close;
		}
		else if("e".equalsIgnoreCase(comm))
		{
			return Command_End;
		}
		else if("t".equalsIgnoreCase(comm))
		{
			return Command_RMoveTo;
		}
		else if("r".equalsIgnoreCase(comm))
		{
			return Command_RLineTo;
		}
		else if("v".equalsIgnoreCase(comm))
		{
			return Command_RCurveTo;
		}		
		else if("nf".equalsIgnoreCase(comm))
		{
			return Command_NoFill;
		}
		else if("ns".equalsIgnoreCase(comm))
		{
			return Command_NoStroke;
		}
		else if("ae".equalsIgnoreCase(comm))
		{
			return Command_AngleEllipseTo;
		}
		else if("al".equalsIgnoreCase(comm))
		{
			return Command_AngleEllipse;
		}
		else if("at".equalsIgnoreCase(comm))
		{
			return Command_ArcTo;
		}
		else if("ar".equalsIgnoreCase(comm))
		{
			return Command_Arc;
		}
		else if("wa".equalsIgnoreCase(comm))
		{
			return Command_ClockwiseArcTo;
		}
		else if("wr".equalsIgnoreCase(comm))
		{
			return Command_ClockwiseArc;
		}
		else if("qx".equalsIgnoreCase(comm))
		{
			return Command_EllipticalQaudrantX;
		}
		else if("qy".equalsIgnoreCase(comm))
		{
			return Command_EllipticalQaudrantY;
		}
		else if("qb".equalsIgnoreCase(comm))
		{
			return Command_QuadraticBezier;
		}
		else if(comm.contains("x") || comm.contains("X"))
		{
			//eg.xm,xe...
			index = index - (comm.length() - 1);
			return Command_Close; 
		}
		return Command_Invalid;
	}
	
	private Integer[] nextParameters(String pathContext)
	{		
		paraList.clear();
		int[] point = null;
		while(hasNextPoint(pathContext))
		{
			point = nextPoint(pathContext);
			
			paraList.add(point[0]);
			paraList.add(point[1]);
		}
		return paraList.toArray(new Integer[paraList.size()]);
	}
	
	private boolean hasNextPoint(String pathContext)
	{
		return index < pathContext.length() && !Character.isLetter(pathContext.charAt(index));		
	}
	
	private int[] nextPoint(String pathContext)
	{
		int[] point = new int[2];
		
		//x
		builder.delete(0, builder.length());		
		while(index < pathContext.length() 
				&& (Character.isDigit(pathContext.charAt(index)) || pathContext.charAt(index) == '-'))
		{
			builder.append(pathContext.charAt(index++));
		}
		
		if(builder.length() > 0)
		{
			point[0] = Integer.parseInt(builder.toString());
		}
		
		//
		if(index < pathContext.length() && pathContext.charAt(index) == ',')
		{
			index++;
			//y
			builder.delete(0, builder.length());		
			while(index < pathContext.length() 
					&& (Character.isDigit(pathContext.charAt(index)) || pathContext.charAt(index) == '-'))
			{
				builder.append(pathContext.charAt(index++));
			}
			
			if(builder.length() > 0)
			{
				point[1] = Integer.parseInt(builder.toString());
			}
			
			if(index < pathContext.length() && pathContext.charAt(index) == ',')
			{
				index++;
			}
		}
		
		return point;
	}
	
	private void processPath(WPAutoShape autoshape, int lineWidth, Path path, byte command, Integer[] parameters)
	{
		ArrowPathAndTail startArrowPathAndTail = null;
		ArrowPathAndTail endArrowPathAndTail = null;
		
		if(preNodeType == NodeType_Start && autoshape != null && autoshape.getStartArrowhead())
		{
			//start arrow
			switch(command)
			{
				case Command_LineTo:
					startArrowPathAndTail = LineArrowPathBuilder.getDirectLineArrowPath(parameters[0], 
							parameters[1], nextNode.x, nextNode.y, autoshape.getStartArrow(), lineWidth);
					break;
					
				case Command_CurveTo:
					startArrowPathAndTail = LineArrowPathBuilder.getCubicBezArrowPath(parameters[4], parameters[5], 
							parameters[2], parameters[3],parameters[0], parameters[1], nextNode.x, nextNode.y, 
							autoshape.getStartArrow(), lineWidth);
					break;
					
				case Command_RLineTo:
					startArrowPathAndTail = LineArrowPathBuilder.getDirectLineArrowPath(parameters[0] + nextNode.x, 
							parameters[1] + nextNode.y, nextNode.x, nextNode.y, autoshape.getStartArrow(), lineWidth);
					break;
					
				case Command_RCurveTo:
					startArrowPathAndTail = LineArrowPathBuilder.getCubicBezArrowPath(parameters[4] + nextNode.x, parameters[5] + nextNode.y, 
							parameters[2] + nextNode.x, parameters[3] + nextNode.y,parameters[0] + nextNode.x, parameters[1] + nextNode.y, 
							nextNode.x, nextNode.y, autoshape.getStartArrow(), lineWidth);
					break;
			}
		}
		
		if(currentNodeType == NodeType_End && autoshape != null && autoshape.getEndArrowhead())
		{
			int cnt = parameters.length;
			//end arrow
			switch(command)
			{
				case Command_LineTo:
					if(cnt > 2)
					{
						//more than two nodes
						endArrowPathAndTail = LineArrowPathBuilder.getDirectLineArrowPath(parameters[cnt - 4], parameters[cnt - 3], 
								parameters[cnt - 2], parameters[cnt - 1],
								autoshape.getEndArrow(), lineWidth);
					}
					else
					{
						endArrowPathAndTail = LineArrowPathBuilder.getDirectLineArrowPath(nextNode.x, nextNode.y, 
								parameters[cnt - 2], parameters[cnt - 1],
								autoshape.getEndArrow(), lineWidth);
					}					
					break;
					
				case Command_CurveTo:
					if(cnt > 6)
					{
						//more than two nodes
						endArrowPathAndTail = LineArrowPathBuilder.getCubicBezArrowPath(parameters[cnt - 8], parameters[cnt - 7],
								parameters[cnt - 6], parameters[cnt - 5], 
								parameters[cnt - 4], parameters[cnt - 3], 
								parameters[cnt - 2], parameters[cnt - 1], 
								autoshape.getEndArrow(), lineWidth);
					}
					else
					{
						endArrowPathAndTail = LineArrowPathBuilder.getCubicBezArrowPath( nextNode.x, nextNode.y,
								parameters[cnt - 6], parameters[cnt - 5], 
								parameters[cnt - 4], parameters[cnt - 3], 
								parameters[cnt - 2], parameters[cnt - 1], 
								autoshape.getEndArrow(), lineWidth);
					}					
					break;
					
				case Command_RLineTo:
					if(cnt > 2)
					{
						//more than two nodes
						endArrowPathAndTail = LineArrowPathBuilder.getDirectLineArrowPath(parameters[cnt - 4] + nextNode.x, parameters[cnt - 3] + nextNode.y, 
								parameters[cnt - 2] + nextNode.x, parameters[cnt - 1] + nextNode.y, 
								autoshape.getEndArrow(), lineWidth);
					}
					else
					{
						endArrowPathAndTail = LineArrowPathBuilder.getDirectLineArrowPath(nextNode.x, nextNode.y, 
								parameters[cnt - 2] + nextNode.x, parameters[cnt - 1] + nextNode.y, 
								autoshape.getEndArrow(), lineWidth);
					}
					break;
					
				case Command_RCurveTo:
					if(cnt > 6)
					{
						endArrowPathAndTail = LineArrowPathBuilder.getCubicBezArrowPath(parameters[cnt - 8] + nextNode.x, parameters[cnt - 7] + nextNode.y, 
								parameters[cnt - 6] + nextNode.x, parameters[cnt - 5] + nextNode.y, 
								parameters[cnt - 4] + nextNode.x, parameters[cnt - 3] + nextNode.y, 
								parameters[cnt - 2] + nextNode.x, parameters[cnt - 1] + nextNode.y,
								autoshape.getEndArrow(), lineWidth);
					}
					else
					{
						endArrowPathAndTail = LineArrowPathBuilder.getCubicBezArrowPath(nextNode.x, nextNode.y, 
								parameters[cnt - 6] + nextNode.x, parameters[cnt - 5] + nextNode.y, 
								parameters[cnt - 4] + nextNode.x, parameters[cnt - 3] + nextNode.y, 
								parameters[cnt - 2] + nextNode.x, parameters[cnt - 1] + nextNode.y,
								autoshape.getEndArrow(), lineWidth);
					}
					break;
			}
		}

		if(startArrowPathAndTail != null)
		{
			startArrowPath = startArrowPathAndTail.getArrowPath();
            
			path.reset();
			PointF pos = LineArrowPathBuilder.getReferencedPosition(nextNode.x, nextNode.y, startArrowPathAndTail.getArrowTailCenter().x, startArrowPathAndTail.getArrowTailCenter().y, autoshape.getStartArrowType());
			
			path.moveTo(pos.x, pos.y);
		}
		
		if(endArrowPathAndTail != null)
		{
			endArrowPath = endArrowPathAndTail.getArrowPath();
            
			int cnt = parameters.length;
			PointF pos = LineArrowPathBuilder.getReferencedPosition(parameters[cnt - 2], parameters[cnt - 1], endArrowPathAndTail.getArrowTailCenter().x, endArrowPathAndTail.getArrowTailCenter().y, autoshape.getEndArrowType());
			parameters[cnt - 2] = (int)pos.x;
			parameters[cnt - 1] = (int)pos.y;
		}
		
		switch(command)
		{
			case Command_MoveTo:
				processCommand_MoveTo(path, parameters);
				break;
				
			case Command_LineTo:
				processCommand_LineTo(path, parameters);
				break;
				
			case Command_CurveTo:
				processCommand_CurveTo(path, parameters);
				break;
				
			case Command_RMoveTo:
				processCommand_rMoveTo(path, parameters);
				break;
				
			case Command_RLineTo:
				processCommand_rLineTo(path, parameters);
				break;
				
			case Command_RCurveTo:
				processCommand_rCurveTo(path, parameters);
				break;
				
			case Command_Close:
				path.close();
				break;
		}
	}
	
	private void processCommand_MoveTo(Path path, Integer[] parameters)
	{
		float x = 0;
		float y = 0;
		if(parameters.length == 2)
		{
			x = parameters[0];
			y = parameters[1];
		}
		else if(parameters.length == 1)
		{
			x = parameters[0];
		}
		
		path.moveTo(x, y);
		
		nextNode.set(x, y);	
	}
	
	private void processCommand_LineTo(Path path, Integer[] parameters)
	{
		int paraIndex = 0;
		while(paraIndex < parameters.length - 1)
		{
			path.lineTo(parameters[paraIndex], parameters[paraIndex + 1]);
			
			preNode.set(nextNode);
			nextNode.set(parameters[paraIndex], parameters[paraIndex + 1]);
			
			paraIndex += 2;
		}
	}
	
	private void processCommand_CurveTo(Path path, Integer[] parameters)
	{
		int paraIndex = 0;
		while(paraIndex < parameters.length - 5)
		{
			path.cubicTo(parameters[paraIndex], parameters[paraIndex + 1], 
					parameters[paraIndex + 2], parameters[paraIndex + 3], 
					parameters[paraIndex + 4], parameters[paraIndex + 5]);
			
			preNode.set(nextNode);
			ctrNode1.set(parameters[paraIndex], parameters[paraIndex + 1]);
			ctrNode2.set(parameters[paraIndex + 2], parameters[paraIndex + 3]);
			nextNode.set(parameters[paraIndex + 4], parameters[paraIndex + 5]);
			
			paraIndex += 6;
		}
	}
	
	/**
	 * relative move to
	 * @param path
	 * @param parameters
	 */
	private void processCommand_rMoveTo(Path path, Integer[] parameters)
	{
		if(parameters.length == 2)
		{
			path.rMoveTo(parameters[0], parameters[1]);
			
			preNode.set(nextNode);
			nextNode.offset(parameters[0], parameters[1]);
					
		}
		else if(parameters.length == 1)
		{
			path.rMoveTo(parameters[0], 0);
			
			preNode.set(nextNode);
			nextNode.offset(parameters[0], 0);
		}
		else 
		{
			path.rMoveTo(0, 0);
			
			preNode.set(nextNode);
			nextNode.offset(0, 0);
		}
	} 
	
	private void processCommand_rLineTo(Path path, Integer[] parameters)
	{
		int paraIndex = 0;
		while(paraIndex < parameters.length - 1)
		{
			path.rLineTo(parameters[paraIndex], parameters[paraIndex + 1]);
			
			preNode.set(nextNode);
			nextNode.offset(parameters[paraIndex], parameters[paraIndex + 1]);
			
			paraIndex += 2;
		}
	}
	
	private void processCommand_rCurveTo(Path path, Integer[] parameters)
	{
		int paraIndex = 0;
		while(paraIndex < parameters.length - 5)
		{
			path.rCubicTo(parameters[paraIndex], parameters[paraIndex + 1], 
					parameters[paraIndex + 2], parameters[paraIndex + 3], 
					parameters[paraIndex + 4], parameters[paraIndex + 5]);
			
			preNode.set(nextNode);
			ctrNode1.offset(parameters[paraIndex], parameters[paraIndex + 1]);
			ctrNode2.offset(parameters[paraIndex + 2], parameters[paraIndex + 3]);
			nextNode.offset(parameters[paraIndex + 4], parameters[paraIndex + 5]);
			
			paraIndex += 6;
		}
	}
	
	//path context index
	private int index;
	
	private StringBuilder builder = new StringBuilder();	
	private List<Integer> paraList = new ArrayList<Integer>();
}
