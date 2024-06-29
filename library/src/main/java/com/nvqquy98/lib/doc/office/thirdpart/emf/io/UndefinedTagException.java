// Copyright 2001, FreeHEP.
package com.nvqquy98.lib.doc.office.thirdpart.emf.io;

import java.io.IOException;

/**
 * Exception for the TaggedOutputStream. Signals that the user tries to write a
 * tag which is not defined at this version or below.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: src/main/java/org/freehep/util/io/UndefinedTagException.java
 *          96b41b903496 2005/11/21 19:50:18 duns $
 */
public class UndefinedTagException extends IOException {

	/**
     * 
     */
	private static final long serialVersionUID = 7504997713135869344L;

	/**
	 * Create an Undefined Tag Exception.
	 */
	public UndefinedTagException() {
		super();
	}

	/**
	 * Create an Undefined Tag Exception.
	 * 
	 * @param msg
	 *            message
	 */
	public UndefinedTagException(String msg) {
		super(msg);
	}

	/**
	 * Create an Undefined Tag Exception.
	 * 
	 * @param code
	 *            undefined tagID
	 */
	public UndefinedTagException(int code) {
		super("Code: (" + code + ")");
	}
}
