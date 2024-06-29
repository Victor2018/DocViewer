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

package com.nvqquy98.lib.doc.office.fc.hwpf;

import java.io.IOException;
import java.io.InputStream;

import com.nvqquy98.lib.doc.office.fc.hwpf.model.BookmarksTables;
import com.nvqquy98.lib.doc.office.fc.hwpf.model.CHPBinTable;
import com.nvqquy98.lib.doc.office.fc.hwpf.model.CPSplitCalculator;
import com.nvqquy98.lib.doc.office.fc.hwpf.model.ComplexFileTable;
import com.nvqquy98.lib.doc.office.fc.hwpf.model.EscherRecordHolder;
import com.nvqquy98.lib.doc.office.fc.hwpf.model.FSPADocumentPart;
import com.nvqquy98.lib.doc.office.fc.hwpf.model.FSPATable;
import com.nvqquy98.lib.doc.office.fc.hwpf.model.FieldsTables;
import com.nvqquy98.lib.doc.office.fc.hwpf.model.FontTable;
import com.nvqquy98.lib.doc.office.fc.hwpf.model.ListTables;
import com.nvqquy98.lib.doc.office.fc.hwpf.model.PAPBinTable;
import com.nvqquy98.lib.doc.office.fc.hwpf.model.PicturesTable;
import com.nvqquy98.lib.doc.office.fc.hwpf.model.PlcfTxbxBkd;
import com.nvqquy98.lib.doc.office.fc.hwpf.model.SectionTable;
import com.nvqquy98.lib.doc.office.fc.hwpf.model.ShapesTable;
import com.nvqquy98.lib.doc.office.fc.hwpf.model.StyleSheet;
import com.nvqquy98.lib.doc.office.fc.hwpf.model.SubdocumentType;
import com.nvqquy98.lib.doc.office.fc.hwpf.model.TextPieceTable;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.Bookmarks;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.BookmarksImpl;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.Field;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.Fields;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.FieldsImpl;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.HWPFList;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.OfficeDrawings;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.OfficeDrawingsImpl;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.Range;
import com.nvqquy98.lib.doc.office.fc.util.Internal;


/**
 *
 * This class acts as the bucket that we throw all of the Word data structures
 * into.
 *
 * @author Ryan Ackley
 */
public final class HWPFDocument extends HWPFDocumentCore
{
    private static final String PROPERTY_PRESERVE_BIN_TABLES = "com.wxiwei.fc.hwpf.preserveBinTables";
    private static final String PROPERTY_PRESERVE_TEXT_TABLE = "com.wxiwei.fc.hwpf.preserveTextTable";

    private static final String STREAM_DATA = "Data";
    private static final String STREAM_TABLE_0 = "0Table";
    private static final String STREAM_TABLE_1 = "1Table";

    /** table stream buffer*/
    protected byte[] _tableStream;

    /** data stream buffer*/
    protected byte[] _dataStream;

    /** Document wide Properties*/
    //protected DocumentProperties _dop;

    /** Contains text of the document wrapped in a obfuscated Word data
    * structure*/
    protected ComplexFileTable _cft;

    /** Contains text buffer linked directly to single-piece document text piece */
    protected StringBuilder _text;

    /** Holds the save history for this document. */
    //protected SavedByTable _sbt;

    /** Holds the revision mark authors for this document. */
    //protected RevisionMarkAuthorTable _rmat;

    /** Holds FSBA (shape) information */
    private FSPATable _fspaHeaders;

    /** Holds FSBA (shape) information */
    private FSPATable _fspaMain;

    /** Escher Drawing Group information */
    protected EscherRecordHolder _escherRecordHolder;

    /** Holds pictures table */
    protected PicturesTable _pictures;

    /** Holds Office Art objects */
    @ Deprecated
    protected ShapesTable _officeArts;

    /** Holds Office Art objects */
    protected OfficeDrawingsImpl _officeDrawingsHeaders;

    /** Holds Office Art objects */
    protected OfficeDrawingsImpl _officeDrawingsMain;

    /** Holds the bookmarks tables */
    protected BookmarksTables _bookmarksTables;

    /** Holds the bookmarks */
    protected Bookmarks _bookmarks;

    /** Holds the ending notes tables */
    //protected NotesTables _endnotesTables = new NotesTables(NoteType.ENDNOTE);

    /** Holds the footnotes */
    //protected Notes _endnotes = new NotesImpl(_endnotesTables);

    /** Holds the footnotes tables */
    //protected NotesTables _footnotesTables = new NotesTables(NoteType.FOOTNOTE);

    /** Holds the footnotes */
    //protected Notes _footnotes = new NotesImpl(_footnotesTables);

    /** Holds the fields PLCFs */
    protected FieldsTables _fieldsTables;

    /** Holds the fields */
    protected Fields _fields;
    
    /**
     * textbox
     */
    protected PlcfTxbxBkd txbxBkd;

    /**
     * This constructor loads a Word document from an InputStream.
     *
     * @param istream The InputStream that contains the Word document.
     * @throws IOException If there is an unexpected IOException from the passed
     *         in InputStream.
     */
    /*public HWPFDocument(InputStream istream) throws IOException
    {
        //do Ole stuff
        this(verifyAndBuildPOIFS(istream));
    }

    *//**
     * This constructor loads a Word document from a POIFSFileSystem
     *
     * @param pfilesystem The POIFSFileSystem that contains the Word document.
     * @throws IOException If there is an unexpected IOException from the passed
     *         in POIFSFileSystem.
     *//*
    public HWPFDocument(POIFSFileSystem pfilesystem) throws IOException
    {
        this(pfilesystem.getRoot());
    }
*/
    /**
     * This constructor loads a Word document from a specific point
     *  in a POIFSFileSystem, probably not the default.
     * Used typically to open embeded documents.
     *
     * @param directory The DirectoryNode that contains the Word document.
     * @throws IOException If there is an unexpected IOException from the passed
     *         in POIFSFileSystem.
     */
    public HWPFDocument(InputStream istream) throws IOException
    {
        super(istream);
        // Load the main stream and FIB
        // Also handles HPSF bits
        //super(directory);

        // Do the CP Split
        CPSplitCalculator _cpSplit = new CPSplitCalculator(_fib);

        // Is this document too old for us?
        if (_fib.getNFib() < 106)
        {
           if (_fib.getNFib() == 0)
           {
               throw new NullPointerException();
           }           
            throw new OldWordFileFormatException(
                "The document is too old - Word 95 or older. Try HWPFOldDocument instead?");
        }

        // use the fib to determine the name of the table stream.
        String name = STREAM_TABLE_0;
        if (_fib.isFWhichTblStm())
        {
            name = STREAM_TABLE_1;
        }

        // Grab the table stream.
        //DocumentEntry tableProps;
        try
        {
            _tableStream = cfbFS.getPropertyRawData(name);
            //tableProps = (DocumentEntry)directory.getEntry(name);
        }
        catch(Exception fnfe)
        {
            throw new IllegalStateException("Table Stream '" + name
                + "' wasn't found - Either the document is corrupt, or is Word95 (or earlier)");
        }

        // read in the table stream.
        /*_tableStream = new byte[tableProps.getSize()];
        directory.createDocumentInputStream(name).read(_tableStream);*/

        _fib.fillVariableFields(_mainStream, _tableStream);

        // read in the data stream.
        try
        {
            /*DocumentEntry dataProps = (DocumentEntry)directory.getEntry(STREAM_DATA);
            _dataStream = new byte[dataProps.getSize()];
            directory.createDocumentInputStream(STREAM_DATA).read(_dataStream);*/
            _dataStream = cfbFS.getPropertyRawData(STREAM_DATA);
        }
        catch(Exception e)
        {
            _dataStream = new byte[0];
        }

        // Get the cp of the start of text in the main stream
        // The latest spec doc says this is always zero!
        int fcMin = 0;
        //fcMin = _fib.getFcMin()

        // Start to load up our standard structures.
        //_dop = new DocumentProperties(_tableStream, _fib.getFcDop(), _fib.getLcbDop());
        _cft = new ComplexFileTable(_mainStream, _tableStream, _fib.getFcClx(), fcMin);
        TextPieceTable _tpt = _cft.getTextPieceTable();

        // Now load the rest of the properties, which need to be adjusted
        //  for where text really begin
        _cbt = new CHPBinTable(_mainStream, _tableStream, _fib.getFcPlcfbteChpx(), _fib.getLcbPlcfbteChpx(), _tpt);
        _pbt = new PAPBinTable(_mainStream, _tableStream, _dataStream, _fib.getFcPlcfbtePapx(), _fib.getLcbPlcfbtePapx(), _tpt);

        _text = _tpt.getText();

        /*
         * in this mode we preserving PAPX/CHPX structure from file, so text may
         * miss from output, and text order may be corrupted
         */
        /*boolean preserveBinTables = false;
        try
        {
            preserveBinTables = Boolean.parseBoolean(System
                .getProperty(PROPERTY_PRESERVE_BIN_TABLES));
        }
        catch(Exception exc)
        {
            // ignore;
        }

        if (!preserveBinTables)*/
        {
            _cbt.rebuild(_cft);
            _pbt.rebuild(_text, _cft);
        }

        /*
         * Property to disable text rebuilding. In this mode changing the text
         * will lead to unpredictable behavior
         */
        /*boolean preserveTextTable = false;
        try
        {
            preserveTextTable = Boolean.parseBoolean(System
                .getProperty(PROPERTY_PRESERVE_TEXT_TABLE));
        }
        catch(Exception exc)
        {
            // ignore;
        }*/
        /*if (!preserveTextTable)
        {
            _cft = new ComplexFileTable();
            _tpt = _cft.getTextPieceTable();
            final TextPiece textPiece = new SinglentonTextPiece(_text);
            _tpt.add(textPiece);
            _text = textPiece.getStringBuilder();
        }*/

        // Read FSPA and Escher information
        // _fspa = new FSPATable(_tableStream, _fib.getFcPlcspaMom(),
        // _fib.getLcbPlcspaMom(), getTextTable().getTextPieces());
        _fspaHeaders = new FSPATable(_tableStream, _fib, FSPADocumentPart.HEADER);
        _fspaMain = new FSPATable(_tableStream, _fib, FSPADocumentPart.MAIN);

        if (_fib.getFcDggInfo() != 0)
        {
            _escherRecordHolder = new EscherRecordHolder(_tableStream, _fib.getFcDggInfo(),
                _fib.getLcbDggInfo());
        }
        else
        {
            _escherRecordHolder = new EscherRecordHolder();
        }

        // read in the pictures stream
        _pictures = new PicturesTable(this, _dataStream, _mainStream, _fspaMain,
            _escherRecordHolder);
        // And the art shapes stream
        _officeArts = new ShapesTable(_tableStream, _fib);

        // And escher pictures
        _officeDrawingsHeaders = new OfficeDrawingsImpl(_fspaHeaders, _escherRecordHolder,
            _mainStream);
        _officeDrawingsMain = new OfficeDrawingsImpl(_fspaMain, _escherRecordHolder, _mainStream);

        _st = new SectionTable(_mainStream, _tableStream, _fib.getFcPlcfsed(),
            _fib.getLcbPlcfsed(), fcMin, _tpt, _cpSplit);
        _ss = new StyleSheet(_tableStream, _fib.getFcStshf());
        _ft = new FontTable(_tableStream, _fib.getFcSttbfffn(), _fib.getLcbSttbfffn());

        int listOffset = _fib.getFcPlcfLst();
        int lfoOffset = _fib.getFcPlfLfo();
        if (listOffset != 0 && _fib.getLcbPlcfLst() != 0)
        {
            _lt = new ListTables(_tableStream, _fib.getFcPlcfLst(), _fib.getFcPlfLfo());
        }

        /*int sbtOffset = _fib.getFcSttbSavedBy();
        int sbtLength = _fib.getLcbSttbSavedBy();
        if (sbtOffset != 0 && sbtLength != 0)
        {
            _sbt = new SavedByTable(_tableStream, sbtOffset, sbtLength);
        }*/

        /*int rmarkOffset = _fib.getFcSttbfRMark();
        int rmarkLength = _fib.getLcbSttbfRMark();
        if (rmarkOffset != 0 && rmarkLength != 0)
        {
            _rmat = new RevisionMarkAuthorTable(_tableStream, rmarkOffset, rmarkLength);
        }*/

        _bookmarksTables = new BookmarksTables(_tableStream, _fib);
        _bookmarks = new BookmarksImpl(_bookmarksTables);

        //_endnotesTables = new NotesTables(NoteType.ENDNOTE, _tableStream, _fib);
        //_endnotes = new NotesImpl(_endnotesTables);
        //_footnotesTables = new NotesTables(NoteType.FOOTNOTE, _tableStream, _fib);
        //_footnotes = new NotesImpl(_footnotesTables);

        _fieldsTables = new FieldsTables(_tableStream, _fib);
        _fields = new FieldsImpl(_fieldsTables);
        
        //textbox
        txbxBkd = new PlcfTxbxBkd(_tableStream, _fib.getFcPlcfTxbxBkd(), _fib.getLcbPlcfTxbxBkd());
    }

    @ Internal
    public TextPieceTable getTextTable()
    {
        return _cft.getTextPieceTable();
    }

    @ Internal
    @ Override
    public StringBuilder getText()
    {
        return _text;
    }

    /*public DocumentProperties getDocProperties()
    {
        return _dop;
    }*/

    public Range getOverallRange()
    {
        return new Range(0, _text.length(), this);
    }

    /**
     * Returns the range which covers the whole of the document, but excludes
     * any headers and footers.
     */
    public Range getRange()
    {
        // // First up, trigger a full-recalculate
        // // Needed in case of deletes etc
        // getOverallRange();
        //
        // if ( getFileInformationBlock().isFComplex() )
        // {
        // /*
        // * Page 31:
        // *
        // * main document must be found by examining the piece table entries
        // * from the 0th piece table entry from the piece table entry that
        // * describes cp=fib.ccpText.
        // */
        // // TODO: review
        // return new Range( _cpSplit.getMainDocumentStart(),
        // _cpSplit.getMainDocumentEnd(), this );
        // }
        //
        // /*
        // * Page 31:
        // *
        // * "In a non-complex file, this means text of the: main document
        // begins
        // * at fib.fcMin in the file and continues through
        // * fib.fcMin+fib.ccpText."
        // */
        // int bytesStart = getFileInformationBlock().getFcMin();
        //
        // int charsStart = getTextTable().getCharIndex( bytesStart );
        // int charsEnd = charsStart
        // + getFileInformationBlock().getSubdocumentTextStreamLength(
        // SubdocumentType.MAIN );

        // it seems much simpler -- sergey
        return getRange(SubdocumentType.MAIN);
    }

    private Range getRange(SubdocumentType subdocument)
    {
        int startCp = 0;
        for (SubdocumentType previos : SubdocumentType.ORDERED)
        {
            int length = getFileInformationBlock().getSubdocumentTextStreamLength(previos);
            if (subdocument == previos)
            {
                return new Range(startCp, startCp + length, this);
            }
            startCp += length;
        }
        throw new UnsupportedOperationException("Subdocument type not supported: " + subdocument);
    }

    /**
     * Returns the {@link Range} which covers all the Footnotes.
     * 
     * @return the {@link Range} which covers all the Footnotes.
     */
    public Range getFootnoteRange()
    {
        return getRange(SubdocumentType.FOOTNOTE);
    }

    /**
     * Returns the {@link Range} which covers all endnotes.
     * 
     * @return the {@link Range} which covers all endnotes.
     */
    public Range getEndnoteRange()
    {
        return getRange(SubdocumentType.ENDNOTE);
    }

    /**
     * Returns the {@link Range} which covers all annotations.
     * 
     * @return the {@link Range} which covers all annotations.
     */
    public Range getCommentsRange()
    {
        return getRange(SubdocumentType.ANNOTATION);
    }

    /**
     * Returns the {@link Range} which covers all textboxes.
     * 
     * @return the {@link Range} which covers all textboxes.
     */
    public Range getMainTextboxRange()
    {
        return getRange(SubdocumentType.TEXTBOX);
    }

    /**
     * Returns the range which covers all "Header Stories".
     * A header story contains a header, footer, end note
     *  separators and footnote separators.
     */
    public Range getHeaderStoryRange()
    {
        return getRange(SubdocumentType.HEADER);
    }

    /**
     * Returns the character length of a document.
     * @return the character length of a document
     */
    public int characterLength()
    {
        return _text.length();
    }

    /**
     * Gets a reference to the saved -by table, which holds the save history for the document.
     *
     * @return the saved-by table.
     */
    /*@ Internal
    public SavedByTable getSavedByTable()
    {
        return _sbt;
    }*/

    /**
     * Gets a reference to the revision mark author table, which holds the revision mark authors for the document.
     *
     * @return the saved-by table.
     * /
    @ Internal
    public RevisionMarkAuthorTable getRevisionMarkAuthorTable()
    {
        return _rmat;
    }

    /**
     * @return PicturesTable object, that is able to extract images from this document
     */
    public PicturesTable getPicturesTable()
    {
        return _pictures;
    }

    @ Internal
    public EscherRecordHolder getEscherRecordHolder()
    {
        return _escherRecordHolder;
    }

    /**
     * @return ShapesTable object, that is able to extract office are shapes
     *         from this document
     * @deprecated use {@link #getOfficeDrawingsMain()} instead
     */
    @ Deprecated
    @ Internal
    public ShapesTable getShapesTable()
    {
        return _officeArts;
    }

    public OfficeDrawings getOfficeDrawingsHeaders()
    {
        return _officeDrawingsHeaders;
    }

    public OfficeDrawings getOfficeDrawingsMain()
    {
        return _officeDrawingsMain;
    }

    /**
     * @return user-friendly interface to access document bookmarks
     */
    public Bookmarks getBookmarks()
    {
        return _bookmarks;
    }

    /**
     * @return user-friendly interface to access document endnotes
     * /
    public Notes getEndnotes()
    {
        return _endnotes;
    }

    /**
     * @return user-friendly interface to access document footnotes
     * /
    public Notes getFootnotes()
    {
        return _footnotes;
    }

    /**
     * @return FieldsTables object, that is able to extract fields descriptors from this document
     * @deprecated
     */
    @ Deprecated
    @ Internal
    public FieldsTables getFieldsTables()
    {
        return _fieldsTables;
    }

    /**
     * Returns user-friendly interface to access document {@link Field}s
     * 
     * @return user-friendly interface to access document {@link Field}s
     */
    public Fields getFields()
    {
        return _fields;
    }

    @ Internal
    public byte[] getDataStream()
    {
        return _dataStream;
    }

    @ Internal
    public byte[] getTableStream()
    {
        return _tableStream;
    }

    public int registerList(HWPFList list)
    {
        if (_lt == null)
        {
            _lt = new ListTables();
        }
        return _lt.addList(list.getListData(), list.getOverride());
    }

    public void delete(int start, int length)
    {
        Range r = new Range(start, start + length, this);
        r.delete();
    }
    
    public int getTextboxStart(int txbx)
    {
    	return txbxBkd.getCharPosition(txbx);
    }
    
    public int getTextboxEnd(int txbx)
    {
    	return txbxBkd.getCharPosition(txbx + 1);
    }
}
