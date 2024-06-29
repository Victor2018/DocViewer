// Copyright 2001, FreeHEP.
package com.nvqquy98.lib.doc.office.thirdpart.emf.io;

/**
 * Keeps the tagID and Length of a specific tag. To be used in the InputStream
 * to return the tagID and Length, and in the OutputStream to write them.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: src/main/java/org/freehep/util/io/TagHeader.java 96b41b903496
 *          2005/11/21 19:50:18 duns $
 */
public class TagHeader {

	int tagID;

	long length;

	/**
	 * Creates a tag header
	 * 
	 * @param tagID
	 *            id of tag
	 * @param length
	 *            length of the tag, including the header
	 */
	public TagHeader(int tagID, long length) {
		this.tagID = tagID;
		this.length = length;
	}

	/**
	 * Sets the tag id
	 * 
	 * @param tagID
	 *            new tag id
	 */
	public void setTag(int tagID) {
		this.tagID = tagID;
	}

	/**
	 * @return tagID
	 */
	public int getTag() {
		return tagID;
	}

	/**
	 * Sets the length of the tag
	 * 
	 * @param length
	 *            new length
	 */
	public void setLength(long length) {
		this.length = length;
	}

	/**
	 * @return tag length
	 */
	public long getLength() {
		return length;
	}
}
