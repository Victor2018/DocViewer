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
package com.nvqquy98.lib.doc.office.fc.hwpf.usermodel;

public interface Field
{
	//field type
	public static final byte REF = 0x03;	//Specified in [ECMA-376] part 4, section 2.16.5.58
	public static final byte FTNREF = 0x05;	//This field is identical to NOTEREF specified in [ECMA-376] part 4, section 2.16.5.47.
	public static final byte SET = 0x06;	//Specified in [ECMA-376] part 4, section 2.16.5.64.
	public static final byte IF = 0x07;		//Specified in [ECMA-376] part 4, section 2.16.5.32.
	public static final byte INDEX = 0x08;	//Specified in [ECMA-376] part 4, section 2.16.5.35.
	public static final byte STYLEREF = 0x0A;	//Specified in [ECMA-376] part 4, section 2.16.5.66.
	public static final byte SEQ = 0x0C;	//Specified in [ECMA-376] part 4, section 2.16.5.63.
	public static final byte TOC = 0x0D;	//Specified in [ECMA-376] part 4, section 2.16.5.75.
	public static final byte INFO = 0x0E;	//Specified in [ECMA-376] part 4, section 2.16.5.36.
	public static final byte TITLE = 0x0F;	//Specified in [ECMA-376] part 4, section 2.16.5.73.
	public static final byte SUBJECT = 0x10;	//Specified in [ECMA-376] part 4, section 2.16.5.67.
	public static final byte AUTHOR = 0x11;	//Specified in [ECMA-376] part 4, section 2.16.5.4.
	public static final byte KEYWORDS = 0x12;	//Specified in [ECMA-376] part 4, section 2.16.5.37.
	public static final byte COMMENTS = 0x13;	//Specified in [ECMA-376] part 4, section 2.16.5.14.
	public static final byte LASTSAVEDBY = 0x14;	//Specified in [ECMA-376] part 4, section 2.16.5.38.
	public static final byte CREATEDATE = 0x15;	//Specified in [ECMA-376] part 4, section 2.16.5.16.
	public static final byte SAVEDATE = 0x16; 	//Specified in [ECMA-376] part 4, section 2.16.5.60.	
	public static final byte PRINTDATE = 0x17;	//Specified in [ECMA-376] part 4, section 2.16.5.54.
	public static final byte REVNUM = 0x18;	//Specified in [ECMA-376] part 4, section 2.16.5.59.
	public static final byte EDITTIME = 0x19;	//Specified in [ECMA-376] part 4, section 2.16.5.21.
	public static final byte NUMPAGES = 0x1A;	//Specified in [ECMA-376] part 4, section 2.16.5.49.
	public static final byte NUMWORDS = 0x1B;	//Specified in [ECMA-376] part 4, section 2.16.5.50.
	public static final byte NUMCHARS = 0x1C;	//Specified in [ECMA-376] part 4, section 2.16.5.48.
	public static final byte FILENAME = 0x1D;	//Specified in [ECMA-376] part 4, section 2.16.5.23.
	public static final byte TEMPLATE = 0x1E;	//Specified in [ECMA-376] part 4, section 2.16.5.71.
	public static final byte DATE = 0x1F;	//Specified in [ECMA-376] part 4, section 2.16.5.18.
	public static final byte TIME = 0x20;	//Specified in [ECMA-376] part 4, section 2.16.5.72.
	public static final byte PAGE = 0x21;	//Specified in [ECMA-376] part 4, section 2.16.5.51.
	public static final byte Equals = 0x22;	//Specified in [ECMA-376]part 4, section 2.16.3.3.
	public static final byte QUOTE = 0x23;	//Specified in [ECMA-376] part 4, section 2.16.5.56.
	public static final byte INCLUDE = 0x24;	//This field is identical to INCLUDETEXT specified in [ECMA-376] part 4, section 2.16.5.34.
	public static final byte PAGEREF = 0x25;	//Specified in [ECMA-376] part 4, section 2.16.5.52.
	public static final byte ASK = 0x26;	//Specified in [ECMA-376] part 4, section 2.16.5.3.
	public static final byte FILLIN = 0x27;	//Specified in [ECMA-376] part 4, section 2.16.5.25.
	public static final byte DATA = 0x28;	//Usage:DATA datafile [headerfile] Specifies that this field SHOULD<224> redirect the mail merge data and header files to the ones specified.
	public static final byte NEXT = 0x29;	//Specified in [ECMA-376] part 4, section 2.16.5.45.
	public static final byte NEXTIF = 0x2A;	//Specified in [ECMA-376] part 4, section 2.16.5.46.
	public static final byte SKIPIF = 0x2B;	//Specified in [ECMA-376] part 4, section 2.16.5.65.
	public static final byte MERGEREC = 0x2C;	//Specified in [ECMA-376] part 4, section 2.16.5.43.
	public static final byte DDE = 0x2D;	//Specified in [MS-OE376] part 2, section 1.3.2.1.
	public static final byte DDEAUTO = 0x2E;	//Specified in [MS-OE376] part 2, section 1.3.2.2.
	public static final byte GLOSSARY = 0x2F;	//This field is identical to AUTOTEXT specified in [ECMA-376] part 4, section 2.16.5.8.
	public static final byte PRINT = 0x30;	//Specified in [ECMA-376] part 4, section 2.16.5.53.
	public static final byte EQ = 0x31;	//Specified in [ECMA-376] part 4, section 2.16.5.22.
	public static final byte GOTOBUTTON = 0x32;	//Specified in [ECMA-376] part 4, section 2.16.5.29.
	public static final byte MACROBUTTON = 0x33;	//Specified in [ECMA-376] part 4, section 2.16.5.41.
	public static final byte AUTONUMOUT = 0x34;	//Specified in [ECMA-376] part 4, section 2.16.5.7.
	public static final byte AUTONUMLGL = 0x35;	//Specified in [ECMA-376] part 4, section 2.16.5.6.
	public static final byte AUTONUM = 0x36;	//Specified in [ECMA-376] part 4, section 2.16.5.5.
	public static final byte IMPORT = 0x37;	//Identical to the INCLUDEPICTURE field specified in [ECMA-376] part 4, section 2.16.5.33.
	public static final byte LINK = 0x38;	//Specified in [ECMA-376] part 4, section 2.16.5.39.
	public static final byte SYMBOL = 0x39;	//Specified in [ECMA-376] part 4, section 2.16.5.68.
	public static final byte EMBED = 0x3A;	//Specifies that the field represents an embedded OLE object.
	public static final byte MERGEFIELD = 0x3B;	//Specified in [ECMA-376] part 4, section 2.16.5.42.
	public static final byte USERNAME = 0x3C;	//Specified in [ECMA-376] part 4, section 2.16.5.78.
	public static final byte USERINITIALS = 0x3D;	//Specified in [ECMA-376] part 4, section 2.16.5.77.
	public static final byte USERADDRESS = 0x3E;	//Specified in [ECMA-376] part 4, section 2.16.5.76.
	public static final byte BARCODE = 0x3F;	//Specified in [ECMA-376] part 4, section 2.16.5.10.
	public static final byte DOCVARIABLE = 0x40;	//Specified in [ECMA-376] part 4, section 2.16.5.20.
	public static final byte SECTION = 0x41;	//Specified in [ECMA-376] part 4, section 2.16.5.61.
	public static final byte SECTIONPAGES = 0x42;	//Specified in [ECMA-376] part 4, section 2.16.5.62.
	public static final byte INCLUDEPICTURE = 0x43;	//Specified in [ECMA-376] part 4, section 2.16.5.33.
	public static final byte INCLUDETEXT = 0x44;	//Specified in [ECMA-376] part 4, section 2.16.5.34.
	public static final byte FILESIZE = 0x45;	//Specified in [ECMA-376] part 4, section 2.16.5.24.
	public static final byte FORMTEXT = 0x46;	//Specified in [ECMA-376] part 4, section 2.16.5.28.
	public static final byte FORMCHECKBOX = 0x47;	//Specified in [ECMA-376] part 4, section 2.16.5.26.
	public static final byte NOTEREF = 0x48;	//Specified in [ECMA-376] part 4, section 2.16.5.47.
	public static final byte TOA = 0x49;	//Specified in [ECMA-376] part 4, section 2.16.5.74.
	public static final byte MERGESEQ = 0x4B;	//Specified in [ECMA-376] part 4, section 2.16.5.44.
	public static final byte AUTOTEXT = 0x4F;	//Specified in [ECMA-376] part 4, section 2.16.5.8.
	public static final byte COMPARE = 0x50;	//Specified in [ECMA-376] part 4, section 2.16.5.15.
	public static final byte ADDIN = 0x51;	//Specifies that the field contains data created by an add-in.
	public static final byte FORMDROPDOWN = 0x53;	//Specified in [ECMA-376] part 4, section 2.16.5.27.
	public static final byte ADVANCE = 0x54;	//Specified in [ECMA-376] part 4, section 2.16.5.2.
	public static final byte DOCPROPERTY = 0x55;	//Specified in [ECMA-376] part 4, section 2.16.5.19.
	public static final byte CONTROL = 0x57;	//Specifies that the field represents an OCX control.
	public static final byte HYPERLINK = 0x58;	//Specified in [ECMA-376] part 4, section 2.16.5.31.
	public static final byte AUTOTEXTLIST = 0x59;	//Specified in [ECMA-376] part 4, section 2.16.5.9.
	public static final byte LISTNUM = 0x5A;	//Specified in [ECMA-376] part 4, section 2.16.5.40.
	public static final byte HTMLCONTROL = 0x5B;	//Specifies the field represents an HTML control.
	public static final byte BIDIOUTLINE = 0x5C;	//Specified in [ECMA-376] part 4, section 2.16.5.12.
	public static final byte ADDRESSBLOCK = 0x5D;	//Specified in [ECMA-376] part 4, section 2.16.5.1.
	public static final byte GREETINGLINE = 0x5E;	//Specified in [ECMA-376] part 4, section 2.16.5.30.
	public static final byte SHAPE = 0x5F;	//This field is identical to QUOTE specified in [ECMA-376] part 4, section 2.16.5.56.
	
    Range firstSubrange( Range parent );

    /**
     * @return character position of first character after field (i.e.
     *         {@link #getMarkEndOffset()} + 1)
     */
    int getFieldEndOffset();

    /**
     * @return character position of first character in field (i.e.
     *         {@link #getFieldStartOffset()})
     */
    int getFieldStartOffset();

    CharacterRun getMarkEndCharacterRun( Range parent );

    /**
     * @return character position of end field mark
     */
    int getMarkEndOffset();

    CharacterRun getMarkSeparatorCharacterRun( Range parent );

    /**
     * @return character position of separator field mark (if present,
     *         {@link NullPointerException} otherwise)
     */
    int getMarkSeparatorOffset();

    CharacterRun getMarkStartCharacterRun( Range parent );

    /**
     * @return character position of start field mark
     */
    int getMarkStartOffset();

    int getType();

    boolean hasSeparator();

    boolean isHasSep();

    boolean isLocked();

    boolean isNested();

    boolean isPrivateResult();

    boolean isResultDirty();

    boolean isResultEdited();

    boolean isZombieEmbed();

    Range secondSubrange( Range parent );
}
