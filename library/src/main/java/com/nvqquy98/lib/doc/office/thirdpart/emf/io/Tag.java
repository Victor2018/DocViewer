// Copyright 2001, FreeHEP.
package com.nvqquy98.lib.doc.office.thirdpart.emf.io;

import java.io.IOException;

/**
 * Generic Tag to be used by TaggedIn/OutputStreams. The tag contains an ID,
 * name and a version. Concrete subclasses should implement the IO Read and
 * Write methods.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: src/main/java/org/freehep/util/io/Tag.java 96b41b903496
 *          2005/11/21 19:50:18 duns $
 */
public abstract class Tag {

	private int tagID;

	private String name;

	private int version;

	/**
	 * This is the tagID for the default tag handler.
	 */
	final public static int DEFAULT_TAG = -1;

	protected Tag(int tagID, int version) {
		this.tagID = tagID;
		this.version = version;
		this.name = null;
	}

	/**
	 * Get the tag number.
	 * 
	 * @return tagID
	 */
	public int getTag() {
		return tagID;
	}

	/**
	 * Get the version number.
	 * 
	 * @return version number
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * Get the tag name.
	 * 
	 * @return tag name
	 */
	public String getName() {
		if (name == null) {
			name = getClass().getName();
			int dot = name.lastIndexOf(".");
			name = (dot >= 0) ? name.substring(dot + 1) : name;
		}
		return name;
	}

	/**
	 * This returns the type of block
	 * 
	 * @return tag type
	 */
	public int getTagType() {
		return 0;
	}

	/**
	 * This reads the information from the given input and returns a new Tag
	 * 
	 * @param tagID
	 *            id of the tag to read
	 * @param input
	 *            stream to read from
	 * @param len
	 *            length to read
	 * @return read Tag
	 * @throws IOException
	 *             if read fails
	 */
	public abstract Tag read(int tagID, TaggedInputStream input, int len)
			throws IOException;

	public abstract String toString();
}
