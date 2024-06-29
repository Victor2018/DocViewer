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

import java.io.IOException;
import java.util.Arrays;

import com.nvqquy98.lib.doc.office.fc.hwpf.model.io.HWPFOutputStream;
import com.nvqquy98.lib.doc.office.fc.util.Internal;


@Internal
public class BookmarksTables
{
    private PlexOfCps descriptorsFirst = new PlexOfCps( 4 );

    private PlexOfCps descriptorsLim = new PlexOfCps( 0 );

    private String[] names = new String[0];

    public BookmarksTables( byte[] tableStream, FileInformationBlock fib )
    {
        read( tableStream, fib );
    }

    public int getBookmarksCount()
    {
        return descriptorsFirst.length();
    }

    public GenericPropertyNode getDescriptorFirst( int index )
            throws IndexOutOfBoundsException
    {
        return descriptorsFirst.getProperty( index );
    }

    public int getDescriptorFirstIndex( GenericPropertyNode descriptorFirst )
    {
        // TODO: very non-optimal
        return Arrays.asList( descriptorsFirst.toPropertiesArray() ).indexOf(
                descriptorFirst );
    }

    public GenericPropertyNode getDescriptorLim( int index )
            throws IndexOutOfBoundsException
    {
        return descriptorsLim.getProperty( index );
    }

    public int getDescriptorsFirstCount()
    {
        return descriptorsFirst.length();
    }

    public int getDescriptorsLimCount()
    {
        return descriptorsLim.length();
    }

    public String getName( int index ) throws ArrayIndexOutOfBoundsException
    {
        return names[index];
    }

    public int getNamesCount()
    {
        return names.length;
    }

    private void read( byte[] tableStream, FileInformationBlock fib )
    {
        int namesStart = fib.getFcSttbfbkmk();
        int namesLength = fib.getLcbSttbfbkmk();

        if ( namesStart != 0 && namesLength != 0 )
            this.names = SttbfUtils.read( tableStream, namesStart );

        int firstDescriptorsStart = fib.getFcPlcfbkf();
        int firstDescriptorsLength = fib.getLcbPlcfbkf();
        if ( firstDescriptorsStart != 0 && firstDescriptorsLength != 0 )
            descriptorsFirst = new PlexOfCps( tableStream,
                    firstDescriptorsStart, firstDescriptorsLength,
                    BookmarkFirstDescriptor.getSize() );

        int limDescriptorsStart = fib.getFcPlcfbkl();
        int limDescriptorsLength = fib.getLcbPlcfbkl();
        if ( limDescriptorsStart != 0 && limDescriptorsLength != 0 )
            descriptorsLim = new PlexOfCps( tableStream, limDescriptorsStart,
                    limDescriptorsLength, 0 );
    }

    public void setName( int index, String name )
    {
        if ( index < names.length )
        {
            String[] newNames = new String[index + 1];
            System.arraycopy( names, 0, newNames, 0, names.length );
            names = newNames;
        }
        names[index] = name;
    }

    public void writePlcfBkmkf( FileInformationBlock fib,
            HWPFOutputStream tableStream ) throws IOException
    {
        if ( descriptorsFirst == null || descriptorsFirst.length() == 0 )
        {
            fib.setFcPlcfbkf( 0 );
            fib.setLcbPlcfbkf( 0 );
            return;
        }

        int start = tableStream.getOffset();
        tableStream.write( descriptorsFirst.toByteArray() );
        int end = tableStream.getOffset();

        fib.setFcPlcfbkf( start );
        fib.setLcbPlcfbkf( end - start );
    }

    public void writePlcfBkmkl( FileInformationBlock fib,
            HWPFOutputStream tableStream ) throws IOException
    {
        if ( descriptorsLim == null || descriptorsLim.length() == 0 )
        {
            fib.setFcPlcfbkl( 0 );
            fib.setLcbPlcfbkl( 0 );
            return;
        }

        int start = tableStream.getOffset();
        tableStream.write( descriptorsLim.toByteArray() );
        int end = tableStream.getOffset();

        fib.setFcPlcfbkl( start );
        fib.setLcbPlcfbkl( end - start );
    }

    public void writeSttbfBkmk( FileInformationBlock fib,
            HWPFOutputStream tableStream ) throws IOException
    {
        if ( names == null || names.length == 0 )
        {
            fib.setFcSttbfbkmk( 0 );
            fib.setLcbSttbfbkmk( 0 );
            return;
        }

        int start = tableStream.getOffset();
        SttbfUtils.write( tableStream, names );
        int end = tableStream.getOffset();

        fib.setFcSttbfbkmk( start );
        fib.setLcbSttbfbkmk( end - start );
    }
}
