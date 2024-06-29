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

package com.nvqquy98.lib.doc.office.fc.hslf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import com.nvqquy98.lib.doc.office.constant.EventConstant;
import com.nvqquy98.lib.doc.office.fc.fs.filesystem.CFBFileSystem;
import com.nvqquy98.lib.doc.office.fc.fs.filesystem.Property;
import com.nvqquy98.lib.doc.office.fc.hslf.blip.Metafile;
import com.nvqquy98.lib.doc.office.fc.hslf.exceptions.CorruptPowerPointFileException;
import com.nvqquy98.lib.doc.office.fc.hslf.exceptions.EncryptedPowerPointFileException;
import com.nvqquy98.lib.doc.office.fc.hslf.model.Picture;
import com.nvqquy98.lib.doc.office.fc.hslf.record.CurrentUserAtom;
import com.nvqquy98.lib.doc.office.fc.hslf.record.ExOleObjStg;
import com.nvqquy98.lib.doc.office.fc.hslf.record.PersistPtrHolder;
import com.nvqquy98.lib.doc.office.fc.hslf.record.PersistRecord;
import com.nvqquy98.lib.doc.office.fc.hslf.record.Record;
import com.nvqquy98.lib.doc.office.fc.hslf.record.UserEditAtom;
import com.nvqquy98.lib.doc.office.fc.hslf.usermodel.ObjectData;
import com.nvqquy98.lib.doc.office.fc.hslf.usermodel.PictureData;
import com.nvqquy98.lib.doc.office.fc.util.LittleEndian;
import com.nvqquy98.lib.doc.office.system.IControl;


/**
 * This class contains the main functionality for the Powerpoint file
 * "reader". It is only a very basic class for now
 *
 * @author Nick Burch
 */
public final class HSLFSlideShow/* extends POIDocument*/
{
    protected static final int CHECKSUM_SIZE = 16;
    // For logging
    //private POILogger logger = POILogFactory.getLogger(this.getClass());

    // Holds metadata on where things are in our document
    private CurrentUserAtom currentUser;

    // Low level contents of the file
    private byte[] _docstream;
    private Property _docProp;
    
    // Low level contents
    private Record[] _records;

    // Raw Pictures contained in the pictures stream
    private List<PictureData> _pictures;

    // Embedded objects stored in storage records in the document stream, lazily populated.
    private ObjectData[] _objects;
    //
    private CFBFileSystem cfbFS;
    //
    private IControl control;

    /**
     * Returns the directory in the underlying POIFSFileSystem for the 
     *  document that is open.
     */
    /*protected DirectoryNode getPOIFSDirectory()
    {
        return directory;
    }*/

    public HSLFSlideShow(IControl control, InputStream fis) throws IOException
    {
        this.control = control;
        cfbFS = new CFBFileSystem(fis);

        // First up, grab the "Current User" stream
        // We need this before we can detect Encrypted Documents
        readCurrentUserStream();

        // Next up, grab the data that makes up the
        //  PowerPoint stream
        readPowerPointStream();

        // Check to see if we have an encrypted document,
        //  bailing out if we do
        boolean encrypted = cfbFS.getPropertyRawData("EncryptedSummary") != null;
        if (encrypted)
        {
            throw new EncryptedPowerPointFileException(
                    "Cannot process encrypted office files!");
        }

        // Now, build records based on the PowerPoint stream
        buildRecords();

        // Look for any other streams
        readOtherStreams();
    }

    /**
     * Constructs a Powerpoint document from fileName. Parses the document
     * and places all the important stuff into data structures.
     *
     * @param fileName The name of the file to read.
     * @throws IOException if there is a problem while parsing the document.
     */
    public HSLFSlideShow(IControl control, String fileName) throws IOException
    {
        this.control = control;
        cfbFS = new CFBFileSystem(new FileInputStream(fileName));

        // First up, grab the "Current User" stream
        // We need this before we can detect Encrypted Documents
        readCurrentUserStream();

        // Next up, grab the data that makes up the
        //  PowerPoint stream
        readPowerPointStream();

        // Check to see if we have an encrypted document,
        //  bailing out if we do
        boolean encrypted = cfbFS.getPropertyRawData("EncryptedSummary") != null;
        if (encrypted)
        {
            throw new EncryptedPowerPointFileException(
                "Cannot process encrypted office files!");
        }

        // Now, build records based on the PowerPoint stream
        buildRecords();

        // Look for any other streams
        readOtherStreams();
    }

    /**
     * Constructs a Powerpoint document from a specific point in a
     *  POIFS Filesystem. Parses the document and places all the
     *  important stuff into data structures.
     *
     * @param dir the POIFS directory to read from
     * @throws IOException if there is a problem while parsing the document.
     */
    /*public HSLFSlideShow(DirectoryNode dir) throws IOException
    {
        super(dir);

        // First up, grab the "Current User" stream
        // We need this before we can detect Encrypted Documents
        readCurrentUserStream();

        // Next up, grab the data that makes up the
        //  PowerPoint stream
        readPowerPointStream();

        // Check to see if we have an encrypted document,
        //  bailing out if we do
        boolean encrypted = EncryptedSlideShow.checkIfEncrypted(this);
        if (encrypted)
        {
            throw new EncryptedPowerPointFileException(
                "Encrypted PowerPoint files are not supported");
        }

        // Now, build records based on the PowerPoint stream
        buildRecords();

        // Look for any other streams
        readOtherStreams();
    }*/

    /**
     * Extracts the main PowerPoint document stream from the
     *  POI file, ready to be passed
     *
     * @throws IOException
     */
    private void readPowerPointStream() throws IOException
    {
        // Get the main document stream
        //DocumentEntry docProps = (DocumentEntry)directory.getEntry("PowerPoint Document");

        // Grab the document stream
        _docstream = cfbFS.getPropertyRawData("PowerPoint Document");//new byte[docProps.getSize()];
        //directory.createDocumentInputStream("PowerPoint Document").read(_docstream);
        
        _docProp = cfbFS.getProperty("PowerPoint Document");
    }

    /**
     * Builds the list of records, based on the contents
     *  of the PowerPoint stream
     */
    private void buildRecords()
    {
        // The format of records in a powerpoint file are:
        //   <little endian 2 byte "info">
        //   <little endian 2 byte "type">
        //   <little endian 4 byte "length">
        // If it has a zero length, following it will be another record
        //		<xx xx yy yy 00 00 00 00> <xx xx yy yy zz zz zz zz>
        // If it has a length, depending on its type it may have children or data
        // If it has children, these will follow straight away
        //		<xx xx yy yy zz zz zz zz <xx xx yy yy zz zz zz zz>>
        // If it has data, this will come straigh after, and run for the length
        //      <xx xx yy yy zz zz zz zz dd dd dd dd dd dd dd>
        // All lengths given exclude the 8 byte record header
        // (Data records are known as Atoms)

        // Document should start with:
        //   0F 00 E8 03 ## ## ## ##
        //     (type 1000 = document, info 00 0f is normal, rest is document length)
        //   01 00 E9 03 28 00 00 00
        //     (type 1001 = document atom, info 00 01 normal, 28 bytes long)
        //   80 16 00 00 E0 10 00 00 xx xx xx xx xx xx xx xx
        //   05 00 00 00 0A 00 00 00 xx xx xx
        //     (the contents of the document atom, not sure what it means yet)
        //   (records then follow)

        // When parsing a document, look to see if you know about that type
        //  of the current record. If you know it's a type that has children,
        //  process the record's data area looking for more records
        // If you know about the type and it doesn't have children, either do
        //  something with the data (eg TextRun) or skip over it
        // If you don't know about the type, play safe and skip over it (using
        //  its length to know where the next record will start)
        //

        _records = read(/*_docstream,*/ (int)currentUser.getCurrentEditOffset());
    }
    
    private Record[] read(/*byte[] docstream, */int usrOffset)
    {
        ArrayList<Integer> lst = new ArrayList<Integer>();
        HashMap<Integer, Integer> offset2id = new HashMap<Integer, Integer>();
        while (usrOffset != 0)
        {
//            UserEditAtom usr = (UserEditAtom)Record.buildRecordAtOffset(docstream, usrOffset);
            byte[] data = _docProp.getRecordData(usrOffset);
            UserEditAtom usr = (UserEditAtom)Record.buildRecordAtOffset(data, 0, usrOffset);
            
            lst.add(Integer.valueOf(usrOffset));
            int psrOffset = usr.getPersistPointersOffset();

//            PersistPtrHolder ptr = (PersistPtrHolder)Record.buildRecordAtOffset(docstream,
//                psrOffset);
            
            data = _docProp.getRecordData(psrOffset);
            PersistPtrHolder ptr = (PersistPtrHolder)Record.buildRecordAtOffset(data, 0, psrOffset);
            
            lst.add(Integer.valueOf(psrOffset));
            Hashtable<Integer, Integer> entries = ptr.getSlideLocationsLookup();
            for (Integer id : entries.keySet())
            {
                Integer offset = entries.get(id);
                lst.add(offset);
                offset2id.put(offset, id);
            }

            usrOffset = usr.getLastUserEditAtomOffset();
        }
        //sort found records by offset.
        //(it is not necessary but SlideShow.findMostRecentCoreRecords() expects them sorted)
        Integer a[] = lst.toArray(new Integer[lst.size()]);
        Arrays.sort(a);
        Record[] rec = new Record[lst.size()];
        for (int i = 0; i < a.length; i++)
        {
            Integer offset = a[i];
//            rec[i] = Record.buildRecordAtOffset(docstream, offset.intValue());
            byte[] data = _docProp.getRecordData(offset.intValue());
            rec[i] = Record.buildRecordAtOffset(data, 0, offset.intValue());
            
            if (rec[i] instanceof PersistRecord)
            {
                PersistRecord psr = (PersistRecord)rec[i];
                Integer id = offset2id.get(offset);
                psr.setPersistId(id.intValue());
            }
        }

        return rec;
    }

    /**
     * Find the "Current User" stream, and load it
     */
    private void readCurrentUserStream()
    {
        try
        {
            currentUser = new CurrentUserAtom(cfbFS);
        }
        catch(IOException ie)
        {
            //logger.log(POILogger.ERROR, "Error finding Current User Atom:\n" + ie);
            currentUser = new CurrentUserAtom();
        }
    }

    /**
     * Find any other streams from the filesystem, and load them
     */
    private void readOtherStreams()
    {
        // Currently, there aren't any
    }

    /**
     * Find and read in pictures contained in this presentation.
     * This is lazily called as and when we want to touch pictures.
     */
    private void readPictures() throws IOException
    {
        if (control == null)
        {
            return;
        }
        //byte[] pictstream = cfbFS.getPropertyRawData("Pictures");

        /*try
        {
            pictstream = cfbFS.getPropertyRawData("Pictures");
            DocumentEntry entry = (DocumentEntry)directory.getEntry("Pictures");
            pictstream = new byte[entry.getSize()];
            DocumentInputStream is = directory.createDocumentInputStream("Pictures");
            is.read(pictstream);
        }
        catch(FileNotFoundException e)
        {
            // Silently catch exceptions if the presentation doesn't
            //  contain pictures - will use a null set instead
            return;
        }*/
        Property property = cfbFS.getProperty("Pictures");
        if (property == null)
        {
            return;
        }
        
        _pictures = new ArrayList<PictureData>();
        long rawDataSize = property.getPropertyRawDataSize();
        
        int pos = 0;
        // An empty picture record (length 0) will take up 8 bytes
        while (pos <= (rawDataSize - 8))
        {
            int offset = pos;

            // Image signature
            int signature = property.getUShort(pos);//LittleEndian.getUShort(pictstream, pos);
            pos += LittleEndian.SHORT_SIZE;
            // Image type + 0xF018
            int type = property.getUShort(pos);//LittleEndian.getUShort(pictstream, pos);
            pos += LittleEndian.SHORT_SIZE;
            // Image size (excluding the 8 byte header)
            int imgsize = property.getInt(pos);//LittleEndian.getInt(pictstream, pos);
            pos += LittleEndian.INT_SIZE;

            // The image size must be 0 or greater
            // (0 is allowed, but odd, since we do wind on by the header each
            //  time, so we won't get stuck)
            if (imgsize < 0)
            {
            	break;
//                throw new CorruptPowerPointFileException(
//                    "The file contains a picture, at position "
//                        + _pictures.size()
//                        + ", which has a negatively sized data length, so we can't trust any of the picture data");
            }

            // If they type (including the bonus 0xF018) is 0, skip it
            if (type == 0)
            {
                /*logger.log(POILogger.ERROR,
                    "Problem reading picture: Invalid image type 0, on picture with length "
                        + imgsize
                        + ".\nYou document will probably become corrupted if you save it!");
                logger.log(POILogger.ERROR, "" + pos);*/
            }
            else
            {
                // Build the PictureData object from the data
                try
                {
                    PictureData pict = PictureData.create(type - 0xF018);
                    pict.setOffset(offset);
                    
                    // Copy the data, ready to pass to PictureData
                    //byte[] imgdata = new byte[imgsize];
                    //System.arraycopy(pictstream, pos, imgdata, 0, imgdata.length);
                    //pict.setRawData(imgdata);
                    
                    if (pict.getType() == Picture.JPEG
                        || pict.getType() == Picture.PNG
                        || pict.getType() == Picture.DIB
                        || pict.getType() == Picture.WMF
                        || pict.getType() == Picture.EMF)
                    {
                        String name = String.valueOf(System.currentTimeMillis()) +  ".tmp";
                        File file = new File(control.getSysKit().getPictureManage().getPicTempPath() + File.separator + name);
                        try
                        {
                            file.createNewFile();
                            FileOutputStream out = new FileOutputStream(file);
                            if(pict.getType() == Picture.WMF || pict.getType() == Picture.EMF)
                            {
                                byte[] rawdata = property.getRecordData(pict.getOffset());
                                
                                pict.setRawData(rawdata);
//                                out.write(pict.getData());
                                ((Metafile)pict).writeByte_WMFAndEMF(out);
                            	
                            }
                            else
                            {
                                if (pict.getType() == Picture.PNG)
                                {
                                    long a = property.getLong(pos + 17);
                                    if (a == 0x0A1A0A0D474E5089L)
                                    {
                                        property.writeByte(out, pos + 17, imgsize - 17);
                                    }
                                    else
                                    {       
                                        // create PNG base on Mac/OS
                                        a = property.getLong(pos + 33);
                                        if (a == 0x0A1A0A0D474E5089L)
                                        {
                                            property.writeByte(out, pos + 33, imgsize - 33);
                                        }                                            
                                    }                                    
                                }
                                else
                                {
                                    property.writeByte(out, pos + 17, imgsize - 17);
                                }                               
                            }
                            
                            out.close();
                        }
                        catch (Exception e)
                        {
                            control.getSysKit().getErrorKit().writerLog(e);
                        }
                        pict.setTempFilePath(file.getAbsolutePath());
                    
                    }
                    _pictures.add(pict);
                }
                catch(IllegalArgumentException e)
                {
                    /*logger.log(POILogger.ERROR, "Problem reading picture: " + e
                        + "\nYou document will probably become corrupted if you save it!");*/
                	control.getSysKit().getErrorKit().writerLog(e);
                }
            }

            pos += imgsize;
        }
    }

    /**
     * Writes out the slideshow file the is represented by an instance
     *  of this class.
     * It will write out the common OLE2 streams. If you require all
     *  streams to be written out, pass in preserveNodes
     * @param out The OutputStream to write to.
     * @throws IOException If there is an unexpected IOException from
     *           the passed in OutputStream
     */
    /*public void write(OutputStream out) throws IOException
    {
        // Write out, but only the common streams
        write(out, false);
    }*/

    /**
     * Writes out the slideshow file the is represented by an instance
     *  of this class.
     * If you require all streams to be written out (eg Marcos, embeded
     *  documents), then set preserveNodes to true
     * @param out The OutputStream to write to.
     * @param preserveNodes Should all OLE2 streams be written back out, or only the common ones?
     * @throws IOException If there is an unexpected IOException from
     *           the passed in OutputStream
     */
    /*public void write(OutputStream out, boolean preserveNodes) throws IOException
    {
        // Get a new Filesystem to write into
        POIFSFileSystem outFS = new POIFSFileSystem();

        // The list of entries we've written out
        List<String> writtenEntries = new ArrayList<String>(1);

        // Write out the Property Streams
        writeProperties(outFS, writtenEntries);

        // For position dependent records, hold where they were and now are
        // As we go along, update, and hand over, to any Position Dependent
        //  records we happen across
        Hashtable<Integer, Integer> oldToNewPositions = new Hashtable<Integer, Integer>();

        // First pass - figure out where all the position dependent
        //   records are going to end up, in the new scheme
        // (Annoyingly, some powerpoing files have PersistPtrHolders
        //  that reference slides after the PersistPtrHolder)
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int i = 0; i < _records.length; i++)
        {
            if (_records[i] instanceof PositionDependentRecord)
            {
                PositionDependentRecord pdr = (PositionDependentRecord)_records[i];
                int oldPos = pdr.getLastOnDiskOffset();
                int newPos = baos.size();
                pdr.setLastOnDiskOffset(newPos);
                oldToNewPositions.put(Integer.valueOf(oldPos), Integer.valueOf(newPos));
                //System.out.println(oldPos + " -> " + newPos);
            }

            // Dummy write out, so the position winds on properly
            _records[i].writeOut(baos);
        }

        // No go back through, actually writing ourselves out
        baos.reset();
        for (int i = 0; i < _records.length; i++)
        {
            // For now, we're only handling PositionDependentRecord's that
            //  happen at the top level.
            // In future, we'll need the handle them everywhere, but that's
            //  a bit trickier
            if (_records[i] instanceof PositionDependentRecord)
            {
                // We've already figured out their new location, and
                //  told them that
                // Tell them of the positions of the other records though
                PositionDependentRecord pdr = (PositionDependentRecord)_records[i];
                pdr.updateOtherRecordReferences(oldToNewPositions);
            }

            // Whatever happens, write out that record tree
            _records[i].writeOut(baos);
        }
        // Update our cached copy of the bytes that make up the PPT stream
        _docstream = baos.toByteArray();

        // Write the PPT stream into the POIFS layer
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        outFS.createDocument(bais, "PowerPoint Document");
        writtenEntries.add("PowerPoint Document");

        // Update and write out the Current User atom
        int oldLastUserEditAtomPos = (int)currentUser.getCurrentEditOffset();
        Integer newLastUserEditAtomPos = (Integer)oldToNewPositions.get(Integer
            .valueOf(oldLastUserEditAtomPos));
        if (newLastUserEditAtomPos == null)
        {
            throw new HSLFException(
                "Couldn't find the new location of the UserEditAtom that used to be at "
                    + oldLastUserEditAtomPos);
        }
        currentUser.setCurrentEditOffset(newLastUserEditAtomPos.intValue());
        currentUser.writeToFS(outFS);
        writtenEntries.add("Current User");

        // Write any pictures, into another stream
        if (_pictures == null)
        {
            readPictures();
        }
        if (_pictures.size() > 0)
        {
            ByteArrayOutputStream pict = new ByteArrayOutputStream();
            for (PictureData p : _pictures)
            {
                p.write(pict);
            }
            outFS.createDocument(new ByteArrayInputStream(pict.toByteArray()), "Pictures");
            writtenEntries.add("Pictures");
        }

        // If requested, write out any other streams we spot
        if (preserveNodes)
        {
            copyNodes(directory.getFileSystem(), outFS, writtenEntries);
        }

        // Send the POIFSFileSystem object out to the underlying stream
        outFS.writeFilesystem(out);
    }*/

    /* ******************* adding methods follow ********************* */

    /**
     * Adds a new root level record, at the end, but before the last
     *  PersistPtrIncrementalBlock.
     */
    public synchronized int appendRootLevelRecord(Record newRecord)
    {
        int addedAt = -1;
        Record[] r = new Record[_records.length + 1];
        boolean added = false;
        for (int i = (_records.length - 1); i >= 0; i--)
        {
            if (added)
            {
                // Just copy over
                r[i] = _records[i];
            }
            else
            {
                r[(i + 1)] = _records[i];
                if (_records[i] instanceof PersistPtrHolder)
                {
                    r[i] = newRecord;
                    added = true;
                    addedAt = i;
                }
            }
        }
        _records = r;
        return addedAt;
    }

    /**
     * Add a new picture to this presentation.
     *
     * @return offset of this picture in the Pictures stream
     */
    public int addPicture(PictureData img)
    {
        // Process any existing pictures if we haven't yet
        if (_pictures == null)
        {
            try
            {
                readPictures();
            }
            catch(IOException e)
            {
                throw new CorruptPowerPointFileException(e.getMessage());
            }
        }

        // Add the new picture in
        int offset = 0;
        if (_pictures.size() > 0)
        {
            PictureData prev = _pictures.get(_pictures.size() - 1);
            offset = prev.getOffset() + prev.getRawData().length + 8;
        }
        img.setOffset(offset);
        _pictures.add(img);
        return offset;
    }

    /* ******************* fetching methods follow ********************* */

    /**
     * Returns an array of all the records found in the slideshow
     */
    public Record[] getRecords()
    {
        return _records;
    }

    /**
     * Returns an array of the bytes of the file. Only correct after a
     *  call to open or write - at all other times might be wrong!
     */
    public byte[] getUnderlyingBytes()
    {
        return _docstream;
    }

    /**
     * Fetch the Current User Atom of the document
     */
    public CurrentUserAtom getCurrentUserAtom()
    {
        return currentUser;
    }

    /**
     *  Return array of pictures contained in this presentation
     *
     *  @return array with the read pictures or <code>null</code> if the
     *  presentation doesn't contain pictures.
     */
    public PictureData[] getPictures()
    {
        if (_pictures == null)
        {
            try
            {
                readPictures();
            }
            catch(IOException e)
            {
                throw new CorruptPowerPointFileException(e.getMessage());
            }
            catch(OutOfMemoryError e)
            {
                control.getSysKit().getErrorKit().writerLog(e, true);
                control.actionEvent(EventConstant.SYS_READER_FINSH_ID, true);
                control = null;
            }
        }

        if (_pictures != null)
        {
            return _pictures.toArray(new PictureData[_pictures.size()]);
        }
        return null;
    }

    /**
     * Gets embedded object data from the slide show.
     *
     * @return the embedded objects.
     */
    public ObjectData[] getEmbeddedObjects()
    {
        if (_objects == null)
        {
            List<ObjectData> objects = new ArrayList<ObjectData>();
            for (int i = 0; i < _records.length; i++)
            {
                if (_records[i] instanceof ExOleObjStg)
                {
                    objects.add(new ObjectData((ExOleObjStg)_records[i]));
                }
            }
            _objects = objects.toArray(new ObjectData[objects.size()]);
        }
        return _objects;
    }
    
    
    /**
     * 
     */
    public void dispose()
    {
        if (currentUser != null)
        {
            currentUser.dispose();
            currentUser = null;
        }
        if (_records != null)
        {
            for (Record rec: _records)
            {
                rec.dispose();
            }
            _records = null;
        }
        if (_pictures != null)
        {
            for (PictureData pd : _pictures)
            {
                pd.dispose();
            }
            _pictures.clear();
            _pictures = null;
        }
        if (_objects != null)
        {
            for (ObjectData od : _objects)
            {
                od.dispose();
            }
            _objects = null;
        }
        if (cfbFS != null)
        {
            cfbFS.dispose();
            cfbFS = null;
        }
        control = null;
        _docstream = null;
    }
}
