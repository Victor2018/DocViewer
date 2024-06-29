// Copyright 2001, FreeHEP.
package com.nvqquy98.lib.doc.office.thirdpart.emf;

import com.nvqquy98.lib.doc.office.thirdpart.emf.data.AbortPath;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.AlphaBlend;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.AngleArc;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.Arc;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.ArcTo;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.BeginPath;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.BitBlt;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.Chord;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.CloseFigure;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.CreateBrushIndirect;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.CreateDIBPatternBrushPt;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.CreatePen;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.DeleteObject;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.EMFPolygon;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.EMFRectangle;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.EOF;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.Ellipse;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.EndPath;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.ExcludeClipRect;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.ExtCreateFontIndirectW;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.ExtCreatePen;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.ExtFloodFill;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.ExtSelectClipRgn;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.ExtTextOutA;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.ExtTextOutW;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.FillPath;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.FlattenPath;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.GDIComment;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.GradientFill;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.IntersectClipRect;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.LineTo;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.ModifyWorldTransform;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.MoveToEx;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.OffsetClipRgn;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.Pie;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.PolyBezier;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.PolyBezier16;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.PolyBezierTo;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.PolyBezierTo16;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.PolyDraw;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.PolyDraw16;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.PolyPolygon;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.PolyPolygon16;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.PolyPolyline;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.PolyPolyline16;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.Polygon16;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.Polyline;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.Polyline16;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.PolylineTo;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.PolylineTo16;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.RealizePalette;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.ResizePalette;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.RestoreDC;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.RoundRect;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.SaveDC;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.ScaleViewportExtEx;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.ScaleWindowExtEx;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.SelectClipPath;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.SelectObject;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.SelectPalette;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.SetArcDirection;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.SetBkColor;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.SetBkMode;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.SetBrushOrgEx;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.SetICMMode;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.SetMapMode;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.SetMapperFlags;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.SetMetaRgn;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.SetMiterLimit;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.SetPixelV;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.SetPolyFillMode;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.SetROP2;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.SetStretchBltMode;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.SetTextAlign;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.SetTextColor;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.SetViewportExtEx;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.SetViewportOrgEx;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.SetWindowExtEx;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.SetWindowOrgEx;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.SetWorldTransform;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.StretchDIBits;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.StrokeAndFillPath;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.StrokePath;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.WidenPath;
import com.nvqquy98.lib.doc.office.thirdpart.emf.io.TagSet;

/**
 * EMF specific tagset.
 * 
 * @author Mark Donszelmann
 * @version $Id: EMFTagSet.java 10515 2007-02-06 18:42:34Z duns $
 */
public class EMFTagSet extends TagSet {

    public EMFTagSet(int version) {
        if (version >= 1) {
            // Set for Windows 3
            addTag(new PolyBezier()); // 2 02
            addTag(new EMFPolygon()); // 3 03
            addTag(new Polyline()); // 4 04
            addTag(new PolyBezierTo()); // 5 05
            addTag(new PolylineTo()); // 6 06
            addTag(new PolyPolyline()); // 7 07
            addTag(new PolyPolygon()); // 8 08
            addTag(new SetWindowExtEx()); // 9 09
            addTag(new SetWindowOrgEx()); // 10 0a
            addTag(new SetViewportExtEx()); // 11 0b
            addTag(new SetViewportOrgEx()); // 12 0c
            addTag(new SetBrushOrgEx()); // 13 0d
            addTag(new EOF()); // 14 0e
            addTag(new SetPixelV()); // 15 0f
            addTag(new SetMapperFlags()); // 16 10
            addTag(new SetMapMode()); // 17 11
            addTag(new SetBkMode()); // 18 12
            addTag(new SetPolyFillMode()); // 19 13
            addTag(new SetROP2()); // 20 14
            addTag(new SetStretchBltMode()); // 21 15
            addTag(new SetTextAlign()); // 22 16
            // addTag(new SetColorAdjustment()); // 23 17
            addTag(new SetTextColor()); // 24 18
            addTag(new SetBkColor()); // 25 19
            addTag(new OffsetClipRgn()); // 26 1a
            addTag(new MoveToEx()); // 27 1b
            addTag(new SetMetaRgn()); // 28 1c
            addTag(new ExcludeClipRect()); // 29 1d
            addTag(new IntersectClipRect()); // 30 1e
            addTag(new ScaleViewportExtEx()); // 31 1f
            addTag(new ScaleWindowExtEx()); // 32 20
            addTag(new SaveDC()); // 33 21
            addTag(new RestoreDC()); // 34 22
            addTag(new SetWorldTransform()); // 35 23
            addTag(new ModifyWorldTransform()); // 36 24
            addTag(new SelectObject()); // 37 25
            addTag(new CreatePen()); // 38 26
            addTag(new CreateBrushIndirect()); // 39 27
            addTag(new DeleteObject()); // 40 28
            addTag(new AngleArc()); // 41 29
            addTag(new Ellipse()); // 42 2a
            addTag(new EMFRectangle()); // 43 2b
            addTag(new RoundRect()); // 44 2c
            addTag(new Arc()); // 45 2d
            addTag(new Chord()); // 46 2e
            addTag(new Pie()); // 47 2f
            addTag(new SelectPalette()); // 48 30
            // addTag(new CreatePalette()); // 49 31
            // addTag(new SetPaletteEntries()); // 50 32
            addTag(new ResizePalette()); // 51 33
            addTag(new RealizePalette()); // 52 34
            addTag(new ExtFloodFill()); // 53 35
            addTag(new LineTo()); // 54 36
            addTag(new ArcTo()); // 55 37
            addTag(new PolyDraw()); // 56 38
            addTag(new SetArcDirection()); // 57 39
            addTag(new SetMiterLimit()); // 58 3a
            addTag(new BeginPath()); // 59 3b
            addTag(new EndPath()); // 60 3c
            addTag(new CloseFigure()); // 61 3d
            addTag(new FillPath()); // 62 3e
            addTag(new StrokeAndFillPath()); // 63 3f
            addTag(new StrokePath()); // 64 40
            addTag(new FlattenPath()); // 65 41
            addTag(new WidenPath()); // 66 42
            addTag(new SelectClipPath()); // 67 43
            addTag(new AbortPath()); // 68 44
            // this tag does not exist // 69 45
            addTag(new GDIComment()); // 70 46
            // addTag(new FillRgn()); // 71 47
            // addTag(new FrameRgn()); // 72 48
            // addTag(new InvertRgn()); // 73 49
            // addTag(new PaintRgn()); // 74 4a
            addTag(new ExtSelectClipRgn()); // 75 4b
            addTag(new BitBlt()); // 76 4c
            // addTag(new StretchBlt()); // 77 4d
            // addTag(new MaskBlt()); // 78 4e
            // addTag(new PlgBlt()); // 79 4f
            // addTag(new SetDIBitsToDevice()); // 80 50
            addTag(new StretchDIBits()); // 81 51
            addTag(new ExtCreateFontIndirectW()); // 82 52
            addTag(new ExtTextOutA()); // 83 53
            addTag(new ExtTextOutW()); // 84 54
            addTag(new PolyBezier16()); // 85 55
            addTag(new Polygon16()); // 86 56
            addTag(new Polyline16()); // 87 57
            addTag(new PolyBezierTo16()); // 88 58
            addTag(new PolylineTo16()); // 89 59
            addTag(new PolyPolyline16()); // 90 5a
            addTag(new PolyPolygon16()); // 91 5b
            addTag(new PolyDraw16()); // 92 5c
            // addTag(new CreateMonoBrush()); // 93 5d
            addTag(new CreateDIBPatternBrushPt()); // 94 5e
            addTag(new ExtCreatePen()); // 95 5f
            // addTag(new PolyTextOutA()); // 96 60
            // addTag(new PolyTextOutW()); // 97 61

            // Set for Windows 4 (NT)
            addTag(new SetICMMode()); // 98 62
            // addTag(new CreateColorSpace()); // 99 63
            // addTag(new SetColorSpace()); // 100 64
            // addTag(new DeleteColorSpace()); // 101 65
            // addTag(new GLSRecord()); // 102 66
            // addTag(new GLSBoundedRecord()); // 103 67
            // addTag(new PixelFormat()); // 104 68

            // Set for Windows 5 (2000/XP)
            // addTag(new DrawEscape()); // 105 69
            // addTag(new ExtEscape()); // 106 6a
            // addTag(new StartDoc()); // 107 6b
            // addTag(new SmallTextOut()); // 108 6c
            // addTag(new ForceUFIMapping()); // 109 6d
            // addTag(new NamedEscape()); // 110 6e
            // addTag(new ColorCorrectPalette()); // 111 6f
            // addTag(new SetICMProfileA()); // 112 70
            // addTag(new SetICMProfileW()); // 113 71
            addTag(new AlphaBlend()); // 114 72
            // addTag(new AlphaDIBBlend()); // 115 73
            // addTag(new TransparentBlt()); // 116 74
            // addTag(new TransparentDIB()); // 117 75
            addTag(new GradientFill()); // 118 76
            // addTag(new SetLinkedUFIs()); // 119 77
            // addTag(new SetTextJustification()); // 120 78
        }
    }
}
