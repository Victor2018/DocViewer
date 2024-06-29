/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package com.nvqquy98.lib.doc.office.fc.hwpf.model;

import java.util.Arrays;

import com.nvqquy98.lib.doc.office.fc.util.BitField;
import com.nvqquy98.lib.doc.office.fc.util.Internal;
import com.nvqquy98.lib.doc.office.fc.util.LittleEndian;


/**
 * Picture Descriptor (on File) (PICF)
 * <p>
 * Based on Microsoft Office Word 97-2007 Binary File Format (.doc)
 * Specification; Page 181 of 210
 * 
 * @author Sergey Vladimirov ( vlsergey {at} gmail {dot} com )
 */
@ Internal
public class PictureDescriptor
{
	public class BlipBooleanProperties
	{
		public BitField fPictureBiLevel;
		public BitField fPictureGray;
		public BitField fUsefPictureBiLevel;
		public BitField fUsefPictureGray;
		int propValue;
	};
	
	public class OfficeArtRecordHeader
	{
		public BitField recVer;
		public BitField recInstance;
		public short recFlag;
		public int recType;
		public long recLength;
	};
	
	public class OfficeArtOpid
	{
		public BitField opid;
		public BitField fBid;
		public BitField fComplex;
		public short flag;
	};
	
	
    private static final int LCB_OFFSET = 0x00;
    private static final int CBHEADER_OFFSET = 0x04;

    private static final int MFP_MM_OFFSET = 0x06;
    private static final int MFP_XEXT_OFFSET = 0x08;
    private static final int MFP_YEXT_OFFSET = 0x0A;
    private static final int MFP_HMF_OFFSET = 0x0C;

    private static final int DXAGOAL_OFFSET = 0x1C;
    private static final int DYAGOAL_OFFSET = 0x1E;

    private static final int MX_OFFSET = 0x20;
    private static final int MY_OFFSET = 0x22;

    private static final int DXACROPLEFT_OFFSET = 0x24;
    private static final int DYACROPTOP_OFFSET = 0x26;
    private static final int DXACROPRIGHT_OFFSET = 0x28;
    private static final int DYACROPBOTTOM_OFFSET = 0x2A;

    /**
     * Number of bytes in the PIC structure plus size of following picture data
     * which may be a Window's metafile, a bitmap, or the filename of a TIFF
     * file. In the case of a Macintosh PICT picture, this includes the size of
     * the PIC, the standard "x" metafile, and the Macintosh PICT data. See
     * Appendix B for more information.
     */
    protected int lcb;

    /**
     * Number of bytes in the PIC (to allow for future expansion).
     */
    protected int cbHeader;

    /*
     * Microsoft Office Word 97-2007 Binary File Format (.doc) Specification
     * 
     * Page 181 of 210
     * 
     * If a Windows metafile is stored immediately following the PIC structure,
     * the mfp is a Window's METAFILEPICT structure. See
     * http://msdn2.microsoft.com/en-us/library/ms649017(VS.85).aspx for more
     * information about the METAFILEPICT structure and
     * http://download.microsoft.com/download/0/B/E/0BE8BDD7-E5E8-422A-ABFD-
     * 4342ED7AD886/WindowsMetafileFormat(wmf)Specification.pdf for Windows
     * Metafile Format specification.
     * 
     * When the data immediately following the PIC is a TIFF filename,
     * mfp.mm==98 If a bitmap is stored after the pic, mfp.mm==99.
     * 
     * When the PIC describes a bitmap, mfp.xExt is the width of the bitmap in
     * pixels and mfp.yExt is the height of the bitmap in pixels.
     */

    protected int mfp_mm;
    protected int mfp_xExt;
    protected int mfp_yExt;
    protected int mfp_hMF;

    /**
     * <li>Window's bitmap structure when PIC describes a BITMAP (14 bytes)
     * 
     * <li>Rectangle for window origin and extents when metafile is stored --
     * ignored if 0 (8 bytes)
     */
    protected byte[] offset14 = new byte[14];

    /**
     * Horizontal measurement in twips of the rectangle the picture should be
     * imaged within
     */
    protected short dxaGoal = 0;

    /**
     * Vertical measurement in twips of the rectangle the picture should be
     * imaged within
     */
    protected short dyaGoal = 0;

    /**
     * Horizontal scaling factor supplied by user expressed in .001% units
     */
    protected short mx;

    /**
     * Vertical scaling factor supplied by user expressed in .001% units
     */
    protected short my;

    /**
     * The amount the picture has been cropped on the left in twips
     */
    protected float dxaCropLeft = 0;

    /**
     * The amount the picture has been cropped on the top in twips
     */
    protected float dyaCropTop = 0;

    /**
     * The amount the picture has been cropped on the right in twips
     */
    protected float dxaCropRight = 0;

    /**
     * The amount the picture has been cropped on the bottom in twips
     */
    protected float dyaCropBottom = 0;

    public float getBright() 
    {
		return fBright;
	}

	public float getContrast() 
	{
		return fContrast;
	}

	public boolean isGrayScl()
	{
		return bGrayScl;
	}

	public float getThreshold()
	{
		return fThreshold;
	}
	
	public boolean isSetBright() 
	{
		return bSetBright;
	}

	public boolean isSetContrast()
	{
		return bSetContrast;
	}
	
	public boolean isSetGrayScl()
	{
		return bSetGrayScl;
	}

	public boolean isSetThreshold()
	{
		return bSetThreshold;
	}

	protected float fBright;
    protected float fContrast;
    protected boolean bGrayScl;
	protected float fThreshold;
	protected boolean bSetBright;
	protected boolean bSetContrast;
	protected boolean bSetGrayScl;
	protected boolean bSetThreshold;
	
    public PictureDescriptor()
    {
    }

    public PictureDescriptor(byte[] _dataStream, int startOffset)
    {
        this.lcb = LittleEndian.getInt(_dataStream, startOffset + LCB_OFFSET);
        this.cbHeader = LittleEndian.getUShort(_dataStream, startOffset + CBHEADER_OFFSET);

        this.mfp_mm = LittleEndian.getUShort(_dataStream, startOffset + MFP_MM_OFFSET);
        this.mfp_xExt = LittleEndian.getUShort(_dataStream, startOffset + MFP_XEXT_OFFSET);
        this.mfp_yExt = LittleEndian.getUShort(_dataStream, startOffset + MFP_YEXT_OFFSET);
        this.mfp_hMF = LittleEndian.getUShort(_dataStream, startOffset + MFP_HMF_OFFSET);

        this.offset14 = LittleEndian.getByteArray(_dataStream, startOffset + 0x0E, 14);

        this.dxaGoal = LittleEndian.getShort(_dataStream, startOffset + DXAGOAL_OFFSET);
        this.dyaGoal = LittleEndian.getShort(_dataStream, startOffset + DYAGOAL_OFFSET);

        this.mx = LittleEndian.getShort(_dataStream, startOffset + MX_OFFSET);
        this.my = LittleEndian.getShort(_dataStream, startOffset + MY_OFFSET);

        int pictureOffset = startOffset + 68;        
        if(this.mfp_mm == 0x66)
        {
        	// Skip the stPicName
    		int cchPicName = _dataStream[pictureOffset] & 0xFFFF;
    		pictureOffset += 1 + cchPicName;
        }
        
        OfficeArtRecordHeader officeArtRecHeader = readHeader(_dataStream, pictureOffset);
        short recVer = getRecVer(officeArtRecHeader);
		short recInstance = getRecInstance(officeArtRecHeader);
		if (recVer == 0xF && recInstance == 0x0000 && officeArtRecHeader.recType == 0xF004)
		{
			// this is OfficeArtSpContainer
			long len = officeArtRecHeader.recLength;
			pictureOffset += 8;
			officeArtRecHeader = null;
			while(len > 0 && pictureOffset < _dataStream.length)
			{
				officeArtRecHeader = readHeader(_dataStream, pictureOffset);
				len -= officeArtRecHeader.recLength;
				pictureOffset += 8;
				
				recVer = getRecVer(officeArtRecHeader);;
				recInstance = getRecInstance(officeArtRecHeader);
				if (recVer == 0x3 && officeArtRecHeader.recType == 0xF00B)
				{
					// this is OfficeArtFOPT
					int nPropertyNum = getRecInstance(officeArtRecHeader);
					int nIndex = 0;
					for (nIndex = 0; nIndex < nPropertyNum; nIndex++)
					{						
						OfficeArtOpid officeArtOpid = readOfficeArtOpid(_dataStream, pictureOffset);
						short opid = getOpid(officeArtOpid);
						boolean bBid = isfBid(officeArtOpid);
						boolean bComplex = isfComplex(officeArtOpid);
						byte[] fixedPoint = null;
						
						if (opid == 0x0100 && bBid == false && bComplex == false)
						{
							// crop from top
							fixedPoint = LittleEndian.getByteArray(_dataStream, pictureOffset + 2, 4);
							this.dyaCropTop = getRealNumFromFixedPoint(fixedPoint);
						}
						if (opid == 0x0101 && bBid == false && bComplex == false)
						{
							// crop from bottom
							fixedPoint = LittleEndian.getByteArray(_dataStream, pictureOffset + 2, 4);
							this.dyaCropBottom = getRealNumFromFixedPoint(fixedPoint);
						}
						if (opid == 0x0102 && bBid == false && bComplex == false)
						{
							// crop from left
							fixedPoint = LittleEndian.getByteArray(_dataStream, pictureOffset + 2, 4);
							this.dxaCropLeft = getRealNumFromFixedPoint(fixedPoint);
						}
						if (opid == 0x0103 && bBid == false && bComplex == false)
						{
							// crop from right
							fixedPoint = LittleEndian.getByteArray(_dataStream, pictureOffset + 2, 4);
							this.dxaCropRight = getRealNumFromFixedPoint(fixedPoint);
						}
						if (opid == 0x0109 && bBid == false && bComplex == false)
						{
							// pictureBrightness
							int propValue = LittleEndian.getInt(_dataStream, pictureOffset + 2);
							this.bSetBright = true;
							this.fBright = (float)propValue / 32768.0f * 255;
						}
						if (opid == 0x0108 && bBid == false && bComplex == false)
						{
							// pictureContrast
							int propValue = LittleEndian.getInt(_dataStream, pictureOffset + 2);
							this.bSetContrast = true;
							this.fContrast = Math.min(propValue / 65536f, 10);
						}
						if (opid == 0x013F && bBid == false && bComplex == false)
						{
							// Blip Boolean Properties
							BlipBooleanProperties blipProperties = readBlipBooleanProperties(_dataStream, pictureOffset + 2);
							
							// fPictureBiLevel property(black and white)
							if (isfUsefPictureBiLevel(blipProperties))
							{
								if (isfPictureBiLevel(blipProperties))
								{
									this.bSetThreshold = true;
									this.fThreshold = 128;
								}
							}
							// fPictureGray property(gray scale)
							else if (isfUsefPictureGray(blipProperties))
							{
								if (isfPictureGray(blipProperties))
								{
									this.bSetGrayScl = true;
									this.bGrayScl = true;										
								}
							}
						}

						pictureOffset += 6;
					}	
					
					break;
				}
				
				pictureOffset += officeArtRecHeader.recLength;
			}
		}
    }
    
    /**
     * 
     * @param officeArtRecHeader
     * @return
     */
    private short getRecVer(OfficeArtRecordHeader officeArtRecHeader)
    {
    	return (short)officeArtRecHeader.recVer.getValue(officeArtRecHeader.recFlag);
    }
    
    /**
     * 
     * @param officeArtRecHeader
     * @return
     */
    private short getRecInstance(OfficeArtRecordHeader officeArtRecHeader)
    {
    	return (short)officeArtRecHeader.recInstance.getValue(officeArtRecHeader.recFlag);
    }
    
    private OfficeArtRecordHeader readHeader(byte[] _dataStream, int startoffset)
    {
    	OfficeArtRecordHeader officeArt = new OfficeArtRecordHeader();
    	officeArt.recVer =  new BitField(0x000F);
    	officeArt.recInstance = new BitField(0xFFF0);
    	if (startoffset + 4 < _dataStream.length)
    	{
        	officeArt.recFlag = LittleEndian.getShort(_dataStream, startoffset);
        	officeArt.recType = LittleEndian.getUShort(_dataStream, startoffset + 2);
        	officeArt.recLength = LittleEndian.getUInt(_dataStream, startoffset + 4);
    	}
    	return officeArt;
    }
    
    private OfficeArtOpid readOfficeArtOpid(byte[] _dataStream, int startoffset)
    {
    	OfficeArtOpid officeArtOpid = new OfficeArtOpid();
    	officeArtOpid.opid =  new BitField(0x3FFF);
    	officeArtOpid.fBid = new BitField(0x4000);
    	officeArtOpid.fComplex = new BitField(0x8000);
    	officeArtOpid.flag = LittleEndian.getShort(_dataStream, startoffset);
    	return officeArtOpid;
    }
    
    private short getOpid(OfficeArtOpid officeArtOpid)
    {
    	return officeArtOpid.opid.getShortValue(officeArtOpid.flag);
    }
    
    private boolean isfBid(OfficeArtOpid officeArtOpid)
    {
    	return officeArtOpid.fBid.getShortValue(officeArtOpid.flag) == 1;
    }
    
    private boolean isfComplex(OfficeArtOpid officeArtOpid)
    {
    	return officeArtOpid.fComplex.getShortValue(officeArtOpid.flag) == 1;
    }
    
    private float getRealNumFromFixedPoint(byte[] data)
    {
    	short integral = LittleEndian.getShort(data, 2);
    	int fractional = LittleEndian.getUShort(data, 0);
    	return integral + (fractional / 65536.0f);
    }
    
    private BlipBooleanProperties readBlipBooleanProperties(byte[] _dataStream, int startoffset)
    {
    	BlipBooleanProperties properties = new BlipBooleanProperties();
    	properties.propValue = LittleEndian.getInt(_dataStream, startoffset);
    	properties.fPictureBiLevel = new BitField(0x20000);
    	properties.fUsefPictureBiLevel = new BitField(0x2);
    	properties.fPictureGray = new BitField(0x40000);
    	properties.fUsefPictureGray = new BitField(0x4);
    	return properties;
    }
    
    private boolean isfUsefPictureBiLevel(BlipBooleanProperties properties)
    {
    	return properties.fUsefPictureBiLevel.getValue(properties.propValue) == 1;
    }

    private boolean isfPictureBiLevel(BlipBooleanProperties properties)
    {
    	return properties.fPictureBiLevel.getValue(properties.propValue) == 1;
    }

    private boolean isfUsefPictureGray(BlipBooleanProperties properties)
    {
    	return properties.fUsefPictureBiLevel.getValue(properties.propValue) == 1;
    }

    private boolean isfPictureGray(BlipBooleanProperties properties)
    {
    	return properties.fPictureBiLevel.getValue(properties.propValue) == 1;
    }
    
    
    /**
     * 
     *(non-Javadoc)
     * @see java.lang.Object#toString()
     *
     */
    public short getZoomX()
    {
        return this.mx;
    }
    
    /**
     * 
     *(non-Javadoc)
     * @see java.lang.Object#toString()
     *
     */
    public short getZoomY()
    {
        return this.my;
    }

    @ Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[PICF]\n");
        stringBuilder.append("        lcb           = ").append(this.lcb).append('\n');
        stringBuilder.append("        cbHeader      = ").append(this.cbHeader).append('\n');

        stringBuilder.append("        mfp.mm        = ").append(this.mfp_mm).append('\n');
        stringBuilder.append("        mfp.xExt      = ").append(this.mfp_xExt).append('\n');
        stringBuilder.append("        mfp.yExt      = ").append(this.mfp_yExt).append('\n');
        stringBuilder.append("        mfp.hMF       = ").append(this.mfp_hMF).append('\n');

        stringBuilder.append("        offset14      = ").append(Arrays.toString(this.offset14))
            .append('\n');
        stringBuilder.append("        dxaGoal       = ").append(this.dxaGoal).append('\n');
        stringBuilder.append("        dyaGoal       = ").append(this.dyaGoal).append('\n');

        stringBuilder.append("        dxaCropLeft   = ").append(this.dxaCropLeft).append('\n');
        stringBuilder.append("        dyaCropTop    = ").append(this.dyaCropTop).append('\n');
        stringBuilder.append("        dxaCropRight  = ").append(this.dxaCropRight).append('\n');
        stringBuilder.append("        dyaCropBottom = ").append(this.dyaCropBottom).append('\n');

        stringBuilder.append("[/PICF]");
        return stringBuilder.toString();
    }
}
