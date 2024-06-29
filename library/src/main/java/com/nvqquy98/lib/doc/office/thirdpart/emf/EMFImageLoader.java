package com.nvqquy98.lib.doc.office.thirdpart.emf;


//import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

import com.nvqquy98.lib.doc.office.java.awt.Color;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.BitmapInfoHeader;
import com.nvqquy98.lib.doc.office.thirdpart.emf.data.BlendFunction;

import android.graphics.Bitmap;

/**
 * this class creates a BufferedImage from EMF imaga data stored in
 * a byte[].
 *
 * @author Steffen Greiffenberg
 * @version $Id$
 */
public class EMFImageLoader {

    /**
     * creates a BufferedImage from an EMFInputStream using
     * BitmapInfoHeader data
     *
     * @param bmi BitmapInfoHeader storing Bitmap informations
     * @param width expected image width
     * @param height expected image height
     * @param emf EMF stream
     * @param len length of image data
     * @param blendFunction contains values for transparency
     * @return BufferedImage or null
     * @throws java.io.IOException thrown by EMFInputStream
     */
    public static Bitmap readImage(
        BitmapInfoHeader bmi,
        int width,
        int height,
        EMFInputStream emf,
        int len,
        BlendFunction blendFunction) throws IOException {

        // 0    Windows 98/Me, Windows 2000/XP: The number of bits-per-pixel
        // is specified or is implied by the JPEG or PNG format.

        if (bmi.getBitCount() == 1) {
            // 1 	The bitmap is monochrome, and the bmiColors
            // member of BITMAPINFO contains two entries. Each
            // bit in the bitmap array represents a pixel. If
            // the bit is clear, the pixel is displayed with
            // the color of the first entry in the bmiColors
            // table; if the bit is set, the pixel has the color
            // of the second entry in the table.
            // byte[] bytes = emf.readByte(len);

            int blue = emf.readUnsignedByte();
            int green = emf.readUnsignedByte();
            int red = emf.readUnsignedByte();
            /*int unused =*/ emf.readUnsignedByte();

            int color1 = new Color(red, green, blue).getRGB();

            blue = emf.readUnsignedByte();
            green = emf.readUnsignedByte();
            red = emf.readUnsignedByte();
            /*unused = */ emf.readUnsignedByte();

            int color2 = new Color(red, green, blue).getRGB();

            //BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            int[] data = emf.readUnsignedByte(len - 8);

            // TODO: this is highly experimental and does
            // not work for the tested examples
            int strangeOffset = width % 8;
            if (strangeOffset != 0) {
                strangeOffset = 8 - strangeOffset;
            }

            // iterator for pixel data
            int pixel = 0;

            // mask for getting the bits from a pixel data byte
            int[] mask = {0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80};

            // image data are swapped compared to java standard
            for (int y = height - 1; y > -1; y--) {
                for (int x = 0; x < width; x++) {
                    int pixelDataGroup = data[pixel / 8];
                    int pixelData = pixelDataGroup & mask[pixel % 8];
                    pixel ++;

                    if (pixelData > 0) {
                        //result.setRGB(x, y, color2);
                        result.setPixel(x, y, color2);
                    } else {
                        //result.setRGB(x, y, color1);
                        result.setPixel(x, y, color1);

                    }
                }
                // add the extra width
                pixel = pixel + strangeOffset;
            }

            /* for debugging: shows every loaded image
            javax.swing.JFrame f = new javax.swing.JFrame("test");
            f.getContentPane().setBackground(Color.green);
            f.getContentPane().setLayout(
                new java.awt.BorderLayout(0, 0));
            f.getContentPane().add(
                java.awt.BorderLayout.CENTER,
                new javax.swing.JLabel(
                    new javax.swing.ImageIcon(result)));
            f.setSize(new com.nvqquy98.lib.doc.office.java.awt.Dimension(width + 20, height + 20));
            f.setVisible(true);*/

            return result;

        } else if ((bmi.getBitCount() == 4)
				&& (bmi.getCompression() == EMFConstants.BI_RGB)) {
			// 4 The bitmap has a maximum of 256 colors, and the bmiColors
			// member
			// of BITMAPINFO contains up to 256 entries. In this case, each byte
			// in
			// the array represents a single pixel.

			// TODO has to be done in BitMapInfoHeader?
			// read the color table
			int colorsUsed = bmi.getClrUsed();

			// typedef struct tagRGBQUAD {
			// BYTE rgbBlue;
			// BYTE rgbGreen;
			// BYTE rgbRed;
			// BYTE rgbReserved;
			// } RGBQUAD;
			int[] colors = emf.readUnsignedByte(colorsUsed * 4);

			// data a indexes to a certain color in the colortable.
			// Each byte represents a pixel
			// int[] data = emf.readUnsignedByte(len - (colorsUsed * 4));
			int[] data = new int[len - (colorsUsed * 4)];
			for (int i = 0; i < (len - (colorsUsed * 4)) / 12; i++) {
				int[] bytes = emf.readUnsignedByte(10);
				emf.readUnsignedByte(2);
				System.arraycopy(bytes, 0, data, i * 10, 10);
			}

			// convert it to a color table
			int[] colorTable = new int[256];
			// iterator for color data
			int color = 0;
            for (int i = 0; i < colorsUsed; i++, color = i * 4) {
                colorTable[i] = new Color(
                    colors[color + 2],
                    colors[color + 1],
                    colors[color]).getRGB();
            }

			// fill with black to avoid ArrayIndexOutOfBoundExceptions;
			// somme images seem to use more colors than stored in ClrUsed
			if (colorsUsed < 256) {
				Arrays.fill(colorTable, colorsUsed, 256, 0);
			}

			// create the image
			Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

			// iterator for pixel data
			int pixel = 0;

			// image data are swapped compared to java standard
			for (int y = height - 1; y > -1; y--) {
				for (int x = 0; x < width; x += 2) {
					if (pixel < data.length) {
						result.setPixel(x, y, colorTable[data[pixel] % 8]);
						result.setPixel(x + 1, y, colorTable[data[pixel] % 8]);
						pixel++;
					} else {
						break;
					}
				}
			}
			return result;
		} else if ((bmi.getBitCount() == 8) &&
            (bmi.getCompression() == EMFConstants.BI_RGB)) {
            // 8 	The bitmap has a maximum of 256 colors, and the bmiColors member
            // of BITMAPINFO contains up to 256 entries. In this case, each byte in
            // the array represents a single pixel.

            // TODO has to be done in BitMapInfoHeader?
            // read the color table
            int colorsUsed = bmi.getClrUsed();

            // typedef struct tagRGBQUAD {
            //   BYTE    rgbBlue;
            //   BYTE    rgbGreen;
            //   BYTE    rgbRed;
            //   BYTE    rgbReserved;
            // } RGBQUAD;
            int[] colors = emf.readUnsignedByte(colorsUsed * 4);

            // data a indexes to a certain color in the colortable.
            // Each byte represents a pixel
            int[] data = emf.readUnsignedByte(len - (colorsUsed * 4));

            // convert it to a color table
            int[] colorTable = new int[256];
            // iterator for color data
            int color = 0;
            for (int i = 0; i < colorsUsed; i++, color = i * 4) {
                colorTable[i] = new Color(
                    colors[color + 2],
                    colors[color + 1],
                    colors[color]).getRGB();
            }

            // fill with black to avoid ArrayIndexOutOfBoundExceptions;
            // somme images seem to use more colors than stored in ClrUsed
            if (colorsUsed < 256) {
                Arrays.fill(colorTable, colorsUsed, 256, 0);
            }

            // don't know why, but the width has to be adjusted ...
            // it took more than an hour to determine the strangeOffset
            int strangeOffset = width % 4;
            if (strangeOffset != 0) {
                strangeOffset = 4 - strangeOffset;
            }

            // create the image
            //BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            // iterator for pixel data
            int pixel = 0;

            // image data are swapped compared to java standard
            for (int y = height - 1; y > -1; y--) {
                for (int x = 0; x < width; x++) {
                    //result.setRGB(x, y, colorTable[data[pixel++]]);
                	result.setPixel(x, y, colorTable[data[pixel++]]);
                }
                // add the extra width
                pixel = pixel + strangeOffset;
            }

            return result;
        }

        // The bitmap has a maximum of 2^16 colors. If the biCompression member
        // of the BITMAPINFOHEADER is BI_RGB, the bmiColors member of BITMAPINFO is
        // NULL.
        else if ((bmi.getBitCount() == 16) &&
            (bmi.getCompression() == EMFConstants.BI_RGB)) {

            // Each WORD in the bitmap array represents a single pixel. The
            // relative intensities of red, green, and blue are represented with
            // five bits for each color component. The value for blue is in the least
            // significant five bits, followed by five bits each for green and red.
            // The most significant bit is not used. The bmiColors color table is used
            // for optimizing colors used on palette-based devices, and must contain
            // the number of entries specified by the biClrUsed member of the
            // BITMAPINFOHEADER.
            int[] data = emf.readDWORD(len / 4);

            // don't know why, by the width has to be the half ...
            // maybe that has something to do with sie HALFTONE rendering setting.
            width = (width + (width % 2)) / 2;
            // to avoid ArrayIndexOutOfBoundExcesptions
            height = data.length / width / 2;

            // create a non transparent image
            //BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            // found no sample and color model to mak this work
            // tag.image.setRGB(0, 0, tag.widthSrc, tag.heightSrc, data, 0, 0);

            // used in the loop
            int off = 0;
            int pixel, neighbor;

            // image data are swapped compared to java standard
            for (int y = height - 1; y > -1; y--, off = off + width) {
                for (int x = 0; x < width; x++) {
                    neighbor = data[off + width];
                    pixel = data[off++];

                    // compute the average of the pixel and it's neighbor
                    // and set the reulting color values
                    //result.setRGB(x, y, new Color(
                    result.setPixel(x, y, new Color(
                        // 0xF800 = 2 * 0x7C00
                        (float)((pixel & 0x7C00) + (neighbor & 0x7C00)) / 0xF800,
                        (float)((pixel & 0x3E0) + (neighbor & 0x3E0)) / 0x7C0,
                        (float)((pixel & 0x1F) + (neighbor & 0x1F)) / 0x3E).getRGB());
                }
            }

            /* for debugging: shows every loaded image
            javax.swing.JFrame f = new javax.swing.JFrame("test");
            f.getContentPane().setBackground(Color.green);
            f.getContentPane().setLayout(
                new java.awt.BorderLayout(0, 0));
            f.getContentPane().add(
                java.awt.BorderLayout.CENTER,
                new javax.swing.JLabel(
                    new javax.swing.ImageIcon(result)));
            f.pack();
            f.setVisible(true);*/

            return result;
        }
        // The bitmap has a maximum of 2^32 colors. If the biCompression member of the
        // BITMAPINFOHEADER is BI_RGB, the bmiColors member of BITMAPINFO is NULL.
        else if ((bmi.getBitCount() == 32) &&
            (bmi.getCompression() == EMFConstants.BI_RGB)) {
            // Each DWORD in the bitmap array represents the relative intensities of blue,
            // green, and red, respectively, for a pixel. The high byte in each DWORD is not
            // used. The bmiColors color table is used for optimizing colors used on
            // palette-based devices, and must contain the number of entries specified
            // by the biClrUsed member of the BITMAPINFOHEADER.

            //width = (width + (width % 20)) / 20;
            //height = (height + (height % 20)) / 20;

            // create a transparent image
            //BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            // read the image data
            //int[] data = emf.readDWORD(len / 4);
            len /= 4;

            // used to iterate the pixels later
            int off = 0;
            int pixel;
            int alpha;

            // The SourceConstantaAlpha member of BLENDFUNCTION specifies an alpha transparency
            // value to be used on the entire source bitmap. The SourceConstantAlpha value is
            // combined with any per-pixel alpha values. If SourceConstantAlpha is 0, it is
            // assumed that the image is transparent. Set the SourceConstantAlpha value to 255
            // (which indicates that the image is opaque) when you only want to use per-pixel
            // alpha values.
            //int sourceConstantAlpha = blendFunction.getSourceConstantAlpha();
			int sourceConstantAlpha = 0xFF;
			int alphaFormat = EMFConstants.AC_SRC_OVER;

			if (blendFunction != null) {
				sourceConstantAlpha = blendFunction.getSourceConstantAlpha();
				alphaFormat = blendFunction.getAlphaFormat();
			}

            //if (blendFunction.getAlphaFormat() != EMFConstants.AC_SRC_ALPHA) {
			if (alphaFormat != EMFConstants.AC_SRC_ALPHA) {
                // If the source bitmap has no per-pixel alpha value (that is, AC_SRC_ALPHA is not
                // set), the SourceConstantAlpha value determines the blend of the source and
                // destination bitmaps, as shown in the following table. Note that SCA is used
                // for SourceConstantAlpha here. Also, SCA is divided by 255 because it has a
                // value that ranges from 0 to 255.

                // Dst.Red 	= Src.Red * (SCA/255.0) 	+ Dst.Red * (1.0 - (SCA/255.0))
                // Dst.Green 	= Src.Green * (SCA/255.0) 	+ Dst.Green * (1.0 - (SCA/255.0))
                // Dst.Blue 	= Src.Blue * (SCA/255.0) 	+ Dst.Blue * (1.0 - (SCA/255.0))

                // If the destination bitmap has an alpha channel, then the blend is as follows.
                // Dst.Alpha 	= Src.Alpha * (SCA/255.0) 	+ Dst.Alpha * (1.0 - (SCA/255.0))

                for (int y = height - 1; y > -1 && off < len; y--) {
                    for (int x = 0; x < width && off < len; x++, off++) {
                        //pixel = data[off++];
                    	pixel = emf.readDWORD();

                        //result.setRGB(x, y, new Color(
                        result.setPixel(x, y, new Color(
                            (pixel & 0xFF0000) >> 16,
                            (pixel & 0xFF00) >> 8,
                            (pixel & 0xFF),
                            // TODO not tested
                            sourceConstantAlpha
                        ).getRGB());
                    }
                }
            }
            // When the BlendOp parameter is AC_SRC_OVER , the source bitmap is placed over
            // the destination bitmap based on the alpha values of the source pixels.
            else {
                // If the source bitmap does not use SourceConstantAlpha (that is, it equals
                // 0xFF), the per-pixel alpha determines the blend of the source and destination
                // bitmaps, as shown in the following table.
                if (sourceConstantAlpha == 0xFF) {
                    // Dst.Red 	= Src.Red 	+ (1 - Src.Alpha) * Dst.Red
                    // Dst.Green 	= Src.Green 	+ (1 - Src.Alpha) * Dst.Green
                    // Dst.Blue 	= Src.Blue 	+ (1 - Src.Alpha) * Dst.Blue

                    // If the destination bitmap has an alpha channel, then the blend is as follows.
                    // Dest.alpha 	= Src.Alpha 	+ (1 - SrcAlpha) * Dst.Alpha

                    // image data are swapped compared to java standard
                    for (int y = height - 1; y > -1 && off < len; y--) {
                        for (int x = 0; x < width && off < len; x++, off++) {
                            //pixel = data[off++];
                        	pixel = emf.readDWORD();
                            alpha = (pixel & 0xFF000000) >> 24;
                            if (alpha == -1) {
                                alpha = 0xFF;
                            }

                            //result.setRGB(x, y, new Color(
                            result.setPixel(x, y, new Color(
                                (pixel & 0xFF0000) >> 16,
                                (pixel & 0xFF00) >> 8,
                                (pixel & 0xFF),
                                alpha
                            ).getRGB());
                        }
                    }
                }

                // If the source has both the SourceConstantAlpha (that is, it is not 0xFF)
                // and per-pixel alpha, the source is pre-multiplied by the SourceConstantAlpha
                // and then the blend is based on the per-pixel alpha. The following tables show
                // this. Note that SourceConstantAlpha is divided by 255 because it has a value
                // that ranges from 0 to 255.
                else {
                    // Src.Red 	= Src.Red 	* SourceConstantAlpha / 255.0;
                    // Src.Green 	= Src.Green 	* SourceConstantAlpha / 255.0;
                    // Src.Blue 	= Src.Blue 	* SourceConstantAlpha / 255.0;
                    // Src.Alpha 	= Src.Alpha 	* SourceConstantAlpha / 255.0;

                    // Dst.Red 	= Src.Red 	+ (1 - Src.Alpha) * Dst.Red
                    // Dst.Green 	= Src.Green 	+ (1 - Src.Alpha) * Dst.Green
                    // Dst.Blue 	= Src.Blue 	+ (1 - Src.Alpha) * Dst.Blue
                    // Dst.Alpha 	= Src.Alpha 	+ (1 - Src.Alpha) * Dst.Alpha

                    for (int y = height - 1; y > -1 && off < len; y--) {
                        for (int x = 0; x < width && off < len; x++, off++) {
                            //pixel = data[off++];
                        	pixel = emf.readDWORD();

                            alpha = (pixel & 0xFF000000) >> 24;
                            if (alpha == -1) {
                                alpha = 0xFF;
                            }

                            // TODO not tested
                            alpha = alpha * sourceConstantAlpha / 0xFF;

                            //result.setRGB(x, y, new Color(
                            result.setPixel(x, y, new Color(
                                (pixel & 0xFF0000) >> 16,
                                (pixel & 0xFF00) >> 8,
                                (pixel & 0xFF),
                                alpha
                            ).getRGB());
                        }
                    }
                }
            }
                        
            /* for debugging: shows every loaded image
            javax.swing.JFrame f = new javax.swing.JFrame("test");
            f.getContentPane().setBackground(Color.green);
            f.getContentPane().setLayout(
                new java.awt.BorderLayout(0, 0));
            f.getContentPane().add(
                java.awt.BorderLayout.CENTER,
                new javax.swing.JLabel(
                    new javax.swing.ImageIcon(result)));
            f.setSize(new com.nvqquy98.lib.doc.office.java.awt.Dimension(width + 20, height + 20));
            f.setVisible(true);*/
            
            return result;
        }
        // If the biCompression member of the BITMAPINFOHEADER is BI_BITFIELDS,
        // the bmiColors member contains three DWORD color masks that specify the
        // red, green, and blue components, respectively, of each pixel. Each DWORD
        // in the bitmap array represents a single pixel.

        // Windows NT/ 2000: When the biCompression member is BI_BITFIELDS, bits set in
        // each DWORD mask must be contiguous and should not overlap the bits of
        // another mask. All the bits in the pixel do not need to be used.

        // Windows 95/98/Me: When the biCompression member is BI_BITFIELDS, the system
        // supports only the following 32-bpp color mask: The blue mask is 0x000000FF,
        // the green mask is 0x0000FF00, and the red mask is 0x00FF0000.
        else if ((bmi.getBitCount() == 32) &&
            (bmi.getCompression() == EMFConstants.BI_BITFIELDS)) {
            /* byte[] bytes =*/ emf.readByte(len);
            return null;
        } else {
            /* byte[] bytes =*/ emf.readByte(len);
            return null;
        }
    }
}
