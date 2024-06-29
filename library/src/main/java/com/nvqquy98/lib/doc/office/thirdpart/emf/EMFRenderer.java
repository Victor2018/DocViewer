
package com.nvqquy98.lib.doc.office.thirdpart.emf;

import java.io.IOException;
import java.util.Stack;
import java.util.Vector;
import java.util.logging.Logger;

import com.nvqquy98.lib.doc.office.java.awt.Color;
import com.nvqquy98.lib.doc.office.java.awt.Dimension;
import com.nvqquy98.lib.doc.office.java.awt.Image;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.java.awt.Shape;
import com.nvqquy98.lib.doc.office.java.awt.Stroke;
import com.nvqquy98.lib.doc.office.java.awt.geom.AffineTransform;
import com.nvqquy98.lib.doc.office.java.awt.geom.Area;
import com.nvqquy98.lib.doc.office.java.awt.geom.GeneralPath;
import com.nvqquy98.lib.doc.office.java.awt.geom.IllegalPathStateException;
import com.nvqquy98.lib.doc.office.java.awt.geom.PathIterator;
import com.nvqquy98.lib.doc.office.simpletext.font.Font;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.BasicStroke;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.GDIObject;
import com.nvqquy98.lib.doc.office.thirdpart.emf.io.Tag;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Typeface;
import android.os.Build;


/**
 * Standalone EMF renderer.
 *
 * @author Daniel Noll (daniel@nuix.com)
 * @version $Id$
 */
public class EMFRenderer
{
    private static final Logger logger = Logger.getLogger("com.nvqquy98.lib.doc.office.thirdpart.emf");

    /**
     * Header read from the EMFInputStream
     */
    private EMFHeader header;

    /**
     * Each logical unit is mapped to one twentieth of a
     * printer's point (1/1440 inch, also called a twip).
     */
    public static double TWIP_SCALE = 1d / 1440 * 254;

    /**
     * affect by all XXXTo methods, e.g. LinTo. ExtMoveTo creates the
     * starting point. CloseFigure closes the figure.
     */
    private GeneralPath figure = null;

    /**
     * AffineTransform which is the base for all rendering
     * operations.
     */
//    private AffineTransform initialTransform;
    private Matrix initialMatrix;

    /**
     * origin of the emf window, set by SetWindowOrgEx
     */
    private Point windowOrigin = null;

    /**
     * origin of the emf viewport, set By SetViewportOrgEx
     */
    private Point viewportOrigin = null;

    /**
     * size of the emf window, set by SetWindowExtEx
     */
    private Dimension windowSize = null;

    /**
     * size of the emf viewport, set by SetViewportExtEx
     */
    private Dimension viewportSize = null;

    /**
     * The MM_ISOTROPIC mode ensures a 1:1 aspect ratio.
     *  The MM_ANISOTROPIC mode allows the x-coordinates
     * and y-coordinates to be adjusted independently.
     */
    private boolean mapModeIsotropic = false;

    /**
     * AffineTransform defined by SetMapMode. Used for
     * resizing the emf to propper device bounds.
     */
    private AffineTransform mapModeTransform = AffineTransform.getScaleInstance(TWIP_SCALE,
        TWIP_SCALE);

    /**
     * clipping area which is the base for all rendering
     * operations.
     */
//    private Shape initialClip;
    private Shape initialClip;

    /**
     * current Graphics2D to paint on. It is set during
     * {@link #paint(java.awt.Graphics2D)}
     */
//    private Graphics2D g2;
    private Canvas mCanvas;

    /**
     * objects used by {@link org.freehep.graphicsio.emf.gdi.SelectObject}.
     * The array is filled by CreateXXX functions, e.g.
     * {@link org.freehep.graphicsio.emf.gdi.CreatePen}
     */
    private GDIObject[] gdiObjects = new GDIObject[256]; // TODO: Make this more flexible.

    // Rendering state.
//    private Paint brushPaint = new Color(0, 0, 0, 0);
//    private Paint penPaint = Color.BLACK;
    private Stroke penStroke = new BasicStroke();
    private Paint brushPaint = new Paint();
    private Paint penPaint = new Paint();

    private int textAlignMode = 0;

    /**
     * color for simple text rendering
     */
    private Color textColor = Color.BLACK;

    /**
     * written by {@link org.freehep.graphicsio.emf.gdi.SetPolyFillMode} used by
     * e.g. {@link org.freehep.graphicsio.emf.gdi.PolyPolygon16}
     */
    private int windingRule = GeneralPath.WIND_EVEN_ODD;

    /**
     * Defined by SetBkModes, either {@link EMFConstants#BKG_OPAQUE} or
     * {@link EMFConstants#BKG_TRANSPARENT}. Used in
     * {@link #fillAndDrawOrAppend(java.awt.Graphics2D, java.awt.Shape)}
     */
    private int bkMode = EMFConstants.BKG_OPAQUE;

    /**
     * The SetBkMode function affects the line styles for lines drawn using a
     * pen created by the CreatePen function. SetBkMode does not affect lines
     * drawn using a pen created by the ExtCreatePen function.
     */
    private boolean useCreatePen = true;

    /**
     * The miter length is defined as the distance from the intersection
     * of the line walls on the inside of the join to the intersection of
     * the line walls on the outside of the join. The miter limit is the
     * maximum allowed ratio of the miter length to the line width.
     */
    private int meterLimit = 10;

    /**
     * The SetROP2 function sets the current foreground mix mode.
     * Default is to use the pen.
     */
    private int rop2 = EMFConstants.R2_COPYPEN;

    /**
     * e.g. {@link Image#SCALE_SMOOTH} for rendering images
     */
    private int scaleMode = Image.SCALE_SMOOTH;

    /**
     * The brush origin is a pair of coordinates specifying the location of one
     * pixel in the bitmap. The default brush origin coordinates are (0,0). For
     * horizontal coordinates, the value 0 corresponds to the leftmost column
     * of pixels; the width corresponds to the rightmost column. For vertical
     * coordinates, the value 0 corresponds to the uppermost row of pixels;
     * the height corresponds to the lowermost row.
     */
    private Point brushOrigin = new Point(0, 0);

    /**
     * stores the parsed tags. Filled by the constructor. Read by
     * {@link #paint(java.awt.Graphics2D)}
     */
    private Vector tags = new Vector(0);

    /**
     * Created by BeginPath and closed by EndPath.
     */
    private GeneralPath path = null;

    /**
     * The transformations set by ModifyWorldTransform are redirected to
     * that AffineTransform. They do not affect the current paint context,
     * after BeginPath is called. Only the figures appended to path
     * are transformed by this AffineTransform.
     * BeginPath clears the transformation, ModifyWorldTransform changes ist.
     */
    private AffineTransform pathTransform = new AffineTransform();

    /**
     * {@link org.freehep.graphicsio.emf.gdi.SaveDC} stores
     * an Instance of DC if saveDC is read. RestoreDC pops an object.
     */
    private Stack dcStack = new Stack();

    /**
     * default direction is counterclockwise
     */
    private int arcDirection = EMFConstants.AD_COUNTERCLOCKWISE;
    
    private Area mCurrClip;
    
    private int escapement = 0;

    /**
     * Class the encapsulate the state of a Graphics2D object.
     * Instances are store in dcStack by
     * {@link org.freehep.graphicsio.emf.EMFRenderer#paint(java.awt.Graphics2D)}
     */
    private class DC
    {
        private Paint paint;
        private Stroke stroke;
        private AffineTransform transform;
        private Shape clip;
        //private Rect clip;
        // Added by wangtang
        private Matrix matrix;
        public GeneralPath path;
        public int bkMode;
        public int windingRule;
        public int meterLimit;
        public boolean useCreatePen;
        public int scaleMode;
        public AffineTransform pathTransform;
    }

    /**
     * Constructs the renderer.
     *
     * @param is the input stream to read the EMF records from.
     * @throws IOException if an error occurs reading the header.
     */
    public EMFRenderer(EMFInputStream is) throws IOException
    {
    	// Initialize Paint
        brushPaint.setColor(new Color(0, 0, 0, 0).getRGB());
        penPaint.setColor(Color.BLACK.getRGB());
        
        this.header = is.readHeader();

        // read all tags
        Tag tag;
        while ((tag = is.readTag()) != null)
        {
            tags.add(tag);
        }
        is.close();
    }

    /**
     * Gets the size of a canvas which would be required to render the EMF.
     *
     * @return the size.
     */
    public Dimension getSize()
    {
        return header.getBounds().getSize();
        // TODO see the mapModePart of resetTransformation()
        // if uncommented size is too small
        /* Dimension bounds = header.getBounds().getSize();
            return new Dimension(
            (int)Math.ceil(bounds.width * TWIP_SCALE),
            (int)Math.ceil(bounds.height * TWIP_SCALE));*/
    }

    /**
     * Paints the EMF onto the provided graphics context.
     *
     * @param g2 the graphics context to paint onto.
     */
//    public void paint(Graphics2D g2)
    public void paint(Canvas canvas)
    {
        //this.g2 = g2;
    	this.mCanvas = canvas;

        // store at leat clip and transformation
        //Shape clip = g2.getClip();
        Rect rect = canvas.getClipBounds();
        
        //AffineTransform at = g2.getTransform();
        Matrix matrix = canvas.getMatrix();
//        mM = mC.getMatrix();
//        Rect r = mC.getClipBounds();
        int cl[] = {-1, rect.top, rect.left, -2, rect.top, rect.right, -2, rect.bottom, rect.right, -2, rect.bottom, rect.left};
        mCurrClip = new Area(createShape(cl));

        
//        Map hints = g2.getRenderingHints();

        // some quality settings
//        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
//            RenderingHints.VALUE_FRACTIONALMETRICS_ON);
//        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
//            RenderingHints.VALUE_INTERPOLATION_BICUBIC);
//        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
//            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        penPaint.setAntiAlias(true);
        penPaint.setFilterBitmap(true);
        penPaint.setDither(true);

        // used by SetWorldTransform to reset transformation
//        initialTransform = g2.getTransform();
        initialMatrix = canvas.getMatrix();

        // set the initial value, defaults for EMF
        path = null;
        figure = null;
        meterLimit = 10;
        windingRule = GeneralPath.WIND_EVEN_ODD;
        bkMode = EMFConstants.BKG_OPAQUE;
        useCreatePen = true;
        scaleMode = Image.SCALE_SMOOTH;

        windowOrigin = null;
        viewportOrigin = null;
        windowSize = null;
        viewportSize = null;

        mapModeIsotropic = false;
        mapModeTransform = AffineTransform.getScaleInstance(TWIP_SCALE, TWIP_SCALE);

        // apply all default settings
        //resetTransformation(g2);
        resetMatrix(canvas);

        // determin initial clip after all basic transformations
//        initialClip = g2.getClip();
        initialClip = mCurrClip;

        // iterate and render all tags
        Tag tag;
        for (int i = 0; i < tags.size(); i++)
        {
            tag = (Tag)tags.get(i);
            if (tag instanceof EMFTag)
            {
                ((EMFTag)tags.get(i)).render(this);
            }
            else
            {
                logger.warning("unknown tag: " + tag);
            }
        }

        // reset Transform and clip
//        g2.setRenderingHints(hints);
//        g2.setTransform(at);
//        g2.setClip(clip);
        penPaint.setAntiAlias(true);
        penPaint.setFilterBitmap(true);
        penPaint.setDither(true);
        
        canvas.setMatrix(matrix);
//        canvas.clipRect(rect);
        setClip(initialClip);
    }

    // ---------------------------------------------------------------------
    //            complex drawing methods for EMFTags
    // ---------------------------------------------------------------------

    /**
     * set the initial transform, the windowOrigin and viewportOrigin,
     * scales by viewportSize and windowSize
     * @param g2 Context to apply transformations
     */
//    private void resetTransformation(Graphics2D g2)
//    {
//        // rest to device configuration
//        if (initialTransform != null)
//        {
//            g2.setTransform(initialTransform);
//        }
//        else
//        {
//            g2.setTransform(new AffineTransform());
//        }
//
//        /* TODO mapModeTransform dows not work correctly
//        if (mapModeTransform != null) {
//            g2.transform(mapModeTransform);
//        }*/
//
//        // move to window origin
//        if (windowOrigin != null)
//        {
//            g2.translate(-windowOrigin.getX(), -windowOrigin.getY());
//        }
//        // move to window origin
//        if (viewportOrigin != null)
//        {
//            g2.translate(-viewportOrigin.getX(), -viewportOrigin.getY());
//        }
//
//        // TWIP_SCALE by window and viewport size
//        if (viewportSize != null && windowSize != null)
//        {
//            double scaleX = viewportSize.getWidth() / windowSize.getWidth();
//            double scaleY = viewportSize.getHeight() / windowSize.getHeight();
//            g2.scale(scaleX, scaleY);
//        }
//    }
    private void resetMatrix(Canvas canvas)
    {
        // rest to device configuration
        if (initialMatrix != null)
        {
//            g2.setTransform(initialTransform);
        	canvas.setMatrix(initialMatrix);
        }
        else
        {
        	canvas.setMatrix(new Matrix());
        }

        /* TODO mapModeTransform dows not work correctly
        if (mapModeTransform != null) {
            g2.transform(mapModeTransform);
        }*/

        // move to window origin
//        if (windowOrigin != null)
//        {
//        	canvas.translate(-windowOrigin.x, -windowOrigin.y);
//        }
        // move to window origin
//        if (viewportOrigin != null)
//        {
//        	canvas.translate(-viewportOrigin.x, -viewportOrigin.y);
//        }

        // TWIP_SCALE by window and viewport size
        if (viewportSize != null && windowSize != null)
        {
            float scaleX = (float) (viewportSize.getWidth() / windowSize.getWidth());
            float scaleY = (float) (viewportSize.getHeight() / windowSize.getHeight());
            canvas.scale(scaleX, scaleY);
        }
    }

    /**
     * Stores the current state. Used by
     * {@link org.freehep.graphicsio.emf.gdi.SaveDC}
     */
    public void saveDC()
    {
        // create a DC instance with current settings
        DC dc = new DC();
//        dc.paint = g2.getPaint();
        dc.paint = penPaint;
                
        //dc.stroke = g2.getStroke();
//        dc.transform = g2.getTransform();
//        dc.pathTransform = pathTransform;
        dc.matrix = mCanvas.getMatrix();
//        dc.clip = g2.getClip();
        dc.clip = mCurrClip;
        dc.path = path;
        dc.meterLimit = meterLimit;
        dc.windingRule = windingRule;
        dc.bkMode = bkMode;
        dc.useCreatePen = useCreatePen;
        dc.scaleMode = scaleMode;
        // push it on top of the stack
        dcStack.push(dc);
        mCanvas.save();
    }

    /**
     * Retores a saved state. Used by
     * {@link org.freehep.graphicsio.emf.gdi.RestoreDC}
     */
    public void retoreDC()
    {
        // is somethoing stored?
        if (!dcStack.empty())
        {
            // read it
            DC dc = (DC)dcStack.pop();

            // use it
            meterLimit = dc.meterLimit;
            windingRule = dc.windingRule;
            path = dc.path;
            bkMode = dc.bkMode;
            useCreatePen = dc.useCreatePen;
            scaleMode = dc.scaleMode;
            pathTransform = dc.pathTransform;
//            g2.setPaint(dc.paint);
//            g2.setStroke(dc.stroke);
//            g2.setTransform(dc.transform);
//            g2.setClip(dc.clip);
            setStroke(penStroke);
            
            mCanvas.setMatrix(dc.matrix);
            setClip(dc.clip);
        }
        else
        {
            // set the default values
        }
        mCanvas.restore();
    }

    /**
     * closes and appends the current open figure to the
     * path
     */
    public void closeFigure()
    {
        if (figure == null)
        {
            return;
        }

        try
        {
            figure.closePath();
            appendToPath(figure);
            figure = null;
        }
        catch(IllegalPathStateException e)
        {
            logger.warning("no figure to close");
        }
    }
    
    /**
     * appends the current open figure to the
     * path
     */
    public void appendFigure()
    {
        if (figure == null)
        {
            return;
        }

        try
        {
            appendToPath(figure);
            figure = null;
        }
        catch(IllegalPathStateException e)
        {
            logger.warning("no figure to append");
        }
    }

    /**
     * Logical units are mapped to arbitrary units with equally scaled axes;
     * that is, one unit along the x-axis is equal to one unit along the y-axis.
     * Use the SetWindowExtEx and SetViewportExtEx functions to specify the
     * units and the orientation of the axes. Graphics device interface (GDI)
     * makes adjustments as necessary to ensure the x and y units remain the
     * same size (When the window extent is set, the viewport will be adjusted
     * to keep the units isotropic).
     */
    public void fixViewportSize()
    {
        if (mapModeIsotropic && (windowSize != null && viewportSize != null))
        {
            viewportSize.setSize(viewportSize.getWidth(),
                viewportSize.getWidth() * (windowSize.getHeight() / windowSize.getWidth()));
        }
    }

    /**
     * fills a shape using the brushPaint,  penPaint and penStroke
     * @param g2 Painting context
     * @param s Shape to fill with current brush
     */
//    private void fillAndDrawOrAppend(Graphics2D g2, Shape s)
    private void fillAndDrawOrAppend(Canvas canvas, Shape s)
    {
        // don't draw, just append the shape if BeginPath
        // has opened the path
        if (!appendToPath(s))
        {
            // The SetBkMode function affects the line styles for lines drawn using a
            // pen created by the CreatePen function. SetBkMode does not affect lines
            // drawn using a pen created by the ExtCreatePen function.
            if (useCreatePen)
            {
                // OPAQUE 	Background is filled with the current background
                // color before the text, hatched brush, or pen is drawn.
                if (bkMode == EMFConstants.BKG_OPAQUE)
                {
//                    fillShape(g2, s);
                	fillShape(s);
                }
                else
                {
                    // TRANSPARENT 	Background remains untouched.
                    // TODO: if we really do nothing some drawings are incomplete
                    // this needs definitly a fix
//                    fillShape(g2, s);
                	fillShape(s);
                }
            }
            else
            {
                // always fill the background if ExtCreatePen is set
//                fillShape(g2, s);
            	fillShape(s);
            }
//            drawShape(g2, s);
            drawShape(canvas, s);
        }
    }

    /**
     * draws a shape using the penPaint and penStroke
     * @param g2 Painting context
     * @param s Shape to draw with current paen
     */
//    private void drawOrAppend(Graphics2D g2, Shape s)
//    {
//        // don't draw, just append the shape if BeginPath
//        // opens a GeneralPath
//        if (!appendToPath(s))
//        {
//            drawShape(g2, s);
//        }
//    }
    private void drawOrAppend(Canvas canvas, Shape s)
    {
        // don't draw, just append the shape if BeginPath
        // opens a GeneralPath
        if (!appendToPath(s))
        {
            drawShape(canvas, s);
        }
    }

    /**
     * draws the text
     *
     * @param text Text
     * @param x x-Position
     * @param y y-Position
     */
//    public void drawOrAppendText(String text, double x, double y)
    public void drawOrAppendText(String text, float x, float y)
    {
        // TODO: Use explicit widths to pixel-position each character, if present.
        // TODO: Implement alignment properly.  What we have already seems to work well enough.
        //            FontRenderContext frc = g2.getFontRenderContext();
        //            TextLayout layout = new TextLayout(str, g2.getFont(), frc);
        //            if ((textAlignMode & EMFConstants.TA_CENTER) != 0) {
        //                layout.draw(g2, x + (width - textWidth) / 2, y);
        //            } else if ((textAlignMode & EMFConstants.TA_RIGHT) != 0) {
        //                layout.draw(g2, x + width - textWidth, y);
        //            } else {
        //                layout.draw(g2, x, y);
        //            }

//        if (path != null)
//        {
            // do not use g2.drawString(str, x, y) to be aware of path
//            TextLayout tl = new TextLayout(text, g2.getFont(), g2.getFontRenderContext());
//            path.append(tl.getOutline(null), false);
//        }
//        else
//        {
//            g2.setPaint(textColor);
//            g2.drawString(text, (int)x, (int)y);
        	Style tmp = penPaint.getStyle();
        	penPaint.setColor(textColor.getRGB());
            penPaint.setStrokeWidth(0);
        	// Vertical text
            if (2700 == escapement)
            {
            	for (int i = 0; i < text.length(); i++)
            	{
            		mCanvas.drawText(String.valueOf(text.charAt(i)), x, y + i * penPaint.getTextSize(), penPaint);
            	}
            }
            else
            {
            	if (EMFConstants.TA_TOP == textAlignMode)
            	{
            		y += penPaint.getTextSize() - 3;
            	}
            	mCanvas.drawText(text, x, y, penPaint);
            }
            penPaint.setStyle(tmp);
//        }
    }

    /**
     * Append the shape to the current path
     *
     * @param s Shape to fill with current brush
     * @return true, if path was changed
     */
    private boolean appendToPath(Shape s)
    {
        // don't draw, just append the shape if BeginPath
        // opens a GeneralPath
        if (path != null)
        {
            // aplly transformation if set
            if (pathTransform != null)
            {
                s = pathTransform.createTransformedShape(s);
            }
            // append the shape
            path.append(s, false);
            // current path set
            return true;
        }
        // current path not set
        return false;
    }

    /**
     * closes the path opened by {@link org.freehep.graphicsio.emf.gdi.BeginPath}
     */
    public void closePath()
    {
        if (path != null)
        {
            try
            {
                path.closePath();
            }
            catch(IllegalPathStateException e)
            {
                logger.warning("no figure to close");
            }
        }
    }
    
    private void getCurrentSegment(PathIterator pi, Path path) {
        float[] coordinates = new float[6];
        int type = pi.currentSegment(coordinates);
        switch (type) {
        case PathIterator.SEG_MOVETO:
            path.moveTo(coordinates[0], coordinates[1]);
            break;
        case PathIterator.SEG_LINETO:
            path.lineTo(coordinates[0], coordinates[1]);
            break;
        case PathIterator.SEG_QUADTO:
            path.quadTo(coordinates[0], coordinates[1], coordinates[2],
                    coordinates[3]);
            break;
        case PathIterator.SEG_CUBICTO:
            path.cubicTo(coordinates[0], coordinates[1], coordinates[2],
                    coordinates[3], coordinates[4], coordinates[5]);
            break;
        case PathIterator.SEG_CLOSE:
            path.close();
            break;
        default:
            break;
        }
    }
    
    private Path getPath(Shape s) {
        Path path = new Path();
        PathIterator pi = s.getPathIterator(null);
        while (pi.isDone() == false) {
            getCurrentSegment(pi, path);
            pi.next();
        }
        return path;
    }

    /**
     * fills a shape using the brushPaint,  penPaint and penStroke.
     * This method should only be called for path painting. It doesn't check for a
     * current path.
     *
     * @param g2 Painting context
     * @param s Shape to fill with current brush
     */
//    private void fillShape(Graphics2D g2, Shape s)
//    {
////        g2.setPaint(brushPaint);
////        g2.fill(s);
//        Paint.Style tmp = brushPaint.getStyle();
//        brushPaint.setStyle(Paint.Style.FILL);
//        mCanvas.drawPath(getPath(s), brushPaint);
//        brushPaint.setStyle(tmp);
//    }
    
    private void setStroke(Stroke stroke) {
//		penPaint.setStyle(Style.STROKE);
//		penPaint.setStrokeCap(Cap.SQUARE);
//		penPaint.setStrokeMiter(10.0f);
        BasicStroke bs = (BasicStroke) stroke;
        penPaint.setStyle(Paint.Style.STROKE);
        penPaint.setStrokeWidth(bs.getLineWidth());

        int cap = bs.getEndCap();
        if (cap == 0) {
        	penPaint.setStrokeCap(Paint.Cap.BUTT);
        } else if (cap == 1) {
        	penPaint.setStrokeCap(Paint.Cap.ROUND);
        } else if (cap == 2) {
        	penPaint.setStrokeCap(Paint.Cap.SQUARE);
        }

        int join = bs.getLineJoin();
        if (join == 0) {
        	penPaint.setStrokeJoin(Paint.Join.MITER);
        } else if (join == 1) {
        	penPaint.setStrokeJoin(Paint.Join.ROUND);
        } else if (join == 2) {
        	penPaint.setStrokeJoin(Paint.Join.BEVEL);
        }
        penPaint.setStrokeMiter(bs.getMiterLimit());
    }

    /**
     * draws a shape using the penPaint and penStroke
     * This method should only be called for path drawing. It doesn't check for a
     * current path.
     *
     * @param g2 Painting context
     * @param s Shape to draw with current pen
     */
//    private void drawShape(Graphics2D g2, Shape s)
    private void drawShape(Canvas canvas, Shape s)
    {
//        g2.setStroke(penStroke);
    	setStroke(penStroke);

        // R2_BLACK 	Pixel is always 0.
        if (rop2 == EMFConstants.R2_BLACK)
        {
            //g2.setComposite(AlphaComposite.SrcOver);
        	penPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_OVER));
//            g2.setPaint(Color.black);
        	penPaint.setColor(Color.black.getRGB());
        }
        // R2_COPYPEN 	Pixel is the pen color.
        else if (rop2 == EMFConstants.R2_COPYPEN)
        {
//            g2.setComposite(AlphaComposite.SrcOver);
        	penPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_OVER));
            //g2.setPaint(penPaint);
        }
        // R2_NOP 	Pixel remains unchanged.
        else if (rop2 == EMFConstants.R2_NOP)
        {
//            g2.setComposite(AlphaComposite.SrcOver);
        	penPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_OVER));
//            g2.setPaint(penPaint);
        }
        // R2_WHITE 	Pixel is always 1.
        else if (rop2 == EMFConstants.R2_WHITE)
        {
//            g2.setComposite(AlphaComposite.SrcOver);
        	penPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_OVER));
//            g2.setPaint(Color.white);
        	penPaint.setColor(Color.white.getRGB());
        }
        // R2_NOTCOPYPEN 	Pixel is the inverse of the pen color.
        else if (rop2 == EMFConstants.R2_NOTCOPYPEN)
        {
//            g2.setComposite(AlphaComposite.SrcOver);
        	penPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_OVER));
            // TODO: set at least inverted color if paint is a color
        }
        // R2_XORPEN 	Pixel is a combination of the colors
        // in the pen and in the screen, but not in both.
        else if (rop2 == EMFConstants.R2_XORPEN)
        {
//            g2.setComposite(AlphaComposite.Xor);
        	penPaint.setXfermode(new PorterDuffXfermode(Mode.XOR));
        }
        else
        {
            logger.warning("got unsupported ROP" + rop2);
            // TODO:
            //R2_MASKNOTPEN 	Pixel is a combination of the colors common to both the screen and the inverse of the pen.
            //R2_MASKPEN 	Pixel is a combination of the colors common to both the pen and the screen.
            //R2_MASKPENNOT 	Pixel is a combination of the colors common to both the pen and the inverse of the screen.
            //R2_MERGENOTPEN 	Pixel is a combination of the screen color and the inverse of the pen color.
            //R2_MERGEPEN 	Pixel is a combination of the pen color and the screen color.
            //R2_MERGEPENNOT 	Pixel is a combination of the pen color and the inverse of the screen color.
            //R2_NOT 	Pixel is the inverse of the screen color.
            //R2_NOTCOPYPEN 	Pixel is the inverse of the pen color.
            //R2_NOTMASKPEN 	Pixel is the inverse of the R2_MASKPEN color.
            //R2_NOTMERGEPEN 	Pixel is the inverse of the R2_MERGEPEN color.
            //R2_NOTXORPEN 	Pixel is the inverse of the R2_XORPEN color.
        }

        //g2.draw(s);
        canvas.drawPath(getPath(s), penPaint);
    }

    // ---------------------------------------------------------------------
    //            simple wrapping methods to the painting context
    // ---------------------------------------------------------------------

//    public void setFont(Font font)
//    {
//        g2.setFont(font);
//    }
    public void setFont(Font font)
    {
        //g2.setFont(font);
        if (font == null) {
            return;
        }

//        mFnt = font;
        Typeface tf = null;
        String nam = font.getName();
        int sty = font.getStyle();
        String aF = "";
        if (nam != null) {
            if (nam.equalsIgnoreCase("Serif")
                    || nam.equalsIgnoreCase("TimesRoman")) {
                aF = "serif";
            } else if (nam.equalsIgnoreCase("SansSerif")
                    || nam.equalsIgnoreCase("Helvetica")) {
                aF = "sans-serif";
            } else if (nam.equalsIgnoreCase("Monospaced")
                    || nam.equalsIgnoreCase("Courier")) {
                aF = "monospace";
            } else {
            	aF = "sans-serif";
            }
        }

        switch (sty) {
        case Font.PLAIN:
            tf = Typeface.create(aF, Typeface.NORMAL);
            break;
        case Font.BOLD:
            tf = Typeface.create(aF, Typeface.BOLD);
            break;
        case Font.ITALIC:
            tf = Typeface.create(aF, Typeface.ITALIC);
            break;
        case Font.BOLD | Font.ITALIC:
            tf = Typeface.create(aF, Typeface.BOLD_ITALIC);
            break;
        default:
            tf = Typeface.DEFAULT;
        }

        penPaint.setTextSize((float) font.getFontSize());
        penPaint.setTypeface(tf);
    }
    
    public void setEscapement(int escapement)
    {
        this.escapement = escapement;
    }

//    public AffineTransform getTransform()
//    {
//        return g2.getTransform();
//    }
    public Matrix getMatrix()
    {
        return mCanvas.getMatrix();
    }

    public void transform(AffineTransform transform)
    {
//        g2.transform(transform);
//    	Matrix matrix = new Matrix();
//    	matrix.setValues(createMatrix(transform));
//    	mCanvas.setMatrix(matrix);
    	Matrix matrix = new Matrix();
    	matrix.setValues(createMatrix(transform));
    	mCanvas.concat(matrix);
    }

    public void resetTransformation()
    {
//        resetTransformation(g2);
    	resetMatrix(mCanvas);
    }

//    public void setTransform(AffineTransform at)
//    {
//        g2.setTransform(at);
//    }
    public void setMatrix(Matrix matrix)
    {
    	mCanvas.setMatrix(matrix);
    }

    public void setClip(Shape shape)
    {
//        g2.setClip(shape);
        mCurrClip = new Area(shape);
        //mCanvas.clipPath(getPath(shape), Region.Op.REPLACE);
    }

    public void clip(Shape shape) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Rectangle bounds = shape.getBounds();
            Path mPathXOR = new Path();
            mPathXOR.moveTo(0, 0);
            mPathXOR.lineTo(bounds.width, 0);
            mPathXOR.lineTo(bounds.width, bounds.height);
            mPathXOR.lineTo(0, bounds.height);
            mPathXOR.close();
            // 以上根据实际的Canvas或View的大小，画出相同大小的Path即可
            mPathXOR.op(getPath(shape), Path.Op.XOR);
            mCanvas.clipPath(mPathXOR);
        } else {
            mCanvas.clipPath(getPath(shape), Region.Op.REPLACE);
        }
        // g2.clip(shape);
    }

    public Shape getClip()
    {
//        return g2.getClip();
    	return mCurrClip;
    }

//    public void drawImage(BufferedImage image, AffineTransform transform)
//    {
//        g2.drawImage(image, transform, null);
//    }
    private Shape createShape(int[] arr) {
        Shape s = new GeneralPath();
        for(int i = 0; i < arr.length; i++) {
            int type = arr[i];    
            switch (type) {
            case -1:
                //MOVETO
                ((GeneralPath)s).moveTo(arr[++i], arr[++i]);
                break;
            case -2:
                //LINETO
                ((GeneralPath)s).lineTo(arr[++i], arr[++i]);
                break;
            case -3:
                //QUADTO
                ((GeneralPath)s).quadTo(arr[++i], arr[++i], arr[++i],
                        arr[++i]);
                break;
            case -4:
                //CUBICTO
                ((GeneralPath)s).curveTo(arr[++i], arr[++i], arr[++i],
                        arr[++i], arr[++i], arr[++i]);
                break;
            case -5:
                //CLOSE
                return s;
            default:
                break;
            }
        }
        return s;
    }
    
    public static float[] createMatrix(AffineTransform Tx) {
        double[] at = new double[9];
        Tx.getMatrix(at);
        float[] f = new float[at.length];
        f[0] = (float) at[0];
        f[1] = (float) at[2];
        f[2] = (float) at[4];
        f[3] = (float) at[1];
        f[4] = (float) at[3];
        f[5] = (float) at[5];
        f[6] = 0;
        f[7] = 0;
        f[8] = 1;
        return f;
    }
    
    public void drawImage(Bitmap bitmap, AffineTransform transform)
    {
//        g2.drawImage(image, transform, null);
    	Matrix matrix = new Matrix();
    	matrix.setValues(createMatrix(transform));
    	mCanvas.drawBitmap(bitmap, matrix, penPaint);
    }

//    public void drawImage(BufferedImage image, int x, int y, int width, int height)
//    {
//        g2.drawImage(image, x, y, width, height, null);
//    }
    public void drawImage(Bitmap bitmap, int x, int y, int width, int height)
    {
//        mCanvas.drawBitmap(bitmap, x, y, penPaint);
//    	mCanvas.drawBitmap(bitmap, x, y, null);
    	Rect dst = new Rect(x, y, x + width, y + height);
    	mCanvas.drawBitmap(bitmap, null, dst, null);
    }

    public void drawShape(Shape shape)
    {
//        drawShape(g2, shape);
    	drawShape(mCanvas, shape);
    }

    public void fillShape(Shape shape)
    {
//        fillShape(g2, shape);
        Paint.Style tmp = brushPaint.getStyle();
        brushPaint.setStyle(Paint.Style.FILL);
        mCanvas.drawPath(getPath(shape), brushPaint);
        brushPaint.setStyle(tmp);
    }
    
    public void fillAndDrawShape(Shape shape)
    {
        Paint.Style tmp = brushPaint.getStyle();
        brushPaint.setStyle(Paint.Style.FILL);
        drawShape(mCanvas, shape);
        brushPaint.setStyle(tmp);
    }

    public void fillAndDrawOrAppend(Shape s)
    {
//        fillAndDrawOrAppend(g2, s);
    	fillAndDrawOrAppend(mCanvas, s);
    }

    public void drawOrAppend(Shape s)
    {
//        drawOrAppend(g2, s);
    	drawOrAppend(mCanvas, s);
    }

    // ---------------------------------------------------------------------
    //            simple getter / setter methods
    // ---------------------------------------------------------------------

    public int getWindingRule()
    {
        return windingRule;
    }

    public GeneralPath getFigure()
    {
        return figure;
    }

    public void setFigure(GeneralPath figure)
    {
        this.figure = figure;
    }

    public GeneralPath getPath()
    {
        return path;
    }

    public void setPath(GeneralPath path)
    {
        this.path = path;
    }

    public Shape getInitialClip()
    {
        return initialClip;
    }

    public AffineTransform getPathTransform()
    {
        return pathTransform;
    }

    public void setPathTransform(AffineTransform pathTransform)
    {
        this.pathTransform = pathTransform;
    }

    public void setWindingRule(int windingRule)
    {
        this.windingRule = windingRule;
    }

    public void setMapModeIsotropic(boolean mapModeIsotropic)
    {
        this.mapModeIsotropic = mapModeIsotropic;
    }

    public AffineTransform getMapModeTransform()
    {
        return mapModeTransform;
    }

    public void setMapModeTransform(AffineTransform mapModeTransform)
    {
        this.mapModeTransform = mapModeTransform;
    }

    public void setWindowOrigin(Point windowOrigin)
    {
        this.windowOrigin = windowOrigin;
        if (windowOrigin != null)
        {
        	mCanvas.translate(-windowOrigin.x, -windowOrigin.y);
        }
    }

    public void setViewportOrigin(Point viewportOrigin)
    {
        this.viewportOrigin = viewportOrigin;
        if (viewportOrigin != null)
        {
        	mCanvas.translate(-viewportOrigin.x, -viewportOrigin.y);
        }
    }

    public void setViewportSize(Dimension viewportSize)
    {
        this.viewportSize = viewportSize;
        fixViewportSize();
        resetTransformation();
    }

    public void setWindowSize(Dimension windowSize)
    {
        this.windowSize = windowSize;
        fixViewportSize();
        resetTransformation();
    }

    public GDIObject getGDIObject(int index)
    {
        return gdiObjects[index];
    }

    public void storeGDIObject(int index, GDIObject tag)
    {
        gdiObjects[index] = tag;
    }

    public void setUseCreatePen(boolean useCreatePen)
    {
        this.useCreatePen = useCreatePen;
    }

//    public void setPenPaint(Paint penPaint)
//    {
//        this.penPaint = penPaint;
//    }
    public void setPenPaint(Color color)
    {
        penPaint.setColor(color.getRGB());
    }

    public Stroke getPenStroke()
    {
        return penStroke;
    }

    public void setPenStroke(Stroke penStroke)
    {
        this.penStroke = penStroke;
    }

//    public void setBrushPaint(Paint brushPaint)
//    {
//        this.brushPaint = brushPaint;
//    }
    public void setBrushPaint(Color color)
    {
        brushPaint.setColor(color.getRGB());
    }
    
    public void setBrushPaint(Bitmap bitmap)
    {
//    	int[] pixels = new int[16 * 16];
//    	bitmap.getPixels(pixels, 0, 16, 0, 0, 16, 16);
//    	brushPaint.setColor(pixels);
//    	brushPaint.setColor(bitmap.getPixel(0, 0));
    	mCanvas.clipRect(0, 0, 16, 16);
    	mCanvas.setBitmap(bitmap);
    }

    public float getMeterLimit()
    {
        return meterLimit;
    }

    public void setMeterLimit(int meterLimit)
    {
        this.meterLimit = meterLimit;
    }

    public void setTextColor(Color textColor)
    {
        this.textColor = textColor;
    }
    
    public void setTextBkColor()
    {
    	setBrushPaint(textColor);
    }

    public void setRop2(int rop2)
    {
        this.rop2 = rop2;
    }

    public void setBkMode(int bkMode)
    {
        this.bkMode = bkMode;
    }

    public int getTextAlignMode()
    {
        return textAlignMode;
    }

    public void setTextAlignMode(int textAlignMode)
    {
        this.textAlignMode = textAlignMode;
    }

    public void setScaleMode(int scaleMode)
    {
        this.scaleMode = scaleMode;
    }

    public Point getBrushOrigin()
    {
        return brushOrigin;
    }

    public void setBrushOrigin(Point brushOrigin)
    {
        this.brushOrigin = brushOrigin;
    }

    public void setArcDirection(int arcDirection)
    {
        this.arcDirection = arcDirection;
    }

    public int getArcDirection()
    {
        return arcDirection;
    }
}
