// Copyright 2001, FreeHEP.
package com.nvqquy98.lib.doc.office.thirdpart.emf.io;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to keep registered Tags, which should be used by the
 * TaggedIn/OutputStream. A set of recognized Tags can be added to this class. A
 * concrete implementation of this stream should install all allowed tags.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: src/main/java/org/freehep/util/io/TagSet.java 96b41b903496
 *          2005/11/21 19:50:18 duns $
 */
public class TagSet {

	/**
	 * This holds the individual tags.
	 */
	protected Map tags;

	/**
	 * The default tag handler.
	 */
	protected Tag defaultTag;

	/**
	 * Creates a Tag Set.
	 */
	public TagSet() {
		// Initialize the tag classes.
		defaultTag = new UndefinedTag();
		tags = new HashMap();
	}

	/**
	 * Add a new tag to this set. If the tagID returned is the DEFAULT_TAG, then
	 * the default handler is set to the given handler.
	 * 
	 * @param tag
	 *            tag to be added to set
	 */
	public void addTag(Tag tag) {
		System.out.println("addTag==========");
		int id = tag.getTag();
		if (id != Tag.DEFAULT_TAG) {
			tags.put(new Integer(id), tag);
		} else {
			defaultTag = tag;
		}
	}

	/**
	 * Find tag for tagID.
	 * 
	 * @param tagID
	 *            tagID to find
	 * @return correspoding tag or UndefinedTag if tagID is not found.
	 */
	public Tag get(int tagID) {
		Tag tag = (Tag) tags.get(new Integer(tagID));
		if (tag == null) {
			tag = defaultTag;
		}
		return tag;
	}

	/**
	 * Finds out if Tag for TagID exists.
	 * 
	 * @param tagID
	 *            tagID to find
	 * @return true if corresponding Tag for TagID exists
	 */
	public boolean exists(int tagID) {
		return (tags.get(new Integer(tagID)) != null);
	}
}
