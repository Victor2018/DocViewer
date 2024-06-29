// Copyright 2001, FreeHEP.
package com.nvqquy98.lib.doc.office.thirdpart.emf.io;

import java.io.IOException;

/**
 * Exception for the TaggedInputStream. Signals that the inputstream contains
 * more bytes than the stream has read for this tag.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: src/main/java/org/freehep/util/io/IncompleteTagException.java
 *          96b41b903496 2005/11/21 19:50:18 duns $
 */
public class IncompleteTagException extends IOException {

	/**
     * 
     */
	private static final long serialVersionUID = -7808675150856818588L;

	private Tag tag;

	private byte[] rest;

	/**
	 * Creates an Incomplete Tag Exception
	 * 
	 * @param tag
	 *            incomplete tag
	 * @param rest
	 *            unused bytes
	 */
	public IncompleteTagException(Tag tag, byte[] rest) {
		super("Tag " + tag + " contains " + rest.length + " unread bytes");
		this.tag = tag;
		this.rest = rest;
	}

	/**
	 * @return tag
	 */
	public Tag getTag() {
		return tag;
	}

	/**
	 * @return unused bytes
	 */
	public byte[] getBytes() {
		return rest;
	}
}
