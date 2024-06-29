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

package com.nvqquy98.lib.doc.office.fc.hslf.blip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.InflaterInputStream;

import com.nvqquy98.lib.doc.office.fc.ShapeKit;
import com.nvqquy98.lib.doc.office.fc.hslf.exceptions.HSLFException;
import com.nvqquy98.lib.doc.office.fc.hslf.model.Picture;
import com.nvqquy98.lib.doc.office.java.awt.Dimension;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;


/**
 * Represents EMF (Windows Enhanced Metafile) picture data.
 *
 * @author Yegor Kozlov
 */
public final class EMF extends Metafile
{

    /**
     * Extract compressed EMF data from a ppt
     */
    public byte[] getData()
    {
        try
        {
            byte[] rawdata = getRawData();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream is = new ByteArrayInputStream(rawdata);
            is.skip(8);
            
            Header header = new Header();
            header.read(rawdata, CHECKSUM_SIZE);
            is.skip(header.getSize() + CHECKSUM_SIZE);

            InflaterInputStream inflater = new InflaterInputStream(is);
            byte[] chunk = new byte[4096];
            int count;
            while ((count = inflater.read(chunk)) >= 0)
            {
                out.write(chunk, 0, count);
            }
            inflater.close();
            return out.toByteArray();
        }
        catch(IOException e)
        {
            throw new HSLFException(e);
        }
    }

    public void setData(byte[] data) throws IOException
    {
        byte[] compressed = compress(data, 0, data.length);

        Header header = new Header();
        header.wmfsize = data.length;
        //we don't have a EMF reader in java, have to set default image size  200x200
        header.bounds = new Rectangle(0, 0, 200, 200);
        header.size = new Dimension(header.bounds.width * ShapeKit.EMU_PER_POINT,
            header.bounds.height * ShapeKit.EMU_PER_POINT);
        header.zipsize = compressed.length;

        byte[] checksum = getChecksum(data);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(checksum);
        header.write(out);
        out.write(compressed);

        setRawData(out.toByteArray());
    }

    public int getType()
    {
        return Picture.EMF;
    }

    /**
     * EMF signature is <code>0x3D40</code>
     *
     * @return EMF signature (<code>0x3D40</code>)
     */
    public int getSignature()
    {
        return 0x3D40;
    }
}
