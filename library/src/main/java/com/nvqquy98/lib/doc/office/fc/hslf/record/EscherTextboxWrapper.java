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

package com.nvqquy98.lib.doc.office.fc.hslf.record;

import com.nvqquy98.lib.doc.office.fc.ddf.EscherTextboxRecord;


/**
 * A wrapper around a DDF (Escher) EscherTextbox Record. Causes the DDF
 *  Record to be accessible as if it were a HSLF record.
 * Note: when asked to write out, will simply put any child records correctly
 *  into the Escher layer. A call to the escher layer to write out (by the
 *  parent PPDrawing) will do the actual write out
 *
 * @author Nick Burch
 */
public final class EscherTextboxWrapper extends RecordContainer
{
    private EscherTextboxRecord _escherRecord;
    private long _type;
    private int shapeId;

    /**
     * Returns the underlying DDF Escher Record
     */
    public EscherTextboxRecord getEscherRecord()
    {
        return _escherRecord;
    }

    /**
     * Creates the wrapper for the given DDF Escher Record and children
     */
    public EscherTextboxWrapper(EscherTextboxRecord textbox)
    {
        _escherRecord = textbox;
        _type = _escherRecord.getRecordId();

        // Find the child records in the escher data
        byte[] data = _escherRecord.getData();
        _children = Record.findChildRecords(data, 0, data.length);
    }

    /**
     * Creates a new, empty wrapper for DDF Escher Records and their children
     */
    public EscherTextboxWrapper()
    {
        _escherRecord = new EscherTextboxRecord();
        _escherRecord.setRecordId(EscherTextboxRecord.RECORD_ID);
        _escherRecord.setOptions((short)15);

        _children = new Record[0];
    }

    /**
     * Return the type of the escher record (normally in the 0xFnnn range)
     */
    public long getRecordType()
    {
        return _type;
    }

    /**
     * @return  Shape ID
     */
    public int getShapeId()
    {
        return shapeId;
    }

    /**
     *  @param id Shape ID
     */
    public void setShapeId(int id)
    {
        shapeId = id;
    }
    
    public void dispose()
    {
        super.dispose();
        if (_escherRecord != null)
        {
            _escherRecord.dispose();
            _escherRecord = null;
        }
    }
}
