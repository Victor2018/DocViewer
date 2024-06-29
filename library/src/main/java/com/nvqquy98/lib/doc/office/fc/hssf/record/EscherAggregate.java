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

package com.nvqquy98.lib.doc.office.fc.hssf.record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nvqquy98.lib.doc.office.fc.ddf.DefaultEscherRecordFactory;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherBoolProperty;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherClientAnchorRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherClientDataRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherContainerRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherDgRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherDggRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherOptRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherProperties;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherRecordFactory;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherSerializationListener;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherSpRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherSpgrRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherTextboxRecord;
import com.nvqquy98.lib.doc.office.fc.hssf.model.AbstractShape;
import com.nvqquy98.lib.doc.office.fc.hssf.model.CommentShape;
import com.nvqquy98.lib.doc.office.fc.hssf.model.ConvertAnchor;
import com.nvqquy98.lib.doc.office.fc.hssf.model.DrawingManager2;
import com.nvqquy98.lib.doc.office.fc.hssf.model.TextboxShape;
import com.nvqquy98.lib.doc.office.fc.hssf.usermodel.HSSFChart;
import com.nvqquy98.lib.doc.office.fc.hssf.usermodel.HSSFClientAnchor;
import com.nvqquy98.lib.doc.office.fc.hssf.usermodel.HSSFPatriarch;
import com.nvqquy98.lib.doc.office.fc.hssf.usermodel.HSSFShape;
import com.nvqquy98.lib.doc.office.fc.hssf.usermodel.HSSFShapeContainer;
import com.nvqquy98.lib.doc.office.fc.hssf.usermodel.HSSFShapeFactory;
import com.nvqquy98.lib.doc.office.fc.hssf.usermodel.HSSFShapeGroup;
import com.nvqquy98.lib.doc.office.fc.hssf.usermodel.HSSFTextbox;
import com.nvqquy98.lib.doc.office.fc.util.POILogFactory;
import com.nvqquy98.lib.doc.office.fc.util.POILogger;
import com.nvqquy98.lib.doc.office.ss.model.XLSModel.AWorkbook;

/**
 * This class is used to aggregate the MSODRAWING and OBJ record
 * combinations.  This is necessary due to the bizare way in which
 * these records are serialized.  What happens is that you get a
 * combination of MSODRAWING -> OBJ -> MSODRAWING -> OBJ records
 * but the escher records are serialized _across_ the MSODRAWING
 * records.
 * <p>
 * It gets even worse when you start looking at TXO records.
 * <p>
 * So what we do with this class is aggregate lazily.  That is
 * we don't aggregate the MSODRAWING -> OBJ records unless we
 * need to modify them.
 *
 *
 * @author Glen Stampoultzis (glens at apache.org)
 */
public final class EscherAggregate extends AbstractEscherHolderRecord {
	public static final short sid = 9876; // not a real sid - dummy value
	private static POILogger log = POILogFactory.getLogger(EscherAggregate.class);

	protected HSSFPatriarch patriarch;   
    
	/** Maps shape container objects to their {@link TextObjectRecord} or {@link ObjRecord} */
	private Map<EscherRecord, Record> shapeToObj = new HashMap<EscherRecord, Record>();
	//chart records
	 private Map<EscherRecord, List<Record>> chartToObj = new HashMap<EscherRecord, List<Record>>();
	 
	private DrawingManager2 drawingManager;
	private short drawingGroupId;

	/**
	 * list of "tail" records that need to be serialized after all drawing group records
	 */
	private List tailRec = new ArrayList();

	public EscherAggregate( DrawingManager2 drawingManager )
	{
		this.drawingManager = drawingManager;
	}

	/**
	 * @return  Returns the current sid.
	 */
	public short getSid()
	{
		return sid;
	}

	/**
	 * Calculates the string representation of this record.  This is
	 * simply a dump of all the records.
	 */
	public String toString()
	{
		String nl = System.getProperty( "line.separtor" );

		StringBuffer result = new StringBuffer();
		result.append( '[' ).append( getRecordName() ).append( ']' + nl );
		for ( Iterator iterator = getEscherRecords().iterator(); iterator.hasNext(); )
		{
			EscherRecord escherRecord = (EscherRecord) iterator.next();
			result.append( escherRecord.toString() );
		}
		result.append( "[/" ).append( getRecordName() ).append( ']' + nl );

		return result.toString();
	}

	/**
	 * 
	 * @param records
	 * @param currentDrawingRecord
	 * @return
	 */
	public static int shapeContainRecords(List records, int currentDrawingRecord)
	{
	    int count = 0;
	    if((sid( records, currentDrawingRecord ) == DrawingRecord.sid || sid( records, currentDrawingRecord ) == ContinueRecord.sid)
	        && isObjectRecord( records, currentDrawingRecord + 1 ))
	    {
	        Record record = (Record) records.get( currentDrawingRecord + 1 );            
	        count = 2;
	        //chart records
            if(record instanceof ObjRecord && ((ObjRecord)record).getSubRecords().get(0) instanceof CommonObjectDataSubRecord)
            {
                CommonObjectDataSubRecord commonObjectDataSubRecord = (CommonObjectDataSubRecord)((ObjRecord)record).getSubRecords().get(0);
                if(commonObjectDataSubRecord.getObjectType() == CommonObjectDataSubRecord.OBJECT_TYPE_CHART)
                {
                  //chart 
                    List<Record> chartRecordsList = new ArrayList<Record>();
                    currentDrawingRecord += 2;     
                    record = (Record) records.get( currentDrawingRecord );
                    while(record.getSid() != EOFRecord.sid)
                    {
                        chartRecordsList.add(record); 
                        
                        currentDrawingRecord ++;
                        count++;
                        record = (Record) records.get( currentDrawingRecord );
                    }
                    return count + 1;
                }
            }
            
            //not chart
            //note record
            if (records.get(currentDrawingRecord + 2) instanceof NoteRecord)
                count++;
	    }
	    return count;
	}
	/**
	 * 
	 * @param records
	 * @param currentIndex
	 * @return
	 */
    public static int nextDrawingRecord(List records, int currentIndex)
    { // TODO - remove this method
        int max = records.size();
        for (int i = currentIndex + 1; i < max; i++)
        {
            Object rb = records.get(i);
            if (!(rb instanceof Record))
            {
                continue;
            }
            Record record = (Record)rb;
            if (record.getSid() == DrawingRecord.sid || record.getSid() == ContinueRecord.sid)
            {
                return i;
            }
        }
        return -1;
    }
    
	/**
	 * Collapses the drawing records into an aggregate.
	 */
	public static EscherAggregate createAggregate( List records, int locFirstDrawingRecord, DrawingManager2 drawingManager )
	{
		// Keep track of any shape records created so we can match them back to the object id's.
		// Textbox objects are also treated as shape objects.
		final List<EscherRecord> shapeRecords = new ArrayList<EscherRecord>();
		EscherRecordFactory recordFactory = new DefaultEscherRecordFactory()
		{
			public EscherRecord createRecord( byte[] data, int offset )
			{
				EscherRecord r = super.createRecord( data, offset );
				if ( r.getRecordId() == EscherClientDataRecord.RECORD_ID || r.getRecordId() == EscherTextboxRecord.RECORD_ID )
				{
					shapeRecords.add( r );
				}
				return r;
			}
		};

		// Calculate the size of the buffer
		EscherAggregate agg = new EscherAggregate(drawingManager);
		int loc = locFirstDrawingRecord;
		int dataSize = 0;
		while ( loc > -1 && loc + 1 < records.size()
				&& (sid( records, loc ) == DrawingRecord.sid || sid( records, loc ) == ContinueRecord.sid))
		{	
		    if(isObjectRecord( records, loc + 1 ))
		    {
		        if(sid( records, loc ) == ContinueRecord.sid )
		        {
		            dataSize += ((ContinueRecord)records.get(loc)).getDataSize();
		        }
		        else
		        {
		            dataSize += ((DrawingRecord)records.get(loc)).getData().length;
		        }
		    }			
			loc = nextDrawingRecord(records, loc);
		}		
		// Create one big buffer
		byte buffer[] = new byte[dataSize];
		int offset = 0;
		byte[] data;
		loc = locFirstDrawingRecord;
		while ( loc > -1 && loc + 1 < records.size()
				&& (sid( records, loc ) == DrawingRecord.sid || sid( records, loc ) == ContinueRecord.sid))
		{
		    if(isObjectRecord( records, loc + 1 ))
            {
		        if(sid( records, loc ) == ContinueRecord.sid )
                {
		            ContinueRecord contd = (ContinueRecord) records.get( loc );
		            data = contd.getData();
                }
                else
                {
                    DrawingRecord drawingRecord = (DrawingRecord) records.get( loc );
                    data = drawingRecord.getData();
                }
		        
		        if(data != null)
		        {
		        	System.arraycopy( data, 0, buffer, offset, data.length );
			        offset += data.length;
		        }		        
            }   
			
			loc = nextDrawingRecord(records, loc);
		}

		// Decode the shapes
		//		agg.escherRecords = new ArrayList();
		int pos = 0;
		while ( pos < dataSize )
		{
		    try
		    {
		        EscherRecord r = recordFactory.createRecord( buffer, pos );
	            int bytesRead = r.fillFields( buffer, pos, recordFactory );
	            agg.addEscherRecord( r );
	            pos += bytesRead;
		    }
			catch(Exception e)
			{
			    break;
			}
		}
				
		
		// Associate the object records with the shapes
		loc = locFirstDrawingRecord;
		int shapeIndex = 0;
		agg.shapeToObj = new HashMap<EscherRecord, Record>();
		while ( loc > -1 && loc + 1 < records.size()
				&& (sid( records, loc ) == DrawingRecord.sid || sid( records, loc ) == ContinueRecord.sid))
		{
		    if(!isObjectRecord( records, loc + 1 ))
            {
		        loc = nextDrawingRecord(records, loc);
		        continue;
            }
		    
		    Record record = (Record) records.get( loc + 1 );
		    try
		    {
		      //chart records
	            if(record instanceof ObjRecord && ((ObjRecord)record).getSubRecords().get(0) instanceof CommonObjectDataSubRecord)
	            {
	                CommonObjectDataSubRecord commonObjectDataSubRecord = (CommonObjectDataSubRecord)((ObjRecord)record).getSubRecords().get(0);
	                if(commonObjectDataSubRecord.getObjectType() == CommonObjectDataSubRecord.OBJECT_TYPE_CHART)
	                {
	                    //chart 
	                    List<Record> chartRecordsList = new ArrayList<Record>();
	                    loc += 2;     
	                    record = (Record) records.get( loc );
	                    while(record.getSid() != EOFRecord.sid)
	                    {
	                        chartRecordsList.add(record); 
	                        
	                        loc ++;
	                        record = (Record) records.get( loc );
	                    }
	                    agg.chartToObj.put(shapeRecords.get( shapeIndex++ ), chartRecordsList );

	                    loc++;
	                }
	                else 
	                {
	                    agg.shapeToObj.put( shapeRecords.get( shapeIndex++ ), record );                    
	                    loc += 2;
	                }
	                
	            }
	            else
	            {
	                agg.shapeToObj.put( shapeRecords.get( shapeIndex++ ), record );
	                loc += 2;
	            }
		    }
		    catch(Exception e)
		    {
		        break;
		    }
		}

		return agg;

	}	
	
	/**
	 * Serializes this aggregate to a byte array.  Since this is an aggregate
	 * record it will effectively serialize the aggregated records.
	 *
	 * @param offset	The offset into the start of the array.
	 * @param data	  The byte array to serialize to.
	 * @return		  The number of bytes serialized.
	 */
	public int serialize( int offset, byte[] data )
	{
		convertUserModelToRecords();

		// Determine buffer size
		List records = getEscherRecords();
		int size = getEscherRecordSize( records );
		byte[] buffer = new byte[size];


		// Serialize escher records into one big data structure and keep note of ending offsets.
		final List spEndingOffsets = new ArrayList();
		final List shapes = new ArrayList();
		int pos = 0;
		for ( Iterator iterator = records.iterator(); iterator.hasNext(); )
		{
			EscherRecord e = (EscherRecord) iterator.next();
			pos += e.serialize( pos, buffer, new EscherSerializationListener()
			{
				public void beforeRecordSerialize( int offset, short recordId, EscherRecord record )
				{
				}

				public void afterRecordSerialize( int offset, short recordId, int size, EscherRecord record )
				{
					if ( recordId == EscherClientDataRecord.RECORD_ID || recordId == EscherTextboxRecord.RECORD_ID )
					{
						spEndingOffsets.add( Integer.valueOf( offset ) );
						shapes.add( record );
					}
				}
			} );
		}
		// todo: fix this
		shapes.add( 0, null );
		spEndingOffsets.add( 0, null );

		// Split escher records into separate MSODRAWING and OBJ, TXO records.  (We don't break on
		// the first one because it's the patriach).
		pos = offset;
		for ( int i = 1; i < shapes.size(); i++ )
		{
			int endOffset = ( (Integer) spEndingOffsets.get( i ) ).intValue() - 1;
			int startOffset;
			if ( i == 1 )
				startOffset = 0;
			else
				startOffset = ( (Integer) spEndingOffsets.get( i - 1 ) ).intValue();

			// Create and write a new MSODRAWING record
			DrawingRecord drawing = new DrawingRecord();
			byte[] drawingData = new byte[endOffset - startOffset + 1];
			System.arraycopy( buffer, startOffset, drawingData, 0, drawingData.length );
			drawing.setData( drawingData );
			int temp = drawing.serialize( pos, data );
			pos += temp;

			// Write the matching OBJ record
			Record obj = shapeToObj.get( shapes.get( i ) );
			temp = obj.serialize( pos, data );
			pos += temp;

		}

		// write records that need to be serialized after all drawing group records
		for ( int i = 0; i < tailRec.size(); i++ )
		{
			Record rec = (Record)tailRec.get(i);
			pos += rec.serialize( pos, data );
		}

		int bytesWritten = pos - offset;
		if ( bytesWritten != getRecordSize() )
			throw new RecordFormatException( bytesWritten + " bytes written but getRecordSize() reports " + getRecordSize() );
		return bytesWritten;
	}

	/**
	 * How many bytes do the raw escher records contain.
	 * @param records   List of escher records
	 * @return  the number of bytes
	 */
	private int getEscherRecordSize( List records )
	{
		int size = 0;
		for ( Iterator iterator = records.iterator(); iterator.hasNext(); )
			size += ( (EscherRecord) iterator.next() ).getRecordSize();
		return size;
	}

	public int getRecordSize() {
		// TODO - convert this to RecordAggregate
		convertUserModelToRecords();
		List records = getEscherRecords();
		int rawEscherSize = getEscherRecordSize( records );
		int drawingRecordSize = rawEscherSize + ( shapeToObj.size() ) * 4;
		int objRecordSize = 0;
		for ( Iterator iterator = shapeToObj.values().iterator(); iterator.hasNext(); )
		{
			Record r = (Record) iterator.next();
			objRecordSize += r.getRecordSize();
		}
		int tailRecordSize = 0;
		for ( Iterator iterator = tailRec.iterator(); iterator.hasNext(); )
		{
			Record r = (Record) iterator.next();
			tailRecordSize += r.getRecordSize();
		}
		return drawingRecordSize + objRecordSize + tailRecordSize;
	}

	/**
	 * Associates an escher record to an OBJ record or a TXO record.
	 */
	Object associateShapeToObjRecord( EscherRecord r, ObjRecord objRecord )
	{
		return shapeToObj.put( r, objRecord );
	}

	public HSSFPatriarch getPatriarch()
	{
		return patriarch;
	}

	public void setPatriarch( HSSFPatriarch patriarch )
	{
		this.patriarch = patriarch;
	}
	
	/**
	 * Converts the Records into UserModel
	 *  objects on the bound HSSFPatriarch
	 */
	public void convertRecordsToUserModel(AWorkbook workbook)
	{
		if(patriarch == null)
		{
			throw new IllegalStateException("Must call setPatriarch() first");
		}

		// The top level container ought to have
		//  the DgRecord and the container of one container
		//  per shape group (patriach overall first)
//		EscherContainerRecord topContainer = getEscherContainer();
//		if(topContainer == null) 
//		{
//			return;
//		}
		List<EscherContainerRecord> containerList = getgetEscherContainers();
		if(containerList.size() == 0)
		{
		    return;
		}
		
		HSSFShape shape = null;
		int index = 0;
		if(containerList.get(index).getChildContainers().size() > 0)
		{
		    EscherContainerRecord topContainer = containerList.get(index++).getChildContainers().get(0);

	        List<EscherContainerRecord> tcc = topContainer.getChildContainers();
	        if(tcc.size() == 0) 
	        {
	            throw new IllegalStateException("No child escher containers at the point that should hold the patriach data, and one container per top level shape!");
	        }

	        // First up, get the patriach position
	        // This is in the first EscherSpgrRecord, in
	        //  the first container, with a EscherSRecord too
	        EscherContainerRecord patriachContainer = (EscherContainerRecord)tcc.get(0);
	        EscherSpgrRecord spgr = null;
	        for(Iterator<EscherRecord> it = patriachContainer.getChildIterator(); it.hasNext();) 
	        {
	            EscherRecord r = it.next();
	            if(r instanceof EscherSpgrRecord)
	            {
	                spgr = (EscherSpgrRecord)r;
	                break;
	            }
	        }
	        if(spgr != null)
	        {
	            patriarch.setCoordinates(
	                    spgr.getRectX1(), spgr.getRectY1(),
	                    spgr.getRectX2(), spgr.getRectY2()
	            );
	        }

	        // Now process the containers for each group
	        //  and objects
	        for(int i=1; i<tcc.size(); i++) 
	        {
	            EscherContainerRecord shapeContainer =
	                (EscherContainerRecord)tcc.get(i);
	            
	            shape = HSSFShapeFactory.createShape(workbook, shapeToObj, shapeContainer, null);
	            if (shape != null)
	            {
	                convertRecordsToUserModel(shapeContainer, shape);
	                patriarch.addShape(shape);
	            }
	        }
		}
		

		for(; index < containerList.size(); index++)
		{
		    EscherContainerRecord shapeContainer = containerList.get(index);
            
            shape = HSSFShapeFactory.createShape(workbook, shapeToObj, shapeContainer, null);
            if (shape != null)
            {
                convertRecordsToUserModel(shapeContainer, shape);
                patriarch.addShape(shape);
            }
		}
		
		// Now, clear any trace of what records make up
		//  the patriarch
		// Otherwise, everything will go horribly wrong
		//  when we try to write out again....
		// clearEscherRecords();
		drawingManager.getDgg().setFileIdClusters(new EscherDggRecord.FileIdCluster[0]);
	}
    
	private void convertRecordsToUserModel(EscherContainerRecord shapeContainer, Object model) 
	{
		for(Iterator<EscherRecord> it = shapeContainer.getChildIterator(); it.hasNext();) 
		{
			EscherRecord r = it.next();
			if(r instanceof EscherSpgrRecord) 
			{
				// This may be overriden by a later EscherClientAnchorRecord
				EscherSpgrRecord spgr = (EscherSpgrRecord)r;

				if(model instanceof HSSFShapeGroup) 
				{
					HSSFShapeGroup group = (HSSFShapeGroup)model;
					group.setCoordinates(spgr.getRectX1(), spgr.getRectY1(), spgr.getRectX2(), spgr.getRectY2());
				} 
				else 
				{
					throw new IllegalStateException("Got top level anchor but not processing a group");
				}
			}
			else if(r instanceof EscherClientAnchorRecord) 
			{
				/*EscherClientAnchorRecord car = (EscherClientAnchorRecord)r;

				if(model instanceof HSSFShape) {
					HSSFShape g = (HSSFShape)model;
					HSSFAnchor anchor = g.getAnchor();
					
					g.getAnchor().setDx1(car.getDx1());
					g.getAnchor().setDx2(car.getDx2());
					g.getAnchor().setDy1(car.getDy1());
					g.getAnchor().setDy2(car.getDy2());
				} else {
					throw new IllegalStateException("Got top level anchor but not processing a group or shape");
				}*/
			}
			else if(r instanceof EscherTextboxRecord) 
			{
				EscherTextboxRecord tbr = (EscherTextboxRecord)r;
				Record obj = shapeToObj.get(tbr);
				if(obj instanceof TextObjectRecord && model instanceof HSSFTextbox)
				{
				    TextObjectRecord textObj = (TextObjectRecord)obj;
				    HSSFTextbox textbox = (HSSFTextbox) model;
			        if(!textbox.isWordArt())
				    {
				    	textbox.setString(textObj.getStr());
				    }
				    
				    textbox.setHorizontalAlignment((short)textObj.getHorizontalTextAlignment());
				    textbox.setVerticalAlignment((short)textObj.getVerticalTextAlignment());
				}
			}
			else if(r instanceof EscherClientDataRecord && model instanceof HSSFChart) 
            {
			    EscherClientDataRecord cdr = (EscherClientDataRecord)r;
                List<Record> recordsList = chartToObj.get(cdr);
                HSSFChart.convertRecordsToChart(recordsList, (HSSFChart) model);
                
            }
			else if(r instanceof EscherSpRecord) 
			{
				// Use flags if needed
			}
			else if(r instanceof EscherOptRecord) 
			{
				// Use properties if needed
			}
			else 
			{
				//System.err.println(r);
			}
		}
	}
	
	public void clear()
	{
		clearEscherRecords();
		shapeToObj.clear();
		chartToObj.clear();
//		lastShapeId = 1024;
	}

	protected String getRecordName()
	{
		return "ESCHERAGGREGATE";
	}

	// =============== Private methods ========================

	private static boolean isObjectRecord( List records, int loc )
	{
		return sid( records, loc ) == ObjRecord.sid || sid( records, loc ) == TextObjectRecord.sid;
	}

	private void convertUserModelToRecords()
	{
		if ( patriarch != null )
		{
			shapeToObj.clear();
			tailRec.clear();
			chartToObj.clear();
			
			clearEscherRecords();
			if ( patriarch.getChildren().size() != 0 )
			{
				convertPatriarch( patriarch );
				EscherContainerRecord dgContainer = (EscherContainerRecord) getEscherRecord( 0 );
				EscherContainerRecord spgrContainer = null;
				Iterator<EscherRecord> iter = dgContainer.getChildIterator();
				while (iter.hasNext()) {
					EscherRecord child = iter.next();
					if (child.getRecordId() == EscherContainerRecord.SPGR_CONTAINER) {
						spgrContainer = (EscherContainerRecord) child;
					}
				}
				convertShapes( patriarch, spgrContainer, shapeToObj );

				patriarch = null;
			}
		}
	}

	private void convertShapes( HSSFShapeContainer parent, EscherContainerRecord escherParent, Map shapeToObj )
	{
		if ( escherParent == null ) throw new IllegalArgumentException( "Parent record required" );

		List shapes = parent.getChildren();
		for ( Iterator iterator = shapes.iterator(); iterator.hasNext(); )
		{
			HSSFShape shape = (HSSFShape) iterator.next();
			if ( shape instanceof HSSFShapeGroup )
			{
				convertGroup( (HSSFShapeGroup) shape, escherParent, shapeToObj );
			}
			else
			{
				AbstractShape shapeModel = AbstractShape.createShape(
						shape,
						drawingManager.allocateShapeId(drawingGroupId) );
				shapeToObj.put( findClientData( shapeModel.getSpContainer() ), shapeModel.getObjRecord() );
				if ( shapeModel instanceof TextboxShape )
				{
					EscherRecord escherTextbox = ( (TextboxShape) shapeModel ).getEscherTextbox();
					shapeToObj.put( escherTextbox, ( (TextboxShape) shapeModel ).getTextObjectRecord() );
					//					escherParent.addChildRecord(escherTextbox);

					if ( shapeModel instanceof CommentShape ){
						CommentShape comment = (CommentShape)shapeModel;
						tailRec.add(comment.getNoteRecord());
					}

				}
				escherParent.addChildRecord( shapeModel.getSpContainer() );
			}
		}
//		drawingManager.newCluster( (short)1 );
//		drawingManager.newCluster( (short)2 );

	}

	private void convertGroup( HSSFShapeGroup shape, EscherContainerRecord escherParent, Map shapeToObj )
	{
		EscherContainerRecord spgrContainer = new EscherContainerRecord();
		EscherContainerRecord spContainer = new EscherContainerRecord();
		EscherSpgrRecord spgr = new EscherSpgrRecord();
		EscherSpRecord sp = new EscherSpRecord();
		EscherOptRecord opt = new EscherOptRecord();
		EscherRecord anchor;
		EscherClientDataRecord clientData = new EscherClientDataRecord();

		spgrContainer.setRecordId( EscherContainerRecord.SPGR_CONTAINER );
		spgrContainer.setOptions( (short) 0x000F );
		spContainer.setRecordId( EscherContainerRecord.SP_CONTAINER );
		spContainer.setOptions( (short) 0x000F );
		spgr.setRecordId( EscherSpgrRecord.RECORD_ID );
		spgr.setOptions( (short) 0x0001 );
		spgr.setRectX1( shape.getX1() );
		spgr.setRectY1( shape.getY1() );
		spgr.setRectX2( shape.getX2() );
		spgr.setRectY2( shape.getY2() );
		sp.setRecordId( EscherSpRecord.RECORD_ID );
		sp.setOptions( (short) 0x0002 );
		int shapeId = drawingManager.allocateShapeId(drawingGroupId);
		sp.setShapeId( shapeId );
		if (shape.getAnchor() instanceof HSSFClientAnchor)
			sp.setFlags( EscherSpRecord.FLAG_GROUP | EscherSpRecord.FLAG_HAVEANCHOR );
		else
			sp.setFlags( EscherSpRecord.FLAG_GROUP | EscherSpRecord.FLAG_HAVEANCHOR | EscherSpRecord.FLAG_CHILD );
		opt.setRecordId( EscherOptRecord.RECORD_ID );
		opt.setOptions( (short) 0x0023 );
		opt.addEscherProperty( new EscherBoolProperty( EscherProperties.PROTECTION__LOCKAGAINSTGROUPING, 0x00040004 ) );
		opt.addEscherProperty( new EscherBoolProperty( EscherProperties.GROUPSHAPE__PRINT, 0x00080000 ) );

		anchor = ConvertAnchor.createAnchor( shape.getAnchor() );
//		clientAnchor.setCol1( ( (HSSFClientAnchor) shape.getAnchor() ).getCol1() );
//		clientAnchor.setRow1( (short) ( (HSSFClientAnchor) shape.getAnchor() ).getRow1() );
//		clientAnchor.setDx1( (short) shape.getAnchor().getDx1() );
//		clientAnchor.setDy1( (short) shape.getAnchor().getDy1() );
//		clientAnchor.setCol2( ( (HSSFClientAnchor) shape.getAnchor() ).getCol2() );
//		clientAnchor.setRow2( (short) ( (HSSFClientAnchor) shape.getAnchor() ).getRow2() );
//		clientAnchor.setDx2( (short) shape.getAnchor().getDx2() );
//		clientAnchor.setDy2( (short) shape.getAnchor().getDy2() );
		clientData.setRecordId( EscherClientDataRecord.RECORD_ID );
		clientData.setOptions( (short) 0x0000 );

		spgrContainer.addChildRecord( spContainer );
		spContainer.addChildRecord( spgr );
		spContainer.addChildRecord( sp );
		spContainer.addChildRecord( opt );
		spContainer.addChildRecord( anchor );
		spContainer.addChildRecord( clientData );

		ObjRecord obj = new ObjRecord();
		CommonObjectDataSubRecord cmo = new CommonObjectDataSubRecord();
		cmo.setObjectType( CommonObjectDataSubRecord.OBJECT_TYPE_GROUP );
		cmo.setObjectId( shapeId );
		cmo.setLocked( true );
		cmo.setPrintable( true );
		cmo.setAutofill( true );
		cmo.setAutoline( true );
		GroupMarkerSubRecord gmo = new GroupMarkerSubRecord();
		EndSubRecord end = new EndSubRecord();
		obj.addSubRecord( cmo );
		obj.addSubRecord( gmo );
		obj.addSubRecord( end );
		shapeToObj.put( clientData, obj );

		escherParent.addChildRecord( spgrContainer );

		convertShapes( shape, spgrContainer, shapeToObj );

	}

	private EscherRecord findClientData( EscherContainerRecord spContainer )
	{
		for (Iterator<EscherRecord> iterator = spContainer.getChildIterator(); iterator.hasNext();) {
			EscherRecord r = iterator.next();
			if (r.getRecordId() == EscherClientDataRecord.RECORD_ID) {
				return r;
			}
		}
		throw new IllegalArgumentException( "Can not find client data record" );
	}

	private void convertPatriarch( HSSFPatriarch patriarch )
	{
		EscherContainerRecord dgContainer = new EscherContainerRecord();
		EscherDgRecord dg;
		EscherContainerRecord spgrContainer = new EscherContainerRecord();
		EscherContainerRecord spContainer1 = new EscherContainerRecord();
		EscherSpgrRecord spgr = new EscherSpgrRecord();
		EscherSpRecord sp1 = new EscherSpRecord();

		dgContainer.setRecordId( EscherContainerRecord.DG_CONTAINER );
		dgContainer.setOptions( (short) 0x000F );
		dg = drawingManager.createDgRecord();
		drawingGroupId = dg.getDrawingGroupId();
//		dg.setOptions( (short) ( drawingId << 4 ) );
//		dg.setNumShapes( getNumberOfShapes( patriarch ) );
//		dg.setLastMSOSPID( 0 );  // populated after all shape id's are assigned.
		spgrContainer.setRecordId( EscherContainerRecord.SPGR_CONTAINER );
		spgrContainer.setOptions( (short) 0x000F );
		spContainer1.setRecordId( EscherContainerRecord.SP_CONTAINER );
		spContainer1.setOptions( (short) 0x000F );
		spgr.setRecordId( EscherSpgrRecord.RECORD_ID );
		spgr.setOptions( (short) 0x0001 );	// version
		spgr.setRectX1( patriarch.getX1() );
		spgr.setRectY1( patriarch.getY1() );
		spgr.setRectX2( patriarch.getX2() );
		spgr.setRectY2( patriarch.getY2() );
		sp1.setRecordId( EscherSpRecord.RECORD_ID );
		sp1.setOptions( (short) 0x0002 );
		sp1.setShapeId( drawingManager.allocateShapeId(dg.getDrawingGroupId()) );
		sp1.setFlags( EscherSpRecord.FLAG_GROUP | EscherSpRecord.FLAG_PATRIARCH );

		dgContainer.addChildRecord( dg );
		dgContainer.addChildRecord( spgrContainer );
		spgrContainer.addChildRecord( spContainer1 );
		spContainer1.addChildRecord( spgr );
		spContainer1.addChildRecord( sp1 );

		addEscherRecord( dgContainer );
	}


	private static short sid( List records, int loc )
	{
		return ( (Record) records.get( loc ) ).getSid();
	}
}
