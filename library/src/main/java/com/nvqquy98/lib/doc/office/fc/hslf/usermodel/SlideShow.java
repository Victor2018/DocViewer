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

package com.nvqquy98.lib.doc.office.fc.hslf.usermodel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.fc.ShapeKit;
import com.nvqquy98.lib.doc.office.fc.hslf.HSLFSlideShow;
import com.nvqquy98.lib.doc.office.fc.hslf.exceptions.CorruptPowerPointFileException;
import com.nvqquy98.lib.doc.office.fc.hslf.model.HeadersFooters;
import com.nvqquy98.lib.doc.office.fc.hslf.model.Hyperlink;
import com.nvqquy98.lib.doc.office.fc.hslf.model.MovieShape;
import com.nvqquy98.lib.doc.office.fc.hslf.model.Notes;
import com.nvqquy98.lib.doc.office.fc.hslf.model.Slide;
import com.nvqquy98.lib.doc.office.fc.hslf.model.SlideMaster;
import com.nvqquy98.lib.doc.office.fc.hslf.model.TitleMaster;
import com.nvqquy98.lib.doc.office.fc.hslf.record.Document;
import com.nvqquy98.lib.doc.office.fc.hslf.record.DocumentAtom;
import com.nvqquy98.lib.doc.office.fc.hslf.record.ExAviMovie;
import com.nvqquy98.lib.doc.office.fc.hslf.record.ExControl;
import com.nvqquy98.lib.doc.office.fc.hslf.record.ExHyperlink;
import com.nvqquy98.lib.doc.office.fc.hslf.record.ExHyperlinkAtom;
import com.nvqquy98.lib.doc.office.fc.hslf.record.ExMCIMovie;
import com.nvqquy98.lib.doc.office.fc.hslf.record.ExObjList;
import com.nvqquy98.lib.doc.office.fc.hslf.record.ExObjListAtom;
import com.nvqquy98.lib.doc.office.fc.hslf.record.ExOleObjAtom;
import com.nvqquy98.lib.doc.office.fc.hslf.record.ExVideoContainer;
import com.nvqquy98.lib.doc.office.fc.hslf.record.ExtendedParagraphHeaderAtom;
import com.nvqquy98.lib.doc.office.fc.hslf.record.ExtendedPresRuleContainer;
import com.nvqquy98.lib.doc.office.fc.hslf.record.FontCollection;
import com.nvqquy98.lib.doc.office.fc.hslf.record.HeadersFootersContainer;
import com.nvqquy98.lib.doc.office.fc.hslf.record.PersistPtrHolder;
import com.nvqquy98.lib.doc.office.fc.hslf.record.PositionDependentRecord;
import com.nvqquy98.lib.doc.office.fc.hslf.record.PositionDependentRecordContainer;
import com.nvqquy98.lib.doc.office.fc.hslf.record.Record;
import com.nvqquy98.lib.doc.office.fc.hslf.record.RecordContainer;
import com.nvqquy98.lib.doc.office.fc.hslf.record.RecordTypes;
import com.nvqquy98.lib.doc.office.fc.hslf.record.SlideListWithText;
import com.nvqquy98.lib.doc.office.fc.hslf.record.SlidePersistAtom;
import com.nvqquy98.lib.doc.office.fc.hslf.record.ExtendedPresRuleContainer.ExtendedParaAtomsSet;
import com.nvqquy98.lib.doc.office.fc.hslf.record.SlideListWithText.SlideAtomsSet;
import com.nvqquy98.lib.doc.office.java.awt.Dimension;


/**
 * This class is a friendly wrapper on top of the more scary HSLFSlideShow.
 *
 * figure out how to match notes to their correct sheet (will involve
 * understanding DocSlideList and DocNotesList) - handle Slide creation cleaner
 *
 * @author Nick Burch
 * @author Yegor kozlov
 */
public final class SlideShow
{

    // For logging
    //private POILogger logger = POILogFactory.getLogger(this.getClass());

    /* ===============================================================
     *                       Setup Code
     * ===============================================================
     */

    /**
     * Constructs a Powerpoint document from the underlying
     * HSLFSlideShow object. Finds the model stuff from this
     *
     * @param hslfSlideShow the HSLFSlideShow to base on
     */
    public SlideShow(HSLFSlideShow hslfSlideShow)
    {
        this(hslfSlideShow, false);
    }
    /**
     * Constructs a Powerpoint document from the underlying
     * HSLFSlideShow object. Finds the model stuff from this
     *
     * @param hslfSlideShow the HSLFSlideShow to base on
     */
    public SlideShow(HSLFSlideShow hslfSlideShow, boolean isGetThumbnail)
    {
        // Get useful things from our base slideshow
        _hslfSlideShow = hslfSlideShow;
        _records = _hslfSlideShow.getRecords();
        this.isGetThumbnail = isGetThumbnail;

        // Handle Parent-aware Records
        for (Record record : _records)
        {
            if (record instanceof RecordContainer)
            {
                RecordContainer.handleParentAwareRecords((RecordContainer)record);
            }
        }

        // Find the versions of the core records we'll want to use
        findMostRecentCoreRecords();

        // Build up the model level Slides and Notes
        buildSlidesAndNotes();
    }
    
    /**
     * Use the PersistPtrHolder entries to figure out what is the "most recent"
     * version of all the core records (Document, Notes, Slide etc), and save a
     * record of them. Do this by walking from the oldest PersistPtr to the
     * newest, overwriting any references found along the way with newer ones
     */
    private void findMostRecentCoreRecords()
    {
        // To start with, find the most recent in the byte offset domain
        Hashtable<Integer, Integer> mostRecentByBytes = new Hashtable<Integer, Integer>();
        for (int i = 0; i < _records.length; i++)
        {
            if (_records[i] instanceof PersistPtrHolder)
            {
                PersistPtrHolder pph = (PersistPtrHolder)_records[i];

                // If we've already seen any of the "slide" IDs for this
                // PersistPtr, remove their old positions
                int[] ids = pph.getKnownSlideIDs();
                for (int j = 0; j < ids.length; j++)
                {
                    Integer id = Integer.valueOf(ids[j]);
                    if (mostRecentByBytes.containsKey(id))
                    {
                        mostRecentByBytes.remove(id);
                    }
                }

                // Now, update the byte level locations with their latest values
                Hashtable<Integer, Integer> thisSetOfLocations = pph.getSlideLocationsLookup();
                for (int j = 0; j < ids.length; j++)
                {
                    Integer id = Integer.valueOf(ids[j]);
                    mostRecentByBytes.put(id, thisSetOfLocations.get(id));
                }
            }
        }

        // We now know how many unique special records we have, so init
        // the array
        _mostRecentCoreRecords = new Record[mostRecentByBytes.size()];

        // We'll also want to be able to turn the slide IDs into a position
        // in this array
        _sheetIdToCoreRecordsLookup = new Hashtable<Integer, Integer>();
        int[] allIDs = new int[_mostRecentCoreRecords.length];
        Enumeration<Integer> ids = mostRecentByBytes.keys();
        for (int i = 0; i < allIDs.length; i++)
        {
            Integer id = ids.nextElement();
            allIDs[i] = id.intValue();
        }
        Arrays.sort(allIDs);
        for (int i = 0; i < allIDs.length; i++)
        {
            _sheetIdToCoreRecordsLookup.put(Integer.valueOf(allIDs[i]), Integer.valueOf(i));
        }

        // Now convert the byte offsets back into record offsets
        for (int i = 0; i < _records.length; i++)
        {
            if (_records[i] instanceof PositionDependentRecord)
            {
                PositionDependentRecord pdr = (PositionDependentRecord)_records[i];
                Integer recordAt = Integer.valueOf(pdr.getLastOnDiskOffset());

                // Is it one we care about?
                for (int j = 0; j < allIDs.length; j++)
                {
                    Integer thisID = Integer.valueOf(allIDs[j]);
                    Integer thatRecordAt = mostRecentByBytes.get(thisID);

                    if (thatRecordAt.equals(recordAt))
                    {
                        // Bingo. Now, where do we store it?
                        Integer storeAtI = _sheetIdToCoreRecordsLookup.get(thisID);
                        int storeAt = storeAtI.intValue();

                        // Tell it its Sheet ID, if it cares
                        if (pdr instanceof PositionDependentRecordContainer)
                        {
                            PositionDependentRecordContainer pdrc = (PositionDependentRecordContainer)_records[i];
                            pdrc.setSheetId(thisID.intValue());
                        }

                        // Finally, save the record
                        _mostRecentCoreRecords[storeAt] = _records[i];
                    }
                }
            }
        }

        // Now look for the interesting records in there
        for (int i = 0; i < _mostRecentCoreRecords.length; i++)
        {
            // Check there really is a record at this number
            if (_mostRecentCoreRecords[i] != null)
            {
                // Find the Document, and interesting things in it
                if (_mostRecentCoreRecords[i].getRecordType() == RecordTypes.Document.typeID)
                {
                    _documentRecord = (Document)_mostRecentCoreRecords[i];
                    _fonts = _documentRecord.getEnvironment().getFontCollection();
                }
            }
            else
            {
                // No record at this number
                // Odd, but not normally a problem
            }
        }
    }

    /**
     * For a given SlideAtomsSet, return the core record, based on the refID
     * from the SlidePersistAtom
     */
    private Record getCoreRecordForSAS(SlideAtomsSet sas)
    {
        SlidePersistAtom spa = sas.getSlidePersistAtom();
        int refID = spa.getRefID();
        return getCoreRecordForRefID(refID);
    }

    /**
     * For a given refID (the internal, 0 based numbering scheme), return the
     * core record
     *
     * @param refID
     *            the refID
     */
    private Record getCoreRecordForRefID(int refID)
    {
        Integer coreRecordId = _sheetIdToCoreRecordsLookup.get(Integer.valueOf(refID));
        if (coreRecordId != null)
        {
            Record r = _mostRecentCoreRecords[coreRecordId.intValue()];
            return r;
        }
        /*logger.log(POILogger.ERROR,
            "We tried to look up a reference to a core record, but there was no core ID for reference ID "
                + refID);*/
        return null;
    }

    /**
     * Build up model level Slide and Notes objects, from the underlying
     * records.
     */
    private void buildSlidesAndNotes()
    {
        // Ensure we really found a Document record earlier
        // If we didn't, then the file is probably corrupt
        if (_documentRecord == null)
        {
            throw new CorruptPowerPointFileException(
                "The PowerPoint file didn't contain a Document Record in its PersistPtr blocks. It is probably corrupt.");
        }

        // Fetch the SlideListWithTexts in the most up-to-date Document Record
        //
        // As far as we understand it:
        // * The first SlideListWithText will contain a SlideAtomsSet
        // for each of the master slides
        // * The second SlideListWithText will contain a SlideAtomsSet
        // for each of the slides, in their current order
        // These SlideAtomsSets will normally contain text
        // * The third SlideListWithText (if present), will contain a
        // SlideAtomsSet for each Notes
        // These SlideAtomsSets will not normally contain text
        //
        // Having indentified the masters, slides and notes + their orders,
        // we have to go and find their matching records
        // We always use the latest versions of these records, and use the
        // SlideAtom/NotesAtom to match them with the StyleAtomSet

        SlideListWithText masterSLWT = _documentRecord.getMasterSlideListWithText();
        SlideListWithText slidesSLWT = _documentRecord.getSlideSlideListWithText();
        SlideListWithText notesSLWT = _documentRecord.getNotesSlideListWithText();

        // Find master slides
        // These can be MainMaster records, but oddly they can also be
        // Slides or Notes, and possibly even other odd stuff....
        // About the only thing you can say is that the master details are in
        // the first SLWT.
        SlideAtomsSet[] masterSets = new SlideAtomsSet[0];
        if (masterSLWT != null)
        {
            masterSets = masterSLWT.getSlideAtomsSets();

            ArrayList<SlideMaster> mmr = new ArrayList<SlideMaster>();
            ArrayList<TitleMaster> tmr = new ArrayList<TitleMaster>();

            for (int i = 0; i < masterSets.length; i++)
            {
                Record r = getCoreRecordForSAS(masterSets[i]);
                SlideAtomsSet sas = masterSets[i];
                int sheetNo = sas.getSlidePersistAtom().getSlideIdentifier();
                if (r instanceof com.nvqquy98.lib.doc.office.fc.hslf.record.Slide)
                {
                    TitleMaster master = new TitleMaster((com.nvqquy98.lib.doc.office.fc.hslf.record.Slide)r, sheetNo);
                    master.setSlideShow(this);
                    tmr.add(master);
                }
                else if (r instanceof com.nvqquy98.lib.doc.office.fc.hslf.record.MainMaster)
                {
                    SlideMaster master = new SlideMaster((com.nvqquy98.lib.doc.office.fc.hslf.record.MainMaster)r, sheetNo);
                    master.setSlideShow(this);
                    mmr.add(master);
                }
            }

            _masters = new SlideMaster[mmr.size()];
            mmr.toArray(_masters);

            _titleMasters = new TitleMaster[tmr.size()];
            tmr.toArray(_titleMasters);
        }

        // Having sorted out the masters, that leaves the notes and slides

        // Start by finding the notes records to go with the entries in
        // notesSLWT
        com.nvqquy98.lib.doc.office.fc.hslf.record.Notes[] notesRecords;
        SlideAtomsSet[] notesSets = new SlideAtomsSet[0];
        Hashtable<Integer, Integer> slideIdToNotes = new Hashtable<Integer, Integer>();
        if (notesSLWT == null)
        {
            // None
            notesRecords = new com.nvqquy98.lib.doc.office.fc.hslf.record.Notes[0];
        }
        else
        {
            // Match up the records and the SlideAtomSets
            notesSets = notesSLWT.getSlideAtomsSets();
            ArrayList<com.nvqquy98.lib.doc.office.fc.hslf.record.Notes> notesRecordsL = new ArrayList<com.nvqquy98.lib.doc.office.fc.hslf.record.Notes>();
            for (int i = 0; i < notesSets.length; i++)
            {
                // Get the right core record
                Record r = getCoreRecordForSAS(notesSets[i]);

                // Ensure it really is a notes record
                if (r instanceof com.nvqquy98.lib.doc.office.fc.hslf.record.Notes)
                {
                    com.nvqquy98.lib.doc.office.fc.hslf.record.Notes notesRecord = (com.nvqquy98.lib.doc.office.fc.hslf.record.Notes)r;
                    notesRecordsL.add(notesRecord);

                    // Record the match between slide id and these notes
                    SlidePersistAtom spa = notesSets[i].getSlidePersistAtom();
                    Integer slideId = Integer.valueOf(spa.getSlideIdentifier());
                    slideIdToNotes.put(slideId, Integer.valueOf(i));
                }
                /*else
                {
                    logger.log(POILogger.ERROR, "A Notes SlideAtomSet at " + i
                        + " said its record was at refID "
                        + notesSets[i].getSlidePersistAtom().getRefID()
                        + ", but that was actually a " + r);
                }*/
            }
            notesRecords = new com.nvqquy98.lib.doc.office.fc.hslf.record.Notes[notesRecordsL.size()];
            notesRecords = notesRecordsL.toArray(notesRecords);
        }

        // Now, do the same thing for our slides
        com.nvqquy98.lib.doc.office.fc.hslf.record.Slide[] slidesRecords;
        SlideAtomsSet[] slidesSets = new SlideAtomsSet[0];
        if (slidesSLWT == null)
        {
            // None
            slidesRecords = new com.nvqquy98.lib.doc.office.fc.hslf.record.Slide[0];
        }
        else
        {
            // Match up the records and the SlideAtomSets
            slidesSets = slidesSLWT.getSlideAtomsSets();
            slidesRecords = new com.nvqquy98.lib.doc.office.fc.hslf.record.Slide[slidesSets.length];
            for (int i = 0; i < slidesSets.length; i++)
            {
                // Get the right core record
                Record r = getCoreRecordForSAS(slidesSets[i]);

                // Ensure it really is a slide record
                if (r instanceof com.nvqquy98.lib.doc.office.fc.hslf.record.Slide)
                {
                    slidesRecords[i] = (com.nvqquy98.lib.doc.office.fc.hslf.record.Slide)r;
                }
                /*else
                {
                    logger.log(POILogger.ERROR, "A Slide SlideAtomSet at " + i
                        + " said its record was at refID "
                        + slidesSets[i].getSlidePersistAtom().getRefID()
                        + ", but that was actually a " + r);
                }*/
            }
        }

        // Finally, generate model objects for everything
        // Notes first
        _notes = new Notes[isGetThumbnail ? Math.min(notesRecords.length, 1) : notesRecords.length];
        for (int i = 0; i < _notes.length; i++)
        {
            _notes[i] = new Notes(notesRecords[i]);
            _notes[i].setSlideShow(this);
        }
        // Then slides
        ExtendedParaAtomsSet[] extendedParaAtomsSets = null;
        if (_documentRecord.getList() != null)
        {
            ExtendedPresRuleContainer extendedPresRule = _documentRecord.getList().getExtendedPresRuleContainer();
            if (extendedPresRule != null)
            {
                extendedParaAtomsSets = extendedPresRule.getExtendedParaAtomsSets();
            }
        }
        _slides = new Slide[isGetThumbnail ? 1 : slidesRecords.length];
        for (int i = 0; i < _slides.length; i++)
        {
            SlideAtomsSet sas = slidesSets[i];
            int slideIdentifier = sas.getSlidePersistAtom().getSlideIdentifier();
            
            //
            Vector<ExtendedParaAtomsSet> extendedSets = new Vector<ExtendedParaAtomsSet>();
            if (extendedParaAtomsSets != null)
            {
                for (int j = 0; j < extendedParaAtomsSets.length; j++)
                {
                    ExtendedParagraphHeaderAtom paraHeaderAtom = extendedParaAtomsSets[j].getExtendedParaHeaderAtom();
                    if (paraHeaderAtom != null && paraHeaderAtom.getRefSlideID() == slideIdentifier)
                    {
                        extendedSets.add(extendedParaAtomsSets[j]);
                    }
                }
            }
            ExtendedParaAtomsSet[] extendedAtoms = null;
            if (extendedSets.size() > 0)
            {
                extendedAtoms = extendedSets.toArray(new ExtendedParaAtomsSet[extendedSets.size()]);
            }

            // Do we have a notes for this?
            Notes notes = null;
            // Slide.SlideAtom.notesId references the corresponding notes slide.
            // 0 if slide has no notes.
            int noteId = slidesRecords[i].getSlideAtom().getNotesID();
            if (noteId != 0)
            {
                Integer notesPos = (Integer)slideIdToNotes.get(Integer.valueOf(noteId));
                if (notesPos != null && notesPos.intValue() < _notes.length)
                    notes = _notes[notesPos.intValue()];
                /*else
                    logger.log(POILogger.ERROR, "Notes not found for noteId=" + noteId);*/
            }

            // Now, build our slide
            _slides[i] = new Slide(slidesRecords[i], notes, sas, extendedAtoms, slideIdentifier, (i + 1));
            _slides[i].setSlideShow(this);
            
            //slide transition  and animation
            _slides[i].setSlideShowSlideInfoAtom(slidesRecords[i].getSlideShowSlideInfoAtom());
            _slides[i].setSlideProgTagsContainer(slidesRecords[i].getSlideProgTagsContainer());
        }
    }

    /**
     * Writes out the slideshow file the is represented by an instance of this
     * class
     *
     * @param out
     *            The OutputStream to write to.
     * @throws IOException
     *             If there is an unexpected IOException from the passed in
     *             OutputStream
     */
    public void write(OutputStream out) throws IOException
    {
        //_hslfSlideShow.write(out);
    }

    /*
     * ===============================================================
     *                         Accessor Code
     * ===============================================================
     */

    /**
     * Returns an array of the most recent version of all the interesting
     * records
     */
    public Record[] getMostRecentCoreRecords()
    {
        return _mostRecentCoreRecords;
    }

    /**
     * Returns an array of all the normal Slides found in the slideshow
     */
    public Slide[] getSlides()
    {
        return _slides;
    }

    /**
     * Returns an array of all the normal Notes found in the slideshow
     */
    public Notes[] getNotes()
    {
        return _notes;
    }

    /**
     * Returns an array of all the normal Slide Masters found in the slideshow
     */
    public SlideMaster[] getSlidesMasters()
    {
        return _masters;
    }

    /**
     * Returns an array of all the normal Title Masters found in the slideshow
     */
    public TitleMaster[] getTitleMasters()
    {
        return _titleMasters;
    }

    /**
     * Returns the data of all the pictures attached to the SlideShow
     */
    public PictureData[] getPictureData()
    {
        return _hslfSlideShow.getPictures();
    }

    /**
     * Returns the data of all the embedded OLE object in the SlideShow
     */
    public ObjectData[] getEmbeddedObjects()
    {
        return _hslfSlideShow.getEmbeddedObjects();
    }

    /**
     * Returns the data of all the embedded sounds in the SlideShow
     */
    public SoundData[] getSoundData()
    {
        return SoundData.find(_documentRecord);
    }

    /**
     * Return the current page size
     */
    public Dimension getPageSize()
    {
        DocumentAtom docatom = _documentRecord.getDocumentAtom();
        // float scale = docatom.getSlideSizeX()*1f/ ScreenUtils.getScreenWidth();
        // int pgx = (int)(docatom.getSlideSizeX() / scale / MainConstant.POINT_TO_PIXEL);
        // int pgy = (int)(docatom.getSlideSizeY() / scale / MainConstant.POINT_TO_PIXEL);
        int pgx = (int)(docatom.getSlideSizeX() * MainConstant.POINT_DPI / ShapeKit.MASTER_DPI);
        int pgy = (int)(docatom.getSlideSizeY() * MainConstant.POINT_DPI / ShapeKit.MASTER_DPI);
        return new Dimension(pgx, pgy);
    }

    /**
     * Change the current page size
     *
     * @param pgsize
     *            page size (in points)
     */
    public void setPageSize(Dimension pgsize)
    {
        DocumentAtom docatom = _documentRecord.getDocumentAtom();
        docatom.setSlideSizeX((long)(pgsize.width * ShapeKit.MASTER_DPI / MainConstant.POINT_DPI));
        docatom.setSlideSizeY((long)(pgsize.height * ShapeKit.MASTER_DPI / MainConstant.POINT_DPI));
    }

    /**
     * Helper method for usermodel: Get the font collection
     */
    protected FontCollection getFontCollection()
    {
        return _fonts;
    }

    /**
     * Helper method for usermodel and model: Get the document record
     */
    public Document getDocumentRecord()
    {
        return _documentRecord;
    }

    /*
     * ===============================================================
     * Re-ordering Code
     * ===============================================================
     */

    /**
     * Re-orders a slide, to a new position.
     *
     * @param oldSlideNumber
     *            The old slide number (1 based)
     * @param newSlideNumber
     *            The new slide number (1 based)
     */
    public void reorderSlide(int oldSlideNumber, int newSlideNumber)
    {
        // Ensure these numbers are valid
        if (oldSlideNumber < 1 || newSlideNumber < 1)
        {
            throw new IllegalArgumentException("Old and new slide numbers must be greater than 0");
        }
        if (oldSlideNumber > _slides.length || newSlideNumber > _slides.length)
        {
            throw new IllegalArgumentException(
                "Old and new slide numbers must not exceed the number of slides (" + _slides.length
                    + ")");
        }

        // The order of slides is defined by the order of slide atom sets in the
        // SlideListWithText container.
        SlideListWithText slwt = _documentRecord.getSlideSlideListWithText();
        SlideAtomsSet[] sas = slwt.getSlideAtomsSets();

        SlideAtomsSet tmp = sas[oldSlideNumber - 1];
        sas[oldSlideNumber - 1] = sas[newSlideNumber - 1];
        sas[newSlideNumber - 1] = tmp;

        ArrayList<Record> lst = new ArrayList<Record>();
        for (int i = 0; i < sas.length; i++)
        {
            lst.add(sas[i].getSlidePersistAtom());
            Record[] r = sas[i].getSlideRecords();
            for (int j = 0; j < r.length; j++)
            {
                lst.add(r[j]);
            }
            _slides[i].setSlideNumber(i + 1);
        }
        Record[] r = lst.toArray(new Record[lst.size()]);
        slwt.setChildRecord(r);
    }

    /**
     * Removes the slide at the given index (0-based).
     * <p>
     * Shifts any subsequent slides to the left (subtracts one from their slide
     * numbers).
     * </p>
     *
     * @param index
     *            the index of the slide to remove (0-based)
     * @return the slide that was removed from the slide show.
     */
    public Slide removeSlide(int index)
    {
        int lastSlideIdx = _slides.length - 1;
        if (index < 0 || index > lastSlideIdx)
        {
            throw new IllegalArgumentException("Slide index (" + index + ") is out of range (0.."
                + lastSlideIdx + ")");
        }

        SlideListWithText slwt = _documentRecord.getSlideSlideListWithText();
        SlideAtomsSet[] sas = slwt.getSlideAtomsSets();

        Slide removedSlide = null;
        ArrayList<Record> records = new ArrayList<Record>();
        ArrayList<SlideAtomsSet> sa = new ArrayList<SlideAtomsSet>();
        ArrayList<Slide> sl = new ArrayList<Slide>();

        ArrayList<Notes> nt = new ArrayList<Notes>();
        for (Notes notes : getNotes())
            nt.add(notes);

        for (int i = 0, num = 0; i < _slides.length; i++)
        {
            if (i != index)
            {
                sl.add(_slides[i]);
                sa.add(sas[i]);
                _slides[i].setSlideNumber(num++);
                records.add(sas[i].getSlidePersistAtom());
                records.addAll(Arrays.asList(sas[i].getSlideRecords()));
            }
            else
            {
                removedSlide = _slides[i];
                nt.remove(_slides[i].getNotesSheet());
            }
        }
        if (sa.size() == 0)
        {
            _documentRecord.removeSlideListWithText(slwt);
        }
        else
        {
            slwt.setSlideAtomsSets(sa.toArray(new SlideAtomsSet[sa.size()]));
            slwt.setChildRecord(records.toArray(new Record[records.size()]));
        }
        _slides = sl.toArray(new Slide[sl.size()]);

        // if the removed slide had notes - remove references to them too

        if (removedSlide != null)
        {
            int notesId = removedSlide.getSlideRecord().getSlideAtom().getNotesID();
            if (notesId != 0)
            {
                SlideListWithText nslwt = _documentRecord.getNotesSlideListWithText();
                records = new ArrayList<Record>();
                ArrayList<SlideAtomsSet> na = new ArrayList<SlideAtomsSet>();
                for (SlideAtomsSet ns : nslwt.getSlideAtomsSets())
                {
                    if (ns.getSlidePersistAtom().getSlideIdentifier() != notesId)
                    {
                        na.add(ns);
                        records.add(ns.getSlidePersistAtom());
                        if (ns.getSlideRecords() != null)
                            records.addAll(Arrays.asList(ns.getSlideRecords()));
                    }
                }
                if (na.size() == 0)
                {
                    _documentRecord.removeSlideListWithText(nslwt);
                }
                else
                {
                    nslwt.setSlideAtomsSets(na.toArray(new SlideAtomsSet[na.size()]));
                    nslwt.setChildRecord(records.toArray(new Record[records.size()]));
                }

            }
        }
        _notes = nt.toArray(new Notes[nt.size()]);

        return removedSlide;
    }

    /*
     * ===============================================================
     *  Addition Code
     * ===============================================================
     */

    /**
     * get the number of fonts in the presentation
     *
     * @return number of fonts
     */
    public int getNumberOfFonts()
    {
        return getDocumentRecord().getEnvironment().getFontCollection().getNumberOfFonts();
    }

    /**
     * Return Header / Footer settings for slides
     *
     * @return Header / Footer settings for slides
     */
    public HeadersFooters getSlideHeadersFooters()
    {
        // detect if this ppt was saved in Office2007
        String tag = getSlidesMasters()[0].getProgrammableTag();
        boolean ppt2007 = "___PPT12".equals(tag);

        HeadersFootersContainer hdd = null;
        Record[] ch = _documentRecord.getChildRecords();
        for (int i = 0; i < ch.length; i++)
        {
            if (ch[i] instanceof HeadersFootersContainer
                && ((HeadersFootersContainer)ch[i]).getOptions() == HeadersFootersContainer.SlideHeadersFootersContainer)
            {
                hdd = (HeadersFootersContainer)ch[i];
                break;
            }
        }
        boolean newRecord = false;
        if (hdd == null)
        {
            hdd = new HeadersFootersContainer(HeadersFootersContainer.SlideHeadersFootersContainer);
            newRecord = true;
        }
        return new HeadersFooters(hdd, this, newRecord, ppt2007);
    }

    /**
     * Return Header / Footer settings for notes
     *
     * @return Header / Footer settings for notes
     */
    public HeadersFooters getNotesHeadersFooters()
    {
        // detect if this ppt was saved in Office2007
        String tag = getSlidesMasters()[0].getProgrammableTag();
        boolean ppt2007 = "___PPT12".equals(tag);

        HeadersFootersContainer hdd = null;
        Record[] ch = _documentRecord.getChildRecords();
        for (int i = 0; i < ch.length; i++)
        {
            if (ch[i] instanceof HeadersFootersContainer
                && ((HeadersFootersContainer)ch[i]).getOptions() == HeadersFootersContainer.NotesHeadersFootersContainer)
            {
                hdd = (HeadersFootersContainer)ch[i];
                break;
            }
        }
        boolean newRecord = false;
        if (hdd == null)
        {
            hdd = new HeadersFootersContainer(HeadersFootersContainer.NotesHeadersFootersContainer);
            newRecord = true;
        }
        if (ppt2007 && _notes.length > 0)
        {
            return new HeadersFooters(hdd, _notes[0], newRecord, ppt2007);
        }
        return new HeadersFooters(hdd, this, newRecord, ppt2007);
    }

    /**
     * Add a movie in this presentation
     *
     * @param path
     *            the path or url to the movie
     * @return 0-based index of the movie
     */
    public int addMovie(String path, int type)
    {
        ExObjList lst = (ExObjList)_documentRecord.findFirstOfType(RecordTypes.ExObjList.typeID);
        if (lst == null)
        {
            lst = new ExObjList();
            _documentRecord.addChildAfter(lst, _documentRecord.getDocumentAtom());
        }

        ExObjListAtom objAtom = lst.getExObjListAtom();
        // increment the object ID seed
        int objectId = (int)objAtom.getObjectIDSeed() + 1;
        objAtom.setObjectIDSeed(objectId);
        ExMCIMovie mci;
        switch (type)
        {
            case MovieShape.MOVIE_MPEG:
                mci = new ExMCIMovie();
                break;
            case MovieShape.MOVIE_AVI:
                mci = new ExAviMovie();
                break;
            default:
                throw new IllegalArgumentException("Unsupported Movie: " + type);
        }

        lst.appendChildRecord(mci);
        ExVideoContainer exVideo = mci.getExVideo();
        exVideo.getExMediaAtom().setObjectId(objectId);
        exVideo.getExMediaAtom().setMask(0xE80000);
        exVideo.getPathAtom().setText(path);
        return objectId;
    }

    /**
     * Add a control in this presentation
     *
     * @param name
     *            name of the control, e.g. "Shockwave Flash Object"
     * @param progId
     *            OLE Programmatic Identifier, e.g.
     *            "ShockwaveFlash.ShockwaveFlash.9"
     * @return 0-based index of the control
     */
    public int addControl(String name, String progId)
    {
        ExObjList lst = (ExObjList)_documentRecord.findFirstOfType(RecordTypes.ExObjList.typeID);
        if (lst == null)
        {
            lst = new ExObjList();
            _documentRecord.addChildAfter(lst, _documentRecord.getDocumentAtom());
        }
        ExObjListAtom objAtom = lst.getExObjListAtom();
        // increment the object ID seed
        int objectId = (int)objAtom.getObjectIDSeed() + 1;
        objAtom.setObjectIDSeed(objectId);
        ExControl ctrl = new ExControl();
        ExOleObjAtom oleObj = ctrl.getExOleObjAtom();
        oleObj.setObjID(objectId);
        oleObj.setDrawAspect(ExOleObjAtom.DRAW_ASPECT_VISIBLE);
        oleObj.setType(ExOleObjAtom.TYPE_CONTROL);
        oleObj.setSubType(ExOleObjAtom.SUBTYPE_DEFAULT);

        ctrl.setProgId(progId);
        ctrl.setMenuName(name);
        ctrl.setClipboardName(name);
        lst.addChildAfter(ctrl, objAtom);

        return objectId;
    }

    /**
     * Add a hyperlink to this presentation
     *
     * @return 0-based index of the hyperlink
     */
    public int addHyperlink(Hyperlink link)
    {
        ExObjList lst = (ExObjList)_documentRecord.findFirstOfType(RecordTypes.ExObjList.typeID);
        if (lst == null)
        {
            lst = new ExObjList();
            _documentRecord.addChildAfter(lst, _documentRecord.getDocumentAtom());
        }
        ExObjListAtom objAtom = lst.getExObjListAtom();
        // increment the object ID seed
        int objectId = (int)objAtom.getObjectIDSeed() + 1;
        objAtom.setObjectIDSeed(objectId);

        ExHyperlink ctrl = new ExHyperlink();
        ExHyperlinkAtom obj = ctrl.getExHyperlinkAtom();
        obj.setNumber(objectId);
        ctrl.setLinkURL(link.getAddress());
        ctrl.setLinkTitle(link.getTitle());
        lst.addChildAfter(ctrl, objAtom);
        link.setId(objectId);

        return objectId;
    }
    
    /**
     * 得到slide张数
     */
    public int getSlideCount()
    {
        return _slides.length;
    }
    
    /**
     * 得到指定的slide
     */
    public Slide getSlide(int index)
    {
        if (index < 0 || index >= getSlideCount())
        {
            return null;
        }
        return _slides[index];
    }
    
    /**
     * 
     */
    public void dispose()
    {
        if (_hslfSlideShow != null)
        {
            _hslfSlideShow.dispose();
            _hslfSlideShow = null;
        }
        if (_records != null)
        {
            for (Record rd : _records)
            {
                rd.dispose();
            }
            _records = null;
        }
        if (_mostRecentCoreRecords != null)
        {
            for (Record rd : _mostRecentCoreRecords)
            {
                rd.dispose();
            }
            _mostRecentCoreRecords = null;
        }
        if (_sheetIdToCoreRecordsLookup != null)
        {
            _sheetIdToCoreRecordsLookup.clear();
            _sheetIdToCoreRecordsLookup = null;
        }
        if (_documentRecord != null)
        {
            _documentRecord.dispose();
            _documentRecord = null;
        }
        
        if (_masters != null)
        {
            for (SlideMaster sm : _masters)
            {
                sm.dispose();
            }
            _masters = null;
        }
        
        if (_titleMasters != null)
        {
            for (TitleMaster tm : _titleMasters)
            {
                tm.dispose();
            }
            _titleMasters = null;
        }
        
        if (_slides != null)
        {
            for (Slide slide : _slides)
            {
                slide.dispose();
            }
            _slides = null;
        }
        
        if (_notes != null)
        {
            for (Notes note : _notes)
            {
                note.dispose();
            }
            _notes = null;
        }
        
        if (_fonts != null)
        {
            _fonts.dispose();
            _fonts = null;
        }
        
    }
    
    // What we're based on
    private HSLFSlideShow _hslfSlideShow;

    // Low level contents, as taken from HSLFSlideShow
    private Record[] _records;

    // Pointers to the most recent versions of the core records
    // (Document, Notes, Slide etc)
    private Record[] _mostRecentCoreRecords;
    // Lookup between the PersitPtr "sheet" IDs, and the position
    // in the mostRecentCoreRecords array
    private Hashtable<Integer, Integer> _sheetIdToCoreRecordsLookup;

    // Records that are interesting
    private Document _documentRecord;

    // Friendly objects for people to deal with
    private SlideMaster[] _masters;
    private TitleMaster[] _titleMasters;
    private Slide[] _slides;
    private Notes[] _notes;
    private FontCollection _fonts;
    
    //
    private boolean isGetThumbnail;
}
